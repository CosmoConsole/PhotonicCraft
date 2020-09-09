package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import io.netty.buffer.ByteBuf;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicAPI;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityRemoteReceiver;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemRemote extends Item implements IEnergyContainerItem {
	public static final int TIMER = 120; 
	public static final int RF_PER_T = 200;
	public ItemRemote() {
        super();
        this.setUnlocalizedName(ModPhotonicCraft.MODID + "_remote");
        this.setTextureName(ModPhotonicCraft.MODID + ":remote");
        this.setMaxStackSize(1);
        this.setMaxDamage(TIMER);
        this.setNoRepair();
		this.setCreativeTab(CreativeTabs.tabTools);
	}
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	};
	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par1ItemStack.stackTagCompound == null) {
			par1ItemStack.stackTagCompound = new NBTTagCompound();
			par1ItemStack.stackTagCompound.setLong("channel", Math.abs(PhotonicAPI.rand.nextLong()));
		}
	}
	@Override
	public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_,
			boolean p_77663_5_) {
		if (p_77663_1_.stackTagCompound == null) {
			p_77663_1_.stackTagCompound = new NBTTagCompound();
			p_77663_1_.stackTagCompound.setLong("channel", Math.abs(PhotonicAPI.rand.nextLong()));
		}
	}
	@Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
		if (stack.stackTagCompound == null) return 0;
        return (double)(this.getMaxEnergyStored(stack) - this.getEnergyStored(stack)) / (double)(this.getMaxEnergyStored(stack));
    }
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if (!(entityLiving instanceof EntityPlayer)) return false;
		EntityPlayer p_77659_3_ = (EntityPlayer) entityLiving;
		ItemStack p_77659_1_ = stack;
		World p_77659_2_ = entityLiving.worldObj;
		if (p_77659_3_.isSneaking()) return false;
		if (p_77659_1_.stackTagCompound == null) {
			p_77659_1_.stackTagCompound = new NBTTagCompound();
			p_77659_1_.stackTagCompound.setLong("channel", Math.abs(PhotonicAPI.rand.nextLong()));
		}
		int energy = p_77659_1_.stackTagCompound.getInteger("energy");
		energy += RF_PER_T;
		//par1ItemStack.damageItem(1, (EntityLivingBase) par3Entity);
		if (energy >= (RF_PER_T * TIMER)) {
			//((EntityPlayer) par3Entity).inventory.setInventorySlotContents(((EntityPlayer) par3Entity).inventory.currentItem, null);
			p_77659_1_.stackTagCompound.setInteger("energy", RF_PER_T * TIMER);
			return false;
		}
		p_77659_1_.stackTagCompound.setInteger("energy", energy);
		double dist = -1;
		double hdist = -1;
		double vdist = -1;
		if (p_77659_2_.isRemote) {
			MovingObjectPosition mop = ItemLaserPointer.rayTraceBoth(p_77659_3_, Minecraft.getMinecraft().renderViewEntity, 50, 1.0F);
			if (mop != null) {
				if (mop.typeOfHit == MovingObjectType.BLOCK) {
					Block b = p_77659_2_.getBlock(mop.blockX, mop.blockY, mop.blockZ);
					if (b == ModPhotonicCraft.remoteReceiver) {
	    				ByteBuf data = io.netty.buffer.Unpooled.buffer(36);
	    				data.writeInt(2);
	    				data.writeInt(p_77659_3_.worldObj.provider.dimensionId);
	    		        data.writeInt(mop.blockX);
	    		        data.writeInt(mop.blockY);
	    		        data.writeInt(mop.blockZ);
	    				data.writeInt(1);
	    		        data.writeInt(p_77659_3_.getEntityId());
	    		        data.writeLong(p_77659_3_.worldObj.getTotalWorldTime());
	    		        C17PacketCustomPayload packet = new C17PacketCustomPayload("PhotonicCraft", data);
	    		        EntityClientPlayerMP player = (EntityClientPlayerMP)p_77659_3_;
	    		        player.sendQueue.addToSendQueue(packet);
					}
				}
			}
		}
		return true;
	}
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		if (p_77659_3_.isSneaking()) {
			return p_77659_1_;
		}
		if (p_77659_1_.stackTagCompound == null)
			p_77659_1_.stackTagCompound = new NBTTagCompound();
		int energy = p_77659_1_.stackTagCompound.getInteger("energy");
		energy += RF_PER_T;
		//par1ItemStack.damageItem(1, (EntityLivingBase) par3Entity);
		if (energy >= (RF_PER_T * TIMER)) {
			//((EntityPlayer) par3Entity).inventory.setInventorySlotContents(((EntityPlayer) par3Entity).inventory.currentItem, null);
			p_77659_1_.stackTagCompound.setInteger("energy", RF_PER_T * TIMER);
			return p_77659_1_;
		}
		p_77659_1_.stackTagCompound.setInteger("energy", energy);
		double dist = -1;
		double hdist = -1;
		double vdist = -1;
		if (p_77659_2_.isRemote) {
			MovingObjectPosition mop = ItemLaserPointer.rayTraceBoth(p_77659_3_, Minecraft.getMinecraft().renderViewEntity, 50, 1.0F);
			if (mop != null) {
				if (mop.typeOfHit == MovingObjectType.BLOCK) {
					Block b = p_77659_2_.getBlock(mop.blockX, mop.blockY, mop.blockZ);
					if (b == ModPhotonicCraft.remoteReceiver) {
	    				ByteBuf data = io.netty.buffer.Unpooled.buffer(36);
	    				data.writeInt(2);
	    				data.writeInt(p_77659_3_.worldObj.provider.dimensionId);
	    		        data.writeInt(mop.blockX);
	    		        data.writeInt(mop.blockY);
	    		        data.writeInt(mop.blockZ);
	    				data.writeInt(2);
	    		        data.writeInt(p_77659_3_.getEntityId());
	    		        data.writeLong(p_77659_3_.worldObj.getTotalWorldTime());
	    		        C17PacketCustomPayload packet = new C17PacketCustomPayload("PhotonicCraft", data);
	    		        EntityClientPlayerMP player = (EntityClientPlayerMP)p_77659_3_;
	    		        player.sendQueue.addToSendQueue(packet);
					}
				}
			}
		}
		return p_77659_1_;
	}
	private boolean sneak(ItemStack is, EntityPlayer p_149727_5_, World w, int x, int y, int z) {
		System.out.println(x + "," + y + "," + z);
		if ((((TileEntityRemoteReceiver) w.getTileEntity(x, y, z))) == null) {
			System.out.println("block has no tile entity");
			return false;
		}
		TileEntityRemoteReceiver te = ((TileEntityRemoteReceiver) w.getTileEntity(x, y, z));
    	if ((w.getTotalWorldTime() - te.lastclick) < 10) return true;
    	te.lastclick = w.getTotalWorldTime();
		if (te.channel < 0) {
			p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_remotereceiver.nochannel"));
		} else {
			p_149727_5_.getHeldItem().stackTagCompound.setLong("channel", te.channel);
			p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_remote.channelcopied",Long.toString(te.channel)));
		}
		return true;
	}
	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if (p_77648_2_.isSneaking()) {
			return sneak(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_);
		}
		if (p_77648_1_.stackTagCompound == null)
			p_77648_1_.stackTagCompound = new NBTTagCompound();
		int energy = p_77648_1_.stackTagCompound.getInteger("energy");
		if (!p_77648_2_.capabilities.isCreativeMode) energy += RF_PER_T;
		//par1ItemStack.damageItem(1, (EntityLivingBase) par3Entity);
		if (energy >= (RF_PER_T * TIMER)) {
			//((EntityPlayer) par3Entity).inventory.setInventorySlotContents(((EntityPlayer) par3Entity).inventory.currentItem, null);
			p_77648_1_.stackTagCompound.setInteger("energy", RF_PER_T * TIMER);
			return false;
		}
		p_77648_1_.stackTagCompound.setInteger("energy", energy);
		if (p_77648_3_.isRemote) {
			MovingObjectPosition mop = ItemLaserPointer.rayTraceBoth(p_77648_2_, Minecraft.getMinecraft().renderViewEntity, 50, 1.0F);
			if (mop != null) {
				if (mop.typeOfHit == MovingObjectType.BLOCK) {
					Block b = p_77648_3_.getBlock(mop.blockX, mop.blockY, mop.blockZ);
					if (b == ModPhotonicCraft.remoteReceiver) {
	    				ByteBuf data = io.netty.buffer.Unpooled.buffer(36);
	    				data.writeInt(2);
	    				data.writeInt(p_77648_2_.worldObj.provider.dimensionId);
	    		        data.writeInt(mop.blockX);
	    		        data.writeInt(mop.blockY);
	    		        data.writeInt(mop.blockZ);
	    				data.writeInt(2);
	    		        data.writeInt(p_77648_2_.getEntityId());
	    		        data.writeLong(p_77648_2_.worldObj.getTotalWorldTime());
	    		        C17PacketCustomPayload packet = new C17PacketCustomPayload("PhotonicCraft", data);
	    		        EntityClientPlayerMP player = (EntityClientPlayerMP)p_77648_2_;
	    		        player.sendQueue.addToSendQueue(packet);
					}
				}
			}
			/*p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.distance",(dist < 0 ? "--" : String.format("%.2f", dist) + " m")));
			p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.hdistance",(dist < 0 ? "--" : String.format("%.2f", hdist) + " m")));
			p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.vdistance",(dist < 0 ? "--" : String.format("%.2f", vdist) + " m")));
			p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.angle",(dist < 0 ? "--" : String.format("%.2f", Math.toDegrees(Math.atan2(vdist, hdist))) + "\u00B0")));*/
		}
		return true;
	}
	/*@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
		if (par2World.isRemote) return;
    	if (par3Entity instanceof EntityPlayer) {
    		if (((EntityPlayer)par3Entity).getHeldItem() == null)
    			return;
    		if (!((EntityPlayer)par3Entity).getHeldItem().equals(par1ItemStack))
    			return;
    		if (par1ItemStack.stackTagCompound == null)
    			par1ItemStack.stackTagCompound = new NBTTagCompound();
    		int energy = par1ItemStack.stackTagCompound.getInteger("energy");
    		energy += RF_PER_T;
    		//par1ItemStack.damageItem(1, (EntityLivingBase) par3Entity);
    		if (energy >= (RF_PER_T * TIMER)) {
    			//((EntityPlayer) par3Entity).inventory.setInventorySlotContents(((EntityPlayer) par3Entity).inventory.currentItem, null);
        		par1ItemStack.stackTagCompound.setInteger("energy", RF_PER_T * TIMER);
    			return;
    		}
    		par1ItemStack.stackTagCompound.setInteger("energy", energy);
    		int x = MathHelper.floor(par3Entity.posX);
    		int y = MathHelper.floor(par3Entity.posY);
    		int z = MathHelper.floor(par3Entity.posZ);
    		for (int xo = -1; xo < 2; xo++)
    			for (int yo = -1; yo < 2; yo++)
    				for (int zo = -1; zo < 2; zo++) {
    					int ax = x + xo, ay = y + yo, az = z + zo;
    					if (par2World.getBlock(ax, ay, az) == Blocks.air || par2World.getBlock(ax, ay, az) == ModPhotonicCraft.lightAir) {
    						par2World.setBlock(ax, ay, az, ModPhotonicCraft.lightAir, 0, 3);
    						//par2World.scheduleBlockUpdate(ax, ay, az, ModPhotonicCraft.lightAir, TileEntityLightAir.DEF_TICKS);
    					}
    				}
    	}
    }*/
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		list.add(this.getEnergyStored(stack) + " / " + this.getMaxEnergyStored(stack) + " RF");
		if (stack.stackTagCompound != null)
			list.add(I18n.format("msg.photoniccraft_remote.channel",Long.toString(stack.stackTagCompound.getLong("channel"))));
	}
	@Override
	public int extractEnergy(ItemStack arg0, int arg1, boolean arg2) {
		return 0;
	}
	@Override
	public int getEnergyStored(ItemStack arg0) {
		if (arg0 == null) return 0;
		if (arg0.stackTagCompound == null) return 0;
		return RF_PER_T * TIMER - arg0.stackTagCompound.getInteger("energy");
	}
	@Override
	public int getMaxEnergyStored(ItemStack arg0) {
		return RF_PER_T * TIMER;
	}
	@Override
	public int receiveEnergy(ItemStack arg0, int arg1, boolean arg2) {
		if (arg0.stackTagCompound == null) return 0;
		if (arg2) {
			return Math.min(arg1, this.getMaxEnergyStored(arg0) - this.getEnergyStored(arg0));
		}
		int energy = arg0.stackTagCompound.getInteger("energy");
		int old_energy = energy;
		energy -= arg1;
		if (energy < 0)
			energy = 0;
		arg0.stackTagCompound.setInteger("energy", energy);
		return old_energy - energy;
	};
}
