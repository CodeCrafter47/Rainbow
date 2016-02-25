package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import net.minecraft.src.CommandBase;
import org.projectrainbow._DiwUtils;

import java.util.Collections;
import java.util.List;


public class _CmdTpAccept implements MC_Command {

    public _CmdTpAccept() {
    }

    public List<String> getAliases() {
        return Collections.singletonList("tpaaccept");
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/tpaccept" + ChatColor.WHITE
                + " RequestingPlayerName";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr != null) {
            MC_Player player = plr;
            if (args.length == 1 && args[0].length() > 0) {
                String tgtName = args[0];
                String pName = player.getName();

                if (pName.equalsIgnoreCase(tgtName)) {
                    player.sendMessage(
                            ChatColor.RED + "You can\'t use that on yourself!");
                } else {
                    String key = pName.toLowerCase() + "."
                            + tgtName.toLowerCase();
                    MC_Location sloc = _CmdTpaHere.tpahereMap.get( key);

                    if (sloc == null) {
                        player.sendMessage(
                                ChatColor.RED
                                        + "No pending teleport request by "
                                        + ChatColor.YELLOW + tgtName);
                    } else if (player.getLocation().dimension != sloc.dimension) {
                        player.sendMessage(
                                ChatColor.RED + "You must be in the same world.");
                    } else {
                        _CmdTpaHere.tpahereMap.remove(key);
                        player.teleport(sloc);
                        player.sendMessage(
                                ChatColor.GREEN + "You teleport to "
                                        + ChatColor.YELLOW + tgtName + ChatColor.WHITE
                                        + " @ " + sloc.toString());
                    }
                }
            } else {
                player.sendMessage(
                        ChatColor.RED + "Usage: " + ChatColor.AQUA + "/tpaccept"
                                + ChatColor.WHITE + " RequestingPlayerName");
            }
        } else {
            System.out.println("--- Only for players!");
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.tpaccept");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getAllUsernames())
                : null;
    }

    public String getCommandName() {
        return "tpaccept";
    }
}
