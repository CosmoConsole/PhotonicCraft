package email.com.gmail.cosmoconsole.forge.photoniccraft.common.event;

import static email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft.colossalCreepers;

import java.util.HashMap;
import java.util.UUID;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicRadioPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityColossalCreeper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemPocketRadio;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.registrar.PhotonicEntityRegistrar;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.registrar.PhotonicMiscRegistrar;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.registrar.PhotonicModelRegistrar;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.registrar.PhotonicSoundRegistrar;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.IEquipListenable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The main event handler for PhotonicCraft.
 */
public class PhotonicEventHandler {

	private HashMap<UUID, Boolean> isPlayerSpectator = new HashMap<>();

	public static void miscPreInit() {
		PhotonicMiscRegistrar.registerTileEntities();

		if (colossalCreepers) {
			for (Biome b : Biome.REGISTRY) {
				if (b != null) {
					EntityRegistry.addSpawn(EntityColossalCreeper.class, 5, 1, 1, EnumCreatureType.MONSTER, b);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void livingSpawn(CheckSpawn event) {
		if (event.getEntity() instanceof EntityColossalCreeper) {
			if (event.getWorld()
					.getTopSolidOrLiquidBlock(new BlockPos((int) (event.getX() - .5), 255, (int) (event.getZ() - .5)))
					.getY() - Math.floor(event.getY()) > 1)
				event.setResult(Result.DENY);
			else {
				for (EntityColossalCreeper e : event.getWorld().getEntitiesWithinAABB(EntityColossalCreeper.class,
						new AxisAlignedBB(event.getX() - 128f, event.getY() - 128f, event.getZ() - 128f,
								event.getX() + 128f, event.getY() + 128f, event.getZ() + 128f))) {
					event.setResult(Result.DENY);
					return;
				}
				int c = 0;
				int kx = (int) (event.getX() - .5);
				int ky = (int) event.getY();
				int kz = (int) (event.getZ() - .5);
				for (int x = -10; x <= 10; x++)
					for (int y = -10; y <= 10; y++)
						for (int z = -10; z <= 10; z++)
							if (event.getWorld().getBlockState(new BlockPos(kx + x, ky + y, kz + z))
									.getBlock() == Blocks.TNT)
								c++;
				if (Math.random() > (0.001D * c))
					event.setResult(Result.DENY);
			}
			if (event.getResult() != Result.DENY || ((EntityColossalCreeper) event.getEntity()).forceSummon)
				event.setResult(Result.ALLOW);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void playerJoinTurnOffRadio(PlayerLoggedInEvent event) {
		for (ItemStack i : PhotonicUtils.multipleLists(event.player.inventory.mainInventory,
				event.player.inventory.offHandInventory, event.player.inventory.armorInventory)) {
			if (!i.isEmpty() && i.getItem() == PhotonicItems.pocketRadio && i.hasTagCompound()) {
				ItemPocketRadio.turnOffRadio(i);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void playerQuit(WorldEvent.Unload event) {
		if (event.getWorld().isRemote) {
			PhotonicRadio.shouldNotBePlaying();
			PhotonicRadio.stopPlayingRadio();
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void playerTick(PlayerTickEvent event) {
		if (event.side == Side.CLIENT) {
			boolean should = PhotonicRadio.shouldBePlaying();
			if (should != PhotonicRadio.radioPlaying) {
				if (PhotonicRadio.radioPlaying)
					PhotonicRadio.stopPlayingRadio();
				else
					PhotonicRadio.startPlayingRadio();
			}
		} else if (event.side == Side.SERVER) {
			boolean specNow = event.player.isSpectator();
			UUID u = event.player.getPersistentID();
			if (isPlayerSpectator.containsKey(u)) {
				boolean specOld = isPlayerSpectator.get(u);
				if (specOld != specNow) {
					for (ItemStack i : event.player.inventory.armorInventory) {
						if (i.getItem() instanceof IEquipListenable) {
							if (specNow) {
								((IEquipListenable) i.getItem()).onEnterSpectator(event.player.world, event.player, i);
							} else {
								((IEquipListenable) i.getItem()).onExitSpectator(event.player.world, event.player, i);
							}
						}
					}
				}
			}
			isPlayerSpectator.put(u, specNow);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void radioPickup(EntityItemPickupEvent e) {
		if (e.getItem() != null && e.getItem().getItem() != null
				&& e.getItem().getItem().getItem() == PhotonicItems.pocketRadio
				&& e.getItem().getItem().hasTagCompound()) {
			e.getItem().getItem().getTagCompound().setBoolean("powered", false);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void radioToss(ItemTossEvent e) {
		if (e.getEntityItem() != null && e.getEntityItem() != null
				&& e.getEntityItem().getItem().getItem() == PhotonicItems.pocketRadio
				&& e.getEntityItem().getItem().hasTagCompound() && PhotonicUtils.hasNoPoweredOnRadio(e.getPlayer())) {
			if (e.getEntityItem().getItem().getTagCompound().getBoolean("powered")) {
				PhotonicUtils.sendPacketToPlayer(ModPhotonicCraft.network, e.getPlayer(), new PhotonicRadioPacket(-1.0,
						new byte[PhotonicRadioPacket.PACKET_SIZE], 0, PhotonicRadioPacket.PACKET_SIZE));
			}
			e.getEntityItem().getItem().getTagCompound().setBoolean("powered", false);
		}
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		PhotonicBlocks.registerBlocks(event.getRegistry());
	}

	@SubscribeEvent
	public void registerEntities(RegistryEvent.Register<EntityEntry> e) {
		PhotonicEntityRegistrar.registerEntities(e.getRegistry());
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		PhotonicItems.registerItems(event.getRegistry());
		PhotonicMiscRegistrar.registerOres();
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		PhotonicModelRegistrar.registerModels();
	}
	
	@SubscribeEvent
	public void migrateBlocks(RegistryEvent.MissingMappings<Block> event) {
		PhotonicMigrate.migrateBlocks(event);
	}
	
	@SubscribeEvent
	public void migrateItems(RegistryEvent.MissingMappings<Item> event) {
		PhotonicMigrate.migrateItems(event);
	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		PhotonicSoundRegistrar.registerSounds(event.getRegistry());
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void somethingCrafted(ItemCraftedEvent event) {
		if (event.crafting.getItem() == PhotonicItems.miningHelmet) {
			event.crafting.setTagCompound(new NBTTagCompound());
			event.crafting.getTagCompound().setInteger("energy",
					event.craftMatrix.getStackInSlot(1).getTagCompound().getInteger("energy"));
		}
	}

	@SubscribeEvent
	public void entityEquip(LivingEquipmentChangeEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			if (!(event.getFrom().getItem() instanceof ItemArmor) || (event.getSlot() != EntityEquipmentSlot.MAINHAND)
					&& (event.getSlot() != EntityEquipmentSlot.OFFHAND)) {
				if (event.getFrom().getItem() instanceof IEquipListenable) {
					((IEquipListenable) event.getFrom().getItem()).onUnequip(event.getEntityLiving().world,
							(EntityPlayer) event.getEntityLiving(), event.getFrom());
				}
			}
			if (!(event.getTo().getItem() instanceof ItemArmor) || (event.getSlot() != EntityEquipmentSlot.MAINHAND)
					&& (event.getSlot() != EntityEquipmentSlot.OFFHAND)) {
				if (event.getTo().getItem() instanceof IEquipListenable) {
					((IEquipListenable) event.getTo().getItem()).onEquip(event.getEntityLiving().world,
							(EntityPlayer) event.getEntityLiving(), event.getTo());
				}
			}
		}
	}

	@SubscribeEvent
	public void dimensionChange(PlayerEvent.PlayerChangedDimensionEvent e) {
		for (ItemStack i : e.player.inventory.armorInventory) {
			if (i.getItem() instanceof IEquipListenable) {
				((IEquipListenable) i.getItem()).onUnequip(e.player.world, e.player, i);
				((IEquipListenable) i.getItem()).onEquip(e.player.world, e.player, i);
			}
		}
	}

	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void gamemodeSwitchTrackerCleanup(PlayerEvent.PlayerLoggedOutEvent e) {
		UUID u = e.player.getPersistentID();
		if (isPlayerSpectator.containsKey(u)) {
			isPlayerSpectator.remove(u);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void infraredFogColor(EntityViewRenderEvent.FogColors e) {
		if (e.getEntity() instanceof EntityPlayer) {
			EntityPlayer ep = (EntityPlayer) e.getEntity();
			if (ep.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == PhotonicItems.irglasses) {
				e.setRed(0.3f);
				e.setGreen(0.3f);
				e.setBlue(0.3f);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent e) {
		PhotonicModelRegistrar.bakePrismModels(e.getModelRegistry());
	}

	
}
