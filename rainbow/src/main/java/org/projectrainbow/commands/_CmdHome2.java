package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import joebkt._SerializableLocation;
import org.projectrainbow._HomeUtils;

import java.util.Iterator;
import java.util.List;


public class _CmdHome2 implements MC_Command {

    public _CmdHome2() {
    }

    public String getCommandName() {
        return "home2";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/home2" + ChatColor.WHITE + " --- Return home!";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr != null) {
            if (plr.getLocation().dimension != 0) {
                plr.sendMessage(
                        ChatColor.RED + "Home2 not allowed in this world!");
            } else {
                String pName1 = plr.getName();

                _SerializableLocation sloc = _HomeUtils.playerHomes2.get(plr.getUUID().toString());
                if(sloc == null) {
                    sloc = _HomeUtils.playerHomes2.get(pName1);
                }
                if (sloc == null) {
                    plr.sendMessage(
                            ChatColor.RED
                                    + "You don\'t have a home2 set. Try first: "
                                    + ChatColor.GOLD + "/sethome2");
                } else {
                    plr.teleport(new MC_Location(sloc.x, sloc.y, sloc.z, 0, sloc.yaw, sloc.pitch));
                    plr.sendMessage(ChatColor.GREEN + "You teleport home2 to " + ChatColor.WHITE + sloc.toString());
                }
            }
        } else {
            System.out.println("Dumping Homes2...");
            System.out.println("-------------------------------------------");
            Iterator pName = _HomeUtils.playerHomes2.keySet().iterator();

            while (pName.hasNext()) {
                String p = (String) pName.next();

                _SerializableLocation sloc = _HomeUtils.playerHomes2.get(p);
                String exc = String.format("Home2 for %s: %s",
                        p, sloc.toString());

                System.out.println(exc);
            }

            System.out.println("-------------------------------------------");
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.home2");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }
}
