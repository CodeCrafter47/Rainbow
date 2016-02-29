package org.projectrainbow;


import PluginReference.ChatColor;
import PluginReference.MC_Player;
import com.google.common.io.Files;
import joebkt._JOT_OnlineData;
import joebkt._JOT_OnlineTimeEntry;
import net.minecraft.src.EntityPlayer;
import org.projectrainbow.commands._CmdDiw;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;


public class _JOT_OnlineTimeUtils {

    public static String DataFilename = "PlayerOnlineTime.dat";
    public static _JOT_OnlineData Data = new _JOT_OnlineData();
    public static boolean ShuttingDown = false;

    public _JOT_OnlineTimeUtils() {
    }

    public static String GetPlayerExactName(String tgtName) {
        return _UUIDMapper.getCaseCorrectedPlayerName(tgtName);
    }

    public static void LoadData() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + DataFilename);
            File oldFile = new File(DataFilename);

            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }

            if (!file.exists()) {
                _DiwUtils.ConsoleMsg("Starting new file: " + DataFilename);
                Data = new _JOT_OnlineData();
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(
                    new BufferedInputStream(f));

            Data = (_JOT_OnlineData) s.readObject();
            s.close();
            long msEnd = System.currentTimeMillis();
            String loadedSince = "Online DB Loaded";
            String msg = String.format(
                    "%-20s: " + ChatColor.WHITE + "%5d players.  Took %3d ms",
                    loadedSince,
                    Data.playerData.size(),
                    msEnd - exc);

            _DiwUtils.ConsoleMsg(ChatColor.YELLOW + msg);
        } catch (Throwable var10) {
            var10.printStackTrace();
            _DiwUtils.ConsoleMsg("Starting new file: " + DataFilename);
            Data = new _JOT_OnlineData();
        }

    }

    public static void SaveData() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + DataFilename);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(
                    new BufferedOutputStream(f));

            s.writeObject(Data);
            s.close();
            long msEnd = System.currentTimeMillis();
            String msg = ChatColor.YELLOW
                    + String.format("%-20s: %5d players.     Took %3d ms",
                    "Online DB Save",
                    Data.playerData.size(),
                    msEnd - exc);

            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable var8) {
            System.out.println("**********************************************");
            System.out.println("SaveData: " + var8.toString());
            System.out.println("**********************************************");
        }

    }

    public static void HandlePlayerLogin(MC_Player plr) {
        if (!ShuttingDown) {
            _JOT_OnlineTimeEntry entry = Data.playerData.get(plr.getUUID().toString());
            if (entry == null) {
                entry = Data.playerData.remove(plr.getName());
            }

            if (entry == null) {
                System.out.println("-----------------------------------------");
                System.out.println("FIRST LOGIN ON THIS SERVER: " + plr.getName());
                System.out.println("-----------------------------------------");
                if (_DiwUtils.DoWelcomeNewPlayers) {
                    _DiwUtils.MessageAllPlayers(
                            _DiwUtils.RainbowString("--- Welcome New Player ---",
                                    "b"));
                }

                entry = new _JOT_OnlineTimeEntry();
            }

            _DiwUtils.ConsoleMsg(
                    ChatColor.GREEN + "Login: " + ChatColor.YELLOW + plr.getName()
                            + ChatColor.AQUA + ", Online Time: "
                            + _DiwUtils.TimeDeltaString(entry.msTotal) + ChatColor.WHITE
                            + ", Last Login: "
                            + _DiwUtils.GetDateStringFromLong(entry.msLastLogin));
            entry.msLastLogin = System.currentTimeMillis() + 1L;
            Data.playerData.put(plr.getUUID().toString(), entry);
        }
    }

    public static void HandlePlayerLogout(String pName, UUID argUUID) {
        if (!ShuttingDown) {
            _JOT_OnlineTimeEntry entry = Data.playerData.get(argUUID.toString());

            if (entry == null) {
                _DiwUtils.ConsoleMsg(
                        ChatColor.RED + "Unexpected Logout before Login: "
                                + ChatColor.YELLOW + pName);
            } else {
                entry.msLastLogout = System.currentTimeMillis();
                long msThisSession = entry.msLastLogout - entry.msLastLogin;

                entry.msTotal += msThisSession;
                Data.playerData.put(argUUID.toString(), entry);
                _DiwUtils.ConsoleMsg(
                        ChatColor.RED + "Logout: " + ChatColor.YELLOW
                                + String.format("%-16s", pName)
                                + ChatColor.AQUA + ": Online Time: "
                                + _DiwUtils.TimeDeltaString(entry.msTotal)
                                + ChatColor.WHITE + ", This Session: "
                                + _DiwUtils.TimeDeltaString_NoDays(msThisSession));
            }
        }
    }

    public static void HandleShutdown() {
        try {

            for (MC_Player p : ServerWrapper.getInstance().getPlayers()) {
                HandlePlayerLogout(p.getName(), p.getUUID());
            }
        } catch (Exception var3) {
        }

        ShuttingDown = true;
    }

    public static long GetTotalOnlineTime(_JOT_OnlineTimeEntry entry) {
        if (entry.msLastLogin > entry.msLastLogout
                && entry.msLastLogin >= _CmdDiw.ServerStartTime) {
            long msNowOnline = System.currentTimeMillis() - entry.msLastLogin;

            return entry.msTotal + msNowOnline;
        } else {
            return entry.msTotal;
        }
    }

    private static void reply(MC_Player sender, String msg) {
        _DiwUtils.reply(sender, msg);
    }

    public static void ShowPlayerLoginTime(MC_Player cs, String pname) {
        if (pname.equalsIgnoreCase("me")) {
            pname = cs.getName();
        }

        UUID uuid1 = _UUIDMapper.getUUID(pname);
        String uuid = uuid1 == null ? null : uuid1.toString();

        if (uuid == null || !Data.playerData.containsKey(uuid)) {
            reply(cs, ChatColor.RED + "No data for: " + ChatColor.YELLOW + pname);
        } else if (!CanSeeHidden(cs) && Data.hidePlayer.containsKey(uuid)) {
            _DiwUtils.ConsoleMsg(
                    ChatColor.LIGHT_PURPLE + "Prevented User "
                            + ChatColor.YELLOW + cs.getName() + ChatColor.LIGHT_PURPLE
                            + " from seeing online time for " + ChatColor.RED
                            + pname);
            reply(cs, ChatColor.RED + "No data for: " + ChatColor.YELLOW + pname);
        } else {
            reply(cs,
                    ChatColor.AQUA
                            + "----------------------------------------------------");
            byte labelLen = 20;

            reply(cs,
                    ChatColor.AQUA
                            + _DiwUtils.TextLabel("Online Time For: ", labelLen)
                            + ChatColor.YELLOW + pname);
            _JOT_OnlineTimeEntry entry = Data.playerData.get(uuid);
            long msTotal = entry.msTotal;

            reply(cs,
                    ChatColor.DARK_AQUA
                            + _DiwUtils.TextLabel("Last Login: ", labelLen)
                            + ChatColor.WHITE
                            + _DiwUtils.GetDateStringFromLong(entry.msLastLogin) + " "
                            + ChatColor.DARK_AQUA
                            + _DiwUtils.GetTimeStringFromLong(entry.msLastLogin));
            reply(cs,
                    ChatColor.DARK_AQUA
                            + _DiwUtils.TextLabel("Last Logout: ", labelLen)
                            + ChatColor.WHITE
                            + _DiwUtils.GetDateStringFromLong(entry.msLastLogout) + " "
                            + ChatColor.DARK_AQUA
                            + _DiwUtils.GetTimeStringFromLong(entry.msLastLogout));
            if (entry.msLastLogin > entry.msLastLogout) {
                long msNowOnline = System.currentTimeMillis()
                        - entry.msLastLogin;

                reply(cs,
                        ChatColor.GREEN
                                + _DiwUtils.TextLabel("This Session: ", labelLen)
                                + ChatColor.DARK_GREEN
                                + _DiwUtils.TimeDeltaString(msNowOnline));
                msTotal += msNowOnline;
            } else {
                reply(cs, ChatColor.RED + "Not Online Currently");
            }

            reply(cs,
                    ChatColor.GREEN
                            + _DiwUtils.TextLabel("Total Online Time: ", labelLen)
                            + ChatColor.GOLD + _DiwUtils.TimeDeltaString(msTotal));
        }
    }

    public static boolean HasAdminPerm(MC_Player cs) {
        return cs == null || cs.isOp();
    }

    public static boolean CanSeeHidden(MC_Player cs) {
        return cs == null || cs.isOp();
    }

    public static void ShowUsage(MC_Player cs) {
        reply(cs,
                "" + ChatColor.AQUA + ChatColor.BOLD
                        + "----- Online Time Options -----");
        reply(cs,
                ChatColor.GOLD + "/jot PlayerName" + ChatColor.WHITE
                        + " -- Show player online time.");
        reply(cs,
                ChatColor.GOLD + "/jot top [Page#]" + ChatColor.WHITE
                        + " -- Show top online time.");
        if (HasAdminPerm(cs)) {
            String pref = ChatColor.RED + "[Admin] ";

            reply(cs,
                    pref + ChatColor.LIGHT_PURPLE + "/jot " + ChatColor.AQUA
                            + "[hide|unhide] " + ChatColor.YELLOW + "PlayerName");
            reply(cs,
                    pref + ChatColor.LIGHT_PURPLE + "/jot " + ChatColor.AQUA
                            + "listhide " + ChatColor.WHITE + " -- List who is hidden.");
        }

        reply(cs,
                "" + ChatColor.DARK_AQUA + ChatColor.ITALIC
                        + "Time measured since " + ChatColor.LIGHT_PURPLE
                        + _DiwUtils.GetDateStringFromLong(Data.msStarted));
    }

    public static void ShowTopPage(MC_Player cs, int page) {
        byte recordsPerPage = 8;
        int idxStart = recordsPerPage * (page - 1);
        int idxEnd = idxStart + recordsPerPage;
        ArrayList<String> names = new ArrayList<String>(Data.playerData.keySet());

        if (!CanSeeHidden(cs)) {
            for (int strPage = names.size() - 1; strPage >= 0; --strPage) {
                if (Data.hidePlayer.containsKey(names.get(strPage))) {
                    names.remove(strPage);
                }
            }
        }

        Collections.sort(names,
                new java.util.Comparator<String>() {
                    public int compare(String name1, String name2) {
                        long total1 = _JOT_OnlineTimeUtils.GetTotalOnlineTime(
                                _JOT_OnlineTimeUtils.Data.playerData.get(
                                        name1));
                        long total2 = _JOT_OnlineTimeUtils.GetTotalOnlineTime(
                                _JOT_OnlineTimeUtils.Data.playerData.get(
                                        name2));

                        return total1 > total2 ? -1 : (total1 < total2 ? 1 : 0);
                    }
                });
        String var13 = "page " + page;

        if (idxEnd >= names.size()) {
            idxEnd = names.size();
            idxStart = idxEnd - recordsPerPage;
            if (idxStart < 0) {
                idxStart = 0;
            }

            var13 = "end of list";
        }

        reply(cs,
                "" + ChatColor.AQUA + ChatColor.BOLD
                        + "----- Top Online Player Time List -----");
        reply(cs,
                ChatColor.GOLD + "Showing " + ChatColor.WHITE + var13
                        + ChatColor.GOLD + ", records " + ChatColor.WHITE
                        + String.format("%d - %d",
                        idxStart + 1,
                        idxEnd));

        for (int i = idxStart; i < idxEnd; ++i) {
            String uuid = names.get(i);
            _JOT_OnlineTimeEntry entry = Data.playerData.get(uuid);
            String name = ServerWrapper.getInstance().getLastKnownPlayerNameFromUUID(uuid);
            MC_Player plr = ServerWrapper.getInstance().getOnlinePlayerByName(name);
            String trailer = "";

            if (plr != null) {
                trailer = ChatColor.GREEN + " Online Now";
            }

            String line = ChatColor.YELLOW
                    + _DiwUtils.TextLabel(
                    String.format("#%d ",
                            i + 1)
                            + name,
                    20)
                    + " "
                    + ChatColor.WHITE
                    + _DiwUtils.TimeDeltaString(
                    GetTotalOnlineTime(entry))
                    + trailer;

            if (!(cs instanceof EntityPlayer)) {
                line = ChatColor.AQUA
                        + String.format(
                        "#%-3d " + ChatColor.YELLOW + "%-16s "
                                + ChatColor.WHITE + "%s",
                        i + 1, name,
                        _DiwUtils.TimeDeltaString(GetTotalOnlineTime(entry)))
                        + trailer;
            }

            reply(cs, line);
        }

        reply(cs,
                "" + ChatColor.DARK_AQUA + ChatColor.ITALIC
                        + "Time measured since " + ChatColor.LIGHT_PURPLE
                        + _DiwUtils.GetDateStringFromLong(Data.msStarted));
    }

    public static void SetPlayerHide(MC_Player cs, String name, boolean flag) {
        String exactName = GetPlayerExactName(name);

        if (exactName == null && flag) {
            reply(cs,
                    ChatColor.LIGHT_PURPLE + "Warning: No online time yet for "
                            + ChatColor.YELLOW + name);
            exactName = name;
        }

        Data.hidePlayer.put(exactName, flag);
        if (!flag) {
            Data.hidePlayer.remove(exactName);
        }

        reply(cs,
                ChatColor.GREEN + "Set \'online time visible\' flag for "
                        + ChatColor.YELLOW + exactName + ChatColor.GREEN + " to "
                        + ChatColor.WHITE + flag);
    }

    public static boolean HandleCommand(MC_Player cs, String[] args) {
        if (HasAdminPerm(cs) && args.length == 1
                && (args[0].equalsIgnoreCase("listhide")
                || args[0].equalsIgnoreCase("hidelist"))) {
            String page1 = _DiwUtils.GetCommaList(Data.hidePlayer.keySet());

            reply(cs,
                    ChatColor.GREEN + "Names Hidden: " + ChatColor.YELLOW
                            + page1);
            return true;
        } else if (HasAdminPerm(cs) && args.length == 1
                && args[0].equalsIgnoreCase("save")) {
            SaveData();
            reply(cs, ChatColor.GREEN + "Data Saved.");
            return true;
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("top")) {
            int page = 1;

            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception var4) {
            }

            if (page < 1) {
                page = 1;
            }

            ShowTopPage(cs, page);
            return true;
        } else if (args.length == 1) {
            ShowPlayerLoginTime(cs, args[0]);
            return true;
        } else if (HasAdminPerm(cs) && args.length == 2
                && args[0].equalsIgnoreCase("hide")) {
            SetPlayerHide(cs, args[1], true);
            return true;
        } else if (HasAdminPerm(cs) && args.length == 2
                && args[0].equalsIgnoreCase("unhide")) {
            SetPlayerHide(cs, args[1], false);
            return true;
        } else {
            ShowUsage(cs);
            return true;
        }
    }
}
