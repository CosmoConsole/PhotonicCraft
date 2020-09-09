package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockTantalumBlock extends BlockCompressed {

	public BlockTantalumBlock(MapColor p_i45414_1_) {
		super(p_i45414_1_);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setStepSound(soundTypePiston);
		this.setBlockName(ModPhotonicCraft.MODID + "_tantalumBlock");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":tantalumBlock");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHarvestLevel("pickaxe", 1);
	}


}
