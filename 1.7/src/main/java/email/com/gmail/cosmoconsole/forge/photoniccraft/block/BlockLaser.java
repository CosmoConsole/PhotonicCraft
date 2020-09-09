package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.ContainerLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.GuiLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaser;
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

public class BlockLaser extends BlockContainer
{

    public BlockLaser()
    {
        super(Material.rock);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setStepSound(soundTypeMetal);
		this.setBlockName(ModPhotonicCraft.MODID + "_laser");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":laser_side");
		this.setHarvestLevel("pickaxe", 0);
		this.setLightOpacity(0);
    }
    @Override
    public int getRenderType()
    {
        return -1;
    }
    @Override
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
        this.func_149930_e(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }
    
    public static EnumFacing func_149937_b(int p_149937_0_)
    {
        return EnumFacing.getFront(p_149937_0_ & 7);
    }
    @Override
    public int getLightValue(IBlockAccess world,int x, int y, int z){
    	TileEntity te = world.getTileEntity(x, y, z);
    	if (te != null && te instanceof TileEntityLaser) {
    		TileEntityLaser t = (TileEntityLaser) te;
    		if (t.isFiring()) {
    			//return 9;
    		}
    	}
    	return 0;
    }
    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
    	int m = world.getBlockMetadata(x, y, z);
    	if (m == 0 || m == 1) return (side != ForgeDirection.UP) && (side != ForgeDirection.DOWN);
    	if (m == 2 || m == 3) return (side != ForgeDirection.NORTH) && (side != ForgeDirection.SOUTH);
    	if (m == 4 || m == 5) return (side != ForgeDirection.EAST) && (side != ForgeDirection.WEST);
    	return true;
    }
    @Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
    	Random r = new Random();
    	TileEntity te = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
    	if (te != null && te instanceof TileEntityLaser) {
    		TileEntityLaser t = (TileEntityLaser) te;
    		t.terminateLaser();
    		for (int i = 0; i < 3; i++) {
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
    
    private void func_149930_e(World p_149930_1_, int p_149930_2_, int p_149930_3_, int p_149930_4_)
    {
        if (!p_149930_1_.isRemote)
        {
            Block block = p_149930_1_.getBlock(p_149930_2_, p_149930_3_, p_149930_4_ - 1);
            Block block1 = p_149930_1_.getBlock(p_149930_2_, p_149930_3_, p_149930_4_ + 1);
            Block block2 = p_149930_1_.getBlock(p_149930_2_ - 1, p_149930_3_, p_149930_4_);
            Block block3 = p_149930_1_.getBlock(p_149930_2_ + 1, p_149930_3_, p_149930_4_);
            byte b0 = 3;

            if (block.func_149730_j() && !block1.func_149730_j())
            {
                b0 = 3;
            }

            if (block1.func_149730_j() && !block.func_149730_j())
            {
                b0 = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j())
            {
                b0 = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j())
            {
                b0 = 4;
            }

            p_149930_1_.setBlockMetadataWithNotify(p_149930_2_, p_149930_3_, p_149930_4_, b0, 2);
        }
    }
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityLaser();
    }
    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        int k = p_149691_2_ & 7;
    	return p_149691_1_ == k ? (k != 1 && k != 0 ? this.field_149935_N : this.field_149936_O) : (k != 1 && k != 0 ? (p_149691_1_ != 1 && p_149691_1_ != 0 ? this.blockIcon : this.field_149934_M) : ((k == 1 && p_149691_1_ == 0) || (k == 0 && p_149691_1_ == 1) ? this.field_149934_M : this.blockIcon));
    }

    @SideOnly(Side.CLIENT)
    private IIcon field_149934_M;
    @SideOnly(Side.CLIENT)
    private IIcon field_149935_N;
    @SideOnly(Side.CLIENT)
    private IIcon field_149936_O;
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("photoniccraft:laser_side");
        this.field_149934_M = p_149651_1_.registerIcon("photoniccraft:laser_top");
        this.field_149935_N = p_149651_1_.registerIcon("photoniccraft:laser_front");
        this.field_149936_O = p_149651_1_.registerIcon("photoniccraft:laser_top_n");
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
            TileEntityLaser tileentitylaser = (TileEntityLaser)p_149727_1_.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_);

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

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if (playerIn.isSneaking())
    		return false;
    		else {
    			if (!worldIn.isRemote) {
    				TileEntityLaser tileEntityLaser = (TileEntityLaser) worldIn.getTileEntity(x, y, z);
    				if (tileEntityLaser != null) {
    					playerIn.openGui(ModPhotonicCraft.instance, 0, worldIn, x, y, z);
    				}
    			}
    			return true;
    		}
    }
    
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return ModPhotonicCraft.laserItem;
    }
    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return ModPhotonicCraft.laserItem;
    }
    @Override
    public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_)
    {
        return 0;
    }
    @Override
    public int damageDropped(int p_149692_1_)
    {
        return 0;
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
    	int l = BlockPistonBase.determineOrientation(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
        super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_, p_149689_6_);
    }
}