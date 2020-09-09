package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.PhotonicClientProxy;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicRadioPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Contains functions used for the PhotonicCraft radio implementation.
 */
public class PhotonicRadio {
	public static final int MAX_CHANNEL = 1073741024;
	public static final String channel64 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";
	public static final String RADIO_TIMER_EVENT = "photoniccraft_radio";

	public static ConcurrentHashMap<UUID, Long> radioPlayers;
	public static ConcurrentHashMap<UUID, Integer> radioChannels;
	public static ConcurrentHashMap<Integer, UUID> chOwners;
	public static ConcurrentHashMap<Integer, Long> lastSend;
	public static ConcurrentHashMap<Integer, byte[]> dataSentLastTick;
	public static HashMap<Integer, byte[]> sendCache;
	public static HashMap<Integer, byte[]> recvData;
	public static HashMap<Integer, Integer> sendPower;
	public static HashMap<Integer, Integer> recvPower;
	public static HashMap<Integer, Integer> lastRecvPower;
	public static HashMap<Integer, PhotonicLocation> sendLoc;
	public static HashMap<Integer, PhotonicLocation> recvLoc;
	public static HashMap<Integer, PhotonicLocation> lastRecvLoc;
	public static HashMap<Integer, Integer> noiseTimeout;
	public static ConcurrentHashMap<UUID, Integer> ownedChannel;
	public static ConcurrentHashMap<UUID, Integer> sentChannels;
	public static Set<UUID> skipNext;
	public static ArrayList<UUID> radioHandled;
	public static Timer serverTimer;
	public static Timer serverTimer2;
	public static boolean serverInited = false;
	public static AtomicBoolean midSend = new AtomicBoolean(false);
	public static AtomicBoolean cannotModify = new AtomicBoolean(false);
	public static AtomicBoolean radioServerRunning = new AtomicBoolean(false);
	public static AtomicBoolean flipState = new AtomicBoolean(false);
	public static AtomicLong sendCounter = new AtomicLong(0);
	public static AtomicLong sendNano = new AtomicLong(0);
	private static ConcurrentLinkedQueue<RadioEventReceiver> computerQueue = new ConcurrentLinkedQueue<>();
	public static Lock sendLock;
	public static Lock playerAccessLock;

	@SideOnly(Side.CLIENT)
	public static Timer radioTimer;
	@SideOnly(Side.CLIENT)
	public static TimerTask radioTask;
	@SideOnly(Side.CLIENT)
	public static boolean radioPlaying;
	@SideOnly(Side.CLIENT)
	public static long lastPing;

	public static long last_send;

	public static boolean allowPlaying(EntityPlayerMP p_77663_3_, long currentTimeMillis, int channel) {
		if (radioPlayers == null || radioChannels == null || p_77663_3_ == null)
			return false;
		if (radioPlayers.contains(p_77663_3_.getUniqueID())) {
			if ((currentTimeMillis - radioPlayers.get(p_77663_3_.getUniqueID())) < 70L) {
				return false;
			}
		}
		playerAccessLock.lock();
		radioPlayers.put(p_77663_3_.getUniqueID(), currentTimeMillis);
		radioChannels.put(p_77663_3_.getUniqueID(), channel);
		radioHandled.add(p_77663_3_.getUniqueID());
		playerAccessLock.unlock();
		return true;
	}

	public static double calculateAmplitude(double ds, Integer rf) {
		if (ds >= (9 * rf * rf))
			return 0.0;
		if (ds <= (rf * rf))
			return 1.0;
		double d = Math.sqrt(ds);
		double s = 1 - ((d - rf) / (2 * rf));
		return s;
	}

	public static String channelIDToName(int ch) {
		if (ch < 0 || ch >= MAX_CHANNEL)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			sb.append(channel64.charAt(ch & 0x3F));
			ch >>= 6;
		}
		return sb.reverse().toString();
	}

	public static int channelNameToID(String str) {
		if (str.length() < 5) {
			return channelNameToID(String.format("%0" + (5 - str.length()) + "d", 0) + str);
		}
		if (str.length() > 5)
			return -1;
		int result = 0;
		int m = 16777216;
		for (char c : str.toCharArray()) {
			int p = channel64.indexOf(c);
			if (p < 0)
				return -1;
			result += p * m;
			m >>= 6;
		}
		return result;
	}

	public static int getActiveRadioPlayers() {
		playerAccessLock.lock();
		try {
			if (radioPlayers == null)
				startRadioServer();
			if (radioPlayers == null)
				return 0;
			return radioPlayers.size();
		} finally {
			playerAccessLock.unlock();
		}
	}

	private synchronized static UUID getChannelOwner(int channel, UUID defaultVal) {
		if (chOwners == null)
			return defaultVal;
		sendLock.lock();
		try {
			if (!chOwners.containsKey(channel) || (chOwners.get(channel) == null))
				return defaultVal;
			if (lastSend.containsKey(channel)) {
				long now = System.currentTimeMillis();
				if ((now - (lastSend.get(channel))) >= 10000L) {
					dataSentLastTick.remove(channel);
					return defaultVal;
				}
			}
			if (ownedChannel.get(chOwners.get(channel)) != channel)
				return chOwners.get(channel);
			return chOwners.get(channel);
		} finally {
			sendLock.unlock();
		}
	}

	public synchronized static boolean radioAuthorizedToSend(UUID senderId, int channel) {
		return senderId.equals(getChannelOwner(channel, senderId));
	}

	public synchronized static boolean radioSendData(UUID senderId, int channel, byte[] data, PhotonicLocation loc,
			int power) {
		if (!radioAuthorizedToSend(senderId, channel)) {
			return false;
		}
		sendLock.lock();
		try {
			int sch = sentChannels.getOrDefault(senderId, 0);
			if (sch >= ModPhotonicCraft.maximumRadioChannelBroadcast) {
				return false;
			}
			sendCache.put(channel, data);
			sendPower.put(channel, power);
			sendLoc.put(channel, loc);
			chOwners.put(channel, senderId);
			ownedChannel.put(senderId, channel);
			lastSend.put(channel, System.currentTimeMillis());
			sentChannels.put(senderId, sch + 1);
		} finally {
			sendLock.unlock();
		}
		return true;
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	public static void sendEventWhenSend(final int channel, final dan200.computercraft.api.peripheral.IComputerAccess computer) {
		computerQueue.add(new RadioEventReceiver(channel, computer));
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	public static void sendEventWhenSend(int channel, li.cil.oc.api.machine.Context context) {
		computerQueue.add(new RadioEventReceiver(channel, context));
	}

	public static void serverSideDeinit() {
		if (!serverInited)
			return;
		last_send = 0L;
		serverTimer.cancel();
		serverTimer2.cancel();
		radioServerRunning.set(false);
		dataSentLastTick.clear();
		serverInited = false;
	}

	public static void serverSideInit() {
		if (serverInited)
			return;
		last_send = 0L;
		startRadioServer();
		serverInited = true;
	}

	@SideOnly(Side.CLIENT)
	public static boolean shouldBePlaying() {
		return (System.currentTimeMillis() - lastPing) < 1500L;
	}

	@SideOnly(Side.CLIENT)
	public static void shouldNotBePlaying() {
		lastPing = 0L;
	}

	public static boolean shouldSend() {
		long now = System.currentTimeMillis();
		if ((now - last_send) >= 200L) {
			last_send = now;
			return true;
		}
		return false;
	}

	public static void skipNextRadioPacket(UUID player) {
		skipNext.add(player);
	}

	@SideOnly(Side.CLIENT)
	public static void startPlayingRadio() {
		radioPlaying = true;
		if (radioTask != null)
			radioTask.cancel();
		radioTask = new TimerTask() {
			@Override
			public void run() {
				try {
					startPlayingRadioNow();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		radioTimer.schedule(radioTask, 300L);
	}

	@SideOnly(Side.CLIENT)
	public static void startPlayingRadioNow() {
		long start = System.currentTimeMillis();
		if (ModPhotonicCraft.sourceDataLine == null)
			PhotonicClientProxy.setupRadio();
		ModPhotonicCraft.sourceDataLine.start();
	}

	public static void startRadioServer() {
		serverTimer = new Timer();
		serverTimer2 = new Timer();
		radioPlayers = new ConcurrentHashMap<UUID, Long>();
		radioChannels = new ConcurrentHashMap<UUID, Integer>();
		radioHandled = new ArrayList<UUID>();
		chOwners = new ConcurrentHashMap<Integer, UUID>();
		sendCache = new HashMap<Integer, byte[]>();
		sendPower = new HashMap<Integer, Integer>();
		sendLoc = new HashMap<Integer, PhotonicLocation>();
		recvData = new HashMap<Integer, byte[]>();
		recvPower = new HashMap<Integer, Integer>();
		recvLoc = new HashMap<Integer, PhotonicLocation>();
		lastRecvPower = new HashMap<Integer, Integer>();
		lastRecvLoc = new HashMap<Integer, PhotonicLocation>();
		noiseTimeout = new HashMap<Integer, Integer>();
		dataSentLastTick = new ConcurrentHashMap<Integer, byte[]>();
		lastSend = new ConcurrentHashMap<Integer, Long>();
		ownedChannel = new ConcurrentHashMap<UUID, Integer>();
		sentChannels = new ConcurrentHashMap<UUID, Integer>();
		skipNext = ConcurrentHashMap.newKeySet();
		if (!(Compat.computercraft || Compat.opencomputers)) {
			PhotonicUtils.debugMessage("Radio server not started: No ComputerCraft or OpenComputers found, nothing to operate with");
			return;		
		}
		PhotonicUtils.debugMessage("Radio server started");
		serverTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (midSend.get())
					return;
				midSend.set(true);
				try {
					MinecraftServer srvr = FMLCommonHandler.instance().getMinecraftServerInstance();
					radioHandled.clear();
					recvData.clear();
					recvPower.clear();
					recvLoc.clear();
					cannotModify.set(true);
					sendLock.lock();
					try {
						dataSentLastTick.clear();
						for (int channel : sendCache.keySet()) {
							recvData.put(channel, sendCache.get(channel));
							dataSentLastTick.put(channel, sendCache.get(channel));
							recvPower.put(channel, sendPower.get(channel));
							recvLoc.put(channel, sendLoc.get(channel));
						}
						sendCache.clear();
						sendPower.clear();
						sendLoc.clear();
						sentChannels.clear();
					} finally {
						sendLock.unlock();
					}
					cannotModify.set(false);
					while (!computerQueue.isEmpty())
						computerQueue.poll().trigger();
					sendCounter.incrementAndGet();
					sendNano.set(System.nanoTime());
					long now = System.currentTimeMillis();
					byte[] b = new byte[2205];
					Arrays.fill(b, (byte) 0);
					ArrayList<UUID> removeKeys = new ArrayList<UUID>();
					byte[] d = new byte[2205];
					int m = 0;
					for (UUID u : radioPlayers.keySet()) {
						if (m >= ModPhotonicCraft.maximumRadio) {
							break;
						}
						if (skipNext.contains(u)) {
							skipNext.remove(u);
							continue;
						}
						if ((now - radioPlayers.get(u)) >= 1000L) {
							removeKeys.add(u);
						} else {
							int ch = radioChannels.get(u);
							double ampl = 0.0;
							EntityPlayer plr = PhotonicUtils.getPlayerFromUUID(srvr, u);
							/*if (plr instanceof EntityPlayerMP) {
								EntityPlayerMP p = (EntityPlayerMP) plr;
								if (recvData.containsKey(ch)) {
									d = recvData.get(ch);
									ampl = calculateAmplitude(
											recvLoc.get(ch).distanceSq(PhotonicLocation.fromPlayer(p)),
											recvPower.get(ch));
								} else
									Arrays.fill(d, (byte) 0);
								byte[] ba = ampl <= 0.0 ? b : PhotonicUtils.cloneByteArray(d);
								for (int i = 0; i < PhotonicRadioPacket.PACKET_SPLIT; ++i) {
									ModPhotonicCraft.network.sendTo(
											new PhotonicRadioPacket(ampl, ba, i * PhotonicRadioPacket.PACKET_SIZE,
													(i + 1) * PhotonicRadioPacket.PACKET_SIZE),
											p);
								}
							} else */
							if (plr != null) {
								if (recvData.containsKey(ch)) {
									d = recvData.get(ch);
									ampl = calculateAmplitude(
											recvLoc.get(ch).distanceSq(PhotonicLocation.fromPlayer(plr)),
											recvPower.get(ch));
									noiseTimeout.put(ch, 4);
									lastRecvLoc.put(ch, recvLoc.get(ch));
									lastRecvPower.put(ch, recvPower.get(ch));
								} else if (noiseTimeout.containsKey(ch)) {
									int i = noiseTimeout.get(ch);
									Arrays.fill(d, (byte) 127);
									ampl = calculateAmplitude(
											lastRecvLoc.get(ch).distanceSq(PhotonicLocation.fromPlayer(plr)),
											lastRecvPower.get(ch));
									--i;
									if (i > 0) 
										noiseTimeout.put(ch, i);
									else
										noiseTimeout.remove(ch);
								} else
									Arrays.fill(d, (byte) 0);
								byte[] ba = ampl <= 0.0 ? b : PhotonicUtils.cloneByteArray(d);
								for (int i = 0; i < PhotonicRadioPacket.PACKET_SPLIT; ++i) {
									PhotonicUtils.sendPacketToPlayer(ModPhotonicCraft.network, plr,
											new PhotonicRadioPacket(ampl, ba, i * PhotonicRadioPacket.PACKET_SIZE,
													(i + 1) * PhotonicRadioPacket.PACKET_SIZE));
								}
							}
						}
					}
					playerAccessLock.lock();
					for (UUID u : removeKeys)
						radioPlayers.remove(u);
					playerAccessLock.unlock();
				} catch (Exception ex) {
					PhotonicUtils.debugMessage("Radio server fatal error");
					ex.printStackTrace();
				} finally {
					flipState.set(!flipState.get());
					cannotModify.set(false);
					midSend.set(false);
				}
			}
		}, 10L, 200L);
	}
	
	public static double estimatedTimeUntilNextSend() {
		return ((sendNano.get() + 200000000L) - System.nanoTime()) * 0.000000001;
	}

	@SideOnly(Side.CLIENT)
	public static void stillPlaying() {
		lastPing = System.currentTimeMillis();
	}

	@SideOnly(Side.CLIENT)
	public static void stopPlayingRadio() {
		radioPlaying = false;
		if (radioTask != null)
			radioTask.cancel();
		radioTask = null;
		if (ModPhotonicCraft.sourceDataLine != null) {
			ModPhotonicCraft.sourceDataLine.stop();
			ModPhotonicCraft.sourceDataLine.close();
			PhotonicRadioPacket.framesInBuffer = 0;
			PhotonicRadioPacket.lastFrame = -1;
			PhotonicClientProxy.setupRadio();
		}
	}

	public static boolean tryBlockUntilSafeToModify() {
		sendLock.lock();
		sendLock.unlock();
		return true;
	}
	
	private PhotonicRadio() {
	}
}
