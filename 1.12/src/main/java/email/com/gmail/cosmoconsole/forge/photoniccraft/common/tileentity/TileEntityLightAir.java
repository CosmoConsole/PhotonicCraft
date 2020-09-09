package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class TileEntityLightAir extends TileEntity implements ITickable {
	public static final int DEF_TICKS = 10;
	public static final EnumSkyBlock lighttype = EnumSkyBlock.BLOCK;

	public static void updateLights(World w, int x, int y, int z) {
		int ix = x, iy = y, iz = z;
		BlockPos pos = new BlockPos(x, y, z);

		w.markBlockRangeForRenderUpdate(ix, iy, iz, 12, 12, 12);
		w.notifyBlockUpdate(new BlockPos(ix, iy, iz), w.getBlockState(pos), w.getBlockState(pos), 2);

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

	public int ticksLeft = DEF_TICKS;

	public TileEntityLightAir() {
		super();
		this.ticksLeft = DEF_TICKS;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		this.ticksLeft = p_145839_1_.getInteger("ticksLeft");
	}

	public void reset() {
		this.ticksLeft = DEF_TICKS;
	}

	@Override
	public void update() {
		if (this.getWorld().isRemote)
			return;
		if (ticksLeft <= 0) {
			if (this.getWorld().getBlockState(this.pos).getBlock() == PhotonicBlocks.lightAir) {
				this.getWorld().setBlockToAir(this.pos);
			}
		} else
			ticksLeft--;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		p_145841_1_.setInteger("ticksLeft", ticksLeft);
		return super.writeToNBT(p_145841_1_);
	}

	private int xCoord() {
		return this.pos.getX();
	}

	private int yCoord() {
		return this.pos.getY();
	}

	private int zCoord() {
		return this.pos.getZ();
	}
}
