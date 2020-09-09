package email.com.gmail.cosmoconsole.forge.photoniccraft.server.network;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityRemoteReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * The packet used by the Infrared Remotes to handle the block giving redstone
 * power.
 */
public class PhotonicRemotePacket implements IMessage {
	public static class Handler implements IMessageHandler<PhotonicRemotePacket, IMessage> {
		@Override
		public IMessage onMessage(PhotonicRemotePacket message, MessageContext ctx) {
			final World par2World = message.world;
			final int x = message.x;
			final int y = message.y;
			final int z = message.z;
			final Entity ent = message.hitEntity;
			final int np = (message.strength << 3) - 1;
			final long time = message.worldTime;
			((WorldServer) par2World).addScheduledTask(new Runnable() {
				@Override
				public void run() {
					try {
						EntityPlayer p = (EntityPlayer) ent;
						long channel = p.getHeldItem(EnumHand.MAIN_HAND).getTagCompound().getLong("channel");
						TileEntityRemoteReceiver r = (TileEntityRemoteReceiver) par2World
								.getTileEntity(new BlockPos(x, y, z));
						if (r.channel == channel) {
							r.submitPower(np, time);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			return null;
		}
	}

	private World world;
	private int x;
	private int y;
	private int z;
	private Entity hitEntity;
	private int strength;

	private long worldTime;

	public PhotonicRemotePacket() {
		this.world = null;
		this.x = this.y = this.z = this.strength = 0;
		this.hitEntity = null;
		this.worldTime = 0L;
	}

	public PhotonicRemotePacket(World world, int x, int y, int z, Entity hitEntity, int strength, long worldTime) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hitEntity = hitEntity;
		this.strength = strength;
		this.worldTime = worldTime;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.world = PhotonicUtils.convertIntegerToWorld(buf.readInt());
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.strength = buf.readInt();
		if (buf.readBoolean())
			this.hitEntity = null;
		else
			this.hitEntity = this.world.getEntityByID(buf.readInt());
		this.worldTime = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(PhotonicUtils.convertWorldToInteger(world));
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(strength);
		buf.writeBoolean(hitEntity == null);
		if (hitEntity != null)
			buf.writeInt(hitEntity.getEntityId());
		buf.writeLong(worldTime);
	}

}
