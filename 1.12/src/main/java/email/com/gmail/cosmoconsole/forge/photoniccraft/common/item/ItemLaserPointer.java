package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserPointer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.server.network.PhotonicServerLaserPointerPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.CapabilityProviderEnergySerializable;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemLaserPointer extends Item {
	public static final int TIMER = 4800;
	public static final int RF_PER_T = 10;

	public static RayTraceResult rayTraceBoth(Entity distance, Entity entity, double d, float f) {
		RayTraceResult m1 = entity.rayTrace(d, f);
		RayTraceResult m2 = ItemLaserPointer.rayTraceEntities(entity, d, f);
		if (m1 == null)
			return m2;
		if (m2 == null)
			return m1;
		if (m1.hitVec == null)
			return m2;
		if (m2.hitVec == null)
			return m1;
		Vec3d pcx = new Vec3d(distance.posX, distance.posY, distance.posZ);
		double db = m1.hitVec.subtract(pcx).lengthVector();
		double de = m2.hitVec.subtract(pcx).lengthVector();
		if (db < de)
			return m1;
		else
			return m2;
	}

	public static RayTraceResult rayTraceEntities(Entity entity2, double distance, float partialTickTime) {
		double d0 = distance;
		Vec3d vec3 = entity2.getPositionVector().addVector(0, entity2.getEyeHeight(), 0);
		Vec3d vec31 = entity2.getLook(partialTickTime);
		Vec3d vec32 = vec3.addVector(vec31.x * d0, vec31.y * d0, vec31.z * d0);
		Entity pointedEntity = null;
		Vec3d vec33 = null;
		float f1 = 1.0F;
		double d1 = distance;
		List<Entity> list = entity2.getEntityWorld().getEntitiesWithinAABBExcludingEntity(entity2,
				new AxisAlignedBB(vec3, vec32).grow(1, 1, 1));
		double d2 = distance;
		RayTraceResult mop = null;
		for (int i = 0; i < list.size(); ++i) {
			if (mop != null)
				break;
			Entity entity = list.get(i);

			if (!entity.noClip && entity.getEntityBoundingBox() != null) {
				float f2 = entity.getCollisionBorderSize();
				AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow(f2, f2, f2);
				RayTraceResult rtr = axisalignedbb.calculateIntercept(vec3, vec32);
				if (axisalignedbb.contains(vec3)) {
					if (0.0D < d2 || d2 == 0.0D) {
						pointedEntity = entity;
						mop = rtr;
						vec33 = rtr == null ? vec3 : rtr.hitVec;
						d2 = 0.0D;
					}
				} else if (rtr != null) {
					double d3 = vec3.distanceTo(rtr.hitVec);

					if (d3 < d2 || d2 == 0.0D) {
						if (entity == entity2.getRidingEntity() && !entity.canRiderInteract()) {
							if (d2 == 0.0D) {
								pointedEntity = entity;
								mop = rtr;
								vec33 = rtr.hitVec;
							}
						} else {
							pointedEntity = entity;
							mop = rtr;
							vec33 = rtr.hitVec;
							d2 = d3;
						}
					}
				}
			}
		}
		if (mop != null) {
			mop.entityHit = pointedEntity;
			mop.typeOfHit = RayTraceResult.Type.ENTITY;
		}
		return mop;
	}

	public ItemLaserPointer() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_laserpointer");
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
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if ((par2World.getTotalWorldTime() & 1) == 0)
			return;
		if (par3Entity instanceof EntityPlayer) {
			if (!((EntityPlayer) par3Entity).getHeldItem(EnumHand.MAIN_HAND).equals(par1ItemStack)
					&& !((EntityPlayer) par3Entity).getHeldItem(EnumHand.OFF_HAND).equals(par1ItemStack))
				return;
			if (((EntityPlayer) par3Entity).isSpectator())
				return;
			if (par1ItemStack.hasTagCompound() == false)
				par1ItemStack.setTagCompound(new NBTTagCompound());
			int energy = par1ItemStack.getTagCompound().getInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY);
			if (!((EntityPlayer) par3Entity).capabilities.isCreativeMode)
				energy += RF_PER_T;
			if (energy >= (RF_PER_T * TIMER)) {
				par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, RF_PER_T * TIMER);
				return;
			}
			par1ItemStack.getTagCompound().setInteger(PhotonicUtils.TAG_ITEM_RF_ENERGY, energy);
			double dist = -1;
			Vec3d didHit = null;
			if (par2World.isRemote) {
				RayTraceResult mop = ItemLaserPointer.rayTraceBoth(par3Entity,
						Minecraft.getMinecraft().getRenderViewEntity(), 80.0, 1.0F);
				if (mop != null) {
					Vec3d pcx = new Vec3d(par3Entity.posX, par3Entity.posY, par3Entity.posZ);
					dist = mop.hitVec.subtract(pcx).lengthVector();
					if (dist < 50) {
						didHit = mop.hitVec;
					}
				}
				if (didHit != null) {
					double x = didHit.x, y = didHit.y, z = didHit.z;
					PhotonicServerLaserPointerPacket pointer = new PhotonicServerLaserPointerPacket(par3Entity.getEntityWorld(), x,
							y, z, mop.entityHit, Minecraft.getMinecraft().player.getUniqueID());
					ModPhotonicCraft.network.sendToServer(pointer);
					EntityLaserPointer e2 = new EntityLaserPointer(par2World);
					e2.setPositionAndRotation(x, y, z, 0.0f, 0.0f);
					e2.setTicks(5);
					par2World.spawnEntity(e2);
					for (Object e : par2World.getEntitiesWithinAABB(EntityOcelot.class,
							new AxisAlignedBB(x - 10, y - 10, z - 10, x + 10, y + 10, z + 10))) {
						EntityOcelot eo = (EntityOcelot) e;
						if (eo.isSitting()) {
							double r = Math.random();
							if (r < 0.05)
								eo.setSitting(false);
							else
								continue;
						}
						eo.getNavigator().tryMoveToXYZ(x, y, z, 1.2);
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
	}
}
