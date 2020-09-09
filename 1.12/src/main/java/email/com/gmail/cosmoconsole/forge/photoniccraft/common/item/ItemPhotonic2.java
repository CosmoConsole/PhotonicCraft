package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.item.Item;

public class ItemPhotonic2 extends Item {
	public ItemPhotonic2(String name) {
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_" + name);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}

}
