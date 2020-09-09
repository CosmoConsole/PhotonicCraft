package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TileEntityPrismModel extends ModelBase {
	//fields
    ModelRenderer Shape1;
    ModelRenderer Shape3;
    ModelRenderer Shape2;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
  
  public TileEntityPrismModel()
  {
	  textureWidth = 32;
	    textureHeight = 32;
	    
	      Shape1 = new ModelRenderer(this, -16, -16);
	      Shape1.addBox(0F, 0F, 0F, 0, 16, 16);
	      Shape1.setRotationPoint(-7F, -8F, 8F);
	      Shape1.setTextureSize(32, 32);
	      Shape1.mirror = true;
	      setRotation(Shape1, 0F, -3.141593F, 0F);
	      Shape3 = new ModelRenderer(this, -16, -16);
	      Shape3.addBox(0F, 0F, 0F, 0, 16, 16);
	      Shape3.setRotationPoint(7F, -8F, 0F);
	      Shape3.setTextureSize(32, 32);
	      Shape3.mirror = true;
	      setRotation(Shape3, 0F, -1.047198F, 0F);
	      Shape2 = new ModelRenderer(this, -16, -16);
	      Shape2.addBox(0F, 0F, 0F, 0, 16, 16);
	      Shape2.setRotationPoint(-7F, -8F, -8F);
	      Shape2.setTextureSize(32, 32);
	      Shape2.mirror = true;
	      setRotation(Shape2, 0F, 1.047198F, 0F);
	      Shape4 = new ModelRenderer(this, -30, 17);
	      Shape4.addBox(0F, 0F, 0F, 16, 0, 14);
	      Shape4.setRotationPoint(-7F, -8F, -8F);
	      Shape4.setTextureSize(32, 32);
	      Shape4.mirror = true;
	      setRotation(Shape4, -3.141593F, -1.570796F, 0F);
	      Shape5 = new ModelRenderer(this, -30, 17);
	      Shape5.addBox(0F, 0F, 0F, 16, 0, 14);
	      Shape5.setRotationPoint(-7F, 8F, 8F);
	      Shape5.setTextureSize(32, 32);
	      Shape5.mirror = true;
	      setRotation(Shape5, 0F, 1.570796F, 0F);
  }
  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(entity, f, f1, f2, f3, f4, f5);
    Shape1.render(f5);
    Shape3.render(f5);
    Shape2.render(f5);
    Shape4.render(f5);
    Shape5.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
  }
}
