package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLightDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityPrism;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntitySkyLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLightDetector extends Block implements ITileEntityProvider {
	public BlockLightDetector() {
		super(Material.glass);
		this.setHardness(1.0F);
		this.setResistance(0.3F);
		this.setStepSound(soundTypeGlass);
        this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setBlockName(ModPhotonicCraft.MODID + "_lightDetector");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":lightDetector");
		this.setLightOpacity(0);
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
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityLightDetector();
    }
	@Override
    public boolean canProvidePower()
    {
        return true;
    }
    @Override
    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
        return (((TileEntityLightDetector) p_149709_1_.getTileEntity(p_149709_2_, p_149709_3_, p_149709_4_)).light);
    }
    @Override
    public int isProvidingStrongPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
        return 0;
    }
}
