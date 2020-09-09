package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAnalogRedstoneLamp extends Block {
	public static final PropertyInteger power = PropertyInteger.create("power", 0, 15);

	public BlockAnalogRedstoneLamp() {
		super(Material.REDSTONE_LIGHT);
		this.setHardness(0.3F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.GLASS);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_analoglamp");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(power, 0));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { power });
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos bp) {
		if (world.getBlockState(bp).getProperties().containsKey(power))
			return PhotonicUtils.readIntegerProperty(world.getBlockState(bp), power);
		else
			return 0;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return PhotonicUtils.readIntegerProperty(state, power);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(power, meta);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!worldIn.isRemote)
			updateLight(worldIn, pos);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote)
			updateLight(worldIn, pos);
	}

	@Override
	public void onNeighborChange(IBlockAccess p_149695_1_, BlockPos bp, BlockPos neighbor) {
		int p_149695_2_ = bp.getX(), p_149695_3_ = bp.getY(), p_149695_4_ = bp.getZ();
		if (p_149695_1_ instanceof World) {
			World w = (World) p_149695_1_;
			if (!w.isRemote) {
				updateLight(w, bp);
			}
		}
	}

	private void updateLight(World w, BlockPos bp) {
		int maxPowerLevel = w.isBlockIndirectlyGettingPowered(bp);
		w.setLightFor(EnumSkyBlock.BLOCK, bp, maxPowerLevel);
		w.setBlockState(bp, this.getDefaultState().withProperty(power, maxPowerLevel), 3);
		w.notifyBlockUpdate(bp, w.getBlockState(bp), w.getBlockState(bp), 3);
		w.checkLight(bp);
	}
}
