package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EnergyContainer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicLocation;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.DeviceInfo;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.Message;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.server.network.Connector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
	@Optional.Interface(iface = "li.cil.oc.api.network.Environment", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true),
	@Optional.Interface(iface = "li.cil.oc.api.driver.NamedBlock", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true),
	@Optional.Interface(iface = "li.cil.oc.api.driver.DeviceInfo", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true)
})
public class TileEntityRadioTransmitter extends TileEntity implements ITickable, Environment, NamedBlock, DeviceInfo {

	public static final int MAX_RF_CAPACITY = 10000;
	EnergyContainer festorage;
	private PhotonicLocation loc;
	private int power;

	public TileEntityRadioTransmitter() {
		super();
		this.festorage = new EnergyContainer(MAX_RF_CAPACITY);
		this.power = 20;
	}

	public void deduceEnergy(int power) {
		this.festorage.extractEnergy(power, false);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (this.world != null) {
				;
				this.world.markChunkDirty(this.pos, this);
			}
			return CapabilityEnergy.ENERGY.cast(this.festorage);
		}
		return super.getCapability(capability, facing);
	}

	public int getTrueEnergyStored() {
		return Math.min(MAX_RF_CAPACITY, this.festorage.getEnergyStored());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		this.festorage.setEnergyStored(p_145839_1_.getInteger("fenergy"));
		this.power = p_145839_1_.getInteger("power");
		if (Compat.opencomputers) {
			node(); // construct if null
			if (node != null && ((Node) node).host() == this) {
				((Node) node).load(p_145839_1_.getCompoundTag("oc:node"));
			}
		}
	}

	@Override
	public void validate() {
		super.validate();

		this.getWorld().scheduleBlockUpdate(this.pos, PhotonicBlocks.radioTransmitter, 10, 2);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		if (Compat.opencomputers && node != null && ((Node) node).host() == this) {
			final NBTTagCompound nodeNbt = new NBTTagCompound();
			((Node) node).save(nodeNbt);
			p_145841_1_.setTag("oc:node", nodeNbt);
		}
		p_145841_1_.setInteger("fenergy", festorage.getEnergyStored());
		p_145841_1_.setInteger("power", power);
		return super.writeToNBT(p_145841_1_);
	}

	private int xCoord() {
		return this.pos.getX();
	}

	private int yCoord() {
		return this.pos.getY();
	}

	private int zCoord() {
		return this.pos.getZ();
	}
	
	@Override
	public void update() {
		if (Compat.opencomputers && node != null && ((Node) node).network() == null) {
			Network.joinOrCreateNetwork(this);
		}
	}
	
	/**
	 * ============ OpenComputers
	 */
	
	protected Object node;

	@Override
	public void onChunkUnload() {
		if (Compat.opencomputers && !world.isRemote) {
			dispose();
		}
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if (Compat.opencomputers) {
			dispose();
		}
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	private void dispose() {
		if (node != null) {
			((Node) node).remove();
		}
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	@Override
	public Node node() {
		if (node == null) {
			node = Network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector().create();
		}
		return ((Node) node);
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	@Override
	public void onConnect(Node arg0) {
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	@Override
	public void onDisconnect(Node arg0) {
		if (node.equals(arg0) && node instanceof Connector) {
			Connector connector = (Connector) node;
			double bufferSize = connector.localBufferSize();
			connector.setLocalBufferSize(0);
			connector.setLocalBufferSize(bufferSize);
		}
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	@Override
	public void onMessage(Message arg0) {
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	@Override
	public String preferredName() {
		return getComponentName();
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	@Override
	public int priority() {
		return 6;
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	private static String getComponentName() {
		return "radio_transmitter";
	}

	@Override
	public Map<String, String> getDeviceInfo() {
		Map<String, String> map = new HashMap<>();
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Class,
				li.cil.oc.api.driver.DeviceInfo.DeviceClass.Communication);
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Description,
				"Radio signal transmitter");
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Vendor,
				"Photonics Equipment Corporation");
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Product,
				"RA55");
		return map;
	}
	
	@Callback(doc = "function(channel:int):boolean -- Check if this computer is allowed to send to the given channel.")
	public Object[] canSendTo(li.cil.oc.api.machine.Context context, li.cil.oc.api.machine.Arguments args) {
		if (this.loc == null) {
			this.loc = new PhotonicLocation(world, pos);
		}
    	if (args.count() < 1)
			return new Object[] { null, "missing channel argument" };
		int channel = 0;
		if (args.isInteger(0)) {
			channel = args.checkInteger(0);
		} else if (args.isDouble(0)) {
			channel = (int) args.checkDouble(0);
		} else if (args.isString(0)) {
			channel = PhotonicRadio.channelNameToID(args.checkString(0));
			if (channel < 0)
				return new Object[] { null, "invalid channel argument" };
		} else {
			return new Object[] { null, "invalid channel argument" };
		}
		if (channel < 0 || channel >= PhotonicRadio.MAX_CHANNEL)
			return new Object[] { null, "invalid channel argument" };
		return new Object[] { PhotonicRadio.radioAuthorizedToSend(loc.getUniqueId(), channel) };
    }

    @Callback(doc = "function():int -- Get the maximum power (RF/4t) this transmitter can send at.")
	public Object[] getMaxPower(li.cil.oc.api.machine.Context context, li.cil.oc.api.machine.Arguments args) {
		return new Object[] { this.getTrueEnergyStored() };
    }

    @Callback(doc = "function():int -- Get the current transmission power.")
	public Object[] getPower(li.cil.oc.api.machine.Context context, li.cil.oc.api.machine.Arguments args) {
		return new Object[] { power };
    }

    @Callback(doc = "function(power:int) -- Set the current transmission power.")
	public Object[] setPower(li.cil.oc.api.machine.Context context, li.cil.oc.api.machine.Arguments args) {
    	if (args.count() < 1)
			return new Object[] { null, "missing power argument" };
    	int newpower = 0;
		if (args.isInteger(0)) {
			newpower = args.checkInteger(0);
		} else if (args.isDouble(0)) {
			newpower = (int) args.checkDouble(0);
		}
		if (newpower < 0 || newpower > TileEntityRadioTransmitter.MAX_RF_CAPACITY)
			return new Object[] { null, "invalid power argument" };
		power = newpower;
		return new Object[] { power };
    }

    @Callback(doc = "function(channel:int,data:string[,offset:int]) -- Transmit to a channel.", limit = 1)
	public Object[] transmit(li.cil.oc.api.machine.Context context, li.cil.oc.api.machine.Arguments args) {
		if (this.loc == null) {
			this.loc = new PhotonicLocation(world, pos);
		}
    	if (args.count() < 1)
    		return new Object[] { null, "missing channel and data argument" };
		if (args.count() < 2)
			return new Object[] { null, "missing data argument" };
		int channel = 0;
		if (args.isInteger(0)) {
			channel = args.checkInteger(0);
		} else if (args.isDouble(0)) {
			channel = (int) args.checkDouble(0);
		} else if (args.isString(0)) {
			channel = PhotonicRadio.channelNameToID(args.checkString(0));
			if (channel < 0)
				return new Object[] { null, "invalid channel argument" };
		} else {
			return new Object[] { null, "invalid channel argument" };
		}
		if (channel < 0 || channel >= PhotonicRadio.MAX_CHANNEL)
			return new Object[] { null, "invalid channel argument" };
		int pos = 1;
		if (args.count() > 2) {
			if (args.isInteger(2)) {
				pos = args.checkInteger(2);
			} else if (args.isDouble(2)) {
				pos = (int) args.checkDouble(2);
			} else {
				return new Object[] { null, "invalid position argument" };
			}
		}
		if (pos < 1)
			return new Object[] { null, "invalid position argument" };
		TileEntityRadioTransmitter t = this;
		if (t.getTrueEnergyStored() < power && node() instanceof li.cil.oc.api.network.Connector) {
			double delta = ((li.cil.oc.api.network.Connector) node()).changeBuffer(-power);
			int eint = (int) (power - delta);
			int rint = this.festorage.receiveEnergy(eint, false);
			((li.cil.oc.api.network.Connector) node()).changeBuffer((eint - rint) + 1.0 - PhotonicUtils.frac(delta));
		}
		if (t.getTrueEnergyStored() < power)
			return new Object[] { null, "not enough RF" };
		t.deduceEnergy(power);
		PhotonicRadio.serverSideInit();
		byte[] binary = new byte[2205];
		Arrays.fill(binary, (byte) 128);
		int bytes = 0;
		try {
			if (args.isByteArray(1)) {
				byte[] b = args.checkByteArray(1);
				--pos;
				for (int i = pos; i < Math.min(pos + 2205, b.length); ++i) {
					binary[i] = b[i];
				}
			} else if (args.isString(1)) {
				String data = args.checkString(1);
				--pos;
				for (int i = pos; i < Math.min(pos + 2205, data.length()); ++i) {
					binary[i] = (byte) (data.charAt(i) & 255);
				}
			} else if (args.isTable(1)) {
				Map<Object, Object> data = args.checkTable(1);
				int cached = 0;
				for (double index = pos; index < pos + 2205; index += 1) {
					if (!data.containsKey(index))
						break;
					binary[bytes++] = (byte) (((Double) data.get(index)).doubleValue());
				}
			} else {
				throw new IllegalArgumentException();
			}
		} catch (Exception ex) {
			return new Object[] { null, "invalid data argument" };
		}
		if (PhotonicRadio.sendCache.containsKey(channel)) {
			return new Object[] { false, "wait for signal photoniccraft_radio before transmitting again" };
		}
		if (!PhotonicRadio.radioSendData(loc.getUniqueId(), channel, binary, loc, power))
			return new Object[] { false, "unauthorized to send data to this channel" };
		PhotonicRadio.sendEventWhenSend(channel, context);
		return new Object[] { true };
    }
}
