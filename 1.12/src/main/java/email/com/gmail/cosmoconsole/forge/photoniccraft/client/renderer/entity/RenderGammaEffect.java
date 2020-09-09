package email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityGammaEffect;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGammaEffect extends Render<EntityGammaEffect> {
	private static final int GL_FUNC_REVERSE_SUBTRACT = 32779;
	protected ModelGammaEffect modelLaser;

	public RenderGammaEffect(RenderManager rnm) {
		super(rnm);
		this.shadowSize = 0.0F;
		this.shadowOpaque = 0.0F;
		this.modelLaser = new ModelGammaEffect();
	}

	@Override
	public void doRender(EntityGammaEffect p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		float rx = (float)(Math.random() * 0.025) - 0.0125f;
		float ry = (float)(Math.random() * 0.025) - 0.0125f;
		float rz = (float)(Math.random() * 0.025) - 0.0125f;
		GL11.glTranslatef((float) p_76986_2_ + rx, (float) p_76986_4_ + ry, (float) p_76986_6_ + rz);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		this.bindEntityTexture(p_76986_1_);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.ONE_MINUS_DST_COLOR, DestFactor.ONE_MINUS_SRC_COLOR);
		GlStateManager.color(p_76986_1_.getAlpha(), p_76986_1_.getAlpha(), p_76986_1_.getAlpha(), p_76986_1_.getAlpha());
		this.modelLaser.render(p_76986_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F);
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGammaEffect p_110775_1_) {
		return new ResourceLocation("photoniccraft:textures/white.png");
	}

}