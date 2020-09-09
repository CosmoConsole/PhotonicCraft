package email.com.gmail.cosmoconsole.forge.photoniccraft.common;

import static email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft.laserItems;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockAnalogRedstoneLamp;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockAnorthosite;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockBlacklight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockErbiumBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockErbiumOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockFloodlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockGammaRayCannon;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockGlisterstone;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockHahniumBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockHahniumOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockHalogenLamp;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockHalogenLampDoubleSlab;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockHalogenLampHalfSlab;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockInfiniteSink;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockInfiniteSource;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserDetector2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserMirror2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserPulsed;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserRedstoneMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserSemiMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserSemiMirror2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLightAir;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLightDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockMercuryOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockMerger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockMicrowaveGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockMicrowavePanel;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockPrism;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockRadioReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockRadioTransceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockRadioTransmitter;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockRemoteReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockRheniumBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockRheniumOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockSkyLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockTantalumBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockTantalumOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockTerahertzTransmitter;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockVantaBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockXRayReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockXRayTransmitter;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockYttriumBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockYttriumOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemHalogenSlab;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemLaserBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.NameAndItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * The class where PhotonicCraft's Block classes are easily accessible. They are also registered here.
 */
public class PhotonicBlocks {
	public static Block erbiumOre;
	public static Block erbiumBlock;
	public static Block yttriumOre;
	public static Block yttriumBlock;
	public static Block mercuryOre;
	public static Block laser;
	public static Block laserMirror;
	public static Block laserMirror2;
	public static Block laserSemiMirror;
	public static Block laserSemiMirror2;
	public static Block laserDetector;
	public static Block laserDetector2;
	public static Block skyLight;
	public static Block prism;
	public static Block floodlight;
	public static Block lightAir;
	public static Block fluorescent;
	public static Block glisterstone;
	public static Block laserMerger;
	public static Block laserGenerator;
	public static Block lightDetector;
	public static Block laserCharger;
	public static Block analoglamp;
	public static Block remoteReceiver;
	public static Block microwave;
	public static Block laserRedstoneMirror;
	public static Block laserRedstoneMirrorOn;
	public static Block tantalumOre;
	public static Block tantalumBlock;
	public static Block anorthosite;
	public static Block microwavePanel;
	public static Block microwaveGenerator;
	public static Block hahniumOre;
	public static Block hahniumBlock;
	public static Block radioReceiver;
	public static Block radioTransmitter;
	public static Block radioTransceiver;
	public static Block terahertzTransmitter;
	public static Block blacklight;
	public static Block rheniumOre;
	public static Block rheniumBlock;
	public static Block pulsedLaser;
	public static Block gammaRayCannon;
	public static Block halogenLamp;
	public static BlockSlab halogenSlab;
	public static BlockSlab halogenDoubleSlab;
	public static Block xrayTransmitter;
	public static Block xrayReceiver;
	public static Block infiniteSource;
	public static Block infiniteSink;
	public static Block vantablock;

	private static void goRegisterBlock(IForgeRegistry<Block> registry, Block block, String name, boolean createItem) {
		block.setRegistryName(name);
		registry.register(block);
		if (createItem) {
			ItemBlock ib = new ItemBlock(block);
			ib.setRegistryName(name);
			PhotonicItems.blockItemList.add(new NameAndItem(name, ib));
		}
	}
	
	private static void goRegisterItemBlock(ItemBlock item) {
		item.setRegistryName(item.getBlock().getRegistryName());
		PhotonicItems.blockItemList.add(new NameAndItem(item.getBlock().getRegistryName().getResourcePath(), item));
	}
	
	public static void registerBlocks(IForgeRegistry<Block> registry) {
		goRegisterBlock(registry, PhotonicBlocks.erbiumOre = new BlockErbiumOre(), "erbiumore", true);
		goRegisterBlock(registry, PhotonicBlocks.erbiumBlock = new BlockErbiumBlock(MapColor.PINK), "erbiumblock",
				true);
		goRegisterBlock(registry, PhotonicBlocks.yttriumOre = new BlockYttriumOre(), "yttriumore", true);
		goRegisterBlock(registry, PhotonicBlocks.yttriumBlock = new BlockYttriumBlock(MapColor.RED), "yttriumblock",
				true);
		goRegisterBlock(registry, PhotonicBlocks.mercuryOre = new BlockMercuryOre(), "mercuryore", true);
		goRegisterBlock(registry, PhotonicBlocks.laser = new BlockLaser(), "laser", false);
		goRegisterBlock(registry, PhotonicBlocks.laserMirror = new BlockLaserMirror(), "lasermirror", false);
		goRegisterBlock(registry, PhotonicBlocks.laserMirror2 = new BlockLaserMirror2(), "lasermirror2", false);
		goRegisterBlock(registry, PhotonicBlocks.laserSemiMirror = new BlockLaserSemiMirror(), "lasersemimirror",
				false);
		goRegisterBlock(registry, PhotonicBlocks.laserSemiMirror2 = new BlockLaserSemiMirror2(), "lasersemimirror2",
				false);
		goRegisterBlock(registry, PhotonicBlocks.laserDetector = new BlockLaserDetector(), "laserdetector", false);
		goRegisterBlock(registry, PhotonicBlocks.laserDetector2 = new BlockLaserDetector2(), "laserdetector2", false);
		goRegisterBlock(registry, PhotonicBlocks.skyLight = new BlockSkyLight(), "skylight", true);
		goRegisterBlock(registry, PhotonicBlocks.prism = new BlockPrism(), "prism", false);
		goRegisterBlock(registry, PhotonicBlocks.floodlight = new BlockFloodlight(), "floodlight", false);
		goRegisterBlock(registry, PhotonicBlocks.lightAir = new BlockLightAir(), "lightair", false);
		goRegisterBlock(registry, PhotonicBlocks.fluorescent = new BlockFluorescentLight(), "fluorescent", false);
		goRegisterBlock(registry, PhotonicBlocks.laserMerger = new BlockMerger(), "lasermerger", true);
		goRegisterBlock(registry, PhotonicBlocks.glisterstone = new BlockGlisterstone(), "glisterstone", true);
		goRegisterBlock(registry, PhotonicBlocks.laserGenerator = new BlockLaserGenerator(), "lasergenerator", true);
		goRegisterBlock(registry, PhotonicBlocks.lightDetector = new BlockLightDetector(), "lightdetector", true);
		goRegisterBlock(registry, PhotonicBlocks.laserCharger = new BlockLaserCharger(), "lasercharger", true);
		goRegisterBlock(registry, PhotonicBlocks.analoglamp = new BlockAnalogRedstoneLamp(), "analoglamp", true);
		goRegisterBlock(registry, PhotonicBlocks.remoteReceiver = new BlockRemoteReceiver(), "remotereceiver", true);
		goRegisterBlock(registry, PhotonicBlocks.microwave = new BlockMicrowaveOven(), "microwaveoven", false);
		goRegisterBlock(registry, PhotonicBlocks.laserRedstoneMirror = new BlockLaserRedstoneMirror(),
				"laserredstonemirror", false);
		goRegisterBlock(registry, PhotonicBlocks.laserRedstoneMirrorOn = new BlockLaserRedstoneMirror(),
				"laserredstonemirroron", false);
		goRegisterBlock(registry, PhotonicBlocks.tantalumOre = new BlockTantalumOre(), "tantalumore", true);
		goRegisterBlock(registry, PhotonicBlocks.tantalumBlock = new BlockTantalumBlock(MapColor.DIAMOND),
				"tantalumblock", true);
		goRegisterBlock(registry, PhotonicBlocks.anorthosite = new BlockAnorthosite(), "anorthosite", true);
		goRegisterBlock(registry, PhotonicBlocks.microwavePanel = new BlockMicrowavePanel(), "microwavepanel", true);
		goRegisterBlock(registry, PhotonicBlocks.microwaveGenerator = new BlockMicrowaveGenerator(),
				"microwavegenerator", true);
		goRegisterBlock(registry, PhotonicBlocks.hahniumOre = new BlockHahniumOre(), "hahniumore", true);
		goRegisterBlock(registry, PhotonicBlocks.hahniumBlock = new BlockHahniumBlock(MapColor.GREEN), "hahniumblock",
				true);
		goRegisterBlock(registry, PhotonicBlocks.radioReceiver = new BlockRadioReceiver(), "radioreceiver", true);
		goRegisterBlock(registry, PhotonicBlocks.radioTransmitter = new BlockRadioTransmitter(), "radiotransmitter",
				true);
		goRegisterBlock(registry, PhotonicBlocks.radioTransceiver = new BlockRadioTransceiver(), "radiotransceiver",
				true);
		goRegisterBlock(registry, PhotonicBlocks.terahertzTransmitter = new BlockTerahertzTransmitter(),
				"terahertztransmitter", true);
		goRegisterBlock(registry, PhotonicBlocks.blacklight = new BlockBlacklight(), "blacklight", false);
		goRegisterBlock(registry, PhotonicBlocks.rheniumOre = new BlockRheniumOre(), "rheniumore", true);
		goRegisterBlock(registry, PhotonicBlocks.rheniumBlock = new BlockRheniumBlock(MapColor.QUARTZ), "rheniumblock",
				true);
		goRegisterBlock(registry, PhotonicBlocks.pulsedLaser = new BlockLaserPulsed(), "pulsedlaser", false);
		goRegisterBlock(registry, PhotonicBlocks.gammaRayCannon = new BlockGammaRayCannon(), "gammaraycannon", true);
		goRegisterBlock(registry, PhotonicBlocks.halogenLamp = new BlockHalogenLamp(), "halogen", true);
		goRegisterBlock(registry, PhotonicBlocks.halogenSlab = new BlockHalogenLampHalfSlab(), "halogenslab", false);
		goRegisterBlock(registry, PhotonicBlocks.halogenDoubleSlab = new BlockHalogenLampDoubleSlab(), "halogendoubleslab", false);
		goRegisterBlock(registry, PhotonicBlocks.xrayReceiver = new BlockXRayReceiver(), "xrayreceiver", true);
		goRegisterBlock(registry, PhotonicBlocks.xrayTransmitter = new BlockXRayTransmitter(), "xraytransmitter", true);
		goRegisterBlock(registry, PhotonicBlocks.infiniteSource = new BlockInfiniteSource(), "infiniterfsource", true);
		goRegisterBlock(registry, PhotonicBlocks.infiniteSink = new BlockInfiniteSink(), "infiniterfsink", true);
		goRegisterBlock(registry, PhotonicBlocks.vantablock = new BlockVantaBlock(MapColor.BLACK), "vantablock", true);
		
		ItemSlab halogenSlabItem = new ItemHalogenSlab(PhotonicBlocks.halogenSlab, PhotonicBlocks.halogenSlab, PhotonicBlocks.halogenDoubleSlab);
		goRegisterItemBlock(halogenSlabItem);

		// fill the NameAndItem lists
		int destIndex = 0;
		Block[] laserBlocks = new Block[] { PhotonicBlocks.laser, PhotonicBlocks.laserMirror,
				PhotonicBlocks.laserMirror2, PhotonicBlocks.laserSemiMirror, PhotonicBlocks.laserSemiMirror2,
				PhotonicBlocks.laserDetector, PhotonicBlocks.laserDetector2, PhotonicBlocks.floodlight,
				PhotonicBlocks.prism, PhotonicBlocks.fluorescent, PhotonicBlocks.microwave,
				PhotonicBlocks.laserRedstoneMirror, PhotonicBlocks.blacklight, PhotonicBlocks.pulsedLaser };
		int ij = 0;
		PhotonicItems.laserItems = new Item[laserBlocks.length];
		for (String s : new String[] { "laser", "lasermirror", "lasermirror2", "lasersemimirror", "lasersemimirror2",
				"laserdetector", "laserdetector2", "floodlight", "prism", "fluorescent", "microwave",
				"laserredstonemirror", "blacklight", "pulsedlaser" }) {
			ItemLaserBlock lib = new ItemLaserBlock(laserBlocks[ij++], "ib" + s);
			lib.setRegistryName("ib" + s);
			PhotonicItems.laserItems[destIndex++] = lib;
			laserItems.add(new NameAndItem(s, lib));
		}
		PhotonicItems.createPhotonicResources();
	}

	private PhotonicBlocks() {
	}
}
