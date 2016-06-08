package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import net.minecraft.command.CommandBase;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._DiwUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _CmdTpaHere implements MC_Command {

    public static Map<String, MC_Location> tpahereMap = new ConcurrentHashMap();
    public static Map<String, Long> tpahereTimer = new ConcurrentHashMap();

    public _CmdTpaHere() {}

    public String getCommandName() {
        return "tpahere";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/tpahere " + ChatColor.WHITE
                + " TargetPlayerName";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {

        if (plr != null) {
            if (args.length == 1 && args[0].length() > 0) {
                String tgtName = args[0];
                String pName = plr.getName();

                if (pName.equalsIgnoreCase(tgtName)) {
                    plr.sendMessage(
                            ChatColor.RED + "You can\'t use that on yourself!");
                } else {
                    MC_Player pTgt = ServerWrapper.getInstance().getOnlinePlayerByName(tgtName);

                    if (pTgt == null) {
                        plr.sendMessage(
                                ChatColor.RED + "No player online with name: "
                                        + ChatColor.YELLOW + tgtName);
                    } else {
                        long curMS = System.currentTimeMillis();
                        Long prev = (Long) tpahereTimer.get(pName);

                        if (prev != null) {
                            long key = 15000L - (curMS - prev.longValue());

                            if (key > 0L) {
                                plr.sendMessage(
                                        ChatColor.RED
                                                + "Too Soon! Please wait: "
                                                + ChatColor.GOLD
                                                + _DiwUtils.TimeDeltaString_JustMinutesSecs(
                                                key));
                                return;
                            }
                        }

                        tpahereTimer.put(pName, Long.valueOf(curMS));
                        if (_CmdIgnore.IsIgnoring(pTgt.getName(),
                                plr.getName())) {
                            plr.sendMessage(
                                    ChatColor.RED
                                            + "That player is ignoring you!");
                        } else {
                            pTgt.sendMessage(
                                    ChatColor.YELLOW + pName + ChatColor.GRAY
                                            + " wants you to teleport to them. To accept, type: "
                                            + ChatColor.GOLD + "/tpaccept " + pName);
                            String key1 = tgtName.toLowerCase() + "."
                                    + pName.toLowerCase();
                            tpahereMap.put(key1, plr.getLocation());
                            plr.sendMessage(
                                    ChatColor.GREEN
                                            + "Teleport Request now active for: "
                                            + ChatColor.YELLOW + tgtName);
                        }
                    }
                }
            } else {
                plr.sendMessage(
                        ChatColor.RED + "Usage: " + ChatColor.AQUA + "/tpahere "
                                + ChatColor.WHITE + " TargetPlayerName");
            }
        } else {
            System.out.println("--- Only for players!");
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.tpahere");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getAllUsernames())
                : null;
    }
}
