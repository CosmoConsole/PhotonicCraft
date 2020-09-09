package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

public class EnergyContainerDynamic extends EnergyContainer {

	public EnergyContainerDynamic(int capacity) {
		super(capacity);
	}

	public EnergyContainerDynamic(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public EnergyContainerDynamic(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public EnergyContainerDynamic(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return super.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getMaxExtract() {
		return this.maxExtract;
	}

	@Override
	public int getMaxReceive() {
		return this.maxReceive;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return super.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public void setEnergyStored(int energy) {
		this.energy = energy;
	}
	
	public void setMaxExtract(int newMaxExtract) {
		this.maxExtract = newMaxExtract;
	}
	
	public void setMaxReceive(int newMaxReceive) {
		this.maxReceive = newMaxReceive;
	}

}
