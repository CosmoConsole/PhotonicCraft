package email.com.gmail.cosmoconsole.forge.photoniccraft;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import paulscode.sound.SoundSystem;

public class PhotonicRadioPacket implements IMessage {
	public byte[] audio;
	public double strength;
	public PhotonicRadioPacket() {
		this.strength = 0;
		this.audio = new byte [0];
	}
	public PhotonicRadioPacket(double strength, byte[] audio) {
		this.strength = strength;
		this.audio = audio;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		strength = buf.readDouble();
		this.audio = new byte[2205];
		buf.readBytes(audio);
	}
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(strength);
		buf.writeBytes(audio);
	}
	public static class Handler implements IMessageHandler<PhotonicRadioPacket, IMessage> {
		public Handler() {
			
		}
		@Override
		public IMessage onMessage(PhotonicRadioPacket message, MessageContext ctx) {
			double strength = message.strength;
			if (strength < 0) {
				PhotonicAPI.shouldNotBePlaying();
				PhotonicAPI.stopPlayingRadio();
				return null;
			}
			byte[] audio = message.audio;
			if (audio.length != 2205) return null;
			byte[] noise = new byte[2205];
			byte[] finalaudio = new byte[2205];
			double istrength = 1 - strength;
			double volumeToPlay = ((SoundSystem)ObfuscationReflectionHelper.getPrivateValue(SoundManager.class, 
					(SoundManager)ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, Minecraft.getMinecraft().getSoundHandler(), PhotonicReflectionHelper.sndManager), 
					PhotonicReflectionHelper.sndSystem)).getMasterVolume();
			PhotonicAPI.rand.nextBytes(noise);
			//Arrays.fill(noise, (byte)128);
			for (int i = 0; i < finalaudio.length; i++) {
				//finalaudio[i] = (byte)(scaleVolume(((strength * unsign(audio[i]) + istrength * unsign(noise[i]))), volumeToPlay));
				if (strength > 0) {
					finalaudio[i] = (byte) scaleVolume(audio[i], noise[i], strength, volumeToPlay);
				} else {
					finalaudio[i] = (byte) scaleVolume(noise[i], (byte)0, 1.0, volumeToPlay);
				}
			}
			/*AudioFormat af = new AudioFormat(Encoding.PCM_UNSIGNED, 11025.0f, 8, 1, 1, 11025.0f, false);*/
			long now = System.currentTimeMillis();
			if ((now - ModPhotonicCraft.lastReceive) > 1500L) {
				return null;
			}
			//System.out.println("write #1, buffer size: " + ModPhotonicCraft.sourceDataLine.getBufferSize());
			long start = System.currentTimeMillis();
			ModPhotonicCraft.sourceDataLine.write(finalaudio, 0, 2205);
			//System.out.println("write #2, delta: " + (System.currentTimeMillis() - start));
			return null;
		}
		private double scaleVolume(byte d, byte n, double s, double v) {
			return (((double)unsign(d) - 128) * s + ((double)unsign(n) - 128) * (1 - s)) * v + 128;
		}
		public double unsign(byte b) {
			if (b < 0)
				return b + 256;
			return b;
		}
	}
}
