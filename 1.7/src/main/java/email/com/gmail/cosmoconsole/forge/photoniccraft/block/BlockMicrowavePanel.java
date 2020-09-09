package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityMicrowaveGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class BlockMicrowavePanel extends BlockOre {
	public BlockMicrowavePanel() {
		super();
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypePiston);
		this.setBlockName(ModPhotonicCraft.MODID + "_microwavePanel");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":microwavePanel");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHarvestLevel("pickaxe", 0);
	}
	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
		int y_ = p_149749_3_;
		int x_ = p_149749_2_;
		int z_ = p_149749_4_;
		for (int x = x_-y_; x <= x_+y_; x++)
			for (int z = z_-y_; z <= z_+y_; z++) 
				for (int y = y_; y < p_149749_1_.getActualHeight(); y++) {
					if (p_149749_1_.getBlock(x, y, z) == ModPhotonicCraft.microwaveGenerator) {
						((TileEntityMicrowaveGenerator) p_149749_1_.getTileEntity(x, y, z)).triggerFullCheck();
					}
				}
	}
}
