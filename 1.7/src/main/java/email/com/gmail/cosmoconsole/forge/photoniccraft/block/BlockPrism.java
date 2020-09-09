package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
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
import net.minecraft.world.World;

public class BlockPrism extends Block implements ITileEntityProvider {
	public BlockPrism() {
		super(Material.glass);
		this.setHardness(1.0F);
		this.setResistance(0.3F);
		this.setStepSound(soundTypeGlass);
        this.setCreativeTab(CreativeTabs.tabMisc);
		this.setBlockName(ModPhotonicCraft.MODID + "_prism");
		this.setBlockTextureName("minecraft:glass");
		this.setLightOpacity(0);
	}
	@Override
    public int getRenderType()
    {
        return -1;
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
        return 8;
    }
    @Override
    public int damageDropped(int p_149692_1_)
    {
        return 8;
    }

    
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }
	@Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityPrism();
    }
}
