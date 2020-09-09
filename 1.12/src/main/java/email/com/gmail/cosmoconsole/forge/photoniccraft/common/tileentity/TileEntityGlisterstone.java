package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;

public class TileEntityGlisterstone extends TileEntity implements ITickable {
	public int light = 0;
	int tick = 5;
	int buffer = 0;

	long ticks = 0L;

	public TileEntityGlisterstone() {
		super();
		this.light = 0;
		this.tick = 5;
		this.buffer = 0;
	}

	public int getLight() {
		return light;
	}

	private int getSkyLight() {
		int i1 = getWorld().getLightFor(EnumSkyBlock.SKY, new BlockPos(this.xCoord(), this.yCoord(), this.zCoord()))
				- getWorld().getSkylightSubtracted();
		float f = getWorld().getCelestialAngleRadians(1.0F);

		if (f < (float) Math.PI) {
			f += (0.0F - f) * 0.2F;
		} else {
			f += (((float) Math.PI * 2F) - f) * 0.2F;
		}

		i1 = Math.round(i1 * MathHelper.cos(f));

		if (i1 < 0) {
			i1 = 0;
		}

		if (i1 > 15) {
			i1 = 15;
		}
		return i1;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		this.light = p_145839_1_.getInteger("light");
		this.tick = p_145839_1_.getInteger("delay");
		this.buffer = p_145839_1_.getInteger("buffer");
	}

	private void trySpread(int i, int j, int k) {
		if (yCoord() == 0 && j < 0)
			return;
		if (yCoord() == (this.getWorld().getHeight() - 1) && j > 0)
			return;
		if (this.getWorld().getBlockState(new BlockPos(this.xCoord() + i, this.yCoord() + j, this.zCoord() + k))
				.getBlock() == PhotonicBlocks.glisterstone) {
			try {
				TileEntityGlisterstone te = (TileEntityGlisterstone) this.getWorld()
						.getTileEntity(new BlockPos(this.xCoord() + i, this.yCoord() + j, this.zCoord() + k));
				if (this.light > te.light)
					te.light = this.light;
			} catch (Exception ex) {
			}
		}
	}

	@Override
	public void update() {
		if (this.tick > 0) {
			--this.tick;
			return;
		}
		ticks++;
		int myl = Math.min(15, this.getSkyLight()
				+ this.getWorld().getLightFor(EnumSkyBlock.BLOCK, new BlockPos(xCoord(), yCoord(), zCoord())));
		if (this.light >= myl && this.light > 0 && (ticks % 60L == 0L)) {
			this.light--;
			this.buffer = 0;
			TileEntityLightAir.updateLights(this.getWorld(), this.xCoord(), this.yCoord(), this.zCoord());
		} else if ((myl - 1) > this.light) {
			if (this.buffer >= 10) {
				this.light = Math.max(0, myl - 1);
				this.buffer = 0;
			} else {
				this.buffer++;
			}
		}
		if ((myl - 1) <= this.light)
			this.buffer = 0;
		if (Math.random() < 0.3) {
			if (Math.random() < 0.3) {
				this.light--;
			}
			this.trySpread(1, 0, 0);
			this.trySpread(-1, 0, 0);
			this.trySpread(0, 1, 0);
			this.trySpread(0, -1, 0);
			this.trySpread(0, 0, 1);
			this.trySpread(0, 0, -1);
			this.tick = 5;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		p_145841_1_.setInteger("light", light);
		p_145841_1_.setInteger("delay", tick);
		p_145841_1_.setInteger("buffer", buffer);
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
