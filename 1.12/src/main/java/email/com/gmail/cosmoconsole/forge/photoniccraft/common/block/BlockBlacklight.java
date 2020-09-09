package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityFluorescentBlacklight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBlacklight extends BlockHorizontal implements ITileEntityProvider {
	public static final PropertyBool powered = PropertyBool.create("powered");
	public static final AxisAlignedBB box = new AxisAlignedBB(0.15f, 14f / 16f, 0.05f, 0.85f, 1f, 0.95f);

	public static int determineOrientation(World par0World, BlockPos bp, EntityLivingBase par4EntityPlayer) {
		int par1 = bp.getX(), par2 = bp.getY(), par3 = bp.getZ();
		return new int[] { 2, 5, 3, 4 }[(int) Math.floor(par4EntityPlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3];
	}

	public BlockBlacklight() {
		super(Material.GLASS);
		this.setHardness(5.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.STONE);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_blacklight");
		this.setLightOpacity(0);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
				.withProperty(powered, false));
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos bp) {
		return world.isBlockNormalCube(bp.add(0, 1, 0), false);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockHorizontal.FACING, powered });
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityFluorescentBlacklight();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return box;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random p_149650_2_, int p_149650_3_) {
		return PhotonicItems.laserItems[12];
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos bp) {
		try {
			TileEntity te = world.getTileEntity(bp);
			return ((TileEntityFluorescentBlacklight) te).isPowered() ? 8 : 0;
		} catch (Exception ex) {
			return 0;
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return PhotonicUtils.readDirectionPropertyAsInteger(state, BlockHorizontal.FACING)
				| (PhotonicUtils.readBoolProperty(state, powered) ? 8 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
				.withProperty(BlockHorizontal.FACING, PhotonicUtils.convertIntToEnumFacingForceHorizontal(meta & 7))
				.withProperty(powered, (meta & 8) != 0);
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, PhotonicUtils
				.convertIntToEnumFacingForceHorizontal(BlockBlacklight.determineOrientation(worldIn, pos, placer))));
	}

	@Override
	public void onNeighborChange(IBlockAccess p_149695_1_, BlockPos bp, BlockPos neighbor) {
		int p_149695_2_ = bp.getX(), p_149695_3_ = bp.getY(), p_149695_4_ = bp.getZ();
		int x = p_149695_2_, y = p_149695_3_, z = p_149695_4_;
		if (p_149695_1_ instanceof World) {
			World w = (World) p_149695_1_;
			if (!w.isBlockNormalCube(bp.add(0, 1, 0), true)) {
				w.getBlockState(bp).getBlock().dropBlockAsItem(w, bp, this.getDefaultState(), 0);
				w.setBlockToAir(bp);
			}
		}
	}
}
