package email.com.gmail.cosmoconsole.forge.photoniccraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityLaserBeam extends Entity {

	
	private int ticks;
	public float blocklen;
	public int color;
	public int power;
	public boolean vertical;
	public boolean yneg;
	public float scale;
	public boolean puny;
	public boolean init;
	private final int LENGTH = 100;
	public EntityLaserBeam(World p_i1582_1_) {
		super(p_i1582_1_);
		this.init = false;
	}
	public EntityLaserBeam(World p_i1582_1_, float len, int color, int laserPower, boolean vertical, boolean yneg) {
		super(p_i1582_1_);
		this.init = true;
		this.blocklen = len;
		this.ticks = LENGTH;
		this.color = color;
		this.power = laserPower;
		this.ignoreFrustumCheck = true;
		this.noClip = true;
		this.vertical = vertical;
		this.yneg = yneg;
		this.puny = false;
		this.scale = (float) (1 + Math.log10(laserPower)) * 6 - 4;
	}

	@Override
	protected void entityInit() {
		this.ticks = LENGTH;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		ticks = p_70037_1_.getInteger("ticks");
		color = p_70037_1_.getInteger("color");
		blocklen = p_70037_1_.getFloat("length");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setInteger("ticks", ticks);
		p_70014_1_.setInteger("color", color);
		p_70014_1_.setFloat("length", blocklen);
	}

	public void setTicks(int t) {
		this.ticks = t;
	}
	@Override
    public void onUpdate()
    {
    	if ((!this.init) || (--this.ticks <= 1)) {
    		this.setDead();
    		return;
    	}
    }
	@Override
	public void setPositionAndRotation2(double x, double y, double z, float a, float b, int par1) {
		this.setPosition(x, y, z);
        this.setRotation(a, b);
	}

	public void setPuny() {
		this.puny = true;
	}
	private EntityLaserBeam trail;
	public EntityLaserBeam getPunyTrail() {
		return trail;
	}
	public void setPunyTrail(EntityLaserBeam e2) {
		trail = e2;
	}
}
