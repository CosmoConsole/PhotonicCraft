	package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserSemiMirror2;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLaserSemiMirror2 extends BlockLaserMirror {

    public BlockLaserSemiMirror2()
    {
        super();
		this.setBlockName(ModPhotonicCraft.MODID + "_lasersemimirror2");
    }
	@Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityLaserSemiMirror2();
    }
    public static int determineOrientation(World p_150071_0_, int p_150071_1_, int p_150071_2_, int p_150071_3_, EntityLivingBase p_150071_4_)
    {
    	return BlockLaserMirror.determineOrientation(p_150071_0_, p_150071_1_, p_150071_2_, p_150071_3_, p_150071_4_) & 0x8;
    }
    @Override
    public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_)
    {
        return 4;
    }
    @Override
    public int damageDropped(int p_149692_1_)
    {
        return 4;
    }
}
