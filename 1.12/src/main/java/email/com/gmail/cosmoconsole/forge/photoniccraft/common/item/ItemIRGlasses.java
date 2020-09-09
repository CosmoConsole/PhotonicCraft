package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicInfraredPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.CapabilityProviderEnergySerializable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.IEquipListenable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemIRGlasses extends ItemArmor implements ISpecialArmor, IEquipListenable {
	public static final int TIMER = 4800;
	public static final int RF_PER_T = 10;
	public String textureName;

	public ItemIRGlasses(ArmorMaterial p_i45325_1_, int p_i45325_2_, EntityEquipmentSlot p_i45325_3_) {
		super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
	}

	public ItemIRGlasses(String unlocalizedName, ArmorMaterial material, String textureName, EntityEquipmentSlot type) {
		super(material, 0, type);
		this.textureName = textureName;
		this.setUnlocalizedName(unlocalizedName);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add((TIMER * RF_PER_T - this.getEnergy(stack)) + " / " + (TIMER * RF_PER_T) + " RF");
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return ModPhotonicCraft.MODID + ":textures/armor/" + this.textureName + "_"
				+ (this.armorType == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
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
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage,
			int slot) {
		return new ArmorProperties(0, 0, 0);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new CapabilityProviderEnergySerializable(stack, TIMER * RF_PER_T, 20);
	}
	
	private boolean lastTickThirdPerson = false;

	@Override
	public void onArmorTick(World par2World, EntityPlayer par3Entity, ItemStack par1ItemStack) {
		if (par2World.isRemote) {
			PotionEffect pe = ((EntityLivingBase) par3Entity).getActivePotionEffect(MobEffects.NIGHT_VISION);
			if (pe != null) {
				pe.setPotionDurationMax(true);
			}
			boolean nowThirdPerson = net.minecraft.client.Minecraft.getMinecraft().gameSettings.thirdPersonView != 0;
			if (!lastTickThirdPerson && nowThirdPerson) {
				PhotonicUtils.removeInfraredEffect();
			} else if (lastTickThirdPerson && !nowThirdPerson) {
				PhotonicUtils.applyInfraredEffect();
			}
			lastTickThirdPerson = nowThirdPerson;
			return;
		}
		if (par3Entity instanceof EntityPlayer) {
			if (!par3Entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).equals(par1ItemStack))
				return;
			if (((EntityPlayer) par3Entity).isSpectator())
				return;
			if (par1ItemStack.hasTagCompound() == false)
				par1ItemStack.setTagCompound(new NBTTagCompound());
			int energy = par1ItemStack.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
			if (!par3Entity.capabilities.isCreativeMode)
				energy += RF_PER_T;
			if (energy >= (RF_PER_T * TIMER)) {
				par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, RF_PER_T * TIMER);
				return;
			}
			par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, energy);
			if (!((EntityPlayer) par3Entity).isSpectator()) {
				EntityLivingBase elb = ((EntityLivingBase) par3Entity);
				if (elb.getActivePotionEffect(MobEffects.NIGHT_VISION) == null
						|| elb.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration() < 300) {
					PotionEffect pe = new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, true, false);
					elb.addPotionEffect(pe);
				}
			}
		}
	}

	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par1ItemStack.hasTagCompound() == false) {
			par1ItemStack.setTagCompound(new NBTTagCompound());
			par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, 0);
		}
	}

	/*
	 * @Override public void onUpdate(ItemStack par1ItemStack, World par2World,
	 * Entity par3Entity, int par4, boolean par5) { if (par2World.isRemote)
	 * return; if (par3Entity instanceof EntityPlayer) { if (((EntityPlayer)
	 * par3Entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD) == null)
	 * return; if (((EntityPlayer)
	 * par3Entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())
	 * return; if (!((EntityPlayer)
	 * par3Entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).equals(
	 * par1ItemStack)) return; if (((EntityPlayer) par3Entity).isSpectator())
	 * return; if (par1ItemStack.hasTagCompound() == false)
	 * par1ItemStack.setTagCompound(new NBTTagCompound()); int energy =
	 * par1ItemStack.getTagCompound().getInteger(PhotonicUtils.
	 * TAG_ITEM_RF_ENERGY); if (!((EntityPlayer)
	 * par3Entity).capabilities.isCreativeMode) energy += RF_PER_T; if (energy
	 * >= (RF_PER_T * TIMER)) {
	 * par1ItemStack.getTagCompound().setInteger(PhotonicUtils.
	 * TAG_ITEM_RF_ENERGY, RF_PER_T * TIMER); return; }
	 * par1ItemStack.getTagCompound().setInteger(PhotonicUtils.
	 * TAG_ITEM_RF_ENERGY, energy); if (!((EntityPlayer)
	 * par3Entity).isSpectator()) { PotionEffect pe = new
	 * PotionEffect(MobEffects.NIGHT_VISION, 15, 0, true, false);
	 * pe.setPotionDurationMax(true); ((EntityLivingBase)
	 * par3Entity).addPotionEffect(pe); } } }
	 */
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public void onEquip(World world, EntityPlayer player, ItemStack itemStack) {
		PhotonicUtils.sendPacketToPlayer(ModPhotonicCraft.network, player, new PhotonicInfraredPacket(true));
	}

	@Override
	public void onUnequip(World world, EntityPlayer player, ItemStack itemStack) {
		PhotonicUtils.sendPacketToPlayer(ModPhotonicCraft.network, player, new PhotonicInfraredPacket(false));
		if (player.getActivePotionEffect(MobEffects.NIGHT_VISION) != null
				&& player.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration() <= 300) {
			player.removePotionEffect(MobEffects.NIGHT_VISION);
		}
	}

	@Override
	public void onEnterSpectator(World world, EntityPlayer player, ItemStack itemStack) {
		this.onUnequip(world, player, itemStack);
	}

	@Override
	public void onExitSpectator(World world, EntityPlayer player, ItemStack itemStack) {
		this.onEquip(world, player, itemStack);
	}
}
