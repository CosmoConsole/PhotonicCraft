package email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityColossalCreeper;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderColossalCreeper extends RenderLiving<EntityColossalCreeper> {
	private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation(
			"textures/entity/creeper/creeper.png");

	public RenderColossalCreeper(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelCreeper(0.0F), 5.0F);
		this.addLayer(new LayerColossalCreeperCharge(this));
	}

	/**
	 * Gets an RGBA int color multiplier to apply.
	 */
	@Override
	protected int getColorMultiplier(EntityColossalCreeper entitylivingbaseIn, float lightBrightness,
			float partialTickTime) {
		float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);

		if ((int) (f * 10.0F) % 2 == 0) {
			return 0;
		} else {
			int i = (int) (f * 0.2F * 255.0F);
			i = MathHelper.clamp(i, 0, 255);
			return i << 24 | 822083583;
		}
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityColossalCreeper entity) {
		return CREEPER_TEXTURES;
	}

	/**
	 * Allows the render to do state modifications necessary before the model is
	 * rendered.
	 */
	@Override
	protected void preRenderCallback(EntityColossalCreeper entitylivingbaseIn, float partialTickTime) {
		float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
		float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		GlStateManager.scale(f2 * 6.0f, f3 * 6.0f, f2 * 6.0f);
	}
}