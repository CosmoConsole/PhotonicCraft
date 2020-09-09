package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import java.util.ArrayList;
import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicReflectionNames;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ItemHypnoBomb extends Item {
	private int[] colors = new int[] { 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0x00FFFF, 0xFF00FF, 0xFFFFFF };

	public ItemHypnoBomb() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_hypnobomb");
		this.setMaxStackSize(4);
		this.setNoRepair();
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World p_77659_2_, EntityPlayer p_77659_3_, EnumHand hand) {
		if (!p_77659_3_.capabilities.isCreativeMode)
			p_77659_3_.inventory.decrStackSize(p_77659_3_.inventory.currentItem, 1);
		double x = p_77659_3_.posX;
		double y = p_77659_3_.posY;
		double z = p_77659_3_.posZ;
		if (p_77659_2_.isRemote)
			p_77659_2_.playSound(x, y + 1.5, z,
					SoundEvent.REGISTRY.getObject(new ResourceLocation("minecraft:entity.generic.explode")),
					SoundCategory.PLAYERS, 1.5F, 1.0F, true);
		if (!p_77659_2_.isRemote) {
			p_77659_3_.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 20, 0, true, false));
			p_77659_2_.playSound(x, y + 1.5, z,
					SoundEvent.REGISTRY.getObject(new ResourceLocation("minecraft:entity.generic.explode")),
					SoundCategory.PLAYERS, 1.5F, 1.0F, true);
			for (EntityPigZombie eo : p_77659_2_.getEntitiesWithinAABB(EntityPigZombie.class,
					new AxisAlignedBB(x - 256, y - 256, z - 256, x + 256, y + 256, z + 256))) {
				if (eo.getAttackTarget() instanceof EntityPlayer || eo.getRevengeTarget() instanceof EntityPlayer) {
					eo.attackEntityAsMob(eo);
					eo.getCombatTracker().reset();
					Vec3d opposite = eo.getPositionVector().add(eo.getPositionVector().subtract(p_77659_3_.getPositionVector()).scale(64));
					double safey = eo.world.getTopSolidOrLiquidBlock(new BlockPos(opposite)).getY();
					eo.getNavigator().tryMoveToXYZ(opposite.x, safey + 1, opposite.z, eo.getAIMoveSpeed());
					eo.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1200, 39));
					eo.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 1200, 39));
					eo.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1200, 39));
				}
				eo.setRevengeTarget(eo);
				eo.setAttackTarget(eo);
				eo.setRevengeTarget(null);
				eo.setAttackTarget(null);
				ObfuscationReflectionHelper.setPrivateValue(EntityPigZombie.class, eo, 0,
						PhotonicReflectionNames.EntityPigZombie_angerLevel);
				ObfuscationReflectionHelper.setPrivateValue(EntityPigZombie.class, eo, null,
						PhotonicReflectionNames.EntityPigZombie_angerTargetUUID);
			}
			for (EntityWolf eo : p_77659_2_.getEntitiesWithinAABB(EntityWolf.class,
					new AxisAlignedBB(x - 128, y - 128, z - 128, x + 128, y + 128, z + 128))) {
				eo.setRevengeTarget(eo);
				eo.setAttackTarget(eo);
				eo.setRevengeTarget(null);
				eo.setAttackTarget(null);
				eo.setAngry(false);
			}
			List l = p_77659_2_.getEntitiesWithinAABB(EntityLiving.class,
					new AxisAlignedBB(x - 128, y - 128, z - 128, x + 128, y + 128, z + 128));
			List<EntityLiving> attackers = new ArrayList<>();
			for (Object e : l) {
				EntityLiving eo = (EntityLiving) e;
				if (eo instanceof EntityPigZombie || eo instanceof EntityWolf) continue;
				if (eo.getRevengeTarget() != null && (eo.getRevengeTarget() instanceof EntityPlayer
						|| eo.getRevengeTarget() instanceof EntityVillager))
					attackers.add(eo);
				else if (eo.getAttackTarget() != null && (eo.getAttackTarget() instanceof EntityPlayer
						|| eo.getAttackTarget() instanceof EntityVillager))
					attackers.add(eo);
				else if (eo.getRevengeTimer() > 0)
					attackers.add(eo);
			}
			for (EntityLiving eo : attackers) {
				eo.setAttackTarget(eo);
				if (eo.getRevengeTimer() > 0)
					eo.setRevengeTarget(eo);
				eo.setAttackTarget(null);
				eo.setRevengeTarget(null);
			}
			/*if (attackers.size() == 1)
				attackers.get(0).setAttackTarget(attackers.get(0));
			else if (attackers.size() > 1)
				for (EntityLiving eo : attackers) {
					EntityLiving ea = eo;
					while (ea.getEntityId() == eo.getEntityId()) {
						ea = attackers.get(PhotonicUtils.rand.nextInt(attackers.size()));
					}
					eo.setAttackTarget(ea);
					if (eo.getRevengeTimer() > 0)
						eo.setRevengeTarget(ea);
				}*/
		}
		for (int i = 0; i < 64; i++) {
			double dx = PhotonicUtils.rand.nextGaussian();
			double dy = PhotonicUtils.rand.nextGaussian();
			double dz = PhotonicUtils.rand.nextGaussian();
			double py = Math.sqrt(dx * dx + dy * dy + dz * dz) / (.5 + .5 * PhotonicUtils.rand.nextDouble());
			dx /= py;
			dy /= py;
			dz /= py;
			double rx = PhotonicUtils.rand.nextGaussian() * 19.09859317102744029227;
			double ry = PhotonicUtils.rand.nextGaussian() * 19.09859317102744029227;
			double rz = PhotonicUtils.rand.nextGaussian() * 19.09859317102744029227;
			int color = colors[PhotonicUtils.rand.nextInt(colors.length)];
			EntityLaserEffect ef = new EntityLaserEffect(p_77659_2_);
			ef.setLocationAndAngles(x, y + 1.5, z, 0, 0);
			ef.motionX = dx;
			ef.motionY = dy;
			ef.motionZ = dz;
			ef.velX = dx;
			ef.velY = dy;
			ef.velZ = dz;
			ef.rotX = PhotonicUtils.rand.nextInt(360);
			ef.rotY = PhotonicUtils.rand.nextInt(360);
			ef.rotZ = PhotonicUtils.rand.nextInt(360);
			ef.rotDX = rx * 0.1875;
			ef.rotDY = ry * 0.1875;
			ef.rotDZ = rz * 0.1875;
			ef.velocityChanged = true;
			ef.color = color;
			p_77659_2_.spawnEntity(ef);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, p_77659_3_.getHeldItem(hand));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer p_77648_2_, World p_77648_3_, BlockPos pos, EnumHand hand,
			EnumFacing face, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		return this.onItemRightClick(p_77648_3_, p_77648_2_, hand).getType();
	}
}
