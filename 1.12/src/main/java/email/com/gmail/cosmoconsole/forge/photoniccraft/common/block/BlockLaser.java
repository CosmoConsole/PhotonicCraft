package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLaser extends BlockContainer {
	public static final PropertyDirection FACING = BlockDirectional.FACING;

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

	public static EnumFacing func_149937_b(int p_149937_0_) {
		return EnumFacing.getFront(p_149937_0_ & 7);
	}

	public BlockLaser() {
		super(Material.ROCK);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_laser");
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public void breakBlock(World p_149749_1_, BlockPos bp, IBlockState bs) {
		Random r = new Random();
		TileEntity te = p_149749_1_.getTileEntity(bp);
		if (te != null && te instanceof TileEntityLaser) {
			TileEntityLaser t = (TileEntityLaser) te;
			t.disableFiring();
			t.terminateLaser();
			InventoryHelper.spawnItemStack(p_149749_1_, bp.getX(), bp.getY(), bp.getZ(), t.getFuel().getStackInSlot(0));
			InventoryHelper.spawnItemStack(p_149749_1_, bp.getX(), bp.getY(), bp.getZ(),
					t.getMedium().getStackInSlot(0));
			InventoryHelper.spawnItemStack(p_149749_1_, bp.getX(), bp.getY(), bp.getZ(),
					t.getCoupler().getStackInSlot(0));
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockDirectional.FACING });
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityLaser();
	}

	@Override
	public Item getItemDropped(IBlockState state, Random p_149650_2_, int p_149650_3_) {
		return PhotonicItems.laserItems[0];
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos bp) {
		TileEntity te = world.getTileEntity(bp);
		if (te != null && te instanceof TileEntityLaser) {
			TileEntityLaser t = (TileEntityLaser) te;
			if (t.isFiring()) {
				// return 9;
			}
		}
		return 0;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return PhotonicUtils.readDirectionPropertyAsInteger(state, BlockDirectional.FACING);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockDirectional.FACING, PhotonicUtils.convertIntToEnumFacing(meta & 7));
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
	public boolean isSideSolid(IBlockState base, IBlockAccess world, BlockPos pos, EnumFacing side) {
		int m = PhotonicUtils.readDirectionPropertyAsInteger(world.getBlockState(pos), FACING);
		if (m == 0 || m == 1)
			return (side != EnumFacing.UP) && (side != EnumFacing.DOWN);
		if (m == 2 || m == 3)
			return (side != EnumFacing.NORTH) && (side != EnumFacing.SOUTH);
		if (m == 4 || m == 5)
			return (side != EnumFacing.EAST) && (side != EnumFacing.WEST);
		return true;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return isSideSolid(state, worldIn, pos, face) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World p_149727_1_, BlockPos bp, IBlockState bs, EntityPlayer p_149727_5_,
			EnumHand hand, EnumFacing face, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_5_.isSneaking())
			return false;
		if (p_149727_1_.isRemote) {
			return true;
		} else {
			TileEntityLaser tileentitylaser = (TileEntityLaser) p_149727_1_.getTileEntity(bp);

			if (tileentitylaser != null) {
				p_149727_5_.openGui(ModPhotonicCraft.instance, 0, p_149727_1_, bp.getX(), bp.getY(), bp.getZ());
			}

			return true;
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityLaser) {
			TileEntityLaser t = (TileEntityLaser) te;
			t.disableFiring();
			t.terminateLaser();
		}
		super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityLaser) {
			TileEntityLaser t = (TileEntityLaser) te;
			t.disableFiring();
			t.terminateLaser();
		}
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
				PhotonicUtils.convertIntToEnumFacing(BlockLaser.determineOrientation(worldIn, pos, placer))));
	}

}