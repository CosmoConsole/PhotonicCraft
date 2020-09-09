package email.com.gmail.cosmoconsole.forge.photoniccraft.integration.computercraft;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

import javax.annotation.Nonnull;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityRadioTransmitter;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicLocation;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

/**
 * The ComputerCraft IPeripheral implementation for the Radio Transceiver
 * peripheral.
 */
@Optional.InterfaceList({@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = Compat.MODID_COMPUTERCRAFT)})
public class PeripheralRadioTransceiver implements dan200.computercraft.api.peripheral.IPeripheral {
	private static final Object[] emptyArray = new Object[0];
	PhotonicLocation loc;
	int power = 20;

	public PeripheralRadioTransceiver(@Nonnull final World world, @Nonnull final BlockPos bp) {
		this.loc = PhotonicLocation.fromBlock(world, bp.getX(), bp.getY(), bp.getZ());
		this.power = 20;
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public void attach(dan200.computercraft.api.peripheral.IComputerAccess computer) {
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public Object[] callMethod(dan200.computercraft.api.peripheral.IComputerAccess computer,
			dan200.computercraft.api.lua.ILuaContext context, int method, Object[] arguments)
			throws dan200.computercraft.api.lua.LuaException, InterruptedException {
		if (this.getMethodNames()[method].equalsIgnoreCase("canSendTo")) {
			if (arguments.length < 1)
				throw new dan200.computercraft.api.lua.LuaException("missing channel argument");
			int channel = 0;
			try {
				channel = (int) (double) (Double) arguments[0];
			} catch (Exception ex) {
				try {
					channel = PhotonicRadio.channelNameToID((String) arguments[0]);
					if (channel < 0)
						throw new IllegalArgumentException();
				} catch (Exception ex2) {
					throw new dan200.computercraft.api.lua.LuaException("invalid channel argument");
				}
			}
			if (channel < 0 || channel >= PhotonicRadio.MAX_CHANNEL)
				throw new dan200.computercraft.api.lua.LuaException("invalid channel argument");
			return new Object[] { PhotonicRadio.radioAuthorizedToSend(this.loc.getUniqueId(), channel) };
		} else if (this.getMethodNames()[method].equalsIgnoreCase("getMaxPower")) {
			return new Object[] { this.getTileEntity().getTrueEnergyStored() };
		} else if (this.getMethodNames()[method].equalsIgnoreCase("getPower")) {
			return new Object[] { this.power };
		} else if (this.getMethodNames()[method].equalsIgnoreCase("setPower")) {
			if (arguments.length < 1)
				throw new dan200.computercraft.api.lua.LuaException("missing power argument");
			int channel = 0;
			try {
				channel = (int) (double) (Double) arguments[0];
			} catch (Exception ex) {
				throw new dan200.computercraft.api.lua.LuaException("invalid power argument");
			}
			this.getTileEntity();
			if (power < 0 || power > TileEntityRadioTransmitter.MAX_RF_CAPACITY)
				throw new dan200.computercraft.api.lua.LuaException("invalid power argument");
			this.power = channel;
		} else if (this.getMethodNames()[method].equalsIgnoreCase("transmit")) {
			// transmit(channel, data)
			if (arguments.length < 1)
				throw new dan200.computercraft.api.lua.LuaException("missing channel and data argument");
			if (arguments.length < 2)
				throw new dan200.computercraft.api.lua.LuaException("missing data argument");
			int channel = 0;
			try {
				channel = (int) (double) (Double) arguments[0];
			} catch (Exception ex) {
				try {
					channel = PhotonicRadio.channelNameToID((String) arguments[0]);
					if (channel < 0)
						throw new IllegalArgumentException();
				} catch (Exception ex2) {
					throw new dan200.computercraft.api.lua.LuaException("invalid channel argument");
				}
			}
			if (power < 0 || power >= PhotonicRadio.MAX_CHANNEL)
				throw new dan200.computercraft.api.lua.LuaException("invalid channel argument");
			int pos = 1;
			if (arguments.length > 2) {
				try {
					pos = (int) (double) (Double) arguments[2];
				} catch (Exception ex) {
					throw new dan200.computercraft.api.lua.LuaException("invalid position argument");
				}
			}
			if (pos < 1)
				throw new dan200.computercraft.api.lua.LuaException("invalid position argument");
			TileEntityRadioTransmitter t = this.getTileEntity();
			if (t.getTrueEnergyStored() < power)
				throw new dan200.computercraft.api.lua.LuaException("not enough RF");
			t.deduceEnergy(power);
			PhotonicRadio.serverSideInit();
			byte[] binary = new byte[2205];
			Arrays.fill(binary, (byte) 128);
			int bytes = 0;
			try {
				if (arguments[1] instanceof String) {
					String data = (String) arguments[1];
					for (int i = 0; i < Math.min(2205, data.length()); ++i) {
						binary[i] = (byte) (data.charAt(i) & 255);
					}
				} else {
					HashMap<Object, Object> data = (HashMap<Object, Object>) arguments[1];
					int cached = 0;
					for (double index = pos; index < pos + 2205; index += 1) {
						if (!data.containsKey(index))
							break;
						binary[bytes++] = (byte) (((Double) data.get(index)).doubleValue());
					}
				}
			} catch (Exception ex) {
				throw new dan200.computercraft.api.lua.LuaException("invalid data argument");
			}
			if (PhotonicRadio.sendCache.containsKey(channel)) {
				context.pullEvent(PhotonicRadio.RADIO_TIMER_EVENT);
			}
			if (!PhotonicRadio.radioSendData(this.loc.getUniqueId(), channel, binary, this.loc, this.power))
				throw new dan200.computercraft.api.lua.LuaException("unauthorized to send data to this channel");
			PhotonicRadio.sendEventWhenSend(channel, computer);
		} else if (this.getMethodNames()[method].equalsIgnoreCase("transmitFile")) {
			// transmitFile(channel, filename)
			if (arguments.length < 1)
				throw new dan200.computercraft.api.lua.LuaException("missing channel and file argument");
			if (arguments.length < 2)
				throw new dan200.computercraft.api.lua.LuaException("missing file argument");
			int channel = 0;
			try {
				channel = (int) (double) (Double) arguments[0];
			} catch (Exception ex) {
				try {
					channel = PhotonicRadio.channelNameToID((String) arguments[0]);
					if (channel < 0)
						throw new IllegalArgumentException();
				} catch (Exception ex2) {
					throw new dan200.computercraft.api.lua.LuaException("invalid channel argument");
				}
			}
			if (channel < 0 || channel >= PhotonicRadio.MAX_CHANNEL)
				throw new dan200.computercraft.api.lua.LuaException("invalid channel argument");
			if (!(arguments[1] instanceof String))
				throw new dan200.computercraft.api.lua.LuaException("invalid file argument");
			Path bp = PhotonicPeripheralProvider.resolveComputerSavePath(computer.getID());
			if (bp == null) {
				throw new dan200.computercraft.api.lua.LuaException(
						"unsupported computer. if on ComputerCraft, please report to PhotonicCraft author");
			}
			try {
				bp.toRealPath();
			} catch (IOException e) {
				throw new dan200.computercraft.api.lua.LuaException(
						"unsupported computer. if on ComputerCraft, please report to PhotonicCraft author");
			}
			Path fp;
			try {
				fp = bp.resolve((String) arguments[1]);
			} catch (InvalidPathException ex) {
				throw new dan200.computercraft.api.lua.LuaException("invalid file argument or file not found");
			}
			if (fp == null) {
				throw new dan200.computercraft.api.lua.LuaException("file not found");
			}
			try {
				fp.toRealPath();
			} catch (IOException e) {
				throw new dan200.computercraft.api.lua.LuaException("file not found");
			}
			// possible exploit?
			if (!PhotonicUtils.doesPathHaveAncestor(fp, bp))
				throw new dan200.computercraft.api.lua.LuaException("invalid file argument");
			TileEntityRadioTransmitter t = this.getTileEntity();
			PhotonicRadio.serverSideInit();
			InputStream ios = null;
			try {
				ios = new FileInputStream(fp.toAbsolutePath().toString());
			} catch (IOException e) {
				throw new dan200.computercraft.api.lua.LuaException("cannot open file");
			}
			try {
				if (fp.getFileName().toString().endsWith(".wav")) {
					try {
						ios.skip(44);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				while (true) {
					if (t.getTrueEnergyStored() < power)
						throw new dan200.computercraft.api.lua.LuaException("not enough RF");
					t.deduceEnergy(power);
					byte[] binary = new byte[2205];
					Arrays.fill(binary, (byte) 128);
					int rb = 0;
					try {
						rb = ios.read(binary);
					} catch (IOException e) {
						throw new dan200.computercraft.api.lua.LuaException("cannot read file");
					}
					if (rb < 0)
						break;
					if (PhotonicRadio.sendCache.containsKey(channel)) {
						context.pullEvent(PhotonicRadio.RADIO_TIMER_EVENT);
					}
					if (!PhotonicRadio.radioSendData(this.loc.getUniqueId(), channel, binary, this.loc, this.power))
						throw new dan200.computercraft.api.lua.LuaException(
								"unauthorized to send data to this channel");
					PhotonicRadio.sendEventWhenSend(channel, computer);
				}
			} finally {
				try {
					ios.close();
				} catch (IOException e) {
				}
			}
		} else if (this.getMethodNames()[method].equalsIgnoreCase("receive")) {
			// receive(channel)
			int tried = 0;
			if (arguments.length < 1)
				throw new dan200.computercraft.api.lua.LuaException("missing channel argument");
			int channel = 0;
			try {
				channel = (int) (double) (Double) arguments[0];
			} catch (Exception ex) {
				try {
					channel = PhotonicRadio.channelNameToID((String) arguments[0]);
					if (channel < 0)
						throw new IllegalArgumentException();
				} catch (Exception ex2) {
					throw new dan200.computercraft.api.lua.LuaException("invalid channel argument");
				}
			}
			if (channel < 0 || channel >= PhotonicRadio.MAX_CHANNEL)
				throw new dan200.computercraft.api.lua.LuaException("invalid channel argument");
			byte[] data = new byte[0];
			PhotonicRadio.serverSideInit();
			while (PhotonicRadio.cannotModify.get())
				context.yield(emptyArray);
			if (PhotonicRadio.dataSentLastTick.containsKey(channel)) {
				data = PhotonicRadio.dataSentLastTick.get(channel);
				double ampl = PhotonicRadio.calculateAmplitude(PhotonicRadio.recvLoc.get(channel).distanceSq(this.loc),
						PhotonicRadio.recvPower.get(channel));
				if (ampl < 0.6) {
					PhotonicUtils.rand.nextBytes(data);
				}
			} else {
				data = new byte[2205];
				PhotonicUtils.rand.nextBytes(data);
			}
			StringBuilder results = new StringBuilder();
			for (int index = 0; index < data.length; index++) {
				results.append((char) PhotonicUtils.unsign(data[index]));
			}
			return new Object[] { results.toString() };
		}
		return null;
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public void detach(dan200.computercraft.api.peripheral.IComputerAccess computer) {
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public boolean equals(dan200.computercraft.api.peripheral.IPeripheral other) {
		return (other != null && other.getClass() == this.getClass()
				&& ((PeripheralRadioTransceiver) other).loc.equals(this.loc));
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public String[] getMethodNames() {
		return new String[] { "getPower", "getMaxPower", "setPower", "canSendTo", "transmit", "transmitFile",
				"receive" };
	}

	private TileEntityRadioTransmitter getTileEntity() {
		return (TileEntityRadioTransmitter) this.loc.getTileEntityAt();
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public String getType() {
		return "photonicradiotransceiver";
	}

}
