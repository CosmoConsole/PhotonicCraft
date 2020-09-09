package email.com.gmail.cosmoconsole.forge.photoniccraft.server;

import java.util.ArrayList;
import java.util.List;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.util.PhotonicRadio;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * The /rch command used for changing the radio channel.
 */
public class RchCommand implements ICommand {

	private ArrayList<String> aliases;

	public RchCommand() {
		this.aliases = new ArrayList<String>();
		this.aliases.add("rch");
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return sender instanceof EntityPlayer;
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] p_71515_2_)
			throws CommandException {
		if (p_71515_2_.length < 1) {
			p_71515_1_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rch.usage")
					.setStyle(new Style().setColor(TextFormatting.RED)));
			return;
		}
		EntityPlayer p = (EntityPlayer) p_71515_1_;
		if (p.getHeldItem(EnumHand.MAIN_HAND) == null
				|| p.getHeldItem(EnumHand.MAIN_HAND).getItem() != PhotonicItems.pocketRadio) {
			p_71515_1_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rch.noRadio"));
			return;
		}
		String ch = p_71515_2_[0];
		int c = 0;
		if (ch.startsWith("#")) {
			try {
				c = Integer.parseInt(ch.substring(1));
				if (c < 0 || c >= PhotonicRadio.MAX_CHANNEL) {
					p_71515_1_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rch.invalid"));
					return;
				}
			} catch (NumberFormatException nfe) {
				p_71515_1_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rch.invalid"));
				return;
			}
		} else {
			c = PhotonicRadio.channelNameToID(ch);
			if (c < 0) {
				p_71515_1_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rch.invalid"));
				return;
			}
		}
		p.getHeldItem(EnumHand.MAIN_HAND).getTagCompound().setInteger("channel", c);
		p_71515_1_.sendMessage(new TextComponentTranslation("msg.photoniccraft_rch.switched",
				PhotonicRadio.channelIDToName(c) + " (#" + c + ")"));
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public String getName() {
		return "rch";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return null;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "rch <channelID>";
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}
