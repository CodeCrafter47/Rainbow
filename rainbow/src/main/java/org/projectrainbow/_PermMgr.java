package org.projectrainbow;


import PluginReference.ChatColor;
import PluginReference.PluginInfo;
import com.google.common.io.Files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _PermMgr {

    public static Map<String, ConcurrentHashMap<String, Boolean>> permData = new ConcurrentHashMap();
    public static String m_DataFilename = "PermissionData.dat";

    public _PermMgr() {}

    public static void SaveData() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + m_DataFilename);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(
                    new BufferedOutputStream(f));

            s.writeObject(permData);
            s.close();
            long msEnd = System.currentTimeMillis();
            String msg = ChatColor.YELLOW
                    + String.format("%-20s: %5d players.     Took %3d ms",
                    new Object[] {
                "Permissions Save",
                Integer.valueOf(permData.size()), Long.valueOf(msEnd - exc)});

            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable var8) {
            System.out.println("**********************************************");
            System.out.println("Saving Permissions: " + var8.toString());
            System.out.println("**********************************************");
        }

    }

    public static void LoadData() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + m_DataFilename);
            File oldFile = new File(m_DataFilename);

            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }

            if (!file.exists()) {
                _DiwUtils.ConsoleMsg(
                        "Loading Permissions: Starting new file: " + m_DataFilename);
                permData = new ConcurrentHashMap();
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(
                    new BufferedInputStream(f));

            permData = (ConcurrentHashMap) s.readObject();
            s.close();
            long msEnd = System.currentTimeMillis();

            _DiwUtils.ConsoleMsg(
                    ChatColor.YELLOW
                            + String.format("%-20s: %5d players.  Took %3d ms",
                            new Object[] {
                "Permissions Load",
                Integer.valueOf(permData.size()), Long.valueOf(msEnd - exc)}));
        } catch (Throwable var8) {
            var8.printStackTrace();
            _DiwUtils.ConsoleMsg(
                    "Loading Permissions: Starting new file: " + m_DataFilename);
            permData = new ConcurrentHashMap();
        }

    }

    public static void givePermission(String key, String perm) {
        if (key != null) {
            if (perm != null) {
                key = key.toLowerCase();
                perm = perm.toLowerCase();
                ConcurrentHashMap map = (ConcurrentHashMap) permData.get(key);

                if (map == null) {
                    map = new ConcurrentHashMap();
                }

                map.put(perm, Boolean.valueOf(true));
                permData.put(key, map);
            }
        }
    }

    public static List<String> getPermissions(String key) {
        ArrayList perms = new ArrayList();

        if (key == null) {
            return perms;
        } else {
            key = key.toLowerCase();
            Map map = (ConcurrentHashMap) permData.get(key);

            if (map == null) {
                return perms;
            } else {
                Iterator var4 = map.keySet().iterator();

                while (var4.hasNext()) {
                    String perm = (String) var4.next();

                    perms.add(perm);
                }

                return perms;
            }
        }
    }

    public static void takePermission(String key, String perm) {
        if (key != null) {
            if (perm != null) {
                key = key.toLowerCase();
                perm = perm.toLowerCase();
                ConcurrentHashMap map = (ConcurrentHashMap) permData.get(key);

                if (map != null) {
                    map.remove(perm);
                    if (map.size() <= 0) {
                        permData.remove(key);
                    } else {
                        permData.put(key, map);
                    }

                }
            }
        }
    }

    public static boolean hasPermission(String key, String perm) {
        return hasPermission(key, perm, true);
    }

    public static boolean hasPermission(String key, String perm, boolean checkAllGroup) {
        if (key == null) {
            return false;
        } else if (perm == null) {
            return true;
        } else {
            key = key.toLowerCase();
            perm = perm.toLowerCase();
            ConcurrentHashMap map = (ConcurrentHashMap) permData.get(key);
            boolean res = false;

            if (map != null) {
                res = map.containsKey(perm);
            }

            Iterator var6 = _DiwUtils.pluginManager.plugins.iterator();

            while (var6.hasNext()) {
                PluginInfo plugin = (PluginInfo) var6.next();

                try {
                    Boolean exc = plugin.ref.onRequestPermission(key, perm);

                    if (exc != null) {
                        res = exc.booleanValue();
                    }
                } catch (Throwable var8) {
                    var8.printStackTrace();
                }
            }

            return !res && checkAllGroup && !key.equals("*")
                    ? hasPermission("*", perm, false)
                    : res;
        }
    }

    public static void clearAllPermissions() {
        permData = new ConcurrentHashMap();
    }
}
