package org.projectrainbow;

import PluginReference.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import joebkt._SerializableLocation;
import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.*;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.projectrainbow.commands.*;
import org.projectrainbow.interfaces.IMixinICommandSender;
import org.projectrainbow.plugins.PluginManager;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class _DiwUtils {
    public static boolean ArmorStand_DanceEverywhere = false;
    public static boolean DoJanitor = true;
    public static String CustomShutdownMessage = null;
    public static boolean DoHideAnnoyingDefaultServerOutput = true;
    public static int AutoSaveMinutes = 31;
    public static int JanitorInterval = 900;
    public static int JanitorWarnSecs = 30;
    public static String MC_VERSION_STRING;
    public static String DiwModVersion = "112.1";
    public static double DiwModVersionNumeric = Double.parseDouble(DiwModVersion);
    public static String MC_SERVER_MOD_NAME = "Rainbow";
    public static String RainbowDataDirectory = "RainbowData" + File.separator;
    public static long ServerStartTime = 0L;
    public static String DefaultMOTD = "§c§lA§6§l §e§lR§a§la§b§li§d§ln§c§lb§6§lo§e§lw§a§l §b§lS§d§le§c§lr§6§lv§e§le§a§lr\n§6%s Fully Supported!";
    public static SimpleDateFormat shortDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    public static boolean g_didLoadDataFiles = false;
    public static ConcurrentHashMap<String, Long> tooSoon = new ConcurrentHashMap<>();
    public static MinecraftServer minecraftServer;
    public static PluginManager pluginManager = new PluginManager();
    public static boolean BlockFlowOn = true;
    public static boolean DoCensor = true;
    public static boolean NotifyAdminCensor = true;
    public static double PayDayAmount = 200.0D;
    public static boolean DoSpamKick = true;
    public static boolean DoWelcomeNewPlayers = true;
    public static String RainbowPropertiesFilename = "Rainbow.properties";
    public static boolean BungeeCord = false;
    public static boolean DoPaydays = true;
    public static boolean DoWeather = true;
    public static boolean DoReconnectDelay = true;
    public static boolean DoSavingWorldNotice = true;
    public static int PayDayMinutes = 30;
    public static int janitorInterval = 900;
    public static int ReconnectDelaySeconds = 3;
    public static boolean ArmorStandsDance = true;
    public static boolean OpsKeepInventory = true;
    public static int MaxNearbyEntities = 250;
    public static long g_standFunIdx = 0L;
    public static List<String> g_removedCommand = new LinkedList<>();
    public static boolean UpdateNameColorOnTab = false;
    public static double netherDistanceRatio = 8.0D;
    public static long g_restartCountdown = -1L;

    public static MinecraftServer getMinecraftServer() {
        return minecraftServer;
    }

    public static void DebugSleepNotice(String msg, final long msDelay) {
        try {
            System.out.println("-----------------------------------------");
            if (!msg.contains("JKC DEBUG")) {
                msg = "JKC DEBUG: " + msg;
            }
            System.out.println(msg);
            System.out.println("-----------------------------------------");
            Thread.sleep(msDelay);
        } catch (Exception ignored) {
        }
    }

    public static void ConsoleMsg(final String msg) {
        System.out.println("[R]: " + _ColorHelper.stripColor(msg));
    }

    public static boolean IsOp(final ICommandSender cs) {
        if (cs instanceof EntityPlayer) {
            final EntityPlayer plr = (EntityPlayer) cs;
            return getMinecraftServer().getPlayerList().canSendCommands(plr.getGameProfile());
        }
        return true;
    }

    public static void reply(final MC_Player cs, final String msg) {
        if (cs != null && cs instanceof EntityPlayer) {
            final EntityPlayer p = (EntityPlayer) cs;
            ((IMixinICommandSender) p).sendMessage(msg);
            return;
        }
        System.out.println(_ColorHelper.stripColor(msg));
    }

    public static void reply(@Nullable ICommandSender cs, String msg) {
        if (cs instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) cs;
            ((IMixinICommandSender) p).sendMessage(msg);
            return;
        }
        System.out.println(_ColorHelper.stripColor(msg));
    }

    public static void SaveStuffs() {
        if (!g_didLoadDataFiles) {
            System.out.println("* Skipping data save since it was never loaded.");
        } else {
            try {
                _CmdNameColor.SaveData();
            } catch (Exception var16) {
                var16.printStackTrace();
            }

            try {
                _DynReward.SaveRewards();
            } catch (Exception var15) {
                var15.printStackTrace();
            }

            try {
                _HomeUtils.SaveHomes();
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            try {
                _HomeUtils.SaveHomes2();
            } catch (Exception var13) {
                var13.printStackTrace();
            }

            try {
                _EventManager.SaveEventCounts();
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            try {
                _JOT_OnlineTimeUtils.SaveData();
            } catch (Exception var11) {
                var11.printStackTrace();
            }

            try {
                _WarpManager.SaveWarps();
            } catch (Exception var9) {
                var9.printStackTrace();
            }

            try {
                _EconomyManager.SaveEconomy();
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            try {
                _UUIDMapper.SaveData();
            } catch (Exception var7) {
                var7.printStackTrace();
            }
            try {
                _CmdIgnore.SaveData();
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            try {
                _PermMgr.SaveData();
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            try {
                WriteRainbowProperties();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            try {
                _CmdCron.SaveData();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }
    }

    public static void EnsureDirectory(final String dirName) {
        final File pDir = new File(dirName);
        if (pDir.isDirectory()) {
            return;
        }
        try {
            System.out.println("Creating directory: " + dirName);
            pDir.mkdir();
        } catch (Throwable exc) {
            System.out.println("EnsureDirectory " + dirName + ": " + exc.toString());
        }
    }

    public static void Startup() {
        _DiwUtils.MC_VERSION_STRING = getMinecraftServer().getMinecraftVersion();
        _DiwUtils.DefaultMOTD = String.format(DefaultMOTD, _DiwUtils.MC_VERSION_STRING);

        // Setup BlockHelper
        ImmutableMap.Builder<Integer, String> mapBlockNames = ImmutableMap.builder();
        for (Block block : Block.REGISTRY) {
            mapBlockNames.put(Block.getIdFromBlock(block), Block.REGISTRY.getNameForObject(block).getResourcePath());
        }
        BlockHelper.mapBlockNames = mapBlockNames.build();

        ImmutableMap.Builder<String, String> mapItemNames = ImmutableMap.builder();
        ImmutableMap.Builder<Integer, Integer> mapNumSubtypes = ImmutableMap.builder();
        Set<String> subtypes = new HashSet<>();
        for (Item item : Item.REGISTRY) {
            int id = Item.getIdFromItem(item);
            if (item.getHasSubtypes()) {
                for (int i = 0; i < 16; i++) {
                    if (id == 162 && i > 1) {
                        continue;
                    }
                    ItemStack itemStack = new ItemStack(item, 1, i);
                    String localizedName = I18n.translateToLocal(item.getItemStackDisplayName(itemStack));
                    if (!subtypes.contains(localizedName)) {
                        mapItemNames.put("" + id + ":" + i, localizedName);
                        subtypes.add(localizedName);
                    }
                }
                mapNumSubtypes.put(id, subtypes.size());
                subtypes.clear();
            } else {
                String localizedName = item.getItemStackDisplayName(new ItemStack(item));
                mapItemNames.put("" + id + ":0", localizedName);
                mapNumSubtypes.put(id, 1);
            }
        }
        BlockHelper.mapItemNames = mapItemNames.build();
        BlockHelper.mapNumSubtypes = mapNumSubtypes.build();

        System.out.println("\nInitializing 'Baked In' server goodies...");
        System.out.println("---------------------------------------------------------------");
        RainbowUtils.setServer(ServerWrapper.getInstance());
        EnsureDirectory(_DiwUtils.RainbowDataDirectory);
        EnsureDirectory("Backpacks");
        EnsureDirectory(RainbowDataDirectory + "Scripts");
        _CmdNameColor.LoadData();
        _HomeUtils.LoadHomes();
        _HomeUtils.LoadHomes2();
        _CmdCron.LoadData();
        _UUIDMapper.LoadData();
        _JOT_OnlineTimeUtils.LoadData();
        _EventManager.LoadEventCounts();
        _DynReward.LoadRewards();
        _Announcer.LoadAnnouncements();
        _EmoteUtils.LoadEmotes();
        _WarpManager.LoadWarps();
        _EconomyManager.LoadEconomy();
        _CmdIgnore.LoadData();
        _PermMgr.LoadData();
        _DiwUtils.g_didLoadDataFiles = true;

        File servProp = new File(RainbowPropertiesFilename);

        if (!servProp.exists()) {
            WriteRainbowProperties();
        } else {
            LoadRainbowProperties();
        }

        // register commands
        ServerWrapper.getInstance().registerCommand(new _CmdNameColor());
        ServerWrapper.getInstance().registerCommand(new _CmdCron());
        ServerWrapper.getInstance().registerCommand(new _CmdVer());
        ServerWrapper.getInstance().registerCommand(new _CmdDiw());
        ServerWrapper.getInstance().registerCommand(new _CmdSuicide());
        ServerWrapper.getInstance().registerCommand(new _CmdHome());
        ServerWrapper.getInstance().registerCommand(new _CmdSetHome());
        ServerWrapper.getInstance().registerCommand(new _CmdSpawn());
        ServerWrapper.getInstance().registerCommand(new _CmdBp());
        ServerWrapper.getInstance().registerCommand(new _CmdAnnouncer());
        ServerWrapper.getInstance().registerCommand(new _CmdPay());
        ServerWrapper.getInstance().registerCommand(new _CmdBal());
        ServerWrapper.getInstance().registerCommand(new _CmdColors());
        ServerWrapper.getInstance().registerCommand(new _CmdDelWarp());
        ServerWrapper.getInstance().registerCommand(new _CmdEcon());
        ServerWrapper.getInstance().registerCommand(new _CmdEnderchest());
        ServerWrapper.getInstance().registerCommand(new _CmdHome2());
        ServerWrapper.getInstance().registerCommand(new _CmdIgnore());
        ServerWrapper.getInstance().registerCommand(new _CmdJEmote());
        ServerWrapper.getInstance().registerCommand(new _CmdJot());
        ServerWrapper.getInstance().registerCommand(new _CmdTpaHere());
        ServerWrapper.getInstance().registerCommand(new _CmdPayday());
        ServerWrapper.getInstance().registerCommand(new _CmdPerm());
        ServerWrapper.getInstance().registerCommand(new _CmdPlugins());
        ServerWrapper.getInstance().registerCommand(new _CmdReward());
        ServerWrapper.getInstance().registerCommand(new _CmdRide());
        ServerWrapper.getInstance().registerCommand(new _CmdSell());
        ServerWrapper.getInstance().registerCommand(new _CmdSetHome2());
        ServerWrapper.getInstance().registerCommand(new _CmdSetWarp());
        ServerWrapper.getInstance().registerCommand(new _CmdSetWorth());
        ServerWrapper.getInstance().registerCommand(new _CmdThrow());
        ServerWrapper.getInstance().registerCommand(new _CmdTpAccept());
        ServerWrapper.getInstance().registerCommand(new _CmdWarp());
        ServerWrapper.getInstance().registerCommand(new _CmdWorth());

        ReadRestrictedCommands();
        Map<String, ICommand> commandMap = getMinecraftServer().getCommandManager().getCommands();
        for (String cmd : g_removedCommand) {
            ICommand command = commandMap.get(cmd);
            if (command != null) {
                commandMap.remove(command.getName());
                for (String alias : command.getAliases()) {
                    commandMap.remove(alias);
                }
            }
        }

        // load plugins
        pluginManager.enable();

        try {
            if (Boolean.valueOf(System.getProperty("rainbow.forceMixins", "false"))) {
                System.out.println("Force loading mixins");
                for (URL url : Launch.classLoader.getSources()) {
                    try {
                        URI uri = url.toURI();
                        if (!"file".equals(uri.getScheme()) || !new File(uri).exists()) {
                            continue;
                        }
                        JarFile jarFile = new JarFile(new File(uri));
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry jarEntry = entries.nextElement();
                            String name = jarEntry.getName();
                            if (name.endsWith(".class")) {
                                String className = name.replace(".class", "").replace('/', '.');
                                if (className.startsWith("net.minecraft.src") || !className.contains(".")) {
                                    try {
                                        Class.forName(className);
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void Shutdown() {
        Hooks.onShutdown();
        _JOT_OnlineTimeUtils.HandleShutdown();
        SaveStuffs();
    }

    public static void ReadRestrictedCommands() {
        EnsureDirectory(RainbowDataDirectory);
        String oldFilename = "RemoveCommands.txt";
        String newFilename = RainbowDataDirectory + oldFilename;

        try {
            File strRestricted = new File(oldFilename);

            if (strRestricted.exists()) {
                Files.move(strRestricted, new File(newFilename));
            }

            if ((new File(newFilename)).exists()) {
                BufferedReader br = new BufferedReader(
                        new FileReader(newFilename));

                String line;

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.length() >= 2 && !line.startsWith(";")) {
                        if (line.startsWith("/")) {
                            line = line.substring(1);
                        }

                        g_removedCommand.add(line.toLowerCase());
                    }
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        if (g_removedCommand.size() > 0) {
            String strRestricted1 = GetCommaList(g_removedCommand);

            System.out.println("Restricting Commands: " + strRestricted1);
        }
    }

    public static void LoadRainbowProperties() {
        try {
            File exc = new File(RainbowPropertiesFilename);
            BufferedReader br = new BufferedReader(new FileReader(exc));

            String line;

            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    line = line.trim();
                    String lwr = line.toLowerCase();

                    if (!lwr.startsWith(";")) {
                        String[] sides = getEquationSides(line);

                        if (sides[0].equalsIgnoreCase("bungeecord")) {
                            BungeeCord = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("censor")) {
                            DoCensor = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("paydays")) {
                            DoPaydays = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("janitor")) {
                            DoJanitor = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("welcome_new_players")) {
                            DoWelcomeNewPlayers = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("announcer")) {
                            _Announcer.IsEnabled = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("weather")) {
                            DoWeather = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("spamkick")) {
                            DoSpamKick = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("reconnect_delay")) {
                            DoReconnectDelay = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("autosave_notice")) {
                            DoSavingWorldNotice = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("autosave_minutes")) {
                            AutoSaveMinutes = getIntegerWithDefault(sides[1], 1,
                                    61);
                        }

                        if (sides[0].equalsIgnoreCase("payday_minutes")) {
                            PayDayMinutes = getIntegerWithDefault(sides[1], 1,
                                    61);
                        }

                        if (sides[0].equalsIgnoreCase("janitor_seconds")) {
                            janitorInterval = getIntegerWithDefault(
                                    sides[1], 1, 900);
                        }

                        if (sides[0].equalsIgnoreCase("reconnect_delay_seconds")) {
                            ReconnectDelaySeconds = getIntegerWithDefault(
                                    sides[1], 1, 3);
                        }

                        if (sides[0].equalsIgnoreCase("armor_stands_dance")) {
                            ArmorStandsDance = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("ops_keep_inventory")) {
                            OpsKeepInventory = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("notify_admins_censor")) {
                            NotifyAdminCensor = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("max_nearby_entities")) {
                            MaxNearbyEntities = getIntegerWithDefault(sides[1],
                                    1, 1000);
                        }

                        if (sides[0].equalsIgnoreCase("payday_amount")) {
                            try {
                                String exc1 = sides[1];

                                exc1 = StringReplace(exc1, ",", ".");
                                PayDayAmount = Double.parseDouble(exc1);
                            } catch (Exception var6) {
                                PayDayAmount = -1.0D;
                            }

                            if (PayDayAmount <= 0.0D) {
                                System.out.println(
                                        "INVALID payday_amount setting: " + line);
                                PayDayAmount = 200.0D;
                                System.out.println(
                                        String.format("* Will use default: %.2f", PayDayAmount));
                            }
                        }

                        if (sides[0].equalsIgnoreCase("update_namecolor_on_tab")) {
                            UpdateNameColorOnTab = isStringTrue(sides[1]);
                        }

                        if (sides[0].equalsIgnoreCase("nether_distance_ratio")) {
                            netherDistanceRatio = getDoubleWithDefault(sides[1], 0.001, 8.0D);
                        }
                    }
                }
            }

            br.close();
        } catch (Exception var7) {
            ConsoleMsg("Rainbow Properties: " + var7.toString());
        }

    }

    public static void WriteRainbowProperties() {
        try {
            File exc = new File(RainbowPropertiesFilename);
            BufferedWriter bw = new BufferedWriter(new FileWriter(exc));
            String fmt = "%-23s = %s\r\n";

            bw.write(String.format(fmt, new Object[]{"Censor", "" + DoCensor}));
            bw.write(
                    String.format(fmt, new Object[]{"Paydays", "" + DoPaydays}));
            bw.write(
                    String.format(fmt, new Object[]{"Janitor", "" + DoJanitor}));
            bw.write(
                    String.format(fmt, new Object[]{"Weather", "" + DoWeather}));
            bw.write(
                    String.format(fmt,
                            new Object[]{"SpamKick", "" + DoSpamKick}));
            bw.write(
                    String.format(fmt,
                            new Object[]{"Announcer", "" + _Announcer.IsEnabled}));
            bw.write(
                    String.format(fmt,
                            new Object[]{"autosave_minutes", "" + AutoSaveMinutes}));
            bw.write(
                    String.format(fmt,
                            new Object[]{"autosave_notice", "" + DoSavingWorldNotice}));
            bw.write(
                    String.format(fmt,
                            new Object[]{"payday_minutes", "" + PayDayMinutes}));
            bw.write(
                    String.format(fmt,
                            new Object[]{
                                    "payday_amount",
                                    String.format("%.2f", PayDayAmount)}));
            bw.write(
                    String.format(fmt,
                            new Object[]{
                                    "janitor_seconds",
                                    "" + janitorInterval}));
            bw.write(
                    String.format(fmt,
                            new Object[]{"reconnect_delay", "" + DoReconnectDelay}));
            bw.write(
                    String.format(fmt,
                            new Object[]{
                                    "reconnect_delay_seconds",
                                    "" + ReconnectDelaySeconds}));
            bw.write(
                    String.format(fmt,
                            new Object[]{
                                    "welcome_new_players",
                                    "" + DoWelcomeNewPlayers}));
            bw.write(
                    String.format(fmt,
                            new Object[]{"armor_stands_dance", "" + ArmorStandsDance}));
            bw.write(
                    String.format(fmt,
                            new Object[]{"ops_keep_inventory", "" + OpsKeepInventory}));
            bw.write(
                    String.format(fmt,
                            new Object[]{
                                    "notify_admins_censor",
                                    "" + NotifyAdminCensor}));
            bw.write(
                    String.format(fmt,
                            new Object[]{
                                    "max_nearby_entities", "" + MaxNearbyEntities}));
            bw.write(String.format(fmt, new Object[]{"bungeecord", BungeeCord}));
            bw.write(String.format(fmt, "update_namecolor_on_tab", UpdateNameColorOnTab));
            bw.write(String.format(fmt, "nether_distance_ratio", netherDistanceRatio));
            bw.close();
        } catch (Exception var3) {
            ConsoleMsg("Failed to save: " + RainbowPropertiesFilename);
        }

    }

    public static boolean isStringTrue(String str) {
        return str.equalsIgnoreCase("yes")
                ? true
                : (str.equalsIgnoreCase("1")
                ? true
                : str.equalsIgnoreCase("true"));
    }

    public static int getIntegerWithDefault(String str, int minValidValue, int argDefault) {
        int res = minValidValue - 1;

        try {
            res = Integer.parseInt(str);
        } catch (Exception ignored) {
        }

        if (res < minValidValue) {
            res = argDefault;
        }

        return res;
    }

    public static double getDoubleWithDefault(String str, double minValidValue, double argDefault) {
        double res;

        try {
            res = Double.parseDouble(str);
        } catch (Exception var5) {
            return argDefault;
        }

        if (res < minValidValue) {
            res = argDefault;
        }

        return res;
    }

    public static String[] getEquationSides(String equation) {
        String[] pieces = new String[]{"", ""};

        if (equation == null) {
            return pieces;
        } else {
            int idx = equation.indexOf(61);

            if (idx <= 0) {
                return pieces;
            } else {
                pieces[0] = equation.substring(0, idx - 1).trim();
                pieces[1] = equation.substring(idx + 1).trim();
                return pieces;
            }
        }
    }

    public static String GetDateStringFromLong(final long dt) {
        return _DiwUtils.shortDateFormat.format(dt);
    }

    public static String ConcatArgs(final String[] args, final int startIdx) {
        if (args == null) {
            return "";
        }
        if (args.length <= 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = startIdx; i < args.length; ++i) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(args[i]);
        }
        return sb.toString();
    }

    public static String TranslateChatString(final String parm, final boolean IsOp) {
        if (parm == null) {
            return "";
        }
        if (parm.equals("&")) {
            return "&";
        }
        final StringBuilder res = new StringBuilder();
        boolean pending = false;
        for (int i = 0; i < parm.length(); ++i) {
            final char ch = parm.charAt(i);
            if (!pending && ch == '&') {
                pending = true;
            } else if (pending) {
                pending = false;
                if (ch == '0' && IsOp) {
                    res.append(String.valueOf(_ColorHelper.BLACK));
                } else if (ch == '1') {
                    res.append(String.valueOf(_ColorHelper.DARK_BLUE));
                } else if (ch == '2') {
                    res.append(String.valueOf(_ColorHelper.DARK_GREEN));
                } else if (ch == '3') {
                    res.append(String.valueOf(_ColorHelper.DARK_AQUA));
                } else if (ch == '4' && IsOp) {
                    res.append(String.valueOf(_ColorHelper.DARK_RED));
                } else if (ch == '5') {
                    res.append(String.valueOf(_ColorHelper.DARK_PURPLE));
                } else if (ch == '6') {
                    res.append(String.valueOf(_ColorHelper.GOLD));
                } else if (ch == '7') {
                    res.append(String.valueOf(_ColorHelper.GRAY));
                } else if (ch == '8') {
                    res.append(String.valueOf(_ColorHelper.DARK_GRAY));
                } else if (ch == '9') {
                    res.append(String.valueOf(_ColorHelper.BLUE));
                } else if (ch == 'a') {
                    res.append(String.valueOf(_ColorHelper.GREEN));
                } else if (ch == 'b') {
                    res.append(String.valueOf(_ColorHelper.AQUA));
                } else if (ch == 'c' && IsOp) {
                    res.append(String.valueOf(_ColorHelper.RED));
                } else if (ch == 'd') {
                    res.append(String.valueOf(_ColorHelper.LIGHT_PURPLE));
                } else if (ch == 'e') {
                    res.append(String.valueOf(_ColorHelper.YELLOW));
                } else if (ch == 'f') {
                    res.append(String.valueOf(_ColorHelper.WHITE));
                } else if (ch == 'l') {
                    res.append(String.valueOf(_ColorHelper.BOLD));
                } else if (ch == 'm') {
                    res.append(String.valueOf(_ColorHelper.STRIKETHROUGH));
                } else if (ch == 'k' && IsOp) {
                    res.append(String.valueOf(_ColorHelper.MAGIC));
                } else if (ch == 'n' && IsOp) {
                    res.append(String.valueOf(_ColorHelper.UNDERLINE));
                } else if (ch == 'o') {
                    res.append(String.valueOf(_ColorHelper.ITALIC));
                } else if (ch == 'r') {
                    res.append(String.valueOf(_ColorHelper.RESET));
                } else {
                    res.append("&");
                    if (ch != '&') {
                        res.append(ch);
                    }
                }
            } else {
                res.append(ch);
            }
        }
        return res.toString();
    }

    public static String FullTranslate(String parm) {
        parm = SpecialTranslate(parm);
        parm = TranslateColorString(parm, true);
        return parm;
    }

    public static String TranslateColorString(final String parm, final boolean IsOp) {
        return TranslateColorString(parm, IsOp, false);
    }

    public static String TranslateColorString(final String parm, final boolean IsOp, final boolean fAllowSpaces) {
        final StringBuilder res = new StringBuilder();
        boolean pending = false;
        for (int i = 0; i < parm.length(); ++i) {
            final char ch = parm.charAt(i);
            if (!pending && ch == '&') {
                pending = true;
            } else if (pending) {
                pending = false;
                if (ch == '0' && IsOp) {
                    res.append(String.valueOf(_ColorHelper.BLACK));
                } else if (ch == '1') {
                    res.append(String.valueOf(_ColorHelper.DARK_BLUE));
                } else if (ch == '2') {
                    res.append(String.valueOf(_ColorHelper.DARK_GREEN));
                } else if (ch == '3') {
                    res.append(String.valueOf(_ColorHelper.DARK_AQUA));
                } else if (ch == '4' && IsOp) {
                    res.append(String.valueOf(_ColorHelper.DARK_RED));
                } else if (ch == '5') {
                    res.append(String.valueOf(_ColorHelper.DARK_PURPLE));
                } else if (ch == '6') {
                    res.append(String.valueOf(_ColorHelper.GOLD));
                } else if (ch == '7') {
                    res.append(String.valueOf(_ColorHelper.GRAY));
                } else if (ch == '8') {
                    res.append(String.valueOf(_ColorHelper.DARK_GRAY));
                } else if (ch == '9') {
                    res.append(String.valueOf(_ColorHelper.BLUE));
                } else if (ch == 'a') {
                    res.append(String.valueOf(_ColorHelper.GREEN));
                } else if (ch == 'b') {
                    res.append(String.valueOf(_ColorHelper.AQUA));
                } else if (ch == 'c' && IsOp) {
                    res.append(String.valueOf(_ColorHelper.RED));
                } else if (ch == 'd') {
                    res.append(String.valueOf(_ColorHelper.LIGHT_PURPLE));
                } else if (ch == 'e') {
                    res.append(String.valueOf(_ColorHelper.YELLOW));
                } else if (ch == 'f') {
                    res.append(String.valueOf(_ColorHelper.WHITE));
                } else if (ch == 'l') {
                    res.append(String.valueOf(_ColorHelper.BOLD));
                } else if (ch == 'm') {
                    res.append(String.valueOf(_ColorHelper.STRIKETHROUGH));
                } else if (ch == 'k' && IsOp) {
                    res.append(String.valueOf(_ColorHelper.MAGIC));
                } else if (ch == 'n' && IsOp) {
                    res.append(String.valueOf(_ColorHelper.UNDERLINE));
                } else if (ch == 'o') {
                    res.append(String.valueOf(_ColorHelper.ITALIC));
                } else if (ch == 'r') {
                    res.append(String.valueOf(_ColorHelper.RESET));
                } else if (ch == '&') {
                    res.append("&");
                }
            } else if (IsOp) {
                res.append(ch);
            } else if (IsCharLetterOrDigit(ch)) {
                res.append(ch);
            } else if (fAllowSpaces && ch == ' ') {
                res.append(ch);
            }
        }
        return res.toString();
    }

    public static boolean IsCharLetterOrDigit(final char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9');
    }

    public static String StringReplace(final String src, final String key, final String val) {
        final int idx = src.indexOf(key);
        if (idx < 0) {
            return src;
        }
        return String.valueOf(src.substring(0, idx)) + val + src.substring(idx + key.length());
    }

    public static String SpecialTranslate(final String txt) {
        String res;
        for (res = txt; res.indexOf("{star1}") >= 0; res = StringReplace(res, "{star1}", "\u269d")) {
        }
        while (res.indexOf("{star2}") >= 0) {
            res = StringReplace(res, "{star2}", "\u2605");
        }
        while (res.indexOf("{star3}") >= 0) {
            res = StringReplace(res, "{star3}", "\u2606");
        }
        while (res.indexOf("{space}") >= 0) {
            res = StringReplace(res, "{space}", " ");
        }
        while (res.indexOf("{_}") >= 0) {
            res = StringReplace(res, "{_}", " ");
        }
        while (res.indexOf("{heart1}") >= 0) {
            res = StringReplace(res, "{heart1}", "\u2764");
        }
        while (res.indexOf("{heart2}") >= 0) {
            res = StringReplace(res, "{heart2}", "\u2661");
        }
        while (res.indexOf("{heart3}") >= 0) {
            res = StringReplace(res, "{heart3}", "\u2665");
        }
        while (res.indexOf("{cross1}") >= 0) {
            res = StringReplace(res, "{cross1}", "\u271e");
        }
        while (res.indexOf("{cross2}") >= 0) {
            res = StringReplace(res, "{cross2}", "\u2671");
        }
        while (res.indexOf("{cross3}") >= 0) {
            res = StringReplace(res, "{cross3}", "\u2670");
        }
        while (res.indexOf("{diamond1}") >= 0) {
            res = StringReplace(res, "{diamond1}", "\u2666");
        }
        while (res.indexOf("{diamond2}") >= 0) {
            res = StringReplace(res, "{diamond2}", "\u2662");
        }
        while (res.indexOf("{radio}") >= 0) {
            res = StringReplace(res, "{radio}", "\u2622");
        }
        while (res.indexOf("{bio}") >= 0) {
            res = StringReplace(res, "{bio}", "\u2623");
        }
        while (res.indexOf("{ankh}") >= 0) {
            res = StringReplace(res, "{ankh}", "\u2625");
        }
        while (res.indexOf("{peace}") >= 0) {
            res = StringReplace(res, "{peace}", "\u262e");
        }
        while (res.indexOf("{yinyang}") >= 0) {
            res = StringReplace(res, "{yinyang}", "\u262f");
        }
        while (res.indexOf("{male}") >= 0) {
            res = StringReplace(res, "{male}", "\u2642");
        }
        while (res.indexOf("{female}") >= 0) {
            res = StringReplace(res, "{female}", "\u2640");
        }
        while (res.indexOf("{aquarius}") >= 0) {
            res = StringReplace(res, "{aquarius}", "\u2652");
        }
        while (res.indexOf("{music1}") >= 0) {
            res = StringReplace(res, "{music1}", "\u2669");
        }
        while (res.indexOf("{music2}") >= 0) {
            res = StringReplace(res, "{music2}", "\u266a");
        }
        while (res.indexOf("{music3}") >= 0) {
            res = StringReplace(res, "{music3}", "\u266b");
        }
        while (res.indexOf("{music4}") >= 0) {
            res = StringReplace(res, "{music4}", "\u266c");
        }
        while (res.indexOf("{music5}") >= 0) {
            res = StringReplace(res, "{music5}", "\u266d");
        }
        while (res.indexOf("{anchor}") >= 0) {
            res = StringReplace(res, "{anchor}", "\u2693");
        }
        while (res.indexOf("{atom}") >= 0) {
            res = StringReplace(res, "{atom}", "\u269b");
        }
        while (res.indexOf("{bolt}") >= 0) {
            res = StringReplace(res, "{bolt}", "\u26a1");
        }
        while (res.indexOf("{plane}") >= 0) {
            res = StringReplace(res, "{plane}", "\u2708");
        }
        while (res.indexOf("{flower1}") >= 0) {
            res = StringReplace(res, "{flower1}", "\u2740");
        }
        while (res.indexOf("{flower2}") >= 0) {
            res = StringReplace(res, "{flower2}", "\u2743");
        }
        while (res.indexOf("{flower3}") >= 0) {
            res = StringReplace(res, "{flower3}", "\u273c");
        }
        while (res.indexOf("{newline}") >= 0) {
            res = StringReplace(res, "{newline}", "\n");
        }
        while (res.indexOf("{fingers}") >= 0) {
            res = StringReplace(res, "{fingers}", "\u270c");
        }
        while (res.indexOf("{coffee}") >= 0) {
            res = StringReplace(res, "{coffee}", "\u2615");
        }
        while (res.indexOf("{shamrock}") >= 0) {
            res = StringReplace(res, "{shamrock}", "\u2618");
        }
        while (res.indexOf("{doctor}") >= 0) {
            res = StringReplace(res, "{doctor}", "\u2624");
        }
        while (res.indexOf("{swords}") >= 0) {
            res = StringReplace(res, "{swords}", "\u2694");
        }
        while (res.indexOf("{hermes}") >= 0) {
            res = StringReplace(res, "{hermes}", "\u269a");
        }
        while (res.indexOf("{heaven}") >= 0) {
            res = StringReplace(res, "{heaven}", "\u2630");
        }
        while (res.indexOf("{earth}") >= 0) {
            res = StringReplace(res, "{earth}", "\u2637");
        }
        while (res.indexOf("{handicap}") >= 0) {
            res = StringReplace(res, "{handicap}", "\u267f");
        }
        while (res.indexOf("{ussr}") >= 0) {
            res = StringReplace(res, "{ussr}", "\u262d");
        }
        while (res.indexOf("{storm}") >= 0) {
            res = StringReplace(res, "{storm}", "\u2608");
        }
        while (res.indexOf("{sun}") >= 0) {
            res = StringReplace(res, "{sun}", "\u2609");
        }
        while (res.indexOf("{sad}") >= 0) {
            res = StringReplace(res, "{sad}", "\u2639");
        }
        while (res.indexOf("{phone}") >= 0) {
            res = StringReplace(res, "{phone}", "\u260e");
        }
        while (res.indexOf("{whiteflag}") >= 0) {
            res = StringReplace(res, "{whiteflag}", "\u2690");
        }
        while (res.indexOf("{blackflag}") >= 0) {
            res = StringReplace(res, "{blackflag}", "\u2691");
        }
        while (res.indexOf("{farsi}") >= 0) {
            res = StringReplace(res, "{farsi}", "\u262b");
        }
        while (res.indexOf("{khanda}") >= 0) {
            res = StringReplace(res, "{khanda}", "\u262c");
        }
        return res;
    }

    public static String ColorWrapFix(final String argMsg, final boolean handlePercents) {
        if (argMsg == null) {
            return "";
        }
        if (argMsg.length() <= 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        String lastColorString = "";
        boolean lastCharWasSpace = false;
        for (int len = argMsg.length(), i = 0; i < len; ++i) {
            final char ch = argMsg.charAt(i);
            if (ch != ' ' && ch != _ColorHelper.COLOR_CHAR && lastCharWasSpace) {
                sb.append(lastColorString);
            }
            sb.append(ch);
            lastCharWasSpace = (ch == ' ');
            if (i < len - 1 && ch == _ColorHelper.COLOR_CHAR) {
                lastColorString = String.valueOf(_ColorHelper.COLOR_CHAR) + argMsg.charAt(i + 1);
            }
            if (handlePercents && ch == '%') {
                sb.append("%").append(lastColorString);
            }
        }
        return sb.toString();
    }

    public static boolean TooSoon(final ICommandSender cs, final String what, final int seconds) {
        if (!(cs instanceof EntityPlayer)) {
            return true;
        }
        final EntityPlayer p = (EntityPlayer) cs;
        if (IsOp(p)) {
            return false;
        }
        final String key = String.valueOf(what) + "." + p.getName();
        final Long msBefore = _DiwUtils.tooSoon.get(key);
        final Long curMS = System.currentTimeMillis();
        if (msBefore != null) {
            final Long msDelta = curMS - msBefore;
            final Long msWaitTime = 1000L * seconds;
            if (msDelta < msWaitTime) {
                reply(p, String.valueOf(_ColorHelper.RED) + "[" + what + "] Too soon, you must wait: " + _ColorHelper.AQUA + RainbowUtils.TimeDeltaString_JustMinutesSecs(msWaitTime - msDelta));
                return true;
            }
        }
        _DiwUtils.tooSoon.put(key, curMS);
        return false;
    }

    public static String TimeDeltaString(long ms) {
        int secs = (int) (ms / 1000L % 60L);
        int mins = (int) (ms / 1000L / 60L % 60L);
        int hours = (int) (ms / 1000L / 60L / 60L % 24L);
        int days = (int) (ms / 1000L / 60L / 60L / 24L);
        return String.format("%02dd %02dh %02dm %02ds", days, hours, mins, secs);
    }

    public static String TimeDeltaString_NoDays(long ms) {
        int secs = (int) (ms / 1000L % 60L);
        int mins = (int) (ms / 1000L / 60L % 60L);
        int hours = (int) (ms / 1000L / 60L / 60L);
        return String.format("%02dh %02dm %02ds", hours, mins, secs);
    }

    public static void MessageAllPlayers(String msg) {
        for (EntityPlayerMP p : getMinecraftServer().getPlayerList().getPlayers()) {
            ((IMixinICommandSender) p).sendMessage(msg);
        }
    }

    public static String GetUsableFilenameFromUUID(UUID uuid) {
        if (uuid == null) {
            return "_NULL_";
        } else {
            String filename = uuid.toString();
            filename = filename.replace("-", "_");
            filename = filename.replace("{", "");
            filename = filename.replace("}", "");
            return filename;
        }
    }

    public static String L33tConvert(String parm) {
        if (parm == null) {
            parm = "";
        }

        parm = parm.toLowerCase().trim();
        char[] src = new char[]{'i', 'l', 'o', 'e', 's', 't', 'a', 'z'};
        char[] tgt = new char[]{'1', '1', '0', '3', '$', '7', '4', '$'};
        StringBuilder res = new StringBuilder();
        int i = 0;

        while (i < parm.length()) {
            char ch = parm.charAt(i);
            boolean found = false;
            int j = 0;

            while (true) {
                if (j < src.length) {
                    if (ch != src[j]) {
                        ++j;
                        continue;
                    }

                    res.append(tgt[j]);
                    found = true;
                }

                if (!found) {
                    res.append(ch);
                }

                ++i;
                break;
            }
        }

        return res.toString();
    }

    public static String L33tDecrypt(String parm) {
        if (parm == null) {
            parm = "";
        }

        parm = parm.toLowerCase().trim();
        char[] src = new char[]{'!', '1', '0', '3', '$', '7', '4'};
        char[] tgt = new char[]{'i', 'i', 'o', 'e', 's', 't', 'a'};
        StringBuilder res = new StringBuilder();
        int i = 0;

        while (i < parm.length()) {
            char ch = parm.charAt(i);
            boolean found = false;
            int j = 0;

            while (true) {
                if (j < src.length) {
                    if (ch != src[j]) {
                        ++j;
                        continue;
                    }

                    res.append(tgt[j]);
                    found = true;
                }

                if (!found) {
                    res.append(ch);
                }

                ++i;
                break;
            }
        }

        return res.toString();
    }

    public static boolean StringL33tEquals(String a, String b) {
        return L33tConvert(a).equals(L33tConvert(b));
    }

    public static boolean HasBadLanguage(String msg) {
        if (!DoCensor) {
            return false;
        } else {
            msg = _ColorHelper.stripColor(msg);
            if (InternalHasBadLanguage(msg)) {
                return true;
            } else {
                String l33t = L33tDecrypt(msg);

                return !l33t.equalsIgnoreCase(msg) && InternalHasBadLanguage(l33t);
            }
        }
    }

    private static boolean InternalHasBadLanguage(String msg) {
        if (msg == null) {
            msg = "";
        }

        String msgUpper = msg.toUpperCase().trim();
        StringBuilder sb = new StringBuilder();
        StringBuilder sbWithSpaces = new StringBuilder();
        boolean removeNext = false;

        for (int sbString = 0; sbString < msgUpper.length(); ++sbString) {
            char sbWithSpacesString = msgUpper.charAt(sbString);

            if (removeNext) {
                removeNext = false;
                if (sbWithSpacesString >= 48 && sbWithSpacesString <= 57
                        || sbWithSpacesString >= 65 && sbWithSpacesString <= 70
                        || sbWithSpacesString == 76 || sbWithSpacesString == 77
                        || sbWithSpacesString == 78 || sbWithSpacesString == 75
                        || sbWithSpacesString == 82) {
                    continue;
                }
            }

            if (sbWithSpacesString >= 65 && sbWithSpacesString <= 90) {
                sb.append(sbWithSpacesString);
                sbWithSpaces.append(sbWithSpacesString);
            } else if (sbWithSpacesString == 36) {
                sb.append("S");
                sbWithSpaces.append("S");
            } else if (sbWithSpacesString == 32) {
                sbWithSpaces.append(" ");
            } else if (sbWithSpacesString == 38) {
                removeNext = true;
            }
        }

        String var8 = sb.toString();
        String var9 = sbWithSpaces.toString().trim();

        if (sb.indexOf("ANAL") >= 0
                && (var9.startsWith("ANAL ") || var9.contains(" ANAL ")
                || var9.endsWith(" ANAL"))) {
            return true;
        } else if (sb.indexOf("VIBRATOR") >= 0) {
            return true;
        } else if (sb.indexOf("STFU") >= 0 && sbWithSpaces.indexOf("ST FU") < 0
                && sbWithSpaces.indexOf("ST /FU") < 0
                && sbWithSpaces.indexOf("CHESTFUL") < 0) {
            return true;
        } else if (sb.indexOf("SUCKON") >= 0) {
            return true;
        } else if (sb.indexOf("SUKON") >= 0) {
            return true;
        } else if (sb.indexOf("JIZZ") >= 0) {
            return true;
        } else if (sb.indexOf("CUNNILI") >= 0) {
            return true;
        } else if (sb.indexOf("CUNNY") >= 0) {
            return true;
        } else if (sb.indexOf("MASTERBAT") >= 0) {
            return true;
        } else if (sb.indexOf("EJACULA") >= 0) {
            return true;
        } else if (sb.indexOf("MASTURBAT") >= 0) {
            return true;
        } else if (sb.indexOf("SUCKMYDI") >= 0) {
            return true;
        } else if (sb.indexOf("SUCKMYBAL") >= 0) {
            return true;
        } else if (sb.indexOf("SUKMYBAL") >= 0) {
            return true;
        } else if (sb.indexOf("SUKMYDI") >= 0) {
            return true;
        } else if (sb.indexOf("FVCK") >= 0) {
            return true;
        } else if (sb.indexOf("FUCK") >= 0) {
            return true;
        } else if (sb.indexOf("FUUCK") >= 0) {
            return true;
        } else if (sb.indexOf("FUCCK") >= 0) {
            return true;
        } else if (sb.indexOf("FCK") >= 0) {
            return true;
        } else if (sb.indexOf("FUCYOU") >= 0) {
            return true;
        } else if (sb.indexOf("FACKYOU") >= 0) {
            return true;
        } else if (sb.indexOf("BITCH") >= 0) {
            return true;
        } else if (sb.indexOf("BTCH") >= 0) {
            return true;
        } else if (sb.indexOf("PUSSY") >= 0) {
            return true;
        } else if (sb.indexOf("PUSSSY") >= 0) {
            return true;
        } else if (sb.indexOf("FAGGOT") >= 0) {
            return true;
        } else if (sb.indexOf("FAGOT") >= 0) {
            return true;
        } else if (sb.indexOf("ASSHOL") >= 0) {
            return true;
        } else if (sb.indexOf("JACKASS") >= 0) {
            return true;
        } else if (sb.indexOf("JAKASS") >= 0) {
            return true;
        } else if (sb.indexOf("YOUSUCK") >= 0) {
            return true;
        } else if (sb.indexOf("YOUALLSUCK") >= 0) {
            return true;
        } else if (sb.indexOf("SERVERSUCK") >= 0) {
            return true;
        } else if (sb.indexOf("YOUSUX") >= 0) {
            return true;
        } else if (sb.indexOf("SERVERSUX") >= 0) {
            return true;
        } else if (sb.indexOf("UBICH") >= 0) {
            return true;
        } else if (sb.indexOf("UBISH") >= 0) {
            return true;
        } else if (sb.indexOf("WTF") >= 0) {
            return true;
        } else if (sb.indexOf("CUNT") >= 0) {
            return true;
        } else if (sb.indexOf("DAMNIT") >= 0) {
            return true;
        } else if (sb.indexOf("DAMMIT") >= 0) {
            return true;
        } else if (sb.indexOf("THISSHIT") >= 0) {
            return true;
        } else if (sb.indexOf("BULLSHIT") >= 0) {
            return true;
        } else if (sb.indexOf("YOUSHIT") >= 0) {
            return true;
        } else if (sb.indexOf("GAYSEX") >= 0) {
            return true;
        } else if (sb.indexOf("ANALSEX") >= 0) {
            return true;
        } else if (sb.indexOf("DILDO") >= 0) {
            return true;
        } else if (var9.indexOf("GTFO") >= 0) {
            return true;
        } else if (sb.indexOf("BASTARD") >= 0) {
            return true;
        } else if (sb.indexOf("ADICK") >= 0) {
            return true;
        } else if (sb.indexOf("MYDICK") >= 0) {
            return true;
        } else if (sb.indexOf("HAVESEX") >= 0) {
            return true;
        } else if (sb.indexOf("HASSEX") >= 0) {
            return true;
        } else if (sb.indexOf("HADSEX") >= 0) {
            return true;
        } else if (sb.indexOf("SEXWITH") >= 0) {
            return true;
        } else if (sb.indexOf("SECHS") >= 0) {
            return true;
        } else if (sb.indexOf("SECKS") >= 0) {
            return true;
        } else if (sb.indexOf("SEXX") >= 0) {
            return true;
        } else if (sb.indexOf("FATBICH") >= 0) {
            return true;
        } else if (sb.indexOf("YOUWHORE") >= 0) {
            return true;
        } else if (sb.indexOf("KICKSASS") >= 0) {
            return true;
        } else if (sb.indexOf("KICKASS") >= 0) {
            return true;
        } else if (sb.indexOf("KISSINGASS") >= 0) {
            return true;
        } else if (sb.indexOf("KISSMYASS") >= 0) {
            return true;
        } else if (sb.indexOf("KICKINGASS") >= 0) {
            return true;
        } else if (sb.indexOf("BALLSACK") >= 0) {
            return true;
        } else if (sb.indexOf("MERAPE") >= 0) {
            return true;
        } else if (sb.indexOf("RAPEYOU") >= 0) {
            return true;
        } else if (sb.indexOf("WILLRAPE") >= 0) {
            return true;
        } else if (sb.indexOf("GOINGTORAPE") >= 0) {
            return true;
        } else if (sb.indexOf("BICTH") >= 0) {
            return true;
        } else if (sb.indexOf("NIPPLE") >= 0) {
            return true;
        } else if (sb.indexOf("DIPSHIT") >= 0) {
            return true;
        } else if (sb.indexOf("COCK") >= 0 && sb.indexOf("PEACOCK") < 0
                && sb.indexOf("GAMECOCK") < 0) {
            return true;
        } else if (sb.indexOf("HAILHITLER") >= 0) {
            return true;
        } else if (sb.indexOf("HEILHITLER") >= 0) {
            return true;
        } else if (sb.indexOf("EATSHIT") >= 0) {
            return true;
        } else if (sb.indexOf("NIGGER") >= 0) {
            return true;
        } else if (sb.indexOf("NIQQA") >= 0) {
            return true;
        } else if (sb.indexOf("JIGGABO") >= 0) {
            return true;
        } else if (sb.indexOf("JIGABOO") >= 0) {
            return true;
        } else if (sb.indexOf("NIQA") >= 0) {
            return true;
        } else if (sb.indexOf("NIGGA") >= 0) {
            return true;
        } else if (sb.indexOf("NIGAH") >= 0) {
            return true;
        } else if (sb.indexOf("FKYOU") >= 0) {
            return true;
        } else if (sb.indexOf("FKOFF") >= 0 && !var9.equals("AFK OFF")) {
            return true;
        } else {
            if (var8.contains("PENI")) {
                if (var9.indexOf(" PENIS") >= 0) {
                    return true;
                }

                if (var9.startsWith("PENIS")) {
                    return true;
                }

                if (var8.equals("PENIS")) {
                    return true;
                }

                if (msgUpper.contains("PENI5")) {
                    return true;
                }
            }

            if (var8.contains("THONG")) {
                return true;
            } else if (var8.contains("WANKER")) {
                return true;
            } else {
                if (var8.contains("ARSE")) {
                    if (var9.endsWith(" ARSE")) {
                        return true;
                    }

                    if (var9.startsWith("ARSE ")) {
                        return true;
                    }

                    if (var9.contains(" ARSE ")) {
                        return true;
                    }
                }

                if (var8.contains("BISH")) {
                    if (var9.endsWith(" BISH")) {
                        return true;
                    }

                    if (var9.startsWith("BISH ")) {
                        return true;
                    }

                    if (var9.contains(" BISH ")) {
                        return true;
                    }
                }

                if (var8.contains("BICH")) {
                    if (var9.startsWith("BICH ")) {
                        return true;
                    }

                    if (var9.contains(" BICH")) {
                        return true;
                    }
                }

                if (var8.contains("RETARD")) {
                    if (var9.contains(" RETARD")) {
                        return true;
                    }

                    if (var9.startsWith("RETARD")) {
                        return true;
                    }
                }

                if (msgUpper.indexOf(" SHIT") >= 0) {
                    return true;
                } else if (msgUpper.indexOf(" RAPE") >= 0) {
                    return true;
                } else if (var9.endsWith(" ASS")) {
                    return true;
                } else if (var9.endsWith(" FU")) {
                    return true;
                } else if (var9.indexOf("VAGINA") >= 0) {
                    return true;
                } else if (var9.indexOf("CROTCH") >= 0) {
                    return true;
                } else if (var9.endsWith("SHIT")) {
                    return true;
                } else if (var9.endsWith("SHITS")) {
                    return true;
                } else if (var9.endsWith("SHITTY")) {
                    return true;
                } else if (var9.indexOf(" HORNY") >= 0) {
                    return true;
                } else if (var9.startsWith("HORNY ")) {
                    return true;
                } else if (var9.startsWith("RAPE ")) {
                    return true;
                } else if (var9.indexOf(" ASS ") >= 0) {
                    return true;
                } else if (var9.indexOf(" FUK") >= 0) {
                    return true;
                } else if (var9.indexOf(" FUC") >= 0
                        && !var9.contains("FUCHSIA")) {
                    return true;
                } else if (var9.indexOf("FUC ") >= 0) {
                    return true;
                } else if (var9.endsWith(" PORN")) {
                    return true;
                } else if (var9.endsWith(" PORNO")) {
                    return true;
                } else if (var9.endsWith(" TIT")) {
                    return true;
                } else if (var9.endsWith(" TITS")) {
                    return true;
                } else if (var9.endsWith(" ANUS")) {
                    return true;
                } else if (var9.endsWith(" SLUT")) {
                    return true;
                } else if (var9.endsWith(" CLIT")) {
                    return true;
                } else if (var9.indexOf(" TITS ") >= 0) {
                    return true;
                } else if (var9.indexOf(" HUMPING ") >= 0) {
                    return true;
                } else if (var9.indexOf(" HUMPIN ") >= 0) {
                    return true;
                } else if (var9.indexOf(" CLIT ") >= 0) {
                    return true;
                } else if (var9.startsWith("TITS ")) {
                    return true;
                } else if (var9.indexOf("FK U") >= 0 && !var9.contains("AFK U")) {
                    return true;
                } else {
                    if (var8.contains("DICK")) {
                        if (var9.contains(" DICK ")) {
                            return true;
                        }

                        if (var9.contains(" DICKS ")) {
                            return true;
                        }

                        if (var9.endsWith(" DICK")) {
                            return true;
                        }

                        if (var9.endsWith(" DICKS")) {
                            return true;
                        }
                    }

                    if (var8.contains("CUM")) {
                        if (var9.contains(" CUM ")) {
                            return true;
                        }

                        if (var9.contains(" CUMS ")) {
                            return true;
                        }

                        if (var9.endsWith(" CUM")) {
                            return true;
                        }

                        if (var9.endsWith(" CUMS")) {
                            return true;
                        }
                    }

                    if (var8.equals("MEDAMN")) {
                        return true;
                    } else if (var8.equals("MESHIT")) {
                        return true;
                    } else if (var8.equals("MECUNT")) {
                        return true;
                    } else if (var8.equals("METWAT")) {
                        return true;
                    } else if (var8.equals("MEWHORE")) {
                        return true;
                    } else if (var8.equals("MECUM")) {
                        return true;
                    } else if (var8.equals("MESEX")) {
                        return true;
                    } else if (var8.equals("MESLUT")) {
                        return true;
                    } else if (var8.equals("MEDICK")) {
                        return true;
                    } else if (var8.equals("MEASS")) {
                        return true;
                    } else if (var8.equals("RAPE")) {
                        return true;
                    } else if (var8.equals("RAPED")) {
                        return true;
                    } else if (var8.equals("RAPIST")) {
                        return true;
                    } else if (var8.equals("FU")) {
                        return true;
                    } else if (var8.equals("HORNY")) {
                        return true;
                    } else if (var8.equals("SLUT")) {
                        return true;
                    } else if (var8.equals("ASS")) {
                        return true;
                    } else if (var8.equals("CUNT")) {
                        return true;
                    } else if (var8.equals("TWAT")) {
                        return true;
                    } else if (var8.equals("DAMN")) {
                        return true;
                    } else if (var8.equals("WHORE")) {
                        return true;
                    } else if (var8.equals("CUM")) {
                        return true;
                    } else if (var8.equals("SEX")) {
                        return true;
                    } else if (var8.equals("SHIT")) {
                        return true;
                    } else if (var8.equals("PUSY")) {
                        return true;
                    } else if (var8.endsWith("KUNT")) {
                        return true;
                    } else if (var8.equals("NIGER")) {
                        return true;
                    } else if (var8.equals("NIGA")) {
                        return true;
                    } else if (var8.equals("DICK")) {
                        return true;
                    } else if (var8.equals("PORN")) {
                        return true;
                    } else if (var8.equals("FUC")) {
                        return true;
                    } else if (var8.equals("SHIIT")) {
                        return true;
                    } else if (var8.startsWith("FUCH")
                            && !var9.startsWith("FUCHSIA")) {
                        return true;
                    } else if (var8.equals("RAPE")) {
                        return true;
                    } else if (var8.startsWith("FKU")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    public static void NotifyCensor(String pName, String msg) {
        if (NotifyAdminCensor) {
            String adminMsg = ChatColor.LIGHT_PURPLE + "[Censor] " + ChatColor.YELLOW + pName + ChatColor.RED + " " + msg;

            for (MC_Player player : ServerWrapper.getInstance().getPlayers()) {
                if (player.hasPermission("rainbow.censor-notice")) {
                    player.sendMessage(adminMsg);
                }
            }
        }
    }

    public static String TextAlignTrailerPerfect(String str, int padLen) {
        StringBuilder tgt = new StringBuilder();
        int pixelsTaken = 0;

        int spacesPixels;

        for (spacesPixels = 0; spacesPixels < str.length(); ++spacesPixels) {
            char left = str.charAt(spacesPixels);

            if (left == 102) {
                pixelsTaken += 5;
            } else if (left == 105) {
                pixelsTaken += 2;
            } else if (left == 44) {
                pixelsTaken += 2;
            } else if (left == 107) {
                pixelsTaken += 5;
            } else if (left == 108) {
                pixelsTaken += 3;
            } else if (left == 39) {
                pixelsTaken += 3;
            } else if (left == 116) {
                pixelsTaken += 4;
            } else if (left == 73) {
                pixelsTaken += 4;
            } else if (left == 91) {
                pixelsTaken += 4;
            } else if (left == 93) {
                pixelsTaken += 4;
            } else if (left == 32) {
                pixelsTaken += 4;
            } else if (left == 9774) {
                pixelsTaken += 4;
            } else if (left == 9876) {
                pixelsTaken += 7;
            } else {
                pixelsTaken += 6;
            }
        }

        spacesPixels = padLen * 6 - pixelsTaken;
        int var7 = spacesPixels % 4;

        int i;

        for (i = 0; i < var7; ++i) {
            tgt.append("\u205a");
        }

        for (i = 0; i < spacesPixels / 4; ++i) {
            tgt.append(" ");
        }

        return tgt.toString();
    }

    public static String RainbowString(String str) {
        return RainbowString(str, "");
    }

    public static String RainbowString(String str, String ctl) {
        if (ctl.equalsIgnoreCase("x")) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder();
            int idx = 0;
            boolean useBold = ctl.indexOf(98) >= 0;
            boolean useItalics = ctl.indexOf(105) >= 0;
            boolean useUnderline = ctl.indexOf(117) >= 0;

            for (int i = 0; i < str.length(); ++i) {
                if (idx % 6 == 0) {
                    sb.append((Object) ChatColor.RED);
                } else if (idx % 6 == 1) {
                    sb.append((Object) ChatColor.GOLD);
                } else if (idx % 6 == 2) {
                    sb.append((Object) ChatColor.YELLOW);
                } else if (idx % 6 == 3) {
                    sb.append((Object) ChatColor.GREEN);
                } else if (idx % 6 == 4) {
                    sb.append((Object) ChatColor.AQUA);
                } else if (idx % 6 == 5) {
                    sb.append((Object) ChatColor.LIGHT_PURPLE);
                }

                if (useBold) {
                    sb.append((Object) ChatColor.BOLD);
                }

                if (useItalics) {
                    sb.append((Object) ChatColor.ITALIC);
                }

                if (useUnderline) {
                    sb.append((Object) ChatColor.UNDERLINE);
                }

                sb.append(str.charAt(i));
                if (str.charAt(i) != 32) {
                    ++idx;
                }
            }

            return sb.toString();
        }
    }

    public static String TextLabel(String str, int padLen) {
        return str + ChatColor.DARK_GRAY + TextAlignTrailerPerfect(str, padLen);
    }

    public static String GetCommaList(Collection<String> arr) {
        return GetCommaList(arr, true);
    }

    public static String GetCommaList(Collection<String> arr, boolean doSort) {
        ArrayList<String> list = new ArrayList<>(arr);
        StringBuilder buf = new StringBuilder();

        Collections.sort(list);

        String str;

        for (Iterator var5 = list.iterator(); var5.hasNext(); buf.append(str)) {
            str = (String) var5.next();
            if (buf.length() > 0) {
                buf.append(", ");
            }
        }

        return buf.toString();
    }

    public static Long IncreaseEventCount(String key) {
        Long cnt = _EventManager.eventCount.get(key);

        if (cnt == null) {
            cnt = 0L;
        }

        cnt = cnt + 1L;
        _EventManager.eventCount.put(key, cnt);
        return cnt;
    }

    public static String GiveRandomItem(MC_Player p) {
        int idx = (int) (Math.random() * 25.0D);

        if (idx == 0) {
            GiveItemToPlayer(p, 5, 32, 4);
            return "32 Acacia Wood Planks";
        } else if (idx == 1) {
            GiveItemToPlayer(p, 5, 32, 5);
            return "32 Dark Oak Wood Planks";
        } else if (idx == 2) {
            GiveItemToPlayer(p, 6, 1, 4);
            return "1 Acacia sapling";
        } else if (idx == 3) {
            GiveItemToPlayer(p, 351, 12, 15);
            return "12 Bone meal";
        } else if (idx == 4) {
            GiveItemToPlayer(p, 364, 4, 0);
            return "4 Steaks";
        } else if (idx == 5) {
            GiveItemToPlayer(p, 388, 3, 0);
            return "3 Emeralds";
        } else if (idx == 6) {
            GiveItemToPlayer(p, 175, 4, (int) (Math.random() * 6.0D));
            return "4 Large Flowers";
        } else if (idx == 7) {
            GiveItemToPlayer(p, 6, 1, 5);
            return "1 Dark Oak Sapling";
        } else if (idx == 8) {
            GiveItemToPlayer(p, 264, 3, 0);
            return "3 Diamonds";
        } else if (idx == 9) {
            GiveItemToPlayer(p, 95, 8, (int) (Math.random() * 16.0D));
            return "8 Stained Glass";
        } else if (idx == 10) {
            GiveItemToPlayer(p, 160, 8, (int) (Math.random() * 16.0D));
            return "8 Stained Glass Pane";
        } else if (idx == 11) {
            GiveItemToPlayer(p, 3, 1, 2);
            return "1 Podzol";
        } else if (idx == 12) {
            GiveItemToPlayer(p, 12, 32, 1);
            return "32 Red Sand";
        } else if (idx == 13) {
            GiveItemToPlayer(p, 416, 1, 0);
            return "1 Armor Stand";
        } else if (idx == 14) {
            GiveItemToPlayer(p, 38, 8, (int) (Math.random() * 8.0D));
            return "8 Flowers";
        } else if (idx == 15) {
            GiveItemToPlayer(p, 349, 4, (int) (Math.random() * 4.0D));
            return "4 Fish";
        } else if (idx == 16) {
            GiveItemToPlayer(p, 351, 9, (int) (Math.random() * 16.0D));
            return "9 Dyes";
        } else if (idx == 17) {
            GiveItemToPlayer(p, 169, 1, 0);
            return "1 Sea Lantern";
        } else if (idx == 18) {
            GiveItemToPlayer(p, 168, 1, 0);
            return "1 Dark Prismarine";
        } else if (idx == 19) {
            GiveItemToPlayer(p, 425, 1, (int) (Math.random() * 16.0D));
            return "1 Banner";
        } else {
            int amt1;

            if (idx == 20) {
                amt1 = (int) (Math.random() * 5.0D);
                GiveItemToPlayer(p, 188 + amt1, 8, 0);
                return "8 new fences";
            } else if (idx == 21) {
                amt1 = (int) (Math.random() * 5.0D);
                GiveItemToPlayer(p, 427 + amt1, 2, 0);
                return "2 new doors";
            } else {
                byte amt;

                if (idx == 22) {
                    amt = 4;
                    GiveItemToPlayer(p, 287, amt, 0);
                    return amt + " String";
                } else if (idx == 23) {
                    amt = 4;
                    GiveItemToPlayer(p, 89, amt, 0);
                    return amt + " Glowstone";
                } else if (idx == 24) {
                    amt = 4;
                    GiveItemToPlayer(p, 100, amt, 0);
                    return amt + " Red Mushroom Blocks";
                } else {
                    return "???";
                }
            }
        }
    }

    public static boolean GiveItemToPlayer(MC_Player p, int itemID, int itemCount, int itemDamage) {
        try {
            Item exc = Item.getItemById(itemID);
            ItemStack is = new ItemStack(exc, itemCount, itemDamage);
            InventoryPlayer inventory = ((EntityPlayer) p).inventory;
            int idx = -1;

            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                if (inventory.getStackInSlot(i).isEmpty()) {
                    idx = i;
                    break;
                }
            }

            if (idx >= 0) {
                inventory.mainInventory.set(idx, is);
                return true;
            }
        } catch (Throwable var7) {
            var7.printStackTrace();
        }

        return false;
    }

    public static String TimeDeltaString_JustMinutesSecs(long ms) {
        int secs = (int) (ms / 1000L % 60L);
        int mins = (int) (ms / 1000L / 60L % 60L);

        return String.format("%02dm %02ds",
                mins, secs);
    }

    public static void MessageAllPlayers(MC_Player cs, String msg) {
        Iterator<MC_Player> var3 = ServerWrapper.getInstance().getPlayers().iterator();

        while (var3.hasNext()) {
            Object oPlayer = var3.next();
            MC_Player p = (MC_Player) oPlayer;

            if (!_CmdIgnore.IsIgnoring(p.getName(), cs.getName())) {
                p.sendMessage(msg);
            }
        }

        System.out.println("[Server] " + _ColorHelper.stripColor(msg));
    }

    public static String JustLettersAndNumbersUpperCase(String x) {
        StringBuilder sb = new StringBuilder();
        String msg = _ColorHelper.stripColor(x).toUpperCase();
        boolean removeNext = false;

        for (int i = 0; i < msg.length(); ++i) {
            char ch = msg.charAt(i);

            if (removeNext) {
                removeNext = false;
                if (ch >= 48 && ch <= 57 || ch >= 65 && ch <= 70 || ch == 76
                        || ch == 77 || ch == 78 || ch == 75 || ch == 82) {
                    continue;
                }
            }

            if (ch >= 65 && ch <= 90) {
                sb.append(ch);
            } else if (ch == 36) {
                sb.append("S");
            } else if (ch == 38) {
                removeNext = true;
            }
        }

        return sb.toString();
    }

    public static String GetTimeStringFromLong(long ms) {
        Date dt = new Date(ms);
        int hr = dt.getHours();
        int min = dt.getMinutes();

        if (hr < 12) {
            return String.format("%02d:%02dam EST",
                    hr == 0 ? 12 : hr,
                    min);
        } else {
            hr -= 12;
            return String.format("%02d:%02dpm EST",
                    hr == 0 ? 12 : hr,
                    min);
        }
    }

    public static boolean IsInsideSpawn(_SerializableLocation loc) {
        return loc.dimension != 0 ? false : IsInsideSpawn(loc.x, loc.z);
    }

    public static boolean IsInsideSpawn(int x, int z) {
        WorldServer world = getMinecraftServer().getWorld(0);
        BlockPos coords = world.getSpawnPoint();
        int depth = getMinecraftServer().getSpawnProtectionSize();
        int spawnX = coords.getX();
        int spawnZ = coords.getZ();

        return x < spawnX - depth
                ? false
                : (z < spawnZ - depth
                ? false
                : (x > spawnX + depth ? false : z <= spawnZ + depth));
    }

    public static boolean IsInsideSpawn(double x, double z) {
        return IsInsideSpawn((int) x, (int) z);
    }

    public static double GetRandPlusMinus(double dx) {
        return -1.0D * dx + Math.random() * 2.0D * dx;
    }

    public static float GetFloatRandPlusMinus(double dx) {
        return (float) GetRandPlusMinus(dx);
    }

    public static void Do_ArmorStand_Fun_At_Spawn() {
        if (ArmorStandsDance) {
            ++g_standFunIdx;
            WorldServer[] worlds = getMinecraftServer().worlds;

            for (int i = 0; i < worlds.length; ++i) {
                WorldServer world = worlds[i];

                if (world.provider.getDimensionType() == DimensionType.OVERWORLD) {
                    for (MC_Entity entity : ((MC_World) world).getEntities()) {
                        if (entity instanceof MC_ArmorStand) {
                            int x = entity.getLocation().getBlockX();
                            int z = entity.getLocation().getBlockZ();

                            if (ArmorStand_DanceEverywhere
                                    || IsInsideSpawn(x, z)
                                    || g_standFunIdx % 50L >= 38L) {
                                MC_ArmorStand stand = (MC_ArmorStand) entity;
                                MC_FloatTriplet fHead = new MC_FloatTriplet(
                                        GetFloatRandPlusMinus(5.0D),
                                        GetFloatRandPlusMinus(2.0D),
                                        GetFloatRandPlusMinus(5.0D));
                                MC_FloatTriplet fBody = new MC_FloatTriplet(
                                        GetFloatRandPlusMinus(8.0D),
                                        GetFloatRandPlusMinus(2.0D),
                                        GetFloatRandPlusMinus(8.0D));
                                MC_FloatTriplet fLeftArm = new MC_FloatTriplet(
                                        GetFloatRandPlusMinus(10.0D),
                                        GetFloatRandPlusMinus(2.0D),
                                        GetFloatRandPlusMinus(10.0D));
                                MC_FloatTriplet fRightArm = new MC_FloatTriplet(
                                        GetFloatRandPlusMinus(10.0D),
                                        GetFloatRandPlusMinus(2.0D),
                                        GetFloatRandPlusMinus(10.0D));
                                MC_FloatTriplet fLeftLeg = new MC_FloatTriplet(
                                        -1.0F + GetFloatRandPlusMinus(10.0D),
                                        GetFloatRandPlusMinus(1.0D),
                                        -1.0F + GetFloatRandPlusMinus(10.0D));
                                MC_FloatTriplet fRightLeg = new MC_FloatTriplet(
                                        1.0F + GetFloatRandPlusMinus(10.0D),
                                        GetFloatRandPlusMinus(1.0D),
                                        1.0F + GetFloatRandPlusMinus(10.0D));

                                stand.setPose(Arrays.asList(fHead, fBody, fLeftArm, fRightArm, fLeftLeg, fRightLeg));
                            }
                        }
                    }
                }
            }

        }
    }

    public static void Do_Spawn_Forcefield_MobClean() {
        new ConcurrentHashMap();
        WorldServer world = getMinecraftServer().getWorld(0);

        for (MC_Entity entity : ((MC_World) world).getEntities()) {

            if (!(entity instanceof EntityBoat)
                    && !(entity instanceof EntityFallingBlock)
                    && !(entity instanceof EntityHanging)
                    && !(entity instanceof EntityFishHook)
                    && !(entity instanceof EntityWeatherEffect)
                    && !(entity instanceof EntityMinecart)
                    && !(entity instanceof EntityThrowable)
                    && !(entity instanceof EntityArrow)
                    && !(entity instanceof EntityEnderEye)
                    && !(entity instanceof EntityXPOrb)
                    && !(entity instanceof EntityItem)
                    && !(entity instanceof EntityFireworkRocket)
                    && !(entity instanceof EntityEnderCrystal)
                    && !(entity instanceof EntityAreaEffectCloud)
                    && !(entity instanceof EntityPlayer)
                    && !(entity instanceof EntityArmorStand)
                    && !(entity instanceof EntityAmbientCreature)
                    && !(entity instanceof EntitySquid)
                    && !(entity instanceof EntityAgeable)) {
                int x = entity.getLocation().getBlockX();
                int z = entity.getLocation().getBlockZ();

                if (IsInsideSpawn(x, z)) {
                    LogManager.getLogger().debug(
                            String.format(
                                    "Spawn ForceField: %15s @ x=%-4d y=%-4d",
                                    new Object[]{
                                            entity.getName(), x,
                                            z}));

                    entity.removeEntity();
                }
            }
        }
    }

    public static MC_Entity GetNearbyEntityByName(MC_Entity p, double checkDist, String tgtName) {
        double bestDist = 100000.0D;
        MC_Entity bestEnt = null;
        String tgtNameLower = tgtName.toLowerCase();
        WorldServer[] worlds = getMinecraftServer().worlds;
        int i = 0;

        while (i < worlds.length) {
            WorldServer world = worlds[i];
            Iterator<MC_Entity> var12 = ((MC_World) world).getEntities().iterator();

            while (true) {
                if (var12.hasNext()) {
                    MC_Entity ent = var12.next();

                    if (ent == p) {
                        continue;
                    }

                    if (ent.getLocation().dimension == p.getLocation().dimension) {
                        double dist2 = ent.getLocation().distanceTo(p.getLocation());

                        if (dist2 <= checkDist) {
                            boolean matches = false;
                            String entClassName = ent.getClass().getSimpleName();

                            if (tgtName.equals("*")) {
                                matches = true;
                            }

                            if (!matches
                                    && entClassName.toLowerCase().contains(
                                    tgtNameLower)) {
                                matches = true;
                            }

                            if (!matches
                                    && _ColorHelper.stripColor(ent.getName()).toLowerCase().contains(
                                    tgtNameLower)) {
                                matches = true;
                            }

                            if (matches && dist2 < bestDist) {
                                bestDist = dist2;
                                bestEnt = ent;
                            }
                        }
                        continue;
                    }
                }

                ++i;
                break;
            }
        }

        return bestEnt;
    }
}
