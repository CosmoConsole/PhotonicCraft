package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.itemhandler.ItemStackHandlerCoupler;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.itemhandler.ItemStackHandlerFuel;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.itemhandler.ItemStackHandlerMedium;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EnergyContainerDynamic;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityLaser extends TileEntity implements ITickable {
	public static final int MAX_RF_CAPACITY = 40000;
	public static final int MAX_RF_CAPACITY_LG = String.valueOf(MAX_RF_CAPACITY).length();
	EnergyContainerDynamic festorage;
	protected ItemStackHandler fuel;
	protected ItemStackHandler medium;
	protected ItemStackHandler coupler;
	protected String field_145958_o;
	public int currentItemBurnTime;
	public int laserBurnTime;
	public boolean powered;

	ArrayList<Entity> dropItems = new ArrayList<Entity>();

	boolean puny = false;

	long lastFiredTick = 0L;

	boolean shouldUpdate = false;

	protected final int FUEL_SCALAR = 2;

	protected boolean laststate = false;

	protected boolean newstate = false;

	EntityLaserBeam[] last = null;

	int lastMet = -1;

	int lastPower = 0;

	public final static int RF_PER_CD = 20;

	public int energy = 0;

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the
	 * furnace burning, or 0 if the item isn't fuel
	 */

	public TileEntityLaser() {
		super();
		this.lastMet = -1;
		this.lastPower = 0;
		this.festorage = new EnergyContainerDynamic(this.getMaximumPowerCapacity());
		this.fuel = new ItemStackHandlerFuel(1, this);
		this.medium = new ItemStackHandlerMedium(1, this);
		this.coupler = new ItemStackHandlerCoupler(1);
	}

	public void disableFiring() {
		this.powered = false;
	}

	private void fireLaserBeam() {
		if (!isPoweredByRF())
			if ((this.fuel.getStackInSlot(0).isEmpty() || this.fuel.getStackInSlot(0).getCount() < 1)
					&& (this.laserBurnTime == 0)) {
				if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
					for (EntityLaserBeam l : last)
						l.setDead();
				return;
			}
		int pow = this.getRawPower();
		if (pow < 0 && this.getBlockMetadata() < 0)
			pow = lastPower;
		lastPower = pow;
		if (pow < 1) {
			if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
				for (EntityLaserBeam l : last)
					l.setDead();
			return;
		}
		int met = -1;
		if (!this.coupler.getStackInSlot(0).isEmpty())
			if (this.coupler.getStackInSlot(0).getItem() == PhotonicItems.photonicCoupler)
				met = coupler.getStackInSlot(0).getItemDamage();
		if (met < 0 && this.getBlockMetadata() < 0)
			met = lastMet;
		if (met < 0) {
			if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
				for (EntityLaserBeam l : last)
					l.setDead();
			return;
		}
		lastMet = met;
		int col = PhotonicLaser.getRGBFromMeta(met);
		if (col == 0)
			return;
		newstate = true;
		EnumFacing thisDir = PhotonicUtils.readDirectionProperty(this.world.getBlockState(this.pos), BlockLaser.FACING);
		EntityLaserBeam[] e = shootBeam(this.getWorld(), this.xCoord() + 0.5F, this.yCoord() + 0.5F,
				this.zCoord() + 0.5F, thisDir, col, pow);
		if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
			for (EntityLaserBeam l : last)
				l.setTicks(3);
		for (EntityLaserBeam elb : e) {
			world.spawnEntity(elb);
		}
		last = e;
	}

	public void forceUpdate() {
		if (this.pos == null || this.world == null || this.world.getBlockState(pos) == null)
			return;
		this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(pos), this.world.getBlockState(pos), 3);
		this.markDirty();
		this.updateInput();
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (this.world != null) {
				;
				this.world.markChunkDirty(this.pos, this);
			}
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.fuel);
		}
		if (capability == CapabilityEnergy.ENERGY) {
			if (this.world != null) {
				;
				this.world.markChunkDirty(this.pos, this);
			}
			return CapabilityEnergy.ENERGY.cast(this.festorage);
		}
		return super.getCapability(capability, facing);
	}

	public ItemStackHandler getCoupler() {
		return this.coupler;
	}

	public ItemStackHandler getFuel() {
		return this.fuel;
	}

	@SideOnly(Side.CLIENT)
	public int getFuelTimeRemainingScaled(int p_145955_1_) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = 200 * FUEL_SCALAR;
		}
		return maxInt(0, this.laserBurnTime) * p_145955_1_ / this.currentItemBurnTime;
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 65536.0D;
	}

	public ItemStackHandler getMedium() {
		return this.medium;
	}

	public String getPower() {
		return String.format("%d", maxInt(getRawPower(), 0));
	}

	protected int getRawPower() {
		ItemStack mi = this.medium.getStackInSlot(0);
		if (mi == null)
			return -1;
		if (mi.isEmpty())
			return -1;
		if (mi.getItem() == PhotonicItems.photonicResources[4]) {
			return 1;
		}
		if (mi.getItem() == PhotonicItems.photonicResources[5]) {
			return 5;
		}
		if (mi.getItem() == PhotonicItems.photonicResources[6]) {
			return 20;
		}
		if (mi.getItem() == PhotonicItems.photonicResources[7]) {
			return 100;
		}
		if (mi.getItem() == PhotonicItems.photonicResources[14]) {
			return 1;
		}
		return 0;
	}

	public String getRFUsage() {
		return String.format("%.2f", (double) maxInt(getRawPower(), 0) * RF_PER_CD) + " RF/t";
	}

	public int getTrueEnergyStored() {
		return Math.min(MAX_RF_CAPACITY, this.festorage.getEnergyStored());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		writeSyncableDataToNBT(tag);
		return tag;
	}

	private void handleFuel() {
		boolean flag = this.laserBurnTime > 0;
		boolean flag1 = false;

		if (!this.getWorld().isRemote && this.isPoweredByRF() && this.isFiring()) {
			int pow = this.getRawPower();
			if (pow < 0 && this.getBlockMetadata() < 0)
				pow = lastPower;
			lastPower = pow;
			updateInput();
			int val = pow * RF_PER_CD;
			energy -= val;
			festorage.extractEnergy(val, false);
			this.getWorld().notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			this.markDirty();
			return;
		}
		if (this.laserBurnTime > 0) {
			int pow = this.getRawPower();
			if (pow < 0 && this.getBlockMetadata() < 0)
				pow = lastPower;
			lastPower = pow;
			this.laserBurnTime -= maxInt(1, pow);
		}

		if (!this.getWorld().isRemote) {
			if (this.laserBurnTime <= 0 && newstate && this.isFiring() && !this.isPoweredByRF()) {
				this.currentItemBurnTime = this.laserBurnTime = TileEntityFurnace
						.getItemBurnTime(this.fuel.getStackInSlot(0)) * FUEL_SCALAR / 2;

				if (this.laserBurnTime > 0) {

					if (!this.fuel.getStackInSlot(0).isEmpty()) {
						flag1 = true;
						this.fuel.extractItem(0, 1, false);

						if (this.fuel.getStackInSlot(0).isEmpty() || this.fuel.getStackInSlot(0).getCount() == 0) {
							this.fuel.setStackInSlot(0, this.fuel.getStackInSlot(0).getItem()
									.getContainerItem(this.fuel.getStackInSlot(0)));
						}
						this.forceUpdate();
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

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		readSyncableDataFromNBT(tag);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY
				|| super.hasCapability(capability, facing);
	}

	public boolean isFiring() {
		return powered;
	}

	public boolean isFuelBurning() {
		return this.laserBurnTime > 0;
	}

	public boolean isPoweredByRF() {
		int sum = (RF_PER_CD * maxInt(getRawPower(), 0));
		return getRawPower() > 0 && getTrueEnergyStored() >= sum;
	}

	protected boolean isPuny() {
		if (this.getBlockMetadata() >= 0)
			if (!this.medium.getStackInSlot(0).isEmpty())
				puny = this.medium.getStackInSlot(0).getItem().equals(PhotonicItems.photonicResources[14]);
			else
				puny = false;
		return puny;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	public boolean isUsableByPlayer(EntityPlayer p_70300_1_) {
		return this.getWorld().getTileEntity(this.pos) != this ? false
				: p_70300_1_.getDistanceSq(this.xCoord() + 0.5D, this.yCoord() + 0.5D, this.zCoord() + 0.5D) <= 64.0D;
	}

	protected int maxInt(int a, int b) {
		return a > b ? a : b;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readSyncableDataFromNBT(pkt.getNbtCompound());
	}

	protected EntityLaserBeam[] shootBeam(World worldObj, float x, float y, float z, EnumFacing laserDirection,
			int laserColor, int laserPower) {
		switch (laserDirection) {
		case DOWN:
			y -= .4f;
			break;
		case EAST:
			x += .4f;
			break;
		case NORTH:
			z -= .4f;
			break;
		case SOUTH:
			z += .4f;
			break;
		case UP:
			y += .4f;
			break;
		case WEST:
			x -= .4f;
			break;
		default:
			break;
		}
		return PhotonicLaser.shootBeam(getWorld(), x, y, z, laserDirection, laserColor, laserPower, 256.0f, isPuny(),
				-1);
	}

	public void terminateLaser() {
		if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
			for (EntityLaserBeam l : last)
				l.setTicks(2);
	}

	@Override
	public void update() {
		long nowTick = this.getWorld().getTotalWorldTime();
		if (nowTick == lastFiredTick)
			return;
		lastFiredTick = nowTick;
		handleFuel();
		if (!getWorld().isRemote) {
			Iterator<Entity> it = dropItems.iterator();
			while (it.hasNext()) {
				Entity entityitem = it.next();
				getWorld().spawnEntity(entityitem);
				it.remove();
			}
		}
		if (shouldUpdate && this.getWorld().getBlockState(this.pos).getBlock()
				.getLightValue(this.getWorld().getBlockState(this.pos), world, this.pos) == 0) {
			shouldUpdate = false;
			updateLights(this.getWorld(), this.xCoord(), this.yCoord(), this.zCoord());
		}
		if (getWorld().isRemote || this.getWorld().getTotalWorldTime() % 2L == 0L)
			updatePower();
		if (this.isFiring()) {
			fireLaserBeam();
		} else {
			if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
				for (EntityLaserBeam l : last)
					l.setDead();
		}
		if (laststate && !newstate)
			shouldUpdate = true;
		if (!laststate && newstate)
			updateLights(this.getWorld(), this.xCoord(), this.yCoord(), this.zCoord());
		laststate = newstate;
	}

	public void updateLights(World w, int x, int y, int z) {
	}

	public void updatePower() {
		powered = this.getWorld().isBlockIndirectlyGettingPowered(this.pos) > 0;
	}

	@Override
	public void validate() {
		super.validate();

		this.getWorld().scheduleBlockUpdate(this.pos, PhotonicBlocks.laser, 10, 3);
	}

	protected void readSyncableDataFromNBT(NBTTagCompound func_148857_g) {
		this.fuel.deserializeNBT(func_148857_g.getCompoundTag("fuelItem"));
		this.medium.deserializeNBT(func_148857_g.getCompoundTag("mediumItem"));
		this.coupler.deserializeNBT(func_148857_g.getCompoundTag("couplerItem"));
		this.lastMet = func_148857_g.getInteger("lastMetadata");
		if (!func_148857_g.hasKey("lastMetadata"))
			this.lastMet = -1;
		this.lastPower = func_148857_g.getInteger("lastPower");
		this.laserBurnTime = func_148857_g.getInteger("laserFuelLeft");
		this.currentItemBurnTime = func_148857_g.getInteger("laserFuelOrig");
		this.powered = func_148857_g.getBoolean("powered");
		this.puny = func_148857_g.getBoolean("puny");
		this.festorage.setEnergyStored(func_148857_g.getInteger("festorage"));
	}

	protected void writeSyncableDataToNBT(NBTTagCompound syncData) {
		syncData.setTag("fuelItem", this.fuel.serializeNBT());
		syncData.setTag("mediumItem", this.medium.serializeNBT());
		syncData.setTag("couplerItem", this.coupler.serializeNBT());
		syncData.setInteger("laserFuelLeft", this.laserBurnTime);
		syncData.setInteger("laserFuelOrig", this.currentItemBurnTime);
		syncData.setInteger("laserTime", laserBurnTime);
		syncData.setInteger("lastMetadata", this.lastMet);
		syncData.setInteger("lastPower", this.lastPower);
		syncData.setInteger("festorage", this.festorage.getEnergyStored());
		syncData.setBoolean("puny", this.puny);
		syncData.setBoolean("powered", this.powered);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		NBTTagList nbttaglist = new NBTTagList();

		writeSyncableDataToNBT(p_145841_1_);
		p_145841_1_.setTag("fuelItem", this.fuel.serializeNBT());
		p_145841_1_.setTag("mediumItem", this.medium.serializeNBT());
		p_145841_1_.setTag("couplerItem", this.coupler.serializeNBT());
		p_145841_1_.setInteger("laserFuelLeft", this.laserBurnTime);
		p_145841_1_.setInteger("laserFuelOrig", this.currentItemBurnTime);
		p_145841_1_.setInteger("lastMetadata", this.lastMet);
		p_145841_1_.setInteger("lastPower", this.lastPower);
		p_145841_1_.setBoolean("powered", this.powered);
		p_145841_1_.setBoolean("puny", puny);
		p_145841_1_.setInteger("festorage", this.festorage.getEnergyStored());
		return super.writeToNBT(p_145841_1_);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);

		readSyncableDataFromNBT(p_145839_1_);
		this.fuel.deserializeNBT(p_145839_1_.getCompoundTag("fuelItem"));
		this.medium.deserializeNBT(p_145839_1_.getCompoundTag("mediumItem"));
		this.coupler.deserializeNBT(p_145839_1_.getCompoundTag("couplerItem"));
		this.lastMet = p_145839_1_.getInteger("lastMetadata");
		if (!p_145839_1_.hasKey("lastMetadata"))
			this.lastMet = -1;
		this.lastPower = p_145839_1_.getInteger("lastPower");
		this.laserBurnTime = p_145839_1_.getInteger("laserFuelLeft");
		this.currentItemBurnTime = p_145839_1_.getInteger("laserFuelOrig");
		this.powered = p_145839_1_.getBoolean("powered");
		this.puny = p_145839_1_.getBoolean("puny");
		this.festorage.setEnergyStored(p_145839_1_.getInteger("festorage"));
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

	public int getMaximumPowerCapacity() {
		return MAX_RF_CAPACITY;
	}

	public int getLogOfMaximumPowerCapacity() {
		return MAX_RF_CAPACITY_LG;
	}

	public String getName() {
		return "laser";
	}

	public void updateInput() {
		this.festorage.setMaxReceive(Math.max(2 * this.getRawPower() * RF_PER_CD, 50));
	}

}