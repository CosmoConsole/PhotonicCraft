package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFluorescentBlacklight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBlacklight extends Block implements ITileEntityProvider {
	public BlockBlacklight() {
		super(Material.glass);
		this.setHardness(5.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeStone);
		this.setBlockName(ModPhotonicCraft.MODID + "_blacklight");
		this.setBlockTextureName("minecraft:stone");
		this.setBlockBounds(0.15f, 14f/16f, 0.05f, 0.85f, 1f, 0.95f);
		this.setLightOpacity(0);
	}
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCubeDefault(x, y+1, z, false);
	}
	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		int x = p_149695_2_, y = p_149695_3_, z = p_149695_4_;
		if (!p_149695_1_.isBlockNormalCubeDefault(x, y+1, z, true)) {
			p_149695_1_.getBlock(x, y, z).dropBlockAsItem(p_149695_1_, x, y, z, p_149695_1_.getBlockMetadata(x, y, z), 0);
			p_149695_1_.setBlockToAir(x, y, z);
		}
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
    public int getRenderType()
    {
        return -1;
    }
    @Override
    public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_)
    {
        return 12;
    }
    @Override
    public int damageDropped(int p_149692_1_)
    {
        return 12;
    }

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		try {
			TileEntity te = world.getTileEntity(x, y, z);
			return ((TileEntityFluorescentBlacklight)te).isPowered() ? 8 : 0;
		} catch (Exception ex) {
			return 0;
		}
	}
    
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	@Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityFluorescentBlacklight();
    }
}
