package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._EconomyManager;

import java.util.List;


public class _CmdPayday implements MC_Command {

    public static String permKey = "rainbow.payday";

    public _CmdPayday() {}

    public String getCommandName() {
        return "payday";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.LIGHT_PURPLE + "/payday" + ChatColor.WHITE
                + " --- Trigger Payday";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (!_DiwUtils.DoPaydays) {
            if (plr != null) {
                plr.sendMessage(
                        ChatColor.RED
                                + "Paydays disabled.  Use /diw toggle paydays");
            }
        } else {
            String msg = "PayDay triggered by " + (plr != null ? plr.getName() : "CONSOLE");

            _DiwUtils.ConsoleMsg(msg);
            _EconomyManager.DoPayDay();
        }

    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission(permKey);
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }
}
