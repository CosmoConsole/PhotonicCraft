package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRemoteReceiver extends TileEntity {
	public long channel = -1L;
	int lastRedstonePower = 0;
	long redstonePowerPushed = 0L;
	public long nonSimultaneous = 0L;
	int nonSimultaneousPow = -1;
	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        channel = p_145839_1_.getLong("channel");
        lastRedstonePower = p_145839_1_.getInteger("lastRedstonePower");
        redstonePowerPushed = p_145839_1_.getLong("redstonePowerPushed");
        /*nonSimultaneous = p_145839_1_.getLong("nonSimultaneous");
        nonSimultaneousPow = p_145839_1_.getInteger("nonSimultaneousPow");*/
    }
	@Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setLong("channel", channel);
        p_145841_1_.setInteger("lastRedstonePower", lastRedstonePower);
        p_145841_1_.setLong("redstonePowerPushed", redstonePowerPushed);
        /*p_145841_1_.setLong("nonSimultaneous", nonSimultaneous);
        p_145841_1_.setInteger("nonSimultaneousPow", nonSimultaneousPow);*/
        
    }
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        this.writeSyncableDataToNBT(syncData);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }
    
    private void writeSyncableDataToNBT(NBTTagCompound syncData) {
		syncData.setLong("channel",channel);
	}

	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readSyncableDataFromNBT(pkt.func_148857_g());
    }
	private void readSyncableDataFromNBT(NBTTagCompound func_148857_g) {
		channel = func_148857_g.getLong("channel");
	}
	public int getPower() {
		if (this.worldObj.getTotalWorldTime() <= (this.redstonePowerPushed + 10L))
			return lastRedstonePower;
		return 0;
	}
	int lastpower = -1;
	public long lastclick = 0L;
	@Override
	public void updateEntity() {
		int power = getPower();
		if (lastpower != power) {
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
			lastpower = power;
		}
	}
	public void submitPower(int power, long relative) {
		System.out.println("Submitting " + power + " on time " + relative);
		if (relative <= nonSimultaneous && power >= nonSimultaneousPow) return;
		if (this.redstonePowerPushed == this.worldObj.getTotalWorldTime())
			return;
		nonSimultaneous = relative;
		nonSimultaneousPow = power;
		this.lastRedstonePower = power;
		this.redstonePowerPushed = this.worldObj.getTotalWorldTime();
	}
}
