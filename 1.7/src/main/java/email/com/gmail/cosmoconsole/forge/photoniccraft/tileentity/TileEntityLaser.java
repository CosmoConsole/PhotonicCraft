package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.LaserDirection;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicAPI;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserBeam;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityLaser extends TileEntity implements ISidedInventory, IEnergyReceiver
{
    private ItemStack[] laserItemStacks = new ItemStack[3];
    private String field_145958_o;
	int currentItemBurnTime;
    int laserBurnTime;
	boolean powered;

    public TileEntityLaser() {
    	super();
    	this.lastMet = -1;
    	this.lastPower = 0;
    	this.storage = new EnergyStorage(40000);
    }
    
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory()
    {
        return this.laserItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return this.laserItemStacks[p_70301_1_];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        if (this.laserItemStacks[p_70298_1_] != null)
        {
            ItemStack itemstack;

            if (this.laserItemStacks[p_70298_1_].stackSize <= p_70298_2_)
            {
                itemstack = this.laserItemStacks[p_70298_1_];
                this.laserItemStacks[p_70298_1_] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.laserItemStacks[p_70298_1_].splitStack(p_70298_2_);

                if (this.laserItemStacks[p_70298_1_].stackSize == 0)
                {
                    this.laserItemStacks[p_70298_1_] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_)
    {
        if (this.laserItemStacks[p_70304_1_] != null)
        {
            ItemStack itemstack = this.laserItemStacks[p_70304_1_];
            this.laserItemStacks[p_70304_1_] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {

        int a = 0;
        if (p_70299_2_ != null && (
        		((p_70299_2_.stackSize + this.getInvAmount(p_70299_1_)) > 1) && (p_70299_1_ > 0)))
        {
        	a = (p_70299_2_.stackSize + this.getInvAmount(p_70299_1_)) - 1;
        	this.laserItemStacks[p_70299_1_].stackSize = 1;
        }
        this.laserItemStacks[p_70299_1_] = p_70299_2_;
        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit())
        {
        	a = p_70299_2_.stackSize - this.getInventoryStackLimit();
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
        if (a > 0) {
        	Random r = new Random();
        	ItemStack itemstack = p_70299_2_;
            float f = r.nextFloat() * 0.8F + 0.1F;
            float f1 = r.nextFloat() * 0.8F + 0.1F;
            float f2 = r.nextFloat() * 0.8F + 0.1F;

            while (a > 0)
            {
                int j1 = r.nextInt(21) + 10;

                if (j1 > a)
                {
                    j1 = a;
                }

                a -= j1;
                EntityItem entityitem = new EntityItem(worldObj, (double)((float)this.xCoord + .5 + f), (double)((float)this.yCoord + .5 + f1), (double)((float)this.zCoord + .5 + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                if (itemstack.hasTagCompound())
                {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                }

                float f3 = 0.05F;
                entityitem.motionX = (double)((float)r.nextGaussian() * f3);
                entityitem.motionY = (double)((float)r.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double)((float)r.nextGaussian() * f3);
                //worldObj.spawnEntityInWorld(entityitem);
                dropItems.add(entityitem);
            }
        }
    }
    ArrayList<Entity> dropItems = new ArrayList<Entity>();
    /**
     * Returns the name of the inventory
     */
    @Override
    public String getInventoryName()
    {
        return ModPhotonicCraft.MODID + "_container.laser";
    }

    /**
     * Returns if the inventory is named
     */
    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    
    public void func_145951_a(String p_145951_1_)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
        this.laserItemStacks = new ItemStack[this.getSizeInventory()];

        readSyncableDataFromNBT(p_145839_1_);
        
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.laserItemStacks.length)
            {
                this.laserItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.lastMet = p_145839_1_.getInteger("lastMetadata");
        if (!p_145839_1_.hasKey("lastMetadata")) this.lastMet = -1;
		this.lastPower = p_145839_1_.getInteger("lastPower");
        this.laserBurnTime = p_145839_1_.getInteger("laserFuelLeft");
        this.currentItemBurnTime = p_145839_1_.getInteger("laserFuelOrig");
        this.powered = p_145839_1_.getBoolean("powered");
        this.puny = p_145839_1_.getBoolean("puny");
        this.storage.setEnergyStored(p_145839_1_.getInteger("storage"));
    }
    @Override
    public void validate() {
    	super.validate();
        
        this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, ModPhotonicCraft.laser, 10);
    }
    boolean puny = false;
    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        NBTTagList nbttaglist = new NBTTagList();

        writeSyncableDataToNBT(p_145841_1_);
        
        for (int i = 0; i < this.laserItemStacks.length; ++i)
        {
            if (this.laserItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.laserItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        p_145841_1_.setTag("Items", nbttaglist);
        p_145841_1_.setInteger("laserFuelLeft", this.laserBurnTime);
        p_145841_1_.setInteger("laserFuelOrig", this.currentItemBurnTime);
        p_145841_1_.setInteger("lastMetadata", this.lastMet);
        p_145841_1_.setInteger("lastPower", this.lastPower);
        p_145841_1_.setBoolean("powered", this.powered);
        p_145841_1_.setBoolean("puny", puny);
        p_145841_1_.setInteger("storage", this.storage.getEnergyStored());
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    
    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_70300_1_.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
    {
        return p_94041_1_ == 0 ? TileEntityFurnace.isItemFuel(p_94041_2_) : (p_94041_1_ == 1 ? (p_94041_2_.getItem() == ModPhotonicCraft.photonicResource && (ModPhotonicCraft.isInside(p_94041_2_.getItemDamage(),8,11) || p_94041_2_.getItemDamage() == 14)) : (p_94041_1_ == 2 ? (p_94041_2_.getItem() == ModPhotonicCraft.photonicCoupler) : true));
    }

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return new int[]{};
	}

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
	@Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_)
    {
        return false;
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
	@Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_)
    {
        return false;
    }
    long lastFiredTick = 0L;
    boolean shouldUpdate = false;
    @Override
    public void updateEntity() {
    	long nowTick = this.worldObj.getTotalWorldTime();
    	if (nowTick == lastFiredTick)
    		return;
    	lastFiredTick = nowTick;
    	handleFuel();
    	if (!worldObj.isRemote) {
    		Iterator<Entity> it = dropItems.iterator();
    		while (it.hasNext()) {
    			Entity entityitem = it.next();
        		worldObj.spawnEntityInWorld(entityitem);
        		it.remove();
    		}
    	}
    	if (shouldUpdate && this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord).getLightValue() == 0) {
    		shouldUpdate = false;
    		updateLights(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    	}
    	if (this.worldObj.getTotalWorldTime() % 5L == 0L)
    		updatePower();
        if (this.isFiring()) {
            fireLaserBeam();
        } else {
			if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
				for (EntityLaserBeam l: last)
					l.setDead();
        }
        if (laststate && !newstate)
    		shouldUpdate = true;
        if (!laststate && newstate)
        	updateLights(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        laststate = newstate;
    }
    public void updateLights(World w, int x, int y, int z) {
    	return;/*
        w.markBlockRangeForRenderUpdate((int)x, (int)y, (int)x, 12, 12, 12);
        w.markBlockForUpdate((int)x, (int)y, (int)z);
        w.updateLightByType(EnumSkyBlock.Block, (int)x, (int)y +1, (int)z);
        w.updateLightByType(EnumSkyBlock.Block, (int)x +1, (int)y +1, (int)z);
        w.updateLightByType(EnumSkyBlock.Block, (int)x +1, (int)y +1, (int)z +1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x +1, (int)y +1, (int)z -1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x -1, (int)y +1, (int)z +1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x -1, (int)y +1, (int)z -1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x -1, (int)y +1, (int)z);
        w.updateLightByType(EnumSkyBlock.Block, (int)x, (int)y +1, (int)z +1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x, (int)y +1, (int)z -1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x, (int)y -1, (int)z);
        w.updateLightByType(EnumSkyBlock.Block, (int)x +1, (int)y -1, (int)z);
        w.updateLightByType(EnumSkyBlock.Block, (int)x +1, (int)y -1, (int)z +1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x +1, (int)y -1, (int)z -1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x -1, (int)y -1, (int)z +1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x -1, (int)y -1, (int)z -1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x -1, (int)y -1, (int)z);
        w.updateLightByType(EnumSkyBlock.Block, (int)x, (int)y -1, (int)z +1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x, (int)y -1, (int)z -1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x +1, (int)y, (int)z);
        w.updateLightByType(EnumSkyBlock.Block, (int)x +1, (int)y, (int)z +1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x +1, (int)y, (int)z -1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x -1, (int)y, (int)z +1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x -1, (int)y, (int)z -1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x -1, (int)y, (int)z);
        w.updateLightByType(EnumSkyBlock.Block, (int)x, (int)y, (int)z +1);
        w.updateLightByType(EnumSkyBlock.Block, (int)x, (int)y, (int)z -1);*/
	}
	private final int FUEL_SCALAR = 2;
    private boolean laststate = false;
    private boolean newstate = false;
    @SideOnly(Side.CLIENT)
    public int getFuelTimeRemainingScaled(int p_145955_1_)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200 * FUEL_SCALAR;
        }
        return maxInt(0, this.laserBurnTime) * p_145955_1_ / this.currentItemBurnTime;
    }
    
    EntityLaserBeam[] last = null;
    
    public void updatePower() {
    	powered = this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
    }
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        this.writeSyncableDataToNBT(syncData);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }
    
    private void writeSyncableDataToNBT(NBTTagCompound syncData) {
		syncData.setInteger("laserTime", laserBurnTime);
		syncData.setInteger("lastMetadata", this.lastMet);
		syncData.setInteger("lastPower", this.lastPower);
		syncData.setInteger("storage", this.storage.getEnergyStored());
		syncData.setBoolean("puny", this.puny);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        if (this.laserItemStacks[0] != null) {
			this.laserItemStacks[0].writeToNBT(nbttagcompound1);
			syncData.setTag("fuelStack", nbttagcompound1);
        }
	}

	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readSyncableDataFromNBT(pkt.func_148857_g());
    }
	private void readSyncableDataFromNBT(NBTTagCompound func_148857_g) {
		laserBurnTime = func_148857_g.getInteger("laserTime");
		lastMet = func_148857_g.getInteger("lastMetadata");
        if (!func_148857_g.hasKey("lastMetadata")) this.lastMet = -1;
		lastPower = func_148857_g.getInteger("lastPower");
		puny = func_148857_g.getBoolean("puny");
		this.storage.setEnergyStored(func_148857_g.getInteger("storage"));
        if (func_148857_g.hasKey("fuelStack")) {
	        NBTTagCompound nbttagcompound1 = func_148857_g.getCompoundTag("fuelStack");
	        if (this.laserItemStacks[0] == null)
	        	this.laserItemStacks[0] = new ItemStack(Blocks.air, 1, 0);
			this.laserItemStacks[0].readFromNBT(nbttagcompound1);
        }
	}
	int lastMet = -1;
	private void fireLaserBeam() {
		if (!isPoweredByRF())
			if ((this.laserItemStacks[0] == null || this.laserItemStacks[0].stackSize < 1) && (this.laserBurnTime == 0)) {
				if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
					for (EntityLaserBeam l: last)
						l.setDead();
				return;
			}
		int pow = this.getRawPower();
		if (pow < 0 && this.blockMetadata < 0) pow = lastPower;
		lastPower = pow;
		if (pow < 1) {
			if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
				for (EntityLaserBeam l: last)
					l.setDead();
			return;
		}
		int met = -1;
		if (laserItemStacks[2] != null)
			if (laserItemStacks[2].getItem() == ModPhotonicCraft.photonicCoupler)
				met = laserItemStacks[2].getItemDamage();
		if (met < 0 && this.blockMetadata < 0) met = lastMet;
		if (met < 0) {
			if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
				for (EntityLaserBeam l: last)
					l.setDead();
			return;
		}
		lastMet = met;
		int col = PhotonicAPI.getRGBFromMeta(met);
		if (col == 0) return;
		newstate = true;
		int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		EntityLaserBeam[] e = shootBeam(this.worldObj, this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F, (new LaserDirection[]{LaserDirection.DOWN, LaserDirection.UP, LaserDirection.NORTH, LaserDirection.SOUTH, LaserDirection.WEST, LaserDirection.EAST})[meta % 6], col, pow);
		if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
			for (EntityLaserBeam l: last)
				l.setTicks(3);
		last = e;
	}
	public void terminateLaser() {
		if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
			for (EntityLaserBeam l: last)
				l.setTicks(3);
	}
	private EntityLaserBeam[] shootBeam(World worldObj, float x, float y, float z,
			LaserDirection laserDirection, int laserColor, int laserPower) {
		switch (laserDirection) {
		case DOWN: y-=.4f;
			break;
		case EAST: x+=.4f;
			break;
		case NORTH:z-=.4f;
			break;
		case SOUTH:z+=.4f;
			break;
		case UP:   y+=.4f;
			break;
		case WEST: x-=.4f;
			break;
		default:
			break;
		}
		return PhotonicAPI.shootBeam(worldObj, x, y, z, laserDirection, laserColor, laserPower, 256.0f, isPuny(), -1);
	}

	private boolean isPuny() {
		if (this.blockMetadata >= 0)
			if (laserItemStacks[1] != null)
				puny = laserItemStacks[1].getItemDamage() == 14;
			else
				puny = false;
		return puny;
	}

	public boolean isPoweredByRF() {
		/*if (this.worldObj.getTotalWorldTime() % 40L == 0) {
			System.out.println("Remote: " + this.worldObj.isRemote + ", RF: " + storage.getEnergyStored());
		}*/
		int sum = (RF_PER_CD * maxInt(getRawPower(),0));
		return maxInt(getRawPower(),0) > 0 && storage.getEnergyStored() >= sum;
	}

	public String getPower() {
		return String.format("%d",maxInt(getRawPower(),0));
	}
	
	private int maxInt(int a, int b) {
		return a>b?a:b;
	}
	int lastPower = 0;

	private int getRawPower() {
		if (laserItemStacks[1] == null) return -1;
		if (laserItemStacks[1].getItem() == ModPhotonicCraft.photonicResource) {
			switch (laserItemStacks[1].getItemDamage()) {
			case 4: return 1;
			case 5: return 5;
			case 6: return 20;
			case 7: return 100;
			case 14: return 1;
			default: break;
			}
		}
		return 0;
	}
	private int getInvAmount(int i) {
		if (laserItemStacks[i] == null) return 0;
		return laserItemStacks[i].stackSize;
	}
	public final int RF_PER_CD = 20;
	public String getRFUsage() {
		return String.format("%.2f",(double)maxInt(getRawPower(),0) * RF_PER_CD);
	}

	public boolean isFiring() {
		return powered;
	}
	@Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }
	private void handleFuel() {
		boolean flag = this.laserBurnTime > 0;
        boolean flag1 = false;

        if (!this.worldObj.isRemote && this.isPoweredByRF() && this.isFiring()) {
    		int pow = this.getRawPower();
    		if (pow < 0 && this.blockMetadata < 0) pow = lastPower;
    		lastPower = pow;
    		int val = pow * RF_PER_CD;
        	energy -= val;
        	storage.setEnergyStored(storage.getEnergyStored() - val);
    		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        	this.markDirty();
        	return;
        }
        if (this.laserBurnTime > 0)
        {
    		int pow = this.getRawPower();
    		if (pow < 0 && this.blockMetadata < 0) pow = lastPower;
    		lastPower = pow;
            this.laserBurnTime -= maxInt(1, pow);
        }

        if (!this.worldObj.isRemote)
        {
            if (this.laserBurnTime <= 0 && newstate && this.isFiring() && !this.isPoweredByRF())
            {
                this.currentItemBurnTime = this.laserBurnTime = TileEntityFurnace.getItemBurnTime(this.laserItemStacks[0]) * FUEL_SCALAR / 2;

                if (this.laserBurnTime > 0) {

                    if (this.laserItemStacks[0] != null)
                    {
                        flag1 = true;
                        --this.laserItemStacks[0].stackSize;

                        if (this.laserItemStacks[0].stackSize == 0)
                        {
                            this.laserItemStacks[0] = laserItemStacks[0].getItem().getContainerItem(laserItemStacks[0]);
                        }
                    }
                }
            }
        }

        if (flag1) {
            this.markDirty();
        } 
        if (laserBurnTime < 0)
        	laserBurnTime = 0;
	}

	public boolean isFuelBurning() {
	    return this.laserBurnTime > 0;
	}
	EnergyStorage storage;
	public int energy = 0;
	
	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) {
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection arg0) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection arg0) {
		return storage.getMaxEnergyStored();
	}

	@Override
	public int receiveEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		int result = storage.receiveEnergy(arg1, arg2);
		energy += result;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		this.markDirty();
		return result;
	}

}