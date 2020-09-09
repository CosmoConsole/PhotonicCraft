package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockFloodlight;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class TileEntityFloodlight extends TileEntity {

	private final EnumSkyBlock lighttype = EnumSkyBlock.Block;
	public TileEntityFloodlight() {
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
    	if (this.worldObj.getTotalWorldTime() % 5L == 0L) {
    		illuminate();
    	}
    }
    boolean powered = false;
    boolean lastpowered = false;
    private void updatePower() {
    	if ((this.worldObj.getTotalWorldTime() % 40L != 0L)) return;
    	lastpowered = powered;
    	powered = this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
    }
    private void illuminate() {
		try {
			BlockFloodlight b = (BlockFloodlight) this.getWorldObj().getBlock(this.xCoord, this.yCoord, this.zCoord);
		} catch (Exception ex) {
			return;
		}
		int light = 15;
		this.updatePower();
		int dir = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		int x = this.xCoord, y = this.yCoord, z = this.zCoord;
		int d = 0;
		boolean update = false;
		while (d++ < 64) {
			if (dir == 0) y--;
			else if (dir == 1) y++;
			else if (dir == 2) z--;
			else if (dir == 3) z++;
			else if (dir == 4) x--;
			else if (dir == 5) x++;
			light -= this.worldObj.getBlockLightOpacity(x, y, z);
			if (light <= 0) {
				int lld = lll;
				lll = d;
				if (d >= lld) {
					break;
				}
				update = true;
			}
			if (update || (this.lastpowered != this.powered)) updateLights(worldObj, x, y, z);
			if (!update && this.powered) this.worldObj.setLightValue(lighttype, x, y, z, light);
			if (!this.lastpowered && this.powered) {
				this.worldObj.setLightValue(lighttype, x, y, z, light);
				updateLights(worldObj, x, y, z);
				this.worldObj.setLightValue(lighttype, x, y, z, light);
			} else if (!this.powered && this.lastpowered) {
				this.worldObj.setLightValue(lighttype, x, y, z, 0);
				updateLights(worldObj, x, y, z);
				this.worldObj.setLightValue(lighttype, x, y, z, 0);
			}
		}
		if (!update)
			lll = d;
	}
    
	public void onPlace() {
    	illuminate();
    }
	
	public void onBreak() {
    	powered = false;
    	lastpowered = true;
    	illuminate();
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
