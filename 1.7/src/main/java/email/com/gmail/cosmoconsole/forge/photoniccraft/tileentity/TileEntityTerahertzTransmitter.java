package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityTerahertzTransmitter extends TileEntity implements IEnergyReceiver {

	EnergyStorage energy;

    public TileEntityTerahertzTransmitter() {
    	super();
    	this.energy = new EnergyStorage(10000);
    }
    @Override
    public void validate() {
    	super.validate();
        
        this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, ModPhotonicCraft.terahertzTransmitter, 10);
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
    public void updateEntity() {
    	if (this.worldObj.isRemote) return;
    	if (this.worldObj.getTotalWorldTime() % 4L != 0L) return;
    	for (Object o: this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.xCoord - 6, this.yCoord - 6, this.zCoord - 6, this.xCoord + 6, this.yCoord + 6, this.zCoord + 6))) {
    		EntityPlayer p = (EntityPlayer) o;
    		boolean charge = false;
    		for (ItemStack i: p.inventory.mainInventory) {
    			if (charge) break;
    			if (i == null) continue;
    			charge = i.getItem() == ModPhotonicCraft.terahertzReceiver;
    		}
    		if (charge) {
    			for (ItemStack i: p.inventory.mainInventory) {
        			if (i == null) continue;
        			if (!(i.getItem() instanceof IEnergyContainerItem)) continue;
        			if (this.energy.getEnergyStored() < 1) return;
        			this.energy.extractEnergy(((IEnergyContainerItem)i.getItem()).receiveEnergy(i, Math.min(100, this.energy.getEnergyStored()), false), false);
        		}
    		}
    	}
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
