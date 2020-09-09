package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.LaserDirection;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserMerger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockMerger extends Block implements ITileEntityProvider {
	
	public BlockMerger()
    {
        super(Material.rock);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
        this.setCreativeTab(CreativeTabs.tabMisc);
		this.setBlockName(ModPhotonicCraft.MODID + "_merger");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":merger_side");
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setHarvestLevel("pickaxe", 0);
    }
	@Override
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }
	
    public static EnumFacing func_149937_b(int p_149937_0_)
    {
        return EnumFacing.getFront(p_149937_0_ & 7);
    }
    
    public static boolean acceptableDirection(int meta, LaserDirection dir) {
    	if (meta == 2 || meta == 3)
    		return dir == LaserDirection.WEST || dir == LaserDirection.EAST;
    	if (meta == 4 || meta == 5)
    		return dir == LaserDirection.NORTH || dir == LaserDirection.SOUTH;
    	return false;
    }
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityLaserMerger();
    }
    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        int k = p_149691_2_ & 7;
        // p_149691_1_ :
        //          0: down
        //			1: up
        //			2: north
        //			3: south
        //			4: west
        //			5: east
        if (k == p_149691_1_ || p_149691_2_ == -1) {
        	return this.field_149935_N;
        }
        if (p_149691_1_ == 2 || p_149691_1_ == 3) 
        	if (p_149691_2_ == 4 || p_149691_2_ == 5)
        		return field_149936_O;
        if (p_149691_1_ == 4 || p_149691_1_ == 5) 
        	if (p_149691_2_ == 2 || p_149691_2_ == 3)
        		return field_149936_O;
        return this.field_149934_M;
    	//return p_149691_1_ == k ? (k != 1 && k != 0 ? this.field_149935_N : this.field_149936_O) : (k != 1 && k != 0 ? (p_149691_1_ != 1 && p_149691_1_ != 0 ? this.blockIcon : this.field_149934_M) : ((k == 1 && p_149691_1_ == 0) || (k == 0 && p_149691_1_ == 1) ? this.field_149934_M : this.blockIcon));
    }
    

    @SideOnly(Side.CLIENT)
    private IIcon field_149934_M;
    @SideOnly(Side.CLIENT)
    private IIcon field_149935_N;
    @SideOnly(Side.CLIENT)
    private IIcon field_149936_O;
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("photoniccraft:laser_top");
        this.field_149934_M = p_149651_1_.registerIcon("photoniccraft:laser_top");
        this.field_149935_N = p_149651_1_.registerIcon("photoniccraft:laser_top_n");
        this.field_149936_O = p_149651_1_.registerIcon("photoniccraft:merger_side");
    }
    /**
     * Called upon block activation (right click on the block.)
     */
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
    
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int l = (int)Math.floor((double) (p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, new int[]{2,5,3,4}[l], 2);
        super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_, p_149689_6_);
    }
}
