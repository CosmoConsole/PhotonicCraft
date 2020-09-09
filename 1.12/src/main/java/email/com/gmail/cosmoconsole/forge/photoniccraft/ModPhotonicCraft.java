package email.com.gmail.cosmoconsole.forge.photoniccraft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicGammaEffectPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicInfraredPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicLaserPointerPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicRadioPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicCommonProxy;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicGuiHandler;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicWorldGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.advancements.DeathrayFiredTrigger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.advancements.TerahertzTrigger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.event.PhotonicEventHandler;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.event.PhotonicRecipes;
import email.com.gmail.cosmoconsole.forge.photoniccraft.contrib.MODSoundCategory;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.computercraft.PhotonicPeripheralProvider;
import email.com.gmail.cosmoconsole.forge.photoniccraft.server.RchCommand;
import email.com.gmail.cosmoconsole.forge.photoniccraft.server.network.PhotonicRadioResponsePacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.server.network.PhotonicRemotePacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.server.network.PhotonicServerLaserPointerPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EnergyContainer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.NameAndItem;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The main mod class where some of the registrations happen.
 */
@Mod(modid = ModPhotonicCraft.MODID, version = ModPhotonicCraft.VERSION)
public class ModPhotonicCraft {
	public static final String MODID = "photoniccraft";
	public static final String VERSION = "0.9.8";

	public static final boolean DEBUG_VERSION = true;

	@SidedProxy(clientSide = "email.com.gmail.cosmoconsole.forge.photoniccraft.client.PhotonicClientProxy", serverSide = "email.com.gmail.cosmoconsole.forge.photoniccraft.server.PhotonicServerProxy")
	static PhotonicCommonProxy proxy;

	public static ToolMaterial YTTRIUM_TOOL = EnumHelper.addToolMaterial("YTTRIUM", 5, 11937, 18.0F, 9.0F, 23);
	public static ArmorMaterial DECO_ARMOR = EnumHelper.addArmorMaterial("DECORATIVE", "DecorativeArmor", 100000,
			new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0f);
	public static ArmorMaterial YTTRIUM_ARMOR = EnumHelper.addArmorMaterial("YTTRIUM", "YttriumArmor", 67,
			new int[] { 6, 10, 9, 5 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 4.0f);

	public static SimpleNetworkWrapper network;

	public static AudioFormat radioFormat = null;
	public static DataLine.Info dataLineInfo = null;
	public static SourceDataLine sourceDataLine = null;
	public static int maximumRadio = 0;
	public static int maximumRadioChannelBroadcast = 0;

	public static List<NameAndItem> photonicResources;
	public static List<NameAndItem> photonicResources2;
	public static List<NameAndItem> laserItems;

	public static SoundCategory SC_RADIO;

	public static SoundEvent microwave_ambient;
	public static SoundEvent microwave_beep;
	public static SoundEvent microwave_start;
	public static SoundEvent deathray_fire;
	public static TerahertzTrigger trg_TERAHERTZ_RECEIVED;
	public static DeathrayFiredTrigger trg_DEATHRAY_FIRE;

	public static Object instance;

	public static long lastReceive = 0L;
	public static boolean colossalCreepers = false;
	public static boolean canCraftVanta = false;
	
	public static ItemStack photonicCraftItemStack = new ItemStack(Blocks.STONE, 1);
	public static final CreativeTabs photoniccraftTab = new CreativeTabs("photoniccraft") {
		@Override
		public ItemStack getTabIconItem() {
			return photonicCraftItemStack;
		}
	};

	/// TODO
	/* 
	 * - migrate to 1.13, convert photonic couplers to 1.13 format when that arrives?
	 */

	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerWorldGenerator(new PhotonicWorldGenerator(), 16);
		PhotonicRecipes.addRecipes();
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent postEvent) {
		if (Compat.computercraft = Loader.isModLoaded(Compat.MODID_COMPUTERCRAFT)) {
			init_ComputerCraft();
		}
		if (Compat.opencomputers = Loader.isModLoaded(Compat.MODID_OPENCOMPUTERS)) {
			init_OpenComputers();
		}
		Compat.openblocks = Loader.isModLoaded(Compat.MODID_OPENBLOCKS);
		Compat.mirage = Loader.isModLoaded(Compat.MODID_MIRAGE);
		Compat.waila = Loader.isModLoaded(Compat.MODID_WAILA);
		proxy.postinit();
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	private void init_ComputerCraft() {
		dan200.computercraft.ComputerCraft.registerPeripheralProvider(new PhotonicPeripheralProvider());
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	private void init_OpenComputers() {
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// singleton instantiation
		instance = this;

		// create NameAndItem lists
		photonicResources = new ArrayList<NameAndItem>();
		photonicResources2 = new ArrayList<NameAndItem>();
		laserItems = new ArrayList<NameAndItem>();

		// create locks
		PhotonicRadio.sendLock = new ReentrantLock();
		PhotonicRadio.playerAccessLock = new ReentrantLock();

		// configuration
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "PhotonicCraft.cfg"));
		config.load();
		colossalCreepers = config.get("mob", "spawnColossalCreepers", false).getBoolean();
		canCraftVanta = config.get("block", "canCraftVantaBlock", false).getBoolean();
		maximumRadio = config.get("item", "maximumRadioListeners", 0).getInt();
		maximumRadioChannelBroadcast = config.get("item", "maximumRadioChannelsPerComputer", 4).getInt();
		config.save();

		// register network manager and IMessage's
		network = NetworkRegistry.INSTANCE.newSimpleChannel("PhotonicCraft_msgs");
		ModPhotonicCraft.network.registerMessage(PhotonicRadioPacket.Handler.class, PhotonicRadioPacket.class, 40,
				Side.CLIENT);
		ModPhotonicCraft.network.registerMessage(PhotonicLaserPointerPacket.Handler.class,
				PhotonicLaserPointerPacket.class, 41, Side.CLIENT);
		ModPhotonicCraft.network.registerMessage(PhotonicInfraredPacket.Handler.class, PhotonicInfraredPacket.class, 42,
				Side.CLIENT);
		ModPhotonicCraft.network.registerMessage(PhotonicGammaEffectPacket.Handler.class, PhotonicGammaEffectPacket.class, 45,
				Side.CLIENT);
		ModPhotonicCraft.network.registerMessage(PhotonicRemotePacket.Handler.class, PhotonicRemotePacket.class, 50,
				Side.SERVER);
		ModPhotonicCraft.network.registerMessage(PhotonicRadioResponsePacket.Handler.class,
				PhotonicRadioResponsePacket.class, 51, Side.SERVER);
		ModPhotonicCraft.network.registerMessage(PhotonicServerLaserPointerPacket.Handler.class,
				PhotonicServerLaserPointerPacket.class, 52, Side.SERVER);

		// register event handler
		MinecraftForge.EVENT_BUS.register(new PhotonicEventHandler());

		// add the Pocket Radio sound category
		SC_RADIO = MODSoundCategory.add("photoniccraft_radio");

		// register EnergyContainer
		CapabilityManager.INSTANCE.register(EnergyContainer.class, new IStorage<EnergyContainer>() {
			@Override
			public void readNBT(Capability<EnergyContainer> capability, EnergyContainer instance, EnumFacing side,
					NBTBase nbt) {
				if (!(instance instanceof EnergyContainer))
					throw new IllegalArgumentException("attempt to deserialize with EnergyContainer to something else");
				instance.setEnergyStored(((NBTTagInt) nbt).getInt());
			}

			@Override
			public NBTBase writeNBT(Capability<EnergyContainer> capability, EnergyContainer instance, EnumFacing side) {
				return new NBTTagInt(instance.getEnergyStored());
			}
		}, () -> new EnergyContainer(1000));

		// register GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new PhotonicGuiHandler());

		// forward preinit
		PhotonicEventHandler.miscPreInit();
		proxy.preinit();

		// add trigger
		trg_TERAHERTZ_RECEIVED = (TerahertzTrigger) PhotonicUtils.registerAdvancementTrigger(new TerahertzTrigger());
		trg_DEATHRAY_FIRE = (DeathrayFiredTrigger) PhotonicUtils.registerAdvancementTrigger(new DeathrayFiredTrigger());

		// better hope debug isn't on
		PhotonicUtils.debugMessage(
				"\"WARNING: Do not look directly into laser with remaining eye.\" This is a debug message. If this is a release build, PLEASE REPORT THIS TO THE MOD CREATOR IMMEDIATELY.");
	}

	@EventHandler
	public void serverClose(FMLServerStoppingEvent event) {
		proxy.serverClose();
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new RchCommand());
		proxy.serverLoad();
	}

	// "MOD ID" of the vanilla game, used for item IDs
	public static final String MODID_MINECRAFT = "minecraft";
}
