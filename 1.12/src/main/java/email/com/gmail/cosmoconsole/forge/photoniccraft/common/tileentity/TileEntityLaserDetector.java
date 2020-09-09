package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityLaserDetector extends TileEntity implements ITickable {
	public int ticks = 0;

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
	}

	public void setTicks(int t) {
		if (ticks <= 1 && t > 0) {
			updateNeighbors();
		}
		ticks = t;
	}

	private void updateNeighbors() {
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
		getWorld().notifyNeighborsOfStateChange(pos, getWorld().getBlockState(pos).getBlock(), true);
	}

	@Override
	public void update() {
		if (!world.isRemote && (world.getTotalWorldTime() % 20L) == 0) {
			updateNeighbors();
		}
		if (ticks > 0) {
			ticks--;
			if (ticks <= 0) {
				ticks = 0;
				updateNeighbors();
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
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
