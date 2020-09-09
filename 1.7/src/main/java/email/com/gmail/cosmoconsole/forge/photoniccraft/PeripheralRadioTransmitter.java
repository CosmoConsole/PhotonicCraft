package email.com.gmail.cosmoconsole.forge.photoniccraft;

import java.util.HashMap;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityRadioTransmitter;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import scala.actors.threadpool.Arrays;

public class PeripheralRadioTransmitter implements IPeripheral {
	PhotonicLocation loc;
	int power = 20;
	public PeripheralRadioTransmitter(World world, int x, int y, int z) {
		this.loc = PhotonicLocation.fromBlock(world, x, y, z);
		this.power = 20;
	}
	
	private TileEntityRadioTransmitter getTileEntity() {
		return (TileEntityRadioTransmitter) this.loc.getTileEntityAt();
	}

	@Override
	public String getType() {
		return "radioTransmitter";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getPower","getMaxPower","setPower","canSendTo","transmit"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {
		if (this.getMethodNames()[method].equalsIgnoreCase("canSendTo")) {
			if (arguments.length < 1)
				throw new LuaException("missing channel argument");
			int channel = 0;
			try {
				channel = (int)(double)(Double)arguments[0];
			} catch (Exception ex) {
				try {
					channel = PhotonicAPI.channelNameToID((String)arguments[0]);
					if (channel < 0) throw new IllegalArgumentException();
				} catch (Exception ex2) {
					throw new LuaException("invalid channel argument");
				}
			}
			if (channel < 0 || channel >= PhotonicAPI.MAX_CHANNEL)
				throw new LuaException("invalid channel argument");
			return new Object[]{PhotonicAPI.radioAuthorizedToSend(this.loc.getUniqueId(), channel)};	
		} else if (this.getMethodNames()[method].equalsIgnoreCase("getMaxPower")) {
			return new Object[]{this.getTileEntity().getEnergyStored(ForgeDirection.UNKNOWN)};	
		} else if (this.getMethodNames()[method].equalsIgnoreCase("getPower")) {
			return new Object[]{this.power};	
		} else if (this.getMethodNames()[method].equalsIgnoreCase("setPower")) {
			if (arguments.length < 1)
				throw new LuaException("missing power argument");
			int channel = 0;
			try {
				channel = (int)(double)(Double)arguments[0];
			} catch (Exception ex) {
				throw new LuaException("invalid power argument");
			}
			if (power < 0 || power > this.getTileEntity().getMaxEnergyStored(ForgeDirection.UNKNOWN))
				throw new LuaException("invalid power argument");
			this.power = channel;
		} else if (this.getMethodNames()[method].equalsIgnoreCase("transmit")) {
			// transmit(channel, data)
			if (arguments.length < 1)
				throw new LuaException("missing channel and data argument");
			if (arguments.length < 2)
				throw new LuaException("missing data argument");
			int channel = 0;
			try {
				channel = (int)(double)(Double)arguments[0];
			} catch (Exception ex) {
				try {
					channel = PhotonicAPI.channelNameToID((String)arguments[0]);
					if (channel < 0) throw new IllegalArgumentException();
				} catch (Exception ex2) {
					throw new LuaException("invalid channel argument");
				}
			}
			if (power < 0 || power >= PhotonicAPI.MAX_CHANNEL)
				throw new LuaException("invalid channel argument");
			int pos = 1;
			if (arguments.length > 2) {
				try {
					pos = (int)(double)(Double)arguments[2];
				} catch (Exception ex) {
					throw new LuaException("invalid position argument");
				}
			}
			if (pos < 1)
				throw new LuaException("invalid position argument");
			TileEntityRadioTransmitter t = this.getTileEntity();
			if (t.getEnergyStored(ForgeDirection.UNKNOWN) < power)
				throw new LuaException("not enough RF");
			t.deduceEnergy(power);
			PhotonicAPI.serverSideInit();
			HashMap<Object, Object> data;
			byte[] binary = new byte[2205];
			Arrays.fill(binary, (byte)128);
			int bytes = 0;
			try {
				data = (HashMap<Object, Object>) arguments[1];
				int cached = 0;
				for (double index = pos; index < pos + 2205; index+=1) {
					if (!data.containsKey(index)) break;
					binary[bytes++] = (byte)(((Double) data.get(index)).doubleValue());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new LuaException("invalid data argument");
			}
			if (PhotonicAPI.sendCache.containsKey(channel)) {
				PhotonicAPI.sendEventWhenSend(computer);
				context.pullEvent("photoniccraft_sendtimer");
			}
			if (!PhotonicAPI.radioSendData(this.loc.getUniqueId(), channel, binary, this.loc, this.power))
				throw new LuaException("unauthorized to send data to this channel");
			PhotonicAPI.sendEventWhenSend(computer);
			context.pullEvent("photoniccraft_sendtimer");
		}
		return null;
	}

	@Override
	public void attach(IComputerAccess computer) {
	}

	@Override
	public void detach(IComputerAccess computer) {
	}

	@Override
	public boolean equals(IPeripheral other) {
		return false;
	}

}
