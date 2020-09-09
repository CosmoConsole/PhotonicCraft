package email.com.gmail.cosmoconsole.forge.photoniccraft.common;

import java.util.ArrayList;
import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemBeefJerky;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemDecorativeArmor;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemFlashlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemHypnoBomb;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemIRGlasses;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemLaserPointer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemMiningHelmet;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemNougat;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemOreRadar;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemPhotonic;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemPhotonic2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemPhotonicCoupler;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemPocketRadio;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemPowerBank;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemRangefinder;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemRemote;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemTerahertzReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemYttriumArmor;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemYttriumAxe;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemYttriumHoe;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemYttriumIngot;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemYttriumPickaxe;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemYttriumSpade;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemYttriumSword;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.registrar.PhotonicModelRegistrar;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.NameAndItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * The class where PhotonicCraft's Item classes are easily accessible. They are also registered here.
 */
public class PhotonicItems {
	public static Item photonicCoupler;
	public static Item blockMirror;
	public static Item rangefinder;
	public static Item yttriumIngot;
	public static Item flashlight;
	public static Item laserpointer;
	public static Item nougat;
	public static Item pocketRadio;
	public static Item terahertzReceiver;
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
	public static Item[] photonicResources;
	public static Item[] photonicResources2;
	public static Item[] laserItems;
	public static Item powerbank;
	
	private static boolean registerForModels = false;
	
	public static ArrayList<NameAndItem> blockItemList = new ArrayList<>();

	private static void goRegisterItem(IForgeRegistry<Item> registry, Item item, String name) {
		item.setRegistryName(name);
		registry.register(item);
		if (registerForModels) {
			addToModelList(new NameAndItem(name, item));
		}
	}

	@SideOnly(Side.CLIENT)
	private static void addToModelList(NameAndItem nameAndItem) {
		PhotonicModelRegistrar.modelItemList.add(nameAndItem);
	}

	private static void goRegisterItems(IForgeRegistry<Item> registry, List<NameAndItem> li) {
		for (NameAndItem i : li) {
			registry.register(i.getItem());
			if (registerForModels) {
				addToModelList(i);
			}
		}
	}
	
	public static void registerItems(IForgeRegistry<Item> registry) {
		goRegisterItems(registry, blockItemList);
		registerForModels = FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
		goRegisterItems(registry, ModPhotonicCraft.laserItems);
		goRegisterItems(registry, ModPhotonicCraft.photonicResources);
		goRegisterItems(registry, ModPhotonicCraft.photonicResources2);
		goRegisterItem(registry,
				PhotonicItems.miningHelmet = new ItemMiningHelmet("photoniccraft_mininghelmet",
						ModPhotonicCraft.DECO_ARMOR, "mining", EntityEquipmentSlot.HEAD),
				"mininghelmet");
		goRegisterItem(registry, PhotonicItems.irglasses = new ItemIRGlasses("photoniccraft_irglasses",
				ModPhotonicCraft.DECO_ARMOR, "irglasses", EntityEquipmentSlot.HEAD), "irglasses");
		goRegisterItem(registry,
				PhotonicItems.safetyglasses = new ItemDecorativeArmor("photoniccraft_safetyglasses",
						ModPhotonicCraft.DECO_ARMOR, "safetyglasses", EntityEquipmentSlot.HEAD)
								.setItemTextureName("safetyglasses"),
				"safetyglasses");
		goRegisterItem(registry,
				PhotonicItems.yttriumHelmet = new ItemYttriumArmor("photoniccraft_yttriumhelmet",
						ModPhotonicCraft.YTTRIUM_ARMOR, "yttrium", EntityEquipmentSlot.HEAD),
				"yttriumhelmet");
		goRegisterItem(registry,
				PhotonicItems.yttriumChestplate = new ItemYttriumArmor("photoniccraft_yttriumchestplate",
						ModPhotonicCraft.YTTRIUM_ARMOR, "yttrium", EntityEquipmentSlot.CHEST),
				"yttriumchestplate");
		goRegisterItem(registry,
				PhotonicItems.yttriumLeggings = new ItemYttriumArmor("photoniccraft_yttriumleggings",
						ModPhotonicCraft.YTTRIUM_ARMOR, "yttrium", EntityEquipmentSlot.LEGS),
				"yttriumleggings");
		goRegisterItem(registry,
				PhotonicItems.yttriumBoots = new ItemYttriumArmor("photoniccraft_yttriumboots",
						ModPhotonicCraft.YTTRIUM_ARMOR, "yttrium", EntityEquipmentSlot.FEET),
				"yttriumboots");
		goRegisterItem(registry,
				PhotonicItems.yttriumPickaxe = new ItemYttriumPickaxe("photoniccraft_yttriumpickaxe",
						ModPhotonicCraft.YTTRIUM_TOOL),
				"yttriumpickaxe");
		goRegisterItem(registry, PhotonicItems.yttriumAxe = new ItemYttriumAxe("photoniccraft_yttriumaxe",
				ModPhotonicCraft.YTTRIUM_TOOL), "yttriumaxe");
		goRegisterItem(registry,
				PhotonicItems.yttriumSpade = new ItemYttriumSpade("photoniccraft_yttriumspade",
						ModPhotonicCraft.YTTRIUM_TOOL),
				"yttriumspade");
		goRegisterItem(registry, PhotonicItems.yttriumHoe = new ItemYttriumHoe("photoniccraft_yttriumhoe",
				ModPhotonicCraft.YTTRIUM_TOOL), "yttriumhoe");
		goRegisterItem(registry,
				PhotonicItems.yttriumSword = new ItemYttriumSword("photoniccraft_yttriumsword",
						ModPhotonicCraft.YTTRIUM_TOOL),
				"yttriumsword");
		goRegisterItem(registry, PhotonicItems.beefJerky = new ItemBeefJerky(7, 10.0f, true), "beefjerky");
		goRegisterItem(registry, PhotonicItems.pocketRadio = new ItemPocketRadio(), "pocketradio");
		goRegisterItem(registry, PhotonicItems.terahertzReceiver = new ItemTerahertzReceiver(),
				"terahertzreceiver");
		goRegisterItem(registry, PhotonicItems.yttriumIngot = new ItemYttriumIngot(), "yttriumingot");
		goRegisterItem(registry, PhotonicItems.flashlight = new ItemFlashlight(), "flashlight");
		goRegisterItem(registry, PhotonicItems.rangefinder = new ItemRangefinder(), "rangefinder");
		goRegisterItem(registry, PhotonicItems.laserpointer = new ItemLaserPointer(), "laserpointer");
		goRegisterItem(registry, PhotonicItems.oreRadar = new ItemOreRadar(), "oreradar");
		goRegisterItem(registry, PhotonicItems.nougat = new ItemNougat(4, 0.6f, false), "nougat");
		goRegisterItem(registry, PhotonicItems.remote = new ItemRemote(), "remotecontrol");
		goRegisterItem(registry, PhotonicItems.hypnoBomb = new ItemHypnoBomb(), "hypnobomb");
		goRegisterItem(registry, PhotonicItems.powerbank = new ItemPowerBank(), "powerbank");
		registerForModels = false;
		goRegisterItem(registry, PhotonicItems.photonicCoupler = new ItemPhotonicCoupler(), "coupler");
		ModPhotonicCraft.photonicCraftItemStack = new ItemStack(laserItems[0], 1);
	}

	static void createPhotonicResources() {
		int destIndex = 0;
		String[] resourceNames = new String[] { "erbiumingot", "opticalfiber", "erbiumamplifier", "erbiumnugget",
				"gainmedium_low", "gainmedium_medium", "gainmedium_high", "gainmedium_extreme", "erbiumcrystalalpha",
				"erbiumcrystalbeta", "erbiumcrystalomega", "photonicrelic", "mirror", "mercury", "gainmedium_puny",
				"ircrystal" };
		photonicResources = new Item[resourceNames.length];
		for (String s : resourceNames) {
			ItemPhotonic ip = new ItemPhotonic(s, s.equals("erbiumcrystalomega"));
			ip.setRegistryName(s);
			PhotonicItems.photonicResources[destIndex++] = ip;
			ModPhotonicCraft.photonicResources.add(new NameAndItem(s, ip));
		}
		destIndex = 0;
		String[] resource2Names = new String[] { "magnetron", "tantalumingot", "tantalumcapacitor", "hahniumingot",
				"antenna", "gyrotron", "rheniumingot", "smallbattery", "mediumbattery", "largebattery", "xraytube" };
		photonicResources2 = new Item[resource2Names.length];
		for (String s : resource2Names) {
			ItemPhotonic2 ip = new ItemPhotonic2(s);
			ip.setRegistryName(s);
			PhotonicItems.photonicResources2[destIndex++] = ip;
			ModPhotonicCraft.photonicResources2.add(new NameAndItem(s, ip));
		}
	}

	private PhotonicItems() {
	}
}
