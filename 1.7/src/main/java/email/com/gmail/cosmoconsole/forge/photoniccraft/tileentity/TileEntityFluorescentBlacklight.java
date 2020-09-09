package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityFluorescentBlacklight extends TileEntityFluorescentLight {

    public void updateEntity() {
    	if (this.worldObj.getTotalWorldTime() <= lastTick) return;
    	lastTick = this.worldObj.getTotalWorldTime();
    	updatePower();
    	if (powered != lastpowered)
    		this.updateLights(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		if (this.powered && ((this.worldObj.getTotalWorldTime() % 4L) == 0L)) {
			World w = this.worldObj;
			for (Object e: this.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(this.xCoord - 2, this.yCoord - 5, this.zCoord - 2, this.xCoord + 2, this.yCoord, this.zCoord + 2))) {
				EntityItem ei = (EntityItem)e;
				if (Math.random() < 0.02 && ei != null && ei.getEntityItem() != null) {
					
				}
			}
			/*for (int x = -2; x <= 2; x++) {
				int ax = xCoord + x;
				for (int z = -2; z <= 2; z++) {
					int az = zCoord + z;
					for (int y = 0; y < 5; y++) { 
						int ay = yCoord - y;
				    	if (w.getBlock(ax, ay, az) == Blocks.air) {
				    		
				    	} else if (w.getBlockLightOpacity(ax, ay, az) > 0)
				    		break;
					}
				}
			}*/
			
			/*this.rhombus(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 20,
					true, true, true, true, true);*/
		}
    }
}
