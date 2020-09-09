package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserRedstoneMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityLaserRedstoneMirror extends TileEntity implements ITickable {

	@Override
	public void update() {
		if (!(this.world.getBlockState(pos).getBlock() instanceof BlockLaserRedstoneMirror))
			return;
		boolean vpowered = (this.world.getBlockState(pos).getBlock() == PhotonicBlocks.laserRedstoneMirrorOn);
		boolean apowered = (this.world.isBlockIndirectlyGettingPowered(pos) > 0);
		if (vpowered != apowered) {
			if (apowered) {
				this.world.setBlockState(pos,
						PhotonicBlocks.laserRedstoneMirrorOn.getDefaultState()
								.withProperty(BlockLaserRedstoneMirror.orient, PhotonicUtils.readIntegerProperty(
										this.world.getBlockState(pos), BlockLaserRedstoneMirror.orient)));
			} else {
				this.world.setBlockState(pos,
						PhotonicBlocks.laserRedstoneMirror.getDefaultState()
								.withProperty(BlockLaserRedstoneMirror.orient, PhotonicUtils.readIntegerProperty(
										this.world.getBlockState(pos), BlockLaserRedstoneMirror.orient)));
			}
		}
	}

}
