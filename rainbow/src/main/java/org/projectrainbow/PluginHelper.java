package org.projectrainbow;

import PluginReference.BlockHelper;
import PluginReference.MC_Block;
import PluginReference.MC_DamageType;
import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_EnchantmentType;
import PluginReference.MC_EntityType;
import PluginReference.MC_GameMode;
import PluginReference.MC_GameRuleType;
import PluginReference.MC_ItemStack;
import PluginReference.MC_PotionEffect;
import PluginReference.MC_PotionEffectType;
import PluginReference.MC_WorldBiomeType;
import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArmorStand;
import net.minecraft.src.EntityBat;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityCaveSpider;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityDragon;
import net.minecraft.src.EntityEgg;
import net.minecraft.src.EntityEnderCrystal;
import net.minecraft.src.EntityEnderEye;
import net.minecraft.src.EntityEnderPearl;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityEndermite;
import net.minecraft.src.EntityExpBottle;
import net.minecraft.src.EntityFallingBlock;
import net.minecraft.src.EntityFireworkRocket;
import net.minecraft.src.EntityFishHook;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityGiant;
import net.minecraft.src.EntityGuardian;
import net.minecraft.src.EntityHorse;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLargeFireball;
import net.minecraft.src.EntityLavaSlime;
import net.minecraft.src.EntityMinecartEmpty;
import net.minecraft.src.EntityMushroomCow;
import net.minecraft.src.EntityOzelot;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.EntityRabbit;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySmallFireball;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.EntitySnowman;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntitySquid;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.EntityTippedArrow;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.EntityVillagerGolem;
import net.minecraft.src.EntityWitch;
import net.minecraft.src.EntityWither;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.rm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PluginHelper {
    public static BiMap<EnumFacing, MC_DirectionNESWUD> directionMap = HashBiMap.create();
    public static BiMap<MC_GameRuleType, String> gameRuleMap = HashBiMap.create();
    public static BiMap<WorldSettings.GameType, MC_GameMode> gamemodeMap = HashBiMap.create();
    public static BiMap<Class<? extends Entity>, MC_EntityType> entityMap = HashBiMap.create();
    public static BiMap<Potion, MC_PotionEffectType> potionMap = HashBiMap.create();
    public static BiMap<Short, MC_EnchantmentType> enchantmentMap = HashBiMap.create();
    public static BiMap<BiomeGenBase, MC_WorldBiomeType> biomeMap = HashBiMap.create();

    public static List<MC_ItemStack> invArrayToList(ItemStack[] items) {
        int size = items.length;
        List<MC_ItemStack> list = new ArrayList<MC_ItemStack>();
        for (int i = 0; i < size; i++) {
            ItemStack itemStack = items[i];
            list.add(itemStack == null ? EmptyItemStack.getInstance() : (MC_ItemStack) (Object) itemStack);
        }
        return list;
    }

    public static void updateInv(ItemStack[] inv, List<MC_ItemStack> items) {
        int size = inv.length;
        for (int i = 0; i < size; i++) {
            inv[i] = (ItemStack) (Object) (items.get(i) instanceof EmptyItemStack ? null : items.get(i));
        }
    }

    public static MC_PotionEffect wrap(PotionEffect potionEffect) {
        MC_PotionEffectType type = potionMap.get(potionEffect.a());
        type = Objects.firstNonNull(type, MC_PotionEffectType.UNSPECIFIED);
        MC_PotionEffect mc_potionEffect = new MC_PotionEffect(type, potionEffect.getPotionID(), potionEffect.getDuration());
        mc_potionEffect.ambient = potionEffect.getIsAmbient();
        mc_potionEffect.showsParticles = potionEffect.getIsShowParticles();
        return mc_potionEffect;
    }

    public static PotionEffect unwrap(MC_PotionEffect potionEffect) {
        return new PotionEffect(potionMap.inverse().get(potionEffect.type), potionEffect.duration, potionEffect.amplifier, potionEffect.ambient, potionEffect.showsParticles);
    }

    static {
        directionMap.put(EnumFacing.DOWN, MC_DirectionNESWUD.DOWN);
        directionMap.put(EnumFacing.UP, MC_DirectionNESWUD.UP);
        directionMap.put(EnumFacing.NORTH, MC_DirectionNESWUD.NORTH);
        directionMap.put(EnumFacing.SOUTH, MC_DirectionNESWUD.SOUTH);
        directionMap.put(EnumFacing.WEST, MC_DirectionNESWUD.WEST);
        directionMap.put(EnumFacing.EAST, MC_DirectionNESWUD.EAST);

        gameRuleMap.put(MC_GameRuleType.DO_FIRE_TICK, "doFireTick");
        gameRuleMap.put(MC_GameRuleType.MOB_GRIEFING, "mobGriefing");
        gameRuleMap.put(MC_GameRuleType.KEEP_INVENTORY, "keepInventory");
        gameRuleMap.put(MC_GameRuleType.DO_MOB_SPAWNING, "doMobSpawning");
        gameRuleMap.put(MC_GameRuleType.DO_MOB_LOOT, "doMobLoot");
        gameRuleMap.put(MC_GameRuleType.DO_TILE_DROPS, "doTileDrops");
        gameRuleMap.put(MC_GameRuleType.DO_ENTITY_DROPS, "doEntityDrops");
        gameRuleMap.put(MC_GameRuleType.COMMAND_BLOCK_OUTPUT, "commandBlockOutput");
        gameRuleMap.put(MC_GameRuleType.NATURAL_REGENERATION, "naturalRegeneration");
        gameRuleMap.put(MC_GameRuleType.DO_DAYLIGHT_CYCLE, "doDaylightCycle");
        gameRuleMap.put(MC_GameRuleType.LOG_ADMIN_COMMANDS, "logAdminCommands");
        gameRuleMap.put(MC_GameRuleType.SHOW_DEATH_MESSAGES, "showDeathMessages");
        gameRuleMap.put(MC_GameRuleType.SEND_COMMAND_FEEDBACK, "sendCommandFeedback");
        gameRuleMap.put(MC_GameRuleType.REDUCED_DEBUG_INFO, "reducedDebugInfo");
        gameRuleMap.put(MC_GameRuleType.SPECTATORS_GENERATE_CHUNKS, "spectatorsGenerateChunks");
        gameRuleMap.put(MC_GameRuleType.DISABLE_ELYTRA_MOVEMENT_CHECK, "disableElytraMovementCheck");

        gamemodeMap.put(WorldSettings.GameType.NOT_SET, MC_GameMode.UNKNOWN);
        gamemodeMap.put(WorldSettings.GameType.SURVIVAL, MC_GameMode.SURVIVAL);
        gamemodeMap.put(WorldSettings.GameType.CREATIVE, MC_GameMode.CREATIVE);
        gamemodeMap.put(WorldSettings.GameType.ADVENTURE, MC_GameMode.ADVENTURE);
        gamemodeMap.put(WorldSettings.GameType.SPECTATOR, MC_GameMode.SPECTATOR);

        entityMap.put(EntityItem.class, MC_EntityType.ITEM);
        entityMap.put(EntityXPOrb.class, MC_EntityType.XP_ORB);
        // todo entityMap.put(EntityAreaEffectCloud.class, );
        entityMap.put(EntityEgg.class, MC_EntityType.THROWN_EGG);
        // todo entityMap.put(EntityLeashKnot.class, );
        // todo entityMap.put(EntityPainting.class, MC_EntityType.HANGING);
        entityMap.put(EntityTippedArrow.class, MC_EntityType.ARROW);
        entityMap.put(EntitySnowball.class, MC_EntityType.SNOWBALL);
        entityMap.put(EntityLargeFireball.class, MC_EntityType.FIREBALL);
        entityMap.put(EntitySmallFireball.class, MC_EntityType.SMALL_FIREBALL);
        entityMap.put(EntityEnderPearl.class, MC_EntityType.THROWN_ENDERPEARL);
        entityMap.put(EntityEnderEye.class, MC_EntityType.EYE_OF_ENDER_SIGNAL);
        entityMap.put(EntityPotion.class, MC_EntityType.THROWN_POTION);
        entityMap.put(EntityExpBottle.class, MC_EntityType.THROWN_EXP_BOTTLE);
        // todo entityMap.put(EntityItemFrame.class, MC_EntityType.HANGING);
        // todo entityMap.put(EntityWitherSkull.class, );
        entityMap.put(EntityTNTPrimed.class, MC_EntityType.PRIMED_TNT);
        entityMap.put(EntityFallingBlock.class, MC_EntityType.FALLING_SAND);
        entityMap.put(EntityFireworkRocket.class, MC_EntityType.FIREWORK);
        // todo entityMap.put(EntitySpectralArrow.class, );
        // todo entityMap.put(EntityShulkerBullet.class, );
        // todo entityMap.put(EntityDragonFireball.class, );
        entityMap.put(EntityArmorStand.class, MC_EntityType.ARMOR_STAND);
        entityMap.put(EntityBoat.class, MC_EntityType.BOAT);
        entityMap.put(EntityMinecartEmpty.class, MC_EntityType.MINECART);
        // todo entityMap.put(EntityMinecartChest.class, EntityMinecart.EnumMinecartType.CHEST.getName(), 43);
        // todo entityMap.put(EntityMinecartFurnace.class, EntityMinecart.EnumMinecartType.FURNACE.getName(), 44);
        // todo entityMap.put(EntityMinecartTNT.class, EntityMinecart.EnumMinecartType.TNT.getName(), 45);
        // todo entityMap.put(EntityMinecartHopper.class, EntityMinecart.EnumMinecartType.HOPPER.getName(), 46);
        // todo entityMap.put(EntityMinecartMobSpawner.class, EntityMinecart.EnumMinecartType.SPAWNER.getName(), 47);
        // todo entityMap.put(EntityMinecartCommandBlock.class, EntityMinecart.EnumMinecartType.COMMAND_BLOCK.getName(), 40);
        entityMap.put(EntityCreeper.class, MC_EntityType.CREEPER);
        entityMap.put(EntitySkeleton.class, MC_EntityType.SKELETON);
        entityMap.put(EntitySpider.class, MC_EntityType.SPIDER);
        entityMap.put(EntityGiant.class, MC_EntityType.GIANT);
        entityMap.put(EntityZombie.class, MC_EntityType.ZOMBIE);
        entityMap.put(EntitySlime.class, MC_EntityType.SLIME);
        entityMap.put(EntityGhast.class, MC_EntityType.GHAST);
        entityMap.put(EntityPigZombie.class, MC_EntityType.PIG_ZOMBIE);
        entityMap.put(EntityEnderman.class, MC_EntityType.ENDERMAN);
        entityMap.put(EntityCaveSpider.class, MC_EntityType.CAVE_SPIDER);
        entityMap.put(EntitySilverfish.class, MC_EntityType.SILVERFISH);
        entityMap.put(EntityBlaze.class, MC_EntityType.BLAZE);
        entityMap.put(EntityLavaSlime.class, MC_EntityType.LAVA_SLIME);
        entityMap.put(EntityDragon.class, MC_EntityType.ENDERDRAGON);
        entityMap.put(EntityWither.class, MC_EntityType.WITHERBOSS);
        entityMap.put(EntityBat.class, MC_EntityType.BAT);
        entityMap.put(EntityWitch.class, MC_EntityType.WITCH);
        entityMap.put(EntityEndermite.class, MC_EntityType.ENDERMITE);
        entityMap.put(EntityGuardian.class, MC_EntityType.GUARDIAN);
        // todo entityMap.put(EntityShulker.class, );
        entityMap.put(EntityPig.class, MC_EntityType.PIG);
        entityMap.put(EntitySheep.class, MC_EntityType.SHEEP);
        entityMap.put(EntityCow.class, MC_EntityType.COW);
        entityMap.put(EntityChicken.class, MC_EntityType.CHICKEN);
        entityMap.put(EntitySquid.class, MC_EntityType.SQUID);
        entityMap.put(EntityWolf.class, MC_EntityType.WOLF);
        entityMap.put(EntityMushroomCow.class, MC_EntityType.MUSHROOM_COW);
        entityMap.put(EntitySnowman.class, MC_EntityType.SNOWMAN);
        entityMap.put(EntityOzelot.class, MC_EntityType.OCELOT);
        entityMap.put(EntityVillagerGolem.class, MC_EntityType.VILLAGER_GOLEM);
        entityMap.put(EntityHorse.class, MC_EntityType.HORSE);
        entityMap.put(EntityRabbit.class, MC_EntityType.RABBIT);
        entityMap.put(EntityVillager.class, MC_EntityType.VILLAGER);
        entityMap.put(EntityEnderCrystal.class, MC_EntityType.ENDER_CRYSTAL);
        entityMap.put(EntityFishHook.class, MC_EntityType.FISHING_HOOK);

        potionMap.put(rm.a, MC_PotionEffectType.SPEED);
        potionMap.put(rm.b, MC_PotionEffectType.SLOWNESS);
        potionMap.put(rm.c, MC_PotionEffectType.HASTE);
        potionMap.put(rm.d, MC_PotionEffectType.MINING_FATIGUE);
        potionMap.put(rm.e, MC_PotionEffectType.STRENGTH);
        potionMap.put(rm.f, MC_PotionEffectType.INSTANT_HEALTH);
        potionMap.put(rm.g, MC_PotionEffectType.INSTANT_DAMAGE);
        potionMap.put(rm.h, MC_PotionEffectType.JUMP_BOOST);
        potionMap.put(rm.i, MC_PotionEffectType.NAUSEA);
        potionMap.put(rm.j, MC_PotionEffectType.REGENERATION);
        potionMap.put(rm.k, MC_PotionEffectType.RESISTANCE);
        potionMap.put(rm.l, MC_PotionEffectType.FIRE_RESISTANCE);
        potionMap.put(rm.m, MC_PotionEffectType.WATER_BREATHING);
        potionMap.put(rm.n, MC_PotionEffectType.INVISIBILITY);
        potionMap.put(rm.o, MC_PotionEffectType.BLINDNESS);
        potionMap.put(rm.p, MC_PotionEffectType.NIGHT_VISION);
        potionMap.put(rm.q, MC_PotionEffectType.HUNGER);
        potionMap.put(rm.r, MC_PotionEffectType.WEAKNESS);
        potionMap.put(rm.s, MC_PotionEffectType.POISON);
        potionMap.put(rm.t, MC_PotionEffectType.WITHER);
        potionMap.put(rm.u, MC_PotionEffectType.HEALTH_BOOST);
        potionMap.put(rm.v, MC_PotionEffectType.ABSORPTION);
        potionMap.put(rm.w, MC_PotionEffectType.SATURATION);
        potionMap.put(rm.x, MC_PotionEffectType.GLOWING);
        potionMap.put(rm.y, MC_PotionEffectType.LEVITATION);
        potionMap.put(rm.z, MC_PotionEffectType.LUCK);

        enchantmentMap.put((short) 0, MC_EnchantmentType.PROTECTION);
        enchantmentMap.put((short) 1, MC_EnchantmentType.FIRE_PROTECTION);
        enchantmentMap.put((short) 2, MC_EnchantmentType.FEATHER_FALLING);
        enchantmentMap.put((short) 3, MC_EnchantmentType.BLAST_PROTECTION);
        enchantmentMap.put((short) 4, MC_EnchantmentType.PROJECTILE_PROTECTION);
        enchantmentMap.put((short) 5, MC_EnchantmentType.RESPIRATION);
        enchantmentMap.put((short) 6, MC_EnchantmentType.AQUA_AFFINITY);
        enchantmentMap.put((short) 7, MC_EnchantmentType.THORNS);
        enchantmentMap.put((short) 8, MC_EnchantmentType.DEPTH_STRIDER);
        // todo enchantmentMap.put(9, MC_EnchantmentType.FROST_WALKER);
        enchantmentMap.put((short) 16, MC_EnchantmentType.SHARPNESS);
        enchantmentMap.put((short) 17, MC_EnchantmentType.SMITE);
        enchantmentMap.put((short) 18, MC_EnchantmentType.BANE_OF_ARTHROPODS);
        enchantmentMap.put((short) 19, MC_EnchantmentType.KNOCKBACK);
        enchantmentMap.put((short) 20, MC_EnchantmentType.FIRE_ASPECT);
        enchantmentMap.put((short) 21, MC_EnchantmentType.LOOTING);
        enchantmentMap.put((short) 32, MC_EnchantmentType.EFFICIENCY);
        enchantmentMap.put((short) 33, MC_EnchantmentType.SILK_TOUCH);
        enchantmentMap.put((short) 34, MC_EnchantmentType.UNBREAKING);
        enchantmentMap.put((short) 35, MC_EnchantmentType.FORTUNE);
        enchantmentMap.put((short) 48, MC_EnchantmentType.POWER);
        enchantmentMap.put((short) 49, MC_EnchantmentType.PUNCH);
        enchantmentMap.put((short) 50, MC_EnchantmentType.FLAME);
        enchantmentMap.put((short) 51, MC_EnchantmentType.INFINITY);
        enchantmentMap.put((short) 61, MC_EnchantmentType.LUCK_OF_THE_SEA);
        enchantmentMap.put((short) 62, MC_EnchantmentType.LURE);
        // todo enchantmentMap.put(70, MC_EnchantmentType.MENDING);

        biomeMap.put(BiomeGenBase.getBiome(0), MC_WorldBiomeType.OCEAN);
        biomeMap.put(BiomeGenBase.getBiome(1), MC_WorldBiomeType.PLAINS);
        biomeMap.put(BiomeGenBase.getBiome(2), MC_WorldBiomeType.DESERT);
        biomeMap.put(BiomeGenBase.getBiome(3), MC_WorldBiomeType.EXTREME_HILLS);
        biomeMap.put(BiomeGenBase.getBiome(4), MC_WorldBiomeType.FOREST);
        biomeMap.put(BiomeGenBase.getBiome(5), MC_WorldBiomeType.TAIGA);
        biomeMap.put(BiomeGenBase.getBiome(6), MC_WorldBiomeType.SWAMPLAND);
        biomeMap.put(BiomeGenBase.getBiome(7), MC_WorldBiomeType.RIVER);
        biomeMap.put(BiomeGenBase.getBiome(8), MC_WorldBiomeType.HELL);
        biomeMap.put(BiomeGenBase.getBiome(9), MC_WorldBiomeType.THE_END);
        biomeMap.put(BiomeGenBase.getBiome(10), MC_WorldBiomeType.FROZEN_OCEAN);
        biomeMap.put(BiomeGenBase.getBiome(11), MC_WorldBiomeType.FROZEN_RIVER);
        biomeMap.put(BiomeGenBase.getBiome(12), MC_WorldBiomeType.ICE_PLAINS);
        biomeMap.put(BiomeGenBase.getBiome(13), MC_WorldBiomeType.ICE_MOUNTAINS);
        biomeMap.put(BiomeGenBase.getBiome(14), MC_WorldBiomeType.MUSHROOM_ISLAND);
        biomeMap.put(BiomeGenBase.getBiome(15), MC_WorldBiomeType.MUSHROOM_ISLAND_SHORE);
        biomeMap.put(BiomeGenBase.getBiome(16), MC_WorldBiomeType.BEACH);
        biomeMap.put(BiomeGenBase.getBiome(17), MC_WorldBiomeType.DESERT_HILLS);
        biomeMap.put(BiomeGenBase.getBiome(18), MC_WorldBiomeType.FOREST_HILLS);
        biomeMap.put(BiomeGenBase.getBiome(19), MC_WorldBiomeType.TAIGA_HILLS);
        biomeMap.put(BiomeGenBase.getBiome(20), MC_WorldBiomeType.EXTREME_HILLS_EDGE);
        biomeMap.put(BiomeGenBase.getBiome(21), MC_WorldBiomeType.JUNGLE);
        biomeMap.put(BiomeGenBase.getBiome(22), MC_WorldBiomeType.JUNGLE_HILLS);
        biomeMap.put(BiomeGenBase.getBiome(23), MC_WorldBiomeType.JUNGLE_EDGE);
        biomeMap.put(BiomeGenBase.getBiome(24), MC_WorldBiomeType.DEEP_OCEAN);
        biomeMap.put(BiomeGenBase.getBiome(25), MC_WorldBiomeType.STONE_BEACH);
        biomeMap.put(BiomeGenBase.getBiome(26), MC_WorldBiomeType.COLD_BEACH);
        biomeMap.put(BiomeGenBase.getBiome(27), MC_WorldBiomeType.BIRCH_FOREST);
        biomeMap.put(BiomeGenBase.getBiome(28), MC_WorldBiomeType.BIRCH_FOREST_HILLS);
        biomeMap.put(BiomeGenBase.getBiome(29), MC_WorldBiomeType.ROOFED_FOREST);
        biomeMap.put(BiomeGenBase.getBiome(30), MC_WorldBiomeType.COLD_TAIGA);
        biomeMap.put(BiomeGenBase.getBiome(31), MC_WorldBiomeType.COLD_TAIGA_HILLS);
        biomeMap.put(BiomeGenBase.getBiome(32), MC_WorldBiomeType.MEGA_TAIGA);
        biomeMap.put(BiomeGenBase.getBiome(33), MC_WorldBiomeType.MEGA_TAIGA_HILLS);
        biomeMap.put(BiomeGenBase.getBiome(34), MC_WorldBiomeType.EXTREME_HILLS_PLUS);
        biomeMap.put(BiomeGenBase.getBiome(35), MC_WorldBiomeType.SAVANNA);
        biomeMap.put(BiomeGenBase.getBiome(36), MC_WorldBiomeType.SAVANNA_PLATEAU);
        biomeMap.put(BiomeGenBase.getBiome(37), MC_WorldBiomeType.MESA);
        biomeMap.put(BiomeGenBase.getBiome(38), MC_WorldBiomeType.MESA_PLATEAU_F);
        biomeMap.put(BiomeGenBase.getBiome(39), MC_WorldBiomeType.MESA_PLATEAU);
        // todo add new biomes
    }

    public static MC_Block getBlockFromName(String blockName) {
        if (blockName == null) {
            return null;
        } else if (blockName.length() <= 0) {
            return null;
        } else {
            if (blockName.equalsIgnoreCase("white")) {
                blockName = "wool";
            }

            Block bo = Block.getBlockFromName(blockName.toLowerCase());

            if (bo != null) {
                return new BlockWrapper(bo.getDefaultState());
            } else {
                Iterator blkID = BlockHelper.mapItemNames.entrySet().iterator();

                while (blkID.hasNext()) {
                    Map.Entry idxColon = (Map.Entry) blkID.next();
                    String subType = (String) idxColon.getValue();

                    if (blockName.equalsIgnoreCase(subType)) {
                        blockName = (String) idxColon.getKey();
                        break;
                    }

                    while (subType.contains(" ")) {
                        subType = subType.replace(" ", "_");
                    }

                    if (subType.equalsIgnoreCase(blockName)
                            || subType.equalsIgnoreCase(blockName + "_wool")) {
                        blockName = (String) idxColon.getKey();
                        break;
                    }
                }

                int idxColon1 = blockName.indexOf(58);
                boolean blkID1 = true;
                int subType1 = 0;
                String realBlockName;
                int blkID2;

                if (idxColon1 < 0) {
                    try {
                        blkID2 = Integer.parseInt(blockName);
                    } catch (Exception var8) {
                        blkID2 = -1;
                    }
                } else {
                    try {
                        realBlockName = blockName.substring(0, idxColon1);
                        String bw = blockName.substring(idxColon1 + 1);

                        blkID2 = Integer.parseInt(realBlockName);
                        subType1 = Integer.parseInt(bw);
                    } catch (Exception var7) {
                        blkID2 = -1;
                        subType1 = -1;
                    }
                }

                if (blkID2 < 0) {
                    return null;
                } else if (subType1 < 0) {
                    return null;
                } else {
                    realBlockName = (String) BlockHelper.mapBlockNames.get(
                            Integer.valueOf(blkID2));
                    if (realBlockName != null) {
                        bo = Block.getBlockFromName(realBlockName.toLowerCase());
                        if (bo != null) {
                            BlockWrapper bw1 = new BlockWrapper(bo.getDefaultState());

                            if (subType1 != 0) {
                                bw1.setSubtype(subType1);
                            }

                            return bw1;
                        }
                    }
                    return null;
                }
            }
        }
    }

    public static MC_DamageType wrap(DamageSource damageSource) {
        if (damageSource == null) {
            return MC_DamageType.UNSPECIFIED;
        } else if (damageSource == DamageSource.anvil) {
            return MC_DamageType.ANVIL;
        } else if (damageSource == DamageSource.cactus) {
            return MC_DamageType.CACTUS;
        } else if (damageSource == DamageSource.drown) {
            return MC_DamageType.DROWN;
        } else if (damageSource == DamageSource.fall) {
            return MC_DamageType.FALL;
        } else if (damageSource == DamageSource.fallingBlock) {
            return MC_DamageType.FALLING_BLOCK;
        } else if (damageSource == DamageSource.p) {
            return MC_DamageType.GENERIC;
        } else if (damageSource == DamageSource.inFire) {
            return MC_DamageType.IN_FIRE;
        } else if (damageSource == DamageSource.inWall) {
            return MC_DamageType.IN_WALL;
        } else if (damageSource == DamageSource.lava) {
            return MC_DamageType.LAVA;
        } else if (damageSource == DamageSource.lightningBolt) {
            return MC_DamageType.LIGHTING_BOLT;
        } else if (damageSource == DamageSource.magic) {
            return MC_DamageType.MAGIC;
        } else if (damageSource == DamageSource.onFire) {
            return MC_DamageType.ON_FIRE;
        } else if (damageSource == DamageSource.generic) {
            return MC_DamageType.OUT_OF_WORLD;
        } else if (damageSource == DamageSource.starve) {
            return MC_DamageType.STARVE;
        } else if (damageSource == DamageSource.anvil) {
            return MC_DamageType.WITHER;
        } else {
            if (damageSource.getDamageType() != null) {
                if (damageSource.getDamageType().equalsIgnoreCase("mob")) {
                    return MC_DamageType.MOB;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("player")) {
                    return MC_DamageType.PLAYER;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("arrow")) {
                    return MC_DamageType.ARROW;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("onFire")) {
                    return MC_DamageType.ON_FIRE;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("fireball")) {
                    return MC_DamageType.FIREBALL;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("thrown")) {
                    return MC_DamageType.THROWN;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("indirectMagic")) {
                    return MC_DamageType.INDIRECT_MAGIC;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("thorns")) {
                    return MC_DamageType.THORNS;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("explosion.player")) {
                    return MC_DamageType.EXPLOSION_PLAYER;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("explosion")) {
                    return MC_DamageType.EXPLOSION;
                }
            }

            return MC_DamageType.UNSPECIFIED;
        }
    }

    public static DamageSource unwrap(MC_DamageType cause) {
        return cause == null
                ? DamageSource.magic
                : (cause == MC_DamageType.ANVIL
                ? DamageSource.fallingBlock
                : (cause == MC_DamageType.CACTUS
                ? DamageSource.cactus
                : (cause == MC_DamageType.DROWN
                ? DamageSource.drown
                : (cause == MC_DamageType.FALL
                ? DamageSource.fall
                : (cause
                == MC_DamageType.FALLING_BLOCK
                ? DamageSource.p
                : (cause
                == MC_DamageType.GENERIC
                ? DamageSource.magic
                : (cause
                == MC_DamageType.IN_FIRE
                ? DamageSource.inFire
                : (cause
                == MC_DamageType.IN_WALL
                ? DamageSource.inWall
                : (cause
                == MC_DamageType.LAVA
                ? DamageSource.lava
                : (cause
                == MC_DamageType.LIGHTING_BOLT
                ? DamageSource.lightningBolt
                : (cause
                == MC_DamageType.MAGIC
                ? DamageSource.wither
                : (cause
                == MC_DamageType.ON_FIRE
                ? DamageSource.onFire
                : (cause
                == MC_DamageType.OUT_OF_WORLD
                ? DamageSource.generic
                : (cause
                == MC_DamageType.STARVE
                ? DamageSource.starve
                : (cause
                == MC_DamageType.WITHER
                ? DamageSource.anvil
                : (cause
                == MC_DamageType.MOB
                ? DamageSource.causeMobDamage(null)
                : (cause
                == MC_DamageType.PLAYER
                ? DamageSource.causePlayerDamage(null)
                : (cause
                == MC_DamageType.ARROW
                ? DamageSource.causeArrowDamage(null, null)
                : (cause
                == MC_DamageType.FIREBALL
                ? DamageSource.causeFireballDamage(null, null)
                : (cause
                == MC_DamageType.THROWN
                ? DamageSource.causeThrownDamage(null, null)
                : (cause
                == MC_DamageType.INDIRECT_MAGIC
                ? DamageSource.causeIndirectMagicDamage(null, null)
                : (cause
                == MC_DamageType.THORNS
                ? DamageSource.causeThornsDamage(null)
                : (cause
                == MC_DamageType.EXPLOSION
                ? DamageSource.setExplosionSource(null)
                : (cause
                == MC_DamageType.EXPLOSION_PLAYER
                ? DamageSource.b(null)
                : DamageSource.generic))))))))))))))))))))))));

    }
}
