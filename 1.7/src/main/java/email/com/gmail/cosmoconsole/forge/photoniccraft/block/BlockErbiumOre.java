package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.BlockOre;
import net.minecraft.creativetab.CreativeTabs;

public class BlockErbiumOre extends BlockOre {
	public BlockErbiumOre() {
		super();
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypePiston);
		this.setBlockName(ModPhotonicCraft.MODID + "_erbiumOre");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":erbiumOre");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHarvestLevel("pickaxe", 2);
	}
}
