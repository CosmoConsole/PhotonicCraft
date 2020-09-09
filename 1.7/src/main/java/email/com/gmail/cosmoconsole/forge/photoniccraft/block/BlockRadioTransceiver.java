package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockRadioTransceiver extends BlockRadioTransmitter {
	public BlockRadioTransceiver() {
		super();
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypePiston);
		this.setBlockName(ModPhotonicCraft.MODID + "_radioTransceiver");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":radioTransceiver");
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
}
