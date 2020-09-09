package email.com.gmail.cosmoconsole.forge.photoniccraft.common.event;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings;

/**
 * This class handles ID migrations within PhotonicCraft.
 */
public class PhotonicMigrate {

	public static void migrateBlocks(MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> missing: event.getMappings()) {
            if (missing.key.getResourceDomain().equals(ModPhotonicCraft.MODID)) {
            	if (missing.key.getResourcePath().equals("bariumore")) {
            		missing.remap(PhotonicBlocks.rheniumOre);
            	} else if (missing.key.getResourcePath().equals("bariumblock")) {
            		missing.remap(PhotonicBlocks.rheniumBlock);
            	}
            }
        }
	}

	public static void migrateItems(MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> missing: event.getMappings()) {
            if (missing.key.getResourceDomain().equals(ModPhotonicCraft.MODID)) {
            	if (missing.key.getResourcePath().equals("bariumore")) {
            		missing.remap(Item.getItemFromBlock(PhotonicBlocks.rheniumOre));
            	} else if (missing.key.getResourcePath().equals("bariumblock")) {
            		missing.remap(Item.getItemFromBlock(PhotonicBlocks.rheniumBlock));
            	} else if (missing.key.getResourcePath().equals("bariumingot")) {
            		missing.remap(PhotonicItems.photonicResources2[6]);
            	}
            }
        }
	}
	
	private PhotonicMigrate() {}
	
}
