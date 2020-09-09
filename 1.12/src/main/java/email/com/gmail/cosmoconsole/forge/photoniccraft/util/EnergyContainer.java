package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import net.minecraftforge.energy.EnergyStorage;

public class EnergyContainer extends EnergyStorage {
	public EnergyContainer(int capacity) {
		super(capacity);
	}

	public EnergyContainer(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public EnergyContainer(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public EnergyContainer(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return super.extractEnergy(maxExtract, simulate);
	}

	public int getMaxExtract() {
		return this.maxExtract;
	}

	public int getMaxReceive() {
		return this.maxReceive;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return super.receiveEnergy(maxReceive, simulate);
	}

	public void setEnergyStored(int energy) {
		this.energy = energy;
	}

}
