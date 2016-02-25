package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;

import java.util.List;


public class _CmdThrow implements MC_Command {

    public _CmdThrow() {
    }

    public String getCommandName() {
        return "throw";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/throw" + ChatColor.WHITE
                + " --- Use if something riding you!";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr == null) {
            System.out.println("Not from console!");
        } else {

            if (plr.getRider() == null) {
                plr.sendMessage(ChatColor.RED + "You don\'t have a rider!");
            } else {
                plr.sendMessage(
                        ChatColor.GREEN + "You eject rider: " + ChatColor.WHITE
                                + plr.getRider().getName());
                plr.setRider(null);
            }
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.throw");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }
}
