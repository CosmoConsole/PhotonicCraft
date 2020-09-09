package email.com.gmail.cosmoconsole.forge.photoniccraft;

import java.util.UUID;

import cofh.lib.util.helpers.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PhotonicLocation {
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
	public static PhotonicLocation fromBlock(World w, int x, int y, int z) {
		return new PhotonicLocation(w, x + 0.5, y + 0.5, z + 0.5);
	}
	public static PhotonicLocation fromPlayer(EntityPlayer p) {
		return new PhotonicLocation(p.worldObj, p.posX, p.posY, p.posZ);
	}
	public double distance(PhotonicLocation other) {
		return Math.sqrt(this.distanceSq(other));
	}
	public double distanceSq(PhotonicLocation other) {
		if (!PhotonicAPI.worldEquals(this.world, other.world))
			return Double.POSITIVE_INFINITY;
		double dx = other.x - this.x, dy = other.y - this.y, dz = other.z - this.z;
		return dx * dx + dy * dy + dz * dz;
	}
	public Block getBlockAt() {
		return world.getBlock(MathHelper.floor(this.x), MathHelper.floor(this.y), MathHelper.floor(this.z));
	}
	public TileEntity getTileEntityAt() {
		return world.getTileEntity(MathHelper.floor(this.x), MathHelper.floor(this.y), MathHelper.floor(this.z));
	}
	@Override
	public String toString() {
		return "'" + this.world.getWorldInfo().getWorldName() + "'(" + this.world.provider.dimensionId + "):" + this.x + "," + this.y + "," + this.z;
	}
	public UUID getUniqueId() {
		return UUID.nameUUIDFromBytes(this.toString().getBytes());
	}
}
