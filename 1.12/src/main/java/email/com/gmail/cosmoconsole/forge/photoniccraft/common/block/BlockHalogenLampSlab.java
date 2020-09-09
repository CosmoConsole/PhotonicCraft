package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.Random;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockHalogenLampSlab extends BlockSlab {
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 1);

    public BlockHalogenLampSlab() {
		this(Material.GLASS);
	}
    
    public BlockHalogenLampSlab(Material materialIn) {
		super(materialIn);
		this.setHardness(0.5F);
		this.setSoundType(SoundType.GLASS);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_halogenslab");
        IBlockState iblockstate = this.blockState.getBaseState();
        if (!this.isDouble()) {
    		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
            iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
            this.useNeighborBrightness = true;
        }
        this.setDefaultState(iblockstate.withProperty(VARIANT, 0));
	}

	@Override
	public String getUnlocalizedName(int meta) {
        return super.getUnlocalizedName();
	}

	@Override
	public abstract boolean isDouble();

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();

        if (!this.isDouble())
        {
            iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockstate;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
        {
            i |= 8;
        }

        return i;
    }
    
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
    	return 15;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(PhotonicBlocks.halogenSlab);
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return this.isDouble() ? new BlockStateContainer(this, new IProperty[] {VARIANT}) : new BlockStateContainer(this, new IProperty[] {HALF, VARIANT});
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
    	return this.isDouble();
    }
    
    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
    	return this.isDouble();
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
    	return this.isDouble();
    }
    
    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    	return this.isDouble() || (side == side.UP && world.getBlockState(pos).getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) || (side == side.DOWN && world.getBlockState(pos).getValue(HALF) == BlockSlab.EnumBlockHalf.BOTTOM);
    }
    
    @Override
    public boolean isTopSolid(IBlockState state) {
    	return this.isDouble() || state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP;
    }
    
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    	return new ItemStack(Item.getItemFromBlock(PhotonicBlocks.halogenSlab), this.isDouble() ? 2 : 1);
    }
    
    public static enum EnumType implements IStringSerializable
    {
        HALOGEN(0, MapColor.SNOW, "halogen");

        private static final BlockHalogenLampSlab.EnumType[] META_LOOKUP = new BlockHalogenLampSlab.EnumType[values().length];
        private final int meta;
        private final MapColor mapColor;
        private final String name;
        private final String unlocalizedName;

        private EnumType(int p_i46381_3_, MapColor p_i46381_4_, String p_i46381_5_)
        {
            this(p_i46381_3_, p_i46381_4_, p_i46381_5_, p_i46381_5_);
        }

        private EnumType(int p_i46382_3_, MapColor p_i46382_4_, String p_i46382_5_, String p_i46382_6_)
        {
            this.meta = p_i46382_3_;
            this.mapColor = p_i46382_4_;
            this.name = p_i46382_5_;
            this.unlocalizedName = p_i46382_6_;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public MapColor getMapColor()
        {
            return this.mapColor;
        }

        public String toString()
        {
            return this.name;
        }

        public static BlockHalogenLampSlab.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static
        {
            for (BlockHalogenLampSlab.EnumType blockstoneslab$enumtype : values())
            {
                META_LOOKUP[blockstoneslab$enumtype.getMetadata()] = blockstoneslab$enumtype;
            }
        }
    }
}
