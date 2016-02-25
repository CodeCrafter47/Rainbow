package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.src.CommandBase;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._PermMgr;

import java.util.List;


public class _CmdPerm implements MC_Command {

    public static boolean DebugMode = false;

    public String getCommandName() {
        return "perm";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.LIGHT_PURPLE + "/perm" + ChatColor.WHITE
                + " --- Manage Permissions";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (args.length == 0) {
            this.showUsage(plr);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
            DebugMode = !DebugMode;
            _DiwUtils.reply(plr,
                    ChatColor.GREEN + "Permission Debug Mode: " + ChatColor.AQUA
                            + DebugMode);
        } else {
            String name;
            String perms1;

            if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
                name = args[1];
                perms1 = args[2];
                _PermMgr.givePermission(name, perms1);
                _DiwUtils.reply(plr,
                        ChatColor.GREEN
                                + String.format("Gave \'%s\' Permission \'%s\'.",
                                new Object[]{name, perms1}));
            } else if (args.length == 3 && args[0].equalsIgnoreCase("take")) {
                name = args[1];
                perms1 = args[2];
                if (!_PermMgr.hasPermission(name, perms1)) {
                    _DiwUtils.reply(plr,
                            ChatColor.GREEN
                                    + String.format(
                                    "Permission \'%s\' is not granted to \'%s\'.",
                                    new Object[]{perms1, name}));
                } else {
                    _PermMgr.takePermission(name, perms1);
                    _DiwUtils.reply(plr,
                            ChatColor.GREEN
                                    + String.format(
                                    "Took Permission \'%s\' from \'%s\'.",
                                    new Object[]{perms1, name}));
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("test")) {
                name = args[1];
                perms1 = args[2];
                if (_PermMgr.hasPermission(name, perms1)) {
                    _DiwUtils.reply(plr,
                            ChatColor.GREEN
                                    + String.format("%s has permission \'%s\'",
                                    new Object[]{name, perms1}));
                } else {
                    _DiwUtils.reply(plr,
                            ChatColor.GREEN
                                    + String.format("%s does not have permission \'%s\'",
                                    new Object[]{name, perms1}));
                }

            } else if (args.length == 2 && args[0].equalsIgnoreCase("list")) {
                name = args[1];
                List perms = _PermMgr.getPermissions(name);
                String strPerms = "None";

                if (perms.size() > 0) {
                    strPerms = perms.toString();
                }

                _DiwUtils.reply(plr,
                        ChatColor.GREEN
                                + String.format(
                                ChatColor.YELLOW + "%s: " + ChatColor.AQUA
                                        + "%s",
                                new Object[]{name, strPerms}));
            } else {
                this.showUsage(plr);
            }
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.isOp();
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getAllUsernames())
                : null;
    }

    public void showUsage(MC_Player plr) {
        _DiwUtils.reply(plr,
                ChatColor.RED + "Usage: /perm " + ChatColor.AQUA + "give "
                        + ChatColor.YELLOW + "PlayerName " + ChatColor.LIGHT_PURPLE
                        + "PermissionName");
        _DiwUtils.reply(plr,
                ChatColor.RED + "Usage: /perm " + ChatColor.AQUA + "take "
                        + ChatColor.YELLOW + "PlayerName " + ChatColor.LIGHT_PURPLE
                        + "PermissionName");
        _DiwUtils.reply(plr,
                ChatColor.RED + "Usage: /perm " + ChatColor.AQUA + "test "
                        + ChatColor.YELLOW + "PlayerName " + ChatColor.LIGHT_PURPLE
                        + "PermissionName");
        _DiwUtils.reply(plr,
                ChatColor.RED + "Usage: /perm " + ChatColor.AQUA + "list "
                        + ChatColor.YELLOW + "PlayerName");
        _DiwUtils.reply(plr,
                ChatColor.RED + "Usage: /perm " + ChatColor.AQUA + "debug");
    }
}
