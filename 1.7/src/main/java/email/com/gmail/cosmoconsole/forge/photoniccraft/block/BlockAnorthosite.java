package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.BlockOre;
import net.minecraft.creativetab.CreativeTabs;

public class BlockAnorthosite extends BlockOre {
	public BlockAnorthosite() {
		super();
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypePiston);
		this.setBlockName(ModPhotonicCraft.MODID + "_anorthosite");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":anorthosite");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHarvestLevel("pickaxe", 0);
	}
}
