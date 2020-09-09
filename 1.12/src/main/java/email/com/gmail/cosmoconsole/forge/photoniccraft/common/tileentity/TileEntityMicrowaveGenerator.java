package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockMicrowaveGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EnergyContainer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityMicrowaveGenerator extends TileEntity implements ITickable {
	private static final double PI133 = Math.PI * (4d / 3);
	private static final double TC = 1d / 12000;
	private static final int RF_PER_BUILD_BLOCK = 2000;
	private static final double F = 0.75;
	public static final long maxpower = 18432000;
	public static final long peringot = 288000;
	public static final int MAX_RF_CAPACITY = 10000000;
	public static final int MAX_BUILD_RF_CAPACITY = 1000;
	boolean fullCheckNext;
	boolean partialCheckNext;
	double min;
	double max;
	boolean producingPower;
	int radius;
	public double output;
	public long powered;
	EnergyContainer festorage;
	EnergyContainer festorageBuild;

	long lastTick = 0;

	public TileEntityMicrowaveGenerator() {
		this.fullCheckNext = this.partialCheckNext = this.producingPower = false;
		this.min = this.max = this.output = 0.0;
		this.powered = this.radius = 0;
		this.festorage = new EnergyContainer(MAX_RF_CAPACITY, MAX_RF_CAPACITY, 1000);
		this.festorageBuild = new EnergyContainer(MAX_BUILD_RF_CAPACITY);
	}

	public void addExtraTime(long l) {
		this.powered += l;
		this.markDirty();
		this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(pos), this.world.getBlockState(pos), 3);
		this.triggerFullCheck();
	}

	public int buildSphere(int maxBlocks) {
		int cy = this.yCoord(), ir = 1;
		while (--cy >= 0 && this.getWorld().getBlockState(new BlockPos(this.xCoord(), cy, this.zCoord()))
				.getBlock() == Blocks.IRON_BLOCK)
			ir++;
		if (ir < 4 || ir > 50) {
			return 0;
		}
		this.radius = ir;
		int blocksPlaced = 0;
		for (int x = this.xCoord() - ir; x <= this.xCoord() + ir; x++) {
			for (int z = this.zCoord() - ir; z <= this.zCoord() + ir; z++) {
				for (int y = this.yCoord(); y >= this.yCoord() - ir; y--) {
					int dx = x - this.xCoord(), dy = y - this.yCoord(), dz = z - this.zCoord();
					if (dx == 0 && dz == 0)
						continue;
					int d = roundedDist3(dx, dy, dz);
					if (d > ir)
						continue;
					if (d == ir) {
						if (this.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock()
								.isReplaceable(this.getWorld(), new BlockPos(x, y, z))) {
							this.getWorld().setBlockState(new BlockPos(x, y, z),
									PhotonicBlocks.microwavePanel.getDefaultState());
							blocksPlaced++;
							if (blocksPlaced >= maxBlocks)
								return blocksPlaced;
						}
					}
				}
			}
		}
		return blocksPlaced;
	}

	private void calculateNewOutput() {
		if (this.getWorld().isRemote)
			return;
		double g = PhotonicUtils.rand.nextGaussian() / 2;
		if (g < -5.0)
			g = -5.0;
		else if (g > 5.0)
			g = 5.0;
		g = (g + 5.0) / 10.0;
		this.output = this.min + (g * (this.max - this.min));
		if (this.powered == 0)
			this.output = 0;
	}

	public void doFullCheck() {
		// turn on if dome, turn off if no dome
		this.producingPower = false;
		int cy = this.yCoord(), ir = 1;
		while (--cy >= 0 && this.getWorld().getBlockState(new BlockPos(this.xCoord(), cy, this.zCoord()))
				.getBlock() == Blocks.IRON_BLOCK)
			ir++;
		if (ir < 4 || ir > 50) {
			return;
		}
		this.radius = ir;
		boolean allMatch = true;
		for (int x = this.xCoord() - ir; x <= this.xCoord() + ir; x++) {
			if (!allMatch)
				break;
			for (int z = this.zCoord() - ir; z <= this.zCoord() + ir; z++) {
				if (!allMatch)
					break;
				for (int y = this.yCoord(); y >= this.yCoord() - ir; y--) {
					if (!allMatch)
						break;
					int dx = x - this.xCoord(), dy = y - this.yCoord(), dz = z - this.zCoord();
					if (dx == 0 && dz == 0)
						continue;
					int d = roundedDist3(dx, dy, dz);
					if (d > ir)
						continue;
					if (d == ir) {
						if (this.getWorld().getBlockState(new BlockPos(x, y, z))
								.getBlock() != PhotonicBlocks.microwavePanel) {
							allMatch = false;
							break;
						}
					} else if (d < ir) {
						if (this.getWorld().getBlockLightOpacity(new BlockPos(x, y, z)) > 0) {
							allMatch = false;
							break;
						}
					}
				}
			}
		}
		this.producingPower = allMatch;
		this.fullCheckNext = false;
		doPartialCheck();
	}

	public void doPartialCheck() {
		// turn off if no sunlight
		int r = this.radius;
		int y = this.yCoord() + 1;
		if (y != this.getWorld().getActualHeight() - 1) {
			for (int x_ = -r; x_ <= r; x_++) {
				if (!this.producingPower)
					break;
				for (int z_ = -r; z_ <= r; z_++) {
					if (!this.producingPower)
						break;
					if (roundedDist2(x_, z_) > r)
						continue;
					int x = x_ + this.xCoord();
					int z = z_ + this.zCoord();
					if (this.getWorld().getLightFor(EnumSkyBlock.SKY, new BlockPos(x, y, z)) < 15) {
						this.producingPower = false;
						break;
					}
				}
			}
		}
		if (this.producingPower) {
			this.min = F * r / 3.0;
			this.max = F * 0.5 * (PI133 * r * r * r * Math.sqrt(Math.sqrt(1 - (getDaylight(this.getWorld()) / 2))));
			if (this.max < this.min)
				this.max = this.min;
			this.calculateNewOutput();
			getWorld().notifyNeighborsOfStateChange(pos, blockType, true);
		}
		int cy = this.yCoord(), ir = 1;
		while (--cy >= 0 && this.getWorld().getBlockState(new BlockPos(this.xCoord(), cy, this.zCoord()))
				.getBlock() == net.minecraft.init.Blocks.IRON_BLOCK)
			ir++;
		this.partialCheckNext = false;
		if (ir != this.radius)
			this.triggerFullCheck();
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (this.world != null) {
				;
				this.world.markChunkDirty(this.pos, this);
			}
			return CapabilityEnergy.ENERGY.cast(this.festorageBuild);
		}
		return super.getCapability(capability, facing);
	}

	private double getDaylight(World worldObj) {
		long t = getWorld().getWorldTime() % 24000;
		if (t >= 0 && t <= 6000) {
			return 0.5 + TC * t;
		} else if (t >= 18000 && t < 24000) {
			return TC * (t - 18000);
		} else if (t > 6000 && t < 18000) {
			return TC * (18000 - t);
		}
		return 0;
	}

	public int getTrueEnergyStored() {
		return Math.min(MAX_RF_CAPACITY, this.festorage.getEnergyStored());
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
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}

	public boolean isBuilding() {
		TileEntity te = this.getWorld().getTileEntity(this.pos.add(0, 1, 0));
		if (te != null)
			return te instanceof TileEntityChest;
		return false;
	}

	public boolean isRunning() {
		return this.producingPower;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readSyncableDataFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		readSyncableDataFromNBT(p_145839_1_);
		radius = p_145839_1_.getInteger("radius");
		min = p_145839_1_.getDouble("minpower");
		max = p_145839_1_.getDouble("maxpower");
		producingPower = p_145839_1_.getBoolean("poweredon");
		fullCheckNext = p_145839_1_.getBoolean("shouldfull");
		partialCheckNext = p_145839_1_.getBoolean("shouldpartial");
		output = p_145839_1_.getDouble("output");
		powered = p_145839_1_.getLong("hahnium");
		festorage.setEnergyStored(p_145839_1_.getInteger("fenergy"));
		festorageBuild.setEnergyStored(p_145839_1_.getInteger("fenergybuild"));
	}

	private void readSyncableDataFromNBT(NBTTagCompound p_145839_1_) {
		radius = p_145839_1_.getInteger("radius");
		min = p_145839_1_.getDouble("minpower");
		max = p_145839_1_.getDouble("maxpower");
		producingPower = p_145839_1_.getBoolean("poweredon");
		fullCheckNext = p_145839_1_.getBoolean("shouldfull");
		partialCheckNext = p_145839_1_.getBoolean("shouldpartial");
		output = p_145839_1_.getDouble("output");
		powered = p_145839_1_.getLong("hahnium");
		festorage.setEnergyStored(p_145839_1_.getInteger("fenergy"));
		festorageBuild.setEnergyStored(p_145839_1_.getInteger("fenergybuild"));
	}

	private int roundedDist2(int x_, int z_) {
		return (int) Math.round(Math.sqrt(x_ * x_ + z_ * z_));
	}

	private int roundedDist3(int x_, int y_, int z_) {
		return (int) Math.round(Math.sqrt(x_ * x_ + y_ * y_ + z_ * z_));
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return (oldState.getBlock() != newSate.getBlock());
	}

	public void triggerFullCheck() {
		this.fullCheckNext = true;
	}

	public void triggerPartialCheck() {
		this.partialCheckNext = true;
	}

	@Override
	public void update() {
		long thisTick = this.getWorld().getTotalWorldTime();
		if (lastTick == thisTick)
			return;
		lastTick = thisTick;
		if (this.fullCheckNext)
			doFullCheck();
		if (this.partialCheckNext)
			doPartialCheck();
		if (!this.getWorld().isRemote) {
			if (this.getWorld().getTotalWorldTime() % 1200L == 0L)
				triggerFullCheck();
			else if (this.getWorld().getTotalWorldTime() % 40L == 0L)
				triggerPartialCheck();
			if (this.getWorld().getTotalWorldTime() % 5L == 0L)
				this.getWorld().setBlockState(this.pos, this.getWorld().getBlockState(pos)
						.withProperty(BlockMicrowaveGenerator.on, (this.producingPower && this.output > 0)), 3);
		}
		if (this.producingPower) {
			if (this.getWorld().getTotalWorldTime() % 5L == 0L) {
				this.calculateNewOutput();
			}
			if (this.output > 0) {
				if ((this.festorage.receiveEnergy((int) output, false) > 0
						|| (this.getWorld().getTotalWorldTime() % 4L == 0L)) && this.powered > 0) {
					this.powered -= 1;
				}
			}
			if ((getTrueEnergyStored() > 0)) {

				for (EnumFacing facing : PhotonicUtils.getShuffledFaces()) {
					TileEntity tile = getWorld().getTileEntity(new BlockPos(xCoord() + facing.getFrontOffsetX(),
							yCoord() + facing.getFrontOffsetY(), zCoord() + facing.getFrontOffsetZ()));
					if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
						IEnergyStorage enstr = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
						festorage.extractEnergy(
								enstr.receiveEnergy(festorage.extractEnergy(festorage.getMaxExtract(), true), false),
								false);
					}
				}
			}
		} else if (this.isBuilding() && (this.getWorld().getTotalWorldTime() % 4L == 0L)
				&& (this.festorageBuild.getEnergyStored() >= RF_PER_BUILD_BLOCK)) {
			TileEntityChest tec = (TileEntityChest) this.getWorld()
					.getTileEntity(new BlockPos(this.xCoord(), this.yCoord() + 1, this.zCoord()));
			// loop over slots and remove one panel if found
			boolean panel = false;
			int slot = -1;
			Item ib = Item.getItemFromBlock(PhotonicBlocks.microwavePanel);
			for (int i = 0; i < tec.getSizeInventory(); ++i) {
				ItemStack s = tec.getStackInSlot(i);
				if (s == null)
					continue;
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
					this.festorageBuild.extractEnergy(RF_PER_BUILD_BLOCK, false);
					tec.decrStackSize(slot, 1);
				}
			}
		}
	}

	@Override
	public void validate() {
		super.validate();

		this.getWorld().scheduleBlockUpdate(this.pos, PhotonicBlocks.microwaveGenerator, 10, 3);
	}

	private void writeSyncableDataToNBT(NBTTagCompound p_145841_1_) {
		p_145841_1_.setInteger("radius", radius);
		p_145841_1_.setDouble("minpower", min);
		p_145841_1_.setDouble("maxpower", max);
		p_145841_1_.setBoolean("poweredon", producingPower);
		p_145841_1_.setBoolean("shouldfull", fullCheckNext);
		p_145841_1_.setBoolean("shouldpartial", partialCheckNext);
		p_145841_1_.setDouble("output", output);
		p_145841_1_.setLong("hahnium", powered);
		p_145841_1_.setInteger("fenergy", this.festorage.getEnergyStored());
		p_145841_1_.setInteger("fenergybuild", this.festorageBuild.getEnergyStored());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		writeSyncableDataToNBT(p_145841_1_);
		p_145841_1_.setInteger("radius", radius);
		p_145841_1_.setDouble("minpower", min);
		p_145841_1_.setDouble("maxpower", max);
		p_145841_1_.setBoolean("poweredon", producingPower);
		p_145841_1_.setBoolean("shouldfull", fullCheckNext);
		p_145841_1_.setBoolean("shouldpartial", partialCheckNext);
		p_145841_1_.setDouble("output", output);
		p_145841_1_.setLong("hahnium", powered);
		p_145841_1_.setInteger("fenergy", this.festorage.getEnergyStored());
		p_145841_1_.setInteger("fenergybuild", this.festorageBuild.getEnergyStored());
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
