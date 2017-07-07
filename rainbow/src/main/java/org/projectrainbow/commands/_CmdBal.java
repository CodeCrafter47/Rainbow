package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.command.CommandBase;
import org.projectrainbow._ColorHelper;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._EconomyManager;
import org.projectrainbow._UUIDMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class _CmdBal implements MC_Command {

    @Override
    public String getCommandName() {
        return "bal";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return _ColorHelper.AQUA + "/bal" + _ColorHelper.WHITE + " --- Check Balance";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (args.length <= 0 && plr != null) {
            _EconomyManager.ShowBalance(plr);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("top")) {
            this.ShowBalTop(plr);
        } else if (args.length == 1) {
            String tgtName = args[0];
            String exactName = ServerWrapper.getInstance().getPlayerExactName(tgtName);

            if (exactName == null) {
                _DiwUtils.reply(plr,
                        ChatColor.RED + "No player found named: "
                                + ChatColor.YELLOW + tgtName);
            } else {
                _EconomyManager.ShowBalanceOf(plr, exactName);
            }
        } else {
            _DiwUtils.reply(plr, getHelpLine(plr));
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.bal");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getOnlinePlayerNames())
                : null;
    }

    public void ShowBalTop(MC_Player cs) {
        ArrayList<String> keys = new ArrayList<>(_EconomyManager.economy.keySet());

        keys.sort(Comparator.<String>comparingDouble(_EconomyManager.economy::get).reversed());

        _DiwUtils.reply(cs,
                ChatColor.GREEN + "Balance" + ChatColor.DARK_GRAY
                        + _DiwUtils.TextAlignTrailerPerfect("Balance", 12)
                        + ChatColor.YELLOW + "   " + "Player Name");
        _DiwUtils.reply(cs,
                ChatColor.GREEN + "-----------" + ChatColor.DARK_GRAY
                        + _DiwUtils.TextAlignTrailerPerfect("-----------", 12)
                        + ChatColor.YELLOW + "   " + "---------------");
        int nShown = 0;
        Iterator var5 = keys.iterator();

        while (var5.hasNext()) {
            String key = (String) var5.next();
            Double amt = _EconomyManager.economy.get(key);
            String exactName = ServerWrapper.getInstance().getPlayerExactName(key);
            if (key.length() > 16) {
                exactName = _UUIDMapper.getName(UUID.fromString(key));
            }

            if (exactName == null) {
                exactName = key;
            }

            String strAmt = String.format("%.2f", amt);

            _DiwUtils.reply(cs,
                    ChatColor.GREEN + strAmt + ChatColor.DARK_GRAY
                            + _DiwUtils.TextAlignTrailerPerfect(strAmt, 12)
                            + ChatColor.YELLOW + "    " + exactName);
            ++nShown;
            if (nShown > 10) {
                break;
            }
        }

    }
}
