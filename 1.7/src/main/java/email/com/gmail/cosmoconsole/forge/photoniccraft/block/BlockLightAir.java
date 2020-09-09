package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityGlisterstone;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLightAir;
import net.minecraft.block.BlockAir;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLightAir extends BlockAir implements ITileEntityProvider {
	public BlockLightAir() {
		super();
		this.setHardness(0.0F);
		this.setResistance(0.0F);
		this.setBlockName(ModPhotonicCraft.MODID + "_lightAir");
		this.setTickRandomly(true);
		this.setLightOpacity(0);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		//System.out.println("Got a scheduled block update!");
		world.setBlock(x, y, z, Blocks.air, 0, 3);
		for (int xo = -20; xo < 21; xo+=2)
			for (int yo = -20; yo < 21; yo+=2)
				for (int zo = -20; zo < 21; zo+=2)
						TileEntityLightAir.updateLights(world, x+xo, y+yo, z+zo);
		//System.out.println("I was set to be air. Now I am " + world.getBlock(x, y, z).getUnlocalizedName() + ". Did I clear the lights right???");
	}
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		try {
			TileEntity te = world.getTileEntity(x, y, z);
			return ((TileEntityLightAir)te).ticksLeft > 0 ? 15 : 0;
		} catch (Exception ex) {
			return 0;
		}
	}
	@Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_,
			float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
		p_149660_1_.scheduleBlockUpdate(p_149660_2_, p_149660_3_, p_149660_4_, ModPhotonicCraft.lightAir, TileEntityLightAir.DEF_TICKS);
		return super.onBlockPlaced(p_149660_1_, p_149660_2_, p_149660_3_, p_149660_4_, p_149660_5_, p_149660_6_, p_149660_7_, p_149660_8_, p_149660_9_);
	}
	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
		p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_, p_149726_4_, ModPhotonicCraft.lightAir, TileEntityLightAir.DEF_TICKS);
	}
	@Override
	public int tickRate(World world) {
		return 1;
	}
	@Override
    public int getRenderType()
    {
        return -1;
    }
	@Override
    public TileEntity createTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityLightAir();
    }
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityLightAir();
	}
}
