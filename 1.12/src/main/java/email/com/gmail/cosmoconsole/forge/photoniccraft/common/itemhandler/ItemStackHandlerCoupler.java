package email.com.gmail.cosmoconsole.forge.photoniccraft.common.itemhandler;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaser;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerCoupler extends ItemStackHandler {
	TileEntityLaser e;

	public ItemStackHandlerCoupler() {
		super();
		this.e = null;
	}

	public ItemStackHandlerCoupler(int size) {
		super(size);
		this.e = null;
	}

	public ItemStackHandlerCoupler(int size, TileEntityLaser e) {
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
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.getItem() != PhotonicItems.photonicCoupler)
			return stack;
		if (this.e != null)
			this.e.forceUpdate();
		return super.insertItem(slot, stack, simulate);
	}
}
