package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import org.projectrainbow._DiwUtils;
import joebkt._SerializableLocation;
import org.projectrainbow._WarpManager;

import java.util.List;


public class _CmdSetWarp implements MC_Command {

    public static String permKey = "rainbow.setwarp";

    public _CmdSetWarp() {
    }

    public String getCommandName() {
        return "setwarp";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.LIGHT_PURPLE + "/setwarp" + ChatColor.WHITE
                + " --- Set a warp";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (args.length > 0 && plr != null) {
            String warpName = _DiwUtils.ConcatArgs(args, 0);

            if (warpName.length() <= 0) {
                this.SendUsage(plr);
            } else {
                String actualWarpName = _WarpManager.GetActualWarpName(warpName);

                if (actualWarpName != null) {
                    _DiwUtils.reply(plr,
                            ChatColor.RED + "Already a warp named: "
                                    + ChatColor.YELLOW + actualWarpName);
                } else {
                    MC_Location location = plr.getLocation();
                    _SerializableLocation sloc = new _SerializableLocation(location.x, location.y, location.z, location.dimension, location.yaw, location.pitch);

                    _WarpManager.adminWarps.put(warpName, sloc);
                    _DiwUtils.reply(plr,
                            ChatColor.GREEN + "Set warp " + ChatColor.YELLOW
                                    + warpName + ChatColor.WHITE + ": "
                                    + sloc.toString());
                    _WarpManager.SaveWarps();
                }
            }
        } else {
            this.SendUsage(plr);
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
