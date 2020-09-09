package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

public class ItemPhotonicCoupler extends Item {
	public static final String[] colors = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan",
			"lightgray", "gray", "pink", "lime", "yellow", "lightblue", "magenta", "orange", "white" };

	public ItemPhotonicCoupler() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_coupler");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> p_150895_3_) {
		if (this.isInCreativeTab(tab)) {
			for (int i = 0; i < 16; ++i) {
				p_150895_3_.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		int i = MathHelper.clamp(p_77667_1_.getItemDamage(), 0, 15);
		return "item." + ModPhotonicCraft.MODID + "_coupler." + colors[i];
	}
}
