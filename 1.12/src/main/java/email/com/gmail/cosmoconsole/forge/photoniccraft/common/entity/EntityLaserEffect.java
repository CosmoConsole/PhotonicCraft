package email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

@Optional.InterfaceList({@Optional.Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = Compat.MODID_MIRAGE)})
public class EntityLaserEffect extends Entity implements IEntityAdditionalSpawnData, com.elytradev.mirage.lighting.IEntityLightEventConsumer {

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
		if (this.velX == 0 && this.velY == 0 && this.velZ == 0) {
			this.setDead();
			return;
		}
		this.setPositionAndUpdate(this.posX + this.velX, this.posY + this.velY, this.posZ + this.velZ);
		if (this.world.isRemote)
			this.setVelocity(this.velX, this.velY, this.velZ);
		rotX += rotDX;
		rotY += rotDY;
		rotZ += rotDZ;
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
	public void readSpawnData(ByteBuf additionalData) {
		color = additionalData.readInt();
		rotX = additionalData.readDouble();
		rotY = additionalData.readDouble();
		rotZ = additionalData.readDouble();
		rotDX = additionalData.readDouble();
		rotDY = additionalData.readDouble();
		rotDZ = additionalData.readDouble();
		velX = additionalData.readDouble();
		velY = additionalData.readDouble();
		velZ = additionalData.readDouble();
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

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(color);
		buffer.writeDouble(rotX);
		buffer.writeDouble(rotY);
		buffer.writeDouble(rotZ);
		buffer.writeDouble(rotDX);
		buffer.writeDouble(rotDY);
		buffer.writeDouble(rotDZ);
		buffer.writeDouble(velX);
		buffer.writeDouble(velY);
		buffer.writeDouble(velZ);
	}

	@Optional.Method(modid = Compat.MODID_MIRAGE)
	@Override
	public void gatherLights(com.elytradev.mirage.event.GatherLightsEvent arg0, Entity arg1) {
		arg0.add(com.elytradev.mirage.lighting.Light.builder().pos(arg1).color(color, false).intensity(0.9f).radius(5.0f).build());
	}
}
