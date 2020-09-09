package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = Compat.MODID_MIRAGE)})
public class TileEntityFluorescentLight extends TileEntity implements ITickable, com.elytradev.mirage.lighting.ILightEventConsumer {
	public static final EnumSkyBlock lighttype = EnumSkyBlock.BLOCK;

	boolean lastpowered = false;

	boolean powered = false;

	public long lastTick = 0L;

	public TileEntityFluorescentLight() {
		super();
	}

	public boolean isPowered() {
		return this.powered;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
	}

	private void rhombus(World w, int x, int y, int z, int l, boolean N, boolean S, boolean W, boolean E, boolean D) {
		if (w.isRemote)
			return;
		if (w.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR) {
			w.setBlockState(new BlockPos(x, y, z), PhotonicBlocks.lightAir.getDefaultState(), 3);
			((TileEntityLightAir) w.getTileEntity(new BlockPos(x, y, z))).ticksLeft = 30;
		} else if (w.getBlockState(new BlockPos(x, y, z)).getBlock() == PhotonicBlocks.lightAir) {
			w.setBlockToAir(new BlockPos(x, y, z));
			w.setBlockState(new BlockPos(x, y, z), PhotonicBlocks.lightAir.getDefaultState(), 3);
			((TileEntityLightAir) w.getTileEntity(new BlockPos(x, y, z))).ticksLeft = 30;
		}
		if (N) {
			int el = l - (1 + w.getBlockLightOpacity(new BlockPos(x, y, z - 1)));
			if (el >= 16)
				this.rhombus(w, x, y, z - 1, el, N, false, W, E, D);
		}
		if (S) {
			int el = l - (1 + w.getBlockLightOpacity(new BlockPos(x, y, z + 1)));
			if (el >= 16)
				this.rhombus(w, x, y, z + 1, el, false, S, W, E, D);
		}
		if (W) {
			int el = l - (1 + w.getBlockLightOpacity(new BlockPos(x - 1, y, z)));
			if (el >= 16)
				this.rhombus(w, x - 1, y, z, el, N, S, W, false, D);
		}
		if (E) {
			int el = l - (1 + w.getBlockLightOpacity(new BlockPos(x + 1, y, z)));
			if (el >= 16)
				this.rhombus(w, x + 1, y, z, el, N, S, false, E, D);
		}
		if (D && y >= 1) {
			int el = l - (1 + w.getBlockLightOpacity(new BlockPos(x, y - 1, z)));
			if (el >= 16)
				this.rhombus(w, x, y - 1, z, el, false, false, false, false, D);
		}
	}

	@Optional.Method(modid = Compat.MODID_MIRAGE)
	@Override
	public void gatherLights(com.elytradev.mirage.event.GatherLightsEvent evt) {
		if (this.powered)
			evt.add(com.elytradev.mirage.lighting.Light.builder().pos(new Vec3d(pos).addVector(0.5, 0.5, 0.5))
					.radius(2.5f).intensity(1.5f).color(1.0f, 0.98f, 0.95f).build());
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void update() {
		if (this.getWorld().getTotalWorldTime() <= lastTick)
			return;
		lastTick = this.getWorld().getTotalWorldTime();
		updatePower();
		if (powered != lastpowered) {
			this.updateLights(this.getWorld(), this.xCoord(), this.yCoord(), this.zCoord());
			this.world.setBlockState(pos,
					this.world.getBlockState(pos).withProperty(BlockFluorescentLight.powered, this.powered));
		}
		if (powered) {
			World w = this.getWorld();
			int ox = xCoord(), oy = yCoord(), oz = zCoord();
			for (int x = -2; x <= 2; x++) {
				int ax = ox + x;
				for (int z = -2; z <= 2; z++) {
					int az = oz + z;
					for (int y = 0; y < 3; y++) {
						int ay = oy - y;
						BlockPos pos = new BlockPos(ax, ay, az);
						if (w.getBlockState(pos).getBlock() == Blocks.AIR) {
							w.setBlockState(pos, PhotonicBlocks.lightAir.getDefaultState(), 3);
						} else if (w.getBlockState(pos).getBlock() == PhotonicBlocks.lightAir) {
							TileEntity te = w.getTileEntity(pos);
							if (te instanceof TileEntityLightAir)
								((TileEntityLightAir) te).reset();
						} else if (w.getBlockLightOpacity(pos) > 0)
							break;
					}
				}
			}
		}
	}

	public void updateLights(World w, int x, int y, int z) {
		int ix = x, iy = y, iz = z;

		w.markBlockRangeForRenderUpdate(ix, iy, iz, 12, 12, 12);
		w.notifyBlockUpdate(new BlockPos(ix, iy, iz), this.world.getBlockState(this.pos),
				this.world.getBlockState(this.pos), 2);

		w.checkLight(new BlockPos(ix, iy, iz));
		w.checkLight(new BlockPos(ix + 1, iy, iz));
		w.checkLight(new BlockPos(ix + 1, iy, iz + 1));
		w.checkLight(new BlockPos(ix + 1, iy, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy, iz + 1));
		w.checkLight(new BlockPos(ix - 1, iy, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy, iz));
		w.checkLight(new BlockPos(ix, iy, iz + 1));
		w.checkLight(new BlockPos(ix, iy, iz - 1));

		w.checkLight(new BlockPos(ix, iy + 1, iz));
		w.checkLight(new BlockPos(ix + 1, iy + 1, iz));
		w.checkLight(new BlockPos(ix + 1, iy + 1, iz + 1));
		w.checkLight(new BlockPos(ix + 1, iy + 1, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy + 1, iz + 1));
		w.checkLight(new BlockPos(ix - 1, iy + 1, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy + 1, iz));
		w.checkLight(new BlockPos(ix, iy + 1, iz + 1));
		w.checkLight(new BlockPos(ix, iy + 1, iz - 1));
		w.checkLight(new BlockPos(ix, iy - 1, iz));
		w.checkLight(new BlockPos(ix + 1, iy - 1, iz));
		w.checkLight(new BlockPos(ix + 1, iy - 1, iz + 1));
		w.checkLight(new BlockPos(ix + 1, iy - 1, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy - 1, iz + 1));
		w.checkLight(new BlockPos(ix - 1, iy - 1, iz - 1));
		w.checkLight(new BlockPos(ix - 1, iy - 1, iz));
		w.checkLight(new BlockPos(ix, iy - 1, iz + 1));
		w.checkLight(new BlockPos(ix, iy - 1, iz - 1));
	}

	protected void updatePower() {
		if ((this.getWorld().getTotalWorldTime() % 5L != 0L))
			return;
		lastpowered = powered;
		powered = this.getWorld()
				.isBlockIndirectlyGettingPowered(new BlockPos(this.xCoord(), this.yCoord(), this.zCoord())) > 0;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
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
