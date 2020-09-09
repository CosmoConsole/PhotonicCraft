package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.CapabilityProviderEnergySerializable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemRangefinder extends Item {
	public static final int TIMER = 240;
	public static final int RF_PER_T = 100;

	public ItemRangefinder() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_rangefinder");
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
		if (stack.hasTagCompound() == false)
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
		if (arg0.hasTagCompound() == false)
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
	public ActionResult<ItemStack> onItemRightClick(World p_77659_2_, EntityPlayer p_77659_3_, EnumHand hand) {
		ItemStack p_77659_1_ = p_77659_3_.getHeldItem(hand);
		if (p_77659_1_.hasTagCompound() == false)
			p_77659_1_.setTagCompound(new NBTTagCompound());
		int energy = p_77659_1_.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
		if (!p_77659_3_.capabilities.isCreativeMode)
			energy += RF_PER_T;
		if (energy >= (RF_PER_T * TIMER)) {
			p_77659_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, RF_PER_T * TIMER);
			return new ActionResult(EnumActionResult.SUCCESS, p_77659_1_);
		}
		p_77659_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, energy);
		double dist = -1;
		double hdist = -1;
		double vdist = -1;
		if (p_77659_2_.isRemote) {
			RayTraceResult mop = ItemLaserPointer.rayTraceBoth(p_77659_3_,
					Minecraft.getMinecraft().getRenderViewEntity(), 200, 1.0F);
			if (mop != null) {
				Vec3d pcx = new Vec3d(p_77659_3_.posX, p_77659_3_.posY + p_77659_3_.getEyeHeight(), p_77659_3_.posZ);
				dist = mop.hitVec.subtract(pcx).lengthVector();
				if (dist >= 180)
					dist = -1;
				else {
					vdist = mop.hitVec.y - pcx.y;
					hdist = Math.sqrt((dist * dist) - (vdist * vdist));
					if (Double.isNaN(hdist) || (hdist != hdist) || (String.format("%.2f", hdist).equals("NaN")))
						hdist = 0;
				}
			}
			p_77659_3_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rangefinder.distance",
					(dist < 0 ? "--" : String.format("%.2f", dist) + " m")));
			p_77659_3_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rangefinder.hdistance",
					(dist < 0 ? "--" : String.format("%.2f", hdist) + " m")));
			p_77659_3_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rangefinder.vdistance",
					(dist < 0 ? "--" : String.format("%.2f", vdist) + " m")));
			p_77659_3_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rangefinder.angle",
					(dist < 0 ? "--" : String.format("%.2f", Math.toDegrees(Math.atan2(vdist, hdist))) + "\u00B0")));
		}
		return new ActionResult(EnumActionResult.SUCCESS, p_77659_1_);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer p_77648_2_, World p_77648_3_, BlockPos pos, EnumHand hand,
			EnumFacing face, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		ItemStack p_77648_1_ = p_77648_2_.getHeldItem(hand);
		if (p_77648_1_.hasTagCompound() == false)
			p_77648_1_.setTagCompound(new NBTTagCompound());
		int energy = p_77648_1_.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
		if (!p_77648_2_.capabilities.isCreativeMode)
			energy += RF_PER_T;
		if (energy >= (RF_PER_T * TIMER)) {
			p_77648_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, RF_PER_T * TIMER);
			return EnumActionResult.FAIL;
		}
		p_77648_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, energy);
		double dist = -1;
		double hdist = -1;
		double vdist = -1;
		if (p_77648_3_.isRemote) {
			RayTraceResult mop = ItemLaserPointer.rayTraceBoth(p_77648_2_,
					Minecraft.getMinecraft().getRenderViewEntity(), 200, 1.0F);
			if (mop != null) {
				Vec3d pcx = new Vec3d(p_77648_2_.posX, p_77648_2_.posY + p_77648_2_.getEyeHeight(), p_77648_2_.posZ);
				dist = mop.hitVec.subtract(pcx).lengthVector();
				if (dist >= 180)
					dist = -1;
				else {
					vdist = mop.hitVec.y - pcx.y;
					hdist = Math.sqrt((dist * dist) - (vdist * vdist));
					if (Double.isNaN(hdist) || (hdist != hdist) || (String.format("%.2f", hdist).equals("NaN")))
						hdist = 0;
				}
			}
			p_77648_2_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rangefinder.distance",
					(dist < 0 ? "--" : String.format("%.2f", dist) + " m")));
			p_77648_2_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rangefinder.hdistance",
					(dist < 0 ? "--" : String.format("%.2f", hdist) + " m")));
			p_77648_2_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rangefinder.vdistance",
					(dist < 0 ? "--" : String.format("%.2f", vdist) + " m")));
			p_77648_2_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rangefinder.angle",
					(dist < 0 ? "--" : String.format("%.2f", Math.toDegrees(Math.atan2(vdist, hdist))) + "\u00B0")));
		}
		return EnumActionResult.SUCCESS;
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
