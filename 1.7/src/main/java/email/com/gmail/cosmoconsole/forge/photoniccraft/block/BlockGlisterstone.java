package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityGlisterstone;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntitySkyLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGlisterstone extends Block implements ITileEntityProvider {
	public BlockGlisterstone() {
		super(Material.glass);
		this.setHardness(5.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeGlass);
		this.setBlockName(ModPhotonicCraft.MODID + "_glisterstone");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":glisterstone");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setLightOpacity(0);
	}
	@Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityGlisterstone();
    }
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		try {
			TileEntity te = world.getTileEntity(x, y, z);
			return ((TileEntityGlisterstone)te).getLight();
		} catch (Exception ex) {
			return 0;
		}
	}
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}
