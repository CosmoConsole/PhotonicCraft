package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityRemoteReceiver extends TileEntity implements ITickable {
	public long channel = -1L;
	int lastRedstonePower = 0;
	long redstonePowerPushed = 0L;
	public long nonSimultaneous = 0L;
	int nonSimultaneousPow = -1;

	int lastpower = -1;

	public long lastclick = 0L;

	public int getPower() {
		if (this.getWorld().getTotalWorldTime() <= (this.redstonePowerPushed + 10L))
			return lastRedstonePower;
		return 0;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		writeSyncableDataToNBT(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		readSyncableDataFromNBT(tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readSyncableDataFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		channel = p_145839_1_.getLong("channel");
		lastRedstonePower = p_145839_1_.getInteger("lastRedstonePower");
		redstonePowerPushed = p_145839_1_.getLong("redstonePowerPushed");
	}

	private void readSyncableDataFromNBT(NBTTagCompound func_148857_g) {
		channel = func_148857_g.getLong("channel");
	}

	public void submitPower(int power, long relative) {
		if (relative <= nonSimultaneous && power >= nonSimultaneousPow)
			return;
		if (this.redstonePowerPushed == this.getWorld().getTotalWorldTime())
			return;
		nonSimultaneous = relative;
		nonSimultaneousPow = power;
		this.lastRedstonePower = power;
		this.redstonePowerPushed = this.getWorld().getTotalWorldTime();
	}

	@Override
	public void update() {
		int power = getPower();
		if (lastpower != power) {
			getWorld().notifyNeighborsOfStateChange(pos, blockType, true);
			lastpower = power;
		}
	}

	private void writeSyncableDataToNBT(NBTTagCompound syncData) {
		syncData.setLong("channel", channel);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		p_145841_1_.setLong("channel", channel);
		p_145841_1_.setInteger("lastRedstonePower", lastRedstonePower);
		p_145841_1_.setLong("redstonePowerPushed", redstonePowerPushed);
		return super.writeToNBT(p_145841_1_);

	}

	private int xCoord() {
		return this.pos.getX();
	}

	private int yCoord() {
		return this.pos.getY();
	}

	private int zCoord() {
		return this.pos.getZ();
	}
}
