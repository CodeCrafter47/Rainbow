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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class _PermMgr {

    private static Map<String, ConcurrentHashMap<String, Boolean>> permData = new ConcurrentHashMap<String, ConcurrentHashMap<String, Boolean>>();
    public static String m_DataFilename = "PermissionData.dat";

    public _PermMgr() {
    }

    public static void onLogin(String name, UUID uuid) {
        if (!permData.containsKey(uuid.toString()) && permData.containsKey(name.toLowerCase())) {
            permData.put(uuid.toString(), permData.remove(name.toLowerCase()));
        }
    }

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
            String msg = ChatColor.YELLOW + String.format("%-20s: %5d players.     Took %3d ms", "Permissions Save", permData.size(), msEnd - exc);

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
                _DiwUtils.ConsoleMsg("Loading Permissions: Starting new file: " + m_DataFilename);
                permData = new ConcurrentHashMap<String, ConcurrentHashMap<String, Boolean>>();
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(
                    new BufferedInputStream(f));

            permData = (Map<String, ConcurrentHashMap<String, Boolean>>) s.readObject();
            s.close();
            long msEnd = System.currentTimeMillis();

            _DiwUtils.ConsoleMsg(ChatColor.YELLOW + String.format("%-20s: %5d players.  Took %3d ms", "Permissions Load", permData.size(), msEnd - exc));
        } catch (Throwable var8) {
            var8.printStackTrace();
            _DiwUtils.ConsoleMsg("Loading Permissions: Starting new file: " + m_DataFilename);
            permData = new ConcurrentHashMap<String, ConcurrentHashMap<String, Boolean>>();
        }

    }

    public static void givePermission(UUID uuid, String perm) {
        if (perm != null) {
            String key = uuid == null ? "*" : uuid.toString();
            perm = perm.toLowerCase();
            ConcurrentHashMap<String, Boolean> map = permData.get(key);

            if (map == null) {
                map = new ConcurrentHashMap<String, Boolean>();
            }

            map.put(perm, true);
            permData.put(key, map);
        }
    }

    public static List<String> getPermissions(UUID uuid) {
        ArrayList<String> perms = new ArrayList<String>();

        String key = uuid == null ? "*" : uuid.toString();
        Map<String, Boolean> map = permData.get(key);

        if (map == null) {
            return perms;
        } else {

            for (String perm : map.keySet())
                perms.add(perm);
        }

        return perms;
    }

    public static void takePermission(UUID uuid, String perm) {
        if (perm != null) {
            String key = uuid == null ? "*" : uuid.toString();
            perm = perm.toLowerCase();
            ConcurrentHashMap<String, Boolean> map = permData.get(key);

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

    public static boolean hasPermission(UUID uuid, String perm) {
        return hasPermission(uuid, perm, true);
    }

    public static boolean hasPermission(UUID uuid, String perm, boolean checkAllGroup) {
        if (perm == null) {
            return true;
        } else {
            String key = uuid == null ? "*" : uuid.toString();
            perm = perm.toLowerCase();
            ConcurrentHashMap map = (ConcurrentHashMap) permData.get(key);
            boolean res = false;

            if (map != null) {
                res = map.containsKey(perm);
            }

            for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
                try {
                    Boolean exc = plugin.ref.onRequestPermission(key, perm);

                    if (exc != null) {
                        res = exc;
                    }
                } catch (Throwable var8) {
                    var8.printStackTrace();
                }
            }

            return !res && checkAllGroup && !key.equals("*")
                    ? hasPermission(null, perm, false)
                    : res;
        }
    }

    public static void clearAllPermissions() {
        permData = new ConcurrentHashMap<String, ConcurrentHashMap<String, Boolean>>();
    }
}
