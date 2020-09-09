package email.com.gmail.cosmoconsole.forge.photoniccraft.server.network;

import java.util.UUID;

import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * A response packet (client -> server) used to signify special states for the
 * radio playback stream. Currently only used to signify skipping a packet as an
 * anti-lag mechanism.
 */
public class PhotonicRadioResponsePacket implements IMessage {
	public static class Handler implements IMessageHandler<PhotonicRadioResponsePacket, IMessage> {
		@Override
		public IMessage onMessage(PhotonicRadioResponsePacket message, MessageContext ctx) {
			if (message.type == Type.SKIP_NEXT) {
				PhotonicRadio.skipNextRadioPacket(message.player);
			}
			return null;
		}
	}

	public enum Type {
		SKIP_NEXT;
	}

	Type type;

	UUID player;

	public PhotonicRadioResponsePacket() {
		this.type = null;
		this.player = null;
	}

	public PhotonicRadioResponsePacket(Type type, UUID id) {
		this.type = type;
		this.player = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.type = Type.values()[buf.readInt()];
		long msb = buf.readLong();
		long lsb = buf.readLong();
		this.player = new UUID(msb, lsb);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(type.ordinal());
		buf.writeLong(player.getMostSignificantBits());
		buf.writeLong(player.getLeastSignificantBits());
	}

}
