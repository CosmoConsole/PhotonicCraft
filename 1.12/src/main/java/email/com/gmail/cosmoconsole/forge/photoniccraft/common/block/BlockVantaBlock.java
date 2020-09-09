package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockVantaBlock extends Block {

	public BlockVantaBlock(MapColor p_i45414_1_) {
		super(Material.GROUND, p_i45414_1_);
		this.setHardness(2.0F);
		this.setResistance(4.0F);
		this.setSoundType(SoundType.CLOTH);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_vantablock");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
	}

}
