package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.CapabilityProviderEnergySerializable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

public class ItemPowerBank extends Item {
	public static final int TIMER = 400;
	public static final int RF_PER_T = 500;

	public ItemPowerBank() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_powerbank");
		this.setMaxStackSize(1);
		this.setMaxDamage(TIMER);
		this.setNoRepair();
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add((TIMER * RF_PER_T - this.getEnergy(stack)) + " / " + (TIMER * RF_PER_T) + " RF");
		tooltip.add(I18n.format("msg.photoniccraft_powerbank.help"));
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.hasTagCompound() == false)
			return 0;
		return (double) (this.getMaxEnergyStored(stack) - this.getEnergyStored(stack))
				/ (double) (this.getMaxEnergyStored(stack));
	};

	private int getEnergy(ItemStack par1ItemStack) {
		if (!par1ItemStack.hasTagCompound())
			return 0;
		return par1ItemStack.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
	}

	public int getEnergyStored(ItemStack arg0) {
		if (arg0 == null)
			return 0;
		if (arg0.hasTagCompound() == false)
			return 0;
		return RF_PER_T * TIMER - arg0.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
	}

	public int getMaxEnergyStored(ItemStack arg0) {
		return RF_PER_T * TIMER;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new CapabilityProviderEnergySerializable(stack, TIMER * RF_PER_T, 1000, RF_PER_T);
	}

	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par1ItemStack.setTagCompound(new NBTTagCompound());
		par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, 0);
	}


	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if (par3Entity instanceof EntityPlayer) {
			if (!par1ItemStack.hasTagCompound()) {
				par1ItemStack.setTagCompound(new NBTTagCompound());
				par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, 0);
			}
			EntityPlayer p = (EntityPlayer) par3Entity;
			if (p.getHeldItemMainhand() == par1ItemStack || p.getHeldItemOffhand() == par1ItemStack) {
				int energy = par1ItemStack.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
				int power = RF_PER_T;
				if (energy >= (RF_PER_T * TIMER)) {
					power = 0;
				}
				if (!((EntityPlayer) par3Entity).capabilities.isCreativeMode)
					energy += RF_PER_T;
				if (energy >= (RF_PER_T * TIMER)) {
					power -= (energy - (RF_PER_T * TIMER));
					par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, RF_PER_T * TIMER);
				} else {
					par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, energy);
				}
				if (power > 0) {
					List<ItemStack> lei = new ArrayList<>();
					for (ItemStack i: PhotonicUtils.multipleLists(PhotonicUtils.getHotbarInventory(p), p.inventory.armorInventory, p.inventory.offHandInventory)) {
						lei.add(i);
					}
					Collections.shuffle(lei);
					for (ItemStack i: lei) {
						if (i == null)
							continue;
						if (power < 1)
							return;
						if (i.hasCapability(CapabilityEnergy.ENERGY, null)) {
							power -= i.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(power, false);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return oldStack.getItem() != newStack.getItem() || slotChanged;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	};
}
