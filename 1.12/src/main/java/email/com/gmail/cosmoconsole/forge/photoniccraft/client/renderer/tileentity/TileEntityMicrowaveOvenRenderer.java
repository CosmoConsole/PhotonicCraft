package email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMicrowaveOvenRenderer extends TileEntitySpecialRenderer<TileEntityMicrowaveOven> {
	final float[] yaws = { 0f, 0f, 180f, 0f, -90f, 90f };
	final int[] xoff = { 0, 0, 1, -1, 0, 0 };
	final int[] zoff = { 0, 0, 0, 0, -1, 1 };
	public final float TB = 0.07f;

	public TileEntityMicrowaveOvenRenderer() {
	}

	private void adjustRotatePivotViaMeta(World world, int x, int y, int z) {
		int meta = PhotonicUtils.readDirectionPropertyAsInteger(world.getBlockState(new BlockPos(x, y, z)),
				BlockHorizontal.FACING);
		GL11.glPushMatrix();
		GL11.glRotatef(meta * (-90), 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}

	@Override
	public void render(TileEntityMicrowaveOven t, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		if (t.entItem != null) {
			int m = PhotonicUtils.readDirectionPropertyAsInteger(t.getWorld().getBlockState(t.getPos()),
					BlockHorizontal.FACING);
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
			t.entItem.hoverStart = 0.0F;
			GL11.glTranslatef((float) x + 0.5F + TB * xoff[m], (float) y - 0.08f, (float) z + 0.5F + TB * zoff[m]);
			GL11.glRotatef((float) t.angle + yaws[m], 0.0f, 1.0f, 0.0f);
			GL11.glScalef(0.8f, 0.8f, 0.8f);
			Minecraft.getMinecraft().getRenderManager().renderEntity(t.entItem, 0, 0, 0, 0.0F, 0.0F, false);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
	}

}
