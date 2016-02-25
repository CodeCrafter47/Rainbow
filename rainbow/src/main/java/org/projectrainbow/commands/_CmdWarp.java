package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import net.minecraft.src.ICommandSender;
import org.projectrainbow._DiwUtils;
import joebkt._SerializableLocation;
import org.projectrainbow._WarpManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class _CmdWarp implements MC_Command {

    public _CmdWarp() {
    }

    public String getCommandName() {
        return "warp";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/warp" + ChatColor.WHITE
                + " --- Warp somewhere";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr == null) {
            System.out.println("--- Only for players!");
        } else {
            String actualWarpName;

            if (args.length != 0) {
                if (args.length == 1 && args[0].length() > 0) {
                    String warpName1 = args[0].toLowerCase();

                    actualWarpName = _WarpManager.GetActualWarpName(warpName1);
                    if (actualWarpName == null) {
                        plr.sendMessage(
                                ChatColor.RED + "No warp found with name: "
                                        + ChatColor.AQUA + warpName1);
                    } else {
                        _SerializableLocation sloc1 = (_SerializableLocation) _WarpManager.adminWarps.get(
                                actualWarpName);

                        if (!_DiwUtils.TooSoon((ICommandSender) plr, "warp", 5)) {
                            plr.teleport(new MC_Location(sloc1.x, sloc1.y, sloc1.z, sloc1.dimension, sloc1.yaw, sloc1.pitch));
                            plr.sendMessage(
                                    ChatColor.GREEN + "You warp to "
                                            + ChatColor.AQUA + actualWarpName);
                        }
                    }
                } else {
                    plr.sendMessage(
                            ChatColor.RED + "Usage: /warp " + ChatColor.WHITE
                                    + "WarpName");
                }
            } else {
                ArrayList warpName = new ArrayList();
                Iterator sloc = _WarpManager.adminWarps.keySet().iterator();

                while (sloc.hasNext()) {
                    actualWarpName = (String) sloc.next();
                    warpName.add(actualWarpName);
                }

                actualWarpName = ChatColor.GRAY + "None";
                if (warpName.size() > 0) {
                    actualWarpName = _DiwUtils.GetCommaList(warpName);
                }

                plr.sendMessage(
                        ChatColor.GREEN + "Warps Available: " + ChatColor.AQUA
                                + actualWarpName);
            }
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.warp");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }
}
