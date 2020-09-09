package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import net.minecraftforge.fml.common.Optional;

public class RadioEventReceiver {
	private int channel;
	private Object receiver;
	
	public RadioEventReceiver(int channel, Object receiver) {
		this.channel = channel;
		this.receiver = receiver;
	}

	public void trigger() {
		if (Compat.computercraft)
			trigger_ComputerCraft();
		if (Compat.opencomputers)
			trigger_OpenComputers();
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	private void trigger_ComputerCraft() {
		if (receiver instanceof dan200.computercraft.api.peripheral.IComputerAccess)
			((dan200.computercraft.api.peripheral.IComputerAccess) receiver)
					.queueEvent(PhotonicRadio.RADIO_TIMER_EVENT, new Object[] { channel });
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	private void trigger_OpenComputers() {
		if (receiver instanceof li.cil.oc.api.machine.Context)
			((li.cil.oc.api.machine.Context) receiver)
					.signal(PhotonicRadio.RADIO_TIMER_EVENT, channel);
	}
}
