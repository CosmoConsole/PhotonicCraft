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

public class ItemNougat extends ItemFood {
	
	public ItemNougat(int p_i45340_1_, float s, boolean p_i45340_2_) {
		super(p_i45340_1_, s, p_i45340_2_);
        this.setTextureName(ModPhotonicCraft.MODID + ":nougat");
        this.setUnlocalizedName(ModPhotonicCraft.MODID + "_nougat");
		this.setCreativeTab(CreativeTabs.tabFood);
	}
	public long lastReceive = 0L;
}
