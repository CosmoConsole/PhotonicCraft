package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserDetector2;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLaserDetector2 extends BlockLaserDetector {
	public BlockLaserDetector2()
    {
        super();
		this.setBlockName(ModPhotonicCraft.MODID + "_laserdetector2");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":laser_side");
    }
	@Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityLaserDetector2();
    }
    @Override
    public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_)
    {
        return 6;
    }
    @Override
    public int damageDropped(int p_149692_1_)
    {
        return 6;
    }
}
