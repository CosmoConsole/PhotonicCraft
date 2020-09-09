package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPhotonic extends Item {
	private boolean effect;

	public ItemPhotonic(String name, boolean hasEffect) {
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_" + name);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.effect = hasEffect;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack p_77636_1_) {
		return this.effect;
	}
}
