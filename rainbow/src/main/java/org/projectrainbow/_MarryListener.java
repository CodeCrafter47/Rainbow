package org.projectrainbow;


import PluginReference.ChatColor;
import PluginReference.MC_Player;
import joebkt._SerializableLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _MarryListener {

    public static Map<String, _SerializableLocation> mapLocChurch1 = new ConcurrentHashMap();
    public static Map<String, _SerializableLocation> mapLocChurch2 = new ConcurrentHashMap();
    public static int g_selIdx = 0;

    public _MarryListener() {}

    public static boolean HandleSetChurchPosition(MC_Player p) {
        if (!p.isOp()) {
            return false;
        } else {
            _SerializableLocation loc = new _SerializableLocation(p.getLocation().x,
                    p.getLocation().y, p.getLocation().z, p.getLocation().dimension, p.getLocation().yaw, p.getLocation().pitch);

            ++g_selIdx;
            if (g_selIdx % 2 == 1) {
                mapLocChurch1.put(p.getName(), loc);
                p.sendMessage(
                        ChatColor.AQUA + "Church Location, 1st Corner Set: "
                        + ChatColor.WHITE + loc.toString());
                PossibleCompleteChurchSelection(p);
            } else {
                mapLocChurch2.put(p.getName(), loc);
                p.sendMessage(
                        ChatColor.AQUA + "Church Location, 2nd Corner Set: "
                        + ChatColor.WHITE + loc.toString());
                PossibleCompleteChurchSelection(p);
            }

            return true;
        }
    }

    public static void PossibleCompleteChurchSelection(MC_Player p) {
        String pName = p.getName();
        _SerializableLocation loc1 = (_SerializableLocation) mapLocChurch1.get(
                pName);

        if (loc1 != null) {
            _SerializableLocation loc2 = (_SerializableLocation) mapLocChurch2.get(
                    pName);

            if (loc2 != null) {
                _MarryManager.churchLoc1 = loc1;
                _MarryManager.churchLoc2 = loc2;
                mapLocChurch1.remove(pName);
                mapLocChurch2.remove(pName);
                p.sendMessage(ChatColor.GREEN + "The Church is now defined!");
            }
        }
    }
}
