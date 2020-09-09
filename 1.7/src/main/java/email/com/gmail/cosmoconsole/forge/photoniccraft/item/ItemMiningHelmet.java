package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.MathHelper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

public class ItemMiningHelmet extends ItemArmor implements ISpecialArmor, IEnergyContainerItem {
	public static final int TIMER = 12000; 
	public static final int RF_PER_T = 2;
	public String textureName;

    public ItemMiningHelmet(ArmorMaterial p_i45325_1_, int p_i45325_2_, int p_i45325_3_) {
        super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
    }
	public ItemMiningHelmet(String unlocalizedName, ArmorMaterial material, String textureName, int type) {
	    super(material, 0, type);
	    this.textureName = textureName;
	    this.setUnlocalizedName(unlocalizedName);
	    this.setTextureName(ModPhotonicCraft.MODID + ":" + unlocalizedName.replace(ModPhotonicCraft.MODID+"_",""));
	}
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
	    return ModPhotonicCraft.MODID + ":textures/armor/" + this.textureName + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
	}
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage,
			int slot) {
		return new ArmorProperties(0, 0, 0);
	}
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	};
	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par1ItemStack.stackTagCompound == null) {
			par1ItemStack.stackTagCompound = new NBTTagCompound();
			par1ItemStack.stackTagCompound.setInteger("energy", 0);
		}
	}
	@Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
		if (stack.stackTagCompound == null) return 0;
        return (double)(this.getMaxEnergyStored(stack) - this.getEnergyStored(stack)) / (double)(this.getMaxEnergyStored(stack));
    }
	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}
	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
	}
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
	public void onArmorTick(World par2World, EntityPlayer par3Entity, ItemStack par1ItemStack) {
		if (par2World.isRemote) return;
    	if (par3Entity instanceof EntityPlayer) {
    		if (((EntityPlayer)par3Entity).inventory.armorInventory[3] == null)
    			return;
    		if (!((EntityPlayer)par3Entity).inventory.armorInventory[3].equals(par1ItemStack))
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
    		int x = MathHelper.floor(par3Entity.posX);
    		int y = MathHelper.floor(par3Entity.posY) + 1;
    		int z = MathHelper.floor(par3Entity.posZ);
    		for (int xo = -1; xo < 2; xo++)
    			for (int yo = -1; yo < 2; yo++)
    				for (int zo = -1; zo < 2; zo++) {
    					int ax = x + xo, ay = y + yo, az = z + zo;
    					if (par2World.getBlock(ax, ay, az) == Blocks.air || par2World.getBlock(ax, ay, az) == ModPhotonicCraft.lightAir) {
    						par2World.setBlock(ax, ay, az, Blocks.air, 0, 0);
    						par2World.setBlock(ax, ay, az, ModPhotonicCraft.lightAir, 0, 3);
    						//par2World.scheduleBlockUpdate(ax, ay, az, ModPhotonicCraft.lightAir, TileEntityLightAir.DEF_TICKS);
    					}
    				}
    	}
	}
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if (par2World.isRemote) return;
    	if (par3Entity instanceof EntityPlayer) {
    		if (((EntityPlayer)par3Entity).inventory.armorInventory[3] == null)
    			return;
    		if (!((EntityPlayer)par3Entity).inventory.armorInventory[3].equals(par1ItemStack))
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
    		int x = MathHelper.floor(par3Entity.posX);
    		int y = MathHelper.floor(par3Entity.posY) + 1;
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
