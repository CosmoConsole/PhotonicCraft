package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import openblocks.common.block.BlockSky;

public class TileEntitySkyLight extends TileEntity implements ITickable {

	private final EnumSkyBlock lighttype = EnumSkyBlock.SKY;

	private int lll = 0;
	private int lastBaseY = -1;

	boolean updating = false;
	boolean mustUpdate = false;
	boolean firstTick = true;

	public TileEntitySkyLight() {
		super();
	}

	private int getSkyLight() {
		/*int i1 = getWorld().getLightFor(EnumSkyBlock.SKY, new BlockPos(this.xCoord(), getWorld().getHeight(), this.zCoord()))
				- getWorld().getSkylightSubtracted();
		float f = getWorld().getCelestialAngleRadians(1.0F);

		if (f < (float) Math.PI) {
			f += (0.0F - f) * 0.2F;
		} else {
			f += (((float) Math.PI * 2F) - f) * 0.2F;
		}

		float cf = MathHelper.cos(f);
		if (cf < 0) {
			cf = 0f;
		}

		i1 = Math.round(i1 * (0.1875f + 0.8125f * MathHelper.sqrt(cf)));

		if (i1 < 0) {
			i1 = 0;
		}

		if (i1 > 15) {
			i1 = 15;
		}
		return i1;*/
		return MathHelper.clamp(getWorld().getLightFor(EnumSkyBlock.SKY, 
				new BlockPos(this.xCoord(), getWorld().getHeight(), this.zCoord()))
				- getWorld().calculateSkylightSubtracted(1.0F), 0, 15);
	}

	public void onBreak() {
		int x = this.xCoord();
		int z = this.zCoord();
		int sl = this.getSkyLight();
		for (int y = 255; y > this.yCoord(); y--) {
			if (sl <= 0)
				break;
			sl -= this.getWorld().getBlockLightOpacity(new BlockPos(x, y, z));
		}
		if (sl < 0)
			sl = 0;
		for (int y = this.yCoord(); y >= lastBaseY; y--) {
			this.getWorld().setLightFor(lighttype, new BlockPos(x, y, z), sl);
			for (int ox = -1; ox < 2; ++ox)
				for (int oz = -1; oz < 2; ++oz) {
					getWorld().checkLight(new BlockPos(x + ox, y, z + oz));
				}
		}
		markBlockRangeForRenderUpdateRange(this.getWorld(), x, this.getWorld().getHeight() / 2, z, 12, this.getWorld().getHeight() / 2, 12);
		for (int ox = -1; ox < 2; ++ox)
			for (int oz = -1; oz < 2; ++oz) {
				this.getWorld().markBlocksDirtyVertical(x, z, 0, 255);
			}
	}

	public void onPlace() {
		int x = this.xCoord();
		int z = this.zCoord();
		int sl = this.getSkyLight();
		int asl = 15;
		for (int y = this.yCoord() - 1; y >= 0; y--) {
			asl -= getBlockLightOpacityWithExceptions(new BlockPos(x, y, z));
			if (asl <= 0)
				return;
			this.getWorld().setLightFor(lighttype, new BlockPos(x, y, z), asl);
			updateLightsHard(getWorld(), x, y, z);
		}
		mustUpdate = true;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		lastBaseY = p_145839_1_.getInteger("baseY");
	}

	private void setLastSunlight(int sl) {
		lll = sl;
		updating = true;
	}

	@Override
	public void update() {
		if (getWorld().isBlockLoaded(pos) && getWorld().provider.hasSkyLight()) {
			int x = this.xCoord();
			int z = this.zCoord();
			int asl = getSkyLight();
			int sl = asl;
			int newBaseY = 0;

			int ly = -1;
			updating |= firstTick;
			for (int y = this.yCoord() - 1; y >= 0; --y) {
				BlockPos pos = new BlockPos(x, y, z);
				if (asl > 0) {
					asl -= getBlockLightOpacityWithExceptions(pos);
					if (asl <= 0) {
						newBaseY = y;
					}
				}
				if (asl < 0)
					asl = 0;
				if (this.getWorld().getBlockState(pos).getBlock() == this.blockType)
					return;
				if ((this.getWorld().getLightFor(EnumSkyBlock.SKY, pos) != asl)) {
					updating = true;
					this.getWorld().setLightFor(lighttype, pos, asl < 0 ? 0 : asl);
					if (asl <= 0)
						this.getWorld().checkLightFor(EnumSkyBlock.SKY, pos);
					if (ly != (y + 1) && ly >= 0) {
						updateLights(getWorld(), x, ly, z);
					}
					ly = y;
				}
				if (asl <= 0 && !updating && lastBaseY >= newBaseY)
					break;
			}
			if (firstTick) {
				for (int y = this.yCoord() - 1; y >= ly; --y) {
					world.notifyBlockUpdate(new BlockPos(x, y, z), this.world.getBlockState(this.pos),
							this.world.getBlockState(this.pos), 3);
				}
				world.markBlockRangeForRenderUpdate(new BlockPos(x - 12, ly, z - 12), new BlockPos(x + 12, this.yCoord(), z + 12));
			}
			if (ly >= 0)
				updateLights(getWorld(), x, ly, z);
			updating = false;
			firstTick = false;
			lastBaseY = newBaseY;
			if (lll != sl)
				setLastSunlight(sl);
		}
		mustUpdate = false;
	}

	private int getBlockLightOpacityWithExceptions(BlockPos pos) {
		if (Compat.openblocks) { // make an exception for OpenBlocks Sky Blocks if they're powered
			IBlockState bs = this.getWorld().getBlockState(pos);
			Block b = bs.getBlock();
			if (b instanceof BlockSky && ((BlockSky) b).isActive(bs)) {
				return 0;
			}
		}
		return this.getWorld().getBlockLightOpacity(pos);
	}

	public void updateLights(World w, int x, int y, int z) {
		int ix = x, iy = y, iz = z;

		markBlockRangeForRenderUpdateRange(this.getWorld(), ix, iy, iz, 12, 12, 12);
		w.notifyBlockUpdate(new BlockPos(ix, iy, iz), this.world.getBlockState(this.pos),
				this.world.getBlockState(this.pos), 2);
	}

	public void updateLightsHard(World w, int x, int y, int z) {
		int ix = x, iy = y, iz = z;

		markBlockRangeForRenderUpdateRange(this.getWorld(), ix, iy, iz, 12, 12, 12);
		w.notifyBlockUpdate(new BlockPos(ix, iy, iz), this.world.getBlockState(this.pos),
				this.world.getBlockState(this.pos), 2);

		checkLightVicinity(w, ix, iy, iz);
	}

	private void checkLightVicinity(World w, int ix, int iy, int iz) {
		w.checkLight(new BlockPos(ix, iy, iz));
		w.checkLight(new BlockPos(ix + 1, iy, iz));
		w.checkLight(new BlockPos(ix + 1, iy, iz + 1));
		w.checkLight(new BlockPos(ix + 1, iy, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy, iz + 1));
		w.checkLight(new BlockPos(ix - 1, iy, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy, iz));
		w.checkLight(new BlockPos(ix, iy, iz + 1));
		w.checkLight(new BlockPos(ix, iy, iz - 1));
	}

	private void markBlockRangeForRenderUpdateRange(World w, int x, int y, int z, int xw, int yw, int zw) {
		BlockPos center = new BlockPos(x, y, z);
		w.markBlockRangeForRenderUpdate(center.add(-xw, -yw, -zw), center.add(xw, yw, zw));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		p_145841_1_.setInteger("baseY", lastBaseY);
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

	public void forceUpdate() {
		mustUpdate = true;
		updating = true;
	}
}
