package email.com.gmail.cosmoconsole.forge.photoniccraft.common.inventory;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerLaser extends Container {
	private TileEntityLaser tileLaser;
	private int laserBurnTime;
	private int lastItemBurnTime;

	public ContainerLaser(InventoryPlayer p_i1812_1_, TileEntityLaser p_i1812_2_) {
		this.tileLaser = p_i1812_2_;
		this.addSlotToContainer(new SlotItemHandler(p_i1812_2_.getFuel(), 0, 25, 30));
		this.addSlotToContainer(new SlotItemHandler(p_i1812_2_.getMedium(), 0, 80, 30));
		this.addSlotToContainer(new SlotItemHandler(p_i1812_2_.getCoupler(), 0, 134, 30));
		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(p_i1812_1_, j + i * 9 + 9, 8 + j * 18, 112 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(p_i1812_1_, i, 8 + i * 18, 170));
		}
	}

	@Override
	public void addListener(IContainerListener p_75132_1_) {
		super.addListener(p_75132_1_);
		p_75132_1_.sendWindowProperty(this, 0, this.tileLaser.laserBurnTime);
		p_75132_1_.sendWindowProperty(this, 1, this.tileLaser.currentItemBurnTime);
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return this.tileLaser.isUsableByPlayer(p_75145_1_);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener icrafting = this.listeners.get(i);

			if (this.laserBurnTime != this.tileLaser.laserBurnTime) {
				icrafting.sendWindowProperty(this, 0, this.tileLaser.laserBurnTime);
			}

			if (this.lastItemBurnTime != this.tileLaser.currentItemBurnTime) {
				icrafting.sendWindowProperty(this, 1, this.tileLaser.currentItemBurnTime);
			}
		}

		this.laserBurnTime = this.tileLaser.laserBurnTime;
		this.lastItemBurnTime = this.tileLaser.currentItemBurnTime;
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(p_82846_2_);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(p_82846_1_, itemstack1);
		}

		return itemstack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
		if (p_75137_1_ == 0) {
			this.tileLaser.laserBurnTime = p_75137_2_;
		}

		if (p_75137_1_ == 1) {
			this.tileLaser.currentItemBurnTime = p_75137_2_;
		}

	}

}