package email.com.gmail.cosmoconsole.forge.photoniccraft.common;

/**
 * Used as a base for the client and server proxies run only at their respective
 * sides.
 */
public abstract class PhotonicCommonProxy {
	public abstract void preinit();

	public abstract void init();

	public abstract void postinit();

	public abstract void serverLoad();

	public abstract void serverClose();
}
