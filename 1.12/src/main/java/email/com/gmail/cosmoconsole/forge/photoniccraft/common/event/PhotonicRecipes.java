package email.com.gmail.cosmoconsole.forge.photoniccraft.common.event;

import static email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft.photonicResources;
import static email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft.photonicResources2;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.PhotonicAPI;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class adds the PhotonicCraft smelting and builtin ultraviolet blacklight recipes.
 */
public class PhotonicRecipes {

	static ResourceLocation group = new ResourceLocation("craftingGroup:photoniccraft");

	public static void addRecipes() {
		PhotonicAPI.addUltravioletRecipe(ModPhotonicCraft.MODID, new ItemStack(Items.ROTTEN_FLESH, 1),
				new ItemStack(PhotonicItems.beefJerky, 1));
		addFurnaceRecipes();
		registerConditionalCraftingRecipes();
	}
	
	private static void addFurnaceRecipes() {
		GameRegistry.addSmelting(new ItemStack(PhotonicBlocks.erbiumOre, 1),
				new ItemStack(photonicResources.get(0).getItem(), 1), 0.8F);
		GameRegistry.addSmelting(new ItemStack(PhotonicBlocks.tantalumOre, 1),
				new ItemStack(photonicResources2.get(1).getItem(), 1), 0.9F);
		GameRegistry.addSmelting(new ItemStack(PhotonicBlocks.hahniumOre, 1),
				new ItemStack(photonicResources2.get(3).getItem(), 1), 0.6F);
		GameRegistry.addSmelting(new ItemStack(PhotonicBlocks.rheniumOre, 1),
				new ItemStack(photonicResources2.get(6).getItem(), 1), 0.6F);
	}
	
	private static void registerConditionalCraftingRecipes() {
		if (ModPhotonicCraft.canCraftVanta) {
			GameRegistry.addShapedRecipe(new ResourceLocation(ModPhotonicCraft.MODID + ":vanta"), null, 
				new ItemStack(PhotonicBlocks.vantablock, 4), new Object[] {
					"BBB",
					"BLB",
					"BBB",
					'B', new ItemStack(Blocks.COAL_BLOCK, 1),
					'L', new ItemStack(Items.LAVA_BUCKET, 1)
				}
			);
		}
	}

	private PhotonicRecipes() {
	}

	/*

	public static long recipeCounter = 0;
	@Deprecated
	static void addRecipes_old() {



		wrapper_addShapelessRecipe(new ItemStack(photonicResources.get(8).getItem(), 1),
				new Object[] { new ItemStack(Items.DIAMOND, 1), new ItemStack(photonicResources.get(0).getItem(), 1) });
		wrapper_addShapelessRecipe(new ItemStack(photonicResources.get(9).getItem(), 1),
				new Object[] { new ItemStack(photonicResources.get(0).getItem(), 1), new ItemStack(Items.EMERALD, 1),
						new ItemStack(photonicResources.get(8).getItem(), 1),
						new ItemStack(photonicResources.get(8).getItem(), 1) });
		wrapper_addShapelessRecipe(new ItemStack(photonicResources.get(10).getItem(), 1),
				new Object[] { new ItemStack(photonicResources.get(9).getItem(), 1),
						new ItemStack(photonicResources.get(9).getItem(), 1),
						new ItemStack(photonicResources.get(9).getItem(), 1),
						new ItemStack(photonicResources.get(9).getItem(), 1),
						new ItemStack(photonicResources.get(11).getItem(), 1) });
		wrapper_addShapelessRecipe(new ItemStack(photonicResources.get(10).getItem(), 1),
				new Object[] { new ItemStack(photonicResources.get(9).getItem(), 1),
						new ItemStack(photonicResources.get(9).getItem(), 1),
						new ItemStack(photonicResources.get(9).getItem(), 1),
						new ItemStack(photonicResources.get(9).getItem(), 1), new ItemStack(Items.NETHER_STAR, 1) });
		wrapper_addShapelessRecipe(new ItemStack(photonicResources.get(12).getItem(), 1), new Object[] {
				new ItemStack(photonicResources.get(13).getItem(), 1), new ItemStack(Blocks.GLASS_PANE, 1) });
		wrapper_addShapelessRecipe(new ItemStack(PhotonicBlocks.analoglamp, 1),
				new Object[] { new ItemStack(Blocks.REDSTONE_LAMP, 1), new ItemStack(Items.QUARTZ, 1) });
		wrapper_addShapelessRecipe(new ItemStack(PhotonicBlocks.radioTransceiver, 1), new Object[] {
				new ItemStack(PhotonicBlocks.radioTransmitter, 1), new ItemStack(PhotonicBlocks.radioReceiver, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.erbiumBlock),
				new Object[] { "AAA", "AAA", "AAA", 'A', new ItemStack(photonicResources.get(0).getItem(), 1) });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(0).getItem(), 9),
				new Object[] { "A", 'A', new ItemStack(PhotonicBlocks.erbiumBlock, 1) });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(0).getItem(), 1),
				new Object[] { "AAA", "AAA", "AAA", 'A', new ItemStack(photonicResources.get(3).getItem(), 1) });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(3).getItem(), 9),
				new Object[] { "A", 'A', new ItemStack(photonicResources.get(0).getItem(), 1) });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(1).getItem(), 6),
				new Object[] { "AAA", "   ", "AAA", 'A', new ItemStack(Blocks.GLASS_PANE) });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(2).getItem(), 1),
				new Object[] { "CAC", "BBB", "CDC", 'A', new ItemStack(photonicResources.get(0).getItem(), 1), 'B',
						new ItemStack(photonicResources.get(1).getItem(), 1), 'C', new ItemStack(Items.IRON_INGOT, 1),
						'D', new ItemStack(Items.REDSTONE, 1), });
		for (int i = 0; i < 16; i++)
			wrapper_addShapedRecipe(new ItemStack(PhotonicItems.photonicCoupler, 1, i), new Object[] { "ABA", 'A',
					new ItemStack(photonicResources.get(12).getItem(), 1), 'B', new ItemStack(Items.DYE, 1, i) });
		wrapper_addShapedRecipe(new ItemStack(photonicResources2.get(2).getItem(), 4),
				new Object[] { "TGT", "R R", 'T', new ItemStack(photonicResources2.get(1).getItem(), 1), 'R',
						new ItemStack(Items.REDSTONE, 1), 'G', new ItemStack(Blocks.GLASS, 1), });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(4).getItem(), 1),
				new Object[] { "GGG", "RER", "GGG", 'E', new ItemStack(photonicResources.get(0).getItem(), 1), 'R',
						new ItemStack(Items.REDSTONE, 1), 'G', new ItemStack(Blocks.GLASS_PANE, 1), });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(5).getItem(), 1),
				new Object[] { "GGG", "RER", "GGG", 'E', new ItemStack(photonicResources.get(8).getItem(), 1), 'R',
						new ItemStack(Items.REDSTONE, 1), 'G', new ItemStack(Blocks.GLASS_PANE, 1), });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(6).getItem(), 1),
				new Object[] { "GGG", "RER", "GGG", 'E', new ItemStack(photonicResources.get(9).getItem(), 1), 'R',
						new ItemStack(Items.REDSTONE, 1), 'G', new ItemStack(Blocks.GLASS_PANE, 1), });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(7).getItem(), 1),
				new Object[] { "GGG", "RER", "GGG", 'E', new ItemStack(photonicResources.get(10).getItem(), 1), 'R',
						new ItemStack(Items.REDSTONE, 1), 'G', new ItemStack(Blocks.GLASS_PANE, 1), });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(14).getItem(), 1),
				new Object[] { "GOG", "RER", "GOG", 'E', new ItemStack(photonicResources.get(9).getItem(), 1), 'O',
						new ItemStack(photonicResources.get(1).getItem(), 1), 'R',
						new ItemStack(Blocks.REDSTONE_BLOCK, 1), 'G', new ItemStack(Blocks.GLASS_PANE, 1), });
		wrapper_addShapedRecipe(new ItemStack(photonicResources.get(15).getItem(), 1),
				new Object[] { "RRR", "RER", "RRR", 'E', new ItemStack(photonicResources.get(8).getItem(), 1), 'R',
						new ItemStack(Items.REDSTONE, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(0).getItem(), 1),
				new Object[] { "ISI", "TEG", "ISI", 'E', new ItemStack(photonicResources.get(2).getItem(), 1), 'T',
						new ItemStack(photonicResources2.get(2).getItem(), 1), 'G', new ItemStack(Blocks.GLASS_PANE, 1),
						'S', new ItemStack(Blocks.STONE, 1), 'I', new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(1).getItem(), 1), new Object[] { "III", " M ", "III", 'M',
				new ItemStack(photonicResources.get(12).getItem(), 1), 'I', new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(2).getItem(), 1), new Object[] { "III", "M M", "III", 'M',
				new ItemStack(photonicResources.get(12).getItem(), 1), 'I', new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(3).getItem(), 1),
				new Object[] { "III", " MC", "III", 'M', new ItemStack(photonicResources.get(12).getItem(), 1), 'C',
						new ItemStack(photonicResources.get(13).getItem(), 1), 'I',
						new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(3).getItem(), 1),
				new Object[] { "III", "CM ", "III", 'M', new ItemStack(photonicResources.get(12).getItem(), 1), 'C',
						new ItemStack(photonicResources.get(13).getItem(), 1), 'I',
						new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(4).getItem(), 1),
				new Object[] { "III", "MCM", "III", 'M', new ItemStack(photonicResources.get(12).getItem(), 1), 'C',
						new ItemStack(photonicResources.get(13).getItem(), 1), 'I',
						new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(5).getItem(), 1),
				new Object[] { "IRI", "GTG", "IEI", 'E', new ItemStack(photonicResources.get(0).getItem(), 1), 'R',
						new ItemStack(Items.REDSTONE, 1), 'T', new ItemStack(photonicResources2.get(2).getItem(), 1),
						'G', new ItemStack(Blocks.GLASS, 1), 'I', new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(6).getItem(), 1),
				new Object[] { "IRI", "GTG", "IEI", 'E', new ItemStack(photonicResources.get(0).getItem(), 1), 'R',
						new ItemStack(Items.REDSTONE, 1), 'T', new ItemStack(photonicResources2.get(2).getItem(), 1),
						'G', new ItemStack(Blocks.GLASS, 1), 'I', new ItemStack(Items.GOLD_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(7).getItem(), 1),
				new Object[] { "ICI", "LRG", "ICI", 'L', new ItemStack(Blocks.GLOWSTONE, 1), 'R',
						new ItemStack(Blocks.REDSTONE_LAMP, 1), 'G', new ItemStack(Blocks.GLASS_PANE, 1), 'C',
						new ItemStack(Items.COAL, 1), 'I', new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(9).getItem(), 1),
				new Object[] { "ETE", "LLL", "GGG", 'E', new ItemStack(photonicResources.get(0).getItem(), 1), 'T',
						new ItemStack(photonicResources2.get(2).getItem(), 1), 'L',
						new ItemStack(Blocks.REDSTONE_LAMP, 1), 'G', new ItemStack(Blocks.GLASS, 1) });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(12).getItem(), 1),
				new Object[] { "ETE", "PLP", "GPG", 'E', new ItemStack(photonicResources.get(0).getItem(), 1), 'T',
						new ItemStack(photonicResources2.get(2).getItem(), 1), 'L',
						new ItemStack(Blocks.REDSTONE_LAMP, 1), 'G', new ItemStack(Blocks.GLASS, 1), 'P',
						new ItemStack(Items.DYE, 1, 5) });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.hypnoBomb, 1),
				new Object[] { " C ", "GTG", " C ", 'G', new ItemStack(Items.GOLD_INGOT, 1), 'C',
						new ItemStack(photonicResources.get(9).getItem(), 1), 'T', new ItemStack(Blocks.TNT, 1) });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(8).getItem(), 1),
				new Object[] { "GGG", "GG ", "G  ", 'G', new ItemStack(Blocks.GLASS, 1) });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(8).getItem(), 1),
				new Object[] { "GGG", " GG", "  G", 'G', new ItemStack(Blocks.GLASS, 1) });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(8).getItem(), 1),
				new Object[] { "  G", " GG", "GGG", 'G', new ItemStack(Blocks.GLASS, 1) });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(8).getItem(), 1),
				new Object[] { "G  ", "GG ", "GGG", 'G', new ItemStack(Blocks.GLASS, 1) });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.skyLight, 1),
				new Object[] { "EEE", "GRG", "LLL", 'G', new ItemStack(Blocks.GLASS, 1), 'L',
						new ItemStack(Blocks.GLOWSTONE, 1), 'E', new ItemStack(Items.ENDER_EYE, 1), 'R',
						new ItemStack(Blocks.REDSTONE_BLOCK, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumHelmet, 1),
				new Object[] { "YYY", "Y Y", 'Y', new ItemStack(PhotonicItems.yttriumIngot, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumChestplate, 1),
				new Object[] { "Y Y", "YYY", "YYY", 'Y', new ItemStack(PhotonicItems.yttriumIngot, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumLeggings, 1),
				new Object[] { "YYY", "Y Y", "Y Y", 'Y', new ItemStack(PhotonicItems.yttriumIngot, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumBoots, 1),
				new Object[] { "Y Y", "Y Y", 'Y', new ItemStack(PhotonicItems.yttriumIngot, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumPickaxe, 1), new Object[] { "YYY", " S ", " S ", 'Y',
				new ItemStack(PhotonicItems.yttriumIngot, 1), 'S', new ItemStack(Items.STICK, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumAxe, 1), new Object[] { "YY", "YS", " S", 'Y',
				new ItemStack(PhotonicItems.yttriumIngot, 1), 'S', new ItemStack(Items.STICK, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumAxe, 1), new Object[] { "YY", "SY", "S ", 'Y',
				new ItemStack(PhotonicItems.yttriumIngot, 1), 'S', new ItemStack(Items.STICK, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumHoe, 1), new Object[] { "YY", " S", " S", 'Y',
				new ItemStack(PhotonicItems.yttriumIngot, 1), 'S', new ItemStack(Items.STICK, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumHoe, 1), new Object[] { "YY", "S ", "S ", 'Y',
				new ItemStack(PhotonicItems.yttriumIngot, 1), 'S', new ItemStack(Items.STICK, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumSword, 1), new Object[] { "Y", "Y", "S", 'Y',
				new ItemStack(PhotonicItems.yttriumIngot, 1), 'S', new ItemStack(Items.STICK, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumSpade, 1), new Object[] { "Y", "S", "S", 'Y',
				new ItemStack(PhotonicItems.yttriumIngot, 1), 'S', new ItemStack(Items.STICK, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.yttriumBlock, 1),
				new Object[] { "YYY", "YYY", "YYY", 'Y', new ItemStack(PhotonicItems.yttriumIngot, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.yttriumIngot, 9),
				new Object[] { "Y", 'Y', new ItemStack(PhotonicBlocks.yttriumBlock, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.miningHelmet, 1), new Object[] { " F ", "III", "I I", 'F',
				new ItemStack(PhotonicItems.flashlight, 1), 'I', new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.glisterstone, 4),
				new Object[] { "GMG", "MLM", "GMG", 'G', new ItemStack(Blocks.GLASS, 1), 'M',
						new ItemStack(photonicResources.get(12).getItem(), 1), 'L',
						new ItemStack(Blocks.GLOWSTONE, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.laserMerger, 1),
				new Object[] { "SSS", "MLM", "SSS", 'S', new ItemStack(Blocks.STONE, 1), 'M',
						new ItemStack(photonicResources.get(12).getItem(), 1), 'L',
						new ItemStack(laserItems.get(0).getItem(), 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.flashlight, 1),
				new Object[] { "SSS", "ELG", "SSS", 'S', new ItemStack(Items.IRON_INGOT, 1), 'E',
						new ItemStack(photonicResources.get(0).getItem(), 1), 'L',
						new ItemStack(Blocks.REDSTONE_LAMP, 1), 'G', new ItemStack(Blocks.GLASS, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.rangefinder, 1),
				new Object[] { "SSS", "ELG", "SSS", 'S', new ItemStack(Items.IRON_INGOT, 1), 'E',
						new ItemStack(photonicResources.get(0).getItem(), 1), 'L',
						new ItemStack(photonicResources.get(15).getItem(), 1), 'G', new ItemStack(Blocks.GLASS, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.remote, 1),
				new Object[] { "SSS", "ELG", "SSS", 'S', new ItemStack(Items.IRON_INGOT, 1), 'E',
						new ItemStack(Blocks.STONE_BUTTON, 1), 'L',
						new ItemStack(photonicResources.get(15).getItem(), 1), 'G', new ItemStack(Blocks.GLASS, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.laserpointer, 1),
				new Object[] { "SSS", "ELG", "SSS", 'S', new ItemStack(Items.IRON_INGOT, 1), 'E',
						new ItemStack(photonicResources.get(0).getItem(), 1), 'L',
						new ItemStack(laserItems.get(0).getItem(), 1), 'G', new ItemStack(Blocks.GLASS, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.oreRadar, 1),
				new Object[] { "SSS", "ELG", "SSS", 'S', new ItemStack(Items.IRON_INGOT, 1), 'E',
						new ItemStack(photonicResources.get(0).getItem(), 1), 'L',
						new ItemStack(photonicResources2.get(0).getItem(), 1), 'G', new ItemStack(Blocks.GLASS, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.laserGenerator, 1),
				new Object[] { "III", "TBT", "RDR", 'I', new ItemStack(Items.IRON_INGOT, 1), 'B',
						new ItemStack(Blocks.IRON_BLOCK, 1), 'R', new ItemStack(Items.REDSTONE, 1), 'T',
						new ItemStack(photonicResources2.get(1).getItem(), 1), 'D',
						new ItemStack(laserItems.get(5).getItem(), 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.laserCharger, 1),
				new Object[] { "RER", "OBO", "RER", 'O', new ItemStack(Blocks.OBSIDIAN, 1), 'B',
						new ItemStack(PhotonicBlocks.laserGenerator, 1), 'R', new ItemStack(Items.REDSTONE, 1), 'E',
						new ItemStack(photonicResources.get(0).getItem(), 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.lightDetector, 1),
				new Object[] { " R ", "QGQ", " R ", 'Q', new ItemStack(Items.QUARTZ, 1), 'G',
						new ItemStack(Blocks.GLASS, 1), 'R', new ItemStack(Items.REDSTONE, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.safetyglasses, 1),
				new Object[] { " O ", "BGB", 'O', new ItemStack(Items.DYE, 1, 14), 'G',
						new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 15), 'B',
						new ItemStack(Blocks.STAINED_GLASS, 1, 15), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.irglasses, 1),
				new Object[] { " O ", "BGB", 'O', new ItemStack(photonicResources.get(15).getItem(), 1), 'G',
						new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 14), 'B',
						new ItemStack(Blocks.STAINED_GLASS, 1, 14), });
		wrapper_addShapedRecipe(new ItemStack(photonicResources2.get(0).getItem(), 1),
				new Object[] { "RGR", "TBT", "IBI", 'G', new ItemStack(Items.GOLD_INGOT, 1), 'T',
						new ItemStack(photonicResources2.get(1).getItem(), 1), 'R', new ItemStack(Items.REDSTONE, 1),
						'B', new ItemStack(photonicResources.get(1).getItem(), 1), 'I',
						new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.tantalumBlock),
				new Object[] { "AAA", "AAA", "AAA", 'A', new ItemStack(photonicResources2.get(1).getItem(), 1) });
		wrapper_addShapedRecipe(new ItemStack(photonicResources2.get(1).getItem(), 9),
				new Object[] { "A", 'A', new ItemStack(PhotonicBlocks.tantalumBlock, 1) });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(10).getItem(), 1),
				new Object[] { "III", "IMG", "IFI", 'I', new ItemStack(Items.IRON_INGOT, 1), 'G',
						new ItemStack(Blocks.GLASS, 1), 'F', new ItemStack(Blocks.FURNACE, 1), 'M',
						new ItemStack(photonicResources2.get(0).getItem(), 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(11).getItem(), 1),
				new Object[] { "III", " MC", "III", 'M', new ItemStack(photonicResources.get(12).getItem(), 1), 'C',
						new ItemStack(Items.REDSTONE, 1), 'I', new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(laserItems.get(11).getItem(), 1),
				new Object[] { "III", "CM ", "III", 'M', new ItemStack(photonicResources.get(12).getItem(), 1), 'C',
						new ItemStack(Items.REDSTONE, 1), 'I', new ItemStack(Items.IRON_INGOT, 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.microwavePanel, 2),
				new Object[] { "A A", " E ", "A A", 'A', new ItemStack(PhotonicBlocks.anorthosite, 1), 'E',
						new ItemStack(photonicResources.get(0).getItem(), 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.microwaveGenerator, 1),
				new Object[] { "PLP", "RGR", "PMP", 'P', new ItemStack(PhotonicBlocks.microwavePanel, 1), 'R',
						new ItemStack(Items.REDSTONE, 1), 'G', new ItemStack(PhotonicBlocks.laserGenerator, 1), 'L',
						new ItemStack(laserItems.get(0).getItem(), 1), 'M',
						new ItemStack(photonicResources2.get(0).getItem(), 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.hahniumBlock),
				new Object[] { "AAA", "AAA", "AAA", 'A', new ItemStack(photonicResources2.get(3).getItem(), 1) });
		wrapper_addShapedRecipe(new ItemStack(photonicResources2.get(3).getItem(), 9),
				new Object[] { "A", 'A', new ItemStack(PhotonicBlocks.hahniumBlock, 1) });
		wrapper_addShapedRecipe(new ItemStack(photonicResources2.get(4).getItem(), 4), new Object[] { "GIG", "GIG",
				"GIG", 'G', new ItemStack(Blocks.GLASS_PANE, 1), 'I', new ItemStack(Blocks.IRON_BARS, 1) });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.radioReceiver, 1),
				new Object[] { "SAS", "RRR", "SAS", 'A', new ItemStack(photonicResources2.get(4).getItem(), 1), 'S',
						new ItemStack(Blocks.STONE, 1), 'R', new ItemStack(Items.REDSTONE, 1) });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.radioTransmitter, 1),
				new Object[] { "SRS", "ARA", "SRS", 'A', new ItemStack(photonicResources2.get(4).getItem(), 1), 'S',
						new ItemStack(Blocks.STONE, 1), 'R', new ItemStack(Items.REDSTONE, 1) });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.pocketRadio, 1),
				new Object[] { "SAS", "ERG", "SSS", 'S', new ItemStack(Items.IRON_INGOT, 1), 'A',
						new ItemStack(photonicResources2.get(4).getItem(), 1), 'E',
						new ItemStack(photonicResources.get(0).getItem(), 1), 'R',
						new ItemStack(PhotonicBlocks.radioReceiver, 1), 'G', new ItemStack(Blocks.GLASS, 1), });
		wrapper_addShapedRecipe(new ItemStack(photonicResources2.get(5).getItem(), 1),
				new Object[] { " II", "IED", " II", 'D', new ItemStack(Items.DIAMOND, 1), 'E',
						new ItemStack(photonicResources.get(2).getItem(), 1), 'I',
						new ItemStack(Items.IRON_INGOT, 1) });
		wrapper_addShapedRecipe(new ItemStack(PhotonicItems.terahertzReceiver, 1),
				new Object[] { "III", "RGR", "III", 'R', new ItemStack(Items.REDSTONE, 1), 'G',
						new ItemStack(photonicResources2.get(5).getItem(), 1), 'I',
						new ItemStack(Items.IRON_INGOT, 1) });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.terahertzTransmitter, 1),
				new Object[] { "III", "TBT", "RGR", 'I', new ItemStack(Items.IRON_INGOT, 1), 'B',
						new ItemStack(Blocks.IRON_BLOCK, 1), 'R', new ItemStack(Items.REDSTONE, 1), 'T',
						new ItemStack(photonicResources2.get(1).getItem(), 1), 'G',
						new ItemStack(photonicResources2.get(5).getItem(), 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.remoteReceiver, 1),
				new Object[] { "III", "RBR", "SGS", 'I', new ItemStack(Items.IRON_INGOT, 1), 'B',
						new ItemStack(Blocks.IRON_BLOCK, 1), 'R', new ItemStack(Items.REDSTONE, 1), 'S',
						new ItemStack(Blocks.COBBLESTONE, 1), 'G',
						new ItemStack(photonicResources.get(15).getItem(), 1), });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.bariumBlock),
				new Object[] { "AAA", "AAA", "AAA", 'A', new ItemStack(photonicResources2.get(6).getItem(), 1) });
		wrapper_addShapedRecipe(new ItemStack(photonicResources2.get(6).getItem(), 9),
				new Object[] { "A", 'A', new ItemStack(PhotonicBlocks.bariumBlock, 1) });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.gammaRayCannon, 1),
				new Object[] { "BEB", "ILR", "GGG", 'B', new ItemStack(PhotonicBlocks.bariumBlock, 1), 'I',
						new ItemStack(Blocks.IRON_BLOCK, 1), 'E', new ItemStack(photonicResources.get(7).getItem(), 1),
						'L', new ItemStack(laserItems.get(0).getItem(), 1), 'R',
						new ItemStack(Blocks.REDSTONE_BLOCK, 1), 'G',
						new ItemStack(photonicResources2.get(5).getItem(), 1) });
		wrapper_addShapedRecipe(new ItemStack(PhotonicBlocks.pulsedLaser, 1),
				new Object[] { "EHE", "RLR", "EHE", 'H', new ItemStack(photonicResources.get(6).getItem(), 1),
						'L', new ItemStack(laserItems.get(0).getItem(), 1),
						'E', new ItemStack(photonicResources.get(0).getItem(), 1),
						'R', new ItemStack(Items.REPEATER, 1)});
		

	}

	private static void wrapper_addShapedRecipe(ItemStack itemStack, Object[] objects) {
		++recipeCounter;
		ResourceLocation name = new ResourceLocation("PhotonicCraft:crafting_recipe_" + recipeCounter);
		GameRegistry.addShapedRecipe(name, group, itemStack, objects);
	}

	private static void wrapper_addShapelessRecipe(ItemStack itemStack, Object[] objects) {
		++recipeCounter;
		ResourceLocation name = new ResourceLocation("PhotonicCraft:crafting_recipe_" + recipeCounter);
		List<Ingredient> ingrs = new ArrayList<Ingredient>();
		for (Object o : objects) {
			if (o instanceof ItemStack) {
				ingrs.add(Ingredient.fromItem(((ItemStack) o).getItem()));
			}
		}
		GameRegistry.addShapelessRecipe(name, group, itemStack, ingrs.toArray(new Ingredient[ingrs.size()]));
	}*/
	
}
