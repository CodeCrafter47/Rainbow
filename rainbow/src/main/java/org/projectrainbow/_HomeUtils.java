package org.projectrainbow;

import com.google.common.io.Files;
import joebkt._SerializableLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class _HomeUtils {
    private static Map<String, _SerializableLocation> playerHomes;
    private static Map<String, _SerializableLocation> playerHomes2;
    private static String DataFilename;
    private static String DataFilename2;

    static {
        _HomeUtils.playerHomes = new ConcurrentHashMap<String, _SerializableLocation>();
        _HomeUtils.playerHomes2 = new ConcurrentHashMap<String, _SerializableLocation>();
        _HomeUtils.DataFilename = "Homes.dat";
        _HomeUtils.DataFilename2 = "Homes2.dat";
    }

    public static void onLogin(String name, UUID uuid) {
        if (!playerHomes.containsKey(uuid.toString()) && playerHomes.containsKey(name)) {
            playerHomes.put(uuid.toString(), playerHomes.remove(name));
        }
        if (!playerHomes2.containsKey(uuid.toString()) && playerHomes2.containsKey(name)) {
            playerHomes2.put(uuid.toString(), playerHomes2.remove(name));
        }
    }

    public static void SaveHomes() {
        try {
            final long msStart = System.currentTimeMillis();
            final File file = new File(String.valueOf(_DiwUtils.RainbowDataDirectory) + _HomeUtils.DataFilename);
            final FileOutputStream f = new FileOutputStream(file);
            final ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(_HomeUtils.playerHomes);
            s.close();
            final long msEnd = System.currentTimeMillis();
            final String msg = String.valueOf(_ColorHelper.YELLOW) + String.format("%-20s: %5d players.     Took %3d ms", "Homes Save", _HomeUtils.playerHomes.size(), msEnd - msStart);
            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable exc) {
            System.out.println("**********************************************");
            System.out.println("SaveHomes: " + exc.toString());
            System.out.println("**********************************************");
        }
    }

    public static void LoadHomes() {
        try {
            final File file = new File(String.valueOf(_DiwUtils.RainbowDataDirectory) + _HomeUtils.DataFilename);
            final File oldFile = new File(_HomeUtils.DataFilename);
            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }
            if (!file.exists()) {
                System.out.println("Starting New Home Database...");
                _HomeUtils.playerHomes = new ConcurrentHashMap<String, _SerializableLocation>();
                return;
            }
            final FileInputStream f = new FileInputStream(file);
            final ObjectInputStream s = new ObjectInputStream(f);
            _HomeUtils.playerHomes = (ConcurrentHashMap<String, _SerializableLocation>) s.readObject();
            s.close();
            final String msg = String.format("Homes Loaded: " + _ColorHelper.WHITE + "%d homes.", _HomeUtils.playerHomes.size());
            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable exc) {
            exc.printStackTrace();
            System.out.println("Starting New Home Database...");
            _HomeUtils.playerHomes = new ConcurrentHashMap<String, _SerializableLocation>();
        }
    }

    public static void SaveHomes2() {
        try {
            final long msStart = System.currentTimeMillis();
            final File file = new File(String.valueOf(_DiwUtils.RainbowDataDirectory) + _HomeUtils.DataFilename2);
            final FileOutputStream f = new FileOutputStream(file);
            final ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(_HomeUtils.playerHomes2);
            s.close();
            final long msEnd = System.currentTimeMillis();
            final String msg = String.valueOf(_ColorHelper.YELLOW) + String.format("%-20s: %5d players.     Took %3d ms", "Homes2 Save", _HomeUtils.playerHomes2.size(), msEnd - msStart);
            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable exc) {
            System.out.println("**********************************************");
            System.out.println("SaveHomes2: " + exc.toString());
            System.out.println("**********************************************");
        }
    }

    public static void LoadHomes2() {
        try {
            final File file = new File(String.valueOf(_DiwUtils.RainbowDataDirectory) + _HomeUtils.DataFilename2);
            final File oldFile = new File(_HomeUtils.DataFilename2);
            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }
            if (!file.exists()) {
                System.out.println("Starting New Home2 Database...");
                _HomeUtils.playerHomes2 = new ConcurrentHashMap<String, _SerializableLocation>();
                return;
            }
            final FileInputStream f = new FileInputStream(file);
            final ObjectInputStream s = new ObjectInputStream(f);
            _HomeUtils.playerHomes2 = (ConcurrentHashMap<String, _SerializableLocation>) s.readObject();
            s.close();
            final String msg = String.format("Homes Loaded: " + _ColorHelper.WHITE + "%d homes.", _HomeUtils.playerHomes2.size());
            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable exc) {
            exc.printStackTrace();
            System.out.println("Starting New Home2 Database...");
            _HomeUtils.playerHomes2 = new ConcurrentHashMap<String, _SerializableLocation>();
        }
    }

    public static _SerializableLocation getHome(UUID uuid) {
        return playerHomes.get(uuid.toString());
    }

    public static void setHome(UUID uuid, _SerializableLocation home) {
        playerHomes.put(uuid.toString(), home);
    }

    public static _SerializableLocation getHome2(UUID uuid) {
        return playerHomes2.get(uuid.toString());
    }

    public static void setHome2(UUID uuid, _SerializableLocation home) {
        playerHomes2.put(uuid.toString(), home);
    }
}
