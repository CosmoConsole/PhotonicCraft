package email.com.gmail.cosmoconsole.forge.photoniccraft.server;

import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicAPI;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicCommonProxy;

public class PhotonicServerProxy extends PhotonicCommonProxy {

	@Override
	public void preinit() {
	}

	@Override
	public void init() {
		PhotonicAPI.serverSideInit();
	}

	@Override
	public void serverLoad() {
		PhotonicAPI.serverSideInit();
	}

}
