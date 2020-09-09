package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityLaserGenerator extends TileEntity implements IEnergyProvider {

	int laserEnergy = 0;
	public TileEntityLaserGenerator() {
		this.storage = new EnergyStorage(100000);
	}
    @Override
	public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.laserEnergy = p_145839_1_.getInteger("laserEnergy");
    }
    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("laserEnergy", laserEnergy);
    }
    public void addLaser(int laserPower) {
    	this.storage.setEnergyStored(Math.min(this.storage.getMaxEnergyStored(), this.storage.getEnergyStored() + 2 * laserPower));
    }
    @Override
    public void updateEntity() {
    	if ((storage.getEnergyStored() > 0)) {
    		for (int i = 0; i < 6; i++){
    			TileEntity tile = worldObj.getTileEntity(xCoord + ForgeDirection.getOrientation(i).offsetX, yCoord + ForgeDirection.getOrientation(i).offsetY, zCoord + ForgeDirection.getOrientation(i).offsetZ);
    			if (tile != null && tile instanceof IEnergyHandler) {
    				storage.extractEnergy(((IEnergyHandler)tile).receiveEnergy(ForgeDirection.getOrientation(i).getOpposite(), storage.extractEnergy(storage.getMaxExtract(), true), false), false);
    			}
    		}
    	}
    }
	
	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) {
		return true;
	}
	
	EnergyStorage storage;

	@Override
	public int extractEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		return storage.extractEnergy(arg1, arg2);
	}

	@Override
	public int getEnergyStored(ForgeDirection arg0) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection arg0) {
		return storage.getMaxEnergyStored();
	}

}
