package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import dan200.computercraft.ComputerCraft.Blocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSnow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMicrowaveGenerator extends TileEntity implements IEnergyProvider, IEnergyReceiver {
	private static final double PI133 = Math.PI * (4d / 3);
	private static final double TC = 1d / 12000;
	private static final int RF_PER_BUILD_BLOCK = 40;
	boolean fullCheckNext = false;
	boolean partialCheckNext = false;
	double min = 0.0;
	double max = 0.0;
	boolean producingPower = false;
	int radius = 0;
	public double output = 0;
	public long powered = 0;
	public static final long maxpower = 18432000;
	public static final long peringot = 288000;
	EnergyStorage internalEnergy;
	public TileEntityMicrowaveGenerator() {
		this.internalEnergy = new EnergyStorage(10000000);
	}
	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        readSyncableDataFromNBT(p_145839_1_);
        radius = p_145839_1_.getInteger("radius");
        min = p_145839_1_.getDouble("minPower");
        max = p_145839_1_.getDouble("maxPower");
        producingPower = p_145839_1_.getBoolean("powered");
        fullCheckNext = p_145839_1_.getBoolean("shouldFull");
        partialCheckNext = p_145839_1_.getBoolean("shouldPartial");
        output = p_145839_1_.getDouble("output");
        internalEnergy.setEnergyStored(p_145839_1_.getInteger("energy"));
        powered = p_145839_1_.getLong("powered");
    }
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        this.writeSyncableDataToNBT(syncData);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }
    
    private void writeSyncableDataToNBT(NBTTagCompound p_145841_1_) {
        p_145841_1_.setInteger("radius", radius);
        p_145841_1_.setDouble("minPower", min);
        p_145841_1_.setDouble("maxPower", max);
        p_145841_1_.setBoolean("powered", producingPower);
        p_145841_1_.setBoolean("shouldFull", fullCheckNext);
        p_145841_1_.setBoolean("shouldPartial", partialCheckNext);
        p_145841_1_.setDouble("output", output);
        p_145841_1_.setInteger("energy", this.internalEnergy.getEnergyStored());
        p_145841_1_.setLong("powered", powered);
	}

	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readSyncableDataFromNBT(pkt.func_148857_g());
    }
	private void readSyncableDataFromNBT(NBTTagCompound p_145839_1_) {
        radius = p_145839_1_.getInteger("radius");
        min = p_145839_1_.getDouble("minPower");
        max = p_145839_1_.getDouble("maxPower");
        producingPower = p_145839_1_.getBoolean("powered");
        fullCheckNext = p_145839_1_.getBoolean("shouldFull");
        partialCheckNext = p_145839_1_.getBoolean("shouldPartial");
        output = p_145839_1_.getDouble("output");
        internalEnergy.setEnergyStored(p_145839_1_.getInteger("energy"));
        powered = p_145839_1_.getLong("powered");
	}
	@Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        writeSyncableDataToNBT(p_145841_1_);
        p_145841_1_.setInteger("radius", radius);
        p_145841_1_.setDouble("minPower", min);
        p_145841_1_.setDouble("maxPower", max);
        p_145841_1_.setBoolean("powered", producingPower);
        p_145841_1_.setBoolean("shouldFull", fullCheckNext);
        p_145841_1_.setBoolean("shouldPartial", partialCheckNext);
        p_145841_1_.setDouble("output", output);
        p_145841_1_.setInteger("energy", this.internalEnergy.getEnergyStored());
        p_145841_1_.setLong("powered", powered);
    }
    @Override
    public void validate() {
    	super.validate();
        
        this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, ModPhotonicCraft.microwaveGenerator, 10);
    }
	@Override
	public void updateEntity() {
		if (this.fullCheckNext)
			doFullCheck();
		if (this.partialCheckNext)
			doPartialCheck();
		if (this.worldObj.getTotalWorldTime() % 12000L == 0L)
			triggerFullCheck();
		else if (this.worldObj.getTotalWorldTime() % 40L == 0L)
			triggerPartialCheck();
		if (this.worldObj.getTotalWorldTime() % 5L == 0L)
			this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, (this.producingPower && this.output > 0) ? 1 : 0, 3);
		if (this.producingPower) {
			if (this.worldObj.getTotalWorldTime() % 4L == 0L) {
				this.calculateNewOutput();
			}
			if (this.output > 0) {
				this.internalEnergy.setEnergyStored(Math.min(this.internalEnergy.getMaxEnergyStored(), this.internalEnergy.getEnergyStored() + (int)output));
				if (this.powered > 0)
					this.powered--;
			}
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			if ((internalEnergy.getEnergyStored() > 0)) {
				for (int i = 0; i < 6; i++){
					TileEntity tile = worldObj.getTileEntity(xCoord + ForgeDirection.getOrientation(i).offsetX, yCoord + ForgeDirection.getOrientation(i).offsetY, zCoord + ForgeDirection.getOrientation(i).offsetZ);
					if (tile != null && tile instanceof IEnergyHandler) {
						internalEnergy.extractEnergy(((IEnergyHandler)tile).receiveEnergy(ForgeDirection.getOrientation(i).getOpposite(), internalEnergy.extractEnergy(internalEnergy.getMaxExtract(), true), false), false);
					}
				}
			}
		} else if (this.isBuilding() && (this.worldObj.getTotalWorldTime() % 4L == 0L) && (this.internalEnergy.getEnergyStored() >= RF_PER_BUILD_BLOCK)) {
			TileEntityChest tec = (TileEntityChest) this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
			// loop over slots and remove one panel if found
			boolean panel = false;
			int slot = -1;
			Item ib = Item.getItemFromBlock(ModPhotonicCraft.microwavePanel);
			for (int i = 0; i < tec.getSizeInventory(); i++) {
				ItemStack s = tec.getStackInSlot(i);
				if (s == null) continue;
				if (s.getItem() == ib) {
					slot = i;
					panel = true;
					break;
				}
			}
			// if panel found, find first block that doesn't conform to sphere
			if (panel) {
				boolean build = this.buildSphere(1) > 0;
				if (build) {
					this.internalEnergy.setEnergyStored(this.internalEnergy.getEnergyStored() - RF_PER_BUILD_BLOCK);
					tec.decrStackSize(slot, 1);
				}
			}
		}
	}
	
	private void calculateNewOutput() {
		if (this.worldObj.isRemote) return;
		double g = PhotonicAPI.rand.nextGaussian() / 2;
		if (g < -5.0) g = -5.0;
		else if (g > 5.0) g = 5.0;
		g = (g + 5.0) / 10.0;
		this.output = this.min + (g * (this.max - this.min));
		if (this.powered == 0)
			this.output = 0;
		//System.out.println("Min: " + min + " RF, Max: " + max + " RF, Out: " + output + " RF, Day: " + getDaylight(this.worldObj));
	}
	public boolean isRunning() {
		return this.producingPower;
	}
	public void triggerFullCheck() {
		this.fullCheckNext = true;
	}
	public void triggerPartialCheck() {
		this.partialCheckNext = true;
	}
	public boolean isBuilding() {
		try {
			return this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord) instanceof TileEntityChest;
		} catch (Exception ex) {
			return false;
		}
	}
	public void doPartialCheck() {
		// turn off if no sunlight
		int r = this.radius;
		int y = this.yCoord + 1;
		if (y == this.worldObj.getActualHeight() - 1) return;
		for (int x_ = -r; x_ <= r; x_++) {
			if (!this.producingPower) break;
  			for (int z_ = -r; z_ <= r; z_++) {
  				if (!this.producingPower) break;
				if (roundedDist2(x_,z_)>r) continue;
				int x = x_ + this.xCoord;
				int z = z_ + this.zCoord;
				if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) < 15) {
					System.out.println("Partial mismatch: " + x + "," + y + "," + z);
					this.producingPower = false;
					break;
				}
			}
		}
		if (this.producingPower) {
			this.min = r * 10;
			this.max = 16 * (PI133*r*r*r*Math.sqrt(Math.sqrt(1-(getDaylight(this.worldObj)/2))));
			this.calculateNewOutput();
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}
		int cy = this.yCoord, ir = 1;
		while (--cy >= 0 && this.worldObj.getBlock(this.xCoord, cy, this.zCoord) == net.minecraft.init.Blocks.iron_block)
			ir++;
		this.partialCheckNext = false;
		if (ir != this.radius)
			this.triggerFullCheck();
	}
	private double getDaylight(World worldObj) {
		long t = worldObj.getWorldTime() % 24000;
		//System.out.println(t);
		if (t >= 0 && t <= 6000) {
			return 0.5 + TC * t;
		} else if (t >= 18000 && t < 24000) {
			return TC * (t - 18000);
		} else if (t > 6000 && t < 18000) {
			return TC * (18000 - t);
		}
		return 0;
	}
	private int roundedDist2(int x_, int z_) {
		return (int) Math.round(Math.sqrt(x_*x_+z_*z_));
	}
	private int roundedDist3(int x_, int y_, int z_) {
		return (int) Math.round(Math.sqrt(x_*x_+y_*y_+z_*z_));
	}
	public void doFullCheck() {
		// turn on if dome, turn off if no dome
		this.producingPower = false;
		int cy = this.yCoord, ir = 1;
		while (--cy >= 0 && this.worldObj.getBlock(this.xCoord, cy, this.zCoord) == net.minecraft.init.Blocks.iron_block)
			ir++;
		if (ir < 4) {
			//System.out.println("Radius below minimum: " + ir);
			return;
		}
		this.radius = ir;
		boolean allMatch = true;
		for (int x = this.xCoord - ir; x <= this.xCoord + ir; x++) {
			if (!allMatch) break;
			for (int z = this.zCoord - ir; z <= this.zCoord + ir; z++) {
				if (!allMatch) break;
				for (int y = this.yCoord; y >= this.yCoord - ir; y--) {
					if (!allMatch) break;
					int dx = x - this.xCoord, dy = y - this.yCoord, dz = z - this.zCoord;
					if (dx == 0 && dz == 0) continue;
					int d = roundedDist3(dx,dy,dz);
					if (d > ir) continue;
					if (d == ir) {
						if (this.worldObj.getBlock(x, y, z) != ModPhotonicCraft.microwavePanel) {
							//System.out.println("Full mismatch (should be panel): " + x + "," + y + "," + z);
							allMatch = false;
							break;
						}
					} else if (d < ir) {
						if (this.worldObj.getBlockLightOpacity(x,y,z) > 0) {
							//System.out.println("Full mismatch (should be air): " + x + "," + y + "," + z);
							allMatch = false;
							break;
						}
					}
				}
			}
		}
		if (allMatch)
			this.producingPower = true;
		this.fullCheckNext = false;
		doPartialCheck();
	}
	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) {
		return true;
	}
	@Override
	public int extractEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		int e = internalEnergy.extractEnergy(arg1, arg2);
		//System.out.println("Asking " + arg1 + " RF, sim: " + arg2 + " ::: giving " + e + " RF");
		return e;
	}
	@Override
	public int getEnergyStored(ForgeDirection arg0) {
		return internalEnergy.getEnergyStored();
	}
	@Override
	public int getMaxEnergyStored(ForgeDirection arg0) {
		return internalEnergy.getMaxEnergyStored();
	}
	public int buildSphere(int maxBlocks) {
		int cy = this.yCoord, ir = 1;
		while (--cy >= 0 && this.worldObj.getBlock(this.xCoord, cy, this.zCoord) == net.minecraft.init.Blocks.iron_block)
			ir++;
		if (ir < 4) {
			//System.out.println("Radius below minimum: " + ir);
			return 0;
		}
		this.radius = ir;
		int blocksPlaced = 0;
		for (int x = this.xCoord - ir; x <= this.xCoord + ir; x++) {
			for (int z = this.zCoord - ir; z <= this.zCoord + ir; z++) {
				for (int y = this.yCoord; y >= this.yCoord - ir; y--) {
					int dx = x - this.xCoord, dy = y - this.yCoord, dz = z - this.zCoord;
					if (dx == 0 && dz == 0) continue;
					int d = roundedDist3(dx,dy,dz);
					if (d > ir) continue;
					if (d == ir) {	
						if (this.worldObj.getBlock(x, y, z).isReplaceable(this.worldObj, x, y, z)) {
							this.worldObj.setBlock(x, y, z, ModPhotonicCraft.microwavePanel);
							blocksPlaced++;
							if (blocksPlaced >= maxBlocks) return blocksPlaced;
						}
					}
				}
			}
		}
		return blocksPlaced;
	}
	@Override
	public int receiveEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		if (this.producingPower)
			return 0;
		if (!this.isBuilding())
			return 0;
		return internalEnergy.receiveEnergy(arg1, arg2);
	}
}
