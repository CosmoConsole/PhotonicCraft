package email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserEffect;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLaserEffect extends Render<EntityLaserEffect> {
	protected ModelBase modelLaser;

	public RenderLaserEffect(RenderManager rnm) {
		super(rnm);
		this.shadowSize = 0.0F;
		this.shadowOpaque = 0.0F;
		this.modelLaser = new ModelLaserEffect();
	}

	@Override
	public void doRender(EntityLaserEffect p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
		GL11.glRotatef(180.0F - p_76986_8_, 0.0F, 1.0F, 0.0F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		this.bindEntityTexture(p_76986_1_);
		float f1 = 1.0f / 255.0f;
		int c1 = p_76986_1_.color;
		GlStateManager.color(f1 * (c1 >> 16 & 0xFF), f1 * (c1 >> 8 & 0xFF), f1 * (c1 & 0xFF), 1.0F);
		GL11.glRotatef((float) p_76986_1_.rotX, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef((float) p_76986_1_.rotY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef((float) p_76986_1_.rotZ, 0.0F, 0.0F, 1.0F);
		this.modelLaser.render(p_76986_1_, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLaserEffect p_110775_1_) {
		return new ResourceLocation("photoniccraft:textures/white.png");
	}

}