package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import java.util.HashMap;
import java.util.Map;

import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
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
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
	@Optional.Interface(iface = "li.cil.oc.api.network.Environment", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true),
	@Optional.Interface(iface = "li.cil.oc.api.driver.NamedBlock", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true),
	@Optional.Interface(iface = "li.cil.oc.api.driver.DeviceInfo", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true)
})
public class TileEntityRadioReceiver extends TileEntity implements ITickable, Environment, NamedBlock, DeviceInfo {
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
			node = Network.newNode(this, Visibility.Network).withComponent(getComponentName()).create();
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
		return "radio_receiver";
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	@Override
	public Map<String, String> getDeviceInfo() {
		Map<String, String> map = new HashMap<>();
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Class,
				li.cil.oc.api.driver.DeviceInfo.DeviceClass.Communication);
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Description,
				"Radio signal receiver");
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Vendor,
				"Photonics Equipment Corporation");
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Product,
				"RA52");
		return map;
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
    @Callback(doc = "function(channel:int):string -- Receives binary data from the given radio channel.", direct=true)
	public Object[] receive(li.cil.oc.api.machine.Context context, li.cil.oc.api.machine.Arguments args) {
    	int tried = 0;
		if (args.count() < 1)
			return new Object[]{ null, "missing channel argument" };
		int channel = 0;
		if (args.isInteger(0)) {
			channel = args.checkInteger(0);
		} else if (args.isDouble(0)) {
			channel = (int) args.checkDouble(0);
		} else if (args.isString(0)) {
			channel = PhotonicRadio.channelNameToID(args.checkString(0));
			if (channel < 0)
				return new Object[]{ null, "invalid channel argument" };
		} else {
			return new Object[]{ null, "invalid channel argument" };
		}
		if (channel < 0 || channel >= PhotonicRadio.MAX_CHANNEL)
			return new Object[]{ null, "invalid channel argument" };
		byte[] data = new byte[0];
		PhotonicRadio.serverSideInit();
		while (PhotonicRadio.cannotModify.get())
			Thread.yield();	
		if (PhotonicRadio.dataSentLastTick.containsKey(channel)) {
			data = PhotonicRadio.dataSentLastTick.get(channel);
			double ampl = PhotonicRadio.calculateAmplitude(PhotonicRadio.recvLoc.get(channel).distanceSq(new PhotonicLocation(world, pos)),
					PhotonicRadio.recvPower.get(channel));
			if (ampl < 0.6) {
				PhotonicUtils.rand.nextBytes(data);
			}
		} else {
			data = new byte[2205];
			PhotonicUtils.rand.nextBytes(data);
		}
		StringBuilder results = new StringBuilder();
		for (int index = 0; index < data.length; index++) {
			results.append((char) PhotonicUtils.unsign(data[index]));
		}
		return new Object[] { results.toString() };
    }

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		if (Compat.opencomputers) {
			node(); // construct if null
			if (node != null && ((Node) node).host() == this) {
				((Node) node).load(p_145839_1_.getCompoundTag("oc:node"));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		if (Compat.opencomputers && node != null && ((Node) node).host() == this) {
			final NBTTagCompound nodeNbt = new NBTTagCompound();
			((Node) node).save(nodeNbt);
			p_145841_1_.setTag("oc:node", nodeNbt);
		}
		return super.writeToNBT(p_145841_1_);
	}
}
