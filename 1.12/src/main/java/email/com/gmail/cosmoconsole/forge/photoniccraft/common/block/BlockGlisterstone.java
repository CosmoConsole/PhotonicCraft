package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityGlisterstone;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGlisterstone extends Block implements ITileEntityProvider {
	public BlockGlisterstone() {
		super(Material.GLASS);
		this.setHardness(0.5F);
		this.setSoundType(SoundType.GLASS);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_glisterstone");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setLightOpacity(0);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityGlisterstone();
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos bp) {
		try {
			TileEntity te = world.getTileEntity(bp);
			return ((TileEntityGlisterstone) te).getLight();
		} catch (Exception ex) {
			return 0;
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
