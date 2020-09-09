package email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicLaser;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityLaserBeam extends Entity implements IEntityAdditionalSpawnData {

	private int ticks;
	public float blocklen;
	public int color;
	public int power;
	public boolean vertical;
	public boolean yneg;
	public float scale;
	public boolean puny;
	public boolean init;
	public float cyaw;
	public boolean fadeout;
	public int iticks;
	public float alpha;
	private final int LENGTH = 100;

	private EntityLaserBeam trail;

	public EntityLaserBeam(World p_i1582_1_) {
		super(p_i1582_1_);
		this.init = false;
		this.ignoreFrustumCheck = true;
		this.noClip = true;
	}

	public EntityLaserBeam(World p_i1582_1_, float len, int color, int laserPower, boolean vertical, boolean yneg,
			float yaw) {
		this(p_i1582_1_);
		this.init = true;
		this.blocklen = len;
		this.ticks = this.iticks = LENGTH;
		this.color = color;
		this.power = laserPower;
		this.vertical = vertical;
		this.yneg = yneg;
		this.puny = false;
		this.scale = PhotonicLaser.computeLaserScale(laserPower);
		this.cyaw = yaw;
		this.fadeout = false;
		this.alpha = 1.0f;
		/*this.R = (float) (0.5 * Math.log(this.power));
		if (this.power >= 200) {
			this.I = 0.625f;
		} else {
			this.I = (float) (0.625f / (1.0f + Math.exp(this.power * -0.2f)));
		}*/
	}

	@Override
	protected void entityInit() {
		this.ticks = this.iticks = LENGTH;
	}

	public float getPermanentYaw() {
		return this.cyaw;
	}

	public EntityLaserBeam getPunyTrail() {
		return trail;
	}

	@Override
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.blocklen * 32.0D * getRenderDistanceWeight();
		return distance < d0 * d0;
	}

	@Override
	public boolean isInvisible() {
		return false;
	}

	@Override
	public void onUpdate() {
		if ((!this.world.isRemote && !this.init) || (this.ticks < 1)) {
			this.setDead();
			return;
		} else if (this.ticks > 0) {
			if (this.fadeout) {
				this.alpha = (float) this.ticks / (float) this.iticks;
			}
			--this.ticks;
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		ticks = p_70037_1_.getInteger("ticks");
		iticks = p_70037_1_.getInteger("iticks");
		alpha = p_70037_1_.getFloat("alpha");
		color = p_70037_1_.getInteger("color");
		blocklen = p_70037_1_.getFloat("length");
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

	public void setPuny() {
		this.puny = true;
	}

	public void setPunyTrail(EntityLaserBeam e2) {
		trail = e2;
	}

	public void setTicks(int t) {
		this.ticks = this.iticks = t;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setInteger("ticks", ticks);
		p_70014_1_.setInteger("iticks", iticks);
		p_70014_1_.setFloat("alpha", alpha);
		p_70014_1_.setInteger("color", color);
		p_70014_1_.setFloat("length", blocklen);
	}

	public void setFadeout(boolean b) {
		this.fadeout = b;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(this.ticks);
		buffer.writeInt(this.iticks);
		buffer.writeBoolean(this.fadeout);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.ticks = additionalData.readInt();
		this.iticks = additionalData.readInt();
		this.fadeout = additionalData.readBoolean();
	}
}
