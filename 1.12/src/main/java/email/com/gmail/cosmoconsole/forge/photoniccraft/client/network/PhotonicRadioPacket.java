package email.com.gmail.cosmoconsole.forge.photoniccraft.client.network;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.PhotonicClientProxy;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicReflectionNames;
import email.com.gmail.cosmoconsole.forge.photoniccraft.server.network.PhotonicRadioResponsePacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import paulscode.sound.SoundSystem;

/**
 * A packet of audio data sent by the server to the client for playback. This is
 * the crux of how the Pocket Radio works.
 */
public class PhotonicRadioPacket implements IMessage {
	public static class Handler implements IMessageHandler<PhotonicRadioPacket, IMessage> {
		@Override
		public IMessage onMessage(PhotonicRadioPacket message, MessageContext ctx) {
			if (ModPhotonicCraft.sourceDataLine == null)
				return null;
			if (!PhotonicRadio.radioPlaying)
				return null;
			double strength = message.strength;
			if (strength < 0) {
				PhotonicRadio.shouldNotBePlaying();
				PhotonicRadio.stopPlayingRadio();
				PhotonicClientProxy.restartSourceDataLine();
				return null;
			}
			if (strength > 1.0)
				strength = 1.0;
			byte[] audio = message.audio;
			if (audio.length != PACKET_SIZE) {
				return null;
			}
			byte[] noise = new byte[PACKET_SIZE];
			byte[] finalaudio = new byte[PACKET_SIZE * 2];

			long now = System.currentTimeMillis();
			if ((now - ModPhotonicCraft.lastReceive) > 1500L) {
				PhotonicClientProxy.restartSourceDataLine();
				return null;
			}

			float scv = 1.0F;
			try {
				scv = (Minecraft.getMinecraft().gameSettings.getSoundLevel(ModPhotonicCraft.SC_RADIO));
			} catch (Exception ex) {
			}
			double volumeToPlay = ((SoundSystem) ObfuscationReflectionHelper.getPrivateValue(SoundManager.class,
					(SoundManager) ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class,
							Minecraft.getMinecraft().getSoundHandler(), PhotonicReflectionNames.SoundHandler_sndManager),
					PhotonicReflectionNames.SoundManager_sndSystem)).getMasterVolume() * scv;
			if (volumeToPlay <= 0)
				return null;
			PhotonicClientProxy.setVolume(volumeToPlay);
			volumeToPlay = 1.0;

			double istrength = 1 - strength;
			PhotonicUtils.silentNoiseGenerate(noise);
			double sampl;
			short tmp;
			for (int i = 0; i < PACKET_SIZE; ++i) {
				sampl = scaleVolume(audio[i], noise[i], strength, volumeToPlay);
				tmp = (short) (sampl * 32750);
				finalaudio[2 * i + 0] = (byte) (tmp & 0xFF);
				finalaudio[2 * i + 1] = (byte) ((tmp >> 8) & 0xFF);
			}
			long start = System.currentTimeMillis();
			if (lastFrame >= 0) {
				framesInBuffer += PACKET_SIZE;
				long thisFrame = ModPhotonicCraft.sourceDataLine.getLongFramePosition();
				framesInBuffer -= thisFrame - lastFrame;
				lastFrame = thisFrame;
				if (framesInBuffer >= 10000) {
					PhotonicUtils.debugMessage("[radio client] buffer overrun!");
					PhotonicClientProxy.restartSourceDataLine();
					framesInBuffer = 0;
				}
			} else {
				lastFrame = ModPhotonicCraft.sourceDataLine.getLongFramePosition();
			}
			int ping = Minecraft.getMinecraft().player.connection
					.getPlayerInfo(Minecraft.getMinecraft().player.getGameProfile().getId()).getResponseTime();
			if (ping > 140 && ping != lastPing && (now - lastSkipTry) > (3000 + Math.random() * 4000)) {
				PhotonicUtils.debugMessage("[radio client] ping compensation!");
				ModPhotonicCraft.network
						.sendToServer(new PhotonicRadioResponsePacket(PhotonicRadioResponsePacket.Type.SKIP_NEXT,
								Minecraft.getMinecraft().player.getGameProfile().getId()));
				lastSkipTry = now;
				lastPing = ping;
			}
			ModPhotonicCraft.sourceDataLine.write(finalaudio, 0, 2 * PACKET_SIZE);
			return null;
		}
	}

	public static final int PACKET_SPLIT = 5;
	public static final int PACKET_SIZE = 2205 / PACKET_SPLIT;
	public static long framesInBuffer = 0;
	public static long lastFrame = -1;
	static long lastSkipTry = 0;
	static long lastPing = 0;

	private static byte qualityRuin(byte val, double q) {
		if (q == 0.0)
			return 0;
		if (q >= 1.0)
			return val;
		if (q >= 0.9671682101338347) {
			return scaleDown(((val & 0xFE) | ((val & 0x80) >> 7)), (1 + q) * .5);
		}
		if (q >= 0.9306048591020996) {
			return scaleDown(((val & 0xFC) | ((val & 0xC0) >> 6)), (1 + q) * .5);
		}
		if (q >= 0.8891397050194614) {
			return scaleDown(((val & 0xF8) | ((val & 0xE0) >> 5)), q);
		}
		if (q >= 0.7937005259840997) {
			return scaleDown(((val & 0xF0) | ((val & 0xF0) >> 4)), q);
		}
		if (q >= 0.5590169943749473) {
			return scaleDown(((val & 0xE0) | ((val & 0xE0) >> 3) | ((val & 0xE0) >> 6)), q * q);
		}
		if (q >= 0.30981389406680654) {
			return scaleDown(((val & 0xC0) | (0b00010101 * ((val & 0xC0) >> 6))), q * q);
		}
		return scaleDown(((val & 0x80) | (((val & 0x80) != 0) ? 0x7F : 0x00)), q * q * q);
	}

	private static byte scaleDown(int b, double s) {
		return (byte) (((b - 128) * s) + 128);
	}

	private static double scaleVolume(byte d, byte n, double s, double v) {
		return v * (toDS(qualityRuin(d, s)) * s + toDS(n) * (1 - s));
	}

	public static double toDS(byte n) {
		return (unsign(n) - 127.5) / 128;
	}

	public static double unsign(byte b) {
		if (b < 0)
			return b + 256;
		return b;
	}

	public byte[] audio;
	private int length;
	private int start;
	private int end;

	public double strength;

	public PhotonicRadioPacket() {
		this.strength = 0;
		this.length = 0;
		this.audio = new byte[0];
		this.start = this.end = 0;
	}

	public PhotonicRadioPacket(double strength, byte[] audio, int start, int end) {
		this.strength = strength;
		this.length = end - start;
		this.start = start;
		this.end = end;
		this.audio = audio;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		strength = buf.readDouble();
		length = buf.readShort();
		start = 0;
		end = length;
		this.audio = new byte[length];
		buf.readBytes(this.audio);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(strength);
		buf.writeShort(length);
		buf.writeBytes(audio, start, end - start);
	}
}
