package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EnergyContainer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityTerahertzTransmitter extends TileEntity implements ITickable {
	public static final int MAX_RF_CAPACITY = 10000;

	EnergyContainer festorage;

	public TileEntityTerahertzTransmitter() {
		super();
		this.festorage = new EnergyContainer(MAX_RF_CAPACITY, MAX_RF_CAPACITY, 400);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (this.world != null) {
				;
				this.world.markChunkDirty(this.pos, this);
			}
			return CapabilityEnergy.ENERGY.cast(this.festorage);
		}
		return super.getCapability(capability, facing);
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
		writeToNBT(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		readFromNBT(tag);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		this.festorage.setEnergyStored(p_145839_1_.getInteger("fenergy"));
	}

	@Override
	public void update() {
		if (this.getWorld().isRemote)
			return;
		if (this.getWorld().getTotalWorldTime() % 2L != 0L)
			return;
		List<EntityPlayer> lep = this.getWorld().getEntitiesWithinAABB(EntityPlayer.class,
				new AxisAlignedBB(this.xCoord() - 8, this.yCoord() - 8, this.zCoord() - 8, this.xCoord() + 8,
						this.yCoord() + 8, this.zCoord() + 8));
		Collections.shuffle(lep);
		int power = this.festorage.extractEnergy(festorage.getMaxExtract(), true);
		for (Object o: lep) {
			EntityPlayer p = (EntityPlayer) o;
			if (p.isSpectator())
				continue;
			boolean charge = false;
			for (ItemStack i: PhotonicUtils.multipleLists(p.inventory.mainInventory, p.inventory.offHandInventory)) {
				if (charge)
					break;
				if (i == null)
					continue;
				charge = i.getItem() == PhotonicItems.terahertzReceiver;
			}
			if (charge) {
				List<ItemStack> lei = new ArrayList<>();
				for (ItemStack i: PhotonicUtils.multipleLists(p.inventory.mainInventory, p.inventory.armorInventory, p.inventory.offHandInventory)) {
					lei.add(i);
				}
				Collections.shuffle(lei);
				for (ItemStack i: lei) {
					if (i == null)
						continue;
					if (power < 1)
						return;
					if (i.hasCapability(CapabilityEnergy.ENERGY, null)) {
						ModPhotonicCraft.trg_TERAHERTZ_RECEIVED.trigger((EntityPlayerMP) p);
						IEnergyStorage istorage = i.getCapability(CapabilityEnergy.ENERGY, null);
						power -= istorage.receiveEnergy(power / 2, false) * 2;
					}
				}
			}
		}
	}

	@Override
	public void validate() {
		super.validate();

		this.getWorld().scheduleBlockUpdate(this.pos, PhotonicBlocks.terahertzTransmitter, 10, 2);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		p_145841_1_.setInteger("fenergy", festorage.getEnergyStored());
		return super.writeToNBT(p_145841_1_);
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
