package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLightDetector;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLightDetector extends Block implements ITileEntityProvider {
	public BlockLightDetector() {
		super(Material.GLASS);
		this.setHardness(1.0F);
		this.setResistance(0.3F);
		this.setSoundType(SoundType.GLASS);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_lightdetector");
		this.setLightOpacity(0);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityLightDetector();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess p_149709_1_, BlockPos pos, EnumFacing side) {
		return 0;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess p_149709_1_, BlockPos pos, EnumFacing side) {
		return (((TileEntityLightDetector) p_149709_1_.getTileEntity(pos)).light);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
