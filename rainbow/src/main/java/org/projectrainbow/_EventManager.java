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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _EventManager {

    public static Map<String, Long> eventCount = new ConcurrentHashMap();
    private static String OldFilename = "EventCounts2.dat";
    private static String Filename = "EventCounts.dat";

    public _EventManager() {}

    public static void SaveEventCounts() {
        try {
            long exc = System.currentTimeMillis();
            File file = new File(_DiwUtils.RainbowDataDirectory + Filename);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(
                    new BufferedOutputStream(f));

            s.writeObject(eventCount);
            s.close();
            long msEnd = System.currentTimeMillis();
            String msg = ChatColor.YELLOW
                    + String.format("%-20s: %5d events.      Took %3d ms",
                    new Object[] {
                "Event Counts",
                Integer.valueOf(eventCount.size()), Long.valueOf(msEnd - exc)});

            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable var8) {
            System.out.println("**********************************************");
            System.out.println("SaveEventCounts: " + var8.toString());
            System.out.println("**********************************************");
        }

    }

    public static void LoadEventCounts() {
        try {
            File file = new File(_DiwUtils.RainbowDataDirectory + Filename);
            File oldFile = new File(OldFilename);

            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }

            if (!file.exists()) {
                System.out.println("Starting New Event Count DB: " + Filename);
                eventCount = new ConcurrentHashMap();
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(
                    new BufferedInputStream(f));

            eventCount = (ConcurrentHashMap) s.readObject();
            s.close();
        } catch (Throwable var4) {
            var4.printStackTrace();
            System.out.println("Starting New Event Count DB: " + Filename);
            eventCount = new ConcurrentHashMap();
        }

    }
}
