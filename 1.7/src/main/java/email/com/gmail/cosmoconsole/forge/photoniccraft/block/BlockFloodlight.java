package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFloodlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserMirror;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFloodlight extends Block implements ITileEntityProvider
{

    public BlockFloodlight()
    {
        super(Material.rock);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
        this.setCreativeTab(CreativeTabs.tabMisc);
		this.setBlockName(ModPhotonicCraft.MODID + "_floodlight");
		this.setBlockTextureName("minecraft:stone");
		this.setLightOpacity(0);
    }
    @Override
    public int getRenderType()
    {
        return -1;
    }
    public static EnumFacing func_149937_b(int p_149937_0_)
    {
        return EnumFacing.getFront(p_149937_0_ & 7);
    }
    /*@Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
    	int m = world.getBlockMetadata(x, y, z);
    	if (m == 0 || m == 1) return (side != ForgeDirection.UP) && (side != ForgeDirection.DOWN);
    	if (m == 2 || m == 3) return (side != ForgeDirection.NORTH) && (side != ForgeDirection.SOUTH);
    	if (m == 4 || m == 5) return (side != ForgeDirection.EAST) && (side != ForgeDirection.WEST);
    	return true;
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
    }*/
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityFloodlight();
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
        return 7;
    }
    @Override
    public int damageDropped(int p_149692_1_)
    {
        return 7;
    }

    /*@SideOnly(Side.CLIENT)
    private IIcon field_149934_M;
    @SideOnly(Side.CLIENT)
    private IIcon field_149935_N;
    @SideOnly(Side.CLIENT)
    private IIcon field_149936_O;
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("photoniccraft:laser_side");
        this.field_149934_M = p_149651_1_.registerIcon("photoniccraft:laser_top");
        this.field_149935_N = p_149651_1_.registerIcon("photoniccraft:laser_front");
        this.field_149936_O = p_149651_1_.registerIcon("photoniccraft:laser_top_n");
    }*/
    
    public static int determineOrientation(World p_150071_0_, int p_150071_1_, int p_150071_2_, int p_150071_3_, EntityLivingBase p_150071_4_)
    {
        if (MathHelper.abs((float)p_150071_4_.posX - (float)p_150071_1_) < 2.0F && MathHelper.abs((float)p_150071_4_.posZ - (float)p_150071_3_) < 2.0F)
        {
            double d0 = p_150071_4_.posY + 1.82D - (double)p_150071_4_.yOffset;
            int l = MathHelper.floor_double((double)(p_150071_4_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

            if (d0 - (double)p_150071_2_ > 2.0D)
            {
                return new int[]{2,3,10,11}[(l+1)&3];
            }

            if ((double)p_150071_2_ - d0 > 0.0D)
            {
                return new int[]{13,12,5,4}[l];
            }
        }

        int l = MathHelper.floor_double((double)((p_150071_4_.rotationYaw + 45.0F) * 4.0F / 360.0F) + 0.5D) & 3;
        return new int[]{0,1,8,9}[l];
    }
    
    /*
     * 
    @Override
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        // debagu
    	p_149727_5_.addChatMessage(IChatComponent.Serializer.func_150699_a(String.valueOf(p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_))));
    	return true;
    }*/
    /*public void func_146104_a(EntityPlayer p_149727_5_, TileEntityLaser p_146104_1_)
    {
    	if (p_149727_5_ instanceof EntityPlayerSP) {
    		EntityPlayerSP p = (EntityPlayerSP) p_149727_5_;
    		FMLClientHandler.instance().displayGuiScreen(p, new GuiLaser(p.inventory, p_146104_1_));
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
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
    	return false;
    }

    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
    	int l = BlockPistonBase.determineOrientation(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
        super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_, p_149689_6_);
    }
    /**
     * Called when the block is placed in the world.
     */
    /*public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
    	int l = BlockPistonBase.determineOrientation(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
        super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_, p_149689_6_);
    }*/
}