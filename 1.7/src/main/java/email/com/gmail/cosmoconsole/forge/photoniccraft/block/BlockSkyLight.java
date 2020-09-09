package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntitySkyLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSkyLight extends Block implements ITileEntityProvider {
	public BlockSkyLight() {
		super(Material.rock);
		this.setHardness(1.0F);
		this.setResistance(60.0F);
		this.setStepSound(soundTypeCloth);
		this.setBlockName(ModPhotonicCraft.MODID + "_skyLight");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":skylight");
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	@Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
		TileEntity te = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
		if (te != null && te instanceof TileEntitySkyLight)
			((TileEntitySkyLight) te).onBreak();
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		if (world.getTileEntity(x, y, z) != null)
			world.getTileEntity(x, y, z).updateEntity();
	}
	@Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntitySkyLight();
    }
	@Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_, p_149689_6_);
        TileEntity te = p_149689_1_.getTileEntity(p_149689_2_, p_149689_3_, p_149689_4_);
		if (te != null && te instanceof TileEntitySkyLight)
			((TileEntitySkyLight) te).onPlace();
		super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_, p_149689_6_);
    }
}
