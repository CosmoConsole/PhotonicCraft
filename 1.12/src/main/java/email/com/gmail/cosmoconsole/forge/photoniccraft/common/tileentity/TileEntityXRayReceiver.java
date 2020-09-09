package email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicBlocks;
import email.com.gmail.cosmoconsole.forge.photoniccraft.integration.Compat;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.EnergyContainer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicUtils;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.SlotAndItem;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.DeviceInfo;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.Message;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.server.network.Connector;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@Optional.InterfaceList({
	@Optional.Interface(iface = "li.cil.oc.api.network.Environment", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true),
	@Optional.Interface(iface = "li.cil.oc.api.driver.NamedBlock", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true),
	@Optional.Interface(iface = "li.cil.oc.api.driver.DeviceInfo", modid = Compat.MODID_OPENCOMPUTERS, striprefs = true)
})
public class TileEntityXRayReceiver extends TileEntity implements ITickable, Environment, NamedBlock, DeviceInfo {

	public static final int MAX_RF_CAPACITY = 100;
	EnergyContainer festorage;
	private boolean powered;

	public TileEntityXRayReceiver() {
		super();
		this.festorage = new EnergyContainer(MAX_RF_CAPACITY);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (this.world != null) {
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
	public void validate() {
		super.validate();

		this.getWorld().scheduleBlockUpdate(this.pos, PhotonicBlocks.xrayTransmitter, 10, 2);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		readSyncableDataFromNBT(p_145839_1_);
		this.festorage.setEnergyStored(p_145839_1_.getInteger("fenergy"));
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
		p_145841_1_.setInteger("fenergy", festorage.getEnergyStored());
		writeSyncableDataToNBT(p_145841_1_);
		return super.writeToNBT(p_145841_1_);
	}

	private void writeSyncableDataToNBT(NBTTagCompound p_145841_1_) {
		p_145841_1_.setBoolean("powered", powered);
		if (selectedItem != null) {
			p_145841_1_.setInteger("SlotIndex", selectedItem.getSlot());
			selectedItem.getItem().writeToNBT(p_145841_1_);
		}
	}

	private void readSyncableDataFromNBT(NBTTagCompound tag) {
		powered = tag.getBoolean("powered");
		if (tag.hasKey("SlotIndex")) {
			selectItem(new SlotAndItem(tag.getInteger("SlotIndex"), new ItemStack(tag)));
		} else {
			clearItem();
			if (this.world != null && this.pos != null) {
				this.getWorld().notifyBlockUpdate(this.pos, this.world.getBlockState(pos),
						this.world.getBlockState(pos), 3);
			}
			this.markDirty();
		}
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		writeSyncableDataToNBT(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		readSyncableDataFromNBT(tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readSyncableDataFromNBT(pkt.getNbtCompound());
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
	
	public boolean isPowered() {
		return powered;
	}
	
	private SlotAndItem selectedItem;
	public EntityItem entItem = null;
	public float angle = 0.0f;
	private void selectItem(SlotAndItem item) {
		this.selectedItem = item;
		if (!item.getItem().isEmpty()) {
			this.entItem = new EntityItem(getWorld(), 0D, 0D, 0D, item.getItem());
		} else {
			this.entItem = null;
		}
		if (this.world != null && this.pos != null) {
			this.getWorld().notifyBlockUpdate(this.pos, this.world.getBlockState(pos),
					this.world.getBlockState(pos), 3);
		}
		this.markDirty();
	}
	private void clearItem() {
		this.selectedItem = null;
		this.entItem = null;
	}
	public SlotAndItem getSelectedItem() {
		return this.selectedItem;
	}

	@Override
	public void update() {
		angle += 1.0f;
		if (Compat.opencomputers && node != null && ((Node) node).network() == null) {
			Network.joinOrCreateNetwork(this);
		}
		if (!world.isRemote) {
			if (Compat.opencomputers) {
				if ((world.getTotalWorldTime() % 2L) == 0L && getTrueEnergyStored() < MAX_RF_CAPACITY && node() instanceof Connector) {
					double delta = ((Connector) node()).changeBuffer(-20);
					int eint = (int) (20 - delta);
					int rint = this.festorage.receiveEnergy(eint, false);
					((Connector) node()).changeBuffer((eint - rint) + 1.0 - PhotonicUtils.frac(delta));
				}
			}
			int power = this.festorage.extractEnergy(20, true);
			if (power >= 20 && (world.getTotalWorldTime() % 10L) == 0) {
				this.festorage.extractEnergy(20, false);
				powered = true;
				List<SlotAndItem> items = new ArrayList<>();
				BlockPos bp = this.pos;
				for (EnumFacing facing: EnumFacing.VALUES) {
					BlockPos p = bp;
					List<IItemHandler> blockContainers = new ArrayList<>();
					List<EntityLivingBase> entityContainers = new ArrayList<>();
					for (int i = 0; i < 3; ++i) {
						p = p.add(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ());
						if (p.getY() < 0 || p.getY() >= world.getHeight()) break;
						IBlockState bs = world.getBlockState(p);
						TileEntity tile = getWorld().getTileEntity(p);
						if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
							IItemHandler ih = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
							blockContainers.add(ih);
						}
						if (bs.getBlock() == PhotonicBlocks.xrayTransmitter 
								&& PhotonicUtils.readDirectionProperty(bs, BlockDirectional.FACING) == facing.getOpposite()) {
							if (!((TileEntityXRayTransmitter)tile).isPowered()) break;
							for (EntityLivingBase e: world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(bp.add(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ())))) {
								entityContainers.add(e);
							}
							items.addAll(findItemsFromBlocks(blockContainers));
							items.addAll(findItemsFromEntities(entityContainers));
							break;
						}
						if (bs.getBlockHardness(world, p) >= 5) break;
					}
				}
				if (!items.isEmpty()) {
					int ind = PhotonicUtils.rand.nextInt(items.size());
					selectItem(items.get(ind));
				} else {
					boolean update = this.entItem != null;
					clearItem();
					if (update) {
						this.getWorld().notifyBlockUpdate(this.pos, this.world.getBlockState(pos),
								this.world.getBlockState(pos), 3);
						this.markDirty();
					}
				}
			} else if (power < 20) {
				boolean update = this.entItem != null;
				powered = false;
				clearItem();
				if ((world.getTotalWorldTime() % 20L) == 0 && update) {
					this.getWorld().notifyBlockUpdate(this.pos, this.world.getBlockState(pos),
							this.world.getBlockState(pos), 3);
					this.markDirty();
				}
			}
		} 
	}

	private void maybeAddItem(List<SlotAndItem> res, SlotAndItem slotAndItem) {
		if (slotAndItem.getItem() == null) 
			return;
		if (PhotonicUtils.rand.nextInt(4) == 0)
			return;
		if (slotAndItem.getItem().isEmpty() && PhotonicUtils.rand.nextInt(8) > 0)
			return;
		res.add(slotAndItem);
	}

	private List<SlotAndItem> findItemsFromEntities(List<EntityLivingBase> entityContainers) {
		List<SlotAndItem> res = new ArrayList<>();
		for (EntityLivingBase el: entityContainers) {
			if (el instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer) el;
				int i = 0;
				for (ItemStack is: p.inventory.armorInventory) {
					maybeAddItem(res, new SlotAndItem(PhotonicUtils.armorSlotToSlotIndex(i++), is));
				}
				i = 0;
				for (ItemStack is: p.inventory.offHandInventory) {
					maybeAddItem(res, new SlotAndItem(PhotonicUtils.offHandSlotToSlotIndex(i++), is));
				}
				i = 0;
				for (ItemStack is: p.inventory.mainInventory) {
					maybeAddItem(res, new SlotAndItem(PhotonicUtils.mainSlotToSlotIndex(i++), is));
				}
			} else {
				for (EntityEquipmentSlot s: EntityEquipmentSlot.values()) {
					maybeAddItem(res, new SlotAndItem(s.getSlotIndex(), el.getItemStackFromSlot(s)));
				}
			}
		}
		return res;
	}

	private List<SlotAndItem> findItemsFromBlocks(List<IItemHandler> blockContainers) {
		List<SlotAndItem> res = new ArrayList<>();
		for (IItemHandler ih: blockContainers) {
			for (int i = 0; i < ih.getSlots(); ++i) {
				try {
					maybeAddItem(res, new SlotAndItem(i, ih.getStackInSlot(i)));
				} catch (Exception ex) {
					System.err.println("[WARN]" + ModPhotonicCraft.MODID + " x-ray: cannot get items from " + ih + ", is there a bug in some other mod?");
					ex.printStackTrace();
				}
			}
		}
		return res;
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
		return 1;
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	private static String getComponentName() {
		return "xray_receiver";
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
	@Override
	public Map<String, String> getDeviceInfo() {
		Map<String, String> map = new HashMap<>();
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Class,
				li.cil.oc.api.driver.DeviceInfo.DeviceClass.Generic);
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Description,
				"X-ray item inspector");
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Vendor,
				"Photonics Equipment Corporation");
		map.put(li.cil.oc.api.driver.DeviceInfo.DeviceAttribute.Product,
				"ITEMstation 410");
		return map;
	}

	@Optional.Method(modid = Compat.MODID_OPENCOMPUTERS)
    @Callback(doc = "function():table -- Gets the item currently displayed on the receiver.")
	public Object[] getItem(li.cil.oc.api.machine.Context context, li.cil.oc.api.machine.Arguments args) {
		SlotAndItem si = this.getSelectedItem();
		if (si == null)
			return new Object[] { new HashMap<Object, Object>() };
		HashMap<Object, Object> hm = new HashMap<>();
		hm.put("slot", si.getSlot());
		hm.put("item", PhotonicUtils.simpleItemToTable(si.getItem()));
		return new Object[] { hm };
    }
}
