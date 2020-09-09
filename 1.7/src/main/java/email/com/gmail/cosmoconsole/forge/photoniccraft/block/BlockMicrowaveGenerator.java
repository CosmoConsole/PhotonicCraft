package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityMicrowaveGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockMicrowaveGenerator extends Block implements ITileEntityProvider {
	public BlockMicrowaveGenerator() {
        super(Material.rock);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setStepSound(soundTypeMetal);
		this.setBlockName(ModPhotonicCraft.MODID + "_microwaveGenerator");
		this.setBlockTextureName(ModPhotonicCraft.MODID + ":microwaveGenerator");
	}

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        /*int k = p_149691_2_ & 7;
    	return p_149691_1_ == k ? (k != 1 && k != 0 ? this.field_149935_N : this.field_149936_O) : (k != 1 && k != 0 ? (p_149691_1_ != 1 && p_149691_1_ != 0 ? this.blockIcon : this.field_149934_M) : ((k == 1 && p_149691_1_ == 0) || (k == 0 && p_149691_1_ == 1) ? this.field_149934_M : this.blockIcon));*/
    	return p_149691_2_ > 0 ? field_149935_N : field_149934_M;
    }

    @SideOnly(Side.CLIENT)
    private IIcon field_149934_M;
    @SideOnly(Side.CLIENT)
    private IIcon field_149935_N;
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("photoniccraft:microwaveGenerator");
        this.field_149934_M = p_149651_1_.registerIcon("photoniccraft:microwaveGenerator");
        this.field_149935_N = p_149651_1_.registerIcon("photoniccraft:microwaveGenerator_on");
    }
    
    @Override
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
    		EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_1_.isRemote) return true;
	    TileEntityMicrowaveGenerator t = ((TileEntityMicrowaveGenerator) p_149727_1_.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_));
	    if (t.isRunning()) {
	    	if (p_149727_5_.getHeldItem() != null && p_149727_5_.getHeldItem().getItem() == ModPhotonicCraft.photonicResource2 && p_149727_5_.getHeldItem().getItemDamage() == 3) {
	    		int give_max = p_149727_5_.getHeldItem().stackSize;
	    	    if (p_149727_5_.isSneaking()) give_max = 1;
	    		int take_max = (int)((t.maxpower - t.powered) / t.peringot);
	    		int giving = Math.min(take_max, give_max);
	    		if (giving < 1) {
	    			p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_microwaveGenerator.full"));
	    		} else {
	    			if (t.powered < 0)
	    				p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_microwaveGenerator.enabled"));
	    			t.powered += giving * t.peringot;
	    			p_149727_5_.inventory.decrStackSize(p_149727_5_.inventory.currentItem, giving);
	    		}
	    		return true;
	    	}
    	    if (p_149727_5_.isSneaking()) return false;
	    	p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_microwaveGenerator.output",Integer.toString((int)t.output)));
	    	p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_microwaveGenerator.time",String.format("%02d",(t.powered / 72000L)),String.format("%02d",(t.powered / 1200L) % 60),String.format("%02d",(t.powered / 20L) % 60)));
	    	return true;
	    }
	    if (p_149727_5_.isSneaking()) return false;
	    t.doFullCheck();
	    if (!t.isRunning())
	    	p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_microwaveGenerator.invalid"));
	    else if (t.powered < 1)
	    	p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_microwaveGenerator.enterHahnium"));
	    else
	    	p_149727_5_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_microwaveGenerator.enabled"));
	    return true;
    }
    
	@Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityMicrowaveGenerator();
    }
}
