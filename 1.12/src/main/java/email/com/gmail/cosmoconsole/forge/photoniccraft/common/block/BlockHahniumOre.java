package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;

public class BlockHahniumOre extends BlockOre {
	public BlockHahniumOre() {
		super();
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_hahniumore");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setHarvestLevel("pickaxe", 1);
		this.setLightLevel(5 / 16f);
	}
}
