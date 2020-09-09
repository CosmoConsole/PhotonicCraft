package email.com.gmail.cosmoconsole.forge.photoniccraft.server;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicCommonProxy;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;

public class PhotonicServerProxy extends PhotonicCommonProxy {

	@Override
	public void init() {
	}

	@Override
	public void preinit() {
	}
	
	@Override
	public void postinit() {
	}

	@Override
	public void serverClose() {
		PhotonicRadio.serverSideDeinit();
	}

	@Override
	public void serverLoad() {
		PhotonicRadio.serverSideInit();
	}

}
