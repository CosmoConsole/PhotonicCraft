package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityMicrowaveOven;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityMicrowaveOvenRenderer extends TileEntitySpecialRenderer {

	private final TileEntityMicrowaveOvenModel model;
    ResourceLocation textures = (new ResourceLocation("photoniccraft:textures/blocks/microwave.png")); 
    ResourceLocation textures2 = (new ResourceLocation("photoniccraft:textures/blocks/microwave_on.png")); 
    
    public TileEntityMicrowaveOvenRenderer() {
            this.model = new TileEntityMicrowaveOvenModel();
    }
    
    
    private void adjustRotatePivotViaMeta(World world, int x, int y, int z) {
            int meta = world.getBlockMetadata(x, y, z);
            GL11.glPushMatrix();
            GL11.glRotatef(meta * (-90), 0.0F, 0.0F, 1.0F);
            GL11.glPopMatrix();
    }
	final float[] yaws = {0f, 0f, 180f, 0f, 90f, -90f};
	final int[] xoff = {0, 0, 1, -1, 0, 0};
	final int[] zoff = {0, 0, 0, 0, 1, -1};
	public final float TB = 0.1f;
	
	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double x,
			double y, double z, float scale) {
		GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        TileEntityMicrowaveOven t = (TileEntityMicrowaveOven) p_147500_1_;
        if (t.isOn())
        	Minecraft.getMinecraft().renderEngine.bindTexture(textures2);
        else
        	Minecraft.getMinecraft().renderEngine.bindTexture(textures);
        GL11.glPushMatrix();
        int m = t.getWorldObj().getBlockMetadata(t.xCoord, t.yCoord, t.zCoord);
        GL11.glRotatef(yaws[m], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
        //GL11.glTranslatef(0.0F, (pitches[m]>=0F?0F:-1F), 0f);
        //if (pitches[m]<0f) GL11.glTranslatef(0f, 0f, 1f);
        //if (pitches[m]>0f) GL11.glTranslatef(0f, -1f, -1f);
        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        if (t.entItem != null) {
            GL11.glPushMatrix();
        	GL11.glDisable(GL11.GL_LIGHTING);
    	    t.entItem.hoverStart = 0.0F;
    	    RenderItem.renderInFrame = true;
    	    GL11.glTranslatef((float)x + 0.5F + TB * xoff[m], (float)y + 0.18f, (float)z + 0.5F + TB * zoff[m]);
    	    GL11.glRotatef((float) t.angle + yaws[m], 0.0f, 1.0f, 0.0f);
    	    GL11.glScalef(0.8f, 0.8f, 0.8f);
    	    RenderManager.instance.renderEntityWithPosYaw(t.entItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
    	    RenderItem.renderInFrame = false;
        	GL11.glEnable(GL11.GL_LIGHTING);
    	    GL11.glPopMatrix();
        }
	}
	
    private void adjustLightFixture(World world, int i, int j, int k, Block block) {
        Tessellator tess = Tessellator.instance;
        float brightness = block.getLightValue(world, i, j, k);
        int skyLight = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int modulousModifier = skyLight % 65536;
        int divModifier = skyLight / 65536;
        tess.setColorOpaque_F(brightness, brightness, brightness);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,  (float) modulousModifier,  divModifier);
    }

}
