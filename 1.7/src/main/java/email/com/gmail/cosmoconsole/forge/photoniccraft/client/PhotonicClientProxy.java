package email.com.gmail.cosmoconsole.forge.photoniccraft.client;

import java.util.Timer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicAPI;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicCommonProxy;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicGuiHandler;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityColossalCreeper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserPointer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.rendering.RenderColossalCreeper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.rendering.RenderLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.rendering.RenderLaserEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.rendering.RenderLaserPointer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFloodlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFluorescentBlacklight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserDetector2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserMirror2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserRedstoneMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserSemiMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserSemiMirror2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityPrism;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering.TileEntityFloodlightRenderer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering.TileEntityFluorescentBlacklightRenderer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering.TileEntityFluorescentLightRenderer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering.TileEntityLaserDetectorRenderer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering.TileEntityLaserMirrorRenderer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering.TileEntityLaserRenderer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering.TileEntityMicrowaveOvenRenderer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering.TileEntityPrismRenderer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering.TileEntityRedstoneMirrorRenderer;
import net.minecraft.util.ResourceLocation;

public class PhotonicClientProxy extends PhotonicCommonProxy
{
	@Override
	public void init() {
		RenderingRegistry.registerEntityRenderingHandler(EntityColossalCreeper.class, new RenderColossalCreeper());
		RenderingRegistry.registerEntityRenderingHandler(EntityLaserBeam.class, new RenderLaserBeam());
		RenderingRegistry.registerEntityRenderingHandler(EntityLaserEffect.class, new RenderLaserEffect());
		RenderingRegistry.registerEntityRenderingHandler(EntityLaserPointer.class, new RenderLaserPointer());
		//RenderingRegistry.registerEntityRenderingHandler(EntityMirror.class, new RenderMirrorEntity());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaser.class, new TileEntityLaserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserMirror.class, new TileEntityLaserMirrorRenderer(new ResourceLocation("photoniccraft:textures/blocks/lasermirror.png")));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserMirror2.class, new TileEntityLaserMirrorRenderer(new ResourceLocation("photoniccraft:textures/blocks/lasermirror2.png")));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserSemiMirror.class, new TileEntityLaserMirrorRenderer(new ResourceLocation("photoniccraft:textures/blocks/lasermirrors.png")));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserSemiMirror2.class, new TileEntityLaserMirrorRenderer(new ResourceLocation("photoniccraft:textures/blocks/lasermirrors2.png")));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserRedstoneMirror.class, new TileEntityRedstoneMirrorRenderer(new ResourceLocation("photoniccraft:textures/blocks/redstonemirror.png")));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserDetector.class, new TileEntityLaserDetectorRenderer(new ResourceLocation("photoniccraft:textures/blocks/laserdetector.png")));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserDetector2.class, new TileEntityLaserDetectorRenderer(new ResourceLocation("photoniccraft:textures/blocks/laserdetector2.png")));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFloodlight.class, new TileEntityFloodlightRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrism.class, new TileEntityPrismRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluorescentLight.class, new TileEntityFluorescentLightRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMicrowaveOven.class, new TileEntityMicrowaveOvenRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluorescentBlacklight.class, new TileEntityFluorescentBlacklightRenderer());
		}

	@Override
	public void preinit() {
		NetworkRegistry.INSTANCE.registerGuiHandler(ModPhotonicCraft.instance, new PhotonicGuiHandler());
		setupRadio();
	}

	private void setupRadio() {
		ModPhotonicCraft.radioFormat = new AudioFormat(Encoding.PCM_UNSIGNED, 11025.0f, 8, 1, 1, 11025.0f, false);
		ModPhotonicCraft.dataLineInfo = new DataLine.Info(SourceDataLine.class, ModPhotonicCraft.radioFormat, 88200);
		PhotonicAPI.lastPing = 0L;
		PhotonicAPI.radioPlaying = false;
		try {
			ModPhotonicCraft.sourceDataLine = (SourceDataLine) AudioSystem.getLine(ModPhotonicCraft.dataLineInfo);
			ModPhotonicCraft.sourceDataLine.open(ModPhotonicCraft.radioFormat, 88200);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Cannot use Radio: line not available");
			return;
		}
        /*if (ModPhotonicCraft.sourceDataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volume = (FloatControl) ModPhotonicCraft.sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-8.0F);
        }*/
		PhotonicAPI.radioTimer = new Timer();
		PhotonicAPI.radioTask = null;
	}

	@Override
	public void serverLoad() {
	}
}
