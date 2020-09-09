package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.ContainerLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.GuiLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLightDetector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLaserCharger extends BlockContainer
{

    public BlockLaserCharger()
    {
        super(Material.rock);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setStepSound(soundTypeMetal);
		this.setBlockName(ModPhotonicCraft.MODID + "_laserCharger");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":laser_charger");
		this.setHarvestLevel("pickaxe", 0);
		this.setLightOpacity(0);
    }
    @Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
    	Random r = new Random();
    	TileEntity te = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
    	if (te != null && te instanceof TileEntityLaserCharger) {
    		TileEntityLaserCharger t = (TileEntityLaserCharger) te;
    		for (int i = 0; i < 1; i++) {
    			ItemStack itemstack = t.getStackInSlot(i);

                if (itemstack != null)
                {
                    float f = r.nextFloat() * 0.8F + 0.1F;
                    float f1 = r.nextFloat() * 0.8F + 0.1F;
                    float f2 = r.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = r.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(p_149749_1_, (double)((float)p_149749_2_ + f), (double)((float)p_149749_3_ + f1), (double)((float)p_149749_4_ + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)r.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)r.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)r.nextGaussian() * f3);
                        p_149749_1_.spawnEntityInWorld(entityitem);
                    }
                }
    		}
    	}
    }
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityLaserCharger();
    }
    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
    	if (p_149727_5_.isSneaking()) return false;
        if (p_149727_1_.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityLaserCharger tileentitylaser = (TileEntityLaserCharger)p_149727_1_.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_);

            if (tileentitylaser != null)
            {
                p_149727_5_.openGui(ModPhotonicCraft.instance, 0, p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_);
            }

            return true;
        }
    }
    /*public void func_146104_a(EntityPlayer p_149727_5_, TileEntityLaser p_146104_1_)
    {
    	if (p_149727_5_ instanceof EntityPlayerSP) {
    		EntityPlayerSP p = (EntityPlayerSP) p_149727_5_;
    		p.openGui(ModPhotonicCraft.instance, p.getEntityId(), p_146104_1_.getWorldObj(), p_146104_1_.xCoord, p_146104_1_.yCoord, p_146104_1_.zCoord);
    		//FMLClientHandler.instance().displayGuiScreen(p, new GuiLaser(p.inventory, p_146104_1_));
    	}
    	else if (p_149727_5_ instanceof EntityPlayerMP) {
    		EntityPlayerMP p = (EntityPlayerMP) p_149727_5_;
        	p.getNextWindowId();
            p.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(p.currentWindowId, 7, p_146104_1_.getInventoryName(), p_146104_1_.getSizeInventory(), p_146104_1_.hasCustomInventoryName()));
            p.openContainer = new ContainerLaser(p.inventory, p_146104_1_);
            p.openContainer.windowId = p.currentWindowId;
            p.openContainer.addCraftingToCrafters(p);
    	}
    }*/
    @Override
    public boolean canProvidePower()
    {
        return true;
    }
    @Override
    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
    	return ((TileEntityLaserCharger)p_149709_1_.getTileEntity(p_149709_2_, p_149709_3_, p_149709_4_)).chargedLastTick ? 15 : 0;
    } 
    @Override
    public int isProvidingStrongPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
        return 0;
    }
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if (playerIn.isSneaking())
    		return false;
    		else {
    			if (!worldIn.isRemote) {
    				TileEntityLaserCharger tileEntityLaser = (TileEntityLaserCharger) worldIn.getTileEntity(x, y, z);
    				if (tileEntityLaser != null) {
    					playerIn.openGui(ModPhotonicCraft.instance, 0, worldIn, x, y, z);
    				}
    			}
    			return true;
    		}
    }
    
}