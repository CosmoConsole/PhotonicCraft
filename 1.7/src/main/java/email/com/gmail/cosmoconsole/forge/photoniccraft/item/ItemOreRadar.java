package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.geom.Vec3d;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.MathHelper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLightAir;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemOreRadar extends Item implements IEnergyContainerItem {
	public static final int TIMER = 30; 
	public static final int RF_PER_T = 800;
	public ItemOreRadar() {
        super();
        this.setUnlocalizedName(ModPhotonicCraft.MODID + "_oreRadar");
        this.setTextureName(ModPhotonicCraft.MODID + ":oreRadar");
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
	@Override
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
			ArrayList<Block> t = new ArrayList<Block>();
			int nx = MathHelper.floor(p_77659_3_.posX);
			int ny = MathHelper.floor(p_77659_3_.posY);
			int nz = MathHelper.floor(p_77659_3_.posZ);
			for (int x = -3; x < 4; x++)
				for (int y = -3; y < 4; y++)
					for (int z = -3; z < 4; z++) {
						Block b = p_77659_2_.getBlock(nx + x, ny + y, nz + z);
						if (b instanceof BlockSilverfish) {
							t.add(b);
							continue;
						}
						int[] ids = OreDictionary.getOreIDs(new ItemStack(Item.getItemFromBlock(b), 1));
						for (int id: ids) {
							if (OreDictionary.getOreName(id).toLowerCase().startsWith("ore")) {
								t.add(b);
								break;
							}
						}
					}
			if (Math.random() < 0.3) 
				t.clear();
			if (t.size() < 1)
				p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_oreRadar.nothing"));
			else
				p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_oreRadar.found",new ItemStack(t.get((int)(Math.random() * t.size())),1).getDisplayName()));
			
			/*MovingObjectPosition mop = ItemLaserPointer.rayTraceBoth(p_77659_3_, Minecraft.getMinecraft().renderViewEntity, 200, 1.0F);
			if (mop != null) {
				Vec3 pcx = Vec3.createVectorHelper(p_77659_3_.posX, p_77659_3_.posY, p_77659_3_.posZ);
				dist = mop.hitVec.subtract(pcx).lengthVector();
				if (dist >= 180) dist = -1;
				else {
					vdist = mop.hitVec.yCoord - pcx.yCoord;
					hdist = Math.sqrt((dist * dist) - (vdist * vdist));
					if (Double.isNaN(hdist) || (hdist != hdist) || (String.format("%.2f", hdist).equals("NaN"))) hdist = 0;
				}
			}
			p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.distance",(dist < 0 ? "--" : String.format("%.2f", dist) + " m")));
			p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.hdistance",(dist < 0 ? "--" : String.format("%.2f", hdist) + " m")));
			p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.vdistance",(dist < 0 ? "--" : String.format("%.2f", vdist) + " m")));
			p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rangefinder.angle",(dist < 0 ? "--" : String.format("%.2f", Math.toDegrees(Math.atan2(vdist, hdist))) + "\u00B0")));*/
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
			MovingObjectPosition mop = ItemLaserPointer.rayTraceBoth(p_77648_2_, Minecraft.getMinecraft().renderViewEntity, 200, 1.0F);
			ArrayList<Block> t = new ArrayList<Block>();
			int nx = MathHelper.floor(p_77648_2_.posX);
			int ny = MathHelper.floor(p_77648_2_.posY);
			int nz = MathHelper.floor(p_77648_2_.posZ);
			for (int x = -3; x < 4; x++)
				for (int y = -3; y < 4; y++)
					for (int z = -3; z < 4; z++) {
						Block b = p_77648_3_.getBlock(nx + x, ny + y, nz + z);
						int[] ids = OreDictionary.getOreIDs(new ItemStack(Item.getItemFromBlock(b), 1));
						for (int id: ids) {
							if (OreDictionary.getOreName(id).toLowerCase().startsWith("ore")) {
								t.add(b);
								break;
							}
						}
					}
			if (Math.random() < 0.3) 
				t.clear();
			if (t.size() < 1)
				p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_oreRadar.nothing"));
			else
				p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_oreRadar.found",new ItemStack(t.get((int)(Math.random() * t.size())),1).getDisplayName()));
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
