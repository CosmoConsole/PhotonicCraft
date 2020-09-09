package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityFloodlight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFloodlight extends BlockDirectional implements ITileEntityProvider {
	public static final PropertyBool powered = PropertyBool.create("powered");

	public static int determineOrientation(World par0World, BlockPos bp, EntityLivingBase par4EntityPlayer) {
		int par1 = bp.getX(), par2 = bp.getY(), par3 = bp.getZ();
		if (MathHelper.abs((float) par4EntityPlayer.posX - par1) < 2.0F
				&& MathHelper.abs((float) par4EntityPlayer.posZ - par3) < 2.0F) {
			double var5 = par4EntityPlayer.posY + 1.82D - par4EntityPlayer.getYOffset();

			if (var5 - par2 > 2.5D) {
				return 1;
			}

			if (par2 - var5 > 0.0D) {
				return 0;
			}
		}

		int var7 = MathHelper.floor(par4EntityPlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		return var7 == 0 ? 2 : (var7 == 1 ? 5 : (var7 == 2 ? 3 : (var7 == 3 ? 4 : 0)));
	}

	public static int determineOrientation(World p_150071_0_, int p_150071_1_, int p_150071_2_, int p_150071_3_,
			EntityLivingBase p_150071_4_) {
		if (MathHelper.abs((float) p_150071_4_.posX - p_150071_1_) < 2.0F
				&& MathHelper.abs((float) p_150071_4_.posZ - p_150071_3_) < 2.0F) {
			double d0 = p_150071_4_.posY + 1.82D - p_150071_4_.getYOffset();
			int l = MathHelper.floor(p_150071_4_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

			if (d0 - p_150071_2_ > 2.0D) {
				return new int[] { 2, 3, 10, 11 }[(l + 1) & 3];
			}

			if (p_150071_2_ - d0 > 0.0D) {
				return new int[] { 13, 12, 5, 4 }[l];
			}
		}

		int l = MathHelper.floor((p_150071_4_.rotationYaw + 45.0F) * 4.0F / 360.0F + 0.5D) & 3;
		return new int[] { 0, 1, 8, 9 }[l];
	}

	public static EnumFacing func_149937_b(int p_149937_0_) {
		return EnumFacing.getFront(p_149937_0_ & 7);
	}

	public BlockFloodlight() {
		super(Material.ROCK);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_floodlight");
		this.setLightOpacity(0);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.NORTH)
				.withProperty(powered, false));
	}

	@Override
	public void breakBlock(World p_149749_1_, BlockPos pos, IBlockState state) {
		TileEntity te = p_149749_1_.getTileEntity(pos);
		if (te != null && te instanceof TileEntityFloodlight)
			((TileEntityFloodlight) te).onBreak();
		super.breakBlock(p_149749_1_, pos, state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockDirectional.FACING, powered });
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityFloodlight();
	}

	@Override
	public Item getItemDropped(IBlockState state, Random p_149650_2_, int p_149650_3_) {
		return PhotonicItems.laserItems[7];
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return PhotonicUtils.readDirectionPropertyAsInteger(state, BlockDirectional.FACING)
				| (PhotonicUtils.readBoolProperty(state, powered) ? 8 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockDirectional.FACING, PhotonicUtils.convertIntToEnumFacing(meta & 7))
				.withProperty(powered, (meta & 8) != 0);
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
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityFloodlight)
			((TileEntityFloodlight) te).onBreak();
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos, state.withProperty(BlockDirectional.FACING,
				PhotonicUtils.convertIntToEnumFacing(BlockFloodlight.determineOrientation(worldIn, pos, placer))));
	}
}