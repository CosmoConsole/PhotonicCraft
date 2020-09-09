package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class EntityCollisionResult {
	private Entity entity;
	private EnumFacing dir;
	private float dist;
	public EntityCollisionResult(Entity entity, EnumFacing dir, float dist) {
		this.entity = entity;
		this.dir = dir;
		this.dist = dist;
	}
	public Entity getEntity() {
		return this.entity;
	}
	public EnumFacing getDirection() {
		return this.dir;
	}
	public float getDistance() {
		return this.dist;
	}
}
