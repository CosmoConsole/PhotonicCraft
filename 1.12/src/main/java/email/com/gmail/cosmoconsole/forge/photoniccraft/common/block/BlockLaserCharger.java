package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserCharger;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLaserCharger extends BlockContainer {

	public BlockLaserCharger() {
		super(Material.ROCK);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_lasercharger");
		this.setHarvestLevel("pickaxe", 0);
		this.setLightOpacity(0);
	}

	@Override
	public void breakBlock(World p_149749_1_, BlockPos bp, IBlockState bs) {
		Random r = new Random();
		TileEntity te = p_149749_1_.getTileEntity(bp);
		if (te != null && te instanceof TileEntityLaserCharger) {
			TileEntityLaserCharger t = (TileEntityLaserCharger) te;
			InventoryHelper.spawnItemStack(p_149749_1_, bp.getX(), bp.getY(), bp.getZ(),
					t.getInvSlot().getStackInSlot(0));
		}
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityLaserCharger();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess p_149709_1_, BlockPos pos, EnumFacing side) {
		return 0;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess p_149709_1_, BlockPos pos, EnumFacing side) {
		return ((TileEntityLaserCharger) p_149709_1_.getTileEntity(pos)).chargedLastTick ? 15 : 0;
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
			TileEntityLaserCharger tileentitylaser = (TileEntityLaserCharger) p_149727_1_.getTileEntity(bp);

			if (tileentitylaser != null) {
				p_149727_5_.openGui(ModPhotonicCraft.instance, 0, p_149727_1_, bp.getX(), bp.getY(), bp.getZ());
			}

			return true;
		}
	}

}