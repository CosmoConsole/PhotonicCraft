package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EnergyContainerItem extends EnergyContainer {
	protected ItemStack item;

	public EnergyContainerItem(ItemStack item, int capacity) {
		super(capacity);
		this.item = item;
	}

	public EnergyContainerItem(ItemStack item, int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
		this.item = item;
	}

	public EnergyContainerItem(ItemStack item, int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
		this.item = item;
	}

	public EnergyContainerItem(ItemStack item, int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
		this.item = item;
	}

	@Override
	public int extractEnergy(int amount, boolean simulate) {
		int energy = this.getEnergyStored();
		int extracting = Math.min(Math.min(energy, this.maxExtract), amount);
		if (!simulate)
			this.setEnergyStored(energy - extracting);
		return extracting;
	}

	@Override
	public int getEnergyStored() {
		if (item == null)
			return 0;
		if (!item.hasTagCompound())
			return 0;
		return this.getMaxEnergyStored() - item.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
	}

	@Override
	public int receiveEnergy(int amount, boolean simulate) {
		int energy = this.getEnergyStored();
		int receiving = Math.min(Math.min(this.capacity - energy, this.maxReceive), amount);
		if (!simulate)
			this.setEnergyStored(energy + receiving);
		return receiving;
	}

	@Override
	public void setEnergyStored(int energy) {
		if (item == null)
			return;
		if (!item.hasTagCompound())
			item.setTagCompound(new NBTTagCompound());
		item.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, this.getMaxEnergyStored() - energy);
	}
}
