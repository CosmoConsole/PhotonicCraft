package email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserPointer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLaserPointer extends Render<EntityLaserPointer> {
	protected ModelBase modelLaser;

	public RenderLaserPointer(RenderManager rnm) {
		super(rnm);
		this.shadowSize = 0.0F;
		this.shadowOpaque = 0.0F;
		this.modelLaser = new ModelLaserPointer();
	}

	@Override
	public void doRender(EntityLaserPointer p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		this.bindEntityTexture(p_76986_1_);
		GlStateManager.color(1.0f, 0.0f, 0.0f, 1.0F);
		this.modelLaser.render(p_76986_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLaserPointer p_110775_1_) {
		return new ResourceLocation("photoniccraft:textures/white.png");
	}

}