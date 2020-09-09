package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * A custom location object for floating-point XYZ coordinates and helper
 * methods.
 */
public class PhotonicLocation {
	public static PhotonicLocation fromBlock(World w, int x, int y, int z) {
		return new PhotonicLocation(w, x + 0.5, y + 0.5, z + 0.5);
	}

	public static PhotonicLocation fromPlayer(EntityPlayer p) {
		return new PhotonicLocation(p.world, p.posX, p.posY, p.posZ);
	}

	public World world;
	public double x;
	public double y;
	public double z;

	public PhotonicLocation(World w, double d, double e, double f) {
		this.world = w;
		this.x = d;
		this.y = e;
		this.z = f;
	}

	public PhotonicLocation(World w, BlockPos pos) {
		this(w, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
	}

	public double distance(PhotonicLocation other) {
		return Math.sqrt(this.distanceSq(other));
	}

	public double distanceSq(PhotonicLocation other) {
		if (!PhotonicUtils.worldEquals(this.world, other.world))
			return Double.POSITIVE_INFINITY;
		double dx = other.x - this.x, dy = other.y - this.y, dz = other.z - this.z;
		return dx * dx + dy * dy + dz * dz;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PhotonicLocation) {
			PhotonicLocation other = (PhotonicLocation) obj;
			return PhotonicUtils.worldEquals(world, other.world) && other.x == this.x && other.y == this.y && other.z == this.z;
		}
		return false;
	}

	public Block getBlockAt() {
		return world
				.getBlockState(
						new BlockPos(MathHelper.floor(this.x), MathHelper.floor(this.y), MathHelper.floor(this.z)))
				.getBlock();
	}

	public TileEntity getTileEntityAt() {
		return world.getTileEntity(
				new BlockPos(MathHelper.floor(this.x), MathHelper.floor(this.y), MathHelper.floor(this.z)));
	}

	public UUID getUniqueId() {
		return UUID.nameUUIDFromBytes(this.toString().getBytes());
	}

	@Override
	public String toString() {
		return "'" + this.world.getWorldInfo().getWorldName() + "'(" + this.world.provider.getDimension() + "):"
				+ this.x + "," + this.y + "," + this.z;
	}

	public BlockPos toBlockPos() {
		return new BlockPos((int)x, (int)y, (int)z);
	}
}
