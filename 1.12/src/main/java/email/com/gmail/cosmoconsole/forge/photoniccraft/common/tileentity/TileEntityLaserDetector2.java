package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class TileEntityLaserDetector2 extends TileEntityLaserDetector implements ITickable, Environment, NamedBlock, DeviceInfo {
	long lasertick = 0L;
	ArrayList<Object[]> thisTick = new ArrayList<Object[]>();
	ArrayList<Object[]> lastTick = new ArrayList<Object[]>();
	Object listLock = new Object();
	ConcurrentHashMap<Integer, ArrayList<Object[]>> captures = new ConcurrentHashMap<Integer, ArrayList<Object[]>>();
	
	public TileEntityLaserDetector2() {
		super();
	}

	public void addLaser(long tick, Object[] objects) {
		if (thisTick == null || lastTick == null) {
			thisTick = new ArrayList<Object[]>();
			lastTick = new ArrayList<Object[]>();
			captures = new ConcurrentHashMap<Integer, ArrayList<Object[]>>();
		}
		synchronized (listLock) {
			if (tick == lasertick) {
				thisTick.add(objects);
			} else if (tick == (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>(thisTick);
				thisTick = new ArrayList<Object[]>();
				thisTick.add(objects);
			} else if (tick > (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>();
				thisTick = new ArrayList<Object[]>();
				thisTick.add(objects);
			}
			lasertick = tick;
		}
	}

	public void catchComputer(long tick, int id) {
		if (thisTick == null || lastTick == null) {
			thisTick = new ArrayList<Object[]>();
			lastTick = new ArrayList<Object[]>();
			captures = new ConcurrentHashMap<Integer, ArrayList<Object[]>>();
		}
		synchronized (listLock) {
			if (tick == (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>(thisTick);
				thisTick = new ArrayList<Object[]>();
			} else if (tick > (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>();
				thisTick = new ArrayList<Object[]>();
			}
			captures.put(id, new ArrayList<Object[]>(lastTick));
		}
	}

	public ArrayList<Object[]> getCatch(int id) {
		return captures.get(id);
	}

	public ArrayList<Object[]> getLastBeams(long tick) {
		if (thisTick == null || lastTick == null) {
			thisTick = new ArrayList<Object[]>();
			lastTick = new ArrayList<Object[]>();
			captures = new ConcurrentHashMap<Integer, ArrayList<Object[]>>();
		}
		ArrayList<Object[]> results;
		synchronized (listLock) {
			if (tick == (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>(thisTick);
				thisTick = new ArrayList<Object[]>();
			} else if (tick > (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>();
				thisTick = new ArrayList<Object[]>();
			}
			results = new ArrayList<Object[]>(lastTick);
		}
		return results;
	}

	public boolean hasCatchForComputer(int id) {
		return captures.containsKey(id);
	}

	public void removeComputer(int id) {
		if (captures.containsKey(id))
			captures.remove(id);
	}

	@Override
	public void update() {
		super.update();
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
		return 1;
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	private static String getComponentName() {
		return "laser_detector";
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	@Override
	public Map<String, String> getDeviceInfo() {
		Map<String, String> map = new HashMap<>();
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Class,
				li.cil.oc.api.driver.DeviceInfo.DeviceClass.Generic);
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Description,
				"Laser beam analyzer");
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Vendor,
				"Photonics Equipment Corporation");
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Product,
				"LBA 8000");
		return map;
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
    @Callback(doc = "function():table -- Gets the laser beams passing through the detector.")
	public Object[] getBeams(li.cil.oc.api.machine.Context context, li.cil.oc.api.machine.Arguments args) {
		ArrayList<Object[]> beams = this.getLastBeams(world.getTotalWorldTime());
		int index = 1;
		HashMap<Object, Object> results = new HashMap<Object, Object>();
		for (Object[] beam : beams) {
			HashMap<Object, Object> b = new HashMap<Object, Object>();
			b.put(1, beam[0]);
			b.put(2, beam[1]);
			b.put(3, beam[2]);
			results.put(index++, b);
		}
		return new Object[] { results };
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
