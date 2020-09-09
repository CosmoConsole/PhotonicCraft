package email.com.gmail.cosmoconsole.forge.photoniccraft.integration.computercraft;

import java.nio.file.Path;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaserDetector2;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Optional;

/**
 * The ComputerCraft IPeripheralProvider for the PhotonicCraft peripherals.
 */
@Optional.InterfaceList({@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheralProvider", modid = Compat.MODID_COMPUTERCRAFT)})
public class PhotonicPeripheralProvider implements dan200.computercraft.api.peripheral.IPeripheralProvider {

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	static Path resolveComputerSavePath(int id) {
		Path p = DimensionManager.getCurrentSaveRootDirectory().toPath();
		if (p == null)
			return null;
		p = p.resolve("computer");
		if (p == null)
			return null;
		p = p.resolve(String.valueOf(id));
		return p;
	}

	@Optional.Method(modid = Compat.MODID_COMPUTERCRAFT)
	@Override
	public dan200.computercraft.api.peripheral.IPeripheral getPeripheral(World world, BlockPos bp, EnumFacing side) {
		if (world.getTileEntity(bp) instanceof TileEntityLaserDetector2) {
			return new PeripheralAdvLaserDetector(world, bp);
		} else if (world.getBlockState(bp).getBlock().equals(PhotonicBlocks.lightDetector)) {
			return new PeripheralLightDetector(world, bp);
		} else if (world.getBlockState(bp).getBlock().equals(PhotonicBlocks.radioReceiver)) {
			return new PeripheralRadioReceiver(world, bp);
		} else if (world.getBlockState(bp).getBlock().equals(PhotonicBlocks.radioTransmitter)) {
			return new PeripheralRadioTransmitter(world, bp);
		} else if (world.getBlockState(bp).getBlock().equals(PhotonicBlocks.radioTransceiver)) {
			return new PeripheralRadioTransceiver(world, bp);
		} else if (world.getBlockState(bp).getBlock().equals(PhotonicBlocks.xrayReceiver)) {
			return new PeripheralXRayReceiver(world, bp);
		}
		return null;
	}

}
