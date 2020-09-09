package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityLaserCharger extends TileEntity implements ITickable {
	public boolean chargedPrevTick = false;
	public boolean chargedLastTick = false;
	public ItemStackHandler inv = new ItemStackHandler(1);

	boolean puny = false;

	public int input = 0;

	public long lastAdded = 0L;

	public TileEntityLaserCharger() {
		super();
		this.inv = new ItemStackHandler(1);
	}

	public void addSignal(int laserPower) {
		if (getWorld().getTotalWorldTime() > lastAdded) {
			lastAdded = getWorld().getTotalWorldTime();
			input = 0;
		}
		input += 2 * laserPower;
		if (!this.inv.getStackInSlot(0).isEmpty()) {
			if (this.inv.getStackInSlot(0).hasCapability(CapabilityEnergy.ENERGY, null)) {
				IEnergyStorage storage = this.inv.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY, null);
				storage.receiveEnergy(input, false);
			}
		}
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (this.world != null) {
				;
				this.world.markChunkDirty(this.pos, this);
			}
			return (T) this.inv;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(ModPhotonicCraft.MODID + "_container.lasercharger");
	}

	public IItemHandler getInvSlot() {
		return this.inv;
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
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.pos) != this ? false
				: player.getDistanceSq(this.xCoord() + 0.5D, this.yCoord() + 0.5D, this.zCoord() + 0.5D) <= 64.0D;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readSyncableDataFromNBT(pkt.getNbtCompound());
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
	}

	@Override
	public void update() {
		if (this.getWorld().getTotalWorldTime() > (lastAdded + 5)) {
			input = 0;
		}
		this.chargedPrevTick = this.chargedLastTick;
		this.chargedLastTick = false;
		if (!this.inv.getStackInSlot(0).isEmpty()) {
			if (this.inv.getStackInSlot(0).hasCapability(CapabilityEnergy.ENERGY, null)) {
				IEnergyStorage storage = this.inv.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY, null);
				storage.receiveEnergy(input, false);
			}
		}
		if ((this.chargedPrevTick != this.chargedLastTick) || ((this.getWorld().getTotalWorldTime() % 20L) == 0L)) {
			getWorld().scheduleBlockUpdate(new BlockPos(xCoord(), yCoord(), zCoord()), PhotonicBlocks.laserCharger, 0,
					2);
			getWorld().notifyNeighborsOfStateChange(pos, blockType, true);
		}
	}

	private void writeSyncableDataToNBT(NBTTagCompound syncData) {
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		syncData.setTag("Item", this.inv.serializeNBT());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		NBTTagCompound x = super.writeToNBT(p_145841_1_);
		NBTTagList nbttaglist = new NBTTagList();

		writeSyncableDataToNBT(p_145841_1_);
		p_145841_1_.setTag("Item", this.inv.serializeNBT());
		return x;
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
