package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFluorescentLight;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAnalogRedstoneLamp extends Block {
	public BlockAnalogRedstoneLamp() {
		super(Material.redstoneLight);
		this.setHardness(0.3F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeGlass);
		this.setBlockName(ModPhotonicCraft.MODID + "_analogLamp");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":analoglamp");
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}
    @SideOnly(Side.CLIENT)
    private static IIcon onIcon;
    @SideOnly(Side.CLIENT)
    private static IIcon offIcon;
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    	this.onIcon = iconRegister.registerIcon(ModPhotonicCraft.MODID + ":analoglampon");
        this.offIcon = iconRegister.registerIcon(ModPhotonicCraft.MODID + ":analoglamp");
    }
	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		int maxPowerLevel = 0;
		for (int i = 0; i < 6; i++)
			maxPowerLevel = Math.max(maxPowerLevel, p_149695_1_.getIndirectPowerLevelTo(p_149695_2_, p_149695_3_, p_149695_4_, i));
		p_149695_1_.setBlockMetadataWithNotify(p_149695_2_, p_149695_3_, p_149695_4_, maxPowerLevel, 3);
	}
    @Override
    public IIcon getIcon(int side, int meta) {

        if (meta > 0) {
            return onIcon;
        } else {
            return offIcon;
        }
    }
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}
    @Override
    public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_)
    {
        return 0;
    }
    @Override
    public int damageDropped(int p_149692_1_)
    {
        return 0;
    }
}
