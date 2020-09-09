package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import cofh.api.energy.EnergyStorage;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.block.BlockLightAir;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class TileEntityLightAir extends TileEntity {
	public static final int DEF_TICKS = 5;
	public int ticksLeft = DEF_TICKS;

    public TileEntityLightAir() {
    	super();
    	this.ticksLeft = DEF_TICKS;
    }
    @Override
	public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.ticksLeft = p_145839_1_.getInteger("ticksLeft");
    }
    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("ticksLeft", ticksLeft);
    }
    @Override
    public void updateEntity() {
		if (this.worldObj.isRemote) return;
    	if (ticksLeft <= 0) {
    		if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == Blocks.air || this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == ModPhotonicCraft.lightAir) {
    			this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.air, 0, 3);
    		}
			this.updateLights(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    	} else
        	ticksLeft--;
    }
    public static final EnumSkyBlock lighttype = EnumSkyBlock.Block;
    public static void updateLights(World w, int x, int y, int z) {
    	int ix = (int)x, iy = (int)y, iz = (int)z;
    	
        w.markBlockRangeForRenderUpdate(ix, iy, iz, 30, 30, 30);
        w.markBlockForUpdate(ix, iy, iz);

        w.updateLightByType(lighttype, ix, iy, iz);
        w.updateLightByType(lighttype, ix +1, iy, iz);
        w.updateLightByType(lighttype, ix +1, iy, iz +1);
        w.updateLightByType(lighttype, ix +1, iy, iz -1);
        w.updateLightByType(lighttype, ix -1, iy, iz +1);
        w.updateLightByType(lighttype, ix -1, iy, iz -1);
        w.updateLightByType(lighttype, ix -1, iy, iz);
        w.updateLightByType(lighttype, ix, iy, iz +1);
        w.updateLightByType(lighttype, ix, iy, iz -1);
        
        
        w.updateLightByType(lighttype, ix, iy +1, iz);
        w.updateLightByType(lighttype, ix +1, iy +1, iz);
        w.updateLightByType(lighttype, ix +1, iy +1, iz +1);
        w.updateLightByType(lighttype, ix +1, iy +1, iz -1);
        w.updateLightByType(lighttype, ix -1, iy +1, iz +1);
        w.updateLightByType(lighttype, ix -1, iy +1, iz -1);
        w.updateLightByType(lighttype, ix -1, iy +1, iz);
        w.updateLightByType(lighttype, ix, iy +1, iz +1);
        w.updateLightByType(lighttype, ix, iy +1, iz -1);
        w.updateLightByType(lighttype, ix, iy -1, iz);
        w.updateLightByType(lighttype, ix +1, iy -1, iz);
        w.updateLightByType(lighttype, ix +1, iy -1, iz +1);
        w.updateLightByType(lighttype, ix +1, iy -1, iz -1);
        w.updateLightByType(lighttype, ix -1, iy -1, iz +1);
        w.updateLightByType(lighttype, ix -1, iy -1, iz -1);
        w.updateLightByType(lighttype, ix -1, iy -1, iz);
        w.updateLightByType(lighttype, ix, iy -1, iz +1);
        w.updateLightByType(lighttype, ix, iy -1, iz -1);
	}
}
