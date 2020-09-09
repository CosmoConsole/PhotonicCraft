package email.com.gmail.cosmoconsole.forge.photoniccraft;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class RchCommand implements ICommand {

	private ArrayList<String> aliases;

	public RchCommand() {
		this.aliases = new ArrayList<String>();
		this.aliases.add("rch");
	}
	
	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "rch";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "rch <channelID>";
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length < 1) {
			p_71515_1_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rch.usage").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			return;
		}
		EntityPlayer p = (EntityPlayer) p_71515_1_;
		if (p.getHeldItem() == null || p.getHeldItem().getItem() != ModPhotonicCraft.pocketRadio) {
			p_71515_1_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rch.noRadio"));
			return;
		}
		String ch = p_71515_2_[0];
		int c = PhotonicAPI.channelNameToID(ch);
		if (c < 0) {
			p_71515_1_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rch.invalid"));
			return;
		}
		p.getHeldItem().stackTagCompound.setInteger("channel", c);
		p_71515_1_.addChatMessage(new ChatComponentTranslation("msg.photoniccraft_rch.switched", c));
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return p_71519_1_ instanceof EntityPlayer;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}
