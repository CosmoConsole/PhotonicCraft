package email.com.gmail.cosmoconsole.forge.photoniccraft;

import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserBeam;

public class EntityLaserBeamResult {
	public EntityLaserBeam beam;
	public EntityLaserBeamResult.Type type;
	public float decrease = 0.0F;
	public LaserDirection direction;
	public int newcolor = 0;
	public LaserDirection direction2;
	public int colormeta = -1;
	public enum Type {
		SUCCESS, HITEND, HITCONTINUE, NEWDIRECTION, NEWCOLOR, NEWDIRECTIONANDCOLOR, NEWDIRCLONE, NEWDIRCOLOR, PRISM, NEWCOLORDIR;
	}
	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b) {
		this(a, b, 0.0F);
	}
	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, float c) {
		this(a, b, c, null, 0, null, -1);
	}
	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, LaserDirection d) {
		this(a, b, 0.0f, d, 0, null, -1);
	}
	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, LaserDirection d, int e, int g) {
		this(a, b, 0.0f, d, e, null, g);
	}
	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, int e) {
		this(a, b, 0.0f, null, e, null, -1);
	}
	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, int e, int g) {
		this(a, b, 0.0f, null, e, null, g);
	}
	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, LaserDirection d, LaserDirection f) {
		this(a, b, 0.0f, d, 0, f, -1);
	}
	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, float c, LaserDirection d, int e, LaserDirection f, int g) {
		beam = a;
		type = b;
		decrease = c;
		direction = d;
		newcolor = e;
		direction2 = f;
		colormeta = g;
	}
}
