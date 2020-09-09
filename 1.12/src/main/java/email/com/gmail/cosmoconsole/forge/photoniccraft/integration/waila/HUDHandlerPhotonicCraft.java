package email.com.gmail.cosmoconsole.forge.photoniccraft.integration.waila;

import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockAnalogRedstoneLamp;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockLightDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.block.BlockRemoteReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLightDetector;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityRemoteReceiver;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class HUDHandlerPhotonicCraft implements IWailaDataProvider {
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		Block block = accessor.getBlock();
		if (block == PhotonicBlocks.lightDetector) {
			tooltip.add(I18n.format("tooltip.photoniccraft.lightdetector.light", 
					((TileEntityLightDetector) accessor.getTileEntity()).light));
		} else if (block == PhotonicBlocks.analoglamp) {
			tooltip.add(I18n.format("tooltip.photoniccraft.analoglamp.light", 
					PhotonicUtils.readIntegerProperty(accessor.getBlockState(), BlockAnalogRedstoneLamp.power)));
		} else if (block == PhotonicBlocks.remoteReceiver) {
			long ch = ((TileEntityRemoteReceiver) accessor.getTileEntity()).channel;
			if (ch >= 0)
				tooltip.add(I18n.format("tooltip.photoniccraft.remotereceiver.channel", ch));
		}
		return IWailaDataProvider.super.getWailaBody(itemStack, tooltip, accessor, config);
	}
	
    public static void register(IWailaRegistrar registrar) {
        IWailaDataProvider provider = new HUDHandlerPhotonicCraft();

        registrar.registerBodyProvider(provider, BlockLightDetector.class);
        registrar.registerBodyProvider(provider, BlockAnalogRedstoneLamp.class);
        registrar.registerBodyProvider(provider, BlockRemoteReceiver.class);
    }
}
