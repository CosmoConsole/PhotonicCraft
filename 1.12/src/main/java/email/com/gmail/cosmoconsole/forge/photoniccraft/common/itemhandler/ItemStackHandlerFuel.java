package email.com.gmail.cosmoconsole.forge.photoniccraft.common.itemhandler;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaser;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerFuel extends ItemStackHandler {
	TileEntityLaser e;

	public ItemStackHandlerFuel() {
		super();
		this.e = null;
	}

	public ItemStackHandlerFuel(int size) {
		super(size);
		this.e = null;
	}

	public ItemStackHandlerFuel(int size, TileEntityLaser e) {
		super(size);
		this.e = e;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (this.e != null)
			this.e.forceUpdate();
		return super.extractItem(slot, amount, simulate);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!TileEntityFurnace.isItemFuel(stack))
			return stack;
		if (this.e != null)
			this.e.forceUpdate();
		return super.insertItem(slot, stack, simulate);
	}
}
