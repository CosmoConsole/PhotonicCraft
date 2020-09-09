package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserSemiMirror;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityRedstoneMirrorRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation textures;
	private final TileEntityRedstoneMirrorModel model; 
    
    public TileEntityRedstoneMirrorRenderer(ResourceLocation t) {
    		textures = t;
            this.model = new TileEntityRedstoneMirrorModel();
    }
    
    private void adjustRotatePivotViaMeta(World world, int x, int y, int z) {
            int meta = world.getBlockMetadata(x, y, z);
            GL11.glPushMatrix();
            GL11.glRotatef(meta * (-90), 0.0F, 0.0F, 1.0F);
            GL11.glPopMatrix();
    }
	final float[] yaws = {0f, 0f, 0f, 180f, 90f, -90f};
	final float[] pitches = {-90f, 90f, 0f, 0f, 0f, 0f};
	final float[] rx = {  0f,  0f,  0f,  0f,180f,180f,  0f,  0f,
						  0f,  0f,  0f,  0f,180f,180f,  0f,  0f};
	final float[] ry = {  0f,  0f, 90f,  0f, 90f,  0f,  0f,  0f,
						 90f, 90f,  0f, 90f,180f, 90f,  0f,  0f};
	final float[] rz = {-90f, 90f,  0f,  0f,  0f,  0f,  0f,  0f,
						-90f, 90f,  0f,  0f,  0f,  0f,  0f,  0f};
	
	
	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double x,
			double y, double z, float scale) {
		GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(textures);
        GL11.glPushMatrix();
        TileEntityLaserMirror t = (TileEntityLaserMirror) p_147500_1_;
        int m = t.getWorldObj().getBlockMetadata(t.xCoord, t.yCoord, t.zCoord);
        GL11.glRotatef(-180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(rx[m], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(ry[m], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(rz[m], 0.0F, 0.0F, 1.0F);
        //GL11.glRotatef(yaws[m], 0.0F, 1.0F, 0.0F);
        //GL11.glRotatef(pitches[m], 1.0F, 0.0F, 0.0F);
        //GL11.glTranslatef(0.0F, (pitches[m]>=0F?0F:-1F), 0f);
        //if (pitches[m]<0f) GL11.glTranslatef(0f, 0f, 1f);
        //if (pitches[m]>0f) GL11.glTranslatef(0f, -1f, -1f);
        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
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
