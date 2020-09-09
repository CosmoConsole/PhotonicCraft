package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLaserDetector extends TileEntity {
	public int ticks = 0;
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
    public void setTicks(int t) {
    	ticks = t;
    }
    @Override
    public void updateEntity() {
    	if (ticks > 0) {
    		ticks--;
    		if (ticks <= 0) {
    			ticks = 0;
    			worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
    		}
    	}
    }
}
