package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import java.util.List;

import com.jcraft.jorbis.Block;
import com.sun.javafx.geom.Vec3d;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.MathHelper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicAPI;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicRadioPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLightAir;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemPocketRadio extends Item implements IEnergyContainerItem {
	public static final int TIMER = 120000; 
	public static final int RF_PER_T = 2;
	public ItemPocketRadio() {
        super();
        this.setUnlocalizedName(ModPhotonicCraft.MODID + "_pocketRadio");
        this.setTextureName(ModPhotonicCraft.MODID + ":pocketRadio");
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
		par1ItemStack.stackTagCompound.setInteger("channel", 0);
		par1ItemStack.stackTagCompound.setBoolean("powered", false);
	}
	@Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
		if (stack.stackTagCompound == null) return 0;
        return (double)(this.getMaxEnergyStored(stack) - this.getEnergyStored(stack)) / (double)(this.getMaxEnergyStored(stack));
    }
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		if (p_77659_2_.isRemote) 
			return p_77659_1_;
		if (p_77659_1_.stackTagCompound == null) {
			p_77659_1_.stackTagCompound = new NBTTagCompound();
			p_77659_1_.stackTagCompound.setInteger("energy", 0);
			p_77659_1_.stackTagCompound.setInteger("channel", 0);
			p_77659_1_.stackTagCompound.setBoolean("powered", false);
		}
		if (!p_77659_2_.isRemote) 
			PhotonicAPI.serverSideInit();
		if (!p_77659_1_.stackTagCompound.getBoolean("powered")) {
			// some checks
			if (!radioAllowed(p_77659_3_)) {
				return p_77659_1_;
			}
		}
		p_77659_1_.stackTagCompound.setBoolean("powered", !p_77659_1_.stackTagCompound.getBoolean("powered"));
		if (p_77659_1_.stackTagCompound.getBoolean("powered"))
			p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_pocketRadio.switchedOn",Integer.toString(p_77659_1_.stackTagCompound.getInteger("channel"))));
		else {
			p_77659_3_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_pocketRadio.switchedOff"));
			ModPhotonicCraft.network.sendTo(new PhotonicRadioPacket(-1.0, new byte[2205]), (EntityPlayerMP)p_77659_3_);
		}
		return p_77659_1_;
	}
	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {

		if (p_77648_3_.isRemote) 
			return true;
		if (p_77648_1_.stackTagCompound == null) {
			p_77648_1_.stackTagCompound = new NBTTagCompound();
			p_77648_1_.stackTagCompound.setInteger("energy", 0);
			p_77648_1_.stackTagCompound.setInteger("channel", 0);
			p_77648_1_.stackTagCompound.setBoolean("powered", false);
		}
		if (!p_77648_3_.isRemote) 
			PhotonicAPI.serverSideInit();
		if (!p_77648_1_.stackTagCompound.getBoolean("powered")) {
			// some checks
			if (!radioAllowed(p_77648_2_)) {
				return true;
			}
		}
		p_77648_1_.stackTagCompound.setBoolean("powered", !p_77648_1_.stackTagCompound.getBoolean("powered"));
		if (p_77648_1_.stackTagCompound.getBoolean("powered"))
			p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_pocketRadio.switchedOn",Integer.toString(p_77648_1_.stackTagCompound.getInteger("channel"))));
		else {
			p_77648_2_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_pocketRadio.switchedOff"));
			ModPhotonicCraft.network.sendTo(new PhotonicRadioPacket(-1.0, new byte[2205]), (EntityPlayerMP)p_77648_2_);
		}
		return true;
	}
	private boolean radioAllowed(EntityPlayer p) {
		// return false if not allowed to turn radio on, also send chat message
		if (ModPhotonicCraft.maximumRadio < 1) {
			p.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_pocketRadio.disabled"));
			return false;
		}
		if (PhotonicAPI.getActiveRadioPlayers() >= ModPhotonicCraft.maximumRadio) {
			p.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_pocketRadio.overLimit"));
			return false;
		}
		for (ItemStack i: p.inventory.mainInventory) {
			if (i != null && i.stackTagCompound != null && i.getItem() == ModPhotonicCraft.pocketRadio && i.stackTagCompound.getBoolean("powered")) {
				p.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_pocketRadio.onePerPlayer"));
				return false;
			}
		}
		for (ItemStack i: p.inventory.armorInventory) {
			if (i != null && i.stackTagCompound != null && i.getItem() == ModPhotonicCraft.pocketRadio && i.stackTagCompound.getBoolean("powered")) {
				p.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_pocketRadio.onePerPlayer"));
				return false;
			}
		}
		return true;
	}
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
    	if (par3Entity instanceof EntityPlayer) {
    		if (par1ItemStack.stackTagCompound == null) {
    			par1ItemStack.stackTagCompound = new NBTTagCompound();
    			par1ItemStack.stackTagCompound.setInteger("energy", 0);
    			par1ItemStack.stackTagCompound.setInteger("channel", 0);
    			par1ItemStack.stackTagCompound.setBoolean("powered", false);
    		}
    		if (!par2World.isRemote && (par2World.getTotalWorldTime()%100L)==0L && !par1ItemStack.stackTagCompound.getBoolean("powered")) {
    			ModPhotonicCraft.network.sendTo(new PhotonicRadioPacket(-1.0, new byte[2205]), (EntityPlayerMP)par3Entity);
    		}
    		if (!par1ItemStack.stackTagCompound.getBoolean("powered")) return;
    		int energy = par1ItemStack.stackTagCompound.getInteger("energy");
    		energy += RF_PER_T;
    		//par1ItemStack.damageItem(1, (EntityLivingBase) par3Entity);
    		if (energy >= (RF_PER_T * TIMER)) {
    			//((EntityPlayer) par3Entity).inventory.setInventorySlotContents(((EntityPlayer) par3Entity).inventory.currentItem, null);
        		par1ItemStack.stackTagCompound.setInteger("energy", RF_PER_T * TIMER);
    			return;
    		}
    		par1ItemStack.stackTagCompound.setInteger("energy", energy);
    		if (par2World.isRemote) {
    			ModPhotonicCraft.lastReceive = System.currentTimeMillis();
    			PhotonicAPI.stillPlaying();
    			return;
    		} else
    			PhotonicAPI.serverSideInit();
    		if ((par2World.getTotalWorldTime()%4L)==0L)
    			if (!PhotonicAPI.allowPlaying((EntityPlayerMP)par3Entity, System.currentTimeMillis(), par1ItemStack.stackTagCompound.getInteger("channel")))
    				par1ItemStack.stackTagCompound.setBoolean("powered", false);
    	}
    }
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		list.add(this.getEnergyStored(stack) + " / " + this.getMaxEnergyStored(stack) + " RF");
		if (stack.stackTagCompound == null) return;
		list.add("Channel: " + Integer.toString(stack.stackTagCompound.getInteger("channel")));
		list.add("Powered " + (stack.stackTagCompound.getBoolean("powered") ? "on" : "off"));
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
