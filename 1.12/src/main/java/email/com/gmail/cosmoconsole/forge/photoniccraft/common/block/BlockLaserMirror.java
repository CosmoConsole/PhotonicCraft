package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLaserMirror extends Block {
	public static final PropertyInteger orient = PropertyInteger.create("orient", 0, 15);

	public static int determineOrientation(World p_150071_0_, BlockPos bp, EntityLivingBase p_150071_4_) {
		EnumFacing face = EnumFacing.getDirectionFromEntityLiving(bp, p_150071_4_);
		if (face == EnumFacing.UP) {
			int l = MathHelper.floor((double) (p_150071_4_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			return new int[] { 2, 3, 10, 11 }[(l + 1) & 3];
		}

		if (face == EnumFacing.DOWN) {
			int l = MathHelper.floor((double) (p_150071_4_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			return new int[] { 13, 12, 5, 4 }[l];
		}

		int l = MathHelper.floor((double) ((p_150071_4_.rotationYaw + 45.0F) * 4.0F / 360.0F) + 0.5D) & 3;
		return new int[] { 0, 1, 8, 9 }[l];
	}

	public static EnumFacing func_149937_b(int p_149937_0_) {
		return EnumFacing.getFront(p_149937_0_ & 15);
	}

	public BlockLaserMirror() {
		super(Material.ROCK);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_lasermirror");
		this.setLightOpacity(0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { orient });
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random p_149650_2_, int p_149650_3_) {
		return PhotonicItems.laserItems[1];
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return PhotonicUtils.readIntegerProperty(state, orient);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(orient, meta);
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

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side) {
		int m = PhotonicUtils.readIntegerProperty(world.getBlockState(pos), orient);
		if (m == 0 || m == 1 || m == 8 || m == 9)
			return (side == EnumFacing.UP) || (side == EnumFacing.DOWN);
		if (m == 2 || m == 4 || m == 10 || m == 12)
			return (side == EnumFacing.NORTH) || (side == EnumFacing.SOUTH);
		if (m == 3 || m == 5 || m == 11 || m == 13)
			return (side == EnumFacing.WEST) || (side == EnumFacing.EAST);
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return isSideSolid(state, worldIn, pos, face) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	public void onBlockAdded(World p_149726_1_, BlockPos pos, IBlockState state) {
		super.onBlockAdded(p_149726_1_, pos, state);
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World p_149689_1_, BlockPos pos, IBlockState state, EntityLivingBase p_149689_5_,
			ItemStack p_149689_6_) {
		int l = BlockLaserMirror.determineOrientation(p_149689_1_, pos, p_149689_5_);
		p_149689_1_.setBlockState(pos, this.getDefaultState().withProperty(orient, l), 2);
		super.onBlockPlacedBy(p_149689_1_, pos, state, p_149689_5_, p_149689_6_);
	}
}