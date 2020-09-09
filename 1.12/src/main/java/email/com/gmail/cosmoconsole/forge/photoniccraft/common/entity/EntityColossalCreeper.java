package email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity;

import java.util.Collection;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityColossalCreeper extends EntityCreeper {

	public boolean forceSummon = false;

	private int lastActiveTime;

	/**
	 * The amount of time since the creeper was close enough to the player to
	 * ignite
	 */
	private int timeSinceIgnited;

	private int fuseTime = 30;

	public EntityColossalCreeper(World p_i1733_1_) {
		super(p_i1733_1_);
		this.setSize(this.width * 7.0F, this.height * 6.0F);

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIColossalCreeperSwell(this));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 64.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
	}

	@Override
	protected boolean canDespawn() {
		return this.world.getClosestPlayerToEntity(this, 128.0D) == null && super.canDespawn();
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j;
		int k;

		j = 32 + this.rand.nextInt(10 + p_70628_2_);
		this.dropItem(Items.GUNPOWDER, j);

		j = this.rand.nextInt(4 + p_70628_2_);
		for (k = 0; k < j; ++k) {
			this.dropItem(Item.getItemFromBlock(Blocks.TNT), 1);
		}

		if (this.rand.nextInt(Math.max(1, 3 - p_70628_2_)) == 0)
			this.entityDropItem(new ItemStack(PhotonicItems.photonicResources[11], 1), 0.0F);
	}

	private void explode() {
		if (!this.world.isRemote) {
			this.dead = true;
			this.explodeReal();
			this.setDead();
			this.spawnLingeringCloud();
		}
	}

	private void explodeBonus() {
		if (!this.world.isRemote) {
			this.explodeReal();
			this.spawnLingeringCloud();
		}
	}

	private void explodeReal() {
		float f = this.getPowered() ? 2.0F : 1.0F;
		boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
		this.world.createExplosion(this, this.posX, this.posY + (6 * f), this.posZ, 10 * f, flag);
		this.world.createExplosion(this, this.posX, this.posY - (6 * f), this.posZ, 10 * f, flag);
		this.world.createExplosion(this, this.posX, this.posY, this.posZ + (6 * f), 10 * f, flag);
		this.world.createExplosion(this, this.posX, this.posY, this.posZ - (6 * f), 10 * f, flag);
		this.world.createExplosion(this, this.posX + (6 * f), this.posY, this.posZ, 10 * f, flag);
		this.world.createExplosion(this, this.posX - (6 * f), this.posY, this.posZ, 10 * f, flag);
		this.world.createExplosion(this, this.posX, this.posY, this.posZ, 10 * f, flag);
		this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE,
				SoundCategory.BLOCKS, 10.0F,
				(1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
	}

	@Override
	public int getMaxFallHeight() {
		return this.getAttackTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
	}

	@Override
	public boolean getPowered() {
		return false;
	}

	@Override
	public double getYOffset() {
		return super.getYOffset() * 6.0;
	}

	@Override
	public void onUpdate() {
		if (this.isEntityAlive()) {
			this.lastActiveTime = this.timeSinceIgnited;

			if (this.hasIgnited()) {
				this.setCreeperState(1);
			}

			int i = this.getCreeperState();

			if (i > 0 && this.timeSinceIgnited == 0) {
				this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
			}

			this.timeSinceIgnited += i;

			if (this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= this.fuseTime) {
				this.timeSinceIgnited = this.fuseTime;
				// this.explode();
			}
		}
		super.onUpdate();
		if (this.timeSinceIgnited >= this.fuseTime) {
			this.explodeBonus();
		}
	}
	
	@Override
	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	private void spawnLingeringCloud() {
		Collection<PotionEffect> collection = this.getActivePotionEffects();

		if (!collection.isEmpty()) {
			EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY,
					this.posZ);
			entityareaeffectcloud.setRadius(5.0F);
			entityareaeffectcloud.setRadiusOnUse(-0.5F);
			entityareaeffectcloud.setWaitTime(10);
			entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
			entityareaeffectcloud
					.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());

			for (PotionEffect potioneffect : collection) {
				entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
			}

			this.world.spawnEntity(entityareaeffectcloud);
		}
	}
}
