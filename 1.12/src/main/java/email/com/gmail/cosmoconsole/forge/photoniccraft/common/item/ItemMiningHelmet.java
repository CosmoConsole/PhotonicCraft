package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLightAir;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.CapabilityProviderEnergySerializable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemMiningHelmet extends ItemArmor implements ISpecialArmor {
	public static final int TIMER = 12000;
	public static final int RF_PER_T = 4;
	public String textureName;

	public ItemMiningHelmet(ArmorMaterial p_i45325_1_, int p_i45325_2_, EntityEquipmentSlot p_i45325_3_) {
		super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
	}

	public ItemMiningHelmet(String unlocalizedName, ArmorMaterial material, String textureName,
			EntityEquipmentSlot type) {
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

	@Override
	public void onArmorTick(World par2World, EntityPlayer par3Entity, ItemStack par1ItemStack) {
		if (par2World.isRemote)
			return;
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
			int x = MathHelper.floor(par3Entity.posX);
			int y = MathHelper.floor(par3Entity.posY) + 1;
			int z = MathHelper.floor(par3Entity.posZ);
			for (int xo = -1; xo < 2; xo++)
				for (int yo = -1; yo < 2; yo++)
					for (int zo = -1; zo < 2; zo++) {
						BlockPos bp = new BlockPos(x + xo, y + yo, z + zo);
						if (par2World.getBlockState(bp).getBlock() == Blocks.AIR) {
							par2World.setBlockState(bp, PhotonicBlocks.lightAir.getDefaultState(), 3);
						} else if (par2World.getBlockState(bp).getBlock() == PhotonicBlocks.lightAir) {
							TileEntity te = par2World.getTileEntity(bp);
							if (te instanceof TileEntityLightAir)
								((TileEntityLightAir) te).reset();
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
	 * TAG_ITEM_RF_ENERGY, energy); int x = MathHelper.floor(par3Entity.posX);
	 * int y = MathHelper.floor(par3Entity.posY) + 1; int z =
	 * MathHelper.floor(par3Entity.posZ); for (int xo = -1; xo < 2; xo++) for
	 * (int yo = -1; yo < 2; yo++) for (int zo = -1; zo < 2; zo++) { int ax = x
	 * + xo, ay = y + yo, az = z + zo; if (par2World.getBlockState(new
	 * BlockPos(ax, ay, az)).getBlock() == Blocks.AIR || par2World
	 * .getBlockState(new BlockPos(ax, ay, az)).getBlock() ==
	 * PhotonicBlocks.lightAir) { par2World.setBlockState(new BlockPos(ax, ay,
	 * az), PhotonicBlocks.lightAir.getDefaultState(), 3); } } } }
	 */

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}
}
