package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicAPI;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicRadioPacket;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBeefJerky extends ItemFood {
	
	public ItemBeefJerky(int p_i45340_1_, float s, boolean p_i45340_2_) {
		super(p_i45340_1_, s, p_i45340_2_);
        this.setTextureName(ModPhotonicCraft.MODID + ":beefJerky");
        this.setUnlocalizedName(ModPhotonicCraft.MODID + "_beefJerky");
		this.setCreativeTab(CreativeTabs.tabFood);
	}
	public long lastReceive = 0L;
}
