package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntitySkyLight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSkyLight extends Block implements ITileEntityProvider {
	public BlockSkyLight() {
		super(Material.ROCK);
		this.setHardness(1.0F);
		this.setResistance(60.0F);
		this.setSoundType(SoundType.CLOTH);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_skylight");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}

	@Override
	public void breakBlock(World p_149749_1_, BlockPos pos, IBlockState state) {
		TileEntity te = p_149749_1_.getTileEntity(pos);
		if (te != null && te instanceof TileEntitySkyLight)
			((TileEntitySkyLight) te).onBreak();
		super.breakBlock(p_149749_1_, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntitySkyLight();
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntitySkyLight)
			((TileEntitySkyLight) te).onBreak();
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, BlockPos pos, IBlockState state, EntityLivingBase p_149689_5_,
			ItemStack p_149689_6_) {
		super.onBlockPlacedBy(p_149689_1_, pos, state, p_149689_5_, p_149689_6_);
		TileEntity te = p_149689_1_.getTileEntity(pos);
		if (te != null && te instanceof TileEntitySkyLight)
			((TileEntitySkyLight) te).onPlace();
	}

	@Override
	public void onNeighborChange(IBlockAccess p_149695_1_, BlockPos bp, BlockPos bpn) {
		if (p_149695_1_.getTileEntity(bp) != null)
			((TileEntitySkyLight) p_149695_1_.getTileEntity(bp)).forceUpdate();
	}
}
