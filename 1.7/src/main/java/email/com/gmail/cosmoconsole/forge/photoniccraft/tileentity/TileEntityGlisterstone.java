package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;

public class TileEntityGlisterstone extends TileEntity {
	public int light = 0;
	int tick = 5;
	int buffer = 0;

    public TileEntityGlisterstone() {
    	super();
    	this.light = 0;
    	this.tick = 5;
    	this.buffer = 0;
    }
    @Override
	public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.light = p_145839_1_.getInteger("light");
        this.tick = p_145839_1_.getInteger("delay");
        this.buffer = p_145839_1_.getInteger("buffer");
    }
    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("light", light);
        p_145841_1_.setInteger("delay", tick);
        p_145841_1_.setInteger("buffer", buffer);
    }
	private int getSkyLight() {
    	int i1 = worldObj.getSavedLightValue(EnumSkyBlock.Sky, this.xCoord, this.yCoord, this.zCoord) - worldObj.skylightSubtracted;
        float f = worldObj.getCelestialAngleRadians(1.0F);

        if (f < (float)Math.PI)
        {
            f += (0.0F - f) * 0.2F;
        }
        else
        {
            f += (((float)Math.PI * 2F) - f) * 0.2F;
        }

        i1 = Math.round((float)i1 * MathHelper.cos(f));

        if (i1 < 0)
        {
            i1 = 0;
        }

        if (i1 > 15)
        {
            i1 = 15;
        }
        return i1;
	}
    long ticks = 0L;
    @Override
    public void updateEntity() {
    	if (this.tick-->0)return;
    	ticks++;
		int myl = Math.min(15, this.getSkyLight() + this.worldObj.getSavedLightValue(EnumSkyBlock.Block, xCoord, yCoord, zCoord));
    	if (this.light >= myl && this.light > 0 && (ticks % 60L == 0L)) {
    		this.light--;
    		this.buffer = 0;
    		TileEntityLightAir.updateLights(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    	} else if ((myl - 1) > this.light) {
    		if (this.buffer >= 10) {
    			this.light = Math.max(0, myl - 1);
        		this.buffer = 0;
    		} else {
    			this.buffer++;
    		}
    	}
    	if ((myl - 1) <= this.light)
    		this.buffer = 0;
    	if (Math.random() < 0.25) {
        	if (Math.random() < 0.4) {
        		this.light--;
        	}
    		this.trySpread(1,0,0);
    		this.trySpread(-1,0,0);
    		this.trySpread(0,1,0);
    		this.trySpread(0,-1,0);
    		this.trySpread(0,0,1);
    		this.trySpread(0,0,-1);
    	}
    }
    private void trySpread(int i, int j, int k) {
		if (yCoord == 0 && j < 0) return;
		if (yCoord == (this.worldObj.getHeight() - 1) && j > 0) return;
		if (this.worldObj.getBlock(this.xCoord + i, this.yCoord + j, this.zCoord + k) == ModPhotonicCraft.glisterstone) {
			try {
				TileEntityGlisterstone te = (TileEntityGlisterstone) this.worldObj.getTileEntity(this.xCoord + i, this.yCoord + j, this.zCoord + k);
				if (this.light > te.light)
					te.light = this.light;
			} catch (Exception ex) {}
		}
	}
	public int getLight() {
		return light;
	}

}
