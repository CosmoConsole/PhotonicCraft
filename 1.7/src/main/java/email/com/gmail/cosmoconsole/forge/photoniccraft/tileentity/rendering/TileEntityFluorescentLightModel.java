package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TileEntityFluorescentLightModel extends ModelBase {
    ModelRenderer Shape21;
    ModelRenderer Shape22;
    ModelRenderer Shape23;
    ModelRenderer Shape2;
    ModelRenderer Shape31;
    ModelRenderer Shape32;
    ModelRenderer Shape3;
	public TileEntityFluorescentLightModel() {
    textureWidth = 32;
    textureHeight = 32;
    
      Shape21 = new ModelRenderer(this, 0, 0);
      Shape21.addBox(0F, 0F, 0F, 14, 1, 1);
      Shape21.setRotationPoint(-7F, 0F, -8F);
      Shape21.setTextureSize(32, 32);
      Shape21.mirror = true;
      setRotation(Shape21, 0F, 0F, 0F);
      Shape22 = new ModelRenderer(this, 0, 0);
      Shape22.addBox(0F, 0F, 0F, 12, 1, 1);
      Shape22.setRotationPoint(-6F, 1F, -8F);
      Shape22.setTextureSize(32, 32);
      Shape22.mirror = true;
      setRotation(Shape22, 0F, 0F, 0F);
      Shape23 = new ModelRenderer(this, 0, 0);
      Shape23.addBox(0F, 0F, 0F, 12, 1, 1);
      Shape23.setRotationPoint(-6F, 1F, 7F);
      Shape23.setTextureSize(32, 32);
      Shape23.mirror = true;
      setRotation(Shape23, 0F, 0F, 0F);
      Shape2 = new ModelRenderer(this, 0, 0);
      Shape2.addBox(0F, 0F, 0F, 14, 1, 1);
      Shape2.setRotationPoint(-7F, 0F, 7F);
      Shape2.setTextureSize(32, 32);
      Shape2.mirror = true;
      setRotation(Shape2, 0F, 0F, 0F);
      Shape31 = new ModelRenderer(this, 0, 17);
      Shape31.addBox(0F, 0F, 0F, 1, 1, 14);
      Shape31.setRotationPoint(-0.5F, 0.5F, -7F);
      Shape31.setTextureSize(32, 32);
      Shape31.mirror = true;
      setRotation(Shape31, 0F, 0F, 0F);
      Shape32 = new ModelRenderer(this, 0, 17);
      Shape32.addBox(0F, 0F, 0F, 1, 1, 14);
      Shape32.setRotationPoint(3F, 0.5F, -7F);
      Shape32.setTextureSize(32, 32);
      Shape32.mirror = true;
      setRotation(Shape32, 0F, 0F, 0F);
      Shape3 = new ModelRenderer(this, 0, 17);
      Shape3.addBox(0F, 0F, 0F, 1, 1, 14);
      Shape3.setRotationPoint(-4F, 0.5F, -7F);
      Shape3.setTextureSize(32, 32);
      Shape3.mirror = true;
      setRotation(Shape3, 0F, 0F, 0F);
  }
	@Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Shape21.render(f5);
    Shape22.render(f5);
    Shape23.render(f5);
    Shape2.render(f5);
    Shape31.render(f5);
    Shape32.render(f5);
    Shape3.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }
}
