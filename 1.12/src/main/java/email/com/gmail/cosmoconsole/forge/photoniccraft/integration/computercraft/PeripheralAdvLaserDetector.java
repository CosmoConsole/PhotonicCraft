package email.com.gmail.cosmoconsole.forge.photoniccraft.integration.computercraft;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nonnull;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserDetector2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

/**
 * The ComputerCraft IPeripheral implementation for the Advanced Laser Detector
 * peripheral.
 */
@Optional.InterfaceList({@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = Compat.MODID_COMPUTERCRAFT)})
public class PeripheralAdvLaserDetector implements dan200.computercraft.api.peripheral.IPeripheral {
	World w;
	int x;
	int y;
	int z;

	public PeripheralAdvLaserDetector(@Nonnull final World world, @Nonnull final BlockPos bp) {
		this.w = world;
		this.x = bp.getX();
		this.y = bp.getY();
		this.z = bp.getZ();
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public void attach(dan200.computercraft.api.peripheral.IComputerAccess computer) {
		TileEntityLaserDetector2 te = getTileEntity();
		if (te != null && te.hasCatchForComputer(computer.getID())) {
			te.catchComputer(te.getWorld().getTotalWorldTime(), computer.getID());
		}
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public Object[] callMethod(dan200.computercraft.api.peripheral.IComputerAccess computer,
			dan200.computercraft.api.lua.ILuaContext context, int method, Object[] arguments)
			throws dan200.computercraft.api.lua.LuaException, InterruptedException {
		TileEntityLaserDetector2 te = getTileEntity();
		if (te == null)
			throw new dan200.computercraft.api.lua.LuaException("could not find detector");
		if (method == 0) {
			ArrayList<Object[]> beams = te.getLastBeams(w.getTotalWorldTime());
			int index = 1;
			HashMap<Object, Object> results = new HashMap<Object, Object>();
			for (Object[] beam : beams) {
				HashMap<Object, Object> b = new HashMap<Object, Object>();
				b.put(1, beam[0]);
				b.put(2, beam[1]);
				b.put(3, beam[2]);
				results.put(index++, b);
			}
			return new Object[] { results };
		}
		return null;
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public void detach(dan200.computercraft.api.peripheral.IComputerAccess computer) {
		TileEntityLaserDetector2 te = getTileEntity();
		if (te != null) {
			te.removeComputer(computer.getID());
		}
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public boolean equals(dan200.computercraft.api.peripheral.IPeripheral other) {
		return (other != null && other.getClass() == this.getClass() && ((PeripheralAdvLaserDetector) other).x == this.x
				&& ((PeripheralAdvLaserDetector) other).y == this.y
				&& ((PeripheralAdvLaserDetector) other).z == this.z);
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public String[] getMethodNames() {
		return new String[] { "getBeams" };
	}

	public TileEntityLaserDetector2 getTileEntity() {
		try {
			return (TileEntityLaserDetector2) w.getTileEntity(new BlockPos(x, y, z));
		} catch (Exception ex) {
			return null;
		}
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public String getType() {
		return "photoniclaserdetector";
	}

}
