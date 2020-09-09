package email.com.gmail.cosmoconsole.forge.photoniccraft.common;

import email.com.gmail.cosmoconsole.forge.photoniccraft.client.gui.GuiLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.gui.GuiLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.gui.GuiMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.inventory.ContainerLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.inventory.ContainerLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.inventory.ContainerMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityMicrowaveOven;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * The main GUI handler for PhotonicCraft, used for getting Container and GUI
 * classes.
 */
public class PhotonicGuiHandler implements IGuiHandler {
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity instanceof TileEntityLaser) {
			return new GuiLaser(player.inventory, (TileEntityLaser) tileEntity);
		}
		if (tileEntity instanceof TileEntityLaserCharger) {
			return new GuiLaserCharger(player.inventory, (TileEntityLaserCharger) tileEntity);
		}
		if (tileEntity instanceof TileEntityMicrowaveOven) {
			return new GuiMicrowaveOven(player.inventory, (TileEntityMicrowaveOven) tileEntity);
		}
		return null;

	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity instanceof TileEntityLaser) {
			return new ContainerLaser(player.inventory, (TileEntityLaser) tileEntity);
		}
		if (tileEntity instanceof TileEntityLaserCharger) {
			return new ContainerLaserCharger(player.inventory, (TileEntityLaserCharger) tileEntity);
		}
		if (tileEntity instanceof TileEntityMicrowaveOven) {
			return new ContainerMicrowaveOven(player.inventory, (TileEntityMicrowaveOven) tileEntity);
		}
		return null;
	}
}