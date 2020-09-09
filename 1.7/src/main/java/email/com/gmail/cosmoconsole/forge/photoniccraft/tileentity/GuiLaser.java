package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiLaser extends GuiContainer
{
    private static final ResourceLocation laserGuiTextures = new ResourceLocation("photoniccraft:textures/gui/laser.png"); //  textures/gui/container/furnace.png
    private TileEntityLaser tileLaser;

    public GuiLaser(InventoryPlayer p_i1091_1_, TileEntityLaser p_i1091_2_)
    {
        super(new ContainerLaser(p_i1091_1_, p_i1091_2_));
        this.tileLaser = p_i1091_2_;
        this.ySize = 194;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = I18n.format(this.tileLaser.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("gui.photoniccraft_laser.power",this.tileLaser.getPower() + " Cd"), 10, 60, this.tileLaser.powered ? 8388608 : 4210752);
        this.fontRendererObj.drawString(I18n.format("gui.photoniccraft_laser.rf",this.tileLaser.getRFUsage() + " RF/t"), 10, 84, 4210752);
        this.fontRendererObj.drawString(formatInt(this.tileLaser.storage.getEnergyStored()) + " / " + formatInt(this.tileLaser.storage.getMaxEnergyStored()) + " RF", 10, 72, this.tileLaser.isPoweredByRF() ? 8388608 : 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }
    public String formatInt(int i) {
    	String s = String.format("%05d", i);
    	return s.substring(0, 2) + " " + s.substring(2);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
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
}