package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.src.CommandBase;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._JOT_OnlineTimeUtils;

import java.util.Iterator;
import java.util.List;


public class _CmdName implements MC_Command {

    public String getCommandName() {
        return "name";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/name" + ChatColor.WHITE
                + " --- Find exact player name";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (args.length <= 0) {
            _DiwUtils.reply(plr,
                    ChatColor.RED + "Usage: " + ChatColor.AQUA + "/name "
                            + ChatColor.YELLOW + "PartialName");
        } else {
            String piece = args[0].toLowerCase();

            _DiwUtils.reply(plr,
                    "\n" + ChatColor.YELLOW + "Finding player names containing "
                            + ChatColor.GOLD + piece);
            _DiwUtils.reply(plr,
                    _DiwUtils.RainbowString(
                            "----------------------------------------------"));
            int hits = 0;
            Iterator msg = _JOT_OnlineTimeUtils.Data.playerData.keySet().iterator();

            while (msg.hasNext()) {
                String nPlayers = (String) msg.next();

                if (nPlayers != null
                        && nPlayers.toLowerCase().indexOf(piece) >= 0) {
                    if (hits < 10) {
                        _DiwUtils.reply(plr,
                                ChatColor.GREEN + "Found Match: "
                                        + ChatColor.GOLD + nPlayers);
                    }

                    ++hits;
                    if (hits == 10) {
                        _DiwUtils.reply(plr,
                                ChatColor.YELLOW + "Stopping Output at " + hits
                                        + " matches.");
                    }
                }
            }

            int var7 = _JOT_OnlineTimeUtils.Data.playerData.keySet().size();
            String var8 = String.format(
                    ChatColor.AQUA + "Searched %d players. Found %d matches.",
                    new Object[]{Integer.valueOf(var7), Integer.valueOf(hits)});

            _DiwUtils.reply(plr, var8);
            _DiwUtils.reply(plr,
                    _DiwUtils.RainbowString(
                            "----------------------------------------------"));
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.name");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getAllUsernames())
                : null;
    }
}
