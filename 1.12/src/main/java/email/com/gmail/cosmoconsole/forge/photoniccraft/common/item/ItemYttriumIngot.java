package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemYttriumIngot extends Item {
	public ItemYttriumIngot() {
		super();
		this.setUnlocalizedName("photoniccraft_yttriumingot");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		if (location != null && location instanceof EntityItem && itemstack.getItem() == this)
			((EntityItem) location).setNoDespawn();
		return super.createEntity(world, location, itemstack);
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}
}
