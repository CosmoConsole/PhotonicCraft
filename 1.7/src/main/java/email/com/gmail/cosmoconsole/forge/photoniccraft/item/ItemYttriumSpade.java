package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.item.ItemSpade;

public class ItemYttriumSpade extends ItemSpade {
	public ItemYttriumSpade(ToolMaterial material) {
        super(material);
	}
	public ItemYttriumSpade(String unlocalizedName, ToolMaterial material) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(ModPhotonicCraft.MODID + ":" + unlocalizedName.replace(ModPhotonicCraft.MODID+"_",""));
	}
}
