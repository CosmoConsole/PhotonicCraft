package email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityGammaEffect extends Entity {

	public final int LENGTH = 20;
	private int ticks;

	public EntityGammaEffect(World p_i1582_1_) {
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
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.ticks = compound.getInteger("ticks");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("ticks", ticks);
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

	public float getAlpha() {
		return (float)this.ticks / (float)LENGTH;
	}

	public void setInitialTicks() {
		this.setTicks(LENGTH);
	}
}
