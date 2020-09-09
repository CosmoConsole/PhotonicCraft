package email.com.gmail.cosmoconsole.forge.photoniccraft.common.itemhandler;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityMicrowaveOven;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerMicrowave extends ItemStackHandler {
	TileEntityMicrowaveOven e;

	public ItemStackHandlerMicrowave() {
		super();
		this.e = null;
	}

	public ItemStackHandlerMicrowave(int size) {
		super(size);
		this.e = null;
	}

	public ItemStackHandlerMicrowave(int size, TileEntityMicrowaveOven e) {
		super(size);
		this.e = e;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (this.e != null)
			this.e.okToUse = true;
		return super.extractItem(slot, amount, simulate);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!(stack.getItem() instanceof ItemFood))
			return stack;
		if (!TileEntityMicrowaveOven.canSmelt(stack))
			return stack;
		if (this.e != null)
			this.e.okToUse = true;
		return super.insertItem(slot, stack, simulate);
	}
}
