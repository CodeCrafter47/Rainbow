package org.projectrainbow;

import PluginReference.MC_Player;
import PluginReference.RainbowUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

import java.util.*;

public class _Janitor {
    public static void DoMobClean(MC_Player p, boolean doClean, String[] cleanTarget) {
        final Map<String, Integer> mobCounts = new HashMap<>();
        int sectionSize = 16;
        int mobLimitPerSection = 35;
        Map<String, Integer> sectionHit = new HashMap<>();
        StringBuilder sbPlayers = new StringBuilder();
        int nPlayers = 0;
        int nRemoved = 0;

        for (WorldServer world : _DiwUtils.getMinecraftServer().getWorlds()) {
            for (Entity ent : world.loadedEntityList) {
                EntityType<?> var1 = ent.getType();
                ResourceLocation var2 = EntityType.getId(var1);
                String entClassName = var1.isSerializable() && var2 != null ? var2.toString() : null;
                String mobName = entClassName;
                if (ent instanceof EntityPlayerMP) {
                    mobName = ((MC_Player)ent).getName();
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
                    String key = String.format("%d %d %d %d %s", PluginHelper.getLegacyDimensionId(ent.dimension), secX, secY, secZ, mobName);
                    Integer hits = sectionHit.get(key);
                    if (hits == null) {
                        hits = 1;
                    } else {
                        hits = hits + 1;
                    }

                    sectionHit.put(key, hits);
                    if (hits > mobLimitPerSection) {
                        if (hits == mobLimitPerSection + 1) {
                            String locStr = String.format("%d %d %d %d", PluginHelper.getLegacyDimensionId(ent.dimension), mobX, mobY, mobZ);
                            String msg = String.format("Reached Mob Limit %d (%s) @ %s", mobLimitPerSection, mobName, locStr);
                            _DiwUtils.reply(p, _ColorHelper.LIGHT_PURPLE + msg);
                        }

                        if (!isPlayer && doClean) {
                            ent.remove();
                            ++nRemoved;
                            _DiwUtils.reply(p, _ColorHelper.LIGHT_PURPLE + ent);
                        }
                    }

                    boolean shouldRemove = false;
                    if (cleanTarget != null) {
                        for (String iterator : cleanTarget) {
                            if (iterator.equalsIgnoreCase(entClassName)) {
                                shouldRemove = true;
                                break;
                            }

                            if (iterator.equalsIgnoreCase(mobName) || iterator.equals("*") || mobName.startsWith("item.") && iterator.equalsIgnoreCase("item")) {
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
                        ent.remove();
                    }
                }

                if (!isPlayer && ent.isAlive()) {
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
