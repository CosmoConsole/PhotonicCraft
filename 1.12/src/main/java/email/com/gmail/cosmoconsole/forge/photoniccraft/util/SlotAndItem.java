package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import net.minecraft.item.ItemStack;

public class SlotAndItem {
	private int slot;
	private ItemStack item;
	public SlotAndItem(int slot, ItemStack item) {
		this.slot = slot;
		this.item = item;
	}
	public int getSlot() {
		return this.slot;
	}
	public ItemStack getItem() {
		return this.item;
	}
}
