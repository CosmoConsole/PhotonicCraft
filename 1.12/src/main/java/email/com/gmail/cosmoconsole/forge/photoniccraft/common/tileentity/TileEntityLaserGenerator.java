package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EnergyContainer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityLaserGenerator extends TileEntity implements ITickable {

	public static final int MAX_RF_CAPACITY = 100000;
	int laserEnergy = 0;
	EnergyContainer festorage;

	public TileEntityLaserGenerator() {
		this.festorage = new EnergyContainer(MAX_RF_CAPACITY, 1000, 1000);
	}

	public void addLaser(int laserPower) {
		this.festorage.receiveEnergy(2 * laserPower, false);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		this.laserEnergy = p_145839_1_.getInteger("laserEnergy");
		this.festorage.setEnergyStored(p_145839_1_.getInteger("fenergy"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		p_145841_1_.setInteger("laserEnergy", laserEnergy);
		p_145841_1_.setInteger("fenergy", festorage.getEnergyStored());
		return super.writeToNBT(p_145841_1_);
	}

	@Override
	public void update() {
		if ((festorage.getEnergyStored() > 0)) {
			for (EnumFacing facing : PhotonicUtils.getShuffledFaces()) {
				TileEntity tile = getWorld().getTileEntity(new BlockPos(xCoord() + facing.getFrontOffsetX(),
						yCoord() + facing.getFrontOffsetY(), zCoord() + facing.getFrontOffsetZ()));
				if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
					IEnergyStorage enstr = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
					festorage.extractEnergy(
							enstr.receiveEnergy(festorage.extractEnergy(festorage.getMaxExtract(), true), false),
							false);
				}
			}
		}
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
