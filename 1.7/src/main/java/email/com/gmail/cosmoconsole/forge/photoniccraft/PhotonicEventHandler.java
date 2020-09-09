package email.com.gmail.cosmoconsole.forge.photoniccraft;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityColossalCreeper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.event.world.WorldEvent;

public class PhotonicEventHandler {
	
    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void livingSpawn(CheckSpawn event) {
        if (event.entity instanceof EntityColossalCreeper) {
        	if (event.world.getTopSolidOrLiquidBlock((int) (event.x-.5), (int) (event.z-.5)) - Math.floor(event.y) > 1)
            	event.setResult(Result.DENY);
            else {
            	int c = 0;
            	int kx = (int) (event.x-.5);
            	int ky = (int) event.y;
            	int kz = (int) (event.z-.5);
            	for (int x = -10; x <= 10; x++)
            		for (int y = -10; y <= 10; y++)
            			for (int z = -10; z <= 10; z++)
            				if (event.world.getBlock(kx+x,ky+y,kz+z) == Blocks.tnt)
            					c++;
            	if (Math.random() > (0.001D * c))
            		event.setResult(Result.DENY);
            }
            if (event.getResult() != Result.DENY || ((EntityColossalCreeper)event.entity).forceSummon) event.setResult(Result.ALLOW);
        }
    }  
    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void SomethingCrafted(ItemCraftedEvent event)
    {
    	if (event.crafting.getItem() == ModPhotonicCraft.miningHelmet) {
    		event.crafting.setTagCompound(new NBTTagCompound());
    		event.crafting.getTagCompound().setInteger("energy", event.craftMatrix.getStackInSlot(1).getTagCompound().getInteger("energy"));
    	}
    }
    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void playerTick(PlayerTickEvent event)
    {
    	if (event.side == Side.CLIENT) {
    		boolean should = PhotonicAPI.shouldBePlaying();
    		if (should != PhotonicAPI.radioPlaying) {
    			if (PhotonicAPI.radioPlaying)
    				PhotonicAPI.stopPlayingRadio();
    			else
    				PhotonicAPI.startPlayingRadio();
    		}
    	}
    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void playerQuit(WorldEvent.Unload event)
    {
    	if (event.world.isRemote) {
    		PhotonicAPI.shouldNotBePlaying();
			PhotonicAPI.stopPlayingRadio();
    	}
    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void radioPickup(ItemPickupEvent e)
    {
    	if (e.pickedUp != null && e.pickedUp.getEntityItem() != null && e.pickedUp.getEntityItem().getItem() == ModPhotonicCraft.pocketRadio && e.pickedUp.getEntityItem().stackTagCompound != null) {
    		e.pickedUp.getEntityItem().stackTagCompound.setBoolean("powered", false);
    		System.out.println(e.pickedUp.getEntityItem().stackTagCompound.getBoolean("powered"));
    	}
    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void radioToss(ItemTossEvent e)
    {
    	if (e.entityItem != null && e.entityItem.getEntityItem() != null && e.entityItem.getEntityItem().getItem() == ModPhotonicCraft.pocketRadio && e.entityItem.getEntityItem().stackTagCompound != null) {
    		if (e.entityItem.getEntityItem().stackTagCompound.getBoolean("powered")) {
    			ModPhotonicCraft.network.sendTo(new PhotonicRadioPacket(-1.0, new byte[2205]), (EntityPlayerMP)e.player);
    		}
    		e.entityItem.getEntityItem().stackTagCompound.setBoolean("powered", false);
    	}
    }
}
