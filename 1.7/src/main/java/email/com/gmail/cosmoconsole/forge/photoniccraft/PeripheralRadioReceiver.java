package email.com.gmail.cosmoconsole.forge.photoniccraft;

import java.util.HashMap;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.World;

public class PeripheralRadioReceiver implements IPeripheral {
	PhotonicLocation loc;
	public PeripheralRadioReceiver(World world, int x, int y, int z) {
		this.loc = PhotonicLocation.fromBlock(world, x, y, z);
	}

	@Override
	public String getType() {
		return "radioReceiver";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"receive"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {
		if (this.getMethodNames()[method].equalsIgnoreCase("receive")) {
			// receive(channel)
			int tried = 0;
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
			byte[] data = new byte[0];
			PhotonicAPI.serverSideInit();
			while (tried < 25) {
				PhotonicAPI.sendEventWhenRecv(computer);
				try {
					context.pullEvent("photoniccraft_recvtimer");
				} catch (Exception ex) {
					throw new LuaException("could not sleep");
				}
				if (PhotonicAPI.recvData.containsKey(channel)) {
					data = PhotonicAPI.recvData.get(channel);
					byte[] noise = new byte[data.length];
					PhotonicAPI.rand.nextBytes(noise);
					double ampl = PhotonicAPI.calculateAmplitude(PhotonicAPI.recvLoc.get(channel).distanceSq(this.loc), PhotonicAPI.recvPower.get(channel));
					double iampl = 1 - ampl;
					for (int i = 0; i < data.length; i++) {
						data[i] = (byte)(((ampl * data[i] + iampl * noise[i])));
					}
					break;
				}
				tried++;
			}
			HashMap<Object, Object> results = new HashMap<Object, Object>();
			for (int index = 0; index < data.length; index++) {
				results.put(index + 1, PhotonicAPI.unsign(data[index]));
			}
			return new Object[]{results};	
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
