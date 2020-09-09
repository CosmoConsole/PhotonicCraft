package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockRadioReceiver extends Block {
	public BlockRadioReceiver() {
		super(Material.rock);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypePiston);
		this.setBlockName(ModPhotonicCraft.MODID + "_radioReceiver");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":radioReceiver");
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
}
