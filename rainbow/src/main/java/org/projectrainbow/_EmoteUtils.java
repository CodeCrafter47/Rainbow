package org.projectrainbow;


import PluginReference.ChatColor;
import PluginReference.MC_Player;
import com.google.common.io.Files;
import joebkt._EmoteEntry;
import net.minecraft.command.ICommandSender;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _EmoteUtils {

    public static Map<String, _EmoteEntry> emotes = new ConcurrentHashMap<String, _EmoteEntry>();
    private static String OldFilename = "Emotes.dat.vSnapshot";
    private static String Filename = "Emotes.dat";

    public _EmoteUtils() {}

    public static void LoadEmotes() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + Filename);
            File oldFile = new File(OldFilename);

            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }

            if (!file.exists()) {
                _DiwUtils.ConsoleMsg("Starting New Emote Database...");
                emotes = new ConcurrentHashMap<String, _EmoteEntry>();
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(
                    new BufferedInputStream(f));

            emotes = (Map<String, _EmoteEntry>) s.readObject();
            s.close();
            long msEnd = System.currentTimeMillis();
            String msg = String.format("%-20s: " + ChatColor.WHITE + "%5d emotes.   Took %3d ms", "Emote DB Loaded", emotes.size(), msEnd - exc);

            _DiwUtils.ConsoleMsg(ChatColor.YELLOW + msg);
            if (emotes.containsKey("jemote")) {
                emotes.remove("jemote");
                _DiwUtils.ConsoleMsg(ChatColor.LIGHT_PURPLE + "--- Removed emote named \'jemote\' for safety.");
            }
        } catch (Throwable var9) {
            var9.printStackTrace();
            _DiwUtils.ConsoleMsg("Starting New Emote Database...");
            emotes = new ConcurrentHashMap<String, _EmoteEntry>();
        }

    }

    public static void SaveEmotes() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + Filename);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(
                    new BufferedOutputStream(f));

            s.writeObject(emotes);
            s.close();
            long msEnd = System.currentTimeMillis();

            _DiwUtils.ConsoleMsg(ChatColor.YELLOW + String.format("%-20s: %5d emotes.   Took %3d ms", "Emote DB Save", emotes.size(), msEnd - exc));
        } catch (Throwable var7) {
            System.out.println("**********************************************");
            System.out.println("SaveEmotes: " + var7.toString());
            System.out.println("**********************************************");
        }

    }

    public static void ShowUsage(MC_Player cs) {
        _DiwUtils.reply(cs, "" + ChatColor.AQUA + ChatColor.BOLD + "----- Emotion Options -----");
        _DiwUtils.reply(cs, ChatColor.GOLD + "/jemote list " + ChatColor.WHITE + " -- List all emotes");
        _DiwUtils.reply(cs, ChatColor.GOLD + "/jemote mine " + ChatColor.WHITE + " -- List my emotes");
        if (cs == null || cs.isOp()) {
            _DiwUtils.reply(cs, ChatColor.LIGHT_PURPLE + "- Admin Options: " + ChatColor.WHITE + "Using \'smile\' as example...");
            _DiwUtils.reply(cs, ChatColor.LIGHT_PURPLE + "/jemote info smile");
            _DiwUtils.reply(cs, ChatColor.LIGHT_PURPLE + "/jemote delete smile");
            _DiwUtils.reply(cs, ChatColor.LIGHT_PURPLE + "/jemote add smile " + ChatColor.AQUA + "default" + ChatColor.GREEN + " smiles happily!");
            _DiwUtils.reply(cs, ChatColor.LIGHT_PURPLE + "/jemote add smile " + ChatColor.AQUA + "self" + ChatColor.GREEN + " smiles in a mirror.");
            _DiwUtils.reply(cs, ChatColor.LIGHT_PURPLE + "/jemote add smile " + ChatColor.AQUA + "other" + ChatColor.GREEN + " smiles at %s happily!");
        }

    }

    public static void ShowEmoteDetails(MC_Player cs, String emote) {
        emote = emote.toLowerCase();
        _EmoteEntry entry = emotes.get(emote);

        if (entry == null) {
            _DiwUtils.reply(cs,
                    ChatColor.RED + "Emote is not defined: " + ChatColor.YELLOW
                    + emote);
        } else {
            _DiwUtils.reply(cs,
                    ChatColor.GREEN + "---------------------------------------");
            _DiwUtils.reply(cs,
                    ChatColor.GOLD + "Emote: " + ChatColor.YELLOW + emote);
            _DiwUtils.reply(cs,
                    ChatColor.WHITE + "Created " + ChatColor.AQUA
                    + _DiwUtils.GetDateStringFromLong(entry.msCreated)
                    + ChatColor.WHITE + " by " + ChatColor.YELLOW
                    + entry.createdBy);
            _DiwUtils.reply(cs,
                    ChatColor.WHITE + "Last Updated " + ChatColor.AQUA
                    + _DiwUtils.GetDateStringFromLong(entry.msUpdated)
                    + ChatColor.WHITE + " by " + ChatColor.YELLOW
                    + entry.updatedBy);

            for (String key : entry.msg.keySet()) {
                _DiwUtils.reply(cs, ChatColor.AQUA + key + ": " + ChatColor.GREEN + entry.msg.get(key));
            }

            _DiwUtils.reply(cs, ChatColor.GREEN + "---------------------------------------");
        }
    }

    public static boolean CanDoEmote(MC_Player cs, String emote) {
        if (cs != null) {
            if (!cs.hasPermission("rainbow.jemote." + emote) && !cs.hasPermission("rainbow.jemote.*")) {
                return false;
            }
        }

        return true;
    }

    public static boolean HasAdminPerm(MC_Player cs) {
        return cs == null || cs.isOp();
    }

    public static boolean HandleEmote(MC_Player cs, String cmd) {
        if (cmd == null) {
            return false;
        } else if (cmd.length() <= 0) {
            return false;
        } else {
            String[] pieces = cmd.split("\\s+");
            String emote = pieces[0].toLowerCase();

            if (!emotes.containsKey(emote)) {
                return false;
            } else if (!CanDoEmote(cs, emote)) {
                _DiwUtils.reply(cs, ChatColor.RED + "You don\'t yet have this emote! :(...");
                return true;
            } else if (_DiwUtils.TooSoon((ICommandSender) cs, "Emote", 7)) {
                return true;
            } else {
                _EmoteEntry entry = emotes.get(emote);

                if (pieces.length > 1) {
                    String tgtName = pieces[1];

                    if (tgtName.equalsIgnoreCase(cs.getName())) {
                        _DiwUtils.MessageAllPlayers(cs, ChatColor.GREEN + cs.getName() + " " + entry.msg.get("self"));
                        return true;
                    } else {
                        MC_Player pTgt = ServerWrapper.getInstance().getOnlinePlayerByName(tgtName);

                        if (pTgt == null) {
                            _DiwUtils.reply(cs, ChatColor.RED + "Target player not found: " + ChatColor.YELLOW + tgtName);
                            return true;
                        } else {
                            String trailer = String.format(entry.msg.get("other"), pTgt.getName());

                            _DiwUtils.MessageAllPlayers(cs, ChatColor.GREEN + cs.getName() + " " + trailer);
                            return true;
                        }
                    }
                } else {
                    _DiwUtils.MessageAllPlayers(cs, ChatColor.GREEN + cs.getName() + " " + entry.msg.get("default"));
                    return true;
                }
            }
        }
    }

    public static boolean HandleCommand(MC_Player cs, String[] args) {
        String emote;

        if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            emote = _DiwUtils.GetCommaList(emotes.keySet());
            _DiwUtils.reply(cs, ChatColor.GREEN + "All Emotes: " + ChatColor.YELLOW + emote);
            return true;
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("mine")) {
            ArrayList<String> emote1 = new ArrayList<String>(emotes.keySet());

            new StringBuffer();
            Collections.sort(emote1);
            StringBuilder msNow3 = new StringBuilder();

            for (String msNow4 : emote1) {
                if (CanDoEmote(cs, msNow4)) {
                    if (msNow3.length() > 0) {
                        msNow3.append(", ");
                    }

                    msNow3.append(msNow4);
                }
            }

            _DiwUtils.reply(cs, ChatColor.GREEN + "Your Emotes: " + ChatColor.YELLOW + msNow3);
            return true;
        } else {
            if (HasAdminPerm(cs)) {
                if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
                    emote = args[1].toLowerCase();
                    if (!emotes.containsKey(emote)) {
                        _DiwUtils.reply(cs, ChatColor.RED + "Emote does not exist: " + ChatColor.YELLOW + emote);
                        return true;
                    }

                    emotes.remove(emote);
                    _DiwUtils.reply(cs, ChatColor.GREEN + "Removed Emote: " + ChatColor.YELLOW + emote);
                    SaveEmotes();
                    return true;
                }

                if (args.length >= 2 && args[0].equalsIgnoreCase("info")) {
                    emote = args[1].toLowerCase();
                    ShowEmoteDetails(cs, emote);
                    return true;
                }

                if (args.length >= 1 && args[0].equalsIgnoreCase("save")) {
                    SaveEmotes();
                    _DiwUtils.reply(cs, ChatColor.GREEN + "Saved emotes.");
                    return true;
                }

                if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
                    LoadEmotes();
                    _DiwUtils.reply(cs, ChatColor.GREEN + "Reloadedd emotes.");
                    return true;
                }

                if (args.length >= 4 && args[0].equalsIgnoreCase("add")) {
                    emote = args[1].toLowerCase();
                    if (emote.equalsIgnoreCase("jemote")) {
                        _DiwUtils.reply(cs, ChatColor.RED + "No! You can\'t make an emote named \'jemote\'.");
                        return true;
                    }

                    String entry1 = args[2].toLowerCase();
                    _EmoteEntry msNow2 = emotes.get(emote);
                    long msNow1 = System.currentTimeMillis();

                    if (msNow2 == null) {
                        msNow2 = new _EmoteEntry(emote);
                        msNow2.msCreated = msNow1;
                        msNow2.createdBy = cs.getName();
                    }

                    msNow2.msUpdated = msNow1;
                    msNow2.updatedBy = cs.getName();
                    if (!entry1.equalsIgnoreCase("default") && !entry1.equalsIgnoreCase("self") && !entry1.equalsIgnoreCase("other")) {
                        _DiwUtils.reply(cs, ChatColor.RED + "Unknown target type: " + ChatColor.YELLOW + entry1);
                        return true;
                    }

                    String msg = _DiwUtils.FullTranslate(_DiwUtils.ConcatArgs(args, 3));

                    msNow2.msg.put(entry1, msg);
                    emotes.put(emote, msNow2);
                    SaveEmotes();
                    ShowEmoteDetails(cs, emote);
                    _DiwUtils.reply(cs, ChatColor.GREEN + "Set " + ChatColor.YELLOW + emote + ChatColor.AQUA + " " + entry1 + ChatColor.GREEN + ": " + msg);
                    return true;
                }

                if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
                    emote = args[1].toLowerCase();
                    if (emote.equalsIgnoreCase("jemote")) {
                        _DiwUtils.reply(cs, ChatColor.RED + "No! You can\'t make an emote named \'jemote\'.");
                        return true;
                    }

                    _EmoteEntry entry = emotes.get(emote);
                    long msNow = System.currentTimeMillis();

                    if (entry == null) {
                        entry = new _EmoteEntry(emote);
                        entry.msCreated = msNow;
                        entry.createdBy = cs.getName();
                    }

                    entry.msUpdated = msNow;
                    entry.updatedBy = cs.getName();
                    emotes.put(emote, entry);
                    SaveEmotes();
                    ShowEmoteDetails(cs, emote);
                    return true;
                }
            }

            ShowUsage(cs);
            return false;
        }
    }
}
