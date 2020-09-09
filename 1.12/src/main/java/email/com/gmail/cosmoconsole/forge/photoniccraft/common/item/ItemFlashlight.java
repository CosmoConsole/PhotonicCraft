package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLightAir;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.CapabilityProviderEnergySerializable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemFlashlight extends Item {
	public static final int TIMER = 18000;
	public static final int RF_PER_T = 2;

	public ItemFlashlight() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_flashlight");
		this.setMaxStackSize(1);
		this.setMaxDamage(TIMER);
		this.setNoRepair();
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add((TIMER * RF_PER_T - this.getEnergy(stack)) + " / " + (TIMER * RF_PER_T) + " RF");
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (!stack.hasTagCompound())
			return 0;
		return (double) (this.getMaxEnergyStored(stack) - this.getEnergyStored(stack))
				/ (double) (this.getMaxEnergyStored(stack));
	}

	private int getEnergy(ItemStack par1ItemStack) {
		if (!par1ItemStack.hasTagCompound())
			return 0;
		return par1ItemStack.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
	}

	public int getEnergyStored(ItemStack arg0) {
		if (arg0 == null)
			return 0;
		if (!arg0.hasTagCompound())
			return 0;
		return RF_PER_T * TIMER - arg0.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
	};

	public int getMaxEnergyStored(ItemStack arg0) {
		return RF_PER_T * TIMER;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new CapabilityProviderEnergySerializable(stack, TIMER * RF_PER_T, 20);
	}

	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par1ItemStack.setTagCompound(new NBTTagCompound());
		par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, 0);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if (par2World.isRemote)
			return;
		if (par3Entity instanceof EntityPlayer) {
			if (!((EntityPlayer) par3Entity).getHeldItem(EnumHand.MAIN_HAND).equals(par1ItemStack)
					&& !((EntityPlayer) par3Entity).getHeldItem(EnumHand.OFF_HAND).equals(par1ItemStack))
				return;
			if (((EntityPlayer) par3Entity).isSpectator())
				return;
			if (!par1ItemStack.hasTagCompound())
				par1ItemStack.setTagCompound(new NBTTagCompound());
			int energy = getEnergy(par1ItemStack);
			if (!((EntityPlayer) par3Entity).capabilities.isCreativeMode)
				energy += RF_PER_T;
			if (energy >= (RF_PER_T * TIMER)) {
				par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, RF_PER_T * TIMER);
				return;
			}
			par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, energy);
			final double DD = 0.5;
			double x = par3Entity.posX, y = par3Entity.posY + par3Entity.getEyeHeight(), z = par3Entity.posZ;
			Vec3d lookVecNorm = par3Entity.getLookVec().scale(DD);
			double dx = lookVecNorm.x, dy = lookVecNorm.y, dz = lookVecNorm.z;
			BlockPos lbp = null;
			for (int i = 0; i < 21; ++i) {
				BlockPos bp = new BlockPos(x, y, z);
				x += dx;
				y += dy;
				z += dz;
				if (bp.equals(lbp)) {
					continue;
				}
				lbp = bp;
				IBlockState bs = par2World.getBlockState(bp);
				if (bs.getBlock() == Blocks.AIR) {
					par2World.setBlockState(bp, PhotonicBlocks.lightAir.getDefaultState(), 3);
				} else if (bs.getBlock() == PhotonicBlocks.lightAir) {
					TileEntity te = par2World.getTileEntity(bp);
					if (te instanceof TileEntityLightAir)
						((TileEntityLightAir) te).reset();
				} else if (bs.getBlock().getLightOpacity(bs, par2World, bp) > 0) {
					break;
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
	}
}
