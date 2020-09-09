package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.item.Item;

public class ItemTerahertzReceiver extends Item {
	public ItemTerahertzReceiver() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_terahertzreceiver");
		this.setMaxStackSize(1);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}
}
