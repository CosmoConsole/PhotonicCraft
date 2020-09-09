package email.com.gmail.cosmoconsole.forge.photoniccraft.client.gui;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.inventory.ContainerLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityLaser;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLaser extends GuiContainer {
	private static final ResourceLocation laserGuiTextures = new ResourceLocation(
			"photoniccraft:textures/gui/laser.png"); // textures/gui/container/furnace.png
	private TileEntityLaser tileLaser;

	public GuiLaser(InventoryPlayer p_i1091_1_, TileEntityLaser p_i1091_2_) {
		super(new ContainerLaser(p_i1091_1_, p_i1091_2_));
		this.tileLaser = p_i1091_2_;
		this.ySize = 194;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(laserGuiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		if (tileLaser.isFuelBurning()) {
			int i1 = this.tileLaser.getFuelTimeRemainingScaled(13);
			this.drawTexturedModalRect(k + 25, l + 25 - i1, 176, 12 - i1, 14, i1 + 1);
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		String s = I18n.format(ModPhotonicCraft.MODID + "_container." + this.tileLaser.getName(), new Object[0]);
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("gui.photoniccraft_laser.power", this.tileLaser.getPower() + " Cd"),
				10, 60, this.tileLaser.powered ? 8388608 : 4210752);
		this.fontRenderer.drawString(I18n.format("gui.photoniccraft_laser.rf", this.tileLaser.getRFUsage()), 10, 84,
				4210752);
		this.fontRenderer.drawString(
				formatInt(this.tileLaser.getTrueEnergyStored()) + " / "
						+ formatInt(this.tileLaser.getMaximumPowerCapacity()) + " RF",
				10, 72, this.tileLaser.isPoweredByRF() ? 8388608 : 4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2,
				4210752);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	public String formatInt(int i) {
		return addThousandsGrouping(
				String.format("%0" + String.valueOf(this.tileLaser.getLogOfMaximumPowerCapacity()) + "d", i));
	}

	public static String addThousandsGrouping(String string) {
		StringBuilder s = new StringBuilder();
		int j = 0;
		for (int i = string.length() - 1; i >= 0; --i) {
			s.append(string.charAt(i));
			if (((j++) % 3) == 2)
				s.append(' ');
		}
		return s.reverse().toString();
	}
}