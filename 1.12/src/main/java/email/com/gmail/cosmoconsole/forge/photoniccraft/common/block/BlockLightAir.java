package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLightAir;
import net.minecraft.block.BlockAir;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLightAir extends BlockAir implements ITileEntityProvider {
	public BlockLightAir() {
		super();
		this.setHardness(0.0F);
		this.setResistance(0.0F);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_lightair");
		this.setTickRandomly(true);
		this.setLightOpacity(0);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos bp) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityLightAir();
	}

	@Override
	public TileEntity createTileEntity(World p_149915_1_, IBlockState st) {
		return new TileEntityLightAir();
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos bp) {
		return 15;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState st) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public int tickRate(World world) {
		return 10;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && (te instanceof TileEntityLightAir)) {
			if (((TileEntityLightAir) te).ticksLeft > 2) {
				world.scheduleBlockUpdate(pos, this, tickRate(world), 20);
				return;
			}
		}
		world.setBlockToAir(pos);
		world.scheduleBlockUpdate(pos, this, tickRate(world), 20);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote)
			worldIn.scheduleBlockUpdate(pos, this, tickRate(worldIn), 20);
	}

	@Override
	public boolean requiresUpdates() {
		return true;
	}
}
