package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;

public class ItemYttriumIngot extends Item {
	public ItemYttriumIngot() {
        super();
        this.setUnlocalizedName("photoniccraft_yttrium_ingot");
        this.setTextureName(ModPhotonicCraft.MODID + ":yttriumIngot".replace(ModPhotonicCraft.MODID+"_",""));
	}
}
