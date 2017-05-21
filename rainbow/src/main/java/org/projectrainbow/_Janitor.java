package org.projectrainbow;

import PluginReference.RainbowUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _Janitor {
    public static void DoMobClean(EntityPlayerMP p, boolean doClean, String[] cleanTarget) {
        final Map<String, Integer> mobCounts = new HashMap<>();
        int sectionSize = 16;
        int mobLimitPerSection = 35;
        Map<String, Integer> sectionHit = new HashMap<>();
        StringBuilder sbPlayers = new StringBuilder();
        int nPlayers = 0;
        int nRemoved = 0;
        WorldServer[] worlds = _DiwUtils.getMinecraftServer().worlds;

        for (int i = 0; i < worlds.length; ++i) {
            WorldServer world = worlds[i];

            for (Entity ent : world.loadedEntityList) {
                String entClassName = EntityList.getEntityString(ent);
                String mobName = entClassName;
                if (ent instanceof EntityPlayerMP) {
                    mobName = ent.getName();
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
                    String key = String.format("%d %d %d %d %s", ent.dimension, secX, secY, secZ, mobName);
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
                            _DiwUtils.reply(p, _ColorHelper.LIGHT_PURPLE + msg);
                        }

                        if (!isPlayer && doClean) {
                            ent.setDead();
                            ++nRemoved;
                            _DiwUtils.reply(p, _ColorHelper.LIGHT_PURPLE + ent);
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

        List<String> keys = new ArrayList<>(mobCounts.keySet());
        keys.sort(Comparator.<String>comparingInt(mobCounts::get).reversed());
        int tot = 0;
        String dashes = "--- ---------------";
        _DiwUtils.reply(p, _ColorHelper.LIGHT_PURPLE + dashes);

        for (String mobName : keys) {
            Integer cnt = mobCounts.get(mobName);
            tot += cnt;
            String msg = String.format("%4d %s", cnt, mobName);
            _DiwUtils.reply(p, _ColorHelper.LIGHT_PURPLE + msg);
        }

        _DiwUtils.reply(p, _ColorHelper.LIGHT_PURPLE + dashes);

        String msg = String.format("%4d Total Mobs", tot);
        _DiwUtils.reply(p, _ColorHelper.LIGHT_PURPLE + msg);

        if (sbPlayers != null) {
            String tmsg = String.format("%d Players: " + _ColorHelper.YELLOW + "%s", nPlayers, sbPlayers.toString());
            _DiwUtils.reply(p, _ColorHelper.AQUA + tmsg);
        }

        if (doClean || cleanTarget != null) {
            String tmsg = String.format("REMOVED %d Entities", nRemoved);
            if (cleanTarget != null) {
                tmsg = String.format("REMOVED %d Entities (CleanTarget=%s)", nRemoved, RainbowUtils.GetCommaList(cleanTarget));
            }

            _DiwUtils.reply(p, _ColorHelper.GREEN + tmsg);
        }

    }
}
