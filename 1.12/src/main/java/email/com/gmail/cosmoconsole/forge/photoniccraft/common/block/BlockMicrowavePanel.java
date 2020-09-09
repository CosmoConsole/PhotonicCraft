package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityMicrowaveGenerator;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMicrowavePanel extends BlockOre {
	public BlockMicrowavePanel() {
		super();
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_microwavepanel");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setHarvestLevel("pickaxe", 0);
	}

	@Override
	public void breakBlock(World p_149749_1_, BlockPos bp, IBlockState bs) {
		super.breakBlock(p_149749_1_, bp, bs);
		int y_ = bp.getY();
		int x_ = bp.getX();
		int z_ = bp.getZ();
		for (int x = x_ - y_; x <= x_ + y_; x++)
			for (int z = z_ - y_; z <= z_ + y_; z++)
				for (int y = y_; y < p_149749_1_.getActualHeight(); y++) {
					if (p_149749_1_.getBlockState(new BlockPos(x, y, z))
							.getBlock() == PhotonicBlocks.microwaveGenerator) {
						((TileEntityMicrowaveGenerator) p_149749_1_.getTileEntity(new BlockPos(x, y, z)))
								.triggerFullCheck();
					}
				}
	}
}
