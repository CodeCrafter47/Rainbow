package org.projectrainbow;


import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import com.google.common.base.Objects;
import com.google.common.io.Files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class _EconomyManager {

    public static Map<String, Double> economy = new ConcurrentHashMap<String, Double>();
    public static Map<String, Double> itemWorth = new ConcurrentHashMap<String, Double>();
    private static String Filename = "Economy.dat";

    public _EconomyManager() {
    }

    public static void SaveEconomy() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + Filename);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(
                    new BufferedOutputStream(f));

            s.writeObject(economy);
            s.writeObject(itemWorth);
            s.close();
            long msEnd = System.currentTimeMillis();
            String msg = ChatColor.YELLOW + String.format("%-20s: %5d balances.    Took %3d ms", "Economy", economy.size(), msEnd - exc);

            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable var8) {
            System.out.println("**********************************************");
            System.out.println("SaveEconomy: " + var8.toString());
            System.out.println("**********************************************");
        }

    }

    public static void LoadEconomy() {
        try {
            File file = new File(_DiwUtils.RainbowDataDirectory + Filename);
            File oldFile = new File(Filename);

            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }

            if (!file.exists()) {
                System.out.println("Starting New Economy: " + Filename);
                economy = new ConcurrentHashMap<String, Double>();
                itemWorth = new ConcurrentHashMap<String, Double>();
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(
                    new BufferedInputStream(f));

            economy = (Map<String, Double>) s.readObject();
            itemWorth = (Map<String, Double>) s.readObject();
            s.close();
        } catch (Throwable var4) {
            var4.printStackTrace();
            System.out.println("Starting New Economy: " + Filename);
            economy = new ConcurrentHashMap<String, Double>();
            itemWorth = new ConcurrentHashMap<String, Double>();
        }
    }

    public static void onLogin(String name, UUID uuid) {
        if (!economy.containsKey(uuid.toString()) && economy.containsKey(name.toLowerCase())) {
            economy.put(uuid.toString(), economy.remove(name.toLowerCase()));
        }
    }

    public static Double GetBalance(MC_Player cs) {
        return GetBalance(cs.getUUID());
    }

    public static Double GetBalance(UUID uuid) {
        Double val = economy.get(uuid.toString());

        return val == null ? Double.valueOf(0.0D) : val;
    }

    public static void SetBalance(MC_Player cs, Double val) {
        SetBalance(cs.getUUID(), val);
    }

    public static void SetBalance(UUID uuid, Double val) {
        economy.put(uuid.toString(), val);
    }

    public static void Withdraw(MC_Player player, Double val) {
        SetBalance(player, GetBalance(player) - val);
    }

    public static void Deposit(UUID player, Double val) {
        SetBalance(player, GetBalance(player) + val);
    }

    public static void Deposit(MC_Player player, Double val) {
        SetBalance(player, GetBalance(player) + val);
    }

    public static void ShowBalance(MC_Player cs) {
        Double bal = GetBalance(cs);

        _DiwUtils.reply(cs, ChatColor.AQUA + "Your Balance: " + ChatColor.GREEN + String.format("%.2f", bal));
    }

    public static void ShowBalanceOf(MC_Player cs, String tgtName) {
        UUID uuid = _UUIDMapper.getUUID(tgtName);
        Double bal = uuid == null ? 0 : GetBalance(uuid);

        _DiwUtils.reply(cs, ChatColor.AQUA + tgtName + " Balance: " + ChatColor.GREEN + String.format("%.2f", bal));
    }

    public static String GetItemKey(MC_ItemStack is) {
        try {
            return is.getFriendlyName();
        } catch (Exception var2) {
            return "???";
        }
    }

    public static void SetItemWorth(MC_ItemStack is, Double dbl) {
        String key = GetItemKey(is);

        itemWorth.put(key, dbl);
        if (dbl <= 0.0D) {
            itemWorth.remove(key);
        }

    }

    public static Double GetItemWorth(MC_ItemStack is) {
        Double dbl = itemWorth.get(GetItemKey(is));

        if (dbl == null) {
            dbl = 0.0D;
        }

        return dbl;
    }

    public static void DoPayDay() {
        List<MC_Player> players = ServerWrapper.getInstance().getPlayers();
        int nPlayers = players.size();
        String msgLog = "---- Executing Pay Day: " + nPlayers + " players";

        _DiwUtils.ConsoleMsg(msgLog);

        for (MC_Player p : players) {
            double amt = _DiwUtils.PayDayAmount;
            String msg = _DiwUtils.RainbowString("Pay Day!!! ", "b") + ChatColor.GREEN + "You receive " + String.format("%.2f", amt);

            p.sendMessage(msg);
            Deposit(p, amt);
        }

    }
}
