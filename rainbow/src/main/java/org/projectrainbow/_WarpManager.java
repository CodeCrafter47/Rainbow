package org.projectrainbow;


import PluginReference.ChatColor;
import com.google.common.io.Files;
import joebkt._SerializableLocation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _WarpManager {

    public static Map<String, _SerializableLocation> adminWarps = new ConcurrentHashMap();
    private static String Warp_FileName = "Warps.dat";

    public _WarpManager() {}

    public static void SaveWarps() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + Warp_FileName);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(
                    new BufferedOutputStream(f));

            s.writeObject(adminWarps);
            s.close();
            long msEnd = System.currentTimeMillis();
            String msg = ChatColor.YELLOW
                    + String.format("%-20s: %5d warps.       Took %3d ms",
                    new Object[] {
                "Admin Warps",
                Integer.valueOf(adminWarps.size()), Long.valueOf(msEnd - exc)});

            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable var8) {
            System.out.println("**********************************************");
            System.out.println("SaveWarps: " + var8.toString());
            System.out.println("**********************************************");
        }

    }

    public static void LoadWarps() {
        try {
            File file = new File(_DiwUtils.RainbowDataDirectory + Warp_FileName);
            File oldFile = new File(Warp_FileName);

            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }

            if (!file.exists()) {
                System.out.println("Starting New Admin Warps: " + Warp_FileName);
                adminWarps = new ConcurrentHashMap();
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(
                    new BufferedInputStream(f));

            adminWarps = (ConcurrentHashMap) s.readObject();
            s.close();
        } catch (Throwable var4) {
            var4.printStackTrace();
            System.out.println("Starting New Admin Warps: " + Warp_FileName);
            adminWarps = new ConcurrentHashMap();
        }

    }

    public static String GetActualWarpName(String name) {
        Iterator var2 = adminWarps.keySet().iterator();

        while (var2.hasNext()) {
            String key = (String) var2.next();

            if (key.equalsIgnoreCase(name)) {
                return key;
            }
        }

        return null;
    }
}
