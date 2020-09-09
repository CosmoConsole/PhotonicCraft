package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLaserMirror;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockMerger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityGlisterstone;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserDetector2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserMerger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EntityLaserBeamResult.Type;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * The implementation for the laser beams used by PhotonicCraft lasers.
 */
public class PhotonicLaser {
	public static long laserId = 0L;
	public static long lastFired = 0L;
	public static HashMap<Long, ArrayList<Long>> already = new HashMap<Long, ArrayList<Long>>();

	public static DamageSource laserDamage = new DamageSource("photonicLaser").setDamageBypassesArmor().setFireDamage();

	private static double calculateBluePower(int laserColor) {
		return getBlueComponent(laserColor) / (getRedComponent(laserColor) + getGreenComponent(laserColor) + getBlueComponent(laserColor));
	}

	private static double calculateGreenPower(int laserColor) {
		return getGreenComponent(laserColor) / (getRedComponent(laserColor) + getGreenComponent(laserColor) + getBlueComponent(laserColor));
	}

	private static double calculateRedPower(int laserColor) {
		return getRedComponent(laserColor) / (getRedComponent(laserColor) + getGreenComponent(laserColor) + getBlueComponent(laserColor));
	}

	private static EnumFacing directionLeft(EnumFacing laserDirection) {
		switch (laserDirection) {
		case DOWN:
			return EnumFacing.EAST;
		case UP:
			return EnumFacing.WEST;
		case NORTH:
			return EnumFacing.UP;
		case SOUTH:
			return EnumFacing.DOWN;
		case WEST:
			return EnumFacing.NORTH;
		case EAST:
			return EnumFacing.SOUTH;
		}
		return null;
	}

	private static EnumFacing directionRight(EnumFacing laserDirection) {
		return directionLeft(laserDirection).getOpposite();
	}

	public static float getBlueComponent(int laserColor) {
		return ((laserColor) & 0xFF) / 255f;
	}

	public static float getGreenComponent(int laserColor) {
		return ((laserColor >> 8) & 0xFF) / 255f;
	}

	public static float getRedComponent(int laserColor) {
		return ((laserColor >> 16) & 0xFF) / 255f;
	}

	public static int getRGBFromMeta(int meta) {
		int col = 0;
		switch (meta) {
		case 0:
			col = 0x202020;
			break;
		case 1:
			col = 0xFF0000;
			break;
		case 2:
			col = 0x008000;
			break;
		case 3:
			col = 0x805834;
			break;
		case 4:
			col = 0x0000FF;
			break;
		case 5:
			col = 0x8000B0;
			break;
		case 6:
			col = 0x00FFFF;
			break;
		case 7:
			col = 0xa0a0a0;
			break;
		case 8:
			col = 0x606060;
			break;
		case 9:
			col = 0xff8060;
			break;
		case 10:
			col = 0x00ff00;
			break;
		case 11:
			col = 0xffff00;
			break;
		case 12:
			col = 0x6699ff;
			break;
		case 13:
			col = 0xc000ff;
			break;
		case 14:
			col = 0xff8000;
			break;
		case 15:
			col = 0xffffff;
			break;
		case -1:
			return 0x000000;
		}
		return col;
	}

	private static boolean lookingAtLaser(EntityLivingBase el, float laserY, EnumFacing laserDirection) {
		switch (laserDirection) {
		case DOWN:
			return el.rotationPitch < -1.3089969389957472f;
		case NORTH:
			return Math.abs(el.posY + el.getEyeHeight() - laserY) < 0.4
					&& PhotonicUtils.angleDifference(el.rotationYaw, 0f) < 0.5235987755982988f;
		case SOUTH:
			return Math.abs(el.posY + el.getEyeHeight() - laserY) < 0.4
					&& PhotonicUtils.angleDifference(el.rotationYaw, 3.141592653589793f) < 0.5235987755982988f;
		case WEST:
			return Math.abs(el.posY + el.getEyeHeight() - laserY) < 0.4
					&& PhotonicUtils.angleDifference(el.rotationYaw, -1.5707963267948966f) < 0.5235987755982988f;
		case EAST:
			return Math.abs(el.posY + el.getEyeHeight() - laserY) < 0.4
					&& PhotonicUtils.angleDifference(el.rotationYaw, 1.5707963267948966f) < 0.5235987755982988f;
		case UP:
			return false;
		}
		return false;
	}

	public synchronized static EntityLaserBeam[] shootBeam(World worldObj, float x, float y, float z,
			EnumFacing laserDirection, int laserColor, int laserPower, float maxLen, boolean puny, int ignoreFirst) {
		if (worldObj.getTotalWorldTime() > lastFired) {
			lastFired = worldObj.getTotalWorldTime();
			laserId = 0L;
		}
		already.put(laserId, new ArrayList<Long>());
		return shootBeam(worldObj, x, y, z, laserDirection, laserColor, laserPower, maxLen, puny, ignoreFirst,
				laserId++);
	}

	public synchronized static EntityLaserBeam[] shootBeam(World worldObj, float x, float y, float z,
			EnumFacing laserDirection, int laserColor, int laserPower, float maxLen, boolean puny, int ignoreFirst,
			long cLaserId) {
		if (laserPower < 1)
			return new EntityLaserBeam[] {};
		float totalLen = 0.0f;
		ArrayList<EntityLaserBeam> beams = new ArrayList<EntityLaserBeam>();
		while (totalLen < maxLen) {
			EntityLaserBeamResult e = shootSingleBeam(worldObj, x, y, z, laserDirection, laserColor, laserPower, puny,
					ignoreFirst);
			if (e.beam != null) {
				beams.add(e.beam);
				if (e.beam.getPunyTrail() != null)
					beams.add(e.beam.getPunyTrail());
			}
			if (e.type == Type.HITEND)
				break;
			if (e.type == Type.HITCONTINUE)
				laserPower -= e.decrease;
			ignoreFirst = -1;
			float len = e.beam.blocklen;
			switch (laserDirection) {
			case DOWN:
				y -= len;
				break;
			case EAST:
				x += len;
				break;
			case NORTH:
				z -= len;
				break;
			case SOUTH:
				z += len;
				break;
			case UP:
				y += len;
				break;
			case WEST:
				x -= len;
				break;
			default:
				break;
			}
			long pos = (MathHelper.floor(y) << 50) | (MathHelper.floor(z) << 25) | (MathHelper.floor(x));
			if (e.type == Type.NEWDIRECTION || (e.type == Type.NEWDIRCLONE && laserPower <= 1)) {
				laserDirection = e.direction;
			} else if (e.type == Type.NEWCOLOR) {
				if (e.colormeta >= 0)
					ignoreFirst = e.colormeta;
				laserColor = e.newcolor;
			} else if (e.type == Type.NEWDIRCOLOR) {
				laserDirection = e.direction;
				if (e.colormeta >= 0)
					ignoreFirst = e.colormeta;
				laserColor = e.newcolor;
			} else if (e.type == Type.NEWDIRCLONE) {
				if (laserPower < 3) {
					break;
				}
				laserDirection = e.direction;
				if (already.get(cLaserId).contains(pos)) {
					laserColor = 0;
					laserPower = 0;
				} else
					for (EntityLaserBeam b : shootBeam(worldObj, x, y, z, e.direction2, laserColor, laserPower >> 1,
							maxLen - totalLen - len, puny, -1, cLaserId))
						beams.add(b);
				already.get(cLaserId).add(pos);
				laserPower = laserPower >> 1;
			} else if (e.type == Type.PRISM) {
				if (laserPower < 3) {
					break;
				}
				if (!already.get(cLaserId).contains(pos)) {
					for (EntityLaserBeam b : shootBeam(worldObj, x, y, z, directionLeft(laserDirection), 0xFF0000,
							(int) (calculateRedPower(laserColor) * laserPower), maxLen - totalLen - len, puny, -1,
							cLaserId))
						beams.add(b);
					for (EntityLaserBeam b : shootBeam(worldObj, x, y, z, directionRight(laserDirection), 0x0000FF,
							(int) (calculateBluePower(laserColor) * laserPower), maxLen - totalLen - len, puny, -1,
							cLaserId))
						beams.add(b);
				} else {
					laserColor = 0;
					laserPower = 0;
				}
				already.get(cLaserId).add(pos);
				laserPower = (int) (calculateGreenPower(laserColor) * laserPower);
				laserColor = 0x00FF00;
			} else if (e.type == Type.NEWCOLORDIR) {
				laserDirection = e.direction;
				laserColor = e.newcolor;
				laserPower = e.colormeta;
			}
			totalLen += len;
			if (laserPower == 0)
				break;
			if (laserColor == 0)
				break;
		}
		return beams.toArray(new EntityLaserBeam[] {});
	}

	public static EntityLaserBeamResult shootSingleBeam(World worldObj, float x, float y, float z,
			EnumFacing laserDirection, int laserColor, int laserPower, boolean puny, int ignoreFirst) {
		if (laserColor == 0)
			return new EntityLaserBeamResult(null, Type.HITEND);
		final float[] yaws = { 180f, 0f, 0f, 180f, -90f, 90f };
		final float[] pitches = { 90f, -90f, 0f, 0f, 0f, 0f };
		final float bb = 0.125f;
		final float fbb = 0.5f;
		int i = PhotonicUtils.convertEnumFacingToInt(laserDirection);
		float len = 32.0f;
		// UP, DOWN, NORTH, SOUTH, WEST, EAST;
		int typ = 0;
		int tyc = 0;
		int tym = -1;
		EnumFacing tyd = EnumFacing.DOWN;
		int nx = 0;
		int ny = 0;
		int nz = 0;
		int jk = 0;
		float le = 0.0f;
		long lasertick = worldObj.getTotalWorldTime();
		for (int j = 1; j < len; j++) {
			jk = j;
			float bx = x + j * laserDirection.getFrontOffsetX();
			float by = y + j * laserDirection.getFrontOffsetY();
			float bz = z + j * laserDirection.getFrontOffsetZ();
			if (!worldObj.isBlockLoaded(new BlockPos(bx, by, bz))) {
				typ = 1;
				tyc = 0;
				break;
			}
			if (by >= (worldObj.getActualHeight() + 4)) {
				typ = 1;
				tyc = 0;
				break;
			}
			/*AxisAlignedBB aabb = null;	
			float rx = MathHelper.floor(bx - laserDirection.getFrontOffsetX()) + 0.5f;
			float ry = MathHelper.floor(by - laserDirection.getFrontOffsetY()) + 0.5f;
			float rz = MathHelper.floor(bz - laserDirection.getFrontOffsetZ()) + 0.5f;
			switch (laserDirection) {
			case WEST:
			case EAST:
				aabb = new AxisAlignedBB(rx - fbb, ry - bb, rz - bb, rx + fbb, ry + bb, rz + bb);
				break;
			case DOWN:
			case UP:
				aabb = new AxisAlignedBB(rx - bb, ry - fbb, rz - bb, rx + bb, ry + fbb, rz + bb);
				break;
			case NORTH:
			case SOUTH:
			default:
				aabb = new AxisAlignedBB(rx - bb, ry - bb, rz - fbb, rx + bb, ry + bb, rz + fbb);
				break;
			}
			EntityCollisionResult ecr = findEntityCollision(worldObj, aabb, laserDirection);
			if (ecr != null) {
				Entity e = ecr.getEntity();
				if (laserPower >= 100) {
					if (e instanceof EntityItem
							&& ((EntityItem) e).getItem().getItem() != PhotonicItems.yttriumIngot
							&& ((EntityItem) e).getItem().getItem() != PhotonicItems.nougat)
						((EntityItem) e).setDead();
				}
				if (laserPower >= 20) {
					double d = Math.min(laserPower, 100) * 0.01d;
					d = d * d * d * d * d;
					boolean destroy = d >= Math.random();
					if (e instanceof EntityMinecart)
						((EntityMinecart) e).attackEntityFrom(null, destroy ? 100 : 1);
					if (e instanceof EntityBoat)
						((EntityBoat) e).attackEntityFrom(null, destroy ? 100 : 1);
					if (e instanceof EntityTNTPrimed && destroy)
						((EntityTNTPrimed) e).setFuse(0);
				}
				int everyTicks = (int) Math.ceil(100.0 / Math.min(laserPower, 100));
				everyTicks *= everyTicks;
				everyTicks = (int) Math.ceil(everyTicks * 0.5d);
				if (worldObj.getTotalWorldTime() % everyTicks == 0L && (e instanceof EntityLivingBase)) {
					double d = Math.min(laserPower, 100) * 0.01d;
					d = d * d * d * d * d;
					boolean destroy = d >= Math.random();
					EntityLivingBase el = (EntityLivingBase) e;
					if (destroy || laserPower >= 100)
						el.setFire(Math.min(5 * (int) Math.ceil(Math.random() * laserPower), 500));
					el.attackEntityFrom(laserDamage, (laserPower / 20));
					if (laserPower >= 20 && (Math.random()) < (laserPower / 200.0)
							&& lookingAtLaser(el, y, laserDirection)
							&& el.getItemStackFromSlot(EntityEquipmentSlot.HEAD)
									.getItem() != PhotonicItems.safetyglasses) {
						if (!(el instanceof EntityPlayer) || (!((EntityPlayer) el).capabilities.isCreativeMode
								&& !((EntityPlayer) el).isSpectator())) {
							el.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 150, 4, true, false));
							el.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 150, 1, true, false));
						}
					}
				}
				typ = 1;
				tyc = 1;
				le = ecr.getDistance() - 1.0f;
				break;
			}*/
			/*
			for (float n = 0.75f; n >= 0.0f; n -= 0.25f) {
				float cx = (float) (x + PhotonicUtils.sigsub(j * laserDirection.getFrontOffsetX(), n));
				float cy = (float) (y + PhotonicUtils.sigsub(j * laserDirection.getFrontOffsetY(), n));
				float cz = (float) (z + PhotonicUtils.sigsub(j * laserDirection.getFrontOffsetZ(), n));
				for (Object e : worldObj.getEntitiesWithinAABB(Entity.class,
						new AxisAlignedBB(cx - bb, cy - bb, cz - bb, cx + bb, cy + bb, cz + bb))) {
					if ((e instanceof EntityItem) || (e instanceof EntityBoat) || (e instanceof EntityMinecart)
							|| (e instanceof EntityArrow) || (e instanceof EntityThrowable)
							|| (e instanceof EntityFallingBlock) || (e instanceof EntityTNTPrimed)
							|| (e instanceof EntityLivingBase)) {
						if (laserPower >= 100) {
							if (e instanceof EntityItem
									&& ((EntityItem) e).getItem().getItem() != PhotonicItems.yttriumIngot
									&& ((EntityItem) e).getItem().getItem() != PhotonicItems.nougat)
								((EntityItem) e).setDead();
						}
						if (laserPower >= 20) {
							double d = Math.min(laserPower, 100) * 0.01d;
							d = d * d * d * d * d;
							boolean destroy = d >= Math.random();
							if (e instanceof EntityMinecart)
								((EntityMinecart) e).attackEntityFrom(null, destroy ? 100 : 1);
							if (e instanceof EntityBoat)
								((EntityBoat) e).attackEntityFrom(null, destroy ? 100 : 1);
							if (e instanceof EntityTNTPrimed && destroy)
								((EntityTNTPrimed) e).setFuse(0);
						}
						int everyTicks = (int) Math.ceil(100.0 / Math.min(laserPower, 100));
						everyTicks *= everyTicks;
						everyTicks = (int) Math.ceil(everyTicks * 0.5d);
						if (worldObj.getTotalWorldTime() % everyTicks == 0L && (e instanceof EntityLivingBase)) {
							double d = Math.min(laserPower, 100) * 0.01d;
							d = d * d * d * d * d;
							boolean destroy = d >= Math.random();
							EntityLivingBase el = (EntityLivingBase) e;
							if (destroy || laserPower >= 100)
								el.setFire(Math.min(5 * (int) Math.ceil(Math.random() * laserPower), 500));
							el.attackEntityFrom(laserDamage, (laserPower / 20));
							if (laserPower >= 20 && (Math.random()) < (laserPower / 200.0)
									&& lookingAtLaser(el, y, laserDirection)
									&& el.getItemStackFromSlot(EntityEquipmentSlot.HEAD)
											.getItem() != PhotonicItems.safetyglasses) {
								if (!(el instanceof EntityPlayer) || (!((EntityPlayer) el).capabilities.isCreativeMode
										&& !((EntityPlayer) el).isSpectator())) {
									el.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 150, 4, true, false));
									el.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 150, 1, true, false));
								}
							}
						}
						typ = 1;
						tyc = 1;
						le = -n + 0.4f;
						break;
					}
				}
			}
			*/
			if (tyc > 0)
				break;
			nx = (int) Math.floor(bx);
			ny = (int) Math.floor(by);
			nz = (int) Math.floor(bz);
			BlockPos nbp = new BlockPos(nx, ny, nz);
			Block b = worldObj.getBlockState(nbp).getBlock();
			if (!b.isAir(worldObj.getBlockState(nbp), worldObj, nbp)) {
				if (b == Blocks.GLASS || b == Blocks.GLASS_PANE) {
					continue;
				} else if (b == Blocks.STAINED_GLASS_PANE || b == Blocks.STAINED_GLASS) {
					int meta = PhotonicUtils.getStainedGlassColor(worldObj.getBlockState(nbp));
					if (j == 1 && ignoreFirst == meta)
						continue;
					typ = 3;
					tym = meta;
					int col = getRGBFromMeta(15 - meta);
					tyc = laserColor & col;
					if (b == Blocks.STAINED_GLASS_PANE)
						le = 7 / 16;
					break;
				} else if (b == PhotonicBlocks.laserRedstoneMirror || b == PhotonicBlocks.laserRedstoneMirrorOn) {
					final EnumFacing[][] mirrors = new EnumFacing[][] { { EnumFacing.SOUTH, EnumFacing.EAST },
							{ EnumFacing.SOUTH, EnumFacing.WEST }, { EnumFacing.DOWN, EnumFacing.EAST },
							{ EnumFacing.DOWN, EnumFacing.SOUTH }, { EnumFacing.UP, EnumFacing.EAST },
							{ EnumFacing.UP, EnumFacing.NORTH }, {}, {}, { EnumFacing.NORTH, EnumFacing.WEST },
							{ EnumFacing.NORTH, EnumFacing.EAST }, { EnumFacing.DOWN, EnumFacing.WEST },
							{ EnumFacing.DOWN, EnumFacing.NORTH }, { EnumFacing.UP, EnumFacing.WEST },
							{ EnumFacing.UP, EnumFacing.SOUTH }, {}, {}, };
					boolean powered = (b == PhotonicBlocks.laserRedstoneMirrorOn);
					int meta = PhotonicUtils.readIntegerProperty(worldObj.getBlockState(nbp), BlockLaserMirror.orient);
					if ((meta & 0x7) < 6 && powered) {
						int m = meta;
						if (laserDirection == mirrors[m][0]) {
							tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][1].getOpposite());
							typ = 2;
							le = 0.5f;
							break;
						} else if (laserDirection == mirrors[m][1]) {
							tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][0].getOpposite());
							typ = 2;
							le = 0.5f;
							break;
						} else if (laserDirection == mirrors[m][0].getOpposite()) {
							if (b == PhotonicBlocks.laserMirror2) {
								tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][1]);
								typ = 2;
								le = 0.5f;
								break;
							} else {
								typ = 1;
								le = 0.5f;
								break;
							}
						} else if (laserDirection == mirrors[m][1].getOpposite()) {
							if (b == PhotonicBlocks.laserMirror2) {
								tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][0]);
								typ = 2;
								le = 0.5f;
								break;
							} else {
								typ = 1;
								le = 0.5f;
								break;
							}
						}
					}
					if (typ < 0) {
						typ = 1;
						break;
					}
				} else if (b == PhotonicBlocks.laserMirror || b == PhotonicBlocks.laserMirror2) {
					final EnumFacing[][] mirrors = new EnumFacing[][] { { EnumFacing.SOUTH, EnumFacing.EAST },
							{ EnumFacing.SOUTH, EnumFacing.WEST }, { EnumFacing.DOWN, EnumFacing.EAST },
							{ EnumFacing.DOWN, EnumFacing.SOUTH }, { EnumFacing.UP, EnumFacing.EAST },
							{ EnumFacing.UP, EnumFacing.NORTH }, {}, {}, { EnumFacing.NORTH, EnumFacing.WEST },
							{ EnumFacing.NORTH, EnumFacing.EAST }, { EnumFacing.DOWN, EnumFacing.WEST },
							{ EnumFacing.DOWN, EnumFacing.NORTH }, { EnumFacing.UP, EnumFacing.WEST },
							{ EnumFacing.UP, EnumFacing.SOUTH }, {}, {}, };
					int meta = PhotonicUtils.readIntegerProperty(worldObj.getBlockState(nbp), BlockLaserMirror.orient);
					if ((meta & 0x7) < 6) {
						int m = meta;
						if (laserDirection == mirrors[m][0]) {
							tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][1].getOpposite());
							typ = 2;
							le = 0.5f;
							break;
						} else if (laserDirection == mirrors[m][1]) {
							tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][0].getOpposite());
							typ = 2;
							le = 0.5f;
							break;
						} else if (laserDirection == mirrors[m][0].getOpposite()) {
							if (b == PhotonicBlocks.laserMirror2) {
								tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][1]);
								typ = 2;
								le = 0.5f;
								break;
							} else {
								typ = 1;
								le = 0.5f;
								break;
							}
						} else if (laserDirection == mirrors[m][1].getOpposite()) {
							if (b == PhotonicBlocks.laserMirror2) {
								tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][0]);
								typ = 2;
								le = 0.5f;
								break;
							} else {
								typ = 1;
								le = 0.5f;
								break;
							}
						}
					}
					if (typ < 0) {
						typ = 1;
						break;
					}
				} else if (b == PhotonicBlocks.laserSemiMirror || b == PhotonicBlocks.laserSemiMirror2) {
					final EnumFacing[][] mirrors = new EnumFacing[][] { { EnumFacing.SOUTH, EnumFacing.EAST },
							{ EnumFacing.SOUTH, EnumFacing.WEST }, { EnumFacing.DOWN, EnumFacing.EAST },
							{ EnumFacing.DOWN, EnumFacing.SOUTH }, { EnumFacing.UP, EnumFacing.EAST },
							{ EnumFacing.UP, EnumFacing.NORTH }, {}, {}, { EnumFacing.NORTH, EnumFacing.WEST },
							{ EnumFacing.NORTH, EnumFacing.EAST }, { EnumFacing.DOWN, EnumFacing.WEST },
							{ EnumFacing.DOWN, EnumFacing.NORTH }, { EnumFacing.UP, EnumFacing.WEST },
							{ EnumFacing.UP, EnumFacing.SOUTH }, };
					int meta = PhotonicUtils.readIntegerProperty(worldObj.getBlockState(nbp), BlockLaserMirror.orient);
					if ((meta & 0x7) < 6) {
						int m = meta;
						if (laserDirection == mirrors[m][0]) {
							tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][1].getOpposite())
									| (PhotonicUtils.convertEnumFacingToInt(laserDirection) << 4);
							typ = 4;
							le = 0.5f;
							break;
						} else if (laserDirection == mirrors[m][1]) {
							tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][0].getOpposite())
									| (PhotonicUtils.convertEnumFacingToInt(laserDirection) << 4);
							typ = 4;
							le = 0.5f;
							break;
						} else if (laserDirection == mirrors[m][0].getOpposite()) {
							if (b == PhotonicBlocks.laserSemiMirror2) {
								tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][1])
										| (PhotonicUtils.convertEnumFacingToInt(laserDirection) << 4);
								typ = 4;
								le = 0.5f;
								break;
							} else {
								typ = 1;
								le = 0.5f;
								break;
							}
						} else if (laserDirection == mirrors[m][1].getOpposite()) {
							if (b == PhotonicBlocks.laserSemiMirror2) {
								tyc = PhotonicUtils.convertEnumFacingToInt(mirrors[m][0])
										| (PhotonicUtils.convertEnumFacingToInt(laserDirection) << 4);
								typ = 4;
								le = 0.5f;
								break;
							} else {
								typ = 1;
								le = 0.5f;
								break;
							}
						}
					}
					if (typ < 0) {
						typ = 1;
						break;
					}
				} else if (b == PhotonicBlocks.laserGenerator 
							|| b == PhotonicBlocks.laserCharger
							|| b == PhotonicBlocks.laserMerger) {
					typ = 1;
					le = 0.5f;
					break;
				} else if (b == PhotonicBlocks.prism) {
					typ = 5;
					le = 0.5f;
					break;
				} else if (b == Blocks.WATER || b == Blocks.FLOWING_WATER) {
					if (j == 1 && ignoreFirst >= 0)
						continue;
					typ = 3;
					int waterColor = worldObj.getBiome(new BlockPos(nx, 64, nz)).getWaterColorMultiplier();
					int wr = (waterColor >> 16) & 0xFF;
					int wg = (waterColor >> 8) & 0xFF;
					int wb = (waterColor) & 0xFF;
					wr = (wr * 0x30) >> 8;
					wg = (wg * 0x45) >> 8;
					wb = (wb * 0xF4) >> 8;
					waterColor = ((wr & 0xFF) << 16) | ((wg & 0xFF) << 8) | ((wb & 0xFF));
					tyc = laserColor & waterColor;
					le = 1 / 16;
					break;
				} else if ((b == Blocks.LAVA || b == Blocks.FLOWING_LAVA) && !(j == 1 && ignoreFirst >= 0)) {
					typ = 1;
					le = 2 / 16;
					break;
				} else if ((b.isNormalCube(worldObj.getBlockState(nbp), worldObj, nbp))
						|| (b == Blocks.LAVA || b == Blocks.FLOWING_LAVA)) {
					if (j == 1 && ignoreFirst >= 0)
						continue;
					typ = 1;
					tyc = 0;
					break;
				}
			}
		}
		if (typ > 0) {
			len = jk - (PhotonicUtils.frac(x) * laserDirection.getFrontOffsetX())
					- (PhotonicUtils.frac(y) * laserDirection.getFrontOffsetY())
					- (PhotonicUtils.frac(z) * laserDirection.getFrontOffsetZ());
			if ((laserDirection.getFrontOffsetX() < 0 || laserDirection.getFrontOffsetY() < 0
					|| laserDirection.getFrontOffsetZ() < 0))
				len -= 1f;
			len += le;
		}
		boolean entityHit = false;
		AxisAlignedBB aabb = constructLaserAABB(x, y, z, laserDirection, len, laserPower);
		EntityCollisionResult ecr = findEntityCollision(worldObj, x, y, z, laserDirection, aabb);		
		if (ecr != null) {
			entityHit = true;
			Entity e = ecr.getEntity();
			if (laserPower >= 100) {
				if (e instanceof EntityItem
						&& ((EntityItem) e).getItem().getItem() != PhotonicItems.yttriumIngot
						&& ((EntityItem) e).getItem().getItem() != PhotonicItems.nougat)
					((EntityItem) e).setDead();
			}
			if (laserPower >= 20) {
				double d = Math.min(laserPower, 100) * 0.01d;
				d = d * d * d * d * d;
				boolean destroy = d >= Math.random();
				if (e instanceof EntityMinecart)
					((EntityMinecart) e).attackEntityFrom(null, destroy ? 100 : 1);
				if (e instanceof EntityBoat)
					((EntityBoat) e).attackEntityFrom(null, destroy ? 100 : 1);
				if (e instanceof EntityTNTPrimed && destroy)
					((EntityTNTPrimed) e).setFuse(0);
			}
			int everyTicks = (int) Math.ceil(100.0 / Math.min(laserPower, 100));
			everyTicks *= everyTicks;
			everyTicks = (int) Math.ceil(everyTicks * 0.5d);
			if (worldObj.getTotalWorldTime() % everyTicks == 0L && (e instanceof EntityLivingBase)) {
				double d = Math.min(laserPower, 100) * 0.01d;
				d = d * d * d * d * d;
				boolean destroy = d >= Math.random();
				EntityLivingBase el = (EntityLivingBase) e;
				if (destroy || laserPower >= 100)
					el.setFire(Math.min(5 * (int) Math.ceil(Math.random() * laserPower), 500));
				el.attackEntityFrom(laserDamage, (laserPower / 20));
				if (laserPower >= 20 && (Math.random()) < (laserPower / 200.0)
						&& lookingAtLaser(el, y, laserDirection)
						&& el.getItemStackFromSlot(EntityEquipmentSlot.HEAD)
								.getItem() != PhotonicItems.safetyglasses) {
					if (!(el instanceof EntityPlayer) || (!((EntityPlayer) el).capabilities.isCreativeMode
							&& !((EntityPlayer) el).isSpectator())) {
						el.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 150, 4, true, false));
						el.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 150, 1, true, false));
					}
				}
			}
			typ = 1;
			tyc = 1;
			len = ecr.getDistance();
			jk = blockDifferenceTaxicab(x, y, z, x + laserDirection.getFrontOffsetX() * len, y + laserDirection.getFrontOffsetY() * len, z + laserDirection.getFrontOffsetZ() * len);
		}
		BlockPos nbp = new BlockPos(x, y, z);
		for (int j = 1; j <= jk; ++j) {
			nbp = nbp.add(laserDirection.getDirectionVec());
			if (j == 1 && ignoreFirst >= 0) continue;
			IBlockState bs = worldObj.getBlockState(nbp);
			Block b = bs.getBlock();
			if (!entityHit && b == PhotonicBlocks.laserMerger) {
				EnumFacing dir = PhotonicUtils.readDirectionProperty(worldObj.getBlockState(nbp),
						BlockMerger.FACING);
				if (BlockMerger.acceptableDirection(dir, laserDirection)) {
					TileEntityLaserMerger telm = (TileEntityLaserMerger) worldObj.getTileEntity(nbp);
					if (telm.laserTick >= lasertick) {
						if (telm.recLaser == 1) {
							telm.recLaser = 2;
							int _r = Math.min(255,
									((laserColor & 0xFF0000) >> 16) + ((telm.laserColor & 0xFF0000) >> 16));
							int _g = Math.min(255,
									((laserColor & 0xFF00) >> 8) + ((telm.laserColor & 0xFF00) >> 8));
							int _b = Math.min(255, ((laserColor & 0xFF)) + ((telm.laserColor & 0xFF)));
							typ = 6;
							tyc = (_r << 16) | (_g << 8) | (_b);
							tym = (int) Math.floor(telm.laserPower * 0.75f + laserPower * 0.75f);
							tyd = dir;
							le = 0.5f;
							break;
						}
					} else {
						telm.laserTick = lasertick;
						telm.recLaser = 1;
						telm.laserColor = laserColor;
						telm.laserPower = laserPower;
					}
				}
				typ = 1;
				le = 0.5f;
				break;
			} else if (b == PhotonicBlocks.glisterstone) {
				if (j == 1 && ignoreFirst >= 0)
					continue;
				if (!worldObj.isRemote) {
					if (laserPower >= 20) {
						double d = Math.min(laserPower, 100) * 0.01d;
						d = d * d * d * d * d;
						boolean destroy = d >= Math.random();
						if (destroy) {
							try {
								TileEntityGlisterstone gs = (TileEntityGlisterstone) worldObj.getTileEntity(nbp);
								gs.light = Math.max(gs.light, Math.min(15, laserPower / 6));
							} catch (Exception ex) {
							}
						}
					}
				}
				break;
			} else if (b == PhotonicBlocks.prism) {
				typ = 5;
				break;
			} else if (b == PhotonicBlocks.laserGenerator) {
				if (j == 1 && ignoreFirst >= 0)
					continue;
				try {
					TileEntityLaserGenerator gs = (TileEntityLaserGenerator) worldObj.getTileEntity(nbp);
					gs.addLaser(Math.max(1, laserPower / 4));
				} catch (Exception ex) {
				}
				break;
			} else if (b == PhotonicBlocks.laserCharger) {
				if (j == 1 && ignoreFirst >= 0)
					continue;
				try {
					TileEntityLaserCharger gs = (TileEntityLaserCharger) worldObj.getTileEntity(nbp);
					gs.addSignal(Math.max(1, laserPower / 4));
				} catch (Exception ex) {
				}
				break;
			} else if (b == Blocks.FURNACE || b == Blocks.LIT_FURNACE) {
				if (j == 1 && ignoreFirst >= 0)
					continue;
				try {
					TileEntityFurnace fs = (TileEntityFurnace) worldObj.getTileEntity(nbp);
					if (!worldObj.isRemote) {
						if (laserPower >= 20) {
							double d = Math.min(laserPower, 100) * 0.01d;
							d = d * d * d * d * d;
							boolean destroy = d >= Math.random();
							if (destroy) {
								fs.smeltItem();
							}
						}
					}
				} catch (Exception ex) {
				}
				break;
			} else if (b == Blocks.EMERALD_ORE) {
				if (j == 1 && ignoreFirst >= 0)
					continue;
				if (Math.random() < 0.002 && !worldObj.isRemote) {
					if (laserPower >= 100) {
						double d = Math.min(laserPower, 100) * 0.01d;
						d = d * d * d * d * d;
						boolean destroy = d >= Math.random();
						if (destroy) {
							worldObj.createExplosion(null, nx, ny, nz, 2.0f, false);
							worldObj.setBlockToAir(nbp);
							for (int fwf = 0; fwf < 5; fwf++) {
								EntityItem item = new EntityItem(worldObj, nx, ny, nz,
										new ItemStack(PhotonicItems.nougat, 1));
								item.motionX = (float) PhotonicUtils.rand.nextGaussian() * 0.2F;
								item.motionY = (float) PhotonicUtils.rand.nextGaussian() * 0.2F + 0.2F;
								item.motionZ = (float) PhotonicUtils.rand.nextGaussian() * 0.2F;
								worldObj.spawnEntity(item);
							}
							break;
						}
					}
				}
				break;
			} else if (b == PhotonicBlocks.yttriumOre) {
				if (j == 1 && ignoreFirst >= 0)
					continue;
				if (Math.random() < 0.3 && !worldObj.isRemote) {
					if (laserPower >= 20) {
						double d = Math.min(laserPower, 200) * 0.005d;
						d = d * d * d * d * d;
						boolean destroy = d >= Math.random();
						if (destroy) {
							worldObj.createExplosion(null, nx, ny, nz, 2.0f, false);
							worldObj.setBlockToAir(nbp);
							EntityItem item = new EntityItem(worldObj, nx, ny, nz,
									new ItemStack(PhotonicItems.yttriumIngot, 1));
							item.motionX = (float) PhotonicUtils.rand.nextGaussian() * 0.2F;
							item.motionY = (float) PhotonicUtils.rand.nextGaussian() * 0.2F + 0.2F;
							item.motionZ = (float) PhotonicUtils.rand.nextGaussian() * 0.2F;
							worldObj.spawnEntity(item);
							break;
						}
					}
				}
				break;
			} else if (b == PhotonicBlocks.laserDetector) {
				((TileEntityLaserDetector) worldObj.getTileEntity(nbp)).setTicks(10);
			} else if (b == PhotonicBlocks.laserDetector2) {
				((TileEntityLaserDetector) worldObj.getTileEntity(nbp)).setTicks(10);
				((TileEntityLaserDetector2) worldObj.getTileEntity(nbp)).addLaser(lasertick,
						new Object[] { laserColor, laserPower, laserDirection.name().toLowerCase() });
			}
		}
		EntityLaserBeam e = new EntityLaserBeam(worldObj, len, laserColor, laserPower, i < 2, i != 0, yaws[i]);
		e.setPositionAndRotation(x, y, z, yaws[i], pitches[i]);
		if (len <= 0)
			return new EntityLaserBeamResult(null, Type.HITEND);
		if (puny)
			e.setPuny();
		float f = 0.01f;
		if (typ != 0 && typ != 2 && puny) {
			int ri = PhotonicUtils.convertEnumFacingToInt(PhotonicUtils.convertIntToEnumFacing(i).getOpposite());
			EntityLaserBeam e2 = new EntityLaserBeam(worldObj, f, laserColor, laserPower, ri < 2, ri != 0, yaws[ri]);
			e2.setPositionAndRotation(x + (len * laserDirection.getFrontOffsetX()),
					y + (len * laserDirection.getFrontOffsetY()), z + (len * laserDirection.getFrontOffsetZ()),
					yaws[ri], pitches[ri]);
			worldObj.spawnEntity(e2);
			e.setPunyTrail(e2);
		}
		if (typ == 1)
			return new EntityLaserBeamResult(e, Type.HITEND);
		if (typ == 2)
			return new EntityLaserBeamResult(e, Type.NEWDIRECTION, PhotonicUtils.convertIntToEnumFacing(tyc));
		if (typ == 3)
			return new EntityLaserBeamResult(e, Type.NEWCOLOR, tyc, tym);
		if (typ == 4)
			return new EntityLaserBeamResult(e, Type.NEWDIRCLONE, PhotonicUtils.convertIntToEnumFacing(tyc & 0xF),
					PhotonicUtils.convertIntToEnumFacing((tyc & 0xF0) >> 4));
		if (typ == 5)
			return new EntityLaserBeamResult(e, Type.PRISM, laserDirection);
		if (typ == 6)
			return new EntityLaserBeamResult(e, Type.NEWCOLORDIR, tyd, tyc, tym);
		return new EntityLaserBeamResult(e, Type.SUCCESS);
	}

	private static int blockDifferenceTaxicab(BlockPos op, BlockPos np) {
		return MathHelper.abs(op.getX() - np.getX()) + MathHelper.abs(op.getY() - np.getY()) + MathHelper.abs(op.getZ() - np.getZ());
	}

	private static int blockDifferenceTaxicab(float x, float y, float z, float f, float g, float h) {
		return blockDifferenceTaxicab(new BlockPos(x, y, z), new BlockPos(f, g, h));
	}

	private static Vec3d vec3d_scale(Vec3d vec, double len) {
		return new Vec3d(vec.x * len, vec.y * len, vec.z * len);
	}

	private static AxisAlignedBB constructLaserAABB(float x, float y, float z, EnumFacing laserDirection, float len,
			int laserPower) {
		float scale = computeLaserScale(laserPower) * 0.0625f * 0.5f;
		switch (laserDirection) {
		case WEST:
			return new AxisAlignedBB(x - len, y - scale, z - scale, x, y + scale, z + scale);
		case EAST:
			return new AxisAlignedBB(x, y - scale, z - scale, x + len, y + scale, z + scale);
		case DOWN:
			return new AxisAlignedBB(x - scale, y - len, z - scale, x + scale, y, z + scale);
		case UP:
			return new AxisAlignedBB(x - scale, y, z - scale, x + scale, y + len, z + scale);
		case NORTH:
			return new AxisAlignedBB(x - scale, y - scale, z - len, x + scale, y + scale, z);
		case SOUTH:
			return new AxisAlignedBB(x - scale, y - scale, z, x + scale, y + scale, z + len);
		}
		return null;
	}

	private static EntityCollisionResult findEntityCollision(World world, float x, float y, float z, EnumFacing laserDirection, AxisAlignedBB aabb) {
		List<Entity> list = world.<Entity>getEntitiesWithinAABB(Entity.class, aabb);
		float dist = Float.MAX_VALUE;
		Entity hitEntity = null;
		for (Entity e: list) {
			if ((e instanceof EntityItem) || (e instanceof EntityBoat) || (e instanceof EntityMinecart)
					|| (e instanceof EntityArrow) || (e instanceof EntityThrowable)
					|| (e instanceof EntityFallingBlock) || (e instanceof EntityTNTPrimed)
					|| (e instanceof EntityLivingBase)) {
				if (e.getEntityBoundingBox() == null) continue;
				float compdist = dist;
				switch (laserDirection) {
				case UP: // +y
					compdist = (float) e.getEntityBoundingBox().minY - y;
					break;
				case DOWN: // -y
					compdist = y - (float) e.getEntityBoundingBox().maxY;
					break;
				case SOUTH: // +z
					compdist = (float) e.getEntityBoundingBox().minZ - z;
					break;
				case NORTH: // -z
					compdist = z - (float) e.getEntityBoundingBox().maxZ;
					break;
				case EAST: // +x
					compdist = (float) e.getEntityBoundingBox().minX - x;
					break;
				case WEST: // -x
					compdist = x - (float) e.getEntityBoundingBox().maxX;
					break;
				}
				if (dist > compdist) {
					dist = compdist;
					hitEntity = e;
				}
			}
		}
		if (hitEntity == null)
			return null;
		return new EntityCollisionResult(hitEntity, laserDirection, dist);
	}

	public static float computeLaserScale(int laserPower) {
		return (float) (1 + Math.log(laserPower) / Math.log(16)) * 6 - 4;
	}

	private PhotonicLaser() {
	}
}
