package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import org.projectrainbow._DiwUtils;

import java.util.List;


public class _CmdColors implements MC_Command {

    @Override
    public String getCommandName() {
        return "colors";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/colors " + ChatColor.WHITE + " --- "
                + _DiwUtils.RainbowString("Color Examples");
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr != null) {
            this.HandleColors(plr);
        } else {
            System.out.println("--- Only for players!");
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.colors");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }

    private void HandleColors(MC_Player cs) {
        cs.sendMessage(
                _DiwUtils.RainbowString(
                        "-----------------------------------------------------"));
        cs.sendMessage(
                ChatColor.DARK_BLUE + _DiwUtils.TextLabel("&1 = Dark Blue", 18)
                        + ChatColor.GRAY + _DiwUtils.TextLabel("&7 = Gray", 18)
                        + ChatColor.LIGHT_PURPLE + "&d = Light Purple");
        cs.sendMessage(
                ChatColor.DARK_GREEN
                        + _DiwUtils.TextLabel("&2 = Dark Green", 18)
                        + ChatColor.DARK_GRAY
                        + _DiwUtils.TextLabel("&8 = Dark Gray", 18)
                        + ChatColor.YELLOW + "&e = Yellow");
        cs.sendMessage(
                ChatColor.DARK_AQUA + _DiwUtils.TextLabel("&3 = Dark Aqua", 18)
                        + ChatColor.BLUE + _DiwUtils.TextLabel("&9 = Blue", 18)
                        + ChatColor.WHITE + "&f = White");
        cs.sendMessage(
                ChatColor.DARK_RED + _DiwUtils.TextLabel("&4 = Dark Red", 18)
                        + ChatColor.GREEN + _DiwUtils.TextLabel("&a = Green", 18)
                        + ChatColor.WHITE + ChatColor.BOLD + "&l = Bold");
        cs.sendMessage(
                ChatColor.DARK_PURPLE
                        + _DiwUtils.TextLabel("&5 = Dark Purple", 18)
                        + ChatColor.AQUA + _DiwUtils.TextLabel("&b = Aqua", 18)
                        + ChatColor.GRAY + ChatColor.ITALIC + "&o = Italic");
        cs.sendMessage(
                ChatColor.GOLD + _DiwUtils.TextLabel("&6 = Gold", 18)
                        + ChatColor.RED + _DiwUtils.TextLabel("&c = Red", 18)
                        + ChatColor.GRAY + ChatColor.UNDERLINE + "&n = Underline");
        cs.sendMessage("   ");
        cs.sendMessage(
                "" + ChatColor.DARK_BLUE + ChatColor.BOLD + "&1&l  "
                        + ChatColor.DARK_GREEN + ChatColor.BOLD + "&2&l  "
                        + ChatColor.DARK_AQUA + ChatColor.BOLD + "&3&l  "
                        + ChatColor.DARK_RED + ChatColor.BOLD + "&4&l  "
                        + ChatColor.DARK_PURPLE + ChatColor.BOLD + "&5&l  "
                        + ChatColor.GOLD + ChatColor.BOLD + "&6&l  " + ChatColor.GRAY
                        + ChatColor.BOLD + "&7&l  " + ChatColor.DARK_GRAY
                        + ChatColor.BOLD + "&8&l  ");
        cs.sendMessage(
                "" + ChatColor.BLUE + ChatColor.BOLD + "&9&l  "
                        + ChatColor.GREEN + ChatColor.BOLD + "&a&l  " + ChatColor.AQUA
                        + ChatColor.BOLD + "&b&l  " + ChatColor.RED + ChatColor.BOLD
                        + "&c&l  " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "&d&l  "
                        + ChatColor.YELLOW + ChatColor.BOLD + "&e&l  " + ChatColor.WHITE
                        + ChatColor.BOLD + "&f&l  ");
    }
}
