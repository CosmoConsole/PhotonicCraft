package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.item.ItemAxe;

public class ItemYttriumAxe extends ItemAxe {
	public ItemYttriumAxe(ToolMaterial material) {
        super(material);
	}
	public ItemYttriumAxe(String unlocalizedName, ToolMaterial material) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(ModPhotonicCraft.MODID + ":" + unlocalizedName.replace(ModPhotonicCraft.MODID+"_",""));
	}
}
