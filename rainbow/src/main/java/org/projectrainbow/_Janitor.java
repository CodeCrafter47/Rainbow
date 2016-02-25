package org.projectrainbow;

import PluginReference.RainbowUtils;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.WorldServer;
import org.projectrainbow.interfaces.IMixinICommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _Janitor {
    public static void DoMobClean(EntityPlayerMP p, boolean doClean, String[] cleanTarget) {
        final Map<String, Integer> mobCounts = new HashMap<String, Integer>();
        int sectionSize = 16;
        int mobLimitPerSection = 35;
        Map<String, Integer> sectionHit = new HashMap<String, Integer>();
        StringBuilder sbPlayers = new StringBuilder();
        int nPlayers = 0;
        int nRemoved = 0;
        WorldServer[] worlds = _DiwUtils.getMinecraftServer().worldServers;

        for (int i = 0; i < worlds.length; ++i) {
            WorldServer world = worlds[i];

            for (Entity ent : world.loadedEntityList) {
                String entClassName = ent.getClass().getSimpleName();
                String mobName = entClassName;
                if (ent instanceof EntityPlayerMP) {
                    mobName = ent.getName();
                }

                if (mobName.equalsIgnoreCase("ItemEntity")) {
                    mobName = "Item";
                }

                boolean isPlayer = false;
                if (ent instanceof EntityPlayerMP) {
                    isPlayer = true;
                }

                if (mobName.equals("Item")) {
                    mobLimitPerSection = 64;
                } else {
                    mobLimitPerSection = 25;
                }

                if (sbPlayers == null) {
                    sbPlayers = new StringBuilder();
                }

                if (isPlayer) {
                    if (sbPlayers.length() > 0) {
                        sbPlayers.append(", ");
                    }

                    sbPlayers.append(mobName);
                    ++nPlayers;
                } else {
                    int mobX = (int) ent.posX;
                    int mobY = (int) ent.posY;
                    int mobZ = (int) ent.posZ;
                    int secX = Math.abs(mobX) / sectionSize * (mobX < 0 ? -1 : 1);
                    int secY = Math.abs(mobY) / sectionSize * (mobY < 0 ? -1 : 1);
                    int secZ = Math.abs(mobZ) / sectionSize * (mobZ < 0 ? -1 : 1);
                    String key = String.format("%d %d %d %d", ent.dimension, secX, secY, secZ);
                    Integer hits = sectionHit.get(key);
                    if (hits == null) {
                        hits = 1;
                    } else {
                        hits = hits + 1;
                    }

                    sectionHit.put(key, hits);
                    if (hits > mobLimitPerSection) {
                        if (hits == mobLimitPerSection + 1) {
                            String locStr = String.format("%d %d %d %d", ent.dimension, mobX, mobY, mobZ);
                            String msg = String.format("Reached Mob Limit %d (%s) @ %s", mobLimitPerSection, mobName, locStr);
                            System.out.println(msg);
                            if (p != null) {
                                ((IMixinICommandSender) p).sendMessage(_ColorHelper.LIGHT_PURPLE + msg);
                            }
                        }

                        if (!isPlayer && doClean) {
                            ent.setDead();
                            ++nRemoved;
                            if (p != null) {
                                ((IMixinICommandSender) p).sendMessage(_ColorHelper.LIGHT_PURPLE + ent.toString());
                            }
                        }
                    }

                    boolean shouldRemove = false;
                    if (cleanTarget != null) {
                        for (int idx = 0; idx < cleanTarget.length; ++idx) {
                            if (cleanTarget[idx].equalsIgnoreCase(entClassName)) {
                                shouldRemove = true;
                                break;
                            }

                            if (cleanTarget[idx].equalsIgnoreCase(mobName) || cleanTarget[idx].equals("*") || mobName.startsWith("item.") && cleanTarget[idx].equalsIgnoreCase("item")) {
                                shouldRemove = true;
                                break;
                            }
                        }
                    }

                    for (Entity entity : ent.getPassengers()) {
                        if (entity instanceof EntityPlayer) {
                            shouldRemove = false;
                        }
                    }

                    if (shouldRemove) {
                        ++nRemoved;
                        ent.setDead();
                    }
                }

                if (!isPlayer && !ent.isDead) {
                    Integer cnt = mobCounts.get(mobName);
                    if (cnt == null) {
                        cnt = 0;
                    }

                    mobCounts.put(mobName, cnt + 1);
                }
            }
        }

        List<String> keys = new ArrayList<String>(mobCounts.keySet());
        Collections.sort(keys, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return mobCounts.get(o2) - mobCounts.get(o1);
            }
        });
        int tot = 0;
        String dashes = "--- ---------------";
        System.out.println(dashes);
        if (p != null) {
            ((IMixinICommandSender) p).sendMessage(_ColorHelper.LIGHT_PURPLE + dashes);
        }

        for (String mobName : keys) {
            Integer cnt = mobCounts.get(mobName);
            tot += cnt;
            String msg = String.format("%4d %s", cnt, mobName);
            System.out.println(msg);
            if (p != null) {
                ((IMixinICommandSender) p).sendMessage(_ColorHelper.LIGHT_PURPLE + msg);
            }
        }

        System.out.println(dashes);
        if (p != null) {
            ((IMixinICommandSender) p).sendMessage(_ColorHelper.LIGHT_PURPLE + dashes);
        }

        String msg = String.format("%4d Total Mobs", tot);
        System.out.println(msg);
        if (p != null) {
            ((IMixinICommandSender) p).sendMessage(_ColorHelper.LIGHT_PURPLE + msg);
        }

        if (sbPlayers != null) {
            String tmsg = String.format("%d Players: %s", nPlayers, sbPlayers.toString());
            System.out.println("\n" + tmsg + "\n");
            if (p != null) {
                tmsg = String.format("%d Players: " + _ColorHelper.YELLOW + "%s", nPlayers, sbPlayers.toString());
                ((IMixinICommandSender) p).sendMessage(_ColorHelper.AQUA + tmsg);
            }
        }

        if (doClean || cleanTarget != null) {
            String tmsg = String.format("REMOVED %d Entities", nRemoved);
            if (cleanTarget != null) {
                tmsg = String.format("REMOVED %d Entities (CleanTarget=%s)", nRemoved, RainbowUtils.GetCommaList(cleanTarget));
            }

            System.out.println("\n" + tmsg + "\n");
            if (p != null) {
                ((IMixinICommandSender) p).sendMessage(_ColorHelper.GREEN + tmsg);
            }
        }

    }
}
