package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityInfiniteSource extends TileEntity implements ITickable {

	EnergyInterface festorage;

	public TileEntityInfiniteSource() {
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

		this.getWorld().scheduleBlockUpdate(this.pos, PhotonicBlocks.infiniteSource, 10, 2);
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
	public void update() {
		for (EnumFacing facing : PhotonicUtils.getShuffledFaces()) {
			TileEntity tile = getWorld().getTileEntity(new BlockPos(xCoord() + facing.getFrontOffsetX(),
					yCoord() + facing.getFrontOffsetY(), zCoord() + facing.getFrontOffsetZ()));
			if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
				IEnergyStorage enstr = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				enstr.receiveEnergy(Integer.MAX_VALUE, false);
			}
		}
	}

	class EnergyInterface extends EnergyStorage {
		public EnergyInterface() {
			super(0);
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return maxExtract;
		}

		public int getMaxExtract() {
			return Integer.MAX_VALUE;
		}

		public int getMaxReceive() {
			return 0;
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return 0;
		}
		
		@Override
		public boolean canExtract() {
			return true;
		}
		
		@Override
		public boolean canReceive() {
			return false;
		}
	}
}
