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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _UUIDMapper {

    public static Map<String, String> LowerNameToUUID = new ConcurrentHashMap();
    public static Map<String, List<String>> UUIDToNameList = new ConcurrentHashMap();
    private static String OldFilename = "Joe_UUID_Mapping.dat";
    private static String Filename = "Player_UUID_Mapping.dat";

    public _UUIDMapper() {}

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
                    new Object[] {
                "UUID Maps",
                Integer.valueOf(LowerNameToUUID.size()),
                Long.valueOf(msEnd - exc)});

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
                LowerNameToUUID = new ConcurrentHashMap();
                UUIDToNameList = new ConcurrentHashMap();
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(
                    new BufferedInputStream(f));

            LowerNameToUUID = (ConcurrentHashMap) s.readObject();

            try {
                UUIDToNameList = (ConcurrentHashMap) s.readObject();
            } catch (Exception var5) {
                UUIDToNameList = new ConcurrentHashMap();
            }

            s.close();
        } catch (Throwable var6) {
            var6.printStackTrace();
            System.out.println("Starting New NameToUUID: " + Filename);
            LowerNameToUUID = new ConcurrentHashMap();
            UUIDToNameList = new ConcurrentHashMap();
        }

    }

    public static void AddMap(String pName, String uuid) {
        LowerNameToUUID.put(pName.toLowerCase(), uuid);
        AddMaptoNameList(pName, uuid);
    }

    public static void AddMaptoNameList(String pName, String uuid) {
        List<String> nameList = UUIDToNameList.get(uuid);

        if (nameList == null) {
            nameList = new ArrayList();
        }

        if (!((List) nameList).contains(pName)) {
            ((List) nameList).add(pName);
            UUIDToNameList.put(uuid, nameList);
            if (((List) nameList).size() > 1) {
                _DiwUtils.ConsoleMsg(
                        "UUIDMapper: " + pName
                        + "\'s UUID has multiple names on record!: "
                        + _DiwUtils.GetCommaList((Collection) nameList));
            }
        }

    }

    public static String GetUUIDFromPlayerName(String pName) {
        return (String) LowerNameToUUID.get(pName.toLowerCase());
    }

    public static List<String> GetPlayerNamesFromUUID(String uid) {
        return (List) UUIDToNameList.get(uid);
    }
}
