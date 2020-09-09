package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRadioTransmitter extends TileEntity implements IEnergyReceiver {

	EnergyStorage energy;

    public TileEntityRadioTransmitter() {
    	super();
    	this.energy = new EnergyStorage(100000);
    }
    @Override
    public void validate() {
    	super.validate();
        
        this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, ModPhotonicCraft.radioTransmitter, 10);
    }
    @Override
	public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.energy.setEnergyStored(p_145839_1_.getInteger("energy"));
    }
    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("energy", energy.getEnergyStored());
    }
	
	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) {
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection arg0) {
		return energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection arg0) {
		return energy.getMaxEnergyStored();
	}

	@Override
	public int receiveEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		return energy.receiveEnergy(arg1, arg2);
	}
	public void deduceEnergy(int power) {
		this.energy.setEnergyStored(Math.max(0, this.energy.getEnergyStored() - power));
	}

}
