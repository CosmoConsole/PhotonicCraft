package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserBeam;
import net.minecraft.util.EnumFacing;

/**
 * A result for a shot laser beam entity, describing the direection, color, and
 * other information.
 */
public class EntityLaserBeamResult {
	public enum Type {
		SUCCESS, HITEND, HITCONTINUE, NEWDIRECTION, NEWCOLOR, NEWDIRECTIONANDCOLOR, NEWDIRCLONE, NEWDIRCOLOR, PRISM, NEWCOLORDIR;
	}

	public EntityLaserBeam beam;
	public EntityLaserBeamResult.Type type;
	public float decrease = 0.0F;
	public EnumFacing direction;
	public int newcolor = 0;
	public EnumFacing direction2;

	public int colormeta = -1;

	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b) {
		this(a, b, 0.0F);
	}

	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, EnumFacing d) {
		this(a, b, 0.0f, d, 0, null, -1);
	}

	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, EnumFacing d, EnumFacing f) {
		this(a, b, 0.0f, d, 0, f, -1);
	}

	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, EnumFacing d, int e, int g) {
		this(a, b, 0.0f, d, e, null, g);
	}

	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, float c) {
		this(a, b, c, null, 0, null, -1);
	}

	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, float c, EnumFacing d, int e,
			EnumFacing f, int g) {
		beam = a;
		type = b;
		decrease = c;
		direction = d;
		newcolor = e;
		direction2 = f;
		colormeta = g;
	}

	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, int e) {
		this(a, b, 0.0f, null, e, null, -1);
	}

	public EntityLaserBeamResult(EntityLaserBeam a, EntityLaserBeamResult.Type b, int e, int g) {
		this(a, b, 0.0f, null, e, null, g);
	}
}
