package email.com.gmail.cosmoconsole.forge.photoniccraft.integration.waila;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaProvider implements IWailaPlugin {
	@Override
	public void register(IWailaRegistrar registrar) {
		HUDHandlerPhotonicCraft.register(registrar);
	}
}
