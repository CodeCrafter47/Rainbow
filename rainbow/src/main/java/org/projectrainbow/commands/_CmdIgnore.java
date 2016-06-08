package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import com.google.common.io.Files;
import net.minecraft.command.CommandBase;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._JOT_OnlineTimeUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _CmdIgnore implements MC_Command {

    private static Map<String, List<String>> ignoring = new ConcurrentHashMap<String, List<String>>();
    private static String Filename = "Ignores.dat";

    public _CmdIgnore() {}

    public static boolean AddIgnore(String pName, String pAnnoying) {
        pName = pName.toLowerCase();
        pAnnoying = pAnnoying.toLowerCase();
        List<String> res = ignoring.get(pName);

        if (res == null) {
            res = new ArrayList<String>();
        }

        if (((List) res).contains(pAnnoying)) {
            return false;
        } else {
            res.add(pAnnoying);
            ignoring.put(pName, res);
            return true;
        }
    }

    // todo figure out where these are used
    public static boolean IsIgnoring(String pName, String pAnnoying) {
        pName = pName.toLowerCase();
        pAnnoying = pAnnoying.toLowerCase();
        List res = (List) ignoring.get(pName);

        return res != null && res.contains(pAnnoying);
    }

    public static boolean RemoveIgnoring(String pName, String pAnnoying) {
        pName = pName.toLowerCase();
        pAnnoying = pAnnoying.toLowerCase();
        List<String> list = ignoring.get(pName);

        if (list == null) {
            return false;
        } else {
            boolean res = list.remove(pAnnoying);

            if (list.size() <= 0) {
                ignoring.remove(pName);
            } else {
                ignoring.put(pName, list);
            }

            return res;
        }
    }

    public String getCommandName() {
        return "ignore";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/ignore" + ChatColor.WHITE
                + " --- Ignore someone";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr != null) {
            if (args.length <= 0) {
                plr.sendMessage(
                        ChatColor.RED + "Usage 1: " + ChatColor.AQUA
                                + "/ignore " + ChatColor.YELLOW + "PlayerName");
                plr.sendMessage(
                        ChatColor.RED + "Usage 2: " + ChatColor.AQUA
                                + "/ignore " + ChatColor.GOLD + "list");
            } else {
                String pName = plr.getName();
                String exactName;

                if (args[0].equalsIgnoreCase("list")) {
                    List<String> tgtName1 = ignoring.get(pName);

                    exactName = ChatColor.AQUA + "No one";
                    if (tgtName1 != null && tgtName1.size() > 0) {
                        exactName = _DiwUtils.GetCommaList(tgtName1);
                    }

                    plr.sendMessage(
                            ChatColor.GREEN + "You are ignoring: "
                                    + ChatColor.YELLOW + exactName);
                } else {
                    String tgtName = args[0];

                    exactName = _JOT_OnlineTimeUtils.GetPlayerExactName(tgtName);
                    if (exactName == null) {
                        plr.sendMessage(
                                ChatColor.RED + "No player known by name: "
                                        + ChatColor.YELLOW + tgtName);
                    } else if (IsIgnoring(pName, exactName)) {
                        RemoveIgnoring(pName, exactName);
                        plr.sendMessage(
                                ChatColor.AQUA + "You are no longer ignoring "
                                        + ChatColor.YELLOW + exactName);
                    } else {
                        AddIgnore(pName, exactName);
                        plr.sendMessage(
                                ChatColor.GREEN + "You are now ignoring "
                                        + ChatColor.YELLOW + exactName);
                    }
                }
            }
        } else {
            System.out.println("--- Only for players!");
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.ignore");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return args.length >= 1
                ? CommandBase.getListOfStringsMatchingLastWord(args,
                _DiwUtils.getMinecraftServer().getAllUsernames())
                : null;
    }

    public static void SaveData() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + Filename);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(
                    new BufferedOutputStream(f));

            s.writeObject(ignoring);
            s.close();
            long msEnd = System.currentTimeMillis();
            String msg = ChatColor.YELLOW
                    + String.format("%-20s: %5d ignores.     Took %3d ms",
                    "Ignores", ignoring.size(),
                    msEnd - exc);

            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable var8) {
            System.out.println("**********************************************");
            System.out.println("SaveData: " + var8.toString());
            System.out.println("**********************************************");
        }

    }

    public static void LoadData() {
        try {
            File exc = new File(_DiwUtils.RainbowDataDirectory + Filename);
            File oldFile = new File(Filename);

            if (oldFile.exists()) {
                Files.move(oldFile, exc);
            }

            FileInputStream f = new FileInputStream(exc);
            ObjectInputStream s = new ObjectInputStream(
                    new BufferedInputStream(f));

            ignoring = (Map<String, List<String>>) s.readObject();
            s.close();
        } catch (Throwable var4) {
            System.out.println("Starting New Ignore DB: " + Filename);
            ignoring = new ConcurrentHashMap<String, List<String>>();
        }

    }
}
