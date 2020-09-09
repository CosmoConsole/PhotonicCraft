package email.com.gmail.cosmoconsole.forge.photoniccraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import cofh.lib.util.helpers.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IComputerAccess;
import email.com.gmail.cosmoconsole.forge.photoniccraft.EntityLaserBeamResult.Type;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockMerger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityGlisterstone;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserDetector2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserMerger;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

public class PhotonicAPI {
	public static final int MAX_CHANNEL = 1073741024;
	public static Random rand = new Random();
	public static long laserId = 0L;
	public static long lastFired = 0L;
	public static HashMap<Long, ArrayList<Long>> already = new HashMap<Long, ArrayList<Long>>();
	public synchronized static EntityLaserBeam[] shootBeam(World worldObj, float x, float y,
			float z, LaserDirection laserDirection, int laserColor,
			int laserPower, float maxLen, boolean puny, int ignoreFirst) {
		if (worldObj.getTotalWorldTime() > lastFired) {
			lastFired = worldObj.getTotalWorldTime();
			laserId = 0L;
		}
		already.put(laserId, new ArrayList<Long>());
		return shootBeam(worldObj, x, y, z, laserDirection, laserColor, laserPower, maxLen, puny, ignoreFirst, laserId++);
	}
	public synchronized static EntityLaserBeam[] shootBeam(World worldObj, float x, float y,
			float z, LaserDirection laserDirection, int laserColor,
			int laserPower, float maxLen, boolean puny, int ignoreFirst, long cLaserId) {
		if (laserPower < 1) return new EntityLaserBeam[]{};
		float totalLen = 0.0f;
		ArrayList<EntityLaserBeam> beams = new ArrayList<EntityLaserBeam>();
		while (totalLen < maxLen) {
			EntityLaserBeamResult e = shootSingleBeam(worldObj, x, y, z, laserDirection, laserColor, laserPower, puny, ignoreFirst);
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
			case DOWN:		y -= len;
				break;
			case EAST:		x += len;
				break;
			case NORTH:		z -= len;
				break;
			case SOUTH:		z += len;
				break;
			case UP:		y += len;
				break;
			case WEST:		x -= len;
				break;
			default:
				break;
			}
			long pos = (MathHelper.floor(y) << 50) | (MathHelper.floor(z) << 25) | (MathHelper.floor(x));
			if (e.type == Type.NEWDIRECTION || (e.type == Type.NEWDIRCLONE && laserPower <= 1))
				laserDirection = e.direction;
			if (e.type == Type.NEWCOLOR) {
				if (e.colormeta >= 0) ignoreFirst = e.colormeta;
				laserColor = e.newcolor;
			}
			if (e.type == Type.NEWDIRCOLOR) {
				laserDirection = e.direction;
				if (e.colormeta >= 0) ignoreFirst = e.colormeta;
				laserColor = e.newcolor;
			}
			if (e.type == Type.NEWDIRCLONE && laserPower > 1) {
				laserDirection = e.direction;
				if (already.get(cLaserId).contains(pos)) {
					laserColor = 0;
					laserPower = 0;
				} else
					for (EntityLaserBeam b: shootBeam(worldObj, x, y, z, e.direction2, laserColor, laserPower >> 1, maxLen - totalLen - len, puny, -1, cLaserId))
						beams.add(b);
				already.get(cLaserId).add(pos);
				laserPower = laserPower >> 1;
			}
			if (e.type == Type.PRISM && laserPower >= 3) {
				if (!already.get(cLaserId).contains(pos)) {
					for (EntityLaserBeam b: shootBeam(worldObj, x, y, z, laserDirection.left(), 0xFF0000, (int)(Math.ceil(((laserColor & 0xFF0000) >> 16) / 255.D) * laserPower / 3), maxLen - totalLen - len, puny, -1, cLaserId))
						beams.add(b);
					for (EntityLaserBeam b: shootBeam(worldObj, x, y, z, laserDirection.right(), 0x0000FF, (int)(Math.ceil(((laserColor & 0xFF)) / 255.D) * laserPower / 3), maxLen - totalLen - len, puny, -1, cLaserId))
						beams.add(b);
				} else {
					laserColor = 0;
					laserPower = 0;
				}
				already.get(cLaserId).add(pos);
				laserColor = 0x00FF00;
				laserPower = (int)(Math.ceil(((laserColor & 0xFF00) >> 8) / 255.D) * laserPower / 3);
			}
			if (e.type == Type.NEWCOLORDIR) {
				laserDirection = e.direction;
				laserColor = e.newcolor;
				laserPower = e.colormeta;
			}
			totalLen += len;
			if (laserPower == 0) break;
			if (laserColor == 0) break;
		}
		return beams.toArray(new EntityLaserBeam[]{});
		//return new EntityLaserBeam[]{shootSingleBeam(worldObj, x, y, z, laserDirection, laserColor, laserPower)};
	}
	public static int getRGBFromMeta(int meta) {
		int col = 0;
		switch (meta) {
		case 0 : col = 0x202020; break;
		case 1 : col = 0xFF0000; break;
		case 2 : col = 0x008000; break;
		case 3 : col = 0x805834; break;
		case 4 : col = 0x0000FF; break;
		case 5 : col = 0x8000B0; break;
		case 6 : col = 0x00FFFF; break;
		case 7 : col = 0xa0a0a0; break;
		case 8 : col = 0x606060; break;
		case 9 : col = 0xff8060; break;
		case 10: col = 0x00ff00; break;
		case 11: col = 0xffff00; break;
		case 12: col = 0x6699ff; break;
		case 13: col = 0xc000ff; break;
		case 14: col = 0xff8000; break;
		case 15: col = 0xffffff; break;
		case -1:				 return 0x000000;
		}
		return col;
	}
	public static DamageSource laserDamage = new DamageSource("photonicLaser").setDamageBypassesArmor().setFireDamage();
	public static EntityLaserBeamResult shootSingleBeam(World worldObj, float x, float y,
			float z, LaserDirection laserDirection, int laserColor,
			int laserPower, boolean puny, int ignoreFirst) {
		if (laserColor == 0) return new EntityLaserBeamResult(null, Type.HITEND);
		final float[] yaws = {180f, 0f, 0f, 180f, -90f, 90f};
		final float[] pitches = {90f, -90f, 0f, 0f, 0f, 0f};
		int i = laserDirection.ordinal();
		float len = 32.0f;
		// UP, DOWN, NORTH, SOUTH, WEST, EAST;
		final int ox[] = { 0,  0,  0,  0, -1, +1};
		final int oy[] = {+1, -1,  0,  0,  0,  0};
		final int oz[] = { 0,  0, -1, +1,  0,  0};
		int typ = 0;
		int tyc = 0;
		int tym = -1;
		LaserDirection tyd = LaserDirection.DOWN;
		int nx = 0;
		int ny = 0;
		int nz = 0;
		int jk = 0;
		float le = 0.0f;
		long lasertick = worldObj.getTotalWorldTime();
		for (int j = 1; j < len; j++) {
			jk = j;
			float bx = x + (j*ox[i]);
			float by = y + (j*oy[i]);
			float bz = z + (j*oz[i]);
			if (by >= 260) {
				typ = 1;
				tyc = 0;
				break;
			}
			final float bb = 0.05f;
			for (float n = 0.875f; n >= 0.0f; n -= 0.125f) {
				float cx = (float) (x + sigsub(j*(ox[i]),n));
				float cy = (float) (y + sigsub(j*(oy[i]),n));
				float cz = (float) (z + sigsub(j*(oz[i]),n));
				for (Object e: worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(cx-bb, cy-bb, cz-bb, cx+bb, cy+bb, cz+bb))) {
					if ((e instanceof EntityItem) || 
							(e instanceof EntityBoat) ||
							(e instanceof EntityMinecart) || 
							(e instanceof EntityArrow) ||
							(e instanceof EntityThrowable) ||
							(e instanceof EntityFallingBlock) ||
							(e instanceof EntityTNTPrimed) || 
							(e instanceof EntityLivingBase)) {
						if (laserPower >= 100) {
							if (e instanceof EntityItem && ((EntityItem)e).getEntityItem().getItem() != ModPhotonicCraft.yttriumIngot && ((EntityItem)e).getEntityItem().getItem() != ModPhotonicCraft.nougat)
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
								((EntityTNTPrimed) e).fuse = 0;
						}
						int everyTicks = (int)Math.ceil(100.0 / Math.min(laserPower, 100));
						everyTicks *= everyTicks;
						everyTicks = (int)Math.ceil(everyTicks * 0.5d);
				    	if (worldObj.getTotalWorldTime() % everyTicks == 0L && (e instanceof EntityLivingBase)) {
							double d = Math.min(laserPower, 100) * 0.01d;
							d = d * d * d * d * d;
							boolean destroy = d >= Math.random();
							EntityLivingBase el = (EntityLivingBase)e;
							el.attackEntityFrom(laserDamage, (laserPower / 20));
							if (destroy)
								el.setFire(5 * (int)Math.ceil(Math.random() * laserPower));
				    	}
						typ = 1;
						tyc = 1;
						le = -n + 0.4f;
						break;
					}
				}
			}
			if (tyc > 0) break;
			nx = (int) Math.floor(bx);
			ny = (int) Math.floor(by);
			nz = (int) Math.floor(bz);
			Block b = worldObj.getBlock(nx, ny, nz);
			if (!b.isAir(worldObj, nx, ny, nz)) {
				if (b == Blocks.glass || b == Blocks.glass_pane) continue;
				if (b == Blocks.stained_glass || b == Blocks.stained_glass_pane) {
					int meta = worldObj.getBlockMetadata(nx, ny, nz);
					if (j == 1 && ignoreFirst==meta) continue;
					typ = 3;
					tym = meta;
					int col = getRGBFromMeta(15 - meta);
					tyc = laserColor & col;
					if (b == Blocks.stained_glass_pane) le = 7/16;
					break;
				}
				else if (b == ModPhotonicCraft.glisterstone) {
					if (j == 1 && ignoreFirst>=0) continue;
					if (!worldObj.isRemote) {
						if (laserPower >= 20) {
							double d = Math.min(laserPower, 100) * 0.01d;
							d = d * d * d * d * d;
							boolean destroy = d >= Math.random();
							if (destroy) {
								try {
									TileEntityGlisterstone gs = (TileEntityGlisterstone)worldObj.getTileEntity(nx, ny, nz);
									gs.light = (int)Math.max(gs.light, Math.min(15, laserPower / 6));
								} catch (Exception ex) {}
							}
						}
					}	
					typ = 1;
					tyc = 0;
					break;
				} else if (b == ModPhotonicCraft.laserGenerator) {
					if (j == 1 && ignoreFirst>=0) continue;
					try {
						TileEntityLaserGenerator gs = (TileEntityLaserGenerator)worldObj.getTileEntity(nx, ny, nz);
						gs.addLaser(laserPower);
					} catch (Exception ex) {}
					typ = 1;
					tyc = 0;
					break;
				} else if (b == ModPhotonicCraft.laserCharger) {
					if (j == 1 && ignoreFirst>=0) continue;
					try {
						TileEntityLaserCharger gs = (TileEntityLaserCharger)worldObj.getTileEntity(nx, ny, nz);
						gs.addSignal(laserPower);
					} catch (Exception ex) {}
					typ = 1;
					tyc = 0;
					break;
				} else if (b == Blocks.furnace || b == Blocks.lit_furnace) {
					if (j == 1 && ignoreFirst>=0) continue;
					try {
						TileEntityFurnace fs = (TileEntityFurnace)worldObj.getTileEntity(nx, ny, nz);
						if (!worldObj.isRemote) {
							if (laserPower >= 20) {
								double d = Math.min(laserPower, 100) * 0.01d;
								d = d * d * d * d * d;
								boolean destroy = d >= Math.random();
								if (destroy) {
									if (fs.currentItemBurnTime == 0)
										fs.currentItemBurnTime = 5;
									fs.furnaceBurnTime = Math.min(fs.currentItemBurnTime, fs.furnaceBurnTime + 1);
								}
							}
						}	
					} catch (Exception ex) {}
					typ = 1;
					tyc = 0;
					break;
				} else if (b == Blocks.diamond_ore) {
					if (j == 1 && ignoreFirst>=0) continue;
					if (Math.random() < 0.002 && !worldObj.isRemote) {
						if (laserPower >= 100) {
							double d = Math.min(laserPower, 100) * 0.01d;
							d = d * d * d * d * d;
							boolean destroy = d >= Math.random();
							if (destroy) {
								worldObj.createExplosion(null, nx, ny, nz, 2.0f, false);
								worldObj.setBlockToAir(nx, ny, nz); 
								for (int fwf = 0; fwf < 5; fwf++) {
									EntityItem item = new EntityItem(worldObj, nx, ny, nz, new ItemStack(ModPhotonicCraft.nougat, 1));
					                item.motionX = (double)((float)rand.nextGaussian() * 0.2F);
					                item.motionY = (double)((float)rand.nextGaussian() * 0.2F + 0.2F);
					                item.motionZ = (double)((float)rand.nextGaussian() * 0.2F);
									worldObj.spawnEntityInWorld(item);
								}
								break;
							}
						}
					}
					typ = 1;
					tyc = 0;
					break;
				} else if (b == ModPhotonicCraft.yttriumOre) {
					if (j == 1 && ignoreFirst>=0) continue;
					if (Math.random() < 0.02 && !worldObj.isRemote) {
						if (laserPower >= 20) {
							double d = Math.min(laserPower, 100) * 0.01d;
							d = d * d * d * d;
							boolean destroy = d >= Math.random();
							if (destroy) {
								worldObj.createExplosion(null, nx, ny, nz, 2.0f, false);
								worldObj.setBlockToAir(nx, ny, nz);
								EntityItem item = new EntityItem(worldObj, nx, ny, nz, new ItemStack(ModPhotonicCraft.yttriumIngot, 1));
				                item.motionX = (double)((float)rand.nextGaussian() * 0.2F);
				                item.motionY = (double)((float)rand.nextGaussian() * 0.2F + 0.2F);
				                item.motionZ = (double)((float)rand.nextGaussian() * 0.2F);
								worldObj.spawnEntityInWorld(item);
								break;
							}
						}
					}
					typ = 1;
					tyc = 0;
					break;
				} else if (b == ModPhotonicCraft.laserRedstoneMirror) {
					final LaserDirection[][] mirrors = new LaserDirection[][]{
						{LaserDirection.NORTH, LaserDirection.SOUTH, LaserDirection.EAST},
						{LaserDirection.NORTH, LaserDirection.SOUTH, LaserDirection.WEST},
						{LaserDirection.WEST, LaserDirection.EAST, LaserDirection.DOWN},
						{LaserDirection.NORTH, LaserDirection.SOUTH, LaserDirection.DOWN},
						{LaserDirection.WEST, LaserDirection.EAST, LaserDirection.UP},
						{LaserDirection.NORTH, LaserDirection.SOUTH, LaserDirection.UP},
						{},
						{},
					};
					int meta = worldObj.getBlockMetadata(nx, ny, nz);
					boolean pass = false;
					if (meta < 6) {
						boolean powered = worldObj.isBlockIndirectlyGettingPowered(nx, ny, nz);
						if (laserDirection == mirrors[meta][0] || laserDirection == mirrors[meta][1]) {
							pass = true;
							if (powered) {
								tyc = mirrors[meta][2].ordinal();
								typ = 2;
								le = 0.5f;
								break;
							}
						}
					}
					if (!pass) {
						typ = 1;
						tyc = 0;
						break;
					}
				} else if (b == ModPhotonicCraft.laserMirror || b == ModPhotonicCraft.laserMirror2) {
					final LaserDirection[][] mirrors = new LaserDirection[][]{
						{LaserDirection.SOUTH, LaserDirection.EAST},
						{LaserDirection.SOUTH, LaserDirection.WEST},
						{LaserDirection.DOWN, LaserDirection.EAST},
						{LaserDirection.DOWN, LaserDirection.SOUTH},
						{LaserDirection.UP, LaserDirection.EAST},
						{LaserDirection.UP, LaserDirection.NORTH},
						{},
						{},
						{LaserDirection.NORTH, LaserDirection.WEST},
						{LaserDirection.NORTH, LaserDirection.EAST},
						{LaserDirection.DOWN, LaserDirection.WEST},
						{LaserDirection.DOWN, LaserDirection.NORTH},
						{LaserDirection.UP, LaserDirection.WEST},
						{LaserDirection.UP, LaserDirection.SOUTH},
						{},
						{},
					};
					int meta = worldObj.getBlockMetadata(nx, ny, nz);
					if ((meta&0x7) < 6) {
						int m = meta;
						if (laserDirection == mirrors[m][0]) {
							tyc = mirrors[m][1].inverse().ordinal();
							typ = 2;
							le = 0.5f;
							break;
						}
						if (laserDirection == mirrors[m][1]) {
							tyc = mirrors[m][0].inverse().ordinal();
							typ = 2;
							le = 0.5f;
							break;
						}
						m ^= 0x8;
						if (laserDirection == mirrors[m][0]) {
							if (b == ModPhotonicCraft.laserMirror2) {
								tyc = mirrors[m][1].inverse().ordinal();
								typ = 2;
								le = 0.5f;
								break;
							} else {
								typ = 1;
								le = 0.5f;
								break;
							}
						}
						if (laserDirection == mirrors[m][1]) {
							if (b == ModPhotonicCraft.laserMirror2) {
								tyc = mirrors[m][0].inverse().ordinal();
								typ = 2;
								le = 0.5f;
								break;
							} else {
								typ = 1;
								le = 0.5f;
								break;
							}
						}
						//if (b == ModPhotonicCraft.laserMirror2)
					}
					if (typ < 0) {
						typ = 1;
						break;
					}
					/*if (laserDirection == LaserDirection.SOUTH) {
						tyc = LaserDirection.UP.ordinal();
						typ = 2;
						le = 0.5f;
						break;
					}*/
				}
				else if (b == ModPhotonicCraft.laserSemiMirror || b == ModPhotonicCraft.laserSemiMirror2) {
					final LaserDirection[][] mirrors = new LaserDirection[][]{
						{LaserDirection.SOUTH, LaserDirection.EAST},
						{LaserDirection.SOUTH, LaserDirection.WEST},
						{LaserDirection.DOWN, LaserDirection.EAST},
						{LaserDirection.DOWN, LaserDirection.SOUTH},
						{LaserDirection.UP, LaserDirection.EAST},
						{LaserDirection.UP, LaserDirection.NORTH},
						{},
						{},
						{LaserDirection.NORTH, LaserDirection.WEST},
						{LaserDirection.NORTH, LaserDirection.EAST},
						{LaserDirection.DOWN, LaserDirection.WEST},
						{LaserDirection.DOWN, LaserDirection.NORTH},
						{LaserDirection.UP, LaserDirection.WEST},
						{LaserDirection.UP, LaserDirection.SOUTH},
					};
					int meta = worldObj.getBlockMetadata(nx, ny, nz);
					if ((meta&0x7) < 6) {
						int m = meta;
						if (laserDirection == mirrors[m][0]) {
							tyc = mirrors[m][1].inverse().ordinal() | laserDirection.ordinal() << 4;
							typ = 4;
							le = 0.5f;
							break;
						}
						if (laserDirection == mirrors[m][1]) {
							tyc = mirrors[m][0].inverse().ordinal() | laserDirection.ordinal() << 4;
							typ = 4;
							le = 0.5f;
							break;
						}
						m ^= 0x8;
						if (laserDirection == mirrors[m][0]) {
							if (b == ModPhotonicCraft.laserSemiMirror2) {
								tyc = mirrors[m][1].inverse().ordinal() | laserDirection.ordinal() << 4;
								typ = 4;
								le = 0.5f;
								break;
							} else {
								typ = 1;
								le = 0.5f;
								break;
							}
						}
						if (laserDirection == mirrors[m][1]) {
							if (b == ModPhotonicCraft.laserSemiMirror2) {
								tyc = mirrors[m][0].inverse().ordinal() | laserDirection.ordinal() << 4;
								typ = 4;
								le = 0.5f;
								break;
							} else {
								typ = 1;
								le = 0.5f;
								break;
							}
						}
						//if (b == ModPhotonicCraft.laserMirror2)
					}
					if (typ < 0) {
						typ = 1;
						break;
					}
					/*if (laserDirection == LaserDirection.SOUTH) {
						tyc = LaserDirection.UP.ordinal();
						typ = 2;
						le = 0.5f;
						break;
					}*/
				}
				else if (b == ModPhotonicCraft.laserMerger) {
					int meta = worldObj.getBlockMetadata(nx, ny, nz);
					if (BlockMerger.acceptableDirection(meta, laserDirection)) {
						//try {
							TileEntityLaserMerger telm = (TileEntityLaserMerger) worldObj.getTileEntity(nx, ny, nz);
							if (telm.laserTick >= lasertick) {
								if (telm.recLaser == 1) {
									telm.recLaser = 2;
									int _r = Math.min(255, ((laserColor & 0xFF0000) >> 16) + ((telm.laserColor & 0xFF0000) >> 16));
									int _g = Math.min(255, ((laserColor & 0xFF00) >> 8) + ((telm.laserColor & 0xFF00) >> 8));
									int _b = Math.min(255, ((laserColor & 0xFF)) + ((telm.laserColor & 0xFF)));
									typ = 6;
									tyc = (_r << 16) | (_g << 8) | (_b);
									tym = (int)Math.floor(telm.laserPower * 0.75f + laserPower * 0.75f);
									tyd = new LaserDirection[]{LaserDirection.UP,LaserDirection.DOWN,
											LaserDirection.NORTH, LaserDirection.SOUTH,
											LaserDirection.WEST, LaserDirection.EAST}[meta];
									le = 0.5f;
									break;
								}
							} else {
								telm.laserTick = lasertick;
								telm.recLaser = 1;
								telm.laserColor = laserColor;
								telm.laserPower = laserPower;
							}
						//} catch (Exception ex) {}
					}
					typ = 1;
					le = 0.5f;
					break;
				}
				else if (b == ModPhotonicCraft.prism) {
					typ = 5;
					le = 0.5f;
					break;
				}
				else if (b == ModPhotonicCraft.laserDetector) {
					((TileEntityLaserDetector) worldObj.getTileEntity(nx, ny, nz)).setTicks(3);
					worldObj.notifyBlocksOfNeighborChange(nx, ny, nz, worldObj.getBlock(nx, ny, nz));
					continue;
				}
				else if (b == ModPhotonicCraft.laserDetector2) {
					((TileEntityLaserDetector) worldObj.getTileEntity(nx, ny, nz)).setTicks(3);
					worldObj.notifyBlocksOfNeighborChange(nx, ny, nz, worldObj.getBlock(nx, ny, nz));
					((TileEntityLaserDetector2) worldObj.getTileEntity(nx, ny, nz)).addLaser(lasertick, new Object[]{laserColor, laserPower, laserDirection.name().toLowerCase()});
					continue;
				}
				else if (b == Blocks.water || b == Blocks.flowing_water) {
					if (j == 1 && ignoreFirst>=0) continue;
					typ = 3;
					int waterColor = worldObj.getBiomeGenForCoords(nx, nz).waterColorMultiplier;
					int wr = (waterColor >> 16) & 0xFF;
					int wg = (waterColor >>  8) & 0xFF;
					int wb = (waterColor      ) & 0xFF;
					wr = (wr * 0x30) >> 8;
					wg = (wg * 0x45) >> 8;
					wb = (wb * 0xF4) >> 8;
					waterColor = ((wr&0xFF) << 16) | ((wg&0xFF) << 8) | ((wb&0xFF));
					tyc = laserColor & waterColor;
					le = 1/16;
					break;
				} 
				else if ((b == Blocks.lava || b == Blocks.flowing_lava) && !(j == 1 && ignoreFirst>=0))
					le = 2/16;
				else if ((b.isNormalCube()) ||
						(b == Blocks.lava || b == Blocks.flowing_lava)) {
					if (j == 1 && ignoreFirst>=0) continue;
					typ = 1;
					tyc = 0;
					break;
				}
			}
		}
		if (typ > 0) {
			len = jk - (frac(x) * ox[i]) - (frac(y) * oy[i]) - (frac(z) * oz[i]);
			if ((ox[i] < 0 || oy[i] < 0 || oz[i] < 0)) 
				len -= 1f;
			len += le;
		}
		EntityLaserBeam e = new EntityLaserBeam(worldObj, len, laserColor, laserPower, i < 2, i != 0);
		e.setPositionAndRotation(x, y, z, yaws[i], pitches[i]);
		if (len <= 0)
			return new EntityLaserBeamResult(null, Type.HITEND);
		if (puny) e.setPuny();
		worldObj.spawnEntityInWorld(e);
		float f = 0.01f;
		if (typ != 0 && typ != 2 && puny) {
			int ri = LaserDirection.values()[i].inverse().ordinal();
			EntityLaserBeam e2 = new EntityLaserBeam(worldObj, f, laserColor, laserPower, ri < 2, ri != 0);
			e2.setPositionAndRotation(x + (len*ox[i]), y + (len*oy[i]), z + (len*oz[i]), yaws[ri], pitches[ri]);
			worldObj.spawnEntityInWorld(e2);
			e.setPunyTrail(e2);
		}
		if (typ == 1)
			return new EntityLaserBeamResult(e, Type.HITEND);
		if (typ == 2)
			return new EntityLaserBeamResult(e, Type.NEWDIRECTION, LaserDirection.values()[tyc]);
		if (typ == 3)
			return new EntityLaserBeamResult(e, Type.NEWCOLOR, tyc, tym);
		if (typ == 4)
			return new EntityLaserBeamResult(e, Type.NEWDIRCLONE, LaserDirection.values()[tyc&0xF], LaserDirection.values()[(tyc&0xF0)>>4]);
		if (typ == 5)
			return new EntityLaserBeamResult(e, Type.PRISM, laserDirection);
		if (typ == 6)
			return new EntityLaserBeamResult(e, Type.NEWCOLORDIR, tyd, tyc, tym);
		return new EntityLaserBeamResult(e, Type.SUCCESS);
	}
	private static double sigsub(double i, double n) {
		return i-Math.signum(i)*n;
	}
	private static float frac(float z) {
		return (float) (z - Math.floor(z));
	}
	@SideOnly(Side.CLIENT)
	public static Timer radioTimer;
	@SideOnly(Side.CLIENT)
	public static TimerTask radioTask;
	@SideOnly(Side.CLIENT)
	public static boolean radioPlaying;
	@SideOnly(Side.CLIENT)
	public static long lastPing;
	@SideOnly(Side.CLIENT)
	public static void stillPlaying() {
		lastPing = System.currentTimeMillis();	 
	}
	@SideOnly(Side.CLIENT)
	public static boolean shouldBePlaying() {
		return (System.currentTimeMillis() - lastPing) < 1500L;
	}
	@SideOnly(Side.CLIENT)
	public static void shouldNotBePlaying() {
		lastPing = 0L;
	}
	@SideOnly(Side.CLIENT)
	public static void startPlayingRadio() {
		radioPlaying = true;
		if (radioTask != null) radioTask.cancel();
		radioTask = new TimerTask() {
			@Override
			public void run() {
				try {
					PhotonicAPI.startPlayingRadioNow();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		radioTimer.schedule(radioTask, 600L);
	}
	@SideOnly(Side.CLIENT)
	public static void startPlayingRadioNow() {
		long start = System.currentTimeMillis();
		ModPhotonicCraft.sourceDataLine.start();
	}
	@SideOnly(Side.CLIENT)
	public static void stopPlayingRadio() {
		radioPlaying = false;
		if (radioTask != null) radioTask.cancel();
		radioTask = null;
		ModPhotonicCraft.sourceDataLine.stop();
		ModPhotonicCraft.sourceDataLine.flush();
	}
	public static long last_send;
	public static boolean shouldSend() {
		long now = System.currentTimeMillis();
		if ((now - last_send) >= 200L) {
			last_send = now;
			return true;
		}
		return false;
	}
	public static ConcurrentHashMap<UUID, Long> radioPlayers;
	public static ConcurrentHashMap<UUID, Integer> radioChannels;
	public static ConcurrentHashMap<Integer, UUID> chOwners;
	public static ConcurrentHashMap<Integer, Long> lastSend;
	public static ConcurrentHashMap<Integer, byte[]> sendCache;
	public static ConcurrentHashMap<Integer, byte[]> recvData;
	public static ConcurrentHashMap<Integer, Integer> sendPower;
	public static ConcurrentHashMap<Integer, Integer> recvPower;
	public static ConcurrentHashMap<Integer, PhotonicLocation> sendLoc;
	public static ConcurrentHashMap<Integer, PhotonicLocation> recvLoc;
	public static ConcurrentHashMap<UUID, Integer> ownedChannel;
	public static ArrayList<UUID> radioHandled;
	public static Timer serverTimer;
	public static Timer serverTimer2;
	public static boolean serverInited = false;
	public static AtomicBoolean waitSend = new AtomicBoolean(false);
	public static AtomicBoolean waitReceive = new AtomicBoolean(false);
	public static AtomicBoolean midSend = new AtomicBoolean(false);
	public static void blockUntilSend() {
		blockUntilSafe();
		PhotonicAPI.waitSend.set(false);
		long l = System.currentTimeMillis();
		while (!PhotonicAPI.waitSend.get()) {
			long n = System.currentTimeMillis();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			if ((n - l) > 200L) break;
		}
	}
	public static void sendEventWhenSend(final IComputerAccess computer) {
		new Thread() {
			@Override
			public void run() {
				PhotonicAPI.waitSend.set(false);
				long l = System.currentTimeMillis();
				while (!PhotonicAPI.waitSend.get()) {
					long n = System.currentTimeMillis();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
					if ((n - l) > 200L) break;
				}
				computer.queueEvent("photoniccraft_sendtimer", new Object[]{});
			}
		}.start();
	}
	public static void blockUntilReceive() {
		PhotonicAPI.waitReceive.set(false);
		long l = System.currentTimeMillis();
		while (!PhotonicAPI.waitReceive.get()) {
			long n = System.currentTimeMillis();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			if ((n - l) > 200L) break;
		}
	}
	public static void sendEventWhenRecv(final IComputerAccess computer) {
		new Thread() {
			@Override
			public void run() {
				PhotonicAPI.waitReceive.set(false);
				long l = System.currentTimeMillis();
				while (!PhotonicAPI.waitReceive.get()) {
					long n = System.currentTimeMillis();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
					if ((n - l) > 200L) break;
				}
				computer.queueEvent("photoniccraft_recvtimer", new Object[]{});
			}
		}.start();
	}
	public static void blockUntilSafe() {
		while (PhotonicAPI.midSend.get()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}
	public static boolean radioSendData(UUID senderId, int channel, byte[] data, PhotonicLocation loc, int power) {
		blockUntilSafe();
		if (!radioAuthorizedToSend(senderId, channel)) {
			return false;
		}
		sendCache.put(channel, data);
		sendPower.put(channel, power);
		sendLoc.put(channel, loc);
		chOwners.put(channel, senderId);
		ownedChannel.put(senderId, channel);
		lastSend.put(channel, System.currentTimeMillis());
		return true;
	}
	public static final String channel64 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";
	public static int channelNameToID(String str) {
		if (str.length() != 5) return -1;
		int result = 0;
		int m = 16777216;
		for (char c: str.toCharArray()) {
			int p = channel64.indexOf(c);
			if (p < 0) return -1;
			result += p * m;
			m >>= 6;
		}
		return result;
	}
	public static String channelIDToName(int ch) {
		if (ch < 0 || ch >= PhotonicAPI.MAX_CHANNEL) return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			sb.append(channel64.charAt(ch & 0x3F));
			ch >>= 6;
		}
		return sb.reverse().toString();
	}
	public static boolean radioAuthorizedToSend(UUID senderId, int channel) {
		if (getChannelOwner(channel) != null && !getChannelOwner(channel).equals(senderId))
			return false;
		return true;
	}
	private static Object getChannelOwner(int channel) {
		if (!chOwners.containsKey(channel)) return null;
		if (lastSend.containsKey(channel)) {
			long now = System.currentTimeMillis();			
			if ((now - (lastSend.get(channel))) >= 10000L)
				return null;
		} 
		return ownedChannel.get(chOwners.get(channel)) == channel ? chOwners.get(channel) : null;
	}
	public static int getActiveRadioPlayers() {
		blockUntilSafe();
		if (radioPlayers == null)
			startRadioServer();
		if (radioPlayers == null)
			return 0;
		return radioPlayers.size();
	}
	public static void startRadioServer() {
		serverTimer = new Timer();
		serverTimer2 = new Timer();
		radioPlayers = new ConcurrentHashMap<UUID, Long>();
		radioChannels = new ConcurrentHashMap<UUID, Integer>();
		radioHandled = new ArrayList<UUID>();
		chOwners = new ConcurrentHashMap<Integer, UUID>();
		sendCache = new ConcurrentHashMap<Integer, byte[]>();
		recvData = new ConcurrentHashMap<Integer, byte[]>();
		sendPower = new ConcurrentHashMap<Integer, Integer>();
		recvPower = new ConcurrentHashMap<Integer, Integer>();
		sendLoc = new ConcurrentHashMap<Integer, PhotonicLocation>();
		recvLoc = new ConcurrentHashMap<Integer, PhotonicLocation>();
		lastSend = new ConcurrentHashMap<Integer, Long>();
		ownedChannel = new ConcurrentHashMap<UUID, Integer>();
		System.out.println("Radio server started");
		serverTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (PhotonicAPI.midSend.get()) return;
				if (MinecraftServer.getServer().getConfigurationManager().playerEntityList.size() < 1) {
					PhotonicAPI.serverSideDeinit();
					return;
				}
				PhotonicAPI.midSend.set(true);
				radioHandled.clear();
				recvData.clear();
				recvPower.clear();
				recvLoc.clear();
				for (int channel: sendCache.keySet()) {
					recvData.put(channel, sendCache.get(channel));
					recvPower.put(channel, sendPower.get(channel));
					recvLoc.put(channel, sendLoc.get(channel));
				}
				sendCache.clear();
				sendPower.clear();
				sendLoc.clear();
				long now = System.currentTimeMillis();
				byte[] b = new byte[2205];
				Arrays.fill(b, (byte)0);
				ArrayList<UUID> removeKeys = new ArrayList<UUID>();
				byte[] d = new byte[2205];
				int m = 0;
				for (UUID u: radioPlayers.keySet()) {
					if (m >= ModPhotonicCraft.maximumRadio) break;
					if ((now - radioPlayers.get(u)) >= 1000L)
						removeKeys.add(u);
					else {
						int ch = radioChannels.get(u);
						double ampl = 0.0;
						Arrays.fill(d, (byte)0);
						EntityPlayerMP p = (EntityPlayerMP)getPlayerFromUUID(u);
						if (recvData.containsKey(ch)) {
							d = recvData.get(ch);
							ampl = calculateAmplitude(recvLoc.get(ch).distanceSq(PhotonicLocation.fromPlayer(p)), recvPower.get(ch));
						} 
						ModPhotonicCraft.network.sendTo(new PhotonicRadioPacket(ampl, ampl <= 0.0 ? b : d), p);
					}
				}
				for (UUID u: removeKeys)
					radioPlayers.remove(u);
				PhotonicAPI.waitSend.set(true);
				PhotonicAPI.midSend.set(false);
			}
		}, 10L, 200L);
		serverTimer2.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				PhotonicAPI.waitReceive.set(true);
			}
		}, 150L, 200L);
	}
	protected static double calculateAmplitude(double ds, Integer rf) {
		if (ds >= (4 * rf * rf)) return 0.0;
		if (ds <= (rf * rf)) return 1.0;
		double d = Math.sqrt(ds);
		double s = 1 - ((d - rf) / rf);
		return s;
	}
	protected static EntityPlayer getPlayerFromUUID(UUID u) {
		for (Object p: MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			if (((EntityPlayer)p).getUniqueID().equals(u))
				return (EntityPlayer)p;
		}
		return null;
	}
	public static boolean allowPlaying(EntityPlayerMP p_77663_3_, long currentTimeMillis, int channel) {
		if (radioPlayers == null) return false;
		if (radioChannels == null) return false;
		if (p_77663_3_ == null) return false;
		if (radioPlayers.contains(p_77663_3_.getUniqueID())) {
			if ((currentTimeMillis - radioPlayers.get(p_77663_3_.getUniqueID())) < 70L) {
				return false;
			}
		}
		blockUntilSafe();
		radioPlayers.put(p_77663_3_.getUniqueID(), currentTimeMillis);
		radioChannels.put(p_77663_3_.getUniqueID(), channel);
		radioHandled.add(p_77663_3_.getUniqueID());
		return true;
	}
	public static void serverSideInit() {
		if (serverInited) return;
		PhotonicAPI.last_send = 0L;
		PhotonicAPI.startRadioServer();
		serverInited = true;
	}
	public static void serverSideDeinit() {
		if (!serverInited) return;
		PhotonicAPI.last_send = 0L;
		PhotonicAPI.serverTimer.cancel();
		PhotonicAPI.serverTimer2.cancel();
		serverInited = false;
	}
	public static boolean worldEquals(World a, World b) {
		return a.getWorldInfo().getWorldName().equals(b.getWorldInfo().getWorldName()) && 
				a.provider.dimensionId == b.provider.dimensionId && 
				a.getTotalWorldTime() == b.getTotalWorldTime();
	}
	public static int unsign(byte b) {
		int i = (int)b;
		if (i < 0)
			return i + 256;
		return i;
	}
}
