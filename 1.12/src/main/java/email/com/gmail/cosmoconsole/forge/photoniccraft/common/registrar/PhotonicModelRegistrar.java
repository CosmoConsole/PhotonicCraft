package email.com.gmail.cosmoconsole.forge.photoniccraft.common.registrar;

import java.util.ArrayList;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.block.PrismBakedModel;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockPrism;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.item.ItemPhotonicCoupler;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.NameAndItem;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.model.ModelLoader;

/**
 * PhotonicCraft's models for items and item blocks as well as other possible models are registered here.
 */
public class PhotonicModelRegistrar {
	public static ArrayList<NameAndItem> modelItemList = new ArrayList<>();

	public static void registerModels() {
		for (NameAndItem it : PhotonicItems.blockItemList)
			ModelLoader.setCustomModelResourceLocation(it.getItem(), 0,
					new ModelResourceLocation(it.getItem().getRegistryName(), "inventory"));
		for (NameAndItem it : modelItemList)
			ModelLoader.setCustomModelResourceLocation(it.getItem(), 0,
					new ModelResourceLocation(it.getItem().getRegistryName(), "inventory"));
		for (int i = 0; i < 16; ++i)
			ModelLoader.setCustomModelResourceLocation(PhotonicItems.photonicCoupler, i,
					new ModelResourceLocation(
							PhotonicItems.photonicCoupler.getRegistryName() + "_" + ItemPhotonicCoupler.colors[i],
							"inventory"));
		PhotonicItems.blockItemList.clear();
		modelItemList.clear();
	}
	
	public static void bakePrismModels(IRegistry<ModelResourceLocation, IBakedModel> registry) {
		StateMapperBase b = new DefaultStateMapper();
		PrismBakedModel.glassSprite = Minecraft.getMinecraft().getTextureMapBlocks()
				.getAtlasSprite(new ResourceLocation(ModPhotonicCraft.MODID_MINECRAFT, "blocks/glass").toString());
		PrismBakedModel.prismSprite = Minecraft.getMinecraft().getTextureMapBlocks()
				.registerSprite(new ResourceLocation(ModPhotonicCraft.MODID, "blocks/prism_top"));
		PrismBakedModel.initTextures();
		for (IBlockState validState : PhotonicBlocks.prism.getBlockState().getValidStates()) {
			String variant = b.getPropertyString(validState.getProperties());
			ModelResourceLocation prismLoc = new ModelResourceLocation(PhotonicBlocks.prism.getRegistryName(), variant);
			registry.putObject(prismLoc,
					new PrismBakedModel(registry.getObject(prismLoc),
							(new int[] { 0, 0, 2, 0, 3, 1 })[PhotonicUtils.readDirectionPropertyAsInteger(validState,
									BlockPrism.FACING)]));
		}
	}

	private PhotonicModelRegistrar() {
	}
}
