package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import java.util.HashMap;
import java.util.Map;

import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
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
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
	@Optional.Interface(iface = "li.cil.oc.api.network.Environment", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true),
	@Optional.Interface(iface = "li.cil.oc.api.driver.NamedBlock", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true),
	@Optional.Interface(iface = "li.cil.oc.api.driver.DeviceInfo", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true)
})
public class TileEntityLightDetector extends TileEntityLaserDetector implements ITickable, Environment, NamedBlock, DeviceInfo {
	public int light = -1;
	public int lastlight = -1;

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
	public void update() {
		if (Compat.opencomputers && node != null && ((Node) node).network() == null) {
			Network.joinOrCreateNetwork(this);
		}
		if ((this.getWorld().getTotalWorldTime() & 1) == 0) {
			this.lastlight = this.light;
			this.light = this.getWorld().getLight(this.pos);
			if (this.lastlight != this.light)
				getWorld().notifyNeighborsOfStateChange(pos, blockType, true);
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

	private int xCoord() {
		return this.pos.getX();
	}

	private int yCoord() {
		return this.pos.getY();
	}

	private int zCoord() {
		return this.pos.getZ();
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
		return 1;
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	private static String getComponentName() {
		return "light_detector";
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	@Override
	public Map<String, String> getDeviceInfo() {
		Map<String, String> map = new HashMap<>();
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Class,
				li.cil.oc.api.driver.DeviceInfo.DeviceClass.Generic);
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Description,
				"Light detector");
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Vendor,
				"Photonics Equipment Corporation");
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Product,
				"LITEmate");
		return map;
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
    @Callback(doc = "function():int -- Gets the current light level inside the detector.")
	public Object[] getLight(li.cil.oc.api.machine.Context context, li.cil.oc.api.machine.Arguments args) {
		return new Object[] { light };
    }
}
