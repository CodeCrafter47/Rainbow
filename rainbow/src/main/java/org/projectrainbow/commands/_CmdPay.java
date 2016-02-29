package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.src.CommandBase;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._EconomyManager;
import org.projectrainbow._UUIDMapper;

import java.util.List;
import java.util.UUID;


public class _CmdPay implements MC_Command {

    public _CmdPay() {}

    public String getCommandName() {
        return "pay";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/pay" + ChatColor.WHITE + " --- Pay someone";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr == null) {
            System.out.println("--- Only for players!");
            return;
        }
        if (args.length != 2) {
            _DiwUtils.reply(plr,
                    ChatColor.RED + "Usage: " + ChatColor.GOLD + "/pay "
                            + ChatColor.YELLOW + "PlayerName" + ChatColor.GREEN
                            + " Amount");
        } else {
            Double amt = Double.valueOf(0.0D);

            try {
                amt = Double.valueOf(Double.parseDouble(args[1]));
            } catch (Throwable var9) {
                _DiwUtils.reply(plr,
                        ChatColor.RED + "Invalid Amount: " + ChatColor.GREEN
                                + args[1]);
                return;
            }

            if (amt < 10.0D) {
                _DiwUtils.reply(plr,
                        ChatColor.RED + "Amount must be at least 10.00");
            } else {
                String tgtName = args[0];
                MC_Player pTgt = ServerWrapper.getInstance().getOnlinePlayerByName(tgtName);

                if (pTgt == null) {
                    _DiwUtils.reply(plr,
                            ChatColor.RED + "No player online with name: "
                                    + ChatColor.YELLOW + tgtName);
                } else {
                    tgtName = pTgt.getName();
                    if (tgtName.equalsIgnoreCase(plr.getName())) {
                        _DiwUtils.reply(plr,
                                ChatColor.RED + "You can\'t pay yourself!");
                    } else {
                        Double bal = _EconomyManager.GetBalance(plr);

                        if (bal < amt) {
                            _DiwUtils.reply(plr,
                                    ChatColor.RED + "You only have "
                                            + ChatColor.GREEN
                                            + String.format("%.2f", bal));
                        } else {
                            _EconomyManager.Withdraw(plr, amt);
                            _EconomyManager.Deposit(_UUIDMapper.getUUID(tgtName), amt);
                            String logMsg = String.format("%s paid %.2f to %s",
                                    plr.getName(), amt, tgtName);

                            System.out.println("ECONOMY: " + logMsg);
                            String strAmt = String.format("%.2f",
                                    amt);

                            pTgt.sendMessage(
                                    ChatColor.GREEN + "You receive "
                                            + ChatColor.AQUA + strAmt + ChatColor.GREEN
                                            + " from " + ChatColor.YELLOW + plr.getName());
                            _DiwUtils.reply(plr,
                                    ChatColor.GREEN + "You sent "
                                            + ChatColor.RED + strAmt + ChatColor.GREEN
                                            + " to " + ChatColor.YELLOW + tgtName);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.pay");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getAllUsernames())
                : null;
    }
}
