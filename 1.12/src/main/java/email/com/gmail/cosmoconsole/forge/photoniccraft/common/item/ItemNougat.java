package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.item.ItemFood;

public class ItemNougat extends ItemFood {

	public long lastReceive = 0L;

	public ItemNougat(int p_i45340_1_, float s, boolean p_i45340_2_) {
		super(p_i45340_1_, s, p_i45340_2_);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_nougat");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}
}
