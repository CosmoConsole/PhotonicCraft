package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserMerger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMerger extends BlockHorizontal implements ITileEntityProvider {

	@Deprecated
	public static boolean acceptableDirection(int meta, EnumFacing dir) {
		if (meta == 2 || meta == 3)
			return dir == EnumFacing.WEST || dir == EnumFacing.EAST;
		if (meta == 4 || meta == 5)
			return dir == EnumFacing.NORTH || dir == EnumFacing.SOUTH;
		return false;
	}

	public static boolean acceptableDirection(EnumFacing mergerDir, EnumFacing dir) {
		if (mergerDir == EnumFacing.NORTH || mergerDir == EnumFacing.SOUTH)
			return dir == EnumFacing.WEST || dir == EnumFacing.EAST;
		if (mergerDir == EnumFacing.WEST || mergerDir == EnumFacing.EAST)
			return dir == EnumFacing.NORTH || dir == EnumFacing.SOUTH;
		return false;
	}

	public static int determineOrientation(World par0World, BlockPos bp, EntityLivingBase par4EntityPlayer) {
		int par1 = bp.getX(), par2 = bp.getY(), par3 = bp.getZ();
		return new int[] { 2, 5, 3, 4 }[(int) Math.floor(par4EntityPlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3];
	}

	public static EnumFacing func_149937_b(int p_149937_0_) {
		return EnumFacing.getFront(p_149937_0_ & 7);
	}

	public BlockMerger() {
		super(Material.ROCK);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_merger");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setHarvestLevel("pickaxe", 0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockHorizontal.FACING });
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityLaserMerger();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return PhotonicUtils.readDirectionPropertyAsInteger(state, BlockHorizontal.FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockHorizontal.FACING,
				PhotonicUtils.convertIntToEnumFacingForceHorizontal(meta & 7));
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, PhotonicUtils
				.convertIntToEnumFacingForceHorizontal(BlockMerger.determineOrientation(worldIn, pos, placer))));
	}
}
