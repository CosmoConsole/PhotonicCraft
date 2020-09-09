package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TileEntityMicrowaveOvenModel extends ModelBase
{
	  //fields
	    ModelRenderer Shape106;
	    ModelRenderer Shape12;
	    ModelRenderer Shape1434;
	    ModelRenderer Shape16;
	    ModelRenderer Shape1;
	    ModelRenderer Shape183;
	    ModelRenderer Shape1515;
	    ModelRenderer Shape1535;
	    ModelRenderer Shape14;
	    ModelRenderer Shape17;
	    ModelRenderer Shape15;
	  
	  public TileEntityMicrowaveOvenModel() 
	  {
	    textureWidth = 64;
	    textureHeight = 32;
	    
	      Shape106 = new ModelRenderer(this, 26, 23);
	      Shape106.addBox(0F, 0F, 0F, 10, 1, 8);
	      Shape106.setRotationPoint(3F, 7F, 5F);
	      Shape106.setTextureSize(64, 32);
	      Shape106.mirror = true;
	      setRotation(Shape106, 3.141593F, 1.570796F, 0F);
	      Shape12 = new ModelRenderer(this, 0, 0);
	      Shape12.addBox(0F, 0F, 0F, 14, 1, 12);
	      Shape12.setRotationPoint(-7F, 7F, -6F);
	      Shape12.setTextureSize(64, 32);
	      Shape12.mirror = true;
	      setRotation(Shape12, 0F, 0F, 0F);
	      Shape1434 = new ModelRenderer(this, 0, 0);
	      Shape1434.addBox(0F, 0F, 0F, 14, 1, 12);
	      Shape1434.setRotationPoint(-7F, -2F, -6F);
	      Shape1434.setTextureSize(64, 32);
	      Shape1434.mirror = true;
	      setRotation(Shape1434, 0F, 0F, 0F);
	      Shape16 = new ModelRenderer(this, 0, -2);
	      Shape16.addBox(0F, 0F, 0F, 1, 8, 10);
	      Shape16.setRotationPoint(-7F, -1F, -5F);
	      Shape16.setTextureSize(64, 32);
	      Shape16.mirror = true;
	      setRotation(Shape16, 0F, 0F, 0F);
	      Shape1 = new ModelRenderer(this, 0, -2);
	      Shape1.addBox(0F, 0F, 0F, 1, 8, 10);
	      Shape1.setRotationPoint(6F, -1F, -5F);
	      Shape1.setTextureSize(64, 32);
	      Shape1.mirror = true;
	      setRotation(Shape1, 0F, 0F, 0F);
	      Shape183 = new ModelRenderer(this, 0, 0);
	      Shape183.addBox(0F, 0F, 0F, 14, 8, 1);
	      Shape183.setRotationPoint(-7F, -1F, 5F);
	      Shape183.setTextureSize(64, 32);
	      Shape183.mirror = true;
	      setRotation(Shape183, 0F, 0F, 0F);
	      Shape1515 = new ModelRenderer(this, 0, 23);
	      Shape1515.addBox(0F, 0F, 0F, 12, 8, 1);
	      Shape1515.setRotationPoint(-6F, -1F, 4F);
	      Shape1515.setTextureSize(64, 32);
	      Shape1515.mirror = true;
	      setRotation(Shape1515, 0F, 0F, 0F);
	      Shape1535 = new ModelRenderer(this, 0, 23);
	      Shape1535.addBox(0F, 0F, 0F, 10, 8, 1);
	      Shape1535.setRotationPoint(-6F, -1F, 5F);
	      Shape1535.setTextureSize(64, 32);
	      Shape1535.mirror = true;
	      setRotation(Shape1535, 0F, 1.570796F, 0F);
	      Shape14 = new ModelRenderer(this, 0, 23);
	      Shape14.addBox(0F, 0F, 0F, 10, 8, 1);
	      Shape14.setRotationPoint(3F, -1F, 5F);
	      Shape14.setTextureSize(64, 32);
	      Shape14.mirror = true;
	      setRotation(Shape14, 0F, 1.570796F, 0F);
	      Shape17 = new ModelRenderer(this, 0, 21);
	      Shape17.addBox(0F, 0F, 0F, 10, 1, 10);
	      Shape17.setRotationPoint(-5F, -1F, 5F);
	      Shape17.setTextureSize(64, 32);
	      Shape17.mirror = true;
	      setRotation(Shape17, 0F, 1.570796F, 0F);
	      Shape15 = new ModelRenderer(this, 34, 0);
	      Shape15.addBox(0F, 0F, 0F, 1, 8, 14);
	      Shape15.setRotationPoint(-7F, -1F, -5F);
	      Shape15.setTextureSize(64, 32);
	      Shape15.mirror = true;
	      setRotation(Shape15, 0F, 1.570796F, 0F);
	  }
	  
	  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	  {
	    super.render(entity, f, f1, f2, f3, f4, f5);
	    setRotationAngles(entity, f, f1, f2, f3, f4, f5);
	    Shape106.render(f5);
	    Shape12.render(f5);
	    Shape1434.render(f5);
	    Shape16.render(f5);
	    Shape1.render(f5);
	    Shape183.render(f5);
	    Shape1515.render(f5);
	    Shape1535.render(f5);
	    Shape14.render(f5);
	    Shape17.render(f5);
	    Shape15.render(f5);
	  }
	  
	  private void setRotation(ModelRenderer model, float x, float y, float z)
	  {
	    model.rotateAngleX = x;
	    model.rotateAngleY = y;
	    model.rotateAngleZ = z;
	  }
	  
	  public void setRotationAngles(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	  {
	    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	  }

	}
