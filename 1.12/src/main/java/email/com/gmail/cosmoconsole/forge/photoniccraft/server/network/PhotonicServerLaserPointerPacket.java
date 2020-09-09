package email.com.gmail.cosmoconsole.forge.photoniccraft.server.network;

import java.util.UUID;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicLaserPointerPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PhotonicServerLaserPointerPacket implements IMessage {
	public static class Handler implements IMessageHandler<PhotonicServerLaserPointerPacket, IMessage> {
		@Override
		public IMessage onMessage(PhotonicServerLaserPointerPacket message, MessageContext ctx) {
			final World world = message.world;
			final double x = message.x;
			final double y = message.y;
			final double z = message.z;
			final Entity hitEntity = message.hitEntity;
			((WorldServer) world).addScheduledTask(new Runnable() {
				@Override
				public void run() {
					PhotonicUtils.spawnLaserPointer(world, x, y, z, hitEntity);
					try {
						ModPhotonicCraft.network.sendToAllAround(
								new PhotonicLaserPointerPacket(world, 
										x, y, z, hitEntity), 
								new TargetPoint(world.provider.getDimension(), 
										x, y, z, 128));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
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
	private UUID player;

	public PhotonicServerLaserPointerPacket() {
		this.world = null;
		this.x = this.y = this.z = 0.0;
		this.player = null;
		this.hitEntity = null;
	}

	public PhotonicServerLaserPointerPacket(World world, double x, double y, double z, Entity hitEntity, UUID player) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hitEntity = hitEntity;
		this.player = player;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.world = PhotonicUtils.convertIntegerToWorld(buf.readInt());
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		if (buf.readBoolean())
			this.hitEntity = null;
		else
			this.hitEntity = this.world.getEntityByID(buf.readInt());
		long msb = buf.readLong();
		long lsb = buf.readLong();
		this.player = new UUID(msb, lsb);
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
		buf.writeLong(player.getMostSignificantBits());
		buf.writeLong(player.getLeastSignificantBits());
	}

}
