package email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLaserEffect extends ModelBase {
	public ModelRenderer[] l = new ModelRenderer[2];

	public ModelLaserEffect() {
		this.l[0] = new ModelRenderer(this, 0, 0);
		this.l[0].addBox(-0.5F, -0.5F, -10F, 1, 1, 20, 0.2F);
		this.l[0].setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		// super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.l[0].render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}