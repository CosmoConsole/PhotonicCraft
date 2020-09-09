package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class TileEntityInfiniteSink extends TileEntity {

	EnergyInterface festorage;

	public TileEntityInfiniteSink() {
		super();
		this.festorage = new EnergyInterface();
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (this.world != null) {
				this.world.markChunkDirty(this.pos, this);
			}
			return CapabilityEnergy.ENERGY.cast(this.festorage);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}

	@Override
	public void validate() {
		super.validate();

		this.getWorld().scheduleBlockUpdate(this.pos, PhotonicBlocks.infiniteSink, 10, 2);
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

	class EnergyInterface extends EnergyStorage {
		public EnergyInterface() {
			super(Integer.MAX_VALUE);
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return 0;
		}

		public int getMaxExtract() {
			return 0;
		}

		public int getMaxReceive() {
			return Integer.MAX_VALUE;
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return maxReceive;
		}
		
		@Override
		public boolean canExtract() {
			return false;
		}
		
		@Override
		public boolean canReceive() {
			return true;
		}
	}
}
