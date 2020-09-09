package email.com.gmail.cosmoconsole.forge.photoniccraft.client.network;

import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PhotonicInfraredPacket implements IMessage {

	private boolean enabled;

	public PhotonicInfraredPacket() {
		this.enabled = false;
	}

	public PhotonicInfraredPacket(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.enabled = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.enabled);
	}

	public static class Handler implements IMessageHandler<PhotonicInfraredPacket, IMessage> {
		@Override
		public IMessage onMessage(PhotonicInfraredPacket message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					if (message.enabled) {
						PhotonicUtils.applyInfraredEffect();
					} else {
						PhotonicUtils.removeInfraredEffect();
					}
				}
			});
			return null;
		}
	}

}
