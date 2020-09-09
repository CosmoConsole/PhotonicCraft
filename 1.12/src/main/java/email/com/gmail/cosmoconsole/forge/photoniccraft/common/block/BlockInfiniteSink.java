package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityInfiniteSink;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockInfiniteSink extends Block implements ITileEntityProvider {
	public BlockInfiniteSink() {
		super(Material.ROCK);
		this.setHardness(6000000.0F);
		this.setResistance(6000000.0F);
		this.setBlockUnbreakable();
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(ModPhotonicCraft.MODID + "_infiniterfsink");
		this.setCreativeTab(ModPhotonicCraft.photoniccraftTab);
		this.disableStats();
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityInfiniteSink();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		tooltip.add(I18n.format("msg.photoniccraft_creativeOnly"));
	}
}
