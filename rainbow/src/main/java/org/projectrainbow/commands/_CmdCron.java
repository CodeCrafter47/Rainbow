package org.projectrainbow.commands;

import PluginReference.MC_Player;
import PluginReference.RainbowUtils;
import com.google.common.io.Files;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.projectrainbow._ColorHelper;
import joebkt._CronData;
import org.projectrainbow._DiwUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class _CmdCron extends CommandBase {
    public static Map<String, _CronData> mapData;
    private static String Filename;

    static {
        _CmdCron.mapData = new ConcurrentHashMap<String, _CronData>();
        _CmdCron.Filename = "Cron.dat";
    }

    @Override
    public String getCommandName() {
        return "cron";
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender cs) {
        return (!(cs instanceof MC_Player)) || ((MC_Player) cs).hasPermission("rainbow.cron");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return String.valueOf(_ColorHelper.LIGHT_PURPLE) + "/cron" + _ColorHelper.WHITE + " --- Schedule Commands";
    }

    public static void SaveData() {
        try {
            final long msStart = System.currentTimeMillis();
            final File file = new File(String.valueOf(_DiwUtils.RainbowDataDirectory) + _CmdCron.Filename);
            final FileOutputStream f = new FileOutputStream(file);
            final ObjectOutputStream s = new ObjectOutputStream(new BufferedOutputStream(f));
            s.writeObject(_CmdCron.mapData);
            s.close();
            final long msEnd = System.currentTimeMillis();
            final String msg = String.valueOf(_ColorHelper.YELLOW) + String.format("%-20s: %5d jobs.        Took %3d ms", "Cron Jobs", _CmdCron.mapData.size(), msEnd - msStart);
            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable exc) {
            System.out.println("**********************************************");
            System.out.println("SaveData: " + exc.toString());
            System.out.println("**********************************************");
        }
    }

    public static void LoadData() {
        try {
            final File file = new File(String.valueOf(_DiwUtils.RainbowDataDirectory) + _CmdCron.Filename);
            final File oldFile = new File(_CmdCron.Filename);
            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }
            final FileInputStream f = new FileInputStream(file);
            final ObjectInputStream s = new ObjectInputStream(new BufferedInputStream(f));
            _CmdCron.mapData = (ConcurrentHashMap<String, _CronData>) s.readObject();
            for (final Map.Entry<String, _CronData> entry : _CmdCron.mapData.entrySet()) {
                entry.getValue().msLastRun = System.currentTimeMillis();
            }
            s.close();
        } catch (Throwable exc) {
            System.out.println("Starting New Cron DB: " + _CmdCron.Filename);
            _CmdCron.mapData = new ConcurrentHashMap<String, _CronData>();
        }
    }

    public static void Run500ms() {
        final long msNow = System.currentTimeMillis();
        for (final Map.Entry<String, _CronData> entry : _CmdCron.mapData.entrySet()) {
            final _CronData data = entry.getValue();
            final long delta = msNow - data.msLastRun;
            final ICommandManager exec = _DiwUtils.getMinecraftServer().getCommandManager();
            if (delta > data.msDelay) {
                try {
                    _DiwUtils.ConsoleMsg("[CRON] Job " + data.jobName + ": " + data.cmdToRun);
                    exec.executeCommand(_DiwUtils.getMinecraftServer(), data.cmdToRun);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                data.msLastRun = msNow;
            }
        }
    }

    public void ShowUsage(final ICommandSender cs) {
        _DiwUtils.reply(cs, String.valueOf(_ColorHelper.RED) + "Usage: /cron " + _ColorHelper.AQUA + "add " + _ColorHelper.YELLOW + "MyLoop " + _ColorHelper.WHITE + "60s " + _ColorHelper.GOLD + "/give @a cake");
        _DiwUtils.reply(cs, String.valueOf(_ColorHelper.RED) + "Usage: /cron " + _ColorHelper.AQUA + "delete " + _ColorHelper.YELLOW + "MyLoop");
        _DiwUtils.reply(cs, String.valueOf(_ColorHelper.RED) + "Usage: /cron " + _ColorHelper.AQUA + "setdelay " + _ColorHelper.YELLOW + "MyLoop " + _ColorHelper.WHITE + "5m");
        _DiwUtils.reply(cs, String.valueOf(_ColorHelper.RED) + "Usage: /cron " + _ColorHelper.AQUA + "setcmd " + _ColorHelper.YELLOW + "MyLoop " + _ColorHelper.GOLD + "/give @a cookie");
        _DiwUtils.reply(cs, String.valueOf(_ColorHelper.RED) + "Usage: /cron " + _ColorHelper.AQUA + "list");
    }

    static long GetMSFromString(String strTime) {
        try {
            final long ms = 0L;
            long multi = 1L;
            if (strTime.endsWith("ms")) {
                strTime = strTime.substring(0, strTime.length() - 2);
                multi = 1L;
            } else if (strTime.endsWith("s")) {
                strTime = strTime.substring(0, strTime.length() - 1);
                multi = 1000L;
            } else if (strTime.endsWith("m")) {
                strTime = strTime.substring(0, strTime.length() - 1);
                multi = 60000L;
            } else if (strTime.endsWith("h")) {
                strTime = strTime.substring(0, strTime.length() - 1);
                multi = 3600000L;
            } else if (strTime.endsWith("d")) {
                strTime = strTime.substring(0, strTime.length() - 1);
                multi = 86400000L;
            } else {
                multi = 1000L;
            }
            return multi * Long.parseLong(strTime);
        } catch (Exception ex) {
            return 0L;
        }
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender cs, String[] args) throws CommandException {
        if (args.length >= 1 && args[0].equalsIgnoreCase("add")) {
            if (args.length < 4) {
                _DiwUtils.reply(cs, _ColorHelper.RED + "Usage: /cron add " + _ColorHelper.YELLOW + "MyLoop " + _ColorHelper.WHITE + "60s " + _ColorHelper.GOLD + "/give @a cake");
            } else {
                String jobName = args[1].trim();
                String strTime = args[2].trim();
                String strCmd = _DiwUtils.ConcatArgs(args, 3).trim();
                long ms = GetMSFromString(strTime);
                if (ms > 0L && strCmd.length() > 0) {
                    String key = jobName.toLowerCase();
                    _CronData data = (_CronData) mapData.get(key);
                    if (data == null) {
                        data = new _CronData();
                    }

                    data.jobName = jobName;
                    data.cmdToRun = strCmd;
                    data.msDelay = ms;
                    data.msLastRun = System.currentTimeMillis();
                    mapData.put(key, data);
                    SaveData();
                    _DiwUtils.reply(cs, _ColorHelper.GREEN + "Saved " + _ColorHelper.AQUA + jobName);
                } else {
                    _DiwUtils.reply(cs, _ColorHelper.RED + "Usage: /cron add " + _ColorHelper.YELLOW + "MyLoop " + _ColorHelper.WHITE + "60s " + _ColorHelper.GOLD + "/give @a cake");
                }
            }
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("delete")) {
            if (args.length <= 1) {
                _DiwUtils.reply(cs, _ColorHelper.RED + "Usage: /cron " + _ColorHelper.AQUA + "delete " + _ColorHelper.YELLOW + "MyLoop");
            } else {
                String jobName = args[1].trim();
                String key = jobName.toLowerCase();
                _CronData data = (_CronData) mapData.get(key);
                if (data == null) {
                    _DiwUtils.reply(cs, _ColorHelper.RED + "No job named: " + _ColorHelper.YELLOW + jobName);
                } else {
                    mapData.remove(key);
                    _DiwUtils.reply(cs, _ColorHelper.GREEN + "Deleted job: " + _ColorHelper.YELLOW + jobName);
                    SaveData();
                }
            }
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            if (mapData.size() <= 0) {
                _DiwUtils.reply(cs, _ColorHelper.AQUA + "There are no jobs setup.");
            } else {
                _DiwUtils.reply(cs, _ColorHelper.LIGHT_PURPLE + RainbowUtils.TextLabel("Job Name", 10) + _ColorHelper.LIGHT_PURPLE + " " + RainbowUtils.TextLabel("Interval", 9) + _ColorHelper.LIGHT_PURPLE + " " + RainbowUtils.TextLabel("Time Left", 9) + _ColorHelper.LIGHT_PURPLE + " Command");
                long msNow = System.currentTimeMillis();
                List<String> keysSorted = new ArrayList<String>(mapData.keySet());
                Collections.sort(keysSorted, new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        _CronData data1 = (_CronData) _CmdCron.mapData.get(o1);
                        _CronData data2 = (_CronData) _CmdCron.mapData.get(o2);
                        return data1.msDelay > data2.msDelay ? 1 : (data1.msDelay < data2.msDelay ? -1 : 0);
                    }
                });

                for (String key : keysSorted) {
                    _CronData data = (_CronData) mapData.get(key);
                    String strTime = RainbowUtils.TimeDeltaString_JustMinutesSecs(data.msDelay);
                    long msLeft = data.msDelay - (msNow - data.msLastRun);
                    if (msLeft < 0L) {
                        msLeft = 0L;
                    }

                    String strLeft = RainbowUtils.TimeDeltaString_JustMinutesSecs(msLeft);
                    _DiwUtils.reply(cs, _ColorHelper.YELLOW + RainbowUtils.TextLabel(data.jobName, 10) + " " + _ColorHelper.WHITE + RainbowUtils.TextLabel(strTime, 9) + " " + _ColorHelper.GRAY + RainbowUtils.TextLabel(strLeft, 9) + " " + _ColorHelper.GOLD + data.cmdToRun);
                }

                _DiwUtils.reply(cs, _ColorHelper.AQUA + mapData.size() + " jobs listed.");
            }
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("setdelay")) {
            if (args.length < 3) {
                _DiwUtils.reply(cs, _ColorHelper.RED + "Usage: /cron " + _ColorHelper.AQUA + "setdelay " + _ColorHelper.YELLOW + "MyLoop " + _ColorHelper.WHITE + "5m");
            } else {
                String jobName = args[1].trim();
                String strTime = RainbowUtils.ConcatArgs(args, 2).trim();
                long ms = GetMSFromString(strTime);
                if (ms <= 0L) {
                    _DiwUtils.reply(cs, _ColorHelper.RED + "Usage: /cron " + _ColorHelper.AQUA + "setdelay " + _ColorHelper.YELLOW + "MyLoop " + _ColorHelper.WHITE + "5m");
                } else {
                    String key = jobName.toLowerCase();
                    _CronData data = (_CronData) mapData.get(key);
                    if (data == null) {
                        _DiwUtils.reply(cs, _ColorHelper.RED + "No job named: " + _ColorHelper.YELLOW + jobName);
                    } else {
                        data.msDelay = ms;
                        mapData.put(key, data);
                        SaveData();
                        _DiwUtils.reply(cs, _ColorHelper.GREEN + "Updated job " + _ColorHelper.AQUA + jobName + _ColorHelper.GREEN + " new interval: " + _ColorHelper.WHITE + RainbowUtils.TimeDeltaString_JustMinutesSecs(ms));
                    }
                }
            }
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("setcmd")) {
            if (args.length < 3) {
                _DiwUtils.reply(cs, _ColorHelper.RED + "Usage: /cron " + _ColorHelper.AQUA + "setcmd " + _ColorHelper.YELLOW + "MyLoop " + _ColorHelper.GOLD + "/give @a cookie");
            } else {
                String jobName = args[1].trim();
                String strCmd = _DiwUtils.ConcatArgs(args, 2).trim();
                if (strCmd.length() <= 0) {
                    _DiwUtils.reply(cs, _ColorHelper.RED + "Usage: /cron " + _ColorHelper.AQUA + "setcmd " + _ColorHelper.YELLOW + "MyLoop " + _ColorHelper.GOLD + "/give @a cookie");
                } else {
                    String key = jobName.toLowerCase();
                    _CronData data = (_CronData) mapData.get(key);
                    if (data == null) {
                        _DiwUtils.reply(cs, _ColorHelper.RED + "No job named: " + _ColorHelper.YELLOW + jobName);
                    } else {
                        data.cmdToRun = strCmd;
                        mapData.put(key, data);
                        SaveData();
                        _DiwUtils.reply(cs, _ColorHelper.GREEN + "Updated job " + _ColorHelper.AQUA + jobName + _ColorHelper.GREEN + " new command: " + _ColorHelper.GOLD + strCmd);
                    }
                }
            }
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("advance")) {
            String strTime = RainbowUtils.ConcatArgs(args, 1).trim();
            long ms = GetMSFromString(strTime);
            _DiwUtils.reply(cs, _ColorHelper.GREEN + "Advancing clock: " + _ColorHelper.WHITE + _DiwUtils.TimeDeltaString_NoDays(ms));

            for (String key : mapData.keySet()) {
                _CronData data = (_CronData) mapData.get(key);
                data.msLastRun -= ms;
            }

        } else {
            this.ShowUsage(cs);
        }
    }
}
