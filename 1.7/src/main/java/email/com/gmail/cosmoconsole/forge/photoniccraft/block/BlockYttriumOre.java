package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.BlockOre;
import net.minecraft.creativetab.CreativeTabs;

public class BlockYttriumOre extends BlockOre {
	public BlockYttriumOre() {
		super();
		this.setHardness(5.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypePiston);
		this.setBlockName(ModPhotonicCraft.MODID + "_yttriumPhotonicO");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":yttriumOre");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHarvestLevel("pickaxe", 4);
	}
}
