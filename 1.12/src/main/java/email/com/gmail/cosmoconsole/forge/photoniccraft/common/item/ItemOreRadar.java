package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import java.util.ArrayList;
import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.CapabilityProviderEnergySerializable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.oredict.OreDictionary;

public class ItemOreRadar extends Item {
	public static final int TIMER = 40;
	public static final int RF_PER_T = 400;

	public ItemOreRadar() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_oreradar");
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
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, p_77659_1_);
		}
		p_77659_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, energy);
		double dist = -1;
		double hdist = -1;
		double vdist = -1;
		if (p_77659_2_.isRemote) {
			ArrayList<Block> t = new ArrayList<Block>();
			int nx = MathHelper.floor(p_77659_3_.posX);
			int ny = MathHelper.floor(p_77659_3_.posY);
			int nz = MathHelper.floor(p_77659_3_.posZ);
			for (int x = -3; x < 4; x++)
				for (int y = -3; y < 4; y++)
					for (int z = -3; z < 4; z++) {
						Block b = p_77659_2_.getBlockState(new BlockPos(nx + x, ny + y, nz + z)).getBlock();
						if (b instanceof BlockSilverfish) {
							t.add(b);
							continue;
						}
						ItemStack its = new ItemStack(Item.getItemFromBlock(b), 1);
						if (its.isEmpty())
							continue;
						int[] ids = OreDictionary.getOreIDs(its);
						for (int id : ids) {
							if (OreDictionary.getOreName(id).toLowerCase().startsWith("ore")) {
								t.add(b);
								break;
							}
						}
					}
			if (Math.random() < 0.3)
				t.clear();
			if (t.size() < 1)
				p_77659_3_.sendMessage(new TextComponentTranslation("msg.photoniccraft_oreRadar.nothing"));
			else
				p_77659_3_.sendMessage(new TextComponentTranslation("msg.photoniccraft_oreRadar.found",
						new ItemStack(t.get((int) (Math.random() * t.size())), 1).getDisplayName()));

		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, p_77659_1_);
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
			ArrayList<Block> t = new ArrayList<Block>();
			int nx = MathHelper.floor(p_77648_2_.posX);
			int ny = MathHelper.floor(p_77648_2_.posY);
			int nz = MathHelper.floor(p_77648_2_.posZ);
			for (int x = -3; x < 4; x++)
				for (int y = -3; y < 4; y++)
					for (int z = -3; z < 4; z++) {
						Block b = p_77648_3_.getBlockState(new BlockPos(nx + x, ny + y, nz + z)).getBlock();
						ItemStack its = new ItemStack(Item.getItemFromBlock(b), 1);
						if (its.isEmpty())
							continue;
						int[] ids = OreDictionary.getOreIDs(its);
						for (int id : ids) {
							if (OreDictionary.getOreName(id).toLowerCase().startsWith("ore")) {
								t.add(b);
								break;
							}
						}
					}
			if (Math.random() < 0.3)
				t.clear();
			if (t.size() < 1)
				p_77648_2_.sendMessage(new TextComponentTranslation("msg.photoniccraft_oreRadar.nothing"));
			else
				p_77648_2_.sendMessage(new TextComponentTranslation("msg.photoniccraft_oreRadar.found",
						new ItemStack(t.get((int) (Math.random() * t.size())), 1).getDisplayName()));
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
