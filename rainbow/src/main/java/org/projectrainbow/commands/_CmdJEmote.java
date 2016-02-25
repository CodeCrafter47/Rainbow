package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import org.projectrainbow._EmoteUtils;

import java.util.List;

public class _CmdJEmote implements MC_Command {
    public String getCommandName() {
        return "jemote";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/jemote" + ChatColor.WHITE + " --- Emotes!";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        _EmoteUtils.HandleCommand(plr, args);
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.jemote");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }
}
