package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityXRayTransmitter;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockXRayTransmitter extends BlockDirectional implements ITileEntityProvider {
	public BlockXRayTransmitter() {
		super(Material.ROCK);
		this.setHardness(8.0F);
		this.setResistance(10.0F);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_xraytransmitter");
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockDirectional.FACING });
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityXRayTransmitter();
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
		return this.getDefaultState()
				.withProperty(BlockDirectional.FACING, PhotonicUtils.convertIntToEnumFacing(meta & 7));
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos, state.withProperty(BlockDirectional.FACING, PhotonicUtils
				.convertIntToEnumFacing(BlockXRayTransmitter.determineOrientation(worldIn, pos, placer))));
	}

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
}
