package email.com.gmail.cosmoconsole.forge.photoniccraft.client.network;

import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The packet used for signifying the position of a laser pointer entity from
 * the server to the client and synchronize entity movement.
 */
public class PhotonicLaserPointerPacket implements IMessage {
	public static class Handler implements IMessageHandler<PhotonicLaserPointerPacket, IMessage> {
		@Override
		public IMessage onMessage(PhotonicLaserPointerPacket message, MessageContext ctx) {
			final World par2World = message.world;
			final double x = message.x;
			final double y = message.y;
			final double z = message.z;
			final Entity ent = message.hitEntity;
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					PhotonicUtils.spawnLaserPointer(par2World, x, y, z, ent);
				}
			});
			return null;
		}
	}

	private World world;
	private double x;
	private double y;
	private double z;

	private Entity hitEntity;

	public PhotonicLaserPointerPacket() {
		this.world = null;
		this.x = this.y = this.z = 0;
		this.hitEntity = null;
	}

	public PhotonicLaserPointerPacket(World world, double x, double y, double z, Entity hitEntity) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hitEntity = hitEntity;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.world = worldDefault(PhotonicUtils.convertIntegerToWorld(buf.readInt()));
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		if (buf.readBoolean())
			this.hitEntity = null;
		else
			this.hitEntity = this.world.getEntityByID(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(PhotonicUtils.convertWorldToInteger(world));
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeBoolean(hitEntity == null);
		if (hitEntity != null)
			buf.writeInt(hitEntity.getEntityId());
	}

	@SideOnly(Side.CLIENT)
	public World worldDefault(World w) {
		if (w == null) {
			return Minecraft.getMinecraft().world;
		} else {
			return w;
		}
	}
}
