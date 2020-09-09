package email.com.gmail.cosmoconsole.forge.photoniccraft;

import java.util.ArrayList;
import java.util.HashMap;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserDetector2;
import net.minecraft.world.World;

public class PeripheralAdvLaserDetector implements IPeripheral {
	World w;
	int x;
	int y;
	int z;
	public PeripheralAdvLaserDetector(World world, int x, int y, int z) {
		this.w = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String getType() {
		return "laserdetector";
	}
	public TileEntityLaserDetector2 getTileEntity() {
		try {
			return (TileEntityLaserDetector2)w.getTileEntity(x, y, z);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"getBeams"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {
		TileEntityLaserDetector2 te = getTileEntity();
		if (te == null) throw new LuaException("could not find detector");
		if (method == 0) {
			ArrayList<Object[]> beams = te.getLastBeams(w.getTotalWorldTime());
			int index = 1;
			HashMap<Object, Object> results = new HashMap<Object, Object>();
			for (Object[] beam: beams) {
				HashMap<Object, Object> b = new HashMap<Object, Object>();
				b.put(1, beam[0]);
				b.put(2, beam[1]);
				b.put(3, beam[2]);
				results.put(index++, b);
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
		TileEntityLaserDetector2 te = getTileEntity();
		if (te != null) {
			te.removeComputer(computer.getID());
		}
	}

	@Override
	public boolean equals(IPeripheral other) {
		return false;
	}

}
