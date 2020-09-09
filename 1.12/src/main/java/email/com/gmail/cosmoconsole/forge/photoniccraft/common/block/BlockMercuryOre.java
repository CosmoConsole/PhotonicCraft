package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockMercuryOre extends BlockOre {
	public BlockMercuryOre() {
		super();
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_mercuryore");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random p_149650_2_, int p_149650_3_) {
		return PhotonicItems.photonicResources[13];
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		int i = random.nextInt(fortune + 2) - 1;
		if (i < 0) {
			i = 0;
		}
		return this.quantityDropped(random) * (i + 1);
	}
}
