package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityRemoteReceiver;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRemoteReceiver extends Block implements ITileEntityProvider {
	public BlockRemoteReceiver() {
		super(Material.ROCK);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_remotereceiver");
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityRemoteReceiver();
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess p_149709_1_, BlockPos pos, EnumFacing side) {
		return 0;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess p_149709_1_, BlockPos pos, EnumFacing side) {
		return (((TileEntityRemoteReceiver) p_149709_1_.getTileEntity(pos)).getPower());
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, BlockPos pos, IBlockState state, EntityPlayer p_149727_5_,
			EnumHand hand, EnumFacing face, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_1_.isRemote)
			return true;
		if (p_149727_5_.isSneaking()) {
			if (p_149727_5_.getHeldItem(EnumHand.MAIN_HAND) != null
						&& p_149727_5_.getHeldItem(EnumHand.MAIN_HAND).getItem() == PhotonicItems.remote) {
				/*
				 * handled by item
				 * TileEntityRemoteReceiver te = ((TileEntityRemoteReceiver) p_149727_1_.getTileEntity(pos));
				if ((p_149727_1_.getTotalWorldTime() - te.lastclick) < 10)
					return true;
				te.lastclick = p_149727_1_.getTotalWorldTime();
				if (te.channel < 0) {
					p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_remotereceiver.nochannel"));
				} else {
					p_149727_5_.getHeldItem(EnumHand.MAIN_HAND).getTagCompound().setLong("channel", te.channel);
					p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_remote.channelcopied",
							Long.toString(te.channel)));
				}*/
				return true;
			}
			return false;
		} else {
			TileEntityRemoteReceiver te = ((TileEntityRemoteReceiver) p_149727_1_.getTileEntity(pos));
			if (p_149727_5_.getHeldItem(EnumHand.MAIN_HAND) != null
					&& p_149727_5_.getHeldItem(EnumHand.MAIN_HAND).getItem() == PhotonicItems.remote) {
				if (p_149727_5_.getHeldItem(EnumHand.MAIN_HAND).getTagCompound().getLong("channel") == te.channel) {
					te.submitPower(15, te.nonSimultaneous + 1);
					return true;
				}
				return true;
			}
			if ((p_149727_1_.getTotalWorldTime() - te.lastclick) < 10)
				return true;
			te.lastclick = p_149727_1_.getTotalWorldTime();
			if (te.channel < 0) {
				p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_remotereceiver.nochannel"));
			} else {
				p_149727_5_.sendMessage(new TextComponentTranslation("msg.photoniccraft_remotereceiver.channelreply",
						Long.toString(te.channel)));
			}
			return true;
		}
	}

	@Override
	public void onBlockClicked(World p_149699_1_, BlockPos pos, EntityPlayer p_149699_5_) {
		if (p_149699_1_.isRemote)
			return;
		if (p_149699_5_.isSneaking()) {
			if (p_149699_5_.getHeldItem(EnumHand.MAIN_HAND) != null
					&& p_149699_5_.getHeldItem(EnumHand.MAIN_HAND).getItem() == PhotonicItems.remote) {
				TileEntityRemoteReceiver te = ((TileEntityRemoteReceiver) p_149699_1_.getTileEntity(pos));
				if (p_149699_5_.getHeldItem(EnumHand.MAIN_HAND).getTagCompound().getLong("channel") >= 0) {
					if ((p_149699_1_.getTotalWorldTime() - te.lastclick) < 10)
						return;
					IBlockState bs = p_149699_1_.getBlockState(pos); 
					te.lastclick = p_149699_1_.getTotalWorldTime();
					te.channel = p_149699_5_.getHeldItem(EnumHand.MAIN_HAND).getTagCompound().getLong("channel");
					p_149699_1_.notifyBlockUpdate(pos, bs, bs, 3);
					te.markDirty();
					p_149699_5_.sendMessage(new TextComponentTranslation(
							"msg.photoniccraft_remotereceiver.channelcopied", Long.toString(te.channel)));
				}
			}
		}
	}
}
