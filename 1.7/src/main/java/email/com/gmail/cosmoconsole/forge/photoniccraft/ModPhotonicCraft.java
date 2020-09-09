package email.com.gmail.cosmoconsole.forge.photoniccraft;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import dan200.computercraft.ComputerCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockAnalogRedstoneLamp;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockAnorthosite;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockBariumBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockBariumOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockBlacklight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockErbiumBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockErbiumOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockFloodlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockGlisterstone;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockHahniumBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockHahniumOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLaserDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLaserDetector2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLaserGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLaserMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLaserMirror2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLaserRedstoneMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLaserSemiMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLaserSemiMirror2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLightAir;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLightDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockMercuryOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockMerger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockMicrowaveGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockMicrowavePanel;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockPrism;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockRadioReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockRadioTransceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockRadioTransmitter;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockRemoteReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockSkyLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockTantalumBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockTantalumOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockTerahertzTransmitter;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockYttriumBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockYttriumOre;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityColossalCreeper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserPointer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemBeefJerky;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemDecorativeArmor;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemFlashlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemHypnoBomb;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemIRGlasses;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemLaserBlock;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemLaserPointer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemMiningHelmet;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemNougat;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemOreRadar;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemPhotonic;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemPhotonic2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemPhotonicCoupler;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemPocketRadio;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemRangefinder;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemRemote;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemTerahertzReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemYttriumArmor;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemYttriumAxe;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemYttriumHoe;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemYttriumIngot;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemYttriumPickaxe;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemYttriumSpade;
import email.com.gmail.cosmoconsole.forge.photoniccraft.item.ItemYttriumSword;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFloodlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFluorescentBlacklight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityGlisterstone;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserDetector2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserMerger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserMirror2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserRedstoneMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserSemiMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserSemiMirror2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLightAir;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLightDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityMicrowaveGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityPrism;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityRadioTransmitter;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityRemoteReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntitySkyLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityTerahertzTransmitter;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = ModPhotonicCraft.MODID, version = ModPhotonicCraft.VERSION)
public class ModPhotonicCraft
{
    public static final String MODID = "photoniccraft";
    public static final String VERSION = "0.9.5";

    @SidedProxy(clientSide="email.com.gmail.cosmoconsole.forge.photoniccraft.client.PhotonicClientProxy", serverSide="email.com.gmail.cosmoconsole.forge.photoniccraft.server.PhotonicServerProxy")
    static PhotonicCommonProxy proxy;

	public static ToolMaterial YTTRIUM_TOOL = EnumHelper.addToolMaterial("YTTRIUM", 5, 7415, 17.0F, 5.0F, 21);
	public static ArmorMaterial DECO_ARMOR = EnumHelper.addArmorMaterial("DECORATIVE", 100000, new int[]{0, 0, 0, 0}, 0);
	public static ArmorMaterial YTTRIUM_ARMOR = EnumHelper.addArmorMaterial("YTTRIUM", 59, new int[]{4, 8, 7, 3}, 22);
	
	public static SimpleNetworkWrapper network;
	
	public static AudioFormat radioFormat = null;
	public static DataLine.Info dataLineInfo = null;
	public static SourceDataLine sourceDataLine = null;
	public static int maximumRadio = 0;
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	instance = this;
    	erbiumOre = new BlockErbiumOre();
    	erbiumBlock = new BlockErbiumBlock(MapColor.pinkColor);
    	mercuryOre = new BlockMercuryOre();
    	photonicResource = new ItemPhotonic();
    	photonicCoupler = new ItemPhotonicCoupler();
    	laser = new BlockLaser();
    	laserMirror = new BlockLaserMirror();
    	laserMirror2 = new BlockLaserMirror2();
    	laserSemiMirror = new BlockLaserSemiMirror();
    	laserSemiMirror2 = new BlockLaserSemiMirror2();
    	laserDetector = new BlockLaserDetector();
    	laserDetector2 = new BlockLaserDetector2();
    	skyLight = new BlockSkyLight();
    	floodlight = new BlockFloodlight();
    	prism = new BlockPrism();
    	lightAir = new BlockLightAir();
    	yttriumOre = new BlockYttriumOre();
    	fluorescent = new BlockFluorescentLight();
    	yttriumBlock = new BlockYttriumBlock(MapColor.redColor);
    	microwave = new BlockMicrowaveOven();
    	laserRedstoneMirror = new BlockLaserRedstoneMirror();
    	photonicResource2 = new ItemPhotonic2();
    	blacklight = new BlockBlacklight();
    	//blockMirror = new ItemHangingEntity(EntityMirror.class).setUnlocalizedName(MODID + "_blockMirror").setTextureName(MODID + ":blockMirror");
        
    	laserItem = new ItemLaserBlock(new Block[]{laser, laserMirror, laserMirror2, laserSemiMirror, laserSemiMirror2, laserDetector, laserDetector2, floodlight, prism, fluorescent, microwave, laserRedstoneMirror, blacklight});
    	
    	Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "PhotonicCraft.cfg"));
    	config.load();
    	
    	colossalCreepers = config.get("mob", "spawnColossalCreepers", true).getBoolean();
    	maximumRadio  = config.get("item", "maximumRadioListeners", 0).getInt();
    	
    	config.save();
    	
    	network = NetworkRegistry.INSTANCE.newSimpleChannel("PhotonicCraft_radio");
        ModPhotonicCraft.network.registerMessage(PhotonicRadioPacket.Handler.class, PhotonicRadioPacket.class, 40, Side.CLIENT);

        // TODO: x-ray tube, uses for x-ray and gamma
        
        GameRegistry.registerBlock(erbiumOre, "erbiumOre");
    	GameRegistry.registerBlock(erbiumBlock, "erbiumBlock");
        GameRegistry.registerBlock(yttriumOre, "yttriumOre");
    	GameRegistry.registerBlock(yttriumBlock, "yttriumBlock");
    	GameRegistry.registerBlock(mercuryOre, "mercuryOre");
    	GameRegistry.registerItem(photonicResource, "itemResource");
    	GameRegistry.registerItem(photonicCoupler, "outputCoupler");
    	GameRegistry.registerBlock(laser, "laser");
    	GameRegistry.registerBlock(laserMirror, "laserMirror");
    	GameRegistry.registerBlock(laserMirror2, "laserMirror2");
    	GameRegistry.registerBlock(laserSemiMirror, "laserSemiMirror");
    	GameRegistry.registerBlock(laserSemiMirror2, "laserSemiMirror2");
    	GameRegistry.registerBlock(laserDetector, "laserDetector");
    	GameRegistry.registerBlock(laserDetector2, "laserDetector2");
    	GameRegistry.registerItem(laserItem, "laserItem");
    	GameRegistry.registerItem(yttriumIngot = new ItemYttriumIngot(), "yttrium_ingot");
    	GameRegistry.registerItem(flashlight = new ItemFlashlight(), "flashlight");
    	GameRegistry.registerItem(rangefinder = new ItemRangefinder(), "rangefinder");
    	GameRegistry.registerItem(laserpointer = new ItemLaserPointer(), "laserpointer");
    	GameRegistry.registerItem(oreRadar = new ItemOreRadar(), "oreRadar");
    	GameRegistry.registerBlock(skyLight, "skyLight");
    	GameRegistry.registerBlock(prism, "prism");
    	GameRegistry.registerBlock(floodlight, "floodlight");
    	GameRegistry.registerBlock(lightAir, "lightAir");
    	GameRegistry.registerBlock(fluorescent, "fluorescent");
    	GameRegistry.registerBlock(laserMerger = new BlockMerger(), "laserMerger");
    	GameRegistry.registerBlock(glisterstone = new BlockGlisterstone(), "glisterstone");
    	GameRegistry.registerBlock(laserGenerator = new BlockLaserGenerator(), "laserGenerator");
    	GameRegistry.registerBlock(lightDetector = new BlockLightDetector(), "lightDetector");
    	GameRegistry.registerBlock(laserCharger = new BlockLaserCharger(), "laserCharger");
    	GameRegistry.registerBlock(analoglamp = new BlockAnalogRedstoneLamp(), "analogLamp");
    	GameRegistry.registerBlock(remoteReceiver = new BlockRemoteReceiver(), "remoteReceiver");
    	GameRegistry.registerItem(nougat = new ItemNougat(4, 0.6f, false), "nougat");
    	GameRegistry.registerItem(remote = new ItemRemote(), "remoteControl");
    	GameRegistry.registerBlock(microwave, "microwaveOven");
    	GameRegistry.registerBlock(laserRedstoneMirror, "laserRedstoneMirror");
    	GameRegistry.registerItem(photonicResource2, "itemResource2");
    	GameRegistry.registerBlock(tantalumOre = new BlockTantalumOre(), "tantalumOre");
    	GameRegistry.registerBlock(tantalumBlock = new BlockTantalumBlock(MapColor.diamondColor), "tantalumBlock");
    	GameRegistry.registerItem(hypnoBomb = new ItemHypnoBomb(), "hypnoBomb");
    	GameRegistry.registerBlock(anorthosite = new BlockAnorthosite(), "anorthosite");
    	GameRegistry.registerBlock(microwavePanel = new BlockMicrowavePanel(), "microwavePanel");
    	GameRegistry.registerBlock(microwaveGenerator = new BlockMicrowaveGenerator(), "microwaveGenerator");
    	GameRegistry.registerBlock(hahniumOre = new BlockHahniumOre(), "hahniumOre");
    	GameRegistry.registerBlock(hahniumBlock = new BlockHahniumBlock(MapColor.greenColor), "hahniumBlock");
    	GameRegistry.registerBlock(radioReceiver = new BlockRadioReceiver(), "radioReceiver");
    	GameRegistry.registerBlock(radioTransmitter = new BlockRadioTransmitter(), "radioTransmitter");
    	GameRegistry.registerBlock(radioTransceiver = new BlockRadioTransceiver(), "radioTransceiver");
    	GameRegistry.registerItem(pocketRadio = new ItemPocketRadio(), "pocketRadio");
    	GameRegistry.registerItem(terahertzReceiver = new ItemTerahertzReceiver(), "terahertzReceiver");
    	GameRegistry.registerBlock(terahertzTransmitter = new BlockTerahertzTransmitter(), "terahertzTransmitter");
    	GameRegistry.registerBlock(blacklight, "blacklight");
    	GameRegistry.registerBlock(bariumOre = new BlockBariumOre(), "bariumOre");
    	GameRegistry.registerBlock(bariumBlock = new BlockBariumBlock(MapColor.quartzColor), "bariumBlock");
    	GameRegistry.registerItem(beefJerky = new ItemBeefJerky(7, 10.0f, true), "beefJerky");
    	//GameRegistry.registerItem(blockMirror, "blockMirror");
    	
    	GameRegistry.registerItem(miningHelmet = new ItemMiningHelmet("photoniccraft_miningHelmet", DECO_ARMOR, "mining", 0), "miningHelmet");
    	GameRegistry.registerItem(irglasses = new ItemIRGlasses("photoniccraft_irglasses", DECO_ARMOR, "irglasses", 0), "irglasses"); 
    	GameRegistry.registerItem(safetyglasses = new ItemDecorativeArmor("photoniccraft_safetyglasses", DECO_ARMOR, "safetyglasses", 0), "safetyglasses"); 
    	
    	GameRegistry.registerItem(yttriumHelmet = new ItemYttriumArmor("photoniccraft_yttrium_helmet", YTTRIUM_ARMOR, "yttrium", 0), "yttrium_helmet"); //0 for helmet
    	GameRegistry.registerItem(yttriumChestplate = new ItemYttriumArmor("photoniccraft_yttrium_chestplate", YTTRIUM_ARMOR, "yttrium", 1), "yttrium_chestplate"); // 1 for chestplate
    	GameRegistry.registerItem(yttriumLeggings = new ItemYttriumArmor("photoniccraft_yttrium_leggings", YTTRIUM_ARMOR, "yttrium", 2), "yttrium_leggings"); // 2 for leggings
    	GameRegistry.registerItem(yttriumBoots = new ItemYttriumArmor("photoniccraft_yttrium_boots", YTTRIUM_ARMOR, "yttrium", 3), "yttrium_boots"); // 3 for boots
    	
    	GameRegistry.registerItem(yttriumPickaxe = new ItemYttriumPickaxe("photoniccraft_yttrium_pickaxe", YTTRIUM_TOOL), "yttrium_pickaxe");
    	GameRegistry.registerItem(yttriumAxe = new ItemYttriumAxe("photoniccraft_yttrium_axe", YTTRIUM_TOOL), "yttrium_axe");
    	GameRegistry.registerItem(yttriumSpade = new ItemYttriumSpade("photoniccraft_yttrium_spade", YTTRIUM_TOOL), "yttrium_spade");
    	GameRegistry.registerItem(yttriumHoe = new ItemYttriumHoe("photoniccraft_yttrium_hoe", YTTRIUM_TOOL), "yttrium_hoe");
    	GameRegistry.registerItem(yttriumSword = new ItemYttriumSword("photoniccraft_yttrium_sword", YTTRIUM_TOOL), "yttrium_sword");
    	
    	GameRegistry.registerTileEntity(TileEntityLaser.class, "photonicsLaser");
    	GameRegistry.registerTileEntity(TileEntityLaserMirror.class, "photonicsLaserMirror");
    	GameRegistry.registerTileEntity(TileEntityLaserMirror2.class, "photonicsLaserMirror2");
    	GameRegistry.registerTileEntity(TileEntityLaserSemiMirror.class, "photonicsLaserSemiMirror");
    	GameRegistry.registerTileEntity(TileEntityLaserSemiMirror2.class, "photonicsLaserSemiMirror2");
    	GameRegistry.registerTileEntity(TileEntityLaserDetector.class, "photonicsLaserDetector");
    	GameRegistry.registerTileEntity(TileEntityLaserDetector2.class, "photonicsLaserDetector2");
    	GameRegistry.registerTileEntity(TileEntityPrism.class, "photonicsPrism");
    	GameRegistry.registerTileEntity(TileEntityLightAir.class, "photonicsLightAir");
    	GameRegistry.registerTileEntity(TileEntityFloodlight.class, "photonicsFloodlight");
    	GameRegistry.registerTileEntity(TileEntitySkyLight.class, "photonicsSkyBox");
    	GameRegistry.registerTileEntity(TileEntityFluorescentLight.class, "photonicsFluorescentlight");
    	GameRegistry.registerTileEntity(TileEntityGlisterstone.class, "photonicsGlisterstone");
    	GameRegistry.registerTileEntity(TileEntityLaserMerger.class, "photonicsLasermerger");
    	GameRegistry.registerTileEntity(TileEntityLaserGenerator.class, "photonicsLaserGenerator");
    	GameRegistry.registerTileEntity(TileEntityLightDetector.class, "photonicsLightDetector");
    	GameRegistry.registerTileEntity(TileEntityLaserCharger.class, "photonicsLaserCharger");
    	GameRegistry.registerTileEntity(TileEntityRemoteReceiver.class, "photonicsRemoteReceiver");
    	GameRegistry.registerTileEntity(TileEntityMicrowaveOven.class, "photonicsMicrowaveOven");
    	GameRegistry.registerTileEntity(TileEntityLaserRedstoneMirror.class, "photonicsLaserRedstoneMirror");
    	GameRegistry.registerTileEntity(TileEntityMicrowaveGenerator.class, "photonicsMicrowaveGenerator");
    	GameRegistry.registerTileEntity(TileEntityRadioTransmitter.class, "photonicsRadioTransmitter");
    	GameRegistry.registerTileEntity(TileEntityTerahertzTransmitter.class, "photonicsTerahertzTransmitter");
    	GameRegistry.registerTileEntity(TileEntityFluorescentBlacklight.class, "photonicsFluorescentBlacklight");
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(instance, new PhotonicGuiHandler());

    	OreDictionary.registerOre("blockErbium", new ItemStack(erbiumBlock));
    	OreDictionary.registerOre("oreErbium", new ItemStack(erbiumOre));
    	OreDictionary.registerOre("ingotErbium", new ItemStack(photonicResource, 1, 0));
    	OreDictionary.registerOre("blockTantalum", new ItemStack(tantalumBlock));
    	OreDictionary.registerOre("oreTantalum", new ItemStack(tantalumOre));
    	OreDictionary.registerOre("ingotTantalum", new ItemStack(photonicResource2, 1, 1));
    	OreDictionary.registerOre("oreMercury", new ItemStack(mercuryOre));
    	OreDictionary.registerOre("materialMercury", new ItemStack(photonicResource, 1, 13));
    	OreDictionary.registerOre("blockHahnium", new ItemStack(hahniumBlock));
    	OreDictionary.registerOre("oreHahnium", new ItemStack(hahniumOre));
    	OreDictionary.registerOre("ingotHahnium", new ItemStack(photonicResource2, 1, 3));
    	OreDictionary.registerOre("blockBarium", new ItemStack(bariumBlock));
    	OreDictionary.registerOre("oreBarium", new ItemStack(bariumOre));
    	OreDictionary.registerOre("ingotBarium", new ItemStack(photonicResource2, 1, 6));
    	
    	EntityRegistry.registerModEntity(EntityColossalCreeper.class, "ColossalCreeper", 1, this, 128, 1, true);
    	//EntityRegistry.registerModEntity(EntityMirror.class, "Mirror", 2, this, 128, 1, false);
    	EntityRegistry.registerModEntity(EntityLaserBeam.class, "PhotonicLaserBeam", 2, this, 1024, 1, false);
    	EntityRegistry.registerModEntity(EntityLaserEffect.class, "PhotonicLaserEffect", 4, this, 128, 1, true);
    	EntityRegistry.registerModEntity(EntityLaserPointer.class, "PhotonicLaserPointer", 3, this, 128, 1, false);
    	
    	if (colossalCreepers) {
	    	for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++) {
		    	if (BiomeGenBase.getBiomeGenArray()[i] != null) {
		    		EntityRegistry.addSpawn(EntityColossalCreeper.class, 5, 1, 1, EnumCreatureType.monster, BiomeGenBase.getBiomeGenArray()[i]);
		    	}
	    	}
	    }
    	
    		
    	MinecraftForge.EVENT_BUS.register(new PhotonicEventHandler());
    	FMLCommonHandler.instance().bus().register(new PhotonicEventHandler());
    	
    	GameRegistry.addSmelting(new ItemStack(erbiumOre, 1), new ItemStack(photonicResource, 1, 0), 0.8F);
    	GameRegistry.addSmelting(new ItemStack(tantalumOre, 1), new ItemStack(photonicResource2, 1, 1), 0.9F);
    	GameRegistry.addSmelting(new ItemStack(hahniumOre, 1), new ItemStack(photonicResource2, 1, 3), 0.6F);
    	GameRegistry.addSmelting(new ItemStack(bariumOre, 1), new ItemStack(photonicResource2, 1, 6), 0.6F);
    	
    	GameRegistry.addRecipe(new ItemStack(erbiumBlock), new Object[]{
    	     "AAA",
    	     "AAA",
    	     "AAA",
    	     'A', new ItemStack(photonicResource, 1, 0)
    	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 9, 0), new Object[]{
	   	     "A",
	   	     'A', new ItemStack(erbiumBlock, 1)
	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 1, 0), new Object[]{
	   	     "AAA",
	   	     "AAA",
	   	     "AAA",
	   	     'A', new ItemStack(photonicResource, 1, 3)
	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 9, 3), new Object[]{
	   	     "A",
	   	     'A', new ItemStack(photonicResource, 1, 0)
	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 6, 1), new Object[]{
	   	     "AAA",
	   	     "   ",
	   	     "AAA",
	   	     'A', new ItemStack(Blocks.glass_pane)
	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 1, 2), new Object[]{
	   	     "CAC",
	   	     "BBB",
	   	     "CDC",
	   	     'A', new ItemStack(photonicResource, 1, 0),
	   	     'B', new ItemStack(photonicResource, 1, 1),
	   	     'C', new ItemStack(Items.iron_ingot, 1),
	   	     'D', new ItemStack(Items.redstone, 1),
	   	     });
    	GameRegistry.addShapelessRecipe(new ItemStack(photonicResource, 1, 8), new Object[] {
    		 new ItemStack(Items.diamond, 1),
    		 new ItemStack(photonicResource, 1, 0)
    	});
    	GameRegistry.addShapelessRecipe(new ItemStack(photonicResource, 1, 9), new Object[] {
	   		 new ItemStack(photonicResource, 1, 0),
	   		 new ItemStack(Items.emerald, 1),
	   		 new ItemStack(photonicResource, 1, 8),
	   		 new ItemStack(photonicResource, 1, 8)
	   	});
    	GameRegistry.addShapelessRecipe(new ItemStack(photonicResource, 1, 10), new Object[] {
	   		 new ItemStack(photonicResource, 1, 9),
	   		 new ItemStack(photonicResource, 1, 9),
	   		 new ItemStack(photonicResource, 1, 9),
	   		 new ItemStack(photonicResource, 1, 9),
	   		 new ItemStack(photonicResource, 1, 11)
	   	});
    	GameRegistry.addShapelessRecipe(new ItemStack(photonicResource, 1, 10), new Object[] {
	   		 new ItemStack(photonicResource, 1, 9),
	   		 new ItemStack(photonicResource, 1, 9),
	   		 new ItemStack(photonicResource, 1, 9),
	   		 new ItemStack(photonicResource, 1, 9),
	   		 new ItemStack(Items.nether_star, 1)
	   	});
    	GameRegistry.addShapelessRecipe(new ItemStack(photonicResource, 1, 12), new Object[] {
	   		 new ItemStack(photonicResource, 1, 13),
	   		 new ItemStack(Blocks.glass_pane, 1)
	   	});
    	GameRegistry.addShapelessRecipe(new ItemStack(analoglamp, 1), new Object[] {
   	   		 new ItemStack(Blocks.redstone_lamp, 1),
   	   		 new ItemStack(Items.quartz, 1)
   	   	});
    	for (int i = 0; i < 16; i++)
        	GameRegistry.addRecipe(new ItemStack(photonicCoupler, 1, i), new Object[]{
        		"ABA",
	   	   	    'A', new ItemStack(photonicResource, 1, 12),
	   	   	    'B', new ItemStack(Items.dye, 1, i)
	   	   	    });
    	GameRegistry.addRecipe(new ItemStack(photonicResource2, 4, 2), new Object[]{
      	   	     "TGT",
      	   	     "R R",
      	   	     'T', new ItemStack(photonicResource2, 1, 1),
      	   	     'R', new ItemStack(Items.redstone, 1),
      	   	     'G', new ItemStack(Blocks.glass, 1),
      	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 1, 4), new Object[]{
   	   	     "GGG",
   	   	     "RER",
   	   	     "GGG",
   	   	     'E', new ItemStack(photonicResource, 1, 0),
   	   	     'R', new ItemStack(Items.redstone, 1),
   	   	     'G', new ItemStack(Blocks.glass_pane, 1),
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 1, 5), new Object[]{
      	   	     "GGG",
      	   	     "RER",
      	   	     "GGG",
      	   	     'E', new ItemStack(photonicResource, 1, 8),
      	   	     'R', new ItemStack(Items.redstone, 1),
      	   	     'G', new ItemStack(Blocks.glass_pane, 1),
      	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 1, 6), new Object[]{
     	   	     "GGG",
     	   	     "RER",
     	   	     "GGG",
     	   	     'E', new ItemStack(photonicResource, 1, 9),
     	   	     'R', new ItemStack(Items.redstone, 1),
     	   	     'G', new ItemStack(Blocks.glass_pane, 1),
     	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 1, 7), new Object[]{
     	   	     "GGG",
     	   	     "RER",
     	   	     "GGG",
     	   	     'E', new ItemStack(photonicResource, 1, 10),
     	   	     'R', new ItemStack(Items.redstone, 1),
     	   	     'G', new ItemStack(Blocks.glass_pane, 1),
     	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 1, 14), new Object[]{
    	   	     "GOG",
    	   	     "RER",
    	   	     "GOG",
    	   	     'E', new ItemStack(photonicResource, 1, 9),
    	   	     'O', new ItemStack(photonicResource, 1, 1),
    	   	     'R', new ItemStack(Blocks.redstone_block, 1),
    	   	     'G', new ItemStack(Blocks.glass_pane, 1),
    	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource, 1, 15), new Object[]{
   	   	     "RRR",
   	   	     "RER",
   	   	     "RRR",
   	   	     'E', new ItemStack(photonicResource, 1, 8),
   	   	     'R', new ItemStack(Items.redstone, 1),
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 0), new Object[]{
	   	     "ISI",
	   	     "TEG",
	   	     "ISI",
	   	     'E', new ItemStack(photonicResource, 1, 2),
	   	     'T', new ItemStack(photonicResource2, 1, 2),
	   	     'G', new ItemStack(Blocks.glass_pane, 1),
	   	     'S', new ItemStack(Blocks.stone, 1),
	   	     'I', new ItemStack(Items.iron_ingot, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 1), new Object[]{
   	   	     "III",
   	   	     " M ",
   	   	     "III",
   	   	     'M', new ItemStack(photonicResource, 1, 12),
   	   	     'I', new ItemStack(Items.iron_ingot, 1),
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 2), new Object[]{
      	   	     "III",
      	   	     "M M",
      	   	     "III",
      	   	     'M', new ItemStack(photonicResource, 1, 12),
      	   	     'I', new ItemStack(Items.iron_ingot, 1),
      	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 3), new Object[]{
      	   	     "III",
      	   	     " MC",
      	   	     "III",
      	   	     'M', new ItemStack(photonicResource, 1, 12),
      	   	     'C', new ItemStack(photonicResource, 1, 13),
      	   	     'I', new ItemStack(Items.iron_ingot, 1),
      	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 3), new Object[]{
     	   	     "III",
     	   	     "CM ",
     	   	     "III",
     	   	     'M', new ItemStack(photonicResource, 1, 12),
     	   	     'C', new ItemStack(photonicResource, 1, 13),
     	   	     'I', new ItemStack(Items.iron_ingot, 1),
     	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 4), new Object[]{
    	   	     "III",
    	   	     "MCM",
    	   	     "III",
    	   	     'M', new ItemStack(photonicResource, 1, 12),
    	   	     'C', new ItemStack(photonicResource, 1, 13),
    	   	     'I', new ItemStack(Items.iron_ingot, 1),
    	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 5), new Object[]{
   	   	     "IRI",
   	   	     "GTG",
   	   	     "IEI",
   	   	     'E', new ItemStack(photonicResource, 1, 0),
	   	     'R', new ItemStack(Items.redstone, 1),
	   	     'T', new ItemStack(photonicResource2, 1, 2),
   	   	     'G', new ItemStack(Blocks.glass, 1),
   	   	     'I', new ItemStack(Items.iron_ingot, 1),
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 6), new Object[]{
      	   	     "IRI",
      	   	     "GTG",
      	   	     "IEI",
      	   	     'E', new ItemStack(photonicResource, 1, 0),
   	   	     'R', new ItemStack(Items.redstone, 1),
	   	     'T', new ItemStack(photonicResource2, 1, 2),
      	   	     'G', new ItemStack(Blocks.glass, 1),
      	   	     'I', new ItemStack(Items.gold_ingot, 1),
      	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 7), new Object[]{
     	   	     "ICI",
     	   	     "LRG",
     	   	     "ICI",
     	   	     'L', new ItemStack(Blocks.glowstone, 1),
     	   	     'R', new ItemStack(Blocks.redstone_lamp, 1),
     	   	     'G', new ItemStack(Blocks.glass_pane, 1),
     	   	     'C', new ItemStack(Items.coal, 1),
     	   	     'I', new ItemStack(Items.iron_ingot, 1),
     	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 9), new Object[]{
   	   	     "ETE",
   	   	     "LLL",
   	   	     "GGG",
   	   	     'E', new ItemStack(photonicResource, 1, 0),
	   	     'T', new ItemStack(photonicResource2, 1, 2),
   	   	     'L', new ItemStack(Blocks.redstone_lamp, 1),
   	   	     'G', new ItemStack(Blocks.glass, 1)
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 8), new Object[]{
    	   	     "GGG",
    	   	     "GG ",
    	   	     "G  ",
    	   	     'G', new ItemStack(Blocks.glass, 1)
    	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 8), new Object[]{
   	   	     "GGG",
   	   	     " GG",
   	   	     "  G",
   	   	     'G', new ItemStack(Blocks.glass, 1)
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 8), new Object[]{
      	   	     "  G",
      	   	     " GG",
      	   	     "GGG",
      	   	     'G', new ItemStack(Blocks.glass, 1)
      	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 8), new Object[]{
      	   	     "G  ",
      	   	     "GG ",
       	   	     "GGG",
   	   	     'G', new ItemStack(Blocks.glass, 1)
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(skyLight, 1), new Object[]{
     	   	     "EEE",
     	   	     "GRG",
      	   	     "LLL",
  	   	     'G', new ItemStack(Blocks.glass, 1),
 	   	     'L', new ItemStack(Blocks.glowstone, 1),
 	   	     'E', new ItemStack(Items.ender_eye, 1),
 	   	     'R', new ItemStack(Blocks.redstone_block, 1),
  	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumHelmet, 1), new Object[]{
    	   	     "YYY",
    	   	     "Y Y",
 	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
 	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumChestplate, 1), new Object[]{
   	   	         "Y Y",
   	   	         "YYY",
    	   	     "YYY",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumLeggings, 1), new Object[]{
  	   	         "YYY",
  	   	         "Y Y",
   	   	         "Y Y",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumBoots, 1), new Object[]{
 	   	         "Y Y",
  	   	         "Y Y",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumPickaxe, 1), new Object[]{
	   	         "YYY",
 	   	         " S ",
 	   	         " S ",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumAxe, 1), new Object[]{
	   	         "YY",
	   	         "YS",
	   	         " S",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumAxe, 1), new Object[]{
	   	         "YY",
	   	         "SY",
	   	         "S ",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumHoe, 1), new Object[]{
	   	         "YY",
	   	         " S",
	   	         " S",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumHoe, 1), new Object[]{
	   	         "YY",
	   	         "S ",
	   	         "S ",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumSword, 1), new Object[]{
	   	         "Y",
	   	         "Y",
	   	         "S",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumSpade, 1), new Object[]{
	   	         "Y",
	   	         "S",
	   	         "S",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     'S', new ItemStack(Items.stick, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumBlock, 1), new Object[]{
	   	         "YYY",
	   	         "YYY",
	   	         "YYY",
	   	     'Y', new ItemStack(yttriumIngot, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(yttriumIngot, 9), new Object[]{
	   	         "Y",
	   	     'Y', new ItemStack(yttriumBlock, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(miningHelmet, 1), new Object[]{
     	   	     " F ",
     	   	     "III",
     	   	     "I I",
     	   	     'F', new ItemStack(flashlight, 1),
     	   	     'I', new ItemStack(Items.iron_ingot, 1),
     	   	     });
    	GameRegistry.addRecipe(new ItemStack(glisterstone, 4), new Object[]{
	   	         "GMG",
	   	         "MLM",
	   	         "GMG",
	   	     'G', new ItemStack(Blocks.glass, 1),
 	   	     'M', new ItemStack(photonicResource, 1, 12),
 	   	     'L', new ItemStack(Blocks.glowstone, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserMerger, 1), new Object[]{
	   	         "SSS",
	   	         "MLM",
	   	         "SSS",
	   	     'S', new ItemStack(Blocks.stone, 1),
	   	     'M', new ItemStack(photonicResource, 1, 12),
	   	     'L', new ItemStack(laserItem, 1, 0),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(flashlight, 1), new Object[]{
	   	         "SSS",
	   	         "ELG",
	   	         "SSS",
	   	     'S', new ItemStack(Items.iron_ingot, 1),
	   	     'E', new ItemStack(photonicResource, 1, 0),
	   	     'R', new ItemStack(Blocks.redstone_block, 1),
	   	     'L', new ItemStack(Blocks.redstone_lamp, 1),
	   	     'G', new ItemStack(Blocks.glass, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(rangefinder, 1), new Object[]{
	   	         "SSS",
	   	         "ELG",
	   	         "SSS",
	   	     'S', new ItemStack(Items.iron_ingot, 1),
	   	     'E', new ItemStack(photonicResource, 1, 0),
	   	     'R', new ItemStack(Blocks.redstone_block, 1),
	   	     'L', new ItemStack(photonicResource, 1, 15),
	   	     'G', new ItemStack(Blocks.glass, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserpointer, 1), new Object[]{
	   	         "SSS",
	   	         "ELG",
	   	         "SSS",
	   	     'S', new ItemStack(Items.iron_ingot, 1),
	   	     'E', new ItemStack(photonicResource, 1, 0),
	   	     'R', new ItemStack(Blocks.redstone_block, 1),
	   	     'L', new ItemStack(laserItem, 1, 0),
	   	     'G', new ItemStack(Blocks.glass, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(oreRadar, 1), new Object[]{
	   	         "SSS",
	   	         "ELG",
	   	         "SSS",
	   	     'S', new ItemStack(Items.iron_ingot, 1),
	   	     'E', new ItemStack(photonicResource, 1, 0),
	   	     'R', new ItemStack(Blocks.redstone_block, 1),
	   	     'L', new ItemStack(photonicResource2, 1, 0),
	   	     'G', new ItemStack(Blocks.glass, 1),
	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserGenerator, 1), new Object[]{
   	   	     "III",
   	   	     "TBT",
   	   	     "RDR",
 	   	     'I', new ItemStack(Items.iron_ingot, 1),
 	   	     'B', new ItemStack(Blocks.iron_block, 1),
   	   	     'R', new ItemStack(Items.redstone, 1),
   	   	     'T', new ItemStack(photonicResource2, 1, 1),
	   	     'D', new ItemStack(laserItem, 1, 5),
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserCharger, 1), new Object[]{
      	   	     "RER",
      	   	     "OBO",
      	   	     "RER",
    	   	     'O', new ItemStack(Blocks.obsidian, 1),
    	   	     'B', new ItemStack(laserGenerator, 1),
      	   	     'R', new ItemStack(Items.redstone, 1),
      	   	     'E', new ItemStack(photonicResource, 1, 0),
      	   	     });
    	GameRegistry.addRecipe(new ItemStack(lightDetector, 1), new Object[]{
      	   	     " R ",
      	   	     "QGQ",
      	   	     " R ",
    	   	     'Q', new ItemStack(Items.quartz, 1),
    	   	     'G', new ItemStack(Blocks.glass, 1),
      	   	     'R', new ItemStack(Items.redstone, 1),
      	   	     });
    	GameRegistry.addRecipe(new ItemStack(safetyglasses, 1), new Object[]{
     	   	     " O ",
     	   	     "BGB",
   	   	         'O', new ItemStack(Items.dye, 1, 14),
   	   	         'G', new ItemStack(Blocks.stained_glass_pane, 1, 15),
     	   	     'B', new ItemStack(Blocks.stained_glass, 1, 15),
     	   	     });
    	GameRegistry.addRecipe(new ItemStack(irglasses, 1), new Object[]{
    	   	     " O ",
    	   	     "BGB",
  	   	         'O', new ItemStack(photonicResource, 1, 15),
  	   	         'G', new ItemStack(Blocks.stained_glass_pane, 1, 14),
    	   	     'B', new ItemStack(Blocks.stained_glass, 1, 14),
    	   	     });
    	GameRegistry.addRecipe(new ItemStack(photonicResource2, 1, 0), new Object[]{
   	   	     "RGR",
   	   	     "TBT",
   	   	     "IBI",
   	   	     'G', new ItemStack(Items.gold_ingot, 1),
   	   	     'T', new ItemStack(photonicResource2, 1, 1),
   	   	     'R', new ItemStack(Items.redstone, 1),
   	   	     'B', new ItemStack(photonicResource, 1, 1),
   	   	     'I', new ItemStack(Items.iron_ingot, 1),
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(tantalumBlock), new Object[]{
       	     "AAA",
       	     "AAA",
       	     "AAA",
       	     'A', new ItemStack(photonicResource2, 1, 1)
       	     });
       	GameRegistry.addRecipe(new ItemStack(photonicResource2, 9, 1), new Object[]{
   	   	     "A",
   	   	     'A', new ItemStack(tantalumBlock, 1)
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 10), new Object[]{
          	     "III",
          	     "IMG",
          	     "IFI",
          	   'I', new ItemStack(Items.iron_ingot, 1),
          	   'G', new ItemStack(Blocks.glass, 1),
          	   'F', new ItemStack(Blocks.furnace, 1),
          	   'M', new ItemStack(photonicResource2, 1, 0),
          	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 11), new Object[]{
     	   	     "III",
     	   	     " MC",
     	   	     "III",
     	   	     'M', new ItemStack(photonicResource, 1, 12),
     	   	     'C', new ItemStack(Items.redstone, 1),
     	   	     'I', new ItemStack(Items.iron_ingot, 1),
     	   	     });
    	GameRegistry.addRecipe(new ItemStack(laserItem, 1, 11), new Object[]{
    	   	     "III",
    	   	     "CM ",
    	   	     "III",
    	   	     'M', new ItemStack(photonicResource, 1, 12),
     	   	     'C', new ItemStack(Items.redstone, 1),
    	   	     'I', new ItemStack(Items.iron_ingot, 1),
    	   	     });
    	GameRegistry.addRecipe(new ItemStack(microwavePanel, 4), new Object[]{
   	   	     "A A",
   	   	     " E ",
   	   	     "A A",
   	   	     'A', new ItemStack(anorthosite, 1),
   	   	     'E', new ItemStack(photonicResource, 1, 0),
   	   	     });
    	GameRegistry.addRecipe(new ItemStack(microwaveGenerator, 1), new Object[]{
      	   	     "PLP",
      	   	     "RGR",
      	   	     "PMP",
      	   	     'P', new ItemStack(microwavePanel, 1),
      	   	     'R', new ItemStack(Items.redstone, 1),
      	   	     'G', new ItemStack(laserGenerator, 1),
      	   	     'L', new ItemStack(laserItem, 1, 0),
      	   	     'M', new ItemStack(photonicResource2, 1, 0),
      	   	     });
    	GameRegistry.addRecipe(new ItemStack(hahniumBlock), new Object[]{
          	     "AAA",
          	     "AAA",
          	     "AAA",
          	     'A', new ItemStack(photonicResource2, 1, 3)
          	     });
          	GameRegistry.addRecipe(new ItemStack(photonicResource2, 9, 3), new Object[]{
      	   	     "A",
      	   	     'A', new ItemStack(hahniumBlock, 1)
      	   	     });
          	GameRegistry.addRecipe(new ItemStack(photonicResource2, 4, 4), new Object[]{
         	   	     "GIG",
         	   	     "GIG",
         	   	     "GIG",
         	   	     'G', new ItemStack(Blocks.glass_pane, 1),
         	   	     'I', new ItemStack(Blocks.iron_bars, 1)
         	   	     });
          	GameRegistry.addRecipe(new ItemStack(radioReceiver, 1), new Object[]{
        	   	     "SAS",
        	   	     "RRR",
        	   	     "SAS",
        	   	     'A', new ItemStack(photonicResource2, 1, 4),
        	   	     'S', new ItemStack(Blocks.stone, 1),
        	   	     'R', new ItemStack(Items.redstone, 1)
        	   	     });
          	GameRegistry.addRecipe(new ItemStack(radioTransmitter, 1), new Object[]{
       	   	     "SRS",
       	   	     "ARA",
       	   	     "SRS",
       	   	     'A', new ItemStack(photonicResource2, 1, 4),
       	   	     'S', new ItemStack(Blocks.stone, 1),
       	   	     'R', new ItemStack(Items.redstone, 1)
       	   	     });
          	GameRegistry.addShapelessRecipe(new ItemStack(radioTransceiver, 1), new Object[]{
          	   	     new ItemStack(radioTransmitter, 1),
          	   	     new ItemStack(radioReceiver, 1),
          	   	     });
        	GameRegistry.addRecipe(new ItemStack(pocketRadio, 1), new Object[]{
   	   	         "SAS",
   	   	         "ERG",
   	   	         "SSS",
   	   	     'S', new ItemStack(Items.iron_ingot, 1),
   	   	     'A', new ItemStack(photonicResource2, 1, 4),
   	   	     'E', new ItemStack(photonicResource, 1, 0),
   	   	     'R', new ItemStack(radioReceiver, 1),
   	   	     'G', new ItemStack(Blocks.glass, 1),
   	   	     });
          	GameRegistry.addRecipe(new ItemStack(photonicResource2, 1, 5), new Object[]{
        	   	     " II",
        	   	     "IED",
        	   	     " II",
        	   	     'D', new ItemStack(Items.diamond, 1),
        	   	     'E', new ItemStack(photonicResource, 1, 2),
        	   	     'I', new ItemStack(Items.iron_ingot, 1)
        	   	     });
          	GameRegistry.addRecipe(new ItemStack(terahertzReceiver, 1), new Object[]{
       	   	     "III",
       	   	     "RGR",
       	   	     "III",
       	   	     'R', new ItemStack(Items.redstone, 1),
       	   	     'G', new ItemStack(photonicResource2, 1, 5),
       	   	     'I', new ItemStack(Items.iron_ingot, 1)
       	   	     });
        	GameRegistry.addRecipe(new ItemStack(terahertzTransmitter, 1), new Object[]{
          	   	     "III",
          	   	     "TBT",
          	   	     "RGR",
        	   	     'I', new ItemStack(Items.iron_ingot, 1),
        	   	     'B', new ItemStack(Blocks.iron_block, 1),
          	   	     'R', new ItemStack(Items.redstone, 1),
          	   	     'T', new ItemStack(photonicResource2, 1, 1),
          	   	     'G', new ItemStack(photonicResource2, 1, 5),
          	   	     });
        	GameRegistry.addRecipe(new ItemStack(bariumBlock), new Object[]{
             	     "AAA",
             	     "AAA",
             	     "AAA",
             	     'A', new ItemStack(photonicResource2, 1, 6)
             	     });
             	GameRegistry.addRecipe(new ItemStack(photonicResource2, 9, 6), new Object[]{
         	   	     "A",
         	   	     'A', new ItemStack(bariumBlock, 1)
         	   	     });

    	proxy.preinit();
    }
    public static boolean computercraft = false;
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	GameRegistry.registerWorldGenerator(new PhotonicGenerator(), 12);
    	computercraft = Loader.isModLoaded("ComputerCraft");
    	if (computercraft)
    		ComputerCraft.registerPeripheralProvider(new PhotonicPeripheralProvider());
    	proxy.init();
    	
    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        NetworkRegistry.INSTANCE.newChannel("PhotonicCraft", new PhotonicPacketHandler());
    	event.registerServerCommand(new RchCommand());
        proxy.serverLoad();
    }
    
    static Block erbiumOre;
    static Block erbiumBlock;
    static Block yttriumOre;
    static Block yttriumBlock;
    static Block mercuryOre;
    public static Block laser;
    static Block laserMirror;
    static Block laserMirror2;
    static Block laserSemiMirror;
    static Block laserSemiMirror2;
    public static Item photonicResource;
    public static Item photonicCoupler;
    public static Item blockMirror;
    public static Item laserItem;
    static Block laserDetector;
    static Block laserDetector2;
    static Block skyLight;
    static Block prism;
    static Block floodlight;
    public static Item rangefinder;
    public static Block lightAir;
    public static Item yttriumIngot;
    public static Item flashlight;
    public static Block fluorescent;
    public static Block glisterstone;
    public static Block laserMerger;
    public static Block laserGenerator;
    public static Item laserpointer;
    static Block lightDetector;
    public static Block laserCharger;
    public static Item nougat;
    static Block analoglamp;
    public static Block remoteReceiver;
    public static Block microwave;
    static Block laserRedstoneMirror;
    public static Item photonicResource2;
    static Block tantalumOre;
    static Block tantalumBlock;
    static Block anorthosite;
    public static Block microwavePanel;
    public static Block microwaveGenerator;
    static Block hahniumOre;
    static Block hahniumBlock;
    public static Block radioReceiver;
    public static Block radioTransmitter;
    public static Block radioTransceiver;
    public static Item pocketRadio;
    public static Item terahertzReceiver;
    public static Block terahertzTransmitter;
    public static Block blacklight;
    static Block bariumOre;
    static Block bariumBlock;
    public static Item beefJerky;
    
    public static Item yttriumHelmet;
    public static Item yttriumChestplate;
    public static Item yttriumLeggings;
    public static Item yttriumBoots;
    public static Item yttriumPickaxe;
    public static Item yttriumAxe;
    public static Item yttriumSpade;
    public static Item yttriumHoe;
    public static Item yttriumSword;
    public static Item miningHelmet;
    public static Item irglasses;
    public static Item safetyglasses;
    public static Item remote;
    public static Item oreRadar;
    public static Item hypnoBomb;
    
	public static Object instance;
	public static long lastReceive = 0L;
	public static boolean isInside(int a, int i, int j) {
		return a >= i && a <= j;
	}
	
	boolean colossalCreepers = true;
}
