package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockFloodlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class TileEntityFloodlight extends TileEntity implements ITickable {

	private final EnumSkyBlock lighttype = EnumSkyBlock.BLOCK;

	private int lll = 0;

	boolean powered = false;

	boolean lastpowered = false;

	public TileEntityFloodlight() {
		super();
	}

	private void illuminate(boolean willLight) {
		try {
			BlockFloodlight b = (BlockFloodlight) this.getWorld().getBlockState(this.pos).getBlock();
		} catch (Exception ex) {
			return;
		}
		int light = 15;
		this.updatePower();
		if (powered != lastpowered) {
			this.world.setBlockState(pos,
					this.world.getBlockState(pos).withProperty(BlockFloodlight.powered, this.powered));
		}
		int dir = PhotonicUtils.readDirectionPropertyAsInteger(this.world.getBlockState(this.pos),
				BlockDirectional.FACING);
		int x = this.pos.getX(), y = this.pos.getY(), z = this.pos.getZ();
		int d = 0;
		boolean update = false;
		while (d++ < 64) {
			if (dir == 0)
				y--;
			else if (dir == 1)
				y++;
			else if (dir == 2)
				z--;
			else if (dir == 3)
				z++;
			else if (dir == 4)
				x--;
			else if (dir == 5)
				x++;
			light -= this.getWorld().getBlockLightOpacity(new BlockPos(x, y, z));
			if (light <= 0) {
				break;
			}
			BlockPos bp = new BlockPos(x, y, z);
			if (willLight) {
				if (this.getWorld().getBlockState(bp).getBlock() == Blocks.AIR) {
					this.getWorld().setBlockState(bp, PhotonicBlocks.lightAir.getDefaultState(), 3);
				} else if (this.getWorld().getBlockState(bp).getBlock() == PhotonicBlocks.lightAir) {
					TileEntity te = this.getWorld().getTileEntity(bp);
					if (te instanceof TileEntityLightAir)
						((TileEntityLightAir) te).reset();
				}
			} else {
				if (this.getWorld().getBlockState(bp).getBlock() == PhotonicBlocks.lightAir) {
					this.getWorld().setBlockToAir(bp);
				}
			}
		}
		if (!update)
			lll = d;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public void onBreak() {
		powered = false;
		lastpowered = true;
		illuminate(false);
	}

	public void onPlace() {
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
	}

	@Override
	public void update() {
		if (this.getWorld().getTotalWorldTime() % 5L == 0L
				&& this.getWorld().isBlockIndirectlyGettingPowered(this.pos) > 0) {
			illuminate(true);
		}
	}

	public void updateLights(World w, int x, int y, int z) {
		int ix = x, iy = y, iz = z;

		w.markBlockRangeForRenderUpdate(ix, iy, iz, 12, 12, 12);
		w.markBlocksDirtyVertical(ix, iz, iy + 1, iy - 1);
		w.notifyBlockUpdate(new BlockPos(ix, iy, iz), this.world.getBlockState(this.pos),
				this.world.getBlockState(this.pos), 200);

		w.checkLight(new BlockPos(ix, iy, iz));
		w.checkLight(new BlockPos(ix + 1, iy, iz));
		w.checkLight(new BlockPos(ix + 1, iy, iz + 1));
		w.checkLight(new BlockPos(ix + 1, iy, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy, iz + 1));
		w.checkLight(new BlockPos(ix - 1, iy, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy, iz));
		w.checkLight(new BlockPos(ix, iy, iz + 1));
		w.checkLight(new BlockPos(ix, iy, iz - 1));

		w.checkLight(new BlockPos(ix, iy + 1, iz));
		w.checkLight(new BlockPos(ix + 1, iy + 1, iz));
		w.checkLight(new BlockPos(ix + 1, iy + 1, iz + 1));
		w.checkLight(new BlockPos(ix + 1, iy + 1, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy + 1, iz + 1));
		w.checkLight(new BlockPos(ix - 1, iy + 1, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy + 1, iz));
		w.checkLight(new BlockPos(ix, iy + 1, iz + 1));
		w.checkLight(new BlockPos(ix, iy + 1, iz - 1));
		w.checkLight(new BlockPos(ix, iy - 1, iz));
		w.checkLight(new BlockPos(ix + 1, iy - 1, iz));
		w.checkLight(new BlockPos(ix + 1, iy - 1, iz + 1));
		w.checkLight(new BlockPos(ix + 1, iy - 1, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy - 1, iz + 1));
		w.checkLight(new BlockPos(ix - 1, iy - 1, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy - 1, iz));
		w.checkLight(new BlockPos(ix, iy - 1, iz + 1));
		w.checkLight(new BlockPos(ix, iy - 1, iz - 1));
	}

	private void updatePower() {
		if ((this.getWorld().getTotalWorldTime() % 40L != 0L))
			return;
		lastpowered = powered;
		powered = this.getWorld().isBlockIndirectlyGettingPowered(this.pos) > 0;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		return super.writeToNBT(p_145841_1_);
	}
}
