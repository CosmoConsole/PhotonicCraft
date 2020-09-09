package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.PhotonicAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = Compat.MODID_MIRAGE)})
public class TileEntityFluorescentBlacklight extends TileEntityFluorescentLight implements com.elytradev.mirage.lighting.ILightEventConsumer {

	private boolean hasLineOfSight(Entity ei) {
		final double DD = 0.4;
		World w = ei.world;
		Vec3d me = new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 0.8, this.pos.getZ() + 0.5);
		Vec3d inter = ei.getPositionVector().subtract(me).normalize().scale(DD);
		int steps = (int) Math.floor(ei.getPositionVector().distanceTo(me) / DD);
		BlockPos bp = new BlockPos(me);
		while (steps > 0) {
			--steps;
			me = me.add(inter);
			BlockPos nbp = new BlockPos(me);
			if (!nbp.equals(bp)) {
				IBlockState bs = w.getBlockState(nbp);
				if (bs.getLightOpacity(w, bp) > 0) {
					return false;
				}
				bp = nbp;
			}
		}
		return true;
	}

	@Optional.Method(modid = Compat.MODID_MIRAGE)
	@Override
	public void gatherLights(com.elytradev.mirage.event.GatherLightsEvent evt) {
		if (this.powered)
			evt.add(com.elytradev.mirage.lighting.Light.builder().pos(new Vec3d(pos).addVector(0.5, 0.5, 0.5)).radius(2.5f).intensity(1.5f)
					.color(1.0f, 0.0f, 1.0f).build());
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
		if (this.powered && ((this.getWorld().getTotalWorldTime() % 4L) == 0L) && !this.world.isRemote) {
			World w = this.getWorld();
			for (EntityItem ei : this.getWorld().getEntitiesWithinAABB(EntityItem.class,
					new AxisAlignedBB(this.xCoord() - 2, this.yCoord() - 5, this.zCoord() - 2, this.xCoord() + 2,
							this.yCoord(), this.zCoord() + 2))) {
				if (Math.random() < 0.02 && ei != null && hasLineOfSight(ei)) {
					ItemStack si = ei.getItem();
					ItemStack di = PhotonicAPI.tryToProcessUltravioletForItem(si);
					if (!di.isEmpty()) {
						int leftover = PhotonicAPI.getUltravioletStackLeftover(si);
						int maxStackSize = di.getItem().getItemStackLimit(si);
						int remaining = di.getCount();
						while (remaining > 0) {
							int curStack = Math.min(maxStackSize, remaining);
							ItemStack nstack = di.copy();
							nstack.setCount(curStack);
							EntityItem nei = new EntityItem(ei.world, ei.posX, ei.posY, ei.posZ, nstack);
							ei.world.spawnEntity(nei);
							remaining -= curStack;
						}
						if (leftover < 1)
							ei.setDead();
						else
							ei.getItem().setCount(leftover);
					}
				}
			}
		}
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
