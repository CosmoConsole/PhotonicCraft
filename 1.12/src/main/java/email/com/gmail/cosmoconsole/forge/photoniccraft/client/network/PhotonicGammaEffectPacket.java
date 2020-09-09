package email.com.gmail.cosmoconsole.forge.photoniccraft.client.network;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityGammaEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PhotonicGammaEffectPacket implements IMessage {
	public static class Handler implements IMessageHandler<PhotonicGammaEffectPacket, IMessage> {
		@Override
		public IMessage onMessage(PhotonicGammaEffectPacket message, MessageContext ctx) {
			World w = message.world;
			if (w == null) {
				w = getBackupWorld();
			}
			final World par2World = w;
			final double x = message.x;
			final double y = message.y;
			final double z = message.z;
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityGammaEffect e2 = new EntityGammaEffect(par2World);
					e2.setPositionAndRotation(x, y, z, 0.0f, 0.0f);
					e2.setInitialTicks();
					par2World.spawnEntity(e2);
				}
			});
			return null;
		}

		@SideOnly(value = Side.CLIENT)
		private World getBackupWorld() {
			return Minecraft.getMinecraft().world;
		}
	}

	private World world;
	private double x;
	private double y;
	private double z;

	public PhotonicGammaEffectPacket() {
		this.world = null;
		this.x = this.y = this.z = 0;
	}

	public PhotonicGammaEffectPacket(World world, double x, double y, double z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.world = PhotonicUtils.convertIntegerToWorld(buf.readInt());
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(PhotonicUtils.convertWorldToInteger(world));
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
	}

}
