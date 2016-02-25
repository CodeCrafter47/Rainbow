package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._WarpManager;

import java.util.List;


public class _CmdDelWarp implements MC_Command {

    public static String permKey = "rainbow.delwarp";

    public _CmdDelWarp() {}

    public String getCommandName() {
        return "delwarp";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.LIGHT_PURPLE + "/delwarp" + ChatColor.WHITE
                + " --- Delete a Warp";
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
            String warpName = _DiwUtils.ConcatArgs(args, 0);

            if (warpName.length() <= 0) {
                this.SendUsage(plr);
            } else {
                String actualWarpName = _WarpManager.GetActualWarpName(warpName);

                if (actualWarpName == null) {
                    _DiwUtils.reply(plr,
                            ChatColor.RED + "No warp named: " + ChatColor.YELLOW
                                    + warpName);
                } else {
                    _WarpManager.adminWarps.remove(actualWarpName);
                    _DiwUtils.reply(plr,
                            ChatColor.GREEN + "Removed warp: "
                                    + ChatColor.YELLOW + actualWarpName);
                    _WarpManager.SaveWarps();
                }
            }
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

    public void SendUsage(MC_Player plr) {
        _DiwUtils.reply(plr, this.getHelpLine(plr));
    }
}
