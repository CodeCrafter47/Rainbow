package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import org.projectrainbow._Announcer;

import java.util.List;

public class _CmdAnnouncer implements MC_Command {

    @Override
    public String getCommandName() {
        return "announcer";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.LIGHT_PURPLE + "/announcer" + ChatColor.WHITE
                + " --- Announcer!";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        _Announcer.HandleCommand(plr, args);
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.isOp();
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }
}
