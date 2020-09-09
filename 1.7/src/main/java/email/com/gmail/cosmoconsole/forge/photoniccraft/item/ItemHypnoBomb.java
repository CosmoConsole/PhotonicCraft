package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicAPI;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicReflectionHelper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserEffect;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemHypnoBomb extends Item {
	private int[] colors = new int[]{0xFF0000,0x00FF00,0x0000FF,0xFFFF00,0x00FFFF,0xFF00FF,0xFFFFFF};
	public ItemHypnoBomb() {
        super();
        this.setUnlocalizedName(ModPhotonicCraft.MODID + "_hypnoBomb");
        this.setTextureName(ModPhotonicCraft.MODID + ":hypnoBomb");
        this.setMaxStackSize(1);
        this.setNoRepair();
		this.setCreativeTab(CreativeTabs.tabTools);
	}
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		if (!p_77659_3_.capabilities.isCreativeMode) p_77659_3_.inventory.setInventorySlotContents(p_77659_3_.inventory.currentItem, null);
		if (!p_77659_2_.isRemote) {
			double x = p_77659_3_.posX;
			double y = p_77659_3_.posY;
			double z = p_77659_3_.posZ;
			p_77659_3_.addPotionEffect(new PotionEffect(Potion.invisibility.id, 20, 0, true));
			p_77659_2_.playSoundEffect(x, y + 1.5, z, "random.explode", 1.5F, 1.0F);
			List l = p_77659_2_.getEntitiesWithinAABB(EntityCreature.class, AxisAlignedBB.getBoundingBox(x-30,y-30,z-30,x+30,y+30,z+30));
			List<EntityCreature> attackers = new ArrayList<EntityCreature>();
			for (Object e: l) {
				EntityCreature eo = (EntityCreature) e;
				if (eo.getAITarget() != null && (eo.getAITarget() instanceof EntityPlayer || eo.getAITarget() instanceof EntityVillager))
					attackers.add(eo);
				else if (eo.getAttackTarget() != null && (eo.getAttackTarget() instanceof EntityPlayer || eo.getAttackTarget() instanceof EntityVillager))
					attackers.add(eo);
				else if (eo.getEntityToAttack() != null && (eo.getEntityToAttack() instanceof EntityPlayer || eo.getEntityToAttack() instanceof EntityVillager))
					attackers.add(eo);
				else if (eo.func_142015_aE() > 0)
					attackers.add(eo);
			}
			l = p_77659_2_.getEntitiesWithinAABB(EntityGhast.class, AxisAlignedBB.getBoundingBox(x-90,y-90,z-90,x+90,y+90,z+90));
			for (Object e: l) {
				EntityCreature eo = (EntityCreature) e;
				if (eo.getAITarget() != null && (eo.getAITarget() instanceof EntityPlayer || eo.getAITarget() instanceof EntityVillager))
					attackers.add(eo);
				else if (eo.getAttackTarget() != null && (eo.getAttackTarget() instanceof EntityPlayer || eo.getAttackTarget() instanceof EntityVillager))
					attackers.add(eo);
				else if (eo.getEntityToAttack() != null && (eo.getEntityToAttack() instanceof EntityPlayer || eo.getEntityToAttack() instanceof EntityVillager))
					attackers.add(eo);
				else if (eo.func_142015_aE() > 0)
					attackers.add(eo);
			}
			// System.out.println("affecting " + attackers.size() + " entities");
			if (attackers.size() == 1)
				attackers.get(0).setTarget(attackers.get(0));
			else if (attackers.size() > 1)
				for (EntityCreature eo: attackers) {
					EntityCreature ea = eo;
					while (ea.getEntityId() == eo.getEntityId()) {
						ea = attackers.get(PhotonicAPI.rand.nextInt(attackers.size()));
					}
					eo.setTarget(ea);
					eo.setAttackTarget(ea);
					if (eo.func_142015_aE() > 0)
						eo.setRevengeTarget(ea);
				}
			for (Object e: p_77659_2_.getEntitiesWithinAABB(EntityPigZombie.class, AxisAlignedBB.getBoundingBox(x-80,y-80,z-80,x+80,y+80,z+80))) {
				EntityPigZombie eo = (EntityPigZombie) e;
				if (eo.getEntityToAttack() != null && eo.getEntityToAttack() instanceof EntityPlayer) {
					eo.setRevengeTarget(eo);
					eo.setTarget(eo);
				}
				/*Field f = ReflectionHelper.findField(EntityPigZombie.class, PhotonicReflectionHelper.angerLevel);
				try {
					f.set(eo, 0);
				} catch (Exception ex) {
					new NoSuchFieldException("PhotonicCraft could not find angerLevel field for EntityPigZombie. This is a bug!").printStackTrace();
				}*/
				ObfuscationReflectionHelper.setPrivateValue(EntityPigZombie.class, eo, 0, PhotonicReflectionHelper.angerLevel);
			}
			for (Object e: p_77659_2_.getEntitiesWithinAABB(EntityWolf.class, AxisAlignedBB.getBoundingBox(x-80,y-80,z-80,x+80,y+80,z+80))) {
				EntityWolf eo = (EntityWolf) e;
				eo.setAngry(false);
				if (eo.getEntityToAttack() != null && eo.getEntityToAttack() instanceof EntityPlayer) {
					eo.setRevengeTarget(eo);
					eo.setTarget(eo);
				}
			}
			for (int i = 0; i < 30; i++) {
				double dx = PhotonicAPI.rand.nextGaussian();
				double dy = PhotonicAPI.rand.nextGaussian();
				double dz = PhotonicAPI.rand.nextGaussian();
				double rx = PhotonicAPI.rand.nextGaussian() * 19.09859317102744029227;
				double ry = PhotonicAPI.rand.nextGaussian() * 19.09859317102744029227;
				double rz = PhotonicAPI.rand.nextGaussian() * 19.09859317102744029227;
				int color = colors[PhotonicAPI.rand.nextInt(colors.length)];
				EntityLaserEffect ef = new EntityLaserEffect(p_77659_2_);
				ef.setLocationAndAngles(x, y + 1.5, z, 0, 0);
				ef.motionX = dx;
				ef.motionY = dy;
				ef.motionZ = dz;
				ef.velocityChanged = true;
				ef.velX = dx;
				ef.velY = dy;
				ef.velZ = dz;
				ef.rotX = PhotonicAPI.rand.nextInt(360);
				ef.rotY = PhotonicAPI.rand.nextInt(360);
				ef.rotZ = PhotonicAPI.rand.nextInt(360);
				ef.rotDX = rx;
				ef.rotDY = ry;
				ef.rotDZ = rz;
				ef.color = color;
				p_77659_2_.spawnEntityInWorld(ef);
			}
		}
		return p_77659_1_;
	}
	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		this.onItemRightClick(p_77648_1_, p_77648_3_, p_77648_2_);
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
}
