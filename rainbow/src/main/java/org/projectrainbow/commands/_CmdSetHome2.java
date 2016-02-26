package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import org.projectrainbow._HomeUtils;
import joebkt._SerializableLocation;

import java.util.List;


public class _CmdSetHome2 implements MC_Command {

    public _CmdSetHome2() {
    }

    public String getCommandName() {
        return "sethome2";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/sethome2" + ChatColor.WHITE
                + " --- Set a home point!";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr == null) {
            System.out.println("Sethome2 is for Players only!");
        } else {

            if (plr.getLocation().dimension != 0) {
                plr.sendMessage(
                        ChatColor.RED + "SetHome2 not allowed in this world!");
            } else {
                String pName = plr.getName();
                _SerializableLocation sloc = new _SerializableLocation(plr.getLocation().x,
                        plr.getLocation().y, plr.getLocation().z, plr.getLocation().dimension, plr.getLocation().yaw, plr.getLocation().pitch);

                _HomeUtils.setHome2(plr.getUUID(), sloc);
                String msg = String.format("Home2 Set for %s set to %s",
                        pName, sloc.toString());

                System.out.println(msg);
                plr.sendMessage(
                        ChatColor.GREEN + "Home2 set to " + ChatColor.WHITE
                                + sloc.toString());
            }
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.sethome2");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }
}
