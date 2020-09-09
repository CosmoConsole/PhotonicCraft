package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;

public class TileEntityLightDetector extends TileEntity {
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
    public int light = -1;
    public long ticks = 0;
    public int lastlight = -1;
    @Override
    public void updateEntity() {
    	ticks++;
    	if ((ticks&1) == 0) {
    		this.lastlight = this.light;
    		this.light = this.worldObj.getBlockLightValue(this.xCoord, this.yCoord, this.zCoord);
    		if (this.lastlight != this.light)
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
    	}
    }
}
