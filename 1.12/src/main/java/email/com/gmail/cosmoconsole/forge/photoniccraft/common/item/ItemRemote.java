package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityRemoteReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.CapabilityProviderEnergySerializable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
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

public class ItemRemote extends Item {
	public static final int TIMER = 240;
	public static final int RF_PER_T = 200;

	public ItemRemote() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_remote");
		this.setMaxStackSize(1);
		this.setMaxDamage(TIMER);
		this.setNoRepair();
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add((TIMER * RF_PER_T - this.getEnergy(stack)) + " / " + (TIMER * RF_PER_T) + " RF");
		if (stack.hasTagCompound())
			tooltip.add(I18n.format("msg.photoniccraft_remote.channel",
					Long.toString(stack.getTagCompound().getLong("channel"))));
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
	};

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
		if (par1ItemStack.hasTagCompound() == false) {
			par1ItemStack.setTagCompound(new NBTTagCompound());
			par1ItemStack.getTagCompound().setLong("channel", Math.abs(PhotonicUtils.rand.nextLong()));
		}
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if (!(entityLiving instanceof EntityPlayer))
			return false;
		EntityPlayer p_77659_3_ = (EntityPlayer) entityLiving;
		ItemStack p_77659_1_ = stack;
		World p_77659_2_ = entityLiving.getEntityWorld();
		if (p_77659_3_.isSneaking())
			return false;
		if (p_77659_1_.hasTagCompound() == false) {
			p_77659_1_.setTagCompound(new NBTTagCompound());
			p_77659_1_.getTagCompound().setLong("channel", Math.abs(PhotonicUtils.rand.nextLong()));
		}
		int energy = p_77659_1_.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
		if (!p_77659_3_.capabilities.isCreativeMode)
			energy += RF_PER_T;
		if (energy >= (RF_PER_T * TIMER)) {
			p_77659_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, RF_PER_T * TIMER);
			return false;
		}
		p_77659_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, energy);
		double dist = -1;
		double hdist = -1;
		double vdist = -1;
		if (p_77659_2_.isRemote) {
			RayTraceResult mop = ItemLaserPointer.rayTraceBoth(p_77659_3_,
					Minecraft.getMinecraft().getRenderViewEntity(), 50, 1.0F);
			if (mop != null) {
				if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
					Block b = p_77659_2_.getBlockState(
							new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ()))
							.getBlock();
					if (b == PhotonicBlocks.remoteReceiver) {
						ByteBuf data = io.netty.buffer.Unpooled.buffer(36);
						data.writeInt(2);
						data.writeInt(p_77659_3_.getEntityWorld().provider.getDimension());
						data.writeInt(mop.getBlockPos().getX());
						data.writeInt(mop.getBlockPos().getY());
						data.writeInt(mop.getBlockPos().getZ());
						data.writeInt(1);
						data.writeInt(p_77659_3_.getEntityId());
						data.writeLong(p_77659_3_.getEntityWorld().getTotalWorldTime());
						PacketBuffer pdata = new PacketBuffer(data);
						CPacketCustomPayload packet = new CPacketCustomPayload("PhotonicCraft", pdata);
						if (p_77659_3_ instanceof EntityPlayerMP) {
							EntityPlayerMP player = (EntityPlayerMP) p_77659_3_;
							player.connection.sendPacket(packet);
						} else if (p_77659_3_ instanceof EntityPlayerSP) {
							EntityPlayerSP player = (EntityPlayerSP) p_77659_3_;
							player.connection.sendPacket(packet);
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World p_77659_2_, EntityPlayer p_77659_3_, EnumHand hand) {
		ItemStack p_77659_1_ = p_77659_3_.getHeldItem(hand);
		if (p_77659_3_.isSneaking()) {
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, p_77659_1_);
		}
		if (p_77659_1_.hasTagCompound() == false)
			p_77659_1_.setTagCompound(new NBTTagCompound());
		int energy = p_77659_1_.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
		if (!p_77659_3_.capabilities.isCreativeMode)
			energy += RF_PER_T;
		// par1ItemStack.damageItem(1, (EntityLivingBase) par3Entity);
		if (energy >= (RF_PER_T * TIMER)) {
			// ((EntityPlayer)
			// par3Entity).inventory.setInventorySlotContents(((EntityPlayer)
			// par3Entity).inventory.currentItem, null);
			p_77659_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, RF_PER_T * TIMER);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, p_77659_1_);
		}
		p_77659_1_.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, energy);
		/*if (p_77659_2_.isRemote) {
			RayTraceResult mop = ItemLaserPointer.rayTraceBoth(p_77659_3_,
					Minecraft.getMinecraft().getRenderViewEntity(), 50, 1.0F);
			if (mop != null) {
				if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
					Block b = p_77659_2_.getBlockState(
							new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ()))
							.getBlock();
					if (b == PhotonicBlocks.remoteReceiver) {
						ByteBuf data = io.netty.buffer.Unpooled.buffer(36);
						data.writeInt(2);
						data.writeInt(p_77659_3_.getEntityWorld().provider.getDimension());
						data.writeInt(mop.getBlockPos().getX());
						data.writeInt(mop.getBlockPos().getY());
						data.writeInt(mop.getBlockPos().getZ());
						data.writeInt(2);
						data.writeInt(p_77659_3_.getEntityId());
						data.writeLong(p_77659_3_.getEntityWorld().getTotalWorldTime());
						PacketBuffer pdata = new PacketBuffer(data);
						CPacketCustomPayload packet = new CPacketCustomPayload("PhotonicCraft", pdata);
						if (p_77659_3_ instanceof EntityPlayerMP) {
							EntityPlayerMP player = (EntityPlayerMP) p_77659_3_;
							player.connection.sendPacket(packet);
						} else if (p_77659_3_ instanceof EntityPlayerSP) {
							EntityPlayerSP player = (EntityPlayerSP) p_77659_3_;
							player.connection.sendPacket(packet);
						}
					}
				}
			}
		}*/
		if (p_77659_2_.isRemote)
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, p_77659_1_);
		Vec3d lookVector = p_77659_3_.getLookVec();
		double yaw = Math.toRadians(p_77659_3_.rotationYaw);
		double pitch = Math.toRadians(p_77659_3_.rotationPitch);
		BlockPos playerPos = new BlockPos(p_77659_3_.getPositionVector().addVector(0, p_77659_3_.getEyeHeight(), 0));
		BlockPos cubeCenter = new BlockPos(p_77659_3_.getPositionVector().addVector(0, p_77659_3_.getEyeHeight(), 0).add(lookVector.scale(RANGE)));
		long channel = p_77659_1_.getTagCompound().getLong("channel");
		for (int x = -RANGE; x <= RANGE; ++x) {
			for (int y = -RANGE; y <= RANGE; ++y) {
				for (int z = -RANGE; z <= RANGE; ++z) {
					BlockPos tpos = cubeCenter.add(x, y, z);
					if (p_77659_2_.getBlockState(tpos).getBlock() == PhotonicBlocks.remoteReceiver) {
						TileEntityRemoteReceiver terr = (TileEntityRemoteReceiver) p_77659_2_.getTileEntity(tpos);
						if (terr.channel == channel) {
							// check horizontal and vertical angle
							double xo = tpos.getX() + 0.5 - p_77659_3_.posX;
							double yo = tpos.getY() + 0.5 - p_77659_3_.posY;
							double zo = tpos.getZ() + 0.5 - p_77659_3_.posZ;
							double ho = Math.sqrt(xo * xo + zo * zo);
							double ha = Math.atan2(-xo, zo);
							double va = Math.atan2(-yo, ho);
							if (PhotonicUtils.angleDifference(ha, yaw) < PhotonicUtils.DEG_45 &&
									PhotonicUtils.angleDifference(va, pitch) < PhotonicUtils.DEG_45) {
								if (isClearLine(p_77659_2_, playerPos, tpos)) {
									terr.submitPower(15, p_77659_2_.getTotalWorldTime());
								}
							}
						}
					}
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, p_77659_1_);
	}

	private static final int RANGE = 9; 
	@Override
	public EnumActionResult onItemUse(EntityPlayer p_77648_2_, World p_77648_3_, BlockPos pos, EnumHand hand,
			EnumFacing face, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		ItemStack p_77648_1_ = p_77648_2_.getHeldItem(hand);
		if (p_77648_2_.isSneaking() && hand == EnumHand.MAIN_HAND) {
			return sneak(p_77648_1_, p_77648_2_, p_77648_3_, pos.getX(), pos.getY(), pos.getZ());
		}
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
		/*if (p_77648_3_.isRemote) {
			RayTraceResult mop = ItemLaserPointer.rayTraceBoth(p_77648_2_,
					Minecraft.getMinecraft().getRenderViewEntity(), 50, 1.0F);
			if (mop != null) {
				if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
					Block b = p_77648_3_.getBlockState(
							new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ()))
							.getBlock();
					if (b == PhotonicBlocks.remoteReceiver) {
						PhotonicRemotePacket packet = new PhotonicRemotePacket(p_77648_3_, mop.getBlockPos().getX(),
								mop.getBlockPos().getY(), mop.getBlockPos().getZ(), p_77648_2_, 2,
								p_77648_3_.getTotalWorldTime());
						ModPhotonicCraft.network.sendToServer(packet);
					}
				}
			}
		}*/
		if (p_77648_3_.isRemote)
			return EnumActionResult.SUCCESS;
		Vec3d lookVector = p_77648_2_.getLookVec();
		double yaw = Math.toRadians(p_77648_2_.rotationYaw);
		double pitch = Math.toRadians(p_77648_2_.rotationPitch);
		BlockPos playerPos = new BlockPos(p_77648_2_.getPositionVector().addVector(0, p_77648_2_.getEyeHeight(), 0));
		BlockPos cubeCenter = new BlockPos(p_77648_2_.getPositionVector().addVector(0, p_77648_2_.getEyeHeight(), 0).add(lookVector.scale(RANGE)));
		long channel = p_77648_1_.getTagCompound().getLong("channel");
		for (int x = -RANGE; x <= RANGE; ++x) {
			for (int y = -RANGE; y <= RANGE; ++y) {
				for (int z = -RANGE; z <= RANGE; ++z) {
					BlockPos tpos = cubeCenter.add(x, y, z);
					if (p_77648_3_.getBlockState(tpos).getBlock() == PhotonicBlocks.remoteReceiver) {
						TileEntityRemoteReceiver terr = (TileEntityRemoteReceiver) p_77648_3_.getTileEntity(tpos);
						if (terr.channel == channel) {
							// check horizontal and vertical angle
							double xo = tpos.getX() + 0.5 - p_77648_2_.posX;
							double yo = tpos.getY() + 0.5 - p_77648_2_.posY;
							double zo = tpos.getZ() + 0.5 - p_77648_2_.posZ;
							double ho = Math.sqrt(xo * xo + zo * zo);
							double ha = Math.atan2(-xo, zo);
							double va = Math.atan2(-yo, ho);
							if (PhotonicUtils.angleDifference(ha, yaw) < PhotonicUtils.DEG_45 &&
									PhotonicUtils.angleDifference(va, pitch) < PhotonicUtils.DEG_45) {
								if (isClearLine(p_77648_3_, playerPos, tpos)) {
									terr.submitPower(15, p_77648_3_.getTotalWorldTime());
								}
							}
						}
					}
				}
			}
		}
		return EnumActionResult.SUCCESS;
	}

	private static final int STRENGTH = 40;
	private boolean isClearLine(World w, BlockPos c, BlockPos d) {
		int a = STRENGTH;
		Vec3d start = new Vec3d(c).addVector(0.5, 0.5, 0.5);
		Vec3d diff = new Vec3d(d).addVector(0.5, 0.5, 0.5).subtract(start);
		Vec3d norm = diff.normalize();
		int blocks = (int) Math.floor(diff.lengthVector());
		BlockPos last = null;
		for (int i = 0; i < blocks; ++i) {
			BlockPos pos = new BlockPos(start);
			start = start.add(norm);
			if (pos.equals(d))
				break;
			if (pos.equals(last)) 
				continue;
			a -= Math.min(PhotonicUtils.MAX_LIGHT_LEVEL, w.getBlockLightOpacity(pos));
			last = pos;
			if (a <= 0) {
				return false;
			}
		}
		return PhotonicUtils.rand.nextDouble() < ((double) a / STRENGTH);
	}

	@Override
	public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_,
			boolean p_77663_5_) {
		if (p_77663_1_.hasTagCompound() == false) {
			p_77663_1_.setTagCompound(new NBTTagCompound());
			p_77663_1_.getTagCompound().setLong("channel", Math.abs(PhotonicUtils.rand.nextLong()));
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

	private EnumActionResult sneak(ItemStack is, EntityPlayer p_149727_5_, World w, int x, int y, int z) {
		if ((((TileEntityRemoteReceiver) w.getTileEntity(new BlockPos(x, y, z)))) == null) {
			return EnumActionResult.FAIL;
		}
		TileEntityRemoteReceiver te = ((TileEntityRemoteReceiver) w.getTileEntity(new BlockPos(x, y, z)));
		if ((w.getTotalWorldTime() - te.lastclick) < 10)
			return EnumActionResult.SUCCESS;
		te.lastclick = w.getTotalWorldTime();
		if (te.channel < 0) {
			if (!w.isRemote) 
				p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_remotereceiver.nochannel"));
		} else {
			p_149727_5_.getHeldItem(EnumHand.MAIN_HAND).getTagCompound().setLong("channel", te.channel);
			if (!w.isRemote) 
				p_149727_5_.sendMessage(
					new TextComponentTranslation("msg.photoniccraft_remote.channelcopied", Long.toString(te.channel)));
		}
		return EnumActionResult.SUCCESS;
	}
}
