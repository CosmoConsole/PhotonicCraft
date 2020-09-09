package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IEquipListenable {
	/**
	 * Called whenever the armor item is equipped by a player.
	 * 
	 * @param world
	 *            The world the player is in
	 * @param player
	 *            The player in question
	 * @param itemStack
	 *            The ItemStack representing the armor
	 */
	public void onEquip(World world, EntityPlayer player, ItemStack itemStack);

	/**
	 * Called whenever the armor item is unequipped by a player.
	 * 
	 * @param world
	 *            The world the player is in
	 * @param player
	 *            The player in question
	 * @param itemStack
	 *            The ItemStack representing the armor
	 */
	public void onUnequip(World world, EntityPlayer player, ItemStack itemStack);

	/**
	 * Called whenever a player enters the spectator game mode wearing a
	 * specific armor item.
	 * 
	 * @param world
	 *            The world the player is in
	 * @param player
	 *            The player in question
	 * @param itemStack
	 *            The ItemStack representing the armor
	 */
	public void onEnterSpectator(World world, EntityPlayer player, ItemStack itemStack);

	/**
	 * Called whenever a player exits the spectator game mode wearing a specific
	 * armor item.
	 * 
	 * @param world
	 *            The world the player is in
	 * @param player
	 *            The player in question
	 * @param itemStack
	 *            The ItemStack representing the armor
	 */
	public void onExitSpectator(World world, EntityPlayer player, ItemStack itemStack);
}
