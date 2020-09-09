package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class TileEntitySkyLight extends TileEntity {

	private final EnumSkyBlock lighttype = EnumSkyBlock.Sky;
	public TileEntitySkyLight() {
		super();
	}
	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
    }
	@Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
    }
    private int lll = 0;
    @Override
    public void updateEntity() {
    	//if (this.worldObj.getTotalWorldTime() % 10L == 0L) {
    	if (!worldObj.provider.hasNoSky) {
    		int x = this.xCoord;
    		int z = this.zCoord;
    		
            int asl = getSkyLight();
			int sl = asl;
    		for (int y = this.yCoord - 1; y >= 0; y--) {
    			asl -= this.worldObj.getBlockLightOpacity(x, y, z);
    			if (asl <= 0) return;
    			if ((this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) != asl && (this.worldObj.getTotalWorldTime() % 10L == 0L)) || (updating)) {
    				updateLights(worldObj, x, y, z);
    				updating = false;
    			}
    			this.worldObj.setLightValue(lighttype, x, y, z, asl);
    		}
    		handleTicks();
    		if (lll != sl) setLastSunlight(sl);
    	}
    }
    private void setLastSunlight(int sl) {
		lll = sl;
		ticks = 0;
		ticking = true;
		updating = false;
	}
	int ticks = 0;
    boolean ticking = false;
    boolean updating = false;
    private void handleTicks() {
		if (ticking) {
			if (ticks == 10) {
				ticking = false;
				updating = true;
				return;
			}
			ticks++;
		}
	}
	private int getSkyLight() {
    	int i1 = worldObj.getSavedLightValue(EnumSkyBlock.Sky, this.xCoord, 256, this.zCoord) - worldObj.skylightSubtracted;
        float f = worldObj.getCelestialAngleRadians(1.0F);

        if (f < (float)Math.PI)
        {
            f += (0.0F - f) * 0.2F;
        }
        else
        {
            f += (((float)Math.PI * 2F) - f) * 0.2F;
        }

        i1 = Math.round((float)i1 * MathHelper.cos(f));

        if (i1 < 0)
        {
            i1 = 0;
        }

        if (i1 > 15)
        {
            i1 = 15;
        }
        return i1;
	}
	public void onPlace() {
    	int x = this.xCoord;
    	int z = this.zCoord;
    	int sl = this.getSkyLight();
    	int asl = 15;
		for (int y = this.yCoord - 1; y >= 0; y--) {
			asl -= this.worldObj.getBlockLightOpacity(x, y, z);
			if (asl <= 0) return;
			this.worldObj.setLightValue(lighttype, x, y, z, asl);
			updateLights(worldObj, x, y, z);
		}
    }
	public void onBreak() {
    	int x = this.xCoord;
    	int z = this.zCoord;
    	int sl = this.getSkyLight();
    	int asl = 15;
    	for (int y = 255; y > this.yCoord; y--) {
    		if (sl <= 0) break;
    		sl -= this.worldObj.getBlockLightOpacity(x, y, z);
    	}
    	if (sl < 0) sl = 0;
		for (int y = this.yCoord - 1; y >= 0; y--) {
			asl -= this.worldObj.getBlockLightOpacity(x, y, z);
			if (asl <= 0) return;
			this.worldObj.setLightValue(lighttype, x, y, z, sl);
			updateLights(worldObj, x, y, z);
			worldObj.updateLightByType(lighttype, x, y, z);
		}
    }
    public void updateLights(World w, int x, int y, int z) {
    	int ix = (int)x, iy = (int)y, iz = (int)z;
    	
        w.markBlockRangeForRenderUpdate(ix, iy, iz, 12, 12, 12);
        w.markBlockForUpdate(ix, iy, iz);

        w.updateLightByType(lighttype, ix, iy, iz);
        w.updateLightByType(lighttype, ix +1, iy, iz);
        w.updateLightByType(lighttype, ix +1, iy, iz +1);
        w.updateLightByType(lighttype, ix +1, iy, iz -1);
        w.updateLightByType(lighttype, ix -1, iy, iz +1);
        w.updateLightByType(lighttype, ix -1, iy, iz -1);
        w.updateLightByType(lighttype, ix -1, iy, iz);
        w.updateLightByType(lighttype, ix, iy, iz +1);
        w.updateLightByType(lighttype, ix, iy, iz -1);
        
        
        /*w.updateLightByType(lighttype, ix, iy +1, iz);
        w.updateLightByType(lighttype, ix +1, iy +1, iz);
        w.updateLightByType(lighttype, ix +1, iy +1, iz +1);
        w.updateLightByType(lighttype, ix +1, iy +1, iz -1);
        w.updateLightByType(lighttype, ix -1, iy +1, iz +1);
        w.updateLightByType(lighttype, ix -1, iy +1, iz -1);
        w.updateLightByType(lighttype, ix -1, iy +1, iz);
        w.updateLightByType(lighttype, ix, iy +1, iz +1);
        w.updateLightByType(lighttype, ix, iy +1, iz -1);
        w.updateLightByType(lighttype, ix, iy -1, iz);
        w.updateLightByType(lighttype, ix +1, iy -1, iz);
        w.updateLightByType(lighttype, ix +1, iy -1, iz +1);
        w.updateLightByType(lighttype, ix +1, iy -1, iz -1);
        w.updateLightByType(lighttype, ix -1, iy -1, iz +1);
        w.updateLightByType(lighttype, ix -1, iy -1, iz -1);
        w.updateLightByType(lighttype, ix -1, iy -1, iz);
        w.updateLightByType(lighttype, ix, iy -1, iz +1);
        w.updateLightByType(lighttype, ix, iy -1, iz -1);*/
	}
}
