package email.com.gmail.cosmoconsole.forge.photoniccraft.common.registrar;

import static email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft.photonicResources;
import static email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft.photonicResources2;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityFloodlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityFluorescentBlacklight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityGammaRayCannon;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityGlisterstone;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityInfiniteSink;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityInfiniteSource;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserDetector2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserMerger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserPulsed;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserRedstoneMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLightAir;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLightDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityMicrowaveGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityPrism;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityRadioReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityRadioTransceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityRadioTransmitter;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityRemoteReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntitySkyLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityTerahertzTransmitter;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityXRayReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityXRayTransmitter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * The class in which miscellaneous stuff is registered. Currently tile entities and ore dictionary entries are registered here.
 */
public class PhotonicMiscRegistrar {

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityLaser.class, new ResourceLocation(ModPhotonicCraft.MODID, "Laser"));
		GameRegistry.registerTileEntity(TileEntityLaserDetector.class, new ResourceLocation(ModPhotonicCraft.MODID, "LaserDetector"));
		GameRegistry.registerTileEntity(TileEntityLaserDetector2.class, new ResourceLocation(ModPhotonicCraft.MODID, "LaserDetector2"));
		GameRegistry.registerTileEntity(TileEntityPrism.class, new ResourceLocation(ModPhotonicCraft.MODID, "Prism"));
		GameRegistry.registerTileEntity(TileEntityLightAir.class, new ResourceLocation(ModPhotonicCraft.MODID, "LightAir"));
		GameRegistry.registerTileEntity(TileEntityFloodlight.class, new ResourceLocation(ModPhotonicCraft.MODID, "Floodlight"));
		GameRegistry.registerTileEntity(TileEntitySkyLight.class, new ResourceLocation(ModPhotonicCraft.MODID, "SkyBox"));
		GameRegistry.registerTileEntity(TileEntityFluorescentLight.class, new ResourceLocation(ModPhotonicCraft.MODID, "Fluorescentlight"));
		GameRegistry.registerTileEntity(TileEntityGlisterstone.class, new ResourceLocation(ModPhotonicCraft.MODID, "Glisterstone"));
		GameRegistry.registerTileEntity(TileEntityLaserMerger.class, new ResourceLocation(ModPhotonicCraft.MODID, "Lasermerger"));
		GameRegistry.registerTileEntity(TileEntityLaserGenerator.class, new ResourceLocation(ModPhotonicCraft.MODID, "LaserGenerator"));
		GameRegistry.registerTileEntity(TileEntityLightDetector.class, new ResourceLocation(ModPhotonicCraft.MODID, "LightDetector"));
		GameRegistry.registerTileEntity(TileEntityLaserCharger.class, new ResourceLocation(ModPhotonicCraft.MODID, "LaserCharger"));
		GameRegistry.registerTileEntity(TileEntityRemoteReceiver.class, new ResourceLocation(ModPhotonicCraft.MODID, "RemoteReceiver"));
		GameRegistry.registerTileEntity(TileEntityMicrowaveOven.class, new ResourceLocation(ModPhotonicCraft.MODID, "MicrowaveOven"));
		GameRegistry.registerTileEntity(TileEntityLaserRedstoneMirror.class, new ResourceLocation(ModPhotonicCraft.MODID, "LaserRedstoneMirror"));
		GameRegistry.registerTileEntity(TileEntityMicrowaveGenerator.class, new ResourceLocation(ModPhotonicCraft.MODID, "MicrowaveGenerator"));
		GameRegistry.registerTileEntity(TileEntityRadioTransmitter.class, new ResourceLocation(ModPhotonicCraft.MODID, "RadioTransmitter"));
		GameRegistry.registerTileEntity(TileEntityRadioReceiver.class, new ResourceLocation(ModPhotonicCraft.MODID, "RadioReceiver"));
		GameRegistry.registerTileEntity(TileEntityRadioTransceiver.class, new ResourceLocation(ModPhotonicCraft.MODID, "RadioTransceiver"));
		GameRegistry.registerTileEntity(TileEntityTerahertzTransmitter.class, new ResourceLocation(ModPhotonicCraft.MODID, "TerahertzTransmitter"));
		GameRegistry.registerTileEntity(TileEntityFluorescentBlacklight.class, new ResourceLocation(ModPhotonicCraft.MODID, "FluorescentBlacklight"));
		GameRegistry.registerTileEntity(TileEntityLaserPulsed.class, new ResourceLocation(ModPhotonicCraft.MODID, "LaserPulsed"));
		GameRegistry.registerTileEntity(TileEntityGammaRayCannon.class, new ResourceLocation(ModPhotonicCraft.MODID, "Deathray"));
		GameRegistry.registerTileEntity(TileEntityXRayReceiver.class, new ResourceLocation(ModPhotonicCraft.MODID, "XrayReceiver"));
		GameRegistry.registerTileEntity(TileEntityXRayTransmitter.class, new ResourceLocation(ModPhotonicCraft.MODID, "XrayTransmitter"));
		GameRegistry.registerTileEntity(TileEntityInfiniteSource.class, new ResourceLocation(ModPhotonicCraft.MODID, "InfiniteSource"));
		GameRegistry.registerTileEntity(TileEntityInfiniteSink.class, new ResourceLocation(ModPhotonicCraft.MODID, "InfiniteSink"));
	}

	public static void registerOres() {
		OreDictionary.registerOre("blockErbium", new ItemStack(PhotonicBlocks.erbiumBlock));
		OreDictionary.registerOre("oreErbium", new ItemStack(PhotonicBlocks.erbiumOre));
		OreDictionary.registerOre("ingotErbium", new ItemStack(photonicResources.get(0).getItem(), 1));
		OreDictionary.registerOre("blockTantalum", new ItemStack(PhotonicBlocks.tantalumBlock));
		OreDictionary.registerOre("oreTantalum", new ItemStack(PhotonicBlocks.tantalumOre));
		OreDictionary.registerOre("ingotTantalum", new ItemStack(photonicResources2.get(1).getItem(), 1));
		OreDictionary.registerOre("oreMercury", new ItemStack(PhotonicBlocks.mercuryOre));
		OreDictionary.registerOre("materialMercury", new ItemStack(photonicResources.get(13).getItem(), 1));
		OreDictionary.registerOre("blockHahnium", new ItemStack(PhotonicBlocks.hahniumBlock));
		OreDictionary.registerOre("oreHahnium", new ItemStack(PhotonicBlocks.hahniumOre));
		OreDictionary.registerOre("ingotHahnium", new ItemStack(photonicResources2.get(3).getItem(), 1));
		OreDictionary.registerOre("blockBarium", new ItemStack(PhotonicBlocks.rheniumBlock));
		OreDictionary.registerOre("oreBarium", new ItemStack(PhotonicBlocks.rheniumOre));
		OreDictionary.registerOre("ingotBarium", new ItemStack(photonicResources2.get(6).getItem(), 1));
	}

	private PhotonicMiscRegistrar() {
	}
}
