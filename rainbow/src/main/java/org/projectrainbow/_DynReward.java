package org.projectrainbow;


import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import com.google.common.io.Files;
import joebkt._DynRewardInfo;
import joebkt._SerializableLocation;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _DynReward {

    public static Map<String, _SerializableLocation> LastSelection = new ConcurrentHashMap<String, _SerializableLocation>();
    public static String MsgPrefix = ChatColor.LIGHT_PURPLE + "[Reward] ";
    public static Map<String, _DynRewardInfo> RewardMarkers = new HashMap<String, _DynRewardInfo>();
    public static Map<String, Long> timeSince = new ConcurrentHashMap<String, Long>();
    private static String OldFilename = "JkcDynRewards2.dat";
    private static String Filename = "DynamicRewards.dat";

    public _DynReward() {
    }

    public static void ReportMessage(String msg) {
        msg = _ColorHelper.stripColor(msg);
        System.out.println("DynReward: " + msg);
    }

    public static void LoadRewards() {
        try {
            File file = new File(_DiwUtils.RainbowDataDirectory + Filename);
            File oldFile = new File(OldFilename);

            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }

            if (!file.exists()) {
                RewardMarkers = new HashMap<String, _DynRewardInfo>();
                return;
            }

            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(f);

            RewardMarkers = (Map<String, _DynRewardInfo>) s.readObject();
            s.close();
        } catch (Throwable var4) {
            var4.printStackTrace();
            RewardMarkers = new HashMap<String, _DynRewardInfo>();
        }

    }

    public static void SaveRewards() {
        try {
            File exc = new File(_DiwUtils.RainbowDataDirectory + Filename);
            FileOutputStream f = new FileOutputStream(exc);
            ObjectOutputStream s = new ObjectOutputStream(f);

            s.writeObject(RewardMarkers);
            s.close();
        } catch (Throwable var3) {
            System.out.println("**********************************************");
            System.out.println("SaveRewards: " + var3.toString());
            System.out.println("**********************************************");
        }

    }

    public static String GetDescribeLine(_DynRewardInfo info) {
        StringBuilder sb = new StringBuilder();

        sb.append((Object) ChatColor.YELLOW);
        if (info.RewardName.isEmpty()) {
            sb.append("No Name");
        } else {
            sb.append(_DiwUtils.TranslateColorString(info.RewardName, true));
        }

        sb.append(ChatColor.GRAY).append(" ").append(info.Loc.toString());
        if (!info.AchievementName.isEmpty()) {
            sb.append(ChatColor.GREEN).append(" ").append(info.AchievementName);
        }

        sb.append(ChatColor.GRAY).append(String.format(" %ds", info.delaySeconds));
        if (info.Rewards == null) {
            sb.append(ChatColor.AQUA).append(" No Items.");
        } else {
            sb.append(ChatColor.AQUA).append(String.format(" %d items.", info.Rewards.keySet().size()));
        }

        if (info.OneTimeOnly) {
            sb.append(ChatColor.RED).append(" Once");
        }

        return sb.toString();
    }

    public static void CmdList(MC_Player cs) {
        cs.sendMessage(
                _DiwUtils.RainbowString(
                        "----------------------------------------------"));
        int count = 0;

        for (Iterator var3 = RewardMarkers.keySet().iterator(); var3.hasNext(); ++count) {
            String key = (String) var3.next();

            cs.sendMessage(
                    GetDescribeLine((_DynRewardInfo) RewardMarkers.get(key)));
        }

        cs.sendMessage(
                _DiwUtils.RainbowString(
                        "----------------------------------------------"));
        cs.sendMessage(ChatColor.GOLD + "There are " + count + " Rewards Set.");
    }

    public static void SendSetUsage(MC_Player p) {
        p.sendMessage(
                MsgPrefix + ChatColor.GOLD
                        + "Usage: /reward set [flag] [values]");
        p.sendMessage(
                MsgPrefix + ChatColor.WHITE + "Flags: " + ChatColor.GRAY
                        + "delay, name, achieve, item, onetime");
    }

    public static void CmdSet(MC_Player cs, String[] args) {
        if (args.length < 3) {
            SendSetUsage(cs);
        } else {
            _SerializableLocation sloc = (_SerializableLocation) LastSelection.get(
                    cs.getName());

            if (sloc == null) {
                cs.sendMessage(
                        MsgPrefix + ChatColor.RED + "No Selection. "
                                + ChatColor.GRAY + "Use EMERALD to select.");
            } else {
                int x = (int) sloc.x;
                int y = (int) sloc.y;
                int z = (int) sloc.z;
                int dimension = sloc.dimension;
                String locKey = GetRewardKey(x, y, z, dimension);
                _DynRewardInfo info = (_DynRewardInfo) RewardMarkers.get(locKey);

                if (info == null) {
                    info = new _DynRewardInfo(x, y, z, dimension);
                }

                if (!args[1].equalsIgnoreCase("onetime")) {
                    if (args[1].equalsIgnoreCase("name")) {
                        info.RewardName = _DiwUtils.ConcatArgs(args, 2);
                        RewardMarkers.put(locKey, info);
                        cs.sendMessage(
                                MsgPrefix + ChatColor.GREEN
                                        + "Reward Name Set To: " + ChatColor.WHITE
                                        + _DiwUtils.TranslateColorString(info.RewardName,
                                        true));
                        SaveRewards();
                    } else {
                        boolean itemID;
                        int itemID1;

                        if (args[1].equalsIgnoreCase("delay")) {
                            itemID = false;

                            try {
                                itemID1 = Integer.parseInt(args[2]);
                            } catch (Throwable var12) {
                                cs.sendMessage(
                                        MsgPrefix + ChatColor.RED
                                                + "Specify a number of seconds.");
                                return;
                            }

                            info.delaySeconds = itemID1;
                            RewardMarkers.put(locKey, info);
                            cs.sendMessage(
                                    MsgPrefix + ChatColor.GREEN
                                            + "Reward Delay Set To: " + ChatColor.WHITE
                                            + info.delaySeconds);
                            SaveRewards();
                        } else if (args[1].equalsIgnoreCase("item")) {
                            itemID = false;

                            try {
                                itemID1 = Integer.parseInt(args[2]);
                            } catch (Throwable var14) {
                                cs.sendMessage(
                                        MsgPrefix + ChatColor.RED
                                                + "Invalid Item ID: /reward set item [ItemID] [Count].");
                                cs.sendMessage(
                                        MsgPrefix + ChatColor.RED
                                                + "- Special Item IDs: Random, Fireworks");
                                cs.sendMessage(
                                        MsgPrefix + ChatColor.RED
                                                + "- To remove item use Count = 0");
                                return;
                            }

                            if (itemID1 < -2) {
                                cs.sendMessage(
                                        MsgPrefix + ChatColor.RED
                                                + "Invalid Item ID: /reward set item [ItemID] [Count].");
                            } else {
                                boolean itemCount = false;

                                int itemCount1;

                                try {
                                    itemCount1 = Integer.parseInt(args[3]);
                                } catch (Throwable var13) {
                                    cs.sendMessage(
                                            MsgPrefix + ChatColor.RED
                                                    + "Invalid Count: /reward set item [ItemID] [Count].");
                                    return;
                                }

                                if (itemCount1 <= 0) {
                                    if (info.Rewards.containsKey(
                                            itemID1)) {
                                        info.Rewards.remove(
                                                itemID1);
                                        RewardMarkers.put(locKey, info);
                                        cs.sendMessage(
                                                MsgPrefix + ChatColor.GREEN
                                                        + String.format(
                                                        "Item ID %d (%s) removed.",
                                                        itemID1,
                                                        GetItemName(itemID1)));
                                        SaveRewards();
                                        return;
                                    }

                                    itemCount1 = 1;
                                }

                                if (info.Rewards == null) {
                                    info.Rewards = new HashMap<Integer, Integer>();
                                }

                                info.Rewards.put(itemID1,
                                        itemCount1);
                                RewardMarkers.put(locKey, info);
                                cs.sendMessage(
                                        MsgPrefix + ChatColor.GREEN
                                                + String.format(
                                                "Added %d of Item ID %d (%s).",
                                                itemCount1,
                                                itemID1,
                                                GetItemName(itemID1)));
                                SaveRewards();
                            }
                        } else if (!args[1].equalsIgnoreCase("achieve")
                                && !args[1].equalsIgnoreCase("achievement")) {
                            cs.sendMessage(
                                    MsgPrefix + ChatColor.RED
                                            + "Unknown parameter: " + ChatColor.GOLD
                                            + args[1]);
                            SendSetUsage(cs);
                        } else {
                            info.AchievementName = _DiwUtils.ConcatArgs(args, 2);
                            info.AchievementName = _DiwUtils.TranslateColorString(
                                    info.AchievementName, true);
                            RewardMarkers.put(locKey, info);
                            cs.sendMessage(
                                    MsgPrefix + ChatColor.GREEN
                                            + "Reward Achievement Set To: "
                                            + ChatColor.WHITE + info.AchievementName);
                            SaveRewards();
                        }
                    }
                } else {
                    if (!args[2].equalsIgnoreCase("on")
                            && !args[2].equalsIgnoreCase("true")) {
                        info.OneTimeOnly = false;
                    } else {
                        info.OneTimeOnly = true;
                    }

                    RewardMarkers.put(locKey, info);
                    cs.sendMessage(
                            MsgPrefix + ChatColor.GREEN
                                    + "Reward OneTime Flag Set To: " + ChatColor.WHITE
                                    + info.OneTimeOnly);
                    SaveRewards();
                }
            }
        }
    }

    public static String GetItemName(int itemID) {
        if (itemID == -1) {
            return "Random";
        } else if (itemID == -2) {
            return "Kinder Egg";
        } else {
            /*String itemName = MixinItemIntIDToString.getIdMap().get(itemID);
            if (itemName == null) {
                itemName = "unknown";
            }

            if (itemName.startsWith("minecraft:")) {
                itemName = itemName.substring("minecraft:".length());
            }

            return itemName;*/
            return "unknown";
        }
    }

    public static void CmdDelete(MC_Player cs) {
        _SerializableLocation sloc = (_SerializableLocation) LastSelection.get(
                cs.getName());

        if (sloc == null) {
            cs.sendMessage(
                    MsgPrefix + ChatColor.RED + "No Selection. "
                            + ChatColor.GRAY + "Use EMERALD to select.");
        } else {
            int x = (int) sloc.x;
            int y = (int) sloc.y;
            int z = (int) sloc.z;
            int dimension = sloc.dimension;
            String locKey = GetRewardKey(x, y, z, dimension);
            _DynRewardInfo info = (_DynRewardInfo) RewardMarkers.get(locKey);

            if (info == null) {
                cs.sendMessage(
                        MsgPrefix + ChatColor.RED + "No Prize at Selection: "
                                + ChatColor.WHITE + locKey);
            } else {
                cs.sendMessage(
                        MsgPrefix + ChatColor.GREEN + "Removed "
                                + ChatColor.YELLOW + info.RewardName + ".");
                RewardMarkers.remove(locKey);
            }
        }
    }

    public static String GetRewardKey(int x, int y, int z, int dimension) {
        return x + "." + y + "." + z + "." + dimension;
    }

    public static boolean HoldingEmerald(MC_Player p) {
        try {
            return p.getItemInHand().getFriendlyName().equalsIgnoreCase(
                    "Emerald");
        } catch (Throwable var2) {
            return false;
        }
    }

    public static boolean HandleInteract(MC_Player p, int x, int y, int z, int dimension) {
        boolean itemID = false;
        int var20;

        if (p.isOp() && HoldingEmerald(p)) {
            _SerializableLocation var21 = new _SerializableLocation((double) x,
                    (double) y, (double) z, dimension, 0.0F, 0.0F);

            LastSelection.put(p.getName(), var21);
            p.sendMessage(
                    MsgPrefix + ChatColor.GREEN + "Selection: "
                            + ChatColor.WHITE + var21.toString());
            String var22 = GetRewardKey(x, y, z, dimension);
            _DynRewardInfo var23 = (_DynRewardInfo) RewardMarkers.get(var22);

            if (var23 != null) {
                p.sendMessage(GetDescribeLine(var23));
                if (var23.Rewards != null && var23.Rewards.keySet().size() > 0) {
                    Iterator var25 = var23.Rewards.keySet().iterator();

                    while (var25.hasNext()) {
                        Integer var24 = (Integer) var25.next();

                        var20 = var24;
                        int var27 = (Integer) var23.Rewards.get(var24);

                        if (var27 > 0) {
                            p.sendMessage(
                                    MsgPrefix + ChatColor.AQUA + "Reward: "
                                            + ChatColor.WHITE + var27 + " "
                                            + ChatColor.GOLD
                                            + _DiwUtils.TranslateColorString(
                                            GetItemName(var20), true)
                                            + " ("
                                            + var20
                                            + ")");
                        }
                    }
                }
            }

            return true;
        } else {
            String locKey = GetRewardKey(x, y, z, dimension);
            _DynRewardInfo info = (_DynRewardInfo) RewardMarkers.get(locKey);

            if (info == null) {
                return false;
            } else if (UserHasToWait(p, info)) {
                return true;
            } else {
                int openInvSlots = 0;

                for (MC_ItemStack itemStack : p.getInventory()) {
                    if (itemStack == null || itemStack.getCount() == 0) {
                        openInvSlots++;
                    }
                }


                int requiredOpenSlots = 0;
                Iterator colorName = info.Rewards.keySet().iterator();

                int itemCount;

                while (colorName.hasNext()) {
                    Integer foundCount = (Integer) colorName.next();

                    var20 = foundCount;
                    itemCount = (Integer) info.Rewards.get(foundCount);
                    if (var20 == -1) {
                        requiredOpenSlots += itemCount;
                    } else {
                        ++requiredOpenSlots;
                    }
                }

                if (requiredOpenSlots > openInvSlots) {
                    p.sendMessage(
                            MsgPrefix + ChatColor.RED + "Requires "
                                    + requiredOpenSlots + " empty item slots. "
                                    + ChatColor.AQUA + "Make room!");
                    return true;
                } else {
                    Long var26 = _DiwUtils.IncreaseEventCount(
                            "REWARD." + p.getName() + "." + info.RewardName);

                    if (var26 > 1L
                            && info.OneTimeOnly) {
                        p.sendMessage(
                                MsgPrefix + ChatColor.GOLD
                                        + "You have already claimed this one time prize.");
                        return true;
                    } else {
                        String var28 = _DiwUtils.TranslateColorString(
                                info.RewardName, true);
                        String msg = MsgPrefix + ChatColor.GREEN + "You found: "
                                + ChatColor.WHITE + var28 + "!";

                        if (var26 > 1L) {
                            msg = msg
                                    + String.format(ChatColor.GRAY + " #%d",
                                    var26);
                        }

                        p.sendMessage(msg);
                        if (var26 <= 1L
                                && info.AchievementName.isEmpty()) {
                            _DiwUtils.MessageAllPlayers(
                                    "" + ChatColor.RED + ChatColor.BOLD
                                            + "Congrats! " + ChatColor.YELLOW
                                            + p.getName() + ChatColor.WHITE + " found "
                                            + ChatColor.GREEN + var28);
                        }

                        if (info.Rewards != null
                                && info.Rewards.keySet().size() > 0) {
                            Iterator var15 = info.Rewards.keySet().iterator();

                            while (var15.hasNext()) {
                                Integer key = (Integer) var15.next();

                                var20 = key;
                                itemCount = (Integer) info.Rewards.get(key);
                                if (itemCount > 0) {
                                    String itemName = GetItemName(var20);

                                    if (var20 == -1) {
                                        for (int var29 = 0; var29 < itemCount; ++var29) {
                                            itemName = _DiwUtils.GiveRandomItem(
                                                    p);
                                            String giftMsg1 = ChatColor.GREEN
                                                    + "Received: "
                                                    + ChatColor.GOLD + itemName;

                                            p.sendMessage(MsgPrefix + giftMsg1);
                                            ReportMessage(
                                                    ChatColor.YELLOW
                                                            + p.getName() + " "
                                                            + giftMsg1);
                                        }
                                    } else {
                                        String giftMsg;

                                        if (var20 == -2) {
                                            giftMsg = String.format(
                                                    "kinder give %s %d",
                                                    p.getName(),
                                                    itemCount);

                                            try {
                                                ServerWrapper.getInstance().executeCommand(giftMsg);
                                            } catch (Exception var19) {
                                                ;
                                            }
                                        } else {
                                            _DiwUtils.GiveItemToPlayer(p, var20,
                                                    itemCount, 0);
                                            giftMsg = ChatColor.GREEN
                                                    + "Received: "
                                                    + ChatColor.WHITE
                                                    + itemCount + " "
                                                    + ChatColor.GOLD + itemName;
                                            p.sendMessage(MsgPrefix + giftMsg);
                                            ReportMessage(
                                                    ChatColor.YELLOW
                                                            + p.getName() + " "
                                                            + giftMsg);
                                        }
                                    }
                                }
                            }
                        } else {
                            p.sendMessage(
                                    MsgPrefix + ChatColor.AQUA
                                            + "No reward items at this time.");
                        }

                        p.updateInventory();

                        return true;
                    }
                }
            }
        }
    }

    public static boolean UserHasToWait(MC_Player p, _DynRewardInfo info) {
        if (info.delaySeconds <= 0) {
            return false;
        } else {
            String key = p.getName() + "." + info.RewardName;
            Long msBefore = (Long) timeSince.get(key);
            Long curMS = System.currentTimeMillis();

            if (msBefore == null) {
                timeSince.put(key, curMS);
                return false;
            } else {
                Long msDelta = curMS - msBefore;
                Long msWaitTime = (long) info.delaySeconds * 1000L;

                if (msDelta < msWaitTime) {
                    p.sendMessage(
                            MsgPrefix + ChatColor.RED
                                    + "Too soon, for this reward you must wait: "
                                    + ChatColor.AQUA
                                    + _DiwUtils.TimeDeltaString_JustMinutesSecs(
                                    msWaitTime - msDelta));
                    return true;
                } else {
                    timeSince.put(key, curMS);
                    return false;
                }
            }
        }
    }
}
