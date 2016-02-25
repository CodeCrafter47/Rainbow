package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Entity;
import PluginReference.MC_Player;
import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayer;
import org.projectrainbow._DiwUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class _CmdRide implements MC_Command {

    public static ConcurrentHashMap<String, Boolean> dictAllow = new ConcurrentHashMap();

    public _CmdRide() {
    }

    public String getCommandName() {
        return "ride";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/ride" + ChatColor.WHITE
                + " --- Ride something!";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr == null) {
            System.out.println("Not from console!");
        } else {
            if (args.length != 1) {
                plr.sendMessage(
                        ChatColor.RED + "Usage: /ride " + ChatColor.YELLOW
                                + "Name" + ChatColor.WHITE + " - Ride Something!");
                plr.sendMessage(
                        ChatColor.RED + "Usage: /ride " + ChatColor.AQUA
                                + "toggle" + ChatColor.WHITE + " - Enable riders");
            } else {
                String tgtName = args[0];

                if (tgtName.equalsIgnoreCase("toggle")) {
                    Boolean tgtEnt1 = (Boolean) dictAllow.get(plr.getName());

                    if (tgtEnt1 == null) {
                        tgtEnt1 = false;
                    }

                    tgtEnt1 = !tgtEnt1;
                    dictAllow.put(plr.getName(), tgtEnt1);
                    if (tgtEnt1) {
                        plr.sendMessage(
                                ChatColor.AQUA + "You are now "
                                        + ChatColor.GREEN + " ALLOWING "
                                        + ChatColor.AQUA + " riders.");
                        plr.sendMessage(
                                "" + ChatColor.GOLD + ChatColor.ITALIC
                                        + "- Use [/throw] to eject a rider");
                        plr.sendMessage(
                                "" + ChatColor.GOLD + ChatColor.ITALIC
                                        + "- Use [/ride toggle] to disable.");
                    } else {
                        plr.sendMessage(
                                ChatColor.AQUA + "You are now " + ChatColor.RED
                                        + " NOT ALLOWING " + ChatColor.AQUA + " riders.");
                    }

                } else {
                    MC_Entity tgtEnt = _DiwUtils.GetNearbyEntityByName(plr,
                            5.0D, tgtName);

                    if (tgtEnt == null) {
                        plr.sendMessage(
                                ChatColor.RED + "No nearby target found named: "
                                        + ChatColor.YELLOW + tgtName);
                    } else {
                        tgtName = tgtEnt.getName();
                        if (tgtEnt instanceof EntityPlayer
                                && !plr.isOp() && !this.CanRide(tgtName)) {
                            plr.sendMessage(
                                    ChatColor.RED
                                            + "That target has not enabled riding.");
                        } else {
                            plr.setVehicle(tgtEnt);
                            tgtEnt.setRider(plr);
                            plr.sendMessage(
                                    ChatColor.GREEN + "You are now riding "
                                            + ChatColor.GOLD + tgtName);
                            if (tgtEnt instanceof MC_Player) {
                                ((MC_Player) tgtEnt).sendMessage(
                                        ChatColor.AQUA + plr.getName()
                                                + " is now riding you!");
                            }

                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.ride");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getAllUsernames())
                : null;
    }

    public boolean CanRide(String pName) {
        Boolean res = (Boolean) dictAllow.get(pName);

        return res == null ? false : res;
    }
}
