package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockHalogenLamp extends Block {

	public BlockHalogenLamp() {
		super(Material.GLASS, MapColor.SNOW);
		this.setHardness(0.5F);
		this.setSoundType(SoundType.GLASS);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_halogen");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 15;
	}
	
}
