package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMicrowaveOven extends TileEntity implements ISidedInventory, IEnergyReceiver {
	public ItemStack chargedItemSlot = null;
	public boolean chargedPrevTick = false;
	public boolean chargedLastTick = false;
	public double angle = 0.0;
	public EntityItem entItem = null;
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
		this.chargedPrevTick = this.chargedLastTick;
		this.chargedLastTick = false;
		if (this.entItem == null && this.chargedItemSlot != null)
            this.entItem = new EntityItem(Minecraft.getMinecraft().theWorld, 0D, 0D, 0D, this.chargedItemSlot);
		else if (this.entItem != null && this.chargedItemSlot == null)
			this.entItem = null;
		if (chargedItemSlot != null) {
			/*if (chargedItemSlot.getItem() instanceof IEnergyContainerItem) {
				this.chargedLastTick = (((IEnergyContainerItem)chargedItemSlot.getItem()).receiveEnergy(chargedItemSlot, Integer.MAX_VALUE, true) > 0);
			}*/
			this.chargedLastTick = false;
			
			ItemStack i = FurnaceRecipes.smelting().getSmeltingResult(chargedItemSlot);
			if (i != null) {
				this.chargedLastTick = (this.chargedItemSlot.getItem() instanceof ItemFood) || (i.getItem() instanceof ItemFood);
				if (this.chargedLastTick)
					this.neededprogress = chargedItemSlot.stackSize * 20;
			}
		}	
		boolean on = this.isOn();
		if ((this.chargedPrevTick != this.chargedLastTick) || ((this.worldObj.getTotalWorldTime() % (on ? 2L : 5L)) == 0L)) {
			//worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, ModPhotonicCraft.microwave, 0);
    		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        	this.markDirty();
			//worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}
		if (on && (((this.neededprogress - this.progress) % 20L) == 0)) {
			this.worldObj.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, this.progress < 1 ? "photoniccraft:microwave.start" : "photoniccraft:microwave.ambient", 0.5F, 1.0F);
		}
		if (on) {
			this.energy.setEnergyStored(this.energy.getEnergyStored() - 15);
			this.progress++;
			this.angle+=6;
			if (this.angle>90) {
				this.angle-=180;
			}
			if (this.progress >= this.neededprogress && !this.worldObj.isRemote) {
				this.worldObj.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "photoniccraft:microwave.beep", 1.5F, 1.0F);
				ItemStack c = chargedItemSlot.copy();
				c.stackSize = 1;
				ItemStack i = FurnaceRecipes.smelting().getSmeltingResult(c);
				i.stackSize *= chargedItemSlot.stackSize;
				if (i.stackSize < 1)
					i.stackSize = chargedItemSlot.stackSize;
				while (i.stackSize > this.getInventoryStackLimit()) {
					ItemStack i2 = i.copy();
					i2.stackSize = i.stackSize - this.getInventoryStackLimit();
					i.stackSize -= this.getInventoryStackLimit();
					EntityItem entityitem = new EntityItem(worldObj, (double)((float)this.xCoord + .5), (double)((float)this.yCoord + .5 ), (double)((float)this.zCoord + .5), i2);

	                if (i2.hasTagCompound())
	                {
	                    entityitem.getEntityItem().setTagCompound((NBTTagCompound)i2.getTagCompound().copy());
	                }

	                float f3 = 0.05F;
	                entityitem.motionX = (double)((float)PhotonicAPI.rand.nextGaussian() * f3);
	                entityitem.motionY = (double)((float)PhotonicAPI.rand.nextGaussian() * f3 + 0.2F);
	                entityitem.motionZ = (double)((float)PhotonicAPI.rand.nextGaussian() * f3);
	                
	                this.worldObj.spawnEntityInWorld(entityitem);
				}
				chargedItemSlot = i;
				this.progress = 0;
		        if (this.chargedItemSlot == null)
		        	this.entItem = null;
		        else
		        	this.entItem = new EntityItem(Minecraft.getMinecraft().theWorld, 0D, 0D, 0D, this.chargedItemSlot);
			}
		} else {
			this.progress = 0;
		}
    }
    public boolean isOn() {
    	return this.chargedLastTick && this.energy.getEnergyStored() >= 15; 
    }
    public int progress = 0;
    public int neededprogress = 9999;
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
    public void validate() {
    	super.validate();
        
        this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, ModPhotonicCraft.microwave, 10);
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
        syncData.setInteger("progress", progress);
        syncData.setInteger("neededprogress", neededprogress);
        syncData.setInteger("energy", this.energy.getEnergyStored());
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
        this.progress = func_148857_g.getInteger("progress");
        this.neededprogress = func_148857_g.getInteger("neededprogress");
        this.energy.setEnergyStored(func_148857_g.getInteger("energy"));
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
			this.progress = 0;
	        int a = 0;
	        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit())
	        {
	            p_70299_2_.stackSize = this.getInventoryStackLimit();
	        }
	        if (this.chargedItemSlot == null)
	        	this.entItem = null;
	        else
	        	this.entItem = new EntityItem(Minecraft.getMinecraft().theWorld, 0D, 0D, 0D, this.chargedItemSlot);
    		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        	this.markDirty();
		}
	}
	@Override
	public int getInventoryStackLimit() { 
		return 64; 
	} 

	@Override
	public String getInventoryName() {
		return ModPhotonicCraft.MODID + "_container.microwaveOven";
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
	EnergyStorage energy = new EnergyStorage(1000);
	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) {
		return arg0 != ForgeDirection.UP;
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

}
