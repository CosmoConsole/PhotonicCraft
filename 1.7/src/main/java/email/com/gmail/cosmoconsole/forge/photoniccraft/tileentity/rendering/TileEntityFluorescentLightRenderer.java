package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaser;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityFluorescentLightRenderer extends TileEntitySpecialRenderer {

	private final TileEntityFluorescentLightModel model;
    ResourceLocation textures = (new ResourceLocation("photoniccraft:textures/blocks/fluorescent_light_off.png")); 
    ResourceLocation textures_on = (new ResourceLocation("photoniccraft:textures/blocks/fluorescent_light_on.png")); 
    
    public TileEntityFluorescentLightRenderer() {
            this.model = new TileEntityFluorescentLightModel();
    }
	
	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double x,
			double y, double z, float scale) {
		GL11.glPushMatrix();
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glEnable(GL11.GL_BLEND); 
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(((TileEntityFluorescentLight)p_147500_1_).isPowered() ? textures_on : textures);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, 0.5f, 0.0f);
        GL11.glRotatef(180f, 1.0F, 0.0F, 0.0F);
        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
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
