package email.com.gmail.cosmoconsole.forge.photoniccraft.entity.rendering;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserBeam;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLaserBeam extends Render
{
    protected ModelBase modelLaser;

    public RenderLaserBeam() {
    	this.shadowSize = 0.0F;
    	this.shadowOpaque = 0.0F;
        this.modelLaser = new ModelLaserBeam();
    }

    public void doRender(EntityLaserBeam p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        //super.doRender((EntityLivingBase)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    	if (p_76986_1_.puny) return;
    	GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        //GL11.glDisable(GL11.GL_LIGHT);
        GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
        GL11.glRotatef(180.0F - p_76986_8_, 0.0F, 1.0F, 0.0F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        this.bindEntityTexture(p_76986_1_);
        if (p_76986_1_.vertical) {
            GL11.glRotatef((p_76986_1_.worldObj.getWorldTime() % 360)*1f, 0.0F, 1.0F, 0.0F);
        	GL11.glScalef(0.25F * p_76986_1_.scale, (p_76986_1_.yneg ? -1f : 1f) * 0.4F * p_76986_1_.blocklen, 0.25F * p_76986_1_.scale);
        } else {
            GL11.glRotatef((p_76986_1_.worldObj.getWorldTime() % 360)*1f, 0.0F, 0.0F, 1.0F);
        	GL11.glScalef(0.25F * p_76986_1_.scale, 0.25F * p_76986_1_.scale, 0.4F * p_76986_1_.blocklen);
        }
        float f1 = 1.0f / 255.0f;
        int c1 = p_76986_1_.color;
        GL11.glColor4f(f1*(c1>>16&0xFF),f1*(c1>>8&0xFF),f1*(c1&0xFF),1.0F);
        ((ModelLaserBeam) this.modelLaser).setVertical(p_76986_1_.vertical);
        this.modelLaser.render(p_76986_1_, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityLaserBeam)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return new ResourceLocation("photoniccraft:textures/white.png");
	}

}