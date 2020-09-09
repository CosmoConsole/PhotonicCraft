package email.com.gmail.cosmoconsole.forge.photoniccraft;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.world.World;

public class PhotonicPeripheralProvider implements IPeripheralProvider {

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		if (world.getBlock(x, y, z) == ModPhotonicCraft.laserDetector2) {
			return new PeripheralAdvLaserDetector(world, x, y, z);
		} else if (world.getBlock(x, y, z) == ModPhotonicCraft.radioReceiver) {
			return new PeripheralRadioReceiver(world, x, y, z);
		} else if (world.getBlock(x, y, z) == ModPhotonicCraft.radioTransmitter) {
			return new PeripheralRadioTransmitter(world, x, y, z);
		} else if (world.getBlock(x, y, z) == ModPhotonicCraft.radioTransceiver) {
			return new PeripheralRadioTransceiver(world, x, y, z);
		}
		return null;
	}

}
