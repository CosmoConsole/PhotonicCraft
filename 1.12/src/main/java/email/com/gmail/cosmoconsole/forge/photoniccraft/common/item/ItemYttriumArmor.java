package email.com.gmail.cosmoconsole.forge.photoniccraft.common.item;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemYttriumArmor extends ItemArmor {
	public String textureName;

	public ItemYttriumArmor(ArmorMaterial p_i45325_1_, int p_i45325_2_, EntityEquipmentSlot p_i45325_3_) {
		super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
	}

	public ItemYttriumArmor(String unlocalizedName, ArmorMaterial material, String textureName,
			EntityEquipmentSlot type) {
		super(material, 0, type);
		this.textureName = textureName;
		this.setUnlocalizedName(unlocalizedName);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return ModPhotonicCraft.MODID + ":textures/armor/" + this.textureName + "_"
				+ (this.armorType == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
	}
}
