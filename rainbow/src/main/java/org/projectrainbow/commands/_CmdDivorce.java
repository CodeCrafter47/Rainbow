package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.src.CommandBase;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._MarryManager;

import java.util.List;


public class _CmdDivorce implements MC_Command {

    public _CmdDivorce() {}

    public String getCommandName() {
        return "divorce";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/divorce" + ChatColor.WHITE
                + " --- Divorce Someone!";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr == null) {
            System.out.println("--- Only for players!");
            return;
        }

        if (args.length <= 0) {
            this.SendUsage(plr);
        } else {
            _MarryManager.HandleDivorce(plr, args[0]);
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.divorce");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getAllUsernames())
                : null;
    }

    public void SendUsage(MC_Player cs) {
        cs.sendMessage(
                ChatColor.GOLD + "/divorce " + ChatColor.YELLOW + "PlayerName");
        String spouseName = _MarryManager.GetSpouse(cs.getName());

        if (spouseName != null) {
            cs.sendMessage(
                    ChatColor.GREEN + "You are married to " + ChatColor.YELLOW
                    + spouseName);
        } else {
            cs.sendMessage(
                    ChatColor.GREEN + "You are single and ready to mingle!");
        }

    }
}
