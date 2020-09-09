package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.block.BlockOre;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlockMercuryOre extends BlockOre {
	public BlockMercuryOre() {
		super();
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypePiston);
		this.setBlockName(ModPhotonicCraft.MODID + "_mercuryOre");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":mercuryOre");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHarvestLevel("pickaxe", 2);
	}
	@Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
    	return ModPhotonicCraft.photonicResource;
    }
	@Override
    public int damageDropped(int p_149692_1_)
    {
        return 13;
    }
}
