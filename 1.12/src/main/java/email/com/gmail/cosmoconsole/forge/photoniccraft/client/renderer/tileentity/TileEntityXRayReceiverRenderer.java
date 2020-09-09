package email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityXRayReceiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityXRayReceiverRenderer extends TileEntitySpecialRenderer<TileEntityXRayReceiver> {

	public TileEntityXRayReceiverRenderer() {
	}

	@Override
	public void render(TileEntityXRayReceiver t, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		if (t.isPowered() && t.entItem != null) {
			GL11.glPushMatrix();
			t.entItem.hoverStart = 0.0F;
			GL11.glTranslatef((float) x + 0.5F, (float) y + 0.125F, (float) z + 0.5F);
			GL11.glRotatef((float) t.angle, 0.0f, 1.0f, 0.0f);
			Minecraft.getMinecraft().getRenderManager().renderEntity(t.entItem, 0, 0, 0, 0.0F, 0.0F, false);
			GL11.glPopMatrix();
		}
	}

}
