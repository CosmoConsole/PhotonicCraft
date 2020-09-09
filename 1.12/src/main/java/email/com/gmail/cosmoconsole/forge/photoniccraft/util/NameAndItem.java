package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import net.minecraft.item.Item;

/**
 * A helper class used for registrating item blocks and items that used to be
 * implemented using subitems.
 */
public class NameAndItem {
	private String name;
	private Item item;

	public NameAndItem(String name, Item item) {
		this.name = name;
		this.item = item;
	}

	public Item getItem() {
		return this.item;
	}

	public String getName() {
		return this.name;
	}
}
