package email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityLaserPointer extends Entity {

	private final int LENGTH = 6;
	private int ticks;

	public EntityLaserPointer(World p_i1582_1_) {
		super(p_i1582_1_);
		this.noClip = true;
	}

	@Override
	protected void entityInit() {
		this.ticks = LENGTH;
	}

	@Override
	public void onUpdate() {
		if (--this.ticks <= 1) {
			this.setDead();
			return;
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		ticks = p_70037_1_.getInteger("ticks");
	}

	@Override
	public void setPositionAndRotation(double x, double y, double z, float a, float b) {
		this.setPosition(x, y, z);
		this.setRotation(a, b);
	}

	public void setPositionAndRotationDirect(double x, double y, double z, float a, float b, int par1) {
		this.setPosition(x, y, z);
		this.setRotation(a, b);
	}

	public void setTicks(int t) {
		this.ticks = t;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setInteger("ticks", ticks);
	}
}
