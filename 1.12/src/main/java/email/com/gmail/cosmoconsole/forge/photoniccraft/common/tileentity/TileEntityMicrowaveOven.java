package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockFloodlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.itemhandler.ItemStackHandlerMicrowave;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EnergyContainer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMicrowaveOven extends TileEntity implements ITickable {
	public static final int MAX_RF_CAPACITY = 1000;

	public static boolean canSmelt(ItemStack stackInSlot) {
		return !FurnaceRecipes.instance().getSmeltingResult(stackInSlot).isEmpty();
	}

	static boolean canAlsoSmeltSmeltingResultOf(ItemStack is) {
		if (!canSmelt(is))
			return false;
		return canSmelt(FurnaceRecipes.instance().getSmeltingResult(is));
	}

	public boolean chargedPrevTick = false;
	public boolean chargedLastTick = false;
	public double angle = 0.0;
	public EntityItem entItem = null;
	public boolean okToUse = true;
	private boolean lastOn = false;

	public ItemStackHandler inv;

	private long lastTick = 0;

	public int progress = 0;

	public int neededprogress = 9999;

	EnergyContainer festorage;

	public TileEntityMicrowaveOven() {
		super();
		this.inv = new ItemStackHandlerMicrowave(1, this);
		this.okToUse = true;
		this.festorage = new EnergyContainer(MAX_RF_CAPACITY);
	}

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
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inv);
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

	public int getInventoryStackLimit() {
		return 64;
	}

	public IItemHandler getInvSlot() {
		return this.inv;
	}

	public int getTrueEnergyStored() {
		return Math.min(MAX_RF_CAPACITY, this.festorage.getEnergyStored());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY
				|| super.hasCapability(capability, facing);
	}

	public boolean isOn() {
		return this.chargedLastTick && this.getTrueEnergyStored() >= 15 && canSmelt(this.inv.getStackInSlot(0));
	}

	public boolean isUsableByPlayer(EntityPlayer p_70300_1_) {
		return this.getWorld().getTileEntity(this.pos) != this ? false
				: p_70300_1_.getDistanceSq(this.xCoord() + 0.5D, this.yCoord() + 0.5D, this.zCoord() + 0.5D) <= 64.0D;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return (oldState.getBlock() != newSate.getBlock());
	}

	@Override
	public void update() {
		long newTick = this.getWorld().getTotalWorldTime();
		if (newTick == lastTick)
			return;
		lastTick = newTick;
		this.chargedPrevTick = this.chargedLastTick;
		this.chargedLastTick = false;
		if (this.entItem == null && !this.inv.getStackInSlot(0).isEmpty())
			this.entItem = new EntityItem(getWorld(), 0D, 0D, 0D, this.inv.getStackInSlot(0));
		else if (this.entItem != null && this.inv.getStackInSlot(0).isEmpty())
			this.entItem = null;
		if (!this.inv.getStackInSlot(0).isEmpty()) {
			this.chargedLastTick = false;

			ItemStack i = FurnaceRecipes.instance().getSmeltingResult(this.inv.getStackInSlot(0));
			if (!i.isEmpty()) {
				this.chargedLastTick = (this.inv.getStackInSlot(0).getItem() instanceof ItemFood)
						|| (i.getItem() instanceof ItemFood);
				if (this.chargedLastTick)
					this.neededprogress = this.inv.getStackInSlot(0).getCount() * 20;
			}
		}
		boolean on = this.isOn() && (this.okToUse || this.getWorld().isRemote);
		if (this.lastOn != on) {
			this.world.setBlockState(pos, this.world.getBlockState(pos).withProperty(BlockFloodlight.powered, on));
		}
		this.lastOn = on;
		if ((this.chargedPrevTick != this.chargedLastTick)
				|| ((this.getWorld().getTotalWorldTime() % (on ? 2L : 5L)) == 0L)) {
			this.getWorld().notifyBlockUpdate(this.pos, this.world.getBlockState(pos), this.world.getBlockState(pos),
					3);
			this.markDirty();
		}
		if (on && (((this.neededprogress - this.progress) % 20L) == 0)) {
			if (this.getWorld().isRemote) {
				this.getWorld().playSound(this.xCoord() + 0.5D, this.yCoord() + 0.5D, this.zCoord() + 0.5D,
						SoundEvent.REGISTRY.getObject(new ResourceLocation(this.progress < 1
								? "photoniccraft:microwave.start" : "photoniccraft:microwave.ambient")),
						SoundCategory.BLOCKS, 1.5F, 1.0F, true);
				if (this.progress < 1)
					++this.progress;
			}
		}
		if (on) {
			this.festorage.extractEnergy(15, false);
			this.progress++;
			this.angle += 6;
			if (this.angle > 180) {
				this.angle -= 360;
			}
			if (this.progress >= this.neededprogress) {
				if (this.getWorld().isRemote && !canAlsoSmeltSmeltingResultOf(this.inv.getStackInSlot(0)))
					this.getWorld().playSound(this.xCoord() + 0.5D, this.yCoord() + 0.5D, this.zCoord() + 0.5D,
							SoundEvent.REGISTRY.getObject(new ResourceLocation("photoniccraft:microwave.beep")),
							SoundCategory.BLOCKS, 3.0F, 1.0F, true);
				if (!this.getWorld().isRemote) {
					ItemStack c = this.inv.getStackInSlot(0).copy();
					if (this.inv.getStackInSlot(0).getCount() < 1 || c.getCount() < 1) {
						return;
					}
					c.setCount(1);
					this.okToUse = false;
					ItemStack i = FurnaceRecipes.instance().getSmeltingResult(c).copy();
					i.setCount(i.getCount() * this.inv.getStackInSlot(0).getCount());
					while (i.getCount() > this.inv.getSlotLimit(0)) {
						ItemStack i2 = i.copy();
						i2.setCount(i.getCount() - this.inv.getSlotLimit(0));
						i.setCount(i.getCount() - this.inv.getSlotLimit(0));
						EntityItem entityitem = new EntityItem(getWorld(), this.xCoord() + .5, this.yCoord() + .5,
								this.zCoord() + .5, i2);

						if (i2.hasTagCompound()) {
							entityitem.getItem().setTagCompound(i2.getTagCompound().copy());
						}

						float f3 = 0.05F;
						entityitem.motionX = (float) PhotonicUtils.rand.nextGaussian() * f3;
						entityitem.motionY = (float) PhotonicUtils.rand.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float) PhotonicUtils.rand.nextGaussian() * f3;

						this.getWorld().spawnEntity(entityitem);
					}
					this.inv.setStackInSlot(0, i);
					if (i.isEmpty())
						this.entItem = null;
					else
						this.entItem = new EntityItem(getWorld(), 0D, 0D, 0D, i);
					this.progress = 0;
					this.getWorld().scheduleBlockUpdate(this.pos, this.blockType, 1, 10);
					this.getWorld().notifyBlockUpdate(this.pos, this.world.getBlockState(pos),
							this.world.getBlockState(pos), 3);
					this.markDirty();
				}
			}
		} else {
			this.progress = 0;
		}
	}

	@Override
	public void validate() {
		super.validate();

		this.getWorld().scheduleBlockUpdate(this.pos, PhotonicBlocks.microwave, 10, 2);
	}

	private void writeSyncableDataToNBT(NBTTagCompound syncData) {
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();

		syncData.setTag("Item", this.inv.serializeNBT());
		syncData.setInteger("progress", progress);
		syncData.setInteger("neededprogress", neededprogress);
		syncData.setInteger("fenergy", this.festorage.getEnergyStored());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		NBTTagList nbttaglist = new NBTTagList();

		writeSyncableDataToNBT(p_145841_1_);
		p_145841_1_.setTag("Item", this.inv.serializeNBT());

		return super.writeToNBT(p_145841_1_);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);

		readSyncableDataFromNBT(p_145839_1_);
		this.inv.deserializeNBT(p_145839_1_.getCompoundTag("Item"));
	}

	private void readSyncableDataFromNBT(NBTTagCompound func_148857_g) {
		this.inv.deserializeNBT(func_148857_g.getCompoundTag("Item"));
		this.progress = func_148857_g.getInteger("progress");
		this.neededprogress = func_148857_g.getInteger("neededprogress");
		this.festorage.setEnergyStored(func_148857_g.getInteger("fenergy"));
		this.entItem = null;
		if (this.inv.getStackInSlot(0).getCount() > 0)
			this.entItem = new EntityItem(getWorld(), 0D, 0D, 0D, this.inv.getStackInSlot(0));
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

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		readSyncableDataFromNBT(tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readSyncableDataFromNBT(pkt.getNbtCompound());
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
