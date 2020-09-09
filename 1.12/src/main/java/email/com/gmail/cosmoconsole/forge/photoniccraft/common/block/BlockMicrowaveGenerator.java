package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityMicrowaveGenerator;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class BlockMicrowaveGenerator extends Block implements ITileEntityProvider {
	public static final PropertyBool on = PropertyBool.create("on");

	public BlockMicrowaveGenerator() {
		super(Material.ROCK);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_microwavegenerator");
		this.setDefaultState(this.blockState.getBaseState().withProperty(on, false));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { on });
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityMicrowaveGenerator();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return PhotonicUtils.readBoolProperty(state, on) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(on, meta > 0);
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, BlockPos bp, IBlockState bs, EntityPlayer p_149727_5_,
			EnumHand hand, EnumFacing face, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_1_.isRemote)
			return true;
		TileEntityMicrowaveGenerator t = ((TileEntityMicrowaveGenerator) p_149727_1_.getTileEntity(bp));
		if (p_149727_5_.getHeldItem(EnumHand.MAIN_HAND) != null && p_149727_5_.getHeldItem(EnumHand.MAIN_HAND)
				.getItem() == ModPhotonicCraft.photonicResources2.get(3).getItem()) {
			int give_max = p_149727_5_.getHeldItem(EnumHand.MAIN_HAND).getCount();
			if (p_149727_5_.isSneaking())
				give_max = 1;
			int take_max = (int) ((TileEntityMicrowaveGenerator.maxpower - t.powered)
					/ TileEntityMicrowaveGenerator.peringot);
			int giving = Math.min(take_max, give_max);
			if (giving < 1) {
				p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_microwaveGenerator.full"));
			} else {
				if (t.powered < 1)
					p_149727_5_
							.sendMessage(new TextComponentTranslation("msg.photoniccraft_microwaveGenerator.enabled"));
				t.addExtraTime(giving * TileEntityMicrowaveGenerator.peringot);
				if (!p_149727_5_.capabilities.isCreativeMode)
					p_149727_5_.inventory.decrStackSize(p_149727_5_.inventory.currentItem, giving);
			}
			return true;
		}
		if (t.isRunning()) {
			if (p_149727_5_.isSneaking())
				return false;
			p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_microwaveGenerator.output",
					Integer.toString((int) t.output)));
			p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_microwaveGenerator.time",
					String.format("%02d", (t.powered / 72000L)), String.format("%02d", (t.powered / 1200L) % 60),
					String.format("%02d", (t.powered / 20L) % 60)));
			return true;
		}
		if (p_149727_5_.isSneaking())
			return false;
		t.doFullCheck();
		if (!t.isRunning())
			p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_microwaveGenerator.invalid"));
		else if (t.powered < 1)
			p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_microwaveGenerator.enterHahnium"));
		else
			p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_microwaveGenerator.enabled"));
		return true;
	}
}
