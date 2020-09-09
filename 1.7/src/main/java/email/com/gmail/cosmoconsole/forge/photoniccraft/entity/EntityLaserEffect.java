package email.com.gmail.cosmoconsole.forge.photoniccraft.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityLaserEffect extends Entity implements IEntityAdditionalSpawnData {

	
	private static final double fac = 0.95;
	private final int LENGTH = 40;
	private int ticks;
	public int color;
	public double velX = 0.0;
	public double velY = 0.0;
	public double velZ = 0.0;
	public double rotX = 0.0;
	public double rotY = 0.0;
	public double rotZ = 0.0;
	public double rotDX = 0.0;
	public double rotDY = 0.0;
	public double rotDZ = 0.0;
	public EntityLaserEffect(World p_i1582_1_) {
		super(p_i1582_1_);
		//this.noClip = true;
	}

	@Override
	protected void entityInit() {
		this.ticks = LENGTH;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		ticks = p_70037_1_.getInteger("ticks");
		color = p_70037_1_.getInteger("color");
		velX = p_70037_1_.getDouble("velX");
		velY = p_70037_1_.getDouble("velY");
		velZ = p_70037_1_.getDouble("velZ");
		rotX = p_70037_1_.getDouble("rotX");
		rotY = p_70037_1_.getDouble("rotY");
		rotZ = p_70037_1_.getDouble("rotZ");
		rotDX = p_70037_1_.getDouble("rotDX");
		rotDY = p_70037_1_.getDouble("rotDY");
		rotDZ = p_70037_1_.getDouble("rotDZ");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setInteger("ticks", ticks);
		p_70014_1_.setInteger("color", color);
		p_70014_1_.setDouble("velX", velX);
		p_70014_1_.setDouble("velY", velY);
		p_70014_1_.setDouble("velZ", velZ);
		p_70014_1_.setDouble("rotX", rotX);
		p_70014_1_.setDouble("rotY", rotY);
		p_70014_1_.setDouble("rotZ", rotZ);
		p_70014_1_.setDouble("rotDX", rotDX);
		p_70014_1_.setDouble("rotDY", rotDY);
		p_70014_1_.setDouble("rotDZ", rotDZ);
	}

	public void setTicks(int t) {
		this.ticks = t;
	}
	@Override
    public void onUpdate()
    {
        if (--this.ticks <= 1) {
    		this.setDead();
    		return;
    	}
        /*this.rotX += this.rotDX;
        this.rotY += this.rotDY;
        this.rotZ += this.rotDZ;
        this.rotX *= fac;
        this.rotY *= fac;
        this.rotZ *= fac;*/
        this.setPosition(this.posX + this.velX, this.posY + this.velY, this.posZ + this.velZ);
    }
	@Override
	public void setPositionAndRotation2(double x, double y, double z, float a, float b, int par1) {
		this.setPosition(x, y, z);
        this.setRotation(a, b);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(color);
		buffer.writeDouble(rotX);
		buffer.writeDouble(rotY);
		buffer.writeDouble(rotZ);
		buffer.writeDouble(rotDX);
		buffer.writeDouble(rotDY);
		buffer.writeDouble(rotDZ);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		color = additionalData.readInt();
		rotX = additionalData.readDouble();
		rotY = additionalData.readDouble();
		rotZ = additionalData.readDouble();
		rotDX = additionalData.readDouble();
		rotDY = additionalData.readDouble();
		rotDZ = additionalData.readDouble();
	}
}
