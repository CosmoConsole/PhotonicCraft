package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TileEntityRedstoneMirrorModel extends ModelBase {
	  //fields
    ModelRenderer Shape1;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape2;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape51;
    ModelRenderer Shape32;
    ModelRenderer Shape31;
    ModelRenderer Shape33;
    ModelRenderer Shape11;
  
  public TileEntityRedstoneMirrorModel()
  {
    textureWidth = 64;
    textureHeight = 64;
    
      Shape1 = new ModelRenderer(this, 0, 2);
      Shape1.addBox(-7F, -8F, 7F, 14, 1, 1);
      Shape1.setRotationPoint(0F, 0F, 0F);
      Shape1.setTextureSize(64, 64);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 3.141593F, 0F);
      Shape3 = new ModelRenderer(this, 32, 13);
      Shape3.addBox(-7F, -7F, 7F, 14, 14, 1);
      Shape3.setRotationPoint(0F, 0F, 0F);
      Shape3.setTextureSize(64, 64);
      Shape3.mirror = true;
      setRotation(Shape3, 1.570796F, 3.141593F, 0F);
      Shape4 = new ModelRenderer(this, 0, 0);
      Shape4.addBox(-7F, 7F, -8F, 14, 1, 1);
      Shape4.setRotationPoint(0F, 0F, 0F);
      Shape4.setTextureSize(64, 64);
      Shape4.mirror = true;
      setRotation(Shape4, 0F, 0F, 0F);
      Shape5 = new ModelRenderer(this, 0, 2);
      Shape5.addBox(-7F, 7F, 7F, 14, 1, 5);
      Shape5.setRotationPoint(0F, 0F, -4F);
      Shape5.setTextureSize(64, 64);
      Shape5.mirror = true;
      setRotation(Shape5, 0F, 0F, 0F);
      //Shape2 = new ModelRenderer(this, 0, 34);
      //      Shape2.addBox(0F, -8F, -7F, 1, 16, 14);
      //Shape2.setRotationPoint(0F, 0F, 0F);
      //Shape2.setTextureSize(64, 64);
      //Shape2.mirror = true;
      //setRotation(Shape2, 0F, -1.570796F, 0.7853982F);
      Shape6 = new ModelRenderer(this, 0, 0);
      Shape6.addBox(7F, -8F, -8F, 1, 16, 16);
      Shape6.setRotationPoint(0F, 0F, 0F);
      Shape6.setTextureSize(64, 64);
      Shape6.mirror = true;
      setRotation(Shape6, 0F, 0F, 0F);
      Shape7 = new ModelRenderer(this, 0, 0);
      Shape7.addBox(-8F, -8F, -8F, 1, 16, 16);
      Shape7.setRotationPoint(0F, 0F, 0F);
      Shape7.setTextureSize(64, 64);
      Shape7.mirror = true;
      setRotation(Shape7, 0F, 0F, 0F);
      Shape51 = new ModelRenderer(this, 0, 1);
      Shape51.addBox(-7F, 7F, 7F, 14, 1, 10);
      Shape51.setRotationPoint(0F, 0F, -14F);
      Shape51.setTextureSize(64, 64);
      Shape51.mirror = true;
      setRotation(Shape51, 0F, 0F, 0F);
      Shape32 = new ModelRenderer(this, 0, 0);
      Shape32.addBox(-7F, -8F, -8F, 14, 1, 1);
      Shape32.setRotationPoint(0F, 0F, 0F);
      Shape32.setTextureSize(64, 64);
      Shape32.mirror = true;
      setRotation(Shape32, 0F, 0F, 0F);
      Shape31 = new ModelRenderer(this, 32, -1);
      Shape31.addBox(-7F, -7F, 7F, 14, 14, 1);
      Shape31.setRotationPoint(0F, 0F, 0F);
      Shape31.setTextureSize(64, 64);
      Shape31.mirror = true;
      setRotation(Shape31, 3.141593F, 3.141593F, 0F);
      Shape33 = new ModelRenderer(this, 32, -1);
      Shape33.addBox(-7F, -7F, 7F, 14, 14, 1);
      Shape33.setRotationPoint(0F, 0F, 0F);
      Shape33.setTextureSize(64, 64);
      Shape33.mirror = true;
      setRotation(Shape33, 0F, 3.141593F, 0F);
      Shape11 = new ModelRenderer(this, 0, 2);
      Shape11.addBox(-7F, -8F, 7F, 14, 1, 1);
      Shape11.setRotationPoint(0F, 0F, 0F);
      Shape11.setTextureSize(64, 64);
      Shape11.mirror = true;
      setRotation(Shape11, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Shape1.render(f5);
    Shape3.render(f5);
    Shape4.render(f5);
    Shape5.render(f5);
    //Shape2.render(f5);
    Shape6.render(f5);
    Shape7.render(f5);
    Shape51.render(f5);
    Shape32.render(f5);
    Shape31.render(f5);
    Shape33.render(f5);
    Shape11.render(f5);
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
