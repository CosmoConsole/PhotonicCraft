package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLaserMerger extends TileEntity {
	public long laserTick = -1L;
	public int recLaser = 0;
	public int laserColor = 0;
	public int laserPower = 0;
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
}
