package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class ItemPhotonic2 extends Item {
	public static final String[] field_150921_b = new String[] {"magnetron","tantalumIngot","tantalumCapacitor","hahniumIngot","antenna","gyrotron","bariumIngot"};
	@SideOnly(Side.CLIENT)
	private IIcon[] field_150920_d;
	public ItemPhotonic2() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName(ModPhotonicCraft.MODID + "_itemResource2");
        this.setTextureName(ModPhotonicCraft.MODID + ":itemResource2");
        this.setCreativeTab(CreativeTabs.tabMaterials);
	}
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {
        for (int i = 0; i < field_150921_b.length; ++i)
        {
            p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
        this.field_150920_d = new IIcon[field_150921_b.length];

        for (int i = 0; i < field_150921_b.length; ++i)
        {
            this.field_150920_d[i] = p_94581_1_.registerIcon(this.getIconString() + "_" + field_150921_b[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        int j = MathHelper.clamp_int(p_77617_1_, 0, 15);
        return this.field_150920_d[j];
    }

    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        int i = MathHelper.clamp_int(p_77667_1_.getItemDamage(), 0, 15);
        return "item." + ModPhotonicCraft.MODID + "_itemResource2." + field_150921_b[i];
    }
}
