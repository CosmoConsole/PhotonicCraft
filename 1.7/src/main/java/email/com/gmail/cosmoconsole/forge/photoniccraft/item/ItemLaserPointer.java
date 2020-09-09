package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import java.util.List;

import com.jcraft.jorbis.Block;
import com.sun.javafx.geom.Vec3d;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.MathHelper;
import io.netty.buffer.ByteBuf;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserPointer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLightAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemLaserPointer extends Item implements IEnergyContainerItem {
	public static final int TIMER = 4800; 
	public static final int RF_PER_T = 5;
	public ItemLaserPointer() {
        super();
        this.setUnlocalizedName(ModPhotonicCraft.MODID + "_laserPointer");
        this.setTextureName(ModPhotonicCraft.MODID + ":laserpointer");
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
		par1ItemStack.stackTagCompound = new NBTTagCompound();
		par1ItemStack.stackTagCompound.setInteger("energy", 0);
	}
	@Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
		if (stack.stackTagCompound == null) return 0;
        return (double)(this.getMaxEnergyStored(stack) - this.getEnergyStored(stack)) / (double)(this.getMaxEnergyStored(stack));
    }
	
	public static MovingObjectPosition rayTraceEntities(EntityLivingBase renderViewEntity, double distance, float partialTickTime) {
		//EntityRenderer.getMouseOver(1.0f);
        double d0 = distance;
        Vec3 vec3 = renderViewEntity.getPosition(partialTickTime);
        Vec3 vec31 = renderViewEntity.getLook(partialTickTime);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
		Entity pointedEntity = null;
        Vec3 vec33 = null;
        float f1 = 1.0F;
        double d1 = distance;
        List list = renderViewEntity.worldObj.getEntitiesWithinAABBExcludingEntity(renderViewEntity, renderViewEntity.boundingBox.addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f1, (double)f1, (double)f1));
        double d2 = distance;
        MovingObjectPosition mop = null;
        for (int i = 0; i < list.size(); ++i)
        {
        	if (mop != null) break;
            Entity entity = (Entity)list.get(i);

            if (entity.canBeCollidedWith())
            {
                float f2 = entity.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f2, (double)f2, (double)f2);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3))
                {
                    if (0.0D < d2 || d2 == 0.0D)
                    {
                        pointedEntity = entity;
                        mop = movingobjectposition;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                }
                else if (movingobjectposition != null)
                {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D)
                    {
                        if (entity == renderViewEntity.ridingEntity && !entity.canRiderInteract())
                        {
                            if (d2 == 0.0D)
                            {
                                pointedEntity = entity;
                                mop = movingobjectposition;
                                vec33 = movingobjectposition.hitVec;
                            }
                        }
                        else
                        {
                            pointedEntity = entity;
                            mop = movingobjectposition;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }
        }
        if (mop != null) {
        	mop.entityHit = pointedEntity;
        	mop.typeOfHit = MovingObjectType.ENTITY;
        }
        return mop;
	}
	public static MovingObjectPosition rayTraceBoth(Entity distance, EntityLivingBase renderViewEntity, double d, float f) {
		MovingObjectPosition m1 = renderViewEntity.rayTrace(d, f);
		MovingObjectPosition m2 = ItemLaserPointer.rayTraceEntities(renderViewEntity, d, f);
		if (m1 == null)
			return m2;
		if (m2 == null)
			return m1;
		if (m1.hitVec == null)
			return m2;
		if (m2.hitVec == null)
			return m1;
		Vec3 pcx = Vec3.createVectorHelper(distance.posX, distance.posY, distance.posZ);
		double db = m1.hitVec.subtract(pcx).lengthVector();
		double de = m2.hitVec.subtract(pcx).lengthVector();
		if (db < de)
			return m1;
		else
			return m2;
	}
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
		if ((par2World.getTotalWorldTime()&1)==0) return;
    	if (par3Entity instanceof EntityPlayer) {
    		if (((EntityPlayer)par3Entity).getHeldItem() == null)
    			return;
    		if (!((EntityPlayer)par3Entity).getHeldItem().equals(par1ItemStack))
    			return;
    		if (par1ItemStack.stackTagCompound == null)
    			par1ItemStack.stackTagCompound = new NBTTagCompound();
    		int energy = par1ItemStack.stackTagCompound.getInteger("energy");
    		if (!((EntityPlayer)par3Entity).capabilities.isCreativeMode) energy += RF_PER_T;
    		//par1ItemStack.damageItem(1, (EntityLivingBase) par3Entity);
    		if (energy >= (RF_PER_T * TIMER)) {
    			//((EntityPlayer) par3Entity).inventory.setInventorySlotContents(((EntityPlayer) par3Entity).inventory.currentItem, null);
        		par1ItemStack.stackTagCompound.setInteger("energy", RF_PER_T * TIMER);
    			return;
    		}
    		par1ItemStack.stackTagCompound.setInteger("energy", energy);
    		double dist = -1;
			Vec3 didHit = null;
    		if (par2World.isRemote) {
    			MovingObjectPosition mop = ItemLaserPointer.rayTraceBoth(par3Entity, Minecraft.getMinecraft().renderViewEntity, 80.0, 1.0F);
    			if (mop != null) {
    				Vec3 pcx = Vec3.createVectorHelper(par3Entity.posX, par3Entity.posY, par3Entity.posZ);
    				dist = mop.hitVec.subtract(pcx).lengthVector();
    				if (dist < 50) {
    					didHit = mop.hitVec;
    				}
    			}
    			if (didHit != null) {
    				ByteBuf data = io.netty.buffer.Unpooled.buffer(36);
    				data.writeInt(1);
    				data.writeInt(par3Entity.worldObj.provider.dimensionId);
    		        data.writeDouble(didHit.xCoord);
    		        data.writeDouble(didHit.yCoord);
    		        data.writeDouble(didHit.zCoord);
    		        data.writeInt(mop.entityHit == null ? -1 : mop.entityHit.getEntityId());
    		        C17PacketCustomPayload packet = new C17PacketCustomPayload("PhotonicCraft", data);
    		        EntityClientPlayerMP player = (EntityClientPlayerMP)par3Entity;
    		        player.sendQueue.addToSendQueue(packet);
    			}
    		}
    		/*
    		        EntityLaserPointer e2 = new EntityLaserPointer(par2World);
    				e2.setPositionAndRotation(par1ItemStack.stackTagCompound.getDouble("posX"), par1ItemStack.stackTagCompound.getDouble("posY"), par1ItemStack.stackTagCompound.getDouble("posZ"), 0, 0);
    				e2.setTicks(5);
    				par2World.spawnEntityInWorld(e2);
    		 */
    	}
    }
	/*@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
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
			MovingObjectPosition mop = Minecraft.getMinecraft().renderViewEntity.rayTrace(200, 1.0F);
			if (mop != null) {
				Vec3 pcx = Vec3.createVectorHelper(p_77659_3_.posX, p_77659_3_.posY, p_77659_3_.posZ);
				dist = mop.hitVec.subtract(pcx).lengthVector();
				if (dist >= 180) dist = -1;
				else {
					vdist = mop.hitVec.yCoord - pcx.yCoord;
					hdist = Math.sqrt((dist * dist) - (vdist * vdist));
				}
			}
			p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.distance",(dist < 0 ? "--" : String.format("%.2f", dist) + " m")));
			p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.hdistance",(dist < 0 ? "--" : String.format("%.2f", hdist) + " m")));
			p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.vdistance",(dist < 0 ? "--" : String.format("%.2f", vdist) + " m")));
			p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.angle",(dist < 0 ? "--" : String.format("%.2f", Math.toDegrees(Math.atan2(vdist, hdist))) + "\u00B0")));
		}
		return p_77659_1_;
	}
	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {

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
		double dist = -1;
		double hdist = -1;
		double vdist = -1;
		if (p_77648_3_.isRemote) {
			MovingObjectPosition mop = Minecraft.getMinecraft().renderViewEntity.rayTrace(200, 1.0F);
			if (mop != null) {
				Vec3 pcx = Vec3.createVectorHelper(p_77648_2_.posX, p_77648_2_.posY, p_77648_2_.posZ);
				dist = mop.hitVec.subtract(pcx).lengthVector();
				if (dist >= 180) dist = -1;
				else {
					vdist = mop.hitVec.yCoord - pcx.yCoord;
					hdist = Math.sqrt((dist * dist) - (vdist * vdist));
				}
			}
			p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.distance",(dist < 0 ? "--" : String.format("%.2f", dist) + " m")));
			p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.hdistance",(dist < 0 ? "--" : String.format("%.2f", hdist) + " m")));
			p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.vdistance",(dist < 0 ? "--" : String.format("%.2f", vdist) + " m")));
			p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.angle",(dist < 0 ? "--" : String.format("%.2f", Math.toDegrees(Math.atan2(vdist, hdist))) + "\u00B0")));
		}
		return true;
	}*/
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
