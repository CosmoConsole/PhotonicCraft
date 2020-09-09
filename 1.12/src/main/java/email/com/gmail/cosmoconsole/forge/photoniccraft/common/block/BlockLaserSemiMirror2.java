package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockLaserSemiMirror2 extends BlockLaserMirror {

	public BlockLaserSemiMirror2() {
		super();
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_lasersemimirror2");
	}

	@Override
	public Item getItemDropped(IBlockState state, Random p_149650_2_, int p_149650_3_) {
		return PhotonicItems.laserItems[4];
	}
}
