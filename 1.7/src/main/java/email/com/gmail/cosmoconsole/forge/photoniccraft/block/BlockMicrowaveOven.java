package email.com.gmail.cosmoconsole.forge.photoniccraft.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityFluorescentLight;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityPrism;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntitySkyLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMicrowaveOven extends Block implements ITileEntityProvider {
	public BlockMicrowaveOven() {
		super(Material.glass);
		this.setHardness(5.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeStone);
		this.setBlockName(ModPhotonicCraft.MODID + "_microwaveOven");
		this.setBlockTextureName("minecraft:stone");
		this.setBlockBounds(1/16f, 0f, 1/16f, 15/16f, 10/16f, 15/16f);
		this.setLightOpacity(0);
	}
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCubeDefault(x, y-1, z, false);
	}
    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return ModPhotonicCraft.laserItem;
    }
    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return ModPhotonicCraft.laserItem;
    }
	@Override
    public int getRenderType()
    {
        return -1;
    }
    @Override
    public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_)
    {
        return 10;
    }
    @Override
    public int damageDropped(int p_149692_1_)
    {
        return 10;
    }


    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		try {
			TileEntity te = world.getTileEntity(x, y, z);
			return ((TileEntityMicrowaveOven)te).isOn() ? 8 : 0;
		} catch (Exception ex) {
			return 0;
		}
	}
    
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	@Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityMicrowaveOven();
    }
    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int l = (int)Math.floor((double) (p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, new int[]{2,4,3,5}[l], 2);
        super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_, p_149689_6_);
    }
	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		int x = p_149695_2_, y = p_149695_3_, z = p_149695_4_;
		if (!p_149695_1_.isBlockNormalCubeDefault(x, y-1, z, true)) {
			p_149695_1_.getBlock(x, y, z).dropBlockAsItem(p_149695_1_, x, y, z, p_149695_1_.getBlockMetadata(x, y, z), 0);
			p_149695_1_.setBlockToAir(x, y, z);
		}
	}
    @Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
    	Random r = new Random();
    	TileEntity te = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
    	if (te != null && te instanceof TileEntityMicrowaveOven) {
    		TileEntityMicrowaveOven t = (TileEntityMicrowaveOven) te;
    		for (int i = 0; i < 1; i++) {
    			ItemStack itemstack = t.getStackInSlot(i);

                if (itemstack != null)
                {
                    float f = r.nextFloat() * 0.8F + 0.1F;
                    float f1 = r.nextFloat() * 0.8F + 0.1F;
                    float f2 = r.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = r.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(p_149749_1_, (double)((float)p_149749_2_ + f), (double)((float)p_149749_3_ + f1), (double)((float)p_149749_4_ + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)r.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)r.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)r.nextGaussian() * f3);
                        p_149749_1_.spawnEntityInWorld(entityitem);
                    }
                }
    		}
    	}
    }
    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
    	if (p_149727_5_.isSneaking()) return false;
        if (p_149727_1_.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityMicrowaveOven tileentitylaser = (TileEntityMicrowaveOven)p_149727_1_.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_);

            if (tileentitylaser != null)
            {
                p_149727_5_.openGui(ModPhotonicCraft.instance, 0, p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_);
            }

            return true;
        }
    }
}
