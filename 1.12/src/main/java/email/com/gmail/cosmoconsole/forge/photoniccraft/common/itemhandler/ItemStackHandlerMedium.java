package email.com.gmail.cosmoconsole.forge.photoniccraft.common.itemhandler;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerMedium extends ItemStackHandler {
	TileEntityLaser e;

	public ItemStackHandlerMedium() {
		super();
		this.e = null;
	}

	public ItemStackHandlerMedium(int size) {
		super(size);
		this.e = null;
	}

	public ItemStackHandlerMedium(int size, TileEntityLaser e) {
		super(size);
		this.e = e;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (this.e != null)
			this.e.forceUpdate();
		ItemStack st = super.extractItem(slot, amount, simulate);
		if (this.e != null) {
			this.e.updateInput();
		}
		return st;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!PhotonicUtils.itemTypeEquals(stack.getItem(), PhotonicItems.photonicResources[4])
				&& !PhotonicUtils.itemTypeEquals(stack.getItem(), PhotonicItems.photonicResources[5])
				&& !PhotonicUtils.itemTypeEquals(stack.getItem(), PhotonicItems.photonicResources[6])
				&& !PhotonicUtils.itemTypeEquals(stack.getItem(), PhotonicItems.photonicResources[7])
				&& !PhotonicUtils.itemTypeEquals(stack.getItem(), PhotonicItems.photonicResources[14]))
			return stack;
		if (this.e != null) {
			this.e.updateInput();
		}
		ItemStack st = super.insertItem(slot, stack, simulate);
		return st;
	}
}
