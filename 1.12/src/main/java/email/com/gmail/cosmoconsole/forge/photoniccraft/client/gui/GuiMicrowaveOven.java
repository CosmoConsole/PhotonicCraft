package email.com.gmail.cosmoconsole.forge.photoniccraft.client.gui;

import org.lwjgl.opengl.GL11;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.inventory.ContainerMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.tileentity.TileEntityMicrowaveOven;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMicrowaveOven extends GuiContainer {
	private static final ResourceLocation laserGuiTextures = new ResourceLocation(
			"photoniccraft:textures/gui/microwave.png");
	private TileEntityMicrowaveOven tileLaser;

	public GuiMicrowaveOven(InventoryPlayer p_i1091_1_, TileEntityMicrowaveOven p_i1091_2_) {
		super(new ContainerMicrowaveOven(p_i1091_1_, p_i1091_2_));
		this.tileLaser = p_i1091_2_;
		this.ySize = 152;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(laserGuiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		String s = I18n.format(ModPhotonicCraft.MODID + "_container.microwaveoven", new Object[0]);
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(
				String.format("%.2f",
						this.tileLaser.neededprogress == 0 ? 0.0
								: (100 * (double) this.tileLaser.progress / this.tileLaser.neededprogress))
						+ " %",
				16, 27, 4210752);
		this.fontRenderer.drawString(
				this.tileLaser.getTrueEnergyStored() + " / " + TileEntityMicrowaveOven.MAX_RF_CAPACITY + " RF", 16, 45,
				4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2,
				4210752);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}