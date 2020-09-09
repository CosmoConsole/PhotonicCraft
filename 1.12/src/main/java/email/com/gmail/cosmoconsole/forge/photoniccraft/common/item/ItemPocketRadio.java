package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicRadioPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.CapabilityProviderEnergySerializable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemPocketRadio extends Item {
	public static final int TIMER = 100000;
	public static final int RF_PER_T = 1;

	public static void turnOffRadio(ItemStack i) {
		if (i.isEmpty())
			return;
		if (i.getItem() != PhotonicItems.pocketRadio)
			return;
		if (!i.hasTagCompound())
			return;
		i.getTagCompound().setBoolean("powered", false);
	}

	public ItemPocketRadio() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_pocketradio");
		this.setMaxStackSize(1);
		this.setMaxDamage(TIMER);
		this.setNoRepair();
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add((TIMER * RF_PER_T - this.getEnergy(stack)) + " / " + (TIMER * RF_PER_T) + " RF");
		if (stack.hasTagCompound()) {
			int c = stack.getTagCompound().getInteger("channel");
			tooltip.add(I18n.format("msg.photoniccraft_pocketRadio.channel",
					PhotonicRadio.channelIDToName(c) + " (#" + c + ")"));
			tooltip.add(stack.getTagCompound().getBoolean("powered") ? I18n.format("msg.photoniccraft_pocketRadio.on")
					: I18n.format("msg.photoniccraft_pocketRadio.off"));
		}
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
		return new CapabilityProviderEnergySerializable(stack, TIMER * RF_PER_T, 20);
	}

	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par1ItemStack.setTagCompound(new NBTTagCompound());
		par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, 0);
		par1ItemStack.getTagCompound().setInteger("channel", 0);
		par1ItemStack.getTagCompound().setBoolean("powered", false);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World p_77659_2_, EntityPlayer p_77659_3_, EnumHand hand) {
		ItemStack p_77659_1_ = p_77659_3_.getHeldItem(hand);
		if (p_77659_2_.isRemote)
			return new ActionResult(EnumActionResult.SUCCESS, p_77659_1_);
		if (p_77659_1_.hasTagCompound() == false) {
			p_77659_1_.setTagCompound(new NBTTagCompound());
			p_77659_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, 0);
			p_77659_1_.getTagCompound().setInteger("channel", 0);
			p_77659_1_.getTagCompound().setBoolean("powered", false);
		}
		if (!p_77659_2_.isRemote)
			PhotonicRadio.serverSideInit();
		if (!p_77659_1_.getTagCompound().getBoolean("powered")) {
			if (!radioAllowed(p_77659_3_)) {
				return new ActionResult(EnumActionResult.SUCCESS, p_77659_1_);
			}
		}
		p_77659_1_.getTagCompound().setBoolean("powered", !p_77659_1_.getTagCompound().getBoolean("powered"));
		if (p_77659_1_.getTagCompound().getBoolean("powered")) {
			int c = p_77659_1_.getTagCompound().getInteger("channel");
			p_77659_3_.sendMessage(new TextComponentTranslation("msg.photoniccraft_pocketRadio.switchedOn",
					PhotonicRadio.channelIDToName(c) + " (#" + c + ")"));
		} else {
			p_77659_3_.sendMessage(new TextComponentTranslation("msg.photoniccraft_pocketRadio.switchedOff"));
			PhotonicUtils.sendPacketToPlayer(ModPhotonicCraft.network, p_77659_3_, 
					new PhotonicRadioPacket(-1.0, new byte[PhotonicRadioPacket.PACKET_SIZE],
						0, PhotonicRadioPacket.PACKET_SIZE));
		}
		return new ActionResult(EnumActionResult.SUCCESS, p_77659_1_);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer p_77648_2_, World p_77648_3_, BlockPos pos, EnumHand hand,
			EnumFacing face, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		ItemStack p_77648_1_ = p_77648_2_.getHeldItem(hand);
		if (p_77648_3_.isRemote)
			return EnumActionResult.SUCCESS;
		if (p_77648_1_.hasTagCompound() == false) {
			p_77648_1_.setTagCompound(new NBTTagCompound());
			p_77648_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, 0);
			p_77648_1_.getTagCompound().setInteger("channel", 0);
			p_77648_1_.getTagCompound().setBoolean("powered", false);
		}
		if (!p_77648_3_.isRemote)
			PhotonicRadio.serverSideInit();
		if (!p_77648_1_.getTagCompound().getBoolean("powered")) {
			// some checks
			if (!radioAllowed(p_77648_2_)) {
				return EnumActionResult.SUCCESS;
			}
		}
		p_77648_1_.getTagCompound().setBoolean("powered", !p_77648_1_.getTagCompound().getBoolean("powered"));
		if (p_77648_1_.getTagCompound().getBoolean("powered")) {
			int c = p_77648_1_.getTagCompound().getInteger("channel");
			p_77648_2_.sendMessage(new TextComponentTranslation("msg.photoniccraft_pocketRadio.switchedOn",
					PhotonicRadio.channelIDToName(c) + " (#" + c + ")"));
		} else {
			p_77648_2_.sendMessage(new TextComponentTranslation("msg.photoniccraft_pocketRadio.switchedOff"));
			PhotonicUtils.sendPacketToPlayer(ModPhotonicCraft.network, p_77648_2_,
					new PhotonicRadioPacket(-1.0, new byte[PhotonicRadioPacket.PACKET_SIZE],
						0, PhotonicRadioPacket.PACKET_SIZE));
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if (par3Entity instanceof EntityPlayer) {
			if (!par1ItemStack.hasTagCompound()) {
				par1ItemStack.setTagCompound(new NBTTagCompound());
				par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, 0);
				par1ItemStack.getTagCompound().setInteger("channel", 0);
				par1ItemStack.getTagCompound().setBoolean("powered", false);
			}
			if (!par1ItemStack.getTagCompound().getBoolean("powered")) {
				if (!par2World.isRemote && par3Entity instanceof EntityPlayerMP
						&& (par2World.getTotalWorldTime() % 100L) == 0L
						&& PhotonicUtils.hasNoPoweredOnRadio((EntityPlayerMP) par3Entity)) {
					ModPhotonicCraft.network.sendTo(new PhotonicRadioPacket(-1.0,
							new byte[PhotonicRadioPacket.PACKET_SIZE], 0, PhotonicRadioPacket.PACKET_SIZE),
							(EntityPlayerMP) par3Entity);
				}
				return;
			}
			int energy = par1ItemStack.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
			if (!((EntityPlayer) par3Entity).capabilities.isCreativeMode)
				energy += RF_PER_T;
			if (energy >= (RF_PER_T * TIMER)) {
				par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, RF_PER_T * TIMER);
				return;
			}
			par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, energy);
			if (par2World.isRemote) {
				ModPhotonicCraft.lastReceive = System.currentTimeMillis();
				PhotonicRadio.stillPlaying();
				return;
			} else
				PhotonicRadio.serverSideInit();
			if ((par2World.getTotalWorldTime() % 4L) == 0L)
				if (!PhotonicRadio.allowPlaying((EntityPlayerMP) par3Entity, System.currentTimeMillis(),
						par1ItemStack.getTagCompound().getInteger("channel")))
					par1ItemStack.getTagCompound().setBoolean("powered", false);
		}
	}

	private boolean radioAllowed(EntityPlayer p) {
		if (ModPhotonicCraft.maximumRadio < 1) {
			p.sendMessage(new TextComponentTranslation("msg.photoniccraft_pocketRadio.disabled"));
			return false;
		}
		if (PhotonicRadio.getActiveRadioPlayers() >= ModPhotonicCraft.maximumRadio) {
			p.sendMessage(new TextComponentTranslation("msg.photoniccraft_pocketRadio.overLimit"));
			return false;
		}
		for (ItemStack i : PhotonicUtils.multipleLists(p.inventory.mainInventory, p.inventory.offHandInventory,
				p.inventory.armorInventory)) {
			if (i != null && i.hasTagCompound() && i.getItem() == PhotonicItems.pocketRadio
					&& i.getTagCompound().getBoolean("powered")) {
				p.sendMessage(new TextComponentTranslation("msg.photoniccraft_pocketRadio.onePerPlayer"));
				return false;
			}
		}
		return true;
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
