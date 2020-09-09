package email.com.gmail.cosmoconsole.forge.photoniccraft.client;

import java.util.Timer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity.RenderColossalCreeper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity.RenderGammaEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity.RenderLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity.RenderLaserEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity.RenderLaserPointer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.tileentity.TileEntityMicrowaveOvenRenderer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.tileentity.TileEntityXRayReceiverRenderer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicCommonProxy;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicGuiHandler;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityColossalCreeper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityGammaEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserPointer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityXRayReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class PhotonicClientProxy extends PhotonicCommonProxy {
	public static void restartSourceDataLine() {
		if (ModPhotonicCraft.sourceDataLine != null) {
			ModPhotonicCraft.sourceDataLine.flush();
			ModPhotonicCraft.sourceDataLine.close();
		}
		setupRadio();
	}

	public static void setupRadio() {
		ModPhotonicCraft.radioFormat = new AudioFormat(Encoding.PCM_SIGNED, 11025.0f, 16, 1, 2, 11025.0f, false);
		ModPhotonicCraft.dataLineInfo = new DataLine.Info(SourceDataLine.class, ModPhotonicCraft.radioFormat, 8820);
		PhotonicRadio.lastPing = 0L;
		PhotonicRadio.radioPlaying = false;
		try {
			ModPhotonicCraft.sourceDataLine = (SourceDataLine) AudioSystem.getLine(ModPhotonicCraft.dataLineInfo);
			ModPhotonicCraft.sourceDataLine.open(ModPhotonicCraft.radioFormat, 88200);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Cannot use Radio: line not available. This is up to your audio driver or system!");
			return;
		}
		PhotonicRadio.radioTimer = new Timer();
		PhotonicRadio.radioTask = null;
	}

	public static void setVolume(double volumeToPlay) {
		if (ModPhotonicCraft.sourceDataLine == null)
			return;
		if (ModPhotonicCraft.sourceDataLine.isControlSupported(FloatControl.Type.VOLUME)) {
			FloatControl volume = (FloatControl) ModPhotonicCraft.sourceDataLine.getControl(FloatControl.Type.VOLUME);
			volume.setValue((float) volumeToPlay);
		} else if (ModPhotonicCraft.sourceDataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl volume = (FloatControl) ModPhotonicCraft.sourceDataLine
					.getControl(FloatControl.Type.MASTER_GAIN);
			float gain = (float) (20 * Math.log(volumeToPlay));
			if (gain < volume.getMinimum())
				gain = volume.getMinimum();
			volume.setValue(gain);
		}
	}

	@Override
	public void init() {
		registerTileEntityRenderers();
		setupRadio();
		setupRadioPausingThread();
	}

	@Override
	public void preinit() {
		registerEntityRenderers();
		registerGuiHandler();
	}
	
	@Override
	public void postinit() {
	}

	private void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityGammaEffect.class, RenderGammaEffect::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityColossalCreeper.class, RenderColossalCreeper::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLaserBeam.class, RenderLaserBeam::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLaserEffect.class, RenderLaserEffect::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLaserPointer.class, RenderLaserPointer::new);
	}

	private void registerGuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(ModPhotonicCraft.instance, new PhotonicGuiHandler());
	}

	private void registerTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMicrowaveOven.class,
				new TileEntityMicrowaveOvenRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityXRayReceiver.class,
				new TileEntityXRayReceiverRenderer());
	}

	@Override
	public void serverClose() {
		restartSourceDataLine();
	}

	@Override
	public void serverLoad() {
	}

	private void setupRadioPausingThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean lastPause = false;
				while (true) {
					if (ModPhotonicCraft.sourceDataLine != null
							&& Minecraft.getMinecraft().isIntegratedServerRunning()) {
						if (Minecraft.getMinecraft().isGamePaused()) {
							if (!lastPause) {
								lastPause = true;
								ModPhotonicCraft.sourceDataLine.flush();
								ModPhotonicCraft.sourceDataLine.stop();
							}
						} else {
							if (lastPause) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										PhotonicRadio.midSend.set(true);
										ModPhotonicCraft.sourceDataLine.drain();
										PhotonicRadio.midSend.set(false);
										ModPhotonicCraft.sourceDataLine.start();
									}
								}).start();
							}
							lastPause = false;
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}
}
