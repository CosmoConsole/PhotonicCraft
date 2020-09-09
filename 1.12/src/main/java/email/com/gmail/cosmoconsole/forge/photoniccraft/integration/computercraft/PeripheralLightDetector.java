package email.com.gmail.cosmoconsole.forge.photoniccraft.integration.computercraft;

import javax.annotation.Nonnull;

import dan200.computercraft.api.peripheral.IPeripheral;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLightDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

public class PeripheralLightDetector implements IPeripheral {

	PhotonicLocation loc;

	public PeripheralLightDetector(@Nonnull final World world, @Nonnull final BlockPos bp) {
		this.loc = PhotonicLocation.fromBlock(world, bp.getX(), bp.getY(), bp.getZ());
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public Object[] callMethod(dan200.computercraft.api.peripheral.IComputerAccess computer,
			dan200.computercraft.api.lua.ILuaContext context, int method, Object[] arguments)
			throws dan200.computercraft.api.lua.LuaException, InterruptedException {
		if (this.getMethodNames()[method].equals("getLight")) {
			TileEntityLightDetector recv = (TileEntityLightDetector) this.loc.getTileEntityAt();
			return new Object[] { recv.light };
		}
		return null;
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (other != null && other.getClass() == this.getClass()
				&& ((PeripheralLightDetector) other).loc.equals(this.loc));
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "getLight" };
	}

	@Override
	public String getType() {
		return "photoniclightdetector";
	}

}
