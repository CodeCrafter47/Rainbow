package org.projectrainbow;

import PluginReference.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

public class Hooks {
    private static Logger logger = LogManager.getLogger("Minecraft");

    public static void onShutdown() {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onShutdown();
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onTick(int tickNumber) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onTick(tickNumber);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPlayerLogin(String playerName, UUID uuid, String ip) {
        _EconomyManager.onLogin(playerName, uuid);
        _HomeUtils.onLogin(playerName, uuid);
        _PermMgr.onLogin(playerName, uuid);
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPlayerLogin(playerName, uuid, ip);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPlayerLogin(String playerName, UUID uuid, InetAddress address, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPlayerLogin(playerName, uuid, address, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPlayerLogout(String playerName, UUID uuid) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPlayerLogout(playerName, uuid);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onInteracted(MC_Player plr, MC_Location loc, MC_ItemStack isHandItem) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onInteracted(plr, loc, isHandItem);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onItemPlaced(MC_Player plr, MC_Location loc, MC_ItemStack isHandItem, MC_Location locPlacedAgainst, MC_DirectionNESWUD dir) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onItemPlaced(plr, loc, isHandItem, locPlacedAgainst, dir);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onBlockBroke(MC_Player plr, MC_Location loc, int blockKey) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onBlockBroke(plr, loc, blockKey);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onBlockBroke(MC_Player plr, MC_Location loc, MC_Block blk) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onBlockBroke(plr, loc, blk);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPlayerDeath(MC_Player plrVictim, MC_Player plrKiller, MC_DamageType dmgType, String deathMsg) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPlayerDeath(plrVictim, plrKiller, dmgType, deathMsg);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPlayerRespawn(MC_Player plr) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPlayerRespawn(plr);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPlayerInput(MC_Player plr, String msg, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPlayerInput(plr, msg, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onConsoleInput(String cmd, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onConsoleInput(cmd, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptBlockBreak(MC_Player plr, MC_Location loc, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptBlockBreak(plr, loc, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptPlaceOrInteract(MC_Player plr, MC_Location loc, MC_DirectionNESWUD dir, MC_Hand hand, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                if (hand == MC_Hand.MAIN_HAND) {
                    plugin.ref.onAttemptPlaceOrInteract(plr, loc, ei, dir);
                }
                plugin.ref.onAttemptPlaceOrInteract(plr, loc, dir, hand, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptExplosion(MC_Location loc, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptExplosion(loc, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static boolean onAttemptExplodeSpecific(MC_Entity ent, List<MC_Location> locs) {
        boolean result = false;
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                result |= plugin.ref.onAttemptExplodeSpecific(ent, locs);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
        return result;
    }

    public static void onAttemptDamageHangingEntity(MC_Player plr, MC_Location loc, MC_HangingEntityType entType, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptDamageHangingEntity(plr, loc, entType, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptItemFrameInteract(MC_Player plr, MC_Location loc, MC_ItemFrameActionType actionType, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptItemFrameInteract(plr, loc, actionType, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptPotionEffect(MC_Player plr, MC_PotionEffectType potionType, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptPotionEffect(plr, potionType, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptPlayerTeleport(MC_Player plr, MC_Location loc, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptPlayerTeleport(plr, loc, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptPlayerChangeDimension(MC_Player plr, int newDimension, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptPlayerChangeDimension(plr, newDimension, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptItemDrop(MC_Player plr, MC_ItemStack is, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptItemDrop(plr, is, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptAttackEntity(MC_Player plr, MC_Entity ent, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptAttackEntity(plr, ent, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptEntityDamage(MC_Entity ent, MC_DamageType dmgType, double amt, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptEntityDamage(ent, dmgType, amt, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onGenerateWorldColumn(int x, int z, MC_GeneratedColumn data) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onGenerateWorldColumn(x, z, data);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptPistonAction(MC_Location loc, MC_DirectionNESWUD dir, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptPistonAction(loc, dir, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptBlockFlow(MC_Location loc, MC_Block blk, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptBlockFlow(loc, blk, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onContainerOpen(MC_Player plr, List<MC_ItemStack> items, String internalClassName) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onContainerOpen(plr, items, internalClassName);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptPlayerMove(MC_Player plr, MC_Location locFrom, MC_Location locTo, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptPlayerMove(plr, locFrom, locTo, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPacketSoundEffect(MC_Player plr, String soundName, MC_Location loc, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPacketSoundEffect(plr, soundName, loc, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPlayerJoin(MC_Player plr) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPlayerJoin(plr);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onSignChanging(MC_Player plr, MC_Sign sign, MC_Location loc, List<String> newLines, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onSignChanging(plr, sign, loc, newLines, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onSignChanged(MC_Player plr, MC_Sign sign, MC_Location loc) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onSignChanged(plr, sign, loc);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptEntitySpawn(MC_Entity ent, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptEntitySpawn(ent, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onServerFullyLoaded() {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onServerFullyLoaded();
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptHopperReceivingItem(MC_Location loc, MC_ItemStack is, boolean isMinecartHopper, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptHopperReceivingItem(loc, is, isMinecartHopper, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptBookChange(MC_Player plr, List<String> bookContent, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptBookChange(plr, bookContent, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptCropTrample(MC_Entity ent, MC_Location loc, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptCropTrample(ent, loc, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onFallComplete(MC_Entity ent, float fallDistance, MC_Location loc, boolean isWaterLanding) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onFallComplete(ent, fallDistance, loc, isWaterLanding);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptArmorStandInteract(MC_Player plr, MC_Entity entStand, MC_ArmorStandActionType actionType, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptArmorStandInteract(plr, entStand, actionType, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onNonPlayerEntityDeath(MC_Entity entVictim, MC_Entity entKiller, MC_DamageType dmgType) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onNonPlayerEntityDeath(entVictim, entKiller, dmgType);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptItemUse(MC_Player plr, MC_ItemStack is, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptItemUse(plr, is, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onContainerClosed(MC_Player plr, MC_ContainerType containerType) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onContainerClosed(plr, containerType);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptItemPickup(MC_Player plr, MC_ItemStack is, boolean isXpOrb, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptItemPickup(plr, is, isXpOrb, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptEntityInteract(MC_Player plr, MC_Entity ent, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptEntityInteract(plr, ent, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onItemCrafted(MC_Player plr, MC_ItemStack isCraftedItem) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onItemCrafted(plr, isCraftedItem);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPlayerBedEnter(MC_Player plr, MC_Block blkBed, MC_Location locBed) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPlayerBedEnter(plr, blkBed, locBed);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPlayerBedLeave(MC_Player plr, MC_Block blkBed, MC_Location locBed) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPlayerBedLeave(plr, blkBed, locBed);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptBlockPlace(MC_Player plr, MC_Location loc, MC_Block blk, MC_ItemStack isHandItem, MC_Location locPlacedAgainst, MC_DirectionNESWUD dir, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptBlockPlace(plr, loc, blk, isHandItem, locPlacedAgainst, dir, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptDeath(MC_Entity entVictim, MC_Entity entKiller, MC_DamageType dmgType, float dmgAmount) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptDeath(entVictim, entKiller, dmgType, dmgAmount);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptFishingReel(MC_Player plr, MC_ItemStack isCatch, MC_Entity entCatch, boolean groundCatch, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptFishingReel(plr, isCatch, entCatch, groundCatch, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptEntityMiscGrief(MC_Entity ent, MC_Location loc, MC_MiscGriefType griefType, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptEntityMiscGrief(ent, loc, griefType, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptSpectateEntity(MC_Player plr, MC_Entity ent, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptSpectateEntity(plr, ent, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptDispense(MC_Location loc, int idxItem, MC_Container container, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptDispense(loc, idxItem, container, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onEntityPushed(MC_Entity entity, MC_Entity pushedEntity, MC_FloatTriplet velocity, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onEntityPushed(entity, pushedEntity, velocity, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptProjectileHitEntity(MC_Projectile projectile, MC_Entity entity, MC_Location hitLocation,  MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptProjectileHitEntity(projectile, entity, hitLocation, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onAttemptProjectileHitBlock(MC_Projectile projectile, MC_Location blockLocation, MC_DirectionNESWUD face, MC_Location hitLocation, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onAttemptProjectileHitBlock(projectile, blockLocation, face, hitLocation, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }

    public static void onPlayerKick(MC_Player player, String reason, MC_EventInfo ei) {
        for (PluginInfo plugin : _DiwUtils.pluginManager.plugins) {
            try {
                plugin.ref.onPlayerKick(player, reason, ei);
            } catch (Throwable th) {
                logger.error("Failed to pass event to plugin " + plugin.name, th);
            }
        }
    }
}
