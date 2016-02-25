package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._DynReward;

import java.util.List;


public class _CmdReward implements MC_Command {

    public static String permKey = "rainbow.reward";

    public String getCommandName() {
        return "reward";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.LIGHT_PURPLE + "/reward" + ChatColor.WHITE
                + " Reward System!";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr == null) {
            System.out.println("Console can\'t set rewards!");
        } else {
            if (args.length <= 0) {
                this.SendHelp(plr);
            } else if (args[0].equalsIgnoreCase("delete")) {
                _DynReward.CmdDelete(plr);
            } else if (args[0].equalsIgnoreCase("set")) {
                _DynReward.CmdSet(plr, args);
            } else if (args[0].equalsIgnoreCase("list")) {
                _DynReward.CmdList(plr);
            } else if (args[0].equalsIgnoreCase("iteminfo")) {
                MC_ItemStack is = plr.getItemInHand();

                plr.sendMessage(
                        ChatColor.LIGHT_PURPLE + "Name: " + ChatColor.GOLD
                                + is.getFriendlyName());
                plr.sendMessage(
                        ChatColor.LIGHT_PURPLE + "Dmg: " + ChatColor.GOLD
                                + is.getDamage());
                plr.sendMessage(
                        ChatColor.LIGHT_PURPLE + "ID: " + ChatColor.GOLD
                                + is.getId());
            } else {
                this.SendHelp(plr);
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
    public void SendHelp(MC_Player cs) {
        cs.sendMessage("\n" + _DiwUtils.RainbowString("Dynamic Reward System!"));
        cs.sendMessage(ChatColor.YELLOW + "-----------------------------------");
        cs.sendMessage(
                ChatColor.AQUA + "/reward delete" + ChatColor.GOLD
                + "     - Delete selected Reward");
        cs.sendMessage(
                ChatColor.AQUA + "/reward set [parms]" + ChatColor.GOLD
                + "       - Set reward");
        cs.sendMessage(
                ChatColor.AQUA + "/reward list" + ChatColor.GOLD
                + "       - List rewards");
        cs.sendMessage(
                ChatColor.AQUA + "/reward iteminfo" + ChatColor.GOLD
                + "       - Info about item in hand");
        cs.sendMessage(
                ChatColor.WHITE
                        + "Interact with Emerald to work with reward points.");
        cs.sendMessage(ChatColor.YELLOW + "-----------------------------------");
    }
}
