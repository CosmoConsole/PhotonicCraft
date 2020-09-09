package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class TileEntityFluorescentLight extends TileEntity {
	public TileEntityFluorescentLight() {
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
	public boolean isPowered() {
		return this.powered;
	}
	boolean lastpowered = false;
	boolean powered = false;
    protected void updatePower() {
    	if ((this.worldObj.getTotalWorldTime() % 5L != 0L)) return;
    	lastpowered = powered;
    	powered = this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
    }
    public long lastTick = 0L;
    @Override
    public void updateEntity() {
    	if (this.worldObj.getTotalWorldTime() <= lastTick) return;
    	lastTick = this.worldObj.getTotalWorldTime();
    	updatePower();
    	if (powered != lastpowered)
    		this.updateLights(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		if (this.powered && ((this.worldObj.getTotalWorldTime() % 4L) == 0L)) {
			World w = this.worldObj;
			for (int x = -3; x <= 3; x++) {
				int ax = xCoord + x;
				for (int z = -3; z <= 3; z++) {
					int az = zCoord + z;
					for (int y = 0; y < 3; y++) { 
						int ay = yCoord - y;
				    	if (w.getBlock(ax, ay, az) == Blocks.air) {
				    		w.setBlock(ax, ay, az, ModPhotonicCraft.lightAir, 0, 3);
							//w.scheduleBlockUpdate(x, y, z, ModPhotonicCraft.lightAir, TileEntityLightAir.DEF_TICKS);
				    	} else if (w.getBlock(ax, ay, az) == ModPhotonicCraft.lightAir) {
				    		w.setBlock(ax, ay, az, Blocks.air, 0, 0);
				    		w.setBlock(ax, ay, az, ModPhotonicCraft.lightAir, 0, 3);
				    	} else if (w.getBlockLightOpacity(ax, ay, az) > 0)
				    		break;
					}
				}
			}
			/*this.rhombus(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 20,
					true, true, true, true, true);*/
		}
    }
    private void rhombus(World w, int x, int y, int z, int l, boolean N, boolean S, boolean W, boolean E, boolean D) {
		if (w.isRemote) return;
    	if (w.getBlock(x, y, z) == Blocks.air) {
    		w.setBlock(x, y, z, ModPhotonicCraft.lightAir, 0, 3);
    		((TileEntityLightAir)w.getTileEntity(x, y, z)).ticksLeft=30;
			//w.scheduleBlockUpdate(x, y, z, ModPhotonicCraft.lightAir, TileEntityLightAir.DEF_TICKS);
    	} else if (w.getBlock(x,  y, z) == ModPhotonicCraft.lightAir) {
    		w.setBlock(x, y, z, Blocks.air, 0, 0);
    		w.setBlock(x, y, z, ModPhotonicCraft.lightAir, 0, 3);
    		((TileEntityLightAir)w.getTileEntity(x, y, z)).ticksLeft=30;
    	}
    	if (N) {
    		int el = l - (1 + w.getBlockLightOpacity(x, y, z - 1));
    		if (el >= 16)
    			this.rhombus(w, x, y, z - 1, el, N, false, W, E, D);
    	}
    	if (S) {
    		int el = l - (1 + w.getBlockLightOpacity(x, y, z + 1));
    		if (el >= 16)
    			this.rhombus(w, x, y, z + 1, el, false, S, W, E, D);
    	}
    	if (W) {
    		int el = l - (1 + w.getBlockLightOpacity(x - 1, y, z));
    		if (el >= 16)
    			this.rhombus(w, x - 1, y, z, el, N, S, W, false, D);
    	}
    	if (E) {
    		int el = l - (1 + w.getBlockLightOpacity(x + 1, y, z));
    		if (el >= 16)
    			this.rhombus(w, x + 1, y, z, el, N, S, false, E, D);
    	}
    	if (D && y >= 1) {
    		int el = l - (1 + w.getBlockLightOpacity(x, y - 1, z));
    		if (el >= 16)
    			this.rhombus(w, x, y - 1, z, el, false, false, false, false, D);
    	}
	}
	public static final EnumSkyBlock lighttype = EnumSkyBlock.Block; 
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
        
        
        w.updateLightByType(lighttype, ix, iy +1, iz);
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
        w.updateLightByType(lighttype, ix, iy -1, iz -1);
	}
}
