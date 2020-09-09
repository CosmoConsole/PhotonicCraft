package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import net.minecraft.item.ItemStack;

/**
 * A recipe for Ultraviolet Blacklights. The source item will slowly (at random) get turned into the result item if under such a blacklight.
 */
public class PhotonicUltravioletRecipe {
	private ItemStack source;
	private ItemStack target;

	public PhotonicUltravioletRecipe(ItemStack input, ItemStack output) {
		this.source = input.copy();
		this.target = output.copy();
	}

	public ItemStack getInput() {
		return this.source;
	}

	public int getLeftover(ItemStack stack) {
		if (!ItemStack.areItemsEqual(stack, source))
			return -1;
		if (stack.getCount() < source.getCount())
			return -1;
		return stack.getCount() - source.getCount() * (stack.getCount() / source.getCount());
	}

	public ItemStack getOutput() {
		return this.target;
	}

	public ItemStack tryPrepare(ItemStack stack) {
		if (!ItemStack.areItemsEqual(stack, source))
			return ItemStack.EMPTY;
		if (stack.getCount() < source.getCount())
			return ItemStack.EMPTY;
		ItemStack result = target.copy();
		result.setCount(result.getCount() * (stack.getCount() / source.getCount()));
		return result;
	}
}
