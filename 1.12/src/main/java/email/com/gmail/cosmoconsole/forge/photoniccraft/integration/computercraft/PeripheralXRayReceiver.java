package email.com.gmail.cosmoconsole.forge.photoniccraft.integration.computercraft;

import java.util.HashMap;

import javax.annotation.Nonnull;

import dan200.computercraft.api.peripheral.IPeripheral;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityXRayReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicLocation;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.SlotAndItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

public class PeripheralXRayReceiver implements IPeripheral {

	PhotonicLocation loc;

	public PeripheralXRayReceiver(@Nonnull final World world, @Nonnull final BlockPos bp) {
		this.loc = PhotonicLocation.fromBlock(world, bp.getX(), bp.getY(), bp.getZ());
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public Object[] callMethod(dan200.computercraft.api.peripheral.IComputerAccess computer,
			dan200.computercraft.api.lua.ILuaContext context, int method, Object[] arguments)
			throws dan200.computercraft.api.lua.LuaException, InterruptedException {
		if (this.getMethodNames()[method].equals("getItem")) {
			TileEntityXRayReceiver recv = (TileEntityXRayReceiver) this.loc.getTileEntityAt();
			SlotAndItem si = recv.getSelectedItem();
			if (si == null)
				return null;
			HashMap<Object, Object> hm = new HashMap<>();
			hm.put("slot", si.getSlot());
			hm.put("item", PhotonicUtils.simpleItemToTable(si.getItem()));
			return new Object[] { hm };
		}
		return null;
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (other != null && other.getClass() == this.getClass()
				&& ((PeripheralXRayReceiver) other).loc.equals(this.loc));
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "getItem" };
	}

	@Override
	public String getType() {
		return "photonicxrayreceiver";
	}

}
