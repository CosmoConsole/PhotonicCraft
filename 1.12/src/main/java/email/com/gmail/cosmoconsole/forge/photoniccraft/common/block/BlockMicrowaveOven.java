package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityMicrowaveOven;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMicrowaveOven extends BlockHorizontal implements ITileEntityProvider {
	public static final PropertyBool powered = PropertyBool.create("powered");
	public static final AxisAlignedBB box = new AxisAlignedBB(1 / 16f, 0f, 1 / 16f, 15 / 16f, 10 / 16f, 15 / 16f);

	public static int determineOrientation(World par0World, BlockPos bp, EntityLivingBase par4EntityPlayer) {
		int par1 = bp.getX(), par2 = bp.getY(), par3 = bp.getZ();
		return new int[] { 2, 5, 3, 4 }[(int) Math.floor(par4EntityPlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3];
	}

	public BlockMicrowaveOven() {
		super(Material.GLASS);
		this.setHardness(5.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.GLASS);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_microwaveoven");
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
				.withProperty(powered, false));
	}

	@Override
	public void breakBlock(World p_149749_1_, BlockPos bp, IBlockState bs) {
		Random r = new Random();
		TileEntity te = p_149749_1_.getTileEntity(bp);
		if (te != null && te instanceof TileEntityMicrowaveOven) {
			TileEntityMicrowaveOven t = (TileEntityMicrowaveOven) te;
			InventoryHelper.spawnItemStack(p_149749_1_, bp.getX(), bp.getY(), bp.getZ(),
					t.getInvSlot().getStackInSlot(0));
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos bp) {
		return world.isBlockNormalCube(bp.add(0, -1, 0), false);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockHorizontal.FACING, powered });
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityMicrowaveOven();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return box;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random p_149650_2_, int p_149650_3_) {
		return PhotonicItems.laserItems[10];
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos bp) {
		try {
			TileEntity te = world.getTileEntity(bp);
			return ((TileEntityMicrowaveOven) te).isOn() ? 8 : 0;
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
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
				.withProperty(BlockHorizontal.FACING, PhotonicUtils.convertIntToEnumFacingForceHorizontal(meta & 7))
				.withProperty(powered, (meta & 8) != 0);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side != EnumFacing.UP;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return isSideSolid(state, worldIn, pos, face) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		return true;
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
			TileEntityMicrowaveOven tileentitylaser = (TileEntityMicrowaveOven) p_149727_1_.getTileEntity(bp);

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

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, PhotonicUtils
				.convertIntToEnumFacingForceHorizontal(BlockMicrowaveOven.determineOrientation(worldIn, pos, placer))));
	}

	@Override
	public void onNeighborChange(IBlockAccess p_149695_1_, BlockPos bp, BlockPos bpn) {
		int x = bp.getX(), y = bp.getY(), z = bp.getZ();
		if (p_149695_1_ instanceof World) {
			World w = (World) p_149695_1_;
			if (!w.isBlockNormalCube(bp.add(0, -1, 0), true)) {
				w.getBlockState(bp).getBlock().dropBlockAsItem(w, bp, this.getDefaultState(), 0);
				w.setBlockToAir(bp);
			}
		}
	}
}
