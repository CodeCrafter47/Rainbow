package org.projectrainbow;


import PluginReference.ChatColor;
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


public class _UUIDMapper {

    private static Map<String, String> LowerNameToUUID = new ConcurrentHashMap<String, String>();
    private static Map<String, List<String>> UUIDToNameList = new ConcurrentHashMap<String, List<String>>();
    private static String OldFilename = "Joe_UUID_Mapping.dat";
    private static String Filename = "Player_UUID_Mapping.dat";

    public _UUIDMapper() {
    }

    public static String getCaseCorrectedPlayerName(String name) {
        String uuid = LowerNameToUUID.get(name.toLowerCase());
        if (uuid == null) {
            return null;
        }
        List<String> names = UUIDToNameList.get(uuid);
        if (names == null || names.isEmpty()) {
            return null;
        }
        return names.get(names.size() - 1);
    }

    public static void SaveData() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + Filename);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(
                    new BufferedOutputStream(f));

            s.writeObject(LowerNameToUUID);
            s.writeObject(UUIDToNameList);
            s.close();
            long msEnd = System.currentTimeMillis();
            String msg = ChatColor.YELLOW
                    + String.format("%-20s: %5d mappings.    Took %3d ms",
                    "UUID Maps",
                    LowerNameToUUID.size(),
                    msEnd - exc);

            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable var8) {
            System.out.println("**********************************************");
            System.out.println("NameToUUID: " + var8.toString());
            System.out.println("**********************************************");
        }

    }

    public static void LoadData() {
        try {
            File file = new File(_DiwUtils.RainbowDataDirectory + Filename);
            File oldFile = new File(OldFilename);

            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }

            if (!file.exists()) {
                System.out.println("Starting New NameToUUID: " + Filename);
                LowerNameToUUID = new ConcurrentHashMap<String, String>();
                UUIDToNameList = new ConcurrentHashMap<String, List<String>>();
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(
                    new BufferedInputStream(f));

            LowerNameToUUID = (Map<String, String>) s.readObject();

            try {
                UUIDToNameList = (Map<String, List<String>>) s.readObject();
            } catch (Exception var5) {
                UUIDToNameList = new ConcurrentHashMap<String, List<String>>();
            }

            s.close();
        } catch (Throwable var6) {
            var6.printStackTrace();
            System.out.println("Starting New NameToUUID: " + Filename);
            LowerNameToUUID = new ConcurrentHashMap<String, String>();
            UUIDToNameList = new ConcurrentHashMap<String, List<String>>();
        }

    }

    public static void AddMap(String pName, String uuid) {
        LowerNameToUUID.put(pName.toLowerCase(), uuid);
        AddMaptoNameList(pName, uuid);
    }

    private static void AddMaptoNameList(String pName, String uuid) {
        List<String> nameList = UUIDToNameList.get(uuid);

        if (nameList == null) {
            nameList = new ArrayList<String>();
        }

        if (!nameList.contains(pName)) {
            nameList.add(pName);
            UUIDToNameList.put(uuid, nameList);
            if (((List) nameList).size() > 1) {
                _DiwUtils.ConsoleMsg(
                        "UUIDMapper: " + pName
                                + "\'s UUID has multiple names on record!: "
                                + _DiwUtils.GetCommaList(nameList));
            }
        }

    }

    public static UUID getUUID(String pName) {
        String uuid = LowerNameToUUID.get(pName.toLowerCase());
        return uuid == null ? null : UUID.fromString(uuid);
    }

    public static String getName(UUID uuid) {
        List<String> nameHistory = getNameHistory(uuid);
        if (nameHistory == null || nameHistory.isEmpty()) {
            return null;
        }
        return nameHistory.get(nameHistory.size() - 1);
    }

    public static List<String> getNameHistory(UUID uuid) {
        return UUIDToNameList.get(uuid.toString());
    }
}
