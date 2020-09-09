package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemLaserBlock extends ItemBlock {
	private String myName;

	public ItemLaserBlock(Block placeBlock, String name) {
		super(placeBlock);
		this.myName = name;
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_ib" + name);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}

	@Override
	public String getUnlocalizedName() {
		return "item." + ModPhotonicCraft.MODID + "_" + myName;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + ModPhotonicCraft.MODID + "_" + myName;
	}

}