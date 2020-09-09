package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLaserCharger extends TileEntity implements ISidedInventory {
	ItemStack chargedItemSlot = null;
	public boolean chargedPrevTick = false;
	public boolean chargedLastTick = false;
	@Override
	public int getSizeInventory() {
		return 1;
	}
	@Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
        this.chargedItemSlot = null;

        readSyncableDataFromNBT(p_145839_1_);
        
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 == 0)
            {
                this.chargedItemSlot = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }
    @Override
    public void updateEntity() {
    	if (this.worldObj.getTotalWorldTime() > (lastAdded + 5)) {
    		input = 0;
    	}
		this.chargedPrevTick = this.chargedLastTick;
		this.chargedLastTick = false;
		if (chargedItemSlot != null) {
			if (chargedItemSlot.getItem() instanceof IEnergyContainerItem) {
				this.chargedLastTick = (((IEnergyContainerItem)chargedItemSlot.getItem()).receiveEnergy(chargedItemSlot, Integer.MAX_VALUE, true) > 0);
			}
		}
		if ((this.chargedPrevTick != this.chargedLastTick) || ((this.worldObj.getTotalWorldTime() % 20L) == 0L)) {
			worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, ModPhotonicCraft.laserCharger, 0);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}
    }
    boolean puny = false;
	public int input = 0;
	public long lastAdded = 0L;
	public void addSignal(int laserPower) {
		if (worldObj.getTotalWorldTime() > lastAdded) {
			lastAdded = worldObj.getTotalWorldTime();
			input = 0;
		}
		input += 2 * laserPower;
		if (chargedItemSlot != null) {
			if (chargedItemSlot.getItem() instanceof IEnergyContainerItem) {
				((IEnergyContainerItem)chargedItemSlot.getItem()).receiveEnergy(chargedItemSlot, input, false);
			}
		}
	}
	@Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        NBTTagList nbttaglist = new NBTTagList();

        writeSyncableDataToNBT(p_145841_1_);
        
        if (this.chargedItemSlot != null)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)0);
            this.chargedItemSlot.writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
        }
    }
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        this.writeSyncableDataToNBT(syncData);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }
    private void writeSyncableDataToNBT(NBTTagCompound syncData) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        if (this.chargedItemSlot != null) {
			this.chargedItemSlot.writeToNBT(nbttagcompound1);
			syncData.setTag("itemStack", nbttagcompound1);
        }
	}

	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readSyncableDataFromNBT(pkt.func_148857_g());
    }
	private void readSyncableDataFromNBT(NBTTagCompound func_148857_g) {
        if (func_148857_g.hasKey("itemStack")) {
	        NBTTagCompound nbttagcompound1 = func_148857_g.getCompoundTag("itemStack");
	        if (nbttagcompound1 == null) {
	        	this.chargedItemSlot = null;
	        } else {
		        if (this.chargedItemSlot == null)
		        	this.chargedItemSlot = new ItemStack(Blocks.air, 1, 0);
				this.chargedItemSlot.readFromNBT(nbttagcompound1);
	        }
        } else {
        	this.chargedItemSlot = null;
        }
	}
    
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		if (p_70301_1_ == 0) return chargedItemSlot;
		return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (p_70298_1_ == 0 && chargedItemSlot != null) {
			ItemStack charged = chargedItemSlot;
			chargedItemSlot = null;
			return charged;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if (p_70304_1_ == 0 && this.chargedItemSlot != null)
        {
            ItemStack itemstack = this.chargedItemSlot;
            this.chargedItemSlot = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		if (p_70299_1_ == 0) {
			this.chargedItemSlot = p_70299_2_;
	        int a = 0;
	        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit())
	        {
	            p_70299_2_.stackSize = this.getInventoryStackLimit();
	        }
		}
	}
	@Override
	public int getInventoryStackLimit() { 
		return 1; 
	} 

	@Override
	public String getInventoryName() {
		return ModPhotonicCraft.MODID + "_container.laserCharger";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_70300_1_.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return (p_94041_2_.getItem() instanceof IEnergyContainerItem);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return new int[]{};
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return false;
	}

}
