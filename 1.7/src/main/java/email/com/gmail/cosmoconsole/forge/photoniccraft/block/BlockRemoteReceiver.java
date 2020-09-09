package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLightDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityPrism;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityRemoteReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntitySkyLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRemoteReceiver extends Block implements ITileEntityProvider {
	public BlockRemoteReceiver() {
		super(Material.rock);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
        this.setStepSound(soundTypeMetal);
        this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setBlockName(ModPhotonicCraft.MODID + "_remoteReceiver");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":remoteReceiver");
	}

    
	@Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityRemoteReceiver();
    }
	@Override
    public boolean canProvidePower()
    {
        return true;
    }
    @Override
    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
        return (((TileEntityRemoteReceiver) p_149709_1_.getTileEntity(p_149709_2_, p_149709_3_, p_149709_4_)).getPower());
    }
    @Override
    public int isProvidingStrongPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
        return 0;
    }
    @Override
    public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_,
    		EntityPlayer p_149699_5_) {
    	if (p_149699_1_.isRemote) return;
    	if (p_149699_5_.isSneaking()) {
    		if (p_149699_5_.getHeldItem() != null && p_149699_5_.getHeldItem().getItem() == ModPhotonicCraft.remote) {
        		TileEntityRemoteReceiver te = ((TileEntityRemoteReceiver) p_149699_1_.getTileEntity(p_149699_2_, p_149699_3_, p_149699_4_));
        		if (p_149699_5_.getHeldItem().stackTagCompound.getLong("channel") >= 0) {
        	    	if ((p_149699_1_.getTotalWorldTime() - te.lastclick) < 10) return;
        	    	te.lastclick = p_149699_1_.getTotalWorldTime();
        			te.channel = p_149699_5_.getHeldItem().stackTagCompound.getLong("channel");
        			p_149699_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_remotereceiver.channelcopied",Long.toString(te.channel)));
        		}
    		}
    	}
    }
    @Override
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
    		EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
    	if (p_149727_1_.isRemote) return true;
    	if (p_149727_5_.isSneaking()) {
    		if (p_149727_5_.getHeldItem() != null && p_149727_5_.getHeldItem().getItem() == ModPhotonicCraft.remote) {
        		TileEntityRemoteReceiver te = ((TileEntityRemoteReceiver) p_149727_1_.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_));
            	if ((p_149727_1_.getTotalWorldTime() - te.lastclick) < 10) return true;
            	te.lastclick = p_149727_1_.getTotalWorldTime();
        		if (te.channel < 0) {
        			p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_remotereceiver.nochannel"));
        		} else {
        			p_149727_5_.getHeldItem().stackTagCompound.setLong("channel", te.channel);
        			p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_remote.channelcopied",Long.toString(te.channel)));
        		}
        		return true;
    		}
        	return false;
    	} else {
    		TileEntityRemoteReceiver te = ((TileEntityRemoteReceiver) p_149727_1_.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_));
    		if (p_149727_5_.getHeldItem() != null && p_149727_5_.getHeldItem().getItem() == ModPhotonicCraft.remote) {
    			if (p_149727_5_.getHeldItem().stackTagCompound.getLong("channel") == te.channel) {
	    			te.submitPower(15, te.nonSimultaneous + 1);
	    			return true;
    			}
	    		return true;
    		}
    		if ((p_149727_1_.getTotalWorldTime() - te.lastclick) < 10) return true;
        	te.lastclick = p_149727_1_.getTotalWorldTime();
    		if (te.channel < 0) {
    			p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_remotereceiver.nochannel"));
    		} else {
    			p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_remotereceiver.channelreply",Long.toString(te.channel)));
    		}
        	return true;
    	}
    }
}
