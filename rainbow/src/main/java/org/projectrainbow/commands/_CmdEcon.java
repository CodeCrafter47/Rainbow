package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.src.CommandBase;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._EconomyManager;

import java.util.Iterator;
import java.util.List;


public class _CmdEcon implements MC_Command {

    public static String permKey = "rainbow.econ";

    public String getCommandName() {
        return "econ";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.LIGHT_PURPLE + "/econ" + ChatColor.WHITE
                + " --- Manage Economy";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        String exactName;
        Double amt;

        if (args.length == 1 && args[0].equalsIgnoreCase("prices")) {
            _DiwUtils.reply(plr, "Sending to console...");
            int var10 = 0;

            _DiwUtils.ConsoleMsg("------------------------------------");
            Iterator var11 = _EconomyManager.itemWorth.keySet().iterator();

            while (var11.hasNext()) {
                exactName = (String) var11.next();
                ++var10;
                amt = (Double) _EconomyManager.itemWorth.get(exactName);
                _DiwUtils.ConsoleMsg(
                        String.format("%8.2f %s", amt, exactName));
            }

            _DiwUtils.ConsoleMsg("------------------------------------");
            _DiwUtils.ConsoleMsg(
                    String.format("Listed %d items.",
                            Integer.valueOf(var10)));
        } else {
            String tgtName;
            String strAmt;

            if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                tgtName = args[1];
                exactName = ServerWrapper.getInstance().getPlayerExactName(tgtName);
                if (exactName == null) {
                    _DiwUtils.reply(plr,
                            ChatColor.RED + "No player found named: "
                                    + ChatColor.YELLOW + tgtName);
                } else {
                    strAmt = args[2];
                    amt = 0.0D;

                    try {
                        amt = Double.parseDouble(strAmt);
                    } catch (Exception var8) {
                        _DiwUtils.reply(plr,
                                ChatColor.RED + "Amount is invalid: "
                                        + ChatColor.WHITE + strAmt);
                        return;
                    }

                    _EconomyManager.SetBalance(tgtName, amt);
                    _EconomyManager.ShowBalanceOf(plr, exactName);
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("add")) {
                tgtName = args[1];
                exactName = ServerWrapper.getInstance().getPlayerExactName(tgtName);
                if (exactName == null) {
                    _DiwUtils.reply(plr,
                            ChatColor.RED + "No player found named: "
                                    + ChatColor.YELLOW + tgtName);
                } else {
                    strAmt = args[2];
                    amt = 0.0D;

                    try {
                        amt = Double.parseDouble(strAmt);
                    } catch (Exception var9) {
                        _DiwUtils.reply(plr,
                                ChatColor.RED + "Amount is invalid: "
                                        + ChatColor.WHITE + strAmt);
                        return;
                    }

                    _EconomyManager.Deposit(tgtName, amt);
                    _EconomyManager.ShowBalanceOf(plr, exactName);
                }
            } else {
                _DiwUtils.reply(plr,
                        ChatColor.RED + "Usage: /econ set|add PlayerName Amount");
            }
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission(permKey);
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getAllUsernames())
                : null;
    }
}
