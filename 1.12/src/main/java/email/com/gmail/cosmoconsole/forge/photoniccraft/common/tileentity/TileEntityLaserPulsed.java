package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import java.util.Iterator;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EnergyContainer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityLaserPulsed extends TileEntityLaser {
	public static final int MAX_RF_CAPACITY = 1000000;
	public static final int MAX_RF_CAPACITY_LG = String.valueOf(MAX_RF_CAPACITY).length();
	EnergyContainer festorage;

	public final int RF_PER_CD = 200;
	public final int RF_PER_FT = 10;
	protected final int FUEL_SCALAR = 1;

	protected boolean lastpowered = false;

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the
	 * furnace burning, or 0 if the item isn't fuel
	 */

	public TileEntityLaserPulsed() {
		super();
		this.festorage = new EnergyContainer(MAX_RF_CAPACITY);
	}

	private void fireLaserBeam(int pow) {
		if (!isPoweredByRF())
			if ((this.fuel.getStackInSlot(0).isEmpty() || this.fuel.getStackInSlot(0).getCount() < 1)
					&& (this.laserBurnTime == 0)) {
				return;
			}
		lastpowered = powered;
		if (pow < 0 && this.getBlockMetadata() < 0)
			pow = lastPower;
		lastPower = pow;
		if (pow < 1) {
			return;
		}
		int met = -1;
		if (!this.coupler.getStackInSlot(0).isEmpty())
			if (this.coupler.getStackInSlot(0).getItem() == PhotonicItems.photonicCoupler)
				met = coupler.getStackInSlot(0).getItemDamage();
		if (met < 0 && this.getBlockMetadata() < 0)
			met = lastMet;
		if (met < 0) {
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
		for (EntityLaserBeam l : e) {
			l.setTicks(6);
			l.setFadeout(true);
			world.spawnEntity(l);
		}
		last = e;
	}

	@Override
	public void forceUpdate() {
		if (this.pos == null || this.world == null || this.world.getBlockState(pos) == null)
			return;
		this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(pos), this.world.getBlockState(pos), 3);
		this.markDirty();
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

	@Override
	public ItemStackHandler getCoupler() {
		return this.coupler;
	}

	@Override
	public ItemStackHandler getFuel() {
		return this.fuel;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getFuelTimeRemainingScaled(int p_145955_1_) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = 200 * FUEL_SCALAR;
		}
		return maxInt(0, this.laserBurnTime) * p_145955_1_ / this.currentItemBurnTime;
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 65536.0D;
	}

	@Override
	public ItemStackHandler getMedium() {
		return this.medium;
	}

	@Override
	protected int getRawPower() {
		ItemStack mi = this.medium.getStackInSlot(0);
		if (mi == null)
			return -1;
		if (mi.isEmpty())
			return -1;
		if (mi.getItem() == PhotonicItems.photonicResources[4]) {
			return 4;
		}
		if (mi.getItem() == PhotonicItems.photonicResources[5]) {
			return 20;
		}
		if (mi.getItem() == PhotonicItems.photonicResources[6]) {
			return 100;
		}
		if (mi.getItem() == PhotonicItems.photonicResources[7]) {
			return 1000;
		}
		if (mi.getItem() == PhotonicItems.photonicResources[14]) {
			return 1;
		}
		return 0;
	}

	private int handleFuel() {
		boolean flag = this.laserBurnTime > 0;
		boolean flag1 = false;
		int okpower = this.getRawPower();

		if (!this.fuel.getStackInSlot(0).isEmpty()) {
			if (this.getTrueEnergyStored() < this.getMaximumPowerCapacity()) {
				int pow = this.getRawPower();
				if (pow < 0 && this.getBlockMetadata() < 0)
					pow = lastPower;
				lastPower = pow;
				if (this.laserBurnTime == 0) {
					this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.fuel.getStackInSlot(0))
							* FUEL_SCALAR / 2;
					this.laserBurnTime = this.currentItemBurnTime;

					if (this.currentItemBurnTime > 0) {

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
				int deposit = pow * (RF_PER_CD / RF_PER_FT);
				this.laserBurnTime -= maxInt(1, deposit);
				if (this.laserBurnTime < 0) {
					deposit += this.laserBurnTime;
					this.laserBurnTime = 0;
				}
				this.festorage.receiveEnergy(deposit * RF_PER_FT, false);
				if (flag1) {
					this.markDirty();
				}
			} else {
				if (this.laserBurnTime > 0)
					this.laserBurnTime--;
			}
		}
		if (this.isFiring() && this.isPoweredByRF()) {
			int pow = this.getRawPower();
			if (pow < 0 && this.getBlockMetadata() < 0)
				pow = lastPower;
			lastPower = pow;
			int val = pow * RF_PER_CD;
			if (!this.getWorld().isRemote) {
				energy -= val;
				okpower = festorage.extractEnergy(val, false);
			} else {
				okpower = minInt(festorage.getEnergyStored(), val);
			}
			this.getWorld().notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			this.markDirty();
			return okpower;
		}
		return 0;
	}

	private int minInt(int a, int b) {
		return (a < b) ? a : b;
	}

	@Override
	public boolean isPoweredByRF() {
		int sum = (RF_PER_CD * maxInt(getRawPower(), 0));
		return getRawPower() > 0 && getTrueEnergyStored() >= sum;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY
				|| super.hasCapability(capability, facing);
	}

	@Override
	public void terminateLaser() {
		if (last != null && last.length > 0 && last[0] instanceof EntityLaserBeam)
			for (EntityLaserBeam l : last)
				l.setTicks(2);
	}

	@Override
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

	@Override
	public String getPower() {
		return String.format("%d", maxInt(getRawPower(), 0));
	}

	@Override
	public boolean isFiring() {
		return powered && !lastpowered;
	}

	@Override
	public void update() {
		long nowTick = this.getWorld().getTotalWorldTime();
		if (nowTick == lastFiredTick)
			return;
		lastFiredTick = nowTick;
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
		int okp = handleFuel();
		if (this.isFiring()) {
			fireLaserBeam(okp);
		}
		if (laststate && !newstate)
			shouldUpdate = true;
		if (!laststate && newstate)
			updateLights(this.getWorld(), this.xCoord(), this.yCoord(), this.zCoord());
		laststate = newstate;
	}

	@Override
	public int getTrueEnergyStored() {
		return Math.min(MAX_RF_CAPACITY, this.festorage.getEnergyStored());
	}

	@Override
	public void updateLights(World w, int x, int y, int z) {
	}

	@Override
	public void updatePower() {
		powered = this.getWorld().isBlockIndirectlyGettingPowered(this.pos) > 0;
		if (!powered)
			lastpowered = false;
	}

	@Override
	public void validate() {
		super.validate();

		this.getWorld().scheduleBlockUpdate(this.pos, PhotonicBlocks.pulsedLaser, 10, 3);
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

	@Override
	protected void readSyncableDataFromNBT(NBTTagCompound func_148857_g) {
		super.readSyncableDataFromNBT(func_148857_g);
		this.lastpowered = func_148857_g.getBoolean("poweredlasttick");
		this.festorage.setEnergyStored(func_148857_g.getInteger("festorage"));
	}

	@Override
	protected void writeSyncableDataToNBT(NBTTagCompound syncData) {
		super.writeSyncableDataToNBT(syncData);
		syncData.setInteger("festorage", this.festorage.getEnergyStored());
		syncData.setBoolean("poweredlasttick", this.lastpowered);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		NBTTagCompound nbt = super.writeToNBT(p_145841_1_);
		writeSyncableDataToNBT(p_145841_1_);
		nbt.setInteger("festorage", this.festorage.getEnergyStored());
		nbt.setBoolean("poweredlasttick", this.lastpowered);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);

		readSyncableDataFromNBT(p_145839_1_);
		this.lastpowered = p_145839_1_.getBoolean("poweredlasttick");
		this.festorage.setEnergyStored(p_145839_1_.getInteger("festorage"));
	}

	@Override
	public int getMaximumPowerCapacity() {
		return MAX_RF_CAPACITY;
	}

	@Override
	public int getLogOfMaximumPowerCapacity() {
		return MAX_RF_CAPACITY_LG;
	}

	@Override
	public String getRFUsage() {
		return String.format("%.2f", (double) maxInt(getRawPower(), 0) * RF_PER_CD) + " RF";
	}

	@Override
	public String getName() {
		return "pulsedlaser";
	}
}