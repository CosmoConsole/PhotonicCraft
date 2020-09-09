package email.com.gmail.cosmoconsole.forge.photoniccraft.item;

import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import io.netty.buffer.ByteBuf;
import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.PhotonicAPI;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityRemoteReceiver;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemTerahertzReceiver extends Item {
	public ItemTerahertzReceiver() {
        super();
        this.setUnlocalizedName(ModPhotonicCraft.MODID + "_terahertzReceiver");
        this.setTextureName(ModPhotonicCraft.MODID + ":terahertzReceiver");
        this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabTools);
	}
}
