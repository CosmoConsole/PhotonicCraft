package email.com.gmail.cosmoconsole.forge.photoniccraft.integration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUltravioletRecipe;
import net.minecraft.item.ItemStack;

/**
 * The public interface for PhotonicCraft. A well-behaving mod should only
 * access PhotonicAPI to interact with PhotonicCraft.
 */
public class PhotonicAPI {
	private static List<PhotonicUltravioletRecipe> recipes = new ArrayList<>();;

	/**
	 * Adds a recipe for items affected by the presence of the Ultraviolet
	 * Blacklight. If a item is within sufficient vicinity of one and there is
	 * nothing obstructing light between the item and the blacklight, the item
	 * will be converted into the other after a random interval.
	 * 
	 * @param mod
	 *            The mod registering the recipe.
	 * @param input
	 *            The input ItemStack (what item is converted to what?). Stack
	 *            sizes greater from 1 are supported: if you plan to add
	 *            multiple recipes with only differing item counts, add the one
	 *            with the highest source item count first.
	 * @param output
	 *            The resulting ItemStack (what is the resulting item?). Stack
	 *            sizes greater from 1 are supported.
	 */
	public static void addUltravioletRecipe(@Nonnull String modid, @Nonnull ItemStack input,
			@Nonnull ItemStack output) {
		if (input == ItemStack.EMPTY) {
			throw new IllegalArgumentException("cannot add Ultraviolet Blacklight recipe for an empty input item");
		}
		for (PhotonicUltravioletRecipe recipe : recipes) {
			ItemStack source = recipe.getInput();
			if (ItemStack.areItemsEqual(input, source) && source.getCount() <= input.getCount()) {
				throw new IllegalArgumentException(
						"an Ultraviolet Blacklight recipe for this item (or one with smaller stack size) has already been registered, so cannot register this recipe (as it would have no effect)");
			}
		}
		recipes.add(new PhotonicUltravioletRecipe(input, output));
	}

	/**
	 * Returns the number of items "left over" by an Ultraviolet recipe.
	 * 
	 * @param item
	 *            The item that will be attempted to be processed.
	 * @return The number of leftover unprocessed input items, or -1 if recipe
	 *         was not found.
	 */
	public static int getUltravioletStackLeftover(@Nonnull ItemStack item) {
		for (PhotonicUltravioletRecipe recipe : recipes) {
			ItemStack result = recipe.tryPrepare(item);
			if (!result.isEmpty()) {
				return recipe.getLeftover(item);
			}
		}
		return -1;
	}

	/**
	 * Attempts to process an ItemStack based on the already registered
	 * Ultraviolet Blacklight recipes.
	 * 
	 * @param item
	 *            The item that will be attempted to be processed.
	 * @return The successfully processed ItemStack (check the stack size before
	 *         assigning to slot or dropping!). If no recipe wsa found, returns
	 *         an empty ItemStack (ItemStack.EMPTY).
	 */
	public static ItemStack tryToProcessUltravioletForItem(@Nonnull ItemStack item) {
		for (PhotonicUltravioletRecipe recipe : recipes) {
			ItemStack result = recipe.tryPrepare(item);
			if (!result.isEmpty()) {
				return result;
			}
		}
		return ItemStack.EMPTY;
	}

	private PhotonicAPI() {
	}
}
