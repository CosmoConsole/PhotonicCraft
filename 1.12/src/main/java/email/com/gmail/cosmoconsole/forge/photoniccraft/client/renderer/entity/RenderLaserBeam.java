package email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserBeam;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLaserBeam extends Render<EntityLaserBeam> {
	protected ModelBase modelLaser;

	public RenderLaserBeam(RenderManager rnm) {
		super(rnm);
		this.shadowSize = 0.0F;
		this.shadowOpaque = 0.0F;
		this.modelLaser = new ModelLaserBeam();
	}

	@Override
	public void doRender(EntityLaserBeam p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		if (p_76986_1_.puny)
			return;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GlStateManager.disableCull();
		GlStateManager.disableFog();
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
		GL11.glRotatef(180.0F - p_76986_1_.getPermanentYaw(), 0.0F, 1.0F, 0.0F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		if (p_76986_1_.vertical) {
			GL11.glRotatef((p_76986_1_.getEntityWorld().getTotalWorldTime() % 720) * 0.5f, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(0.25F * p_76986_1_.scale, (p_76986_1_.yneg ? 1f : -1f) * 0.4F * p_76986_1_.blocklen,
					0.25F * p_76986_1_.scale);
		} else {
			GL11.glRotatef((p_76986_1_.getEntityWorld().getTotalWorldTime() % 720) * 0.5f, 0.0F, 0.0F, 1.0F);
			GL11.glScalef(0.25F * p_76986_1_.scale, 0.25F * p_76986_1_.scale, 0.4F * p_76986_1_.blocklen);
		}
		float f1 = 1.0f / 255.0f;
		int c1 = p_76986_1_.color;
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.color(f1 * (c1 >> 16 & 0xFF), f1 * (c1 >> 8 & 0xFF), f1 * (c1 & 0xFF), p_76986_1_.alpha);
		((ModelLaserBeam) this.modelLaser).setVertical(p_76986_1_.vertical);
		this.modelLaser.render(p_76986_1_, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.enableFog();
		GlStateManager.enableCull();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLaserBeam p_110775_1_) {
		return new ResourceLocation("photoniccraft:textures/white.png");
	}

	@Override
	public boolean shouldRender(EntityLaserBeam livingEntity, ICamera camera, double camX, double camY, double camZ) {
		return livingEntity.isInRangeToRender3d(camX, camY, camZ);
	}

}