package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicGammaEffectPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicReflections;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockGammaRayCannon;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EnergyContainer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class TileEntityGammaRayCannon extends TileEntity implements ITickable {
	public static final int MAX_RF_CAPACITY = 10000000;
	EnergyContainer festorage;
	private boolean poweredLastTick = false;

	public TileEntityGammaRayCannon() {
		this.festorage = new EnergyContainer(MAX_RF_CAPACITY);
		this.poweredLastTick = false;
	}

	private void writeSyncableDataToNBT(NBTTagCompound syncData) {
		syncData.setInteger("fenergy", this.festorage.getEnergyStored());
		syncData.setBoolean("poweredLastTick", poweredLastTick);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		NBTTagList nbttaglist = new NBTTagList();
		writeSyncableDataToNBT(p_145841_1_);
		return super.writeToNBT(p_145841_1_);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		readSyncableDataFromNBT(p_145839_1_);
	}

	private void readSyncableDataFromNBT(NBTTagCompound func_148857_g) {
		this.festorage.setEnergyStored(func_148857_g.getInteger("fenergy"));
		poweredLastTick = func_148857_g.getBoolean("poweredLastTick");
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (this.world != null) {
				this.world.markChunkDirty(this.pos, this);
			}
			return CapabilityEnergy.ENERGY.cast(this.festorage);
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void update() {
		if (!this.world.isRemote) {
			boolean isFull = this.festorage.getEnergyStored() == MAX_RF_CAPACITY;
			boolean powered = this.world.isBlockIndirectlyGettingPowered(pos) > 0;
			boolean shouldFire = isFull && powered && !poweredLastTick;
			poweredLastTick = powered;
			if ((this.world.getTotalWorldTime() % 4L) == 0) {
				boolean appearsFull = PhotonicUtils.readBoolProperty(this.world.getBlockState(pos), BlockGammaRayCannon.full);
				if (isFull != appearsFull) {
					this.world.setBlockState(pos, this.world.getBlockState(pos).withProperty(BlockGammaRayCannon.full, isFull));
				}
			}
			if (shouldFire) {
				this.festorage.extractEnergy(MAX_RF_CAPACITY, false);
				this.world.setBlockState(pos, this.world.getBlockState(pos).withProperty(BlockGammaRayCannon.full, false));
				this.world.playSound(null, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5,
						SoundEvent.REGISTRY.getObject(new ResourceLocation("photoniccraft:deathray.fire")),
						SoundCategory.PLAYERS, 3.5F, 1.0F);
				BlockPos cpos = new BlockPos(pos);
				EnumFacing thisdir = PhotonicUtils.readDirectionProperty(this.world.getBlockState(pos), BlockDirectional.FACING);
				List<EntityLiving> victims = new ArrayList<>();
				for (int i = 0; i < 8; ++i) {
					cpos = cpos.offset(thisdir);
					if (this.world.getBlockLightOpacity(cpos) > 0) break;
					double x = cpos.getX() + .5; 
					double y = cpos.getY() + .5; 
					double z = cpos.getZ() + .5;
					ModPhotonicCraft.network.sendToAllAround(new PhotonicGammaEffectPacket(this.world, x, y, z), new TargetPoint(this.world.provider.getDimension(), x, y, z, 128));
					for (EntityLiving el: this.world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(cpos))) {
						victims.add(el);
 					}
				}
				int baseDamage = 5000;
				int j = victims.size(), k = j;
				EntityPlayer plr = this.world.getClosestPlayer(this.pos.getX(), this.pos.getY(), this.pos.getZ(), Double.POSITIVE_INFINITY, false);
				if (j > 0) {
					for (EntityLiving victim: victims) {
						--j;
						if (plr != null)
							victim.attackEntityFrom(DamageSource.causePlayerDamage(plr), 0.0f);
						if (victim instanceof EntityDragon) {
							PhotonicReflections.attackDragonFrom(((EntityDragon)victim), BlockGammaRayCannon.gammaDamage, calcDamage(baseDamage, j, k));
						} else if (plr != null) {
							if (!victim.attackEntityFrom(BlockGammaRayCannon.gammaDamage, calcDamage(baseDamage, j, k))) {
								boolean oldVulnerable = plr.getIsInvulnerable();
								plr.setEntityInvulnerable(true);
								victim.attackEntityFrom(DamageSource.causePlayerDamage(plr), calcDamage(baseDamage, j, k));
								if (!oldVulnerable)
									plr.setEntityInvulnerable(false);
							}
						}
						Vec3d vel = computeVelocity(new Vec3d(this.pos).addVector(0.5, 0.5, 0.5), victim.getPositionVector(), getWeight(victim));
						victim.motionX += vel.x;
						victim.motionY += vel.y * (PhotonicUtils.readDirectionProperty
									(world.getBlockState(pos), BlockDirectional.FACING)
									== EnumFacing.UP && (vel.y > 0) ? 2.0 : 1.0) + 0.25;
						victim.motionZ += vel.z;
					}
				}
				for (EntityPlayer p: this.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos).grow(8.0))) {
					ModPhotonicCraft.trg_DEATHRAY_FIRE.trigger((EntityPlayerMP) p);
				}
			}
		}
	}

	private double getWeight(EntityLiving victim) {
		AxisAlignedBB aabb = victim.getEntityBoundingBox();
		if (aabb == null) {
			return 1.0;
		}
		double w = (aabb.maxX - aabb.minX) * (aabb.maxY - aabb.minY) * (aabb.maxZ - aabb.minZ);
		if (w == 0) {
			return 8;
		}
		return w;
	}

	private Vec3d computeVelocity(Vec3d origin, Vec3d target, double weight) {
		Vec3d diffnorm = target.subtract(origin).normalize();
		return diffnorm.scale(Math.min(30.0, 2.0 / Math.sqrt(weight)));
	}

	private float calcDamage(int baseDamage, int j, int k) {
		return (baseDamage / k) + ((j - (k / 2f)) / (k / 2f)) * baseDamage * 0.1f;
	}
}
