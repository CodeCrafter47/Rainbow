package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._EconomyManager;

import java.util.List;


public class _CmdSetWorth implements MC_Command {

    public static String permKey = "rainbow.setworth";

    public _CmdSetWorth() {
    }

    public String getCommandName() {
        return "setworth";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.LIGHT_PURPLE + "/setworth" + ChatColor.WHITE
                + " --- Set worth of item in hand";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (args.length <= 0) {
            _DiwUtils.reply(plr, ChatColor.RED + "Usage: /setworth 123.45");
        } else if (plr == null) {
            _DiwUtils.ConsoleMsg("Not from console!  Requires item in hand.");
        } else {
            double val = 0.0D;
            String strAmt = args[0];

            try {
                val = Double.parseDouble(strAmt);
            } catch (Exception var9) {
                _DiwUtils.ConsoleMsg("Invalid amount: " + strAmt);
                return;
            }

            if (val <= 0.0D) {
                val = 0.0D;
            }

            if (plr.getItemInHand() == null) {
                plr.sendMessage(ChatColor.RED + "You must have something in hand.");
            } else {
                MC_ItemStack is = plr.getItemInHand();

                _EconomyManager.SetItemWorth(is, val);
                String msg = ChatColor.GREEN
                        + String.format(
                        "Set Value of " + ChatColor.GOLD + "%s "
                                + ChatColor.GREEN + "to %.2f",
                        _EconomyManager.GetItemKey(is),
                        val);

                plr.sendMessage(msg);
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
}
