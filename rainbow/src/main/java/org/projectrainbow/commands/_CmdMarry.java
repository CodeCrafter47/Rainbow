package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._MarryListener;
import org.projectrainbow._MarryManager;

import java.util.List;


public class _CmdMarry implements MC_Command {

    public String getCommandName() {
        return "marry";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/marry" + ChatColor.WHITE
                + " --- Marry Someone!";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr != null) {
            if (args.length <= 0) {
                this.SendUsage(plr);
            } else if (args.length == 1
                    && args[0].equalsIgnoreCase("selectChurch")
                    && plr.isOp()) {
                _MarryListener.HandleSetChurchPosition(plr);
            } else if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
                _MarryManager.ShowMarriageInfo(plr, plr.getName());
            } else if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
                _MarryManager.ShowMarriageInfo(plr, args[1]);
            } else if (args.length == 1) {
                String spouseName = _MarryManager.GetSpouse(plr.getName());

                if (spouseName != null) {
                    plr.sendMessage(
                            ChatColor.RED + "You are already married! "
                                    + ChatColor.AQUA + "Spouse: " + ChatColor.YELLOW
                                    + spouseName);
                } else {
                    String tgtName = args[0];
                    MC_Player pTgt = ServerWrapper.getInstance().getOnlinePlayerByName(tgtName);

                    if (pTgt == null) {
                        plr.sendMessage(
                                ChatColor.RED + "No player online named: "
                                        + ChatColor.YELLOW + tgtName);
                    } else {
                        tgtName = pTgt.getName();
                        spouseName = _MarryManager.GetSpouse(tgtName);
                        if (spouseName != null) {
                            plr.sendMessage(
                                    ChatColor.YELLOW + tgtName + ChatColor.RED
                                            + " is already married to "
                                            + ChatColor.YELLOW + spouseName);
                        } else if (tgtName.equalsIgnoreCase(plr.getName())) {
                            plr.sendMessage(
                                    ChatColor.RED + "You can\'t marry yourself!");
                        } else if (!_MarryManager.IsInChurch((int) plr.getLocation().x,
                                (int) plr.getLocation().y, (int) plr.getLocation().z, plr.getLocation().dimension)) {
                            plr.sendMessage(
                                    ChatColor.RED
                                            + "You must be in church to marry!");
                        } else if (!_MarryManager.IsInChurch((int) pTgt.getLocation().x,
                                (int) pTgt.getLocation().y, (int) pTgt.getLocation().z,
                                pTgt.getLocation().dimension)) {
                            plr.sendMessage(
                                    ChatColor.RED
                                            + "Your partner must also be in church!");
                        } else {
                            double dx = plr.getLocation().x - pTgt.getLocation().x;
                            double dz = plr.getLocation().z - pTgt.getLocation().z;
                            double dy = plr.getLocation().y - pTgt.getLocation().y;
                            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                            if (dist >= 3.0D) {
                                plr.sendMessage(
                                        ChatColor.RED
                                                + "This is a wedding, stand closer together first!");
                            } else if (!_DiwUtils.TooSoon((ICommandSender) plr, "Marry", 60)) {
                                String tgtProposedTo = (String) _MarryManager.marryPropose.get(
                                        tgtName);

                                if (tgtProposedTo != null
                                        && tgtProposedTo.equalsIgnoreCase(
                                        plr.getName())) {
                                    _MarryManager.HandleMarriage(plr.getName(),
                                            tgtName);
                                    _MarryManager.marryPropose.remove(
                                            plr.getName());
                                    _MarryManager.marryPropose.remove(tgtName);
                                } else {
                                    _MarryManager.marryPropose.put(plr.getName(),
                                            tgtName);
                                    pTgt.sendMessage(
                                            ChatColor.YELLOW + plr.getName()
                                                    + ChatColor.GREEN
                                                    + " proposes marriage to you!");
                                    plr.sendMessage(
                                            ChatColor.GREEN
                                                    + "You propose marriage to "
                                                    + ChatColor.YELLOW + tgtName);
                                }
                            }
                        }
                    }
                }
            } else {
                this.SendUsage(plr);
            }
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.marry");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getAllUsernames())
                : null;
    }

    public void SendUsage(MC_Player cs) {
        cs.sendMessage(
                _DiwUtils.RainbowString("============ Marriage ============",
                "b"));
        cs.sendMessage(
                ChatColor.GOLD + "/marry " + ChatColor.YELLOW + "PlayerName");
        cs.sendMessage(
                ChatColor.GOLD + "/marry info " + ChatColor.YELLOW
                + "PlayerName");
        cs.sendMessage(
                ChatColor.GOLD + "/divorce " + ChatColor.YELLOW + "PlayerName");
        if (cs.isOp()) {
            cs.sendMessage(
                    ChatColor.LIGHT_PURPLE + "[Admin] " + ChatColor.GOLD
                    + "/marry SelectChurch " + ChatColor.GRAY
                    + "- Configure church location");
        }

        String spouseName = _MarryManager.GetSpouse(cs.getName());

        if (spouseName != null) {
            cs.sendMessage(
                    ChatColor.GREEN + "You are married to " + ChatColor.YELLOW
                    + spouseName);
        } else {
            cs.sendMessage(
                    ChatColor.GREEN + "You are single and ready to mingle!");
        }

    }
}
