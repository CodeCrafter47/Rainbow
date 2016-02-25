package org.projectrainbow;


import PluginReference.ChatColor;
import PluginReference.MC_Player;
import com.google.common.io.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class _Announcer {

    public static ArrayList<String> announcements = null;
    public static int Seconds = 60;
    public static int CurrentSecondCount = 0;
    public static String AnnouncerPrefix = null;
    public static int CurIdx = 0;
    public static int LastIdx = -1;
    public static boolean IsEnabled = true;
    public static String Filename = "announcements.txt";

    public _Announcer() {}

    public static void LoadAnnouncements() {
        try {
            Seconds = 60;
            AnnouncerPrefix = ChatColor.RED + "[Announcement]";
            announcements = new ArrayList<String>();
            File exc = new File(_DiwUtils.RainbowDataDirectory + Filename);
            File oldFile = new File(Filename);

            if (oldFile.exists()) {
                Files.move(oldFile, exc);
            }

            BufferedReader br = new BufferedReader(new FileReader(exc));

            String line;

            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    line = line.trim();
                    String lwr = line.toLowerCase();

                    if (!lwr.startsWith(";")) {
                        if (lwr.startsWith("delay=")) {
                            Seconds = Integer.parseInt(line.substring(6));
                            if (Seconds <= 0) {
                                Seconds = Integer.MAX_VALUE;
                                IsEnabled = false;
                            }
                        } else if (lwr.startsWith("prefix=")) {
                            AnnouncerPrefix = _DiwUtils.TranslateColorString(
                                    line.substring(7), true);
                        } else {
                            line = _DiwUtils.TranslateColorString(line, true);
                            announcements.add(line);
                        }
                    }
                }
            }

            br.close();
        } catch (Exception var5) {
            _DiwUtils.ConsoleMsg("No Announcements loaded.");
        }

    }

    public static void SendUsage(MC_Player cs) {
        _DiwUtils.reply(cs,
                "Usage: /announcer [reload | info | forcenext | list | broadcast | toggle]");
    }

    public static void HandleCommand(MC_Player cs, String[] args) {
        if (!cs.isOp()) {
            _DiwUtils.reply(cs, ChatColor.RED + "[Announcer] Ops Only!");
        } else if (args.length <= 0) {
            SendUsage(cs);
        } else if (args[0].equalsIgnoreCase("reload")) {
            LoadAnnouncements();
            _DiwUtils.reply(cs,
                    ChatColor.GREEN + "Reloaded " + ChatColor.YELLOW
                    + announcements.size() + ChatColor.GREEN
                    + " Announcements. Delay=" + ChatColor.WHITE + Seconds + "s");
        } else if (args[0].equalsIgnoreCase("toggle")) {
            IsEnabled = !IsEnabled;
            if (!IsEnabled) {
                _DiwUtils.reply(cs,
                        ChatColor.AQUA + "Announcer " + ChatColor.RED
                        + "Disabled");
            } else {
                _DiwUtils.reply(cs,
                        ChatColor.AQUA + "Announcer " + ChatColor.GREEN
                        + "Enabled");
            }

        } else if (args[0].equalsIgnoreCase("info")) {
            LoadAnnouncements();
            _DiwUtils.reply(cs,
                    ChatColor.GREEN + "Number of Announcements: "
                    + ChatColor.WHITE + announcements.size());
            _DiwUtils.reply(cs,
                    ChatColor.GREEN + "Seconds Delay: " + ChatColor.WHITE
                    + Seconds);
            _DiwUtils.reply(cs,
                    ChatColor.GREEN + "Seconds Left Until Next Announcement: "
                    + ChatColor.WHITE + (Seconds - CurrentSecondCount));
            _DiwUtils.reply(cs,
                    ChatColor.GREEN + "Announcement Prefix: " + AnnouncerPrefix);
        } else if (args[0].equalsIgnoreCase("forcenext")) {
            CurrentSecondCount = Seconds;
            _DiwUtils.reply(cs, ChatColor.GREEN + "OK.");
        } else {
            int idx;

            if (!args[0].equalsIgnoreCase("list")) {
                if (args[0].equalsIgnoreCase("broadcast")) {
                    if (args.length <= 1) {
                        _DiwUtils.reply(cs,
                                ChatColor.RED + "Usage: /announcer broadcast #");
                    } else {
                        idx = -1;

                        try {
                            idx = Integer.parseInt(args[1]);
                        } catch (Throwable var4) {
                            ;
                        }

                        --idx;
                        if (idx >= 0 && idx < announcements.size()) {
                            BroadcastIdx(idx);
                        } else {
                            _DiwUtils.reply(cs,
                                    ChatColor.RED
                                    + "Invalid #. Pick one between 1 and "
                                    + announcements.size());
                        }
                    }
                } else {
                    SendUsage(cs);
                }
            } else {
                for (idx = 0; idx < announcements.size(); ++idx) {
                    _DiwUtils.reply(cs,
                            ChatColor.WHITE
                            + String.format(
                                    ChatColor.GOLD + "%d: " + ChatColor.WHITE
                                    + "%s",
                                    new Object[] {
                        Integer.valueOf(idx + 1),
                        announcements.get(idx)}));
                }

                _DiwUtils.reply(cs,
                        ChatColor.GREEN + "Number of Announcements: "
                        + ChatColor.WHITE + announcements.size());
            }
        }
    }

    public static void BroadcastIdx(int idx) {
        if (announcements.size() > 0) {
            idx %= announcements.size();
            _DiwUtils.MessageAllPlayers(
                    AnnouncerPrefix + " " + ChatColor.WHITE
                    + (String) announcements.get(idx));
        }
    }

    public static void runSecondCount() {
        if (IsEnabled) {
            ++CurrentSecondCount;
            if (CurrentSecondCount >= Seconds) {
                CurrentSecondCount = 0;
                int max = announcements.size();
                int idx = (int) ((double) max * Math.random());

                if (max > 1) {
                    while (idx == LastIdx) {
                        idx = (int) ((double) max * Math.random());
                    }
                }

                LastIdx = idx;

                try {
                    BroadcastIdx(idx);
                } catch (Throwable var3) {
                    _DiwUtils.ConsoleMsg(
                            "Error in Announcer: " + var3.toString());
                }
            }

        }
    }
}
