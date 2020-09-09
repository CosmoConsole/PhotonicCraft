package email.com.gmail.cosmoconsole.forge.photoniccraft.integration.computercraft;

import javax.annotation.Nonnull;

import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicLocation;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

/**
 * The ComputerCraft IPeripheral implementation for the Radio Receiver
 * peripheral.
 */
@Optional.InterfaceList({@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = Compat.MODID_COMPUTERCRAFT)})
public class PeripheralRadioReceiver implements dan200.computercraft.api.peripheral.IPeripheral {
	private static final Object[] emptyArray = new Object[0];
	PhotonicLocation loc;

	public PeripheralRadioReceiver(@Nonnull final World world, @Nonnull final BlockPos bp) {
		this.loc = PhotonicLocation.fromBlock(world, bp.getX(), bp.getY(), bp.getZ());
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
		if (this.getMethodNames()[method].equalsIgnoreCase("receive")) {
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
				&& ((PeripheralRadioReceiver) other).loc.equals(this.loc));
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public String[] getMethodNames() {
		return new String[] { "receive" };
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public String getType() {
		return "photonicradioreceiver";
	}

}
