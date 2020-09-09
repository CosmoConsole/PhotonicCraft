package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

public class CapabilityProviderEnergySerializable implements ICapabilityProvider {
	public final EnergyContainerItem container;

	public CapabilityProviderEnergySerializable(final ItemStack stack, int capacity) {
		this.container = new EnergyContainerItem(stack, capacity);
	}

	public CapabilityProviderEnergySerializable(final ItemStack stack, int capacity, int maxReceive) {
		this.container = new EnergyContainerItem(stack, capacity, maxReceive);
	}

	public CapabilityProviderEnergySerializable(final ItemStack stack, int capacity, int maxReceive, int maxExtract) {
		this.container = new EnergyContainerItem(stack, capacity, maxReceive, maxExtract);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.container);
		}
		return null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return getCapability(capability, facing) != null;
	}
}
