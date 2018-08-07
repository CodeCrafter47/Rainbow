package org.projectrainbow;

import PluginReference.*;
import com.google.common.base.MoreObjects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameType;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class PluginHelper {
    public static BiMap<EnumFacing, MC_DirectionNESWUD> directionMap = HashBiMap.create();
    public static BiMap<MC_GameRuleType, String> gameRuleMap = HashBiMap.create();
    public static BiMap<GameType, MC_GameMode> gamemodeMap = HashBiMap.create();
    public static BiMap<Class<? extends Entity>, MC_EntityType> entityMap = HashBiMap.create();
    public static BiMap<Potion, MC_PotionEffectType> potionMap = HashBiMap.create();
    public static BiMap<Enchantment, MC_EnchantmentType> enchantmentMap = HashBiMap.create();
    public static BiMap<Biome, MC_WorldBiomeType> biomeMap = HashBiMap.create();
    public static BiMap<EnumHand, MC_Hand> handMap = HashBiMap.create();
    public static BiMap<MC_AttributeType, IAttribute> attributeMap = HashBiMap.create();
    public static BiMap<MC_AttributeModifier.Operator, Integer> operatorMap = HashBiMap.create();

    public static MC_EntityType getEntityType(Class<? extends Entity> clazz) {
        if (EntityPlayer.class.isAssignableFrom(clazz)) {
            return MC_EntityType.PLAYER;
        } else {
            return MoreObjects.firstNonNull(entityMap.get(clazz), MC_EntityType.UNSPECIFIED);
        }
    }

    public static List<MC_ItemStack> copyInvList(List<ItemStack> items) {
        int size = items.size();
        List<MC_ItemStack> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ItemStack itemStack = items.get(i);
            list.add((MC_ItemStack) (Object) itemStack);
        }
        return list;
    }

    public static ItemStack getItemStack(MC_ItemStack itemStack) {
        return (ItemStack) (itemStack == null ? ItemStack.EMPTY : itemStack);
    }

    public static void updateInv(List<ItemStack> inv, List<MC_ItemStack> items) {
        int size = inv.size();
        for (int i = 0; i < size; i++) {
            inv.set(i, getItemStack(items.get(i)));
        }
    }

    public static MC_PotionEffect wrap(PotionEffect potionEffect) {
        MC_PotionEffectType type = potionMap.get(potionEffect.getPotion());
        type = MoreObjects.firstNonNull(type, MC_PotionEffectType.UNSPECIFIED);
        MC_PotionEffect mc_potionEffect = new MC_PotionEffect(type, potionEffect.getDuration(), potionEffect.getAmplifier());
        mc_potionEffect.ambient = potionEffect.getIsAmbient();
        mc_potionEffect.showsParticles = potionEffect.doesShowParticles();
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
        gameRuleMap.put(MC_GameRuleType.DO_WEATHER_CYCLE, "doWeatherCycle");
        gameRuleMap.put(MC_GameRuleType.DO_LIMITED_CRAFTING, "doLimitedCrafting");
        gameRuleMap.put(MC_GameRuleType.ANNOUNCE_ADVANCEMENTS, "announceAdvancements");

        gamemodeMap.put(GameType.NOT_SET, MC_GameMode.UNKNOWN);
        gamemodeMap.put(GameType.SURVIVAL, MC_GameMode.SURVIVAL);
        gamemodeMap.put(GameType.CREATIVE, MC_GameMode.CREATIVE);
        gamemodeMap.put(GameType.ADVENTURE, MC_GameMode.ADVENTURE);
        gamemodeMap.put(GameType.SPECTATOR, MC_GameMode.SPECTATOR);

        entityMap.put(EntityItem.class, MC_EntityType.ITEM);
        entityMap.put(EntityXPOrb.class, MC_EntityType.XP_ORB);
        entityMap.put(EntityAreaEffectCloud.class, MC_EntityType.AREA_EFFECT_CLOUD);
        entityMap.put(EntityEgg.class, MC_EntityType.THROWN_EGG);
        entityMap.put(EntityTippedArrow.class, MC_EntityType.ARROW);
        entityMap.put(EntitySnowball.class, MC_EntityType.SNOWBALL);
        entityMap.put(EntityLargeFireball.class, MC_EntityType.FIREBALL);
        entityMap.put(EntitySmallFireball.class, MC_EntityType.SMALL_FIREBALL);
        entityMap.put(EntityEnderPearl.class, MC_EntityType.THROWN_ENDERPEARL);
        entityMap.put(EntityEnderEye.class, MC_EntityType.EYE_OF_ENDER_SIGNAL);
        entityMap.put(EntityPotion.class, MC_EntityType.THROWN_POTION);
        entityMap.put(EntityExpBottle.class, MC_EntityType.THROWN_EXP_BOTTLE);
        entityMap.put(EntityWitherSkull.class, MC_EntityType.WITHER_SKULL);
        entityMap.put(EntityTNTPrimed.class, MC_EntityType.PRIMED_TNT);
        entityMap.put(EntityFallingBlock.class, MC_EntityType.FALLING_SAND);
        entityMap.put(EntityFireworkRocket.class, MC_EntityType.FIREWORK);
        entityMap.put(EntitySpectralArrow.class, MC_EntityType.SPECTRAL_ARROW);
        entityMap.put(EntityShulkerBullet.class, MC_EntityType.SHULKER_BULLET);
        entityMap.put(EntityDragonFireball.class, MC_EntityType.DRAGON_FIREBALL);
        entityMap.put(EntityArmorStand.class, MC_EntityType.ARMOR_STAND);
        entityMap.put(EntityBoat.class, MC_EntityType.BOAT);
        entityMap.put(EntityCreeper.class, MC_EntityType.CREEPER);
        entityMap.put(EntitySkeleton.class, MC_EntityType.SKELETON);
        entityMap.put(EntityWitherSkeleton.class, MC_EntityType.WHITHER_SKELETON);
        entityMap.put(EntityStray.class, MC_EntityType.STRAY);
        entityMap.put(EntitySpider.class, MC_EntityType.SPIDER);
        entityMap.put(EntityGiantZombie.class, MC_EntityType.GIANT);
        entityMap.put(EntityZombie.class, MC_EntityType.ZOMBIE);
        entityMap.put(EntityHusk.class, MC_EntityType.HUSK);
        entityMap.put(EntityZombieVillager.class, MC_EntityType.ZOMBIE_VILLAGER);
        entityMap.put(EntitySlime.class, MC_EntityType.SLIME);
        entityMap.put(EntityGhast.class, MC_EntityType.GHAST);
        entityMap.put(EntityPigZombie.class, MC_EntityType.PIG_ZOMBIE);
        entityMap.put(EntityEnderman.class, MC_EntityType.ENDERMAN);
        entityMap.put(EntityCaveSpider.class, MC_EntityType.CAVE_SPIDER);
        entityMap.put(EntitySilverfish.class, MC_EntityType.SILVERFISH);
        entityMap.put(EntityBlaze.class, MC_EntityType.BLAZE);
        entityMap.put(EntityMagmaCube.class, MC_EntityType.LAVA_SLIME);
        entityMap.put(EntityDragon.class, MC_EntityType.ENDERDRAGON);
        entityMap.put(EntityWither.class, MC_EntityType.WITHERBOSS);
        entityMap.put(EntityBat.class, MC_EntityType.BAT);
        entityMap.put(EntityWitch.class, MC_EntityType.WITCH);
        entityMap.put(EntityEndermite.class, MC_EntityType.ENDERMITE);
        entityMap.put(EntityGuardian.class, MC_EntityType.GUARDIAN);
        entityMap.put(EntityElderGuardian.class, MC_EntityType.ELDER_GUARDIAN);
        entityMap.put(EntityShulker.class, MC_EntityType.SHULKER);
        entityMap.put(EntityPig.class, MC_EntityType.PIG);
        entityMap.put(EntitySheep.class, MC_EntityType.SHEEP);
        entityMap.put(EntityCow.class, MC_EntityType.COW);
        entityMap.put(EntityChicken.class, MC_EntityType.CHICKEN);
        entityMap.put(EntitySquid.class, MC_EntityType.SQUID);
        entityMap.put(EntityWolf.class, MC_EntityType.WOLF);
        entityMap.put(EntityMooshroom.class, MC_EntityType.MUSHROOM_COW);
        entityMap.put(EntitySnowman.class, MC_EntityType.SNOWMAN);
        entityMap.put(EntityOcelot.class, MC_EntityType.OCELOT);
        entityMap.put(EntityIronGolem.class, MC_EntityType.VILLAGER_GOLEM);
        entityMap.put(EntityHorse.class, MC_EntityType.HORSE);
        entityMap.put(EntityZombieHorse.class, MC_EntityType.ZOMBIE_HORSE);
        entityMap.put(EntitySkeletonHorse.class, MC_EntityType.SKELETON_HORSE);
        entityMap.put(EntityDonkey.class, MC_EntityType.DONKEY);
        entityMap.put(EntityMule.class, MC_EntityType.MULE);
        entityMap.put(EntityLlama.class, MC_EntityType.LLAMA);
        entityMap.put(EntityRabbit.class, MC_EntityType.RABBIT);
        entityMap.put(EntityVillager.class, MC_EntityType.VILLAGER);
        entityMap.put(EntityEnderCrystal.class, MC_EntityType.ENDER_CRYSTAL);
        entityMap.put(EntityFishHook.class, MC_EntityType.FISHING_HOOK);
        entityMap.put(EntityItemFrame.class, MC_EntityType.ITEM_FRAME);
        entityMap.put(EntityPainting.class, MC_EntityType.PAINTING);
        entityMap.put(EntityLeashKnot.class, MC_EntityType.LEASH_KNOT);
        entityMap.put(EntityLightningBolt.class, MC_EntityType.LIGHTNING_BOLT);
        entityMap.put(EntityPolarBear.class, MC_EntityType.POLAR_BEAR);
        entityMap.put(EntityEvokerFangs.class, MC_EntityType.EVOCATION_FANGS);
        entityMap.put(EntityEvoker.class, MC_EntityType.EVOKER);
        entityMap.put(EntityVex.class, MC_EntityType.VEX);
        entityMap.put(EntityVindicator.class, MC_EntityType.VINDICATOR);
        entityMap.put(EntityMinecartEmpty.class, MC_EntityType.MINECART);
        entityMap.put(EntityMinecartChest.class, MC_EntityType.MINECART_CHEST);
        entityMap.put(EntityMinecartFurnace.class, MC_EntityType.MINECART_FURNACE);
        entityMap.put(EntityMinecartTNT.class, MC_EntityType.MINECART_TNT);
        entityMap.put(EntityMinecartHopper.class, MC_EntityType.MINECART_HOPPER);
        entityMap.put(EntityMinecartMobSpawner.class, MC_EntityType.MINECART_SPAWNER);
        entityMap.put(EntityParrot.class, MC_EntityType.PARROT);
        entityMap.put(EntityIllusionIllager.class, MC_EntityType.ILLUSIONER);
        entityMap.put(EntityCod.class, MC_EntityType.COD);
        entityMap.put(EntityDolphin.class, MC_EntityType.DOLPHIN);
        entityMap.put(EntityDrowned.class, MC_EntityType.DROWNED);
        entityMap.put(EntityLlamaSpit.class, MC_EntityType.LLAMA_SPIT);
        entityMap.put(EntityMinecartCommandBlock.class, MC_EntityType.MINECART_COMMAND_BLOCK);
        entityMap.put(EntityPufferFish.class, MC_EntityType.PUFFER_FISH);
        entityMap.put(EntitySalmon.class, MC_EntityType.SALMON);
        entityMap.put(EntityTropicalFish.class, MC_EntityType.TROPICAL_FISH);
        entityMap.put(EntityTurtle.class, MC_EntityType.TURTLE);
        entityMap.put(EntityPhantom.class, MC_EntityType.PHANTOM);
        entityMap.put(EntityTrident.class, MC_EntityType.TRIDENT);

        potionMap.put(MobEffects.SPEED, MC_PotionEffectType.SPEED);
        potionMap.put(MobEffects.SLOWNESS, MC_PotionEffectType.SLOWNESS);
        potionMap.put(MobEffects.HASTE, MC_PotionEffectType.HASTE);
        potionMap.put(MobEffects.MINING_FATIGUE, MC_PotionEffectType.MINING_FATIGUE);
        potionMap.put(MobEffects.STRENGTH, MC_PotionEffectType.STRENGTH);
        potionMap.put(MobEffects.INSTANT_HEALTH, MC_PotionEffectType.INSTANT_HEALTH);
        potionMap.put(MobEffects.INSTANT_DAMAGE, MC_PotionEffectType.INSTANT_DAMAGE);
        potionMap.put(MobEffects.JUMP_BOOST, MC_PotionEffectType.JUMP_BOOST);
        potionMap.put(MobEffects.NAUSEA, MC_PotionEffectType.NAUSEA);
        potionMap.put(MobEffects.REGENERATION, MC_PotionEffectType.REGENERATION);
        potionMap.put(MobEffects.RESISTANCE, MC_PotionEffectType.RESISTANCE);
        potionMap.put(MobEffects.FIRE_RESISTANCE, MC_PotionEffectType.FIRE_RESISTANCE);
        potionMap.put(MobEffects.WATER_BREATHING, MC_PotionEffectType.WATER_BREATHING);
        potionMap.put(MobEffects.INVISIBILITY, MC_PotionEffectType.INVISIBILITY);
        potionMap.put(MobEffects.BLINDNESS, MC_PotionEffectType.BLINDNESS);
        potionMap.put(MobEffects.NIGHT_VISION, MC_PotionEffectType.NIGHT_VISION);
        potionMap.put(MobEffects.HUNGER, MC_PotionEffectType.HUNGER);
        potionMap.put(MobEffects.WEAKNESS, MC_PotionEffectType.WEAKNESS);
        potionMap.put(MobEffects.POISON, MC_PotionEffectType.POISON);
        potionMap.put(MobEffects.WITHER, MC_PotionEffectType.WITHER);
        potionMap.put(MobEffects.HEALTH_BOOST, MC_PotionEffectType.HEALTH_BOOST);
        potionMap.put(MobEffects.ABSORPTION, MC_PotionEffectType.ABSORPTION);
        potionMap.put(MobEffects.SATURATION, MC_PotionEffectType.SATURATION);
        potionMap.put(MobEffects.GLOWING, MC_PotionEffectType.GLOWING);
        potionMap.put(MobEffects.LEVITATION, MC_PotionEffectType.LEVITATION);
        potionMap.put(MobEffects.LUCK, MC_PotionEffectType.LUCK);
        potionMap.put(MobEffects.UNLUCK, MC_PotionEffectType.UNLUCK);

        enchantmentMap.put(Enchantments.PROTECTION, MC_EnchantmentType.PROTECTION);
        enchantmentMap.put(Enchantments.FIRE_PROTECTION, MC_EnchantmentType.FIRE_PROTECTION);
        enchantmentMap.put(Enchantments.FEATHER_FALLING, MC_EnchantmentType.FEATHER_FALLING);
        enchantmentMap.put(Enchantments.BLAST_PROTECTION, MC_EnchantmentType.BLAST_PROTECTION);
        enchantmentMap.put(Enchantments.PROJECTILE_PROTECTION, MC_EnchantmentType.PROJECTILE_PROTECTION);
        enchantmentMap.put(Enchantments.RESPIRATION, MC_EnchantmentType.RESPIRATION);
        enchantmentMap.put(Enchantments.AQUA_AFFINITY, MC_EnchantmentType.AQUA_AFFINITY);
        enchantmentMap.put(Enchantments.THORNS, MC_EnchantmentType.THORNS);
        enchantmentMap.put(Enchantments.DEPTH_STRIDER, MC_EnchantmentType.DEPTH_STRIDER);
        enchantmentMap.put(Enchantments.FROST_WALKER, MC_EnchantmentType.FROST_WALKER);
        enchantmentMap.put(Enchantments.BINDING_CURSE, MC_EnchantmentType.CURSE_OF_BINDING);
        enchantmentMap.put(Enchantments.SHARPNESS, MC_EnchantmentType.SHARPNESS);
        enchantmentMap.put(Enchantments.SMITE, MC_EnchantmentType.SMITE);
        enchantmentMap.put(Enchantments.BANE_OF_ARTHROPODS, MC_EnchantmentType.BANE_OF_ARTHROPODS);
        enchantmentMap.put(Enchantments.KNOCKBACK, MC_EnchantmentType.KNOCKBACK);
        enchantmentMap.put(Enchantments.FIRE_ASPECT, MC_EnchantmentType.FIRE_ASPECT);
        enchantmentMap.put(Enchantments.LOOTING, MC_EnchantmentType.LOOTING);
        enchantmentMap.put(Enchantments.SWEEPING, MC_EnchantmentType.SWEEPING);
        enchantmentMap.put(Enchantments.EFFICIENCY, MC_EnchantmentType.EFFICIENCY);
        enchantmentMap.put(Enchantments.SILK_TOUCH, MC_EnchantmentType.SILK_TOUCH);
        enchantmentMap.put(Enchantments.UNBREAKING, MC_EnchantmentType.UNBREAKING);
        enchantmentMap.put(Enchantments.FORTUNE, MC_EnchantmentType.FORTUNE);
        enchantmentMap.put(Enchantments.POWER, MC_EnchantmentType.POWER);
        enchantmentMap.put(Enchantments.PUNCH, MC_EnchantmentType.PUNCH);
        enchantmentMap.put(Enchantments.FLAME, MC_EnchantmentType.FLAME);
        enchantmentMap.put(Enchantments.INFINITY, MC_EnchantmentType.INFINITY);
        enchantmentMap.put(Enchantments.LUCK_OF_THE_SEA, MC_EnchantmentType.LUCK_OF_THE_SEA);
        enchantmentMap.put(Enchantments.LURE, MC_EnchantmentType.LURE);
        enchantmentMap.put(Enchantments.MENDING, MC_EnchantmentType.MENDING);
        enchantmentMap.put(Enchantments.VANISHING_CURSE, MC_EnchantmentType.CURSE_OF_VANISHING);

        biomeMap.put(Biome.getBiome(0), MC_WorldBiomeType.OCEAN);
        biomeMap.put(Biome.getBiome(1), MC_WorldBiomeType.PLAINS);
        biomeMap.put(Biome.getBiome(2), MC_WorldBiomeType.DESERT);
        biomeMap.put(Biome.getBiome(3), MC_WorldBiomeType.EXTREME_HILLS);
        biomeMap.put(Biome.getBiome(4), MC_WorldBiomeType.FOREST);
        biomeMap.put(Biome.getBiome(5), MC_WorldBiomeType.TAIGA);
        biomeMap.put(Biome.getBiome(6), MC_WorldBiomeType.SWAMPLAND);
        biomeMap.put(Biome.getBiome(7), MC_WorldBiomeType.RIVER);
        biomeMap.put(Biome.getBiome(8), MC_WorldBiomeType.HELL);
        biomeMap.put(Biome.getBiome(9), MC_WorldBiomeType.THE_END);
        biomeMap.put(Biome.getBiome(10), MC_WorldBiomeType.FROZEN_OCEAN);
        biomeMap.put(Biome.getBiome(11), MC_WorldBiomeType.FROZEN_RIVER);
        biomeMap.put(Biome.getBiome(12), MC_WorldBiomeType.ICE_PLAINS);
        biomeMap.put(Biome.getBiome(13), MC_WorldBiomeType.ICE_MOUNTAINS);
        biomeMap.put(Biome.getBiome(14), MC_WorldBiomeType.MUSHROOM_ISLAND);
        biomeMap.put(Biome.getBiome(15), MC_WorldBiomeType.MUSHROOM_ISLAND_SHORE);
        biomeMap.put(Biome.getBiome(16), MC_WorldBiomeType.BEACH);
        biomeMap.put(Biome.getBiome(17), MC_WorldBiomeType.DESERT_HILLS);
        biomeMap.put(Biome.getBiome(18), MC_WorldBiomeType.FOREST_HILLS);
        biomeMap.put(Biome.getBiome(19), MC_WorldBiomeType.TAIGA_HILLS);
        biomeMap.put(Biome.getBiome(20), MC_WorldBiomeType.EXTREME_HILLS_EDGE);
        biomeMap.put(Biome.getBiome(21), MC_WorldBiomeType.JUNGLE);
        biomeMap.put(Biome.getBiome(22), MC_WorldBiomeType.JUNGLE_HILLS);
        biomeMap.put(Biome.getBiome(23), MC_WorldBiomeType.JUNGLE_EDGE);
        biomeMap.put(Biome.getBiome(24), MC_WorldBiomeType.DEEP_OCEAN);
        biomeMap.put(Biome.getBiome(25), MC_WorldBiomeType.STONE_BEACH);
        biomeMap.put(Biome.getBiome(26), MC_WorldBiomeType.COLD_BEACH);
        biomeMap.put(Biome.getBiome(27), MC_WorldBiomeType.BIRCH_FOREST);
        biomeMap.put(Biome.getBiome(28), MC_WorldBiomeType.BIRCH_FOREST_HILLS);
        biomeMap.put(Biome.getBiome(29), MC_WorldBiomeType.ROOFED_FOREST);
        biomeMap.put(Biome.getBiome(30), MC_WorldBiomeType.COLD_TAIGA);
        biomeMap.put(Biome.getBiome(31), MC_WorldBiomeType.COLD_TAIGA_HILLS);
        biomeMap.put(Biome.getBiome(32), MC_WorldBiomeType.MEGA_TAIGA);
        biomeMap.put(Biome.getBiome(33), MC_WorldBiomeType.MEGA_TAIGA_HILLS);
        biomeMap.put(Biome.getBiome(34), MC_WorldBiomeType.EXTREME_HILLS_PLUS);
        biomeMap.put(Biome.getBiome(35), MC_WorldBiomeType.SAVANNA);
        biomeMap.put(Biome.getBiome(36), MC_WorldBiomeType.SAVANNA_PLATEAU);
        biomeMap.put(Biome.getBiome(37), MC_WorldBiomeType.MESA);
        biomeMap.put(Biome.getBiome(38), MC_WorldBiomeType.MESA_PLATEAU_F);
        biomeMap.put(Biome.getBiome(39), MC_WorldBiomeType.MESA_PLATEAU);
        biomeMap.put(Biome.getBiome(40), MC_WorldBiomeType.END_SMALL_ISLANDS);
        biomeMap.put(Biome.getBiome(41), MC_WorldBiomeType.END_MIDLANDS);
        biomeMap.put(Biome.getBiome(42), MC_WorldBiomeType.END_HIGHLANDS);
        biomeMap.put(Biome.getBiome(43), MC_WorldBiomeType.END_BARRENS);
        biomeMap.put(Biome.getBiome(44), MC_WorldBiomeType.WARM_OCEAN);
        biomeMap.put(Biome.getBiome(45), MC_WorldBiomeType.LUKEWARM_OCEAN);
        biomeMap.put(Biome.getBiome(46), MC_WorldBiomeType.COLD_OCEAN);
        biomeMap.put(Biome.getBiome(47), MC_WorldBiomeType.DEEP_WARM_OCEAN);
        biomeMap.put(Biome.getBiome(48), MC_WorldBiomeType.DEEP_LUKEWARM_OCEAN);
        biomeMap.put(Biome.getBiome(49), MC_WorldBiomeType.DEEP_COLD_OCEAN);
        biomeMap.put(Biome.getBiome(50), MC_WorldBiomeType.DEEP_FROZEN_OCEAN);
        biomeMap.put(Biome.getBiome(127), MC_WorldBiomeType.VOID);
        biomeMap.put(Biome.getBiome(129), MC_WorldBiomeType.SUNFLOWER_PLAINS);
        biomeMap.put(Biome.getBiome(130), MC_WorldBiomeType.DESERT_M);
        biomeMap.put(Biome.getBiome(131), MC_WorldBiomeType.EXTREME_HILLS_M);
        biomeMap.put(Biome.getBiome(132), MC_WorldBiomeType.FLOWER_FOREST);
        biomeMap.put(Biome.getBiome(133), MC_WorldBiomeType.TAIGA_M);
        biomeMap.put(Biome.getBiome(134), MC_WorldBiomeType.SWAMPLAND_M);
        biomeMap.put(Biome.getBiome(140), MC_WorldBiomeType.ICE_PLAINS_SPIKES);
        biomeMap.put(Biome.getBiome(149), MC_WorldBiomeType.JUNGLE_M);
        biomeMap.put(Biome.getBiome(151), MC_WorldBiomeType.JUNGLE_EDGE_M);
        biomeMap.put(Biome.getBiome(155), MC_WorldBiomeType.BIRCH_FOREST_M);
        biomeMap.put(Biome.getBiome(156), MC_WorldBiomeType.BIRCH_FOREST_HILLS_M);
        biomeMap.put(Biome.getBiome(157), MC_WorldBiomeType.ROOFED_FOREST_M);
        biomeMap.put(Biome.getBiome(158), MC_WorldBiomeType.COLD_TAIGA_M);
        biomeMap.put(Biome.getBiome(160), MC_WorldBiomeType.MEGA_SPRUCE_TAIGA);
        biomeMap.put(Biome.getBiome(161), MC_WorldBiomeType.REDWOOD_TAIGA_HILLS_M);
        biomeMap.put(Biome.getBiome(162), MC_WorldBiomeType.EXTREME_HILLS_PLUS_M);
        biomeMap.put(Biome.getBiome(163), MC_WorldBiomeType.SAVANNA_M);
        biomeMap.put(Biome.getBiome(164), MC_WorldBiomeType.SAVANNA_PLATEAU_M);
        biomeMap.put(Biome.getBiome(165), MC_WorldBiomeType.MESA_M);
        biomeMap.put(Biome.getBiome(166), MC_WorldBiomeType.MESA_PLATEAU_F_M);
        biomeMap.put(Biome.getBiome(167), MC_WorldBiomeType.MESA_PLATEAU_M);

        handMap.put(EnumHand.MAIN_HAND, MC_Hand.MAIN_HAND);
        handMap.put(EnumHand.OFF_HAND, MC_Hand.OFF_HAND);

        attributeMap.put(MC_AttributeType.ARMOR, SharedMonsterAttributes.ARMOR);
        attributeMap.put(MC_AttributeType.ARMOR_TOUGHNESS, SharedMonsterAttributes.ARMOR_TOUGHNESS);
        attributeMap.put(MC_AttributeType.ATTACK_DAMAGE, SharedMonsterAttributes.ATTACK_DAMAGE);
        attributeMap.put(MC_AttributeType.FOLLOW_RANGE, SharedMonsterAttributes.FOLLOW_RANGE);
        attributeMap.put(MC_AttributeType.KNOCKBACK_RESISTANCE, SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        attributeMap.put(MC_AttributeType.MAX_HEALTH, SharedMonsterAttributes.MAX_HEALTH);
        attributeMap.put(MC_AttributeType.MOVEMENT_SPEED, SharedMonsterAttributes.MOVEMENT_SPEED);
        attributeMap.put(MC_AttributeType.PLAYER_ATTACK_SPEED, SharedMonsterAttributes.ATTACK_SPEED);
        attributeMap.put(MC_AttributeType.PLAYER_LUCK, SharedMonsterAttributes.LUCK);

        operatorMap.put(MC_AttributeModifier.Operator.ADD_CONSTANT, 0);
        operatorMap.put(MC_AttributeModifier.Operator.ADD_SCALAR_BASE, 1);
        operatorMap.put(MC_AttributeModifier.Operator.ADD_SCALAR, 2);
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
            }
            return null;
        }
    }

    public static MC_DamageType wrap(DamageSource damageSource) {
        if (damageSource == null) {
            return MC_DamageType.UNSPECIFIED;
        } else if (damageSource == DamageSource.ANVIL) {
            return MC_DamageType.ANVIL;
        } else if (damageSource == DamageSource.CACTUS) {
            return MC_DamageType.CACTUS;
        } else if (damageSource == DamageSource.DROWN) {
            return MC_DamageType.DROWN;
        } else if (damageSource == DamageSource.FALL) {
            return MC_DamageType.FALL;
        } else if (damageSource == DamageSource.FALLING_BLOCK) {
            return MC_DamageType.FALLING_BLOCK;
        } else if (damageSource == DamageSource.GENERIC) {
            return MC_DamageType.GENERIC;
        } else if (damageSource == DamageSource.IN_FIRE) {
            return MC_DamageType.IN_FIRE;
        } else if (damageSource == DamageSource.IN_WALL) {
            return MC_DamageType.IN_WALL;
        } else if (damageSource == DamageSource.LAVA) {
            return MC_DamageType.LAVA;
        } else if (damageSource == DamageSource.LIGHTNING_BOLT) {
            return MC_DamageType.LIGHTING_BOLT;
        } else if (damageSource == DamageSource.MAGIC) {
            return MC_DamageType.MAGIC;
        } else if (damageSource == DamageSource.ON_FIRE) {
            return MC_DamageType.ON_FIRE;
        } else if (damageSource == DamageSource.OUT_OF_WORLD) {
            return MC_DamageType.OUT_OF_WORLD;
        } else if (damageSource == DamageSource.STARVE) {
            return MC_DamageType.STARVE;
        } else if (damageSource == DamageSource.WITHER) {
            return MC_DamageType.WITHER;
        } else if (damageSource == DamageSource.DRAGON_BREATH) {
            return MC_DamageType.DRAGON_BREATH;
        } else if (damageSource == DamageSource.HOT_FLOOR) {
            return MC_DamageType.HOT_FLOOR;
        } else if (damageSource == DamageSource.CRAMMING) {
            return MC_DamageType.CRAMMING;
        } else if (damageSource == DamageSource.FLY_INTO_WALL) {
            return MC_DamageType.FLY_INTO_WALL;
        } else if (damageSource == DamageSource.FIREWORKS) {
            return MC_DamageType.FIREWORKS;
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
        if (cause == null) return DamageSource.GENERIC;
        else if (cause == MC_DamageType.ANVIL) return DamageSource.ANVIL;
        else if (cause == MC_DamageType.CACTUS) return DamageSource.CACTUS;
        else if (cause == MC_DamageType.DROWN) return DamageSource.DROWN;
        else if (cause == MC_DamageType.FALL) return DamageSource.FALL;
        else if (cause == MC_DamageType.FALLING_BLOCK) return DamageSource.FALLING_BLOCK;
        else if (cause == MC_DamageType.GENERIC) return DamageSource.GENERIC;
        else if (cause == MC_DamageType.IN_FIRE) return DamageSource.IN_FIRE;
        else if (cause == MC_DamageType.IN_WALL) return DamageSource.IN_WALL;
        else if (cause == MC_DamageType.LAVA) return DamageSource.LAVA;
        else if (cause == MC_DamageType.LIGHTING_BOLT) return DamageSource.LIGHTNING_BOLT;
        else if (cause == MC_DamageType.MAGIC) return DamageSource.MAGIC;
        else if (cause == MC_DamageType.ON_FIRE) return DamageSource.ON_FIRE;
        else if (cause == MC_DamageType.OUT_OF_WORLD) return DamageSource.OUT_OF_WORLD;
        else if (cause == MC_DamageType.STARVE) return DamageSource.STARVE;
        else if (cause == MC_DamageType.WITHER) return DamageSource.WITHER;
        else if (cause == MC_DamageType.DRAGON_BREATH) return DamageSource.DRAGON_BREATH;
        else if (cause == MC_DamageType.HOT_FLOOR) return DamageSource.HOT_FLOOR;
        else if (cause == MC_DamageType.CRAMMING) return DamageSource.CRAMMING;
        else if (cause == MC_DamageType.FLY_INTO_WALL) return DamageSource.FLY_INTO_WALL;
        else if (cause == MC_DamageType.FIREWORKS) return DamageSource.FIREWORKS;
        else if (cause == MC_DamageType.MOB) return DamageSource.causeMobDamage(null);
        else if (cause == MC_DamageType.PLAYER) return DamageSource.causePlayerDamage(null);
        else if (cause == MC_DamageType.ARROW) return DamageSource.causeArrowDamage(null, null);
        else if (cause == MC_DamageType.FIREBALL) return DamageSource.causeFireballDamage(null, null);
        else if (cause == MC_DamageType.THROWN) return DamageSource.causeThrownDamage(null, null);
        else if (cause == MC_DamageType.INDIRECT_MAGIC) return DamageSource.causeIndirectMagicDamage(null, null);
        else if (cause == MC_DamageType.THORNS) return DamageSource.causeThornsDamage(null);
        else if (cause == MC_DamageType.EXPLOSION) return DamageSource.causeExplosionDamage((EntityLivingBase) null);
        else if (cause == MC_DamageType.EXPLOSION_PLAYER) return DamageSource.causeExplosionDamage((Explosion) null);
        else return DamageSource.GENERIC;

    }
    
    public static Item getItemFromLegacyId(int id) {
        switch (id) {
            case 0:
                return Items.AIR;
            case 1:
                return Item.BLOCK_TO_ITEM.get(Blocks.STONE);
            case 2:
                return Item.BLOCK_TO_ITEM.get(Blocks.GRASS);
            case 3:
                return Item.BLOCK_TO_ITEM.get(Blocks.DIRT);
            case 4:
                return Item.BLOCK_TO_ITEM.get(Blocks.COBBLESTONE);
            case 5:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196662_n); // oak planks
            case 6:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196674_t); // oak sapling
            case 7:
                return Item.BLOCK_TO_ITEM.get(Blocks.BEDROCK);
            case 8:
                return Item.BLOCK_TO_ITEM.get(Blocks.WATER);
            case 9:
                return Item.BLOCK_TO_ITEM.get(Blocks.WATER);
            case 10:
                return Item.BLOCK_TO_ITEM.get(Blocks.LAVA);
            case 11:
                return Item.BLOCK_TO_ITEM.get(Blocks.LAVA);
            case 12:
                return Item.BLOCK_TO_ITEM.get(Blocks.SAND);
            case 13:
                return Item.BLOCK_TO_ITEM.get(Blocks.GRAVEL);
            case 14:
                return Item.BLOCK_TO_ITEM.get(Blocks.GOLD_ORE);
            case 15:
                return Item.BLOCK_TO_ITEM.get(Blocks.IRON_ORE);
            case 16:
                return Item.BLOCK_TO_ITEM.get(Blocks.COAL_ORE);
            case 17:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196617_K); // oak log
            case 18:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196642_W); // oak leaves
            case 19:
                return Item.BLOCK_TO_ITEM.get(Blocks.SPONGE);
            case 20:
                return Item.BLOCK_TO_ITEM.get(Blocks.GLASS);
            case 21:
                return Item.BLOCK_TO_ITEM.get(Blocks.LAPIS_ORE);
            case 22:
                return Item.BLOCK_TO_ITEM.get(Blocks.LAPIS_BLOCK);
            case 23:
                return Item.BLOCK_TO_ITEM.get(Blocks.DISPENSER);
            case 24:
                return Item.BLOCK_TO_ITEM.get(Blocks.SANDSTONE);
            case 25:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196586_al); // note block
            //case 26: return Item.BLOCK_TO_ITEM.get(Blocks.BED);
            case 27:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196552_aC); // powered rail
            case 28:
                return Item.BLOCK_TO_ITEM.get(Blocks.DETECTOR_RAIL);
            case 29:
                return Item.BLOCK_TO_ITEM.get(Blocks.STICKY_PISTON);
            case 30:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196553_aF); // web
            case 31:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196804_gh); // tall grass
            case 32:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196555_aI); // dead bush
            case 33:
                return Item.BLOCK_TO_ITEM.get(Blocks.PISTON);
            case 34:
                return Item.BLOCK_TO_ITEM.get(Blocks.PISTON_HEAD);
            case 35:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196556_aL); // white wool
            //case 36: return Item.BLOCK_TO_ITEM.get(Blocks.PISTON_EXTENSION);
            case 37:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196605_bc); // dandelion / yellow flower
            case 38:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196606_bd); // poppy / red flower
            case 39:
                return Item.BLOCK_TO_ITEM.get(Blocks.BROWN_MUSHROOM);
            case 40:
                return Item.BLOCK_TO_ITEM.get(Blocks.RED_MUSHROOM);
            case 41:
                return Item.BLOCK_TO_ITEM.get(Blocks.GOLD_BLOCK);
            case 42:
                return Item.BLOCK_TO_ITEM.get(Blocks.IRON_BLOCK);
            //case 43: return Item.BLOCK_TO_ITEM.get(Blocks.DOUBLE_STONE_SLAB);
            case 44:
                return Item.BLOCK_TO_ITEM.get(Blocks.STONE_SLAB);
            case 45:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196584_bK); // bricks
            case 46:
                return Item.BLOCK_TO_ITEM.get(Blocks.TNT);
            case 47:
                return Item.BLOCK_TO_ITEM.get(Blocks.BOOKSHELF);
            case 48:
                return Item.BLOCK_TO_ITEM.get(Blocks.MOSSY_COBBLESTONE);
            case 49:
                return Item.BLOCK_TO_ITEM.get(Blocks.OBSIDIAN);
            case 50:
                return Item.BLOCK_TO_ITEM.get(Blocks.TORCH);
            case 51:
                return Item.BLOCK_TO_ITEM.get(Blocks.FIRE);
            case 52:
                return Item.BLOCK_TO_ITEM.get(Blocks.MOB_SPAWNER);
            case 53:
                return Item.BLOCK_TO_ITEM.get(Blocks.OAK_STAIRS);
            case 54:
                return Item.BLOCK_TO_ITEM.get(Blocks.CHEST);
            case 55:
                return Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_WIRE);
            case 56:
                return Item.BLOCK_TO_ITEM.get(Blocks.DIAMOND_ORE);
            case 57:
                return Item.BLOCK_TO_ITEM.get(Blocks.DIAMOND_BLOCK);
            case 58:
                return Item.BLOCK_TO_ITEM.get(Blocks.CRAFTING_TABLE);
            //case 59: return Item.BLOCK_TO_ITEM.get(Blocks.WHEAT);
            case 60:
                return Item.BLOCK_TO_ITEM.get(Blocks.FARMLAND);
            case 61: // fall through
            case 62:
                return Item.BLOCK_TO_ITEM.get(Blocks.FURNACE);
            case 63:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196649_cc); // sign
            case 64:
                return Item.BLOCK_TO_ITEM.get(Blocks.OAK_DOOR);
            case 65:
                return Item.BLOCK_TO_ITEM.get(Blocks.LADDER);
            case 66:
                return Item.BLOCK_TO_ITEM.get(Blocks.RAIL);
            case 67:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196659_cl); // cobblestone stairs
            case 68:
                return Item.BLOCK_TO_ITEM.get(Blocks.WALL_SIGN);
            case 69:
                return Item.BLOCK_TO_ITEM.get(Blocks.LEVER);
            case 70:
                return Item.BLOCK_TO_ITEM.get(Blocks.STONE_PRESSURE_PLATE);
            //case 71: return Item.BLOCK_TO_ITEM.get(Blocks.IRON_DOOR);
            case 72:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196663_cq); // oak pressure plate
            case 73: // fall through
            case 74:
                return Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_ORE);
            case 75:
            case 76:
                return Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_TORCH);
            case 77:
                return Item.BLOCK_TO_ITEM.get(Blocks.STONE_BUTTON);
            case 78:
                return Item.BLOCK_TO_ITEM.get(Blocks.SNOW);
            case 79:
                return Item.BLOCK_TO_ITEM.get(Blocks.ICE);
            case 80:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196604_cC); // snow block
            case 81:
                return Item.BLOCK_TO_ITEM.get(Blocks.CACTUS);
            case 82:
                return Item.BLOCK_TO_ITEM.get(Blocks.CLAY);
            case 84:
                return Item.BLOCK_TO_ITEM.get(Blocks.JUKEBOX);
            case 85:
                return Item.BLOCK_TO_ITEM.get(Blocks.OAK_FENCE);
            case 86:
                return Item.BLOCK_TO_ITEM.get(Blocks.PUMPKIN);
            case 87:
                return Item.BLOCK_TO_ITEM.get(Blocks.NETHERRACK);
            case 88:
                return Item.BLOCK_TO_ITEM.get(Blocks.SOUL_SAND);
            case 89:
                return Item.BLOCK_TO_ITEM.get(Blocks.GLOWSTONE);
            case 90:
                return Item.BLOCK_TO_ITEM.get(Blocks.PORTAL);
            case 91:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196628_cT); // lit pumpkin
            //case 92: return Item.BLOCK_TO_ITEM.get(Blocks.CAKE);
            case 93:
            case 94:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196633_cV); // repeater
            case 95:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196807_gj); // white stained glass
            case 96:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196636_cW); // oak trapdoor
            case 97:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196686_dc); // infested stone
            case 98:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196696_di); // stone bricks
            case 99:
                return Item.BLOCK_TO_ITEM.get(Blocks.BROWN_MUSHROOM_BLOCK);
            case 100:
                return Item.BLOCK_TO_ITEM.get(Blocks.RED_MUSHROOM_BLOCK);
            case 101:
                return Item.BLOCK_TO_ITEM.get(Blocks.IRON_BARS);
            case 102:
                return Item.BLOCK_TO_ITEM.get(Blocks.GLASS_PANE);
            case 103:
                return Item.BLOCK_TO_ITEM.get(Blocks.MELON_BLOCK);
            case 104:
                return Item.BLOCK_TO_ITEM.get(Blocks.PUMPKIN_STEM);
            case 105:
                return Item.BLOCK_TO_ITEM.get(Blocks.MELON_STEM);
            case 106:
                return Item.BLOCK_TO_ITEM.get(Blocks.VINE);
            case 107:
                return Item.BLOCK_TO_ITEM.get(Blocks.OAK_FENCE_GATE);
            case 108:
                return Item.BLOCK_TO_ITEM.get(Blocks.BRICK_STAIRS);
            case 109:
                return Item.BLOCK_TO_ITEM.get(Blocks.STONE_BRICK_STAIRS);
            case 110:
                return Item.BLOCK_TO_ITEM.get(Blocks.MYCELIUM);
            case 111:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196651_dG); // lily pad
            case 112:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196653_dH); // nether bricks
            case 113:
                return Item.BLOCK_TO_ITEM.get(Blocks.NETHER_BRICK_FENCE);
            case 114:
                return Item.BLOCK_TO_ITEM.get(Blocks.NETHER_BRICK_STAIRS);
            //case 115: return Item.BLOCK_TO_ITEM.get(Blocks.NETHER_WART);
            case 116:
                return Item.BLOCK_TO_ITEM.get(Blocks.ENCHANTING_TABLE);
            //case 117: return Item.BLOCK_TO_ITEM.get(Blocks.BREWING_STAND);
            //case 118: return Item.BLOCK_TO_ITEM.get(Blocks.CAULDRON);
            case 119:
                return Item.BLOCK_TO_ITEM.get(Blocks.END_PORTAL);
            case 120:
                return Item.BLOCK_TO_ITEM.get(Blocks.END_PORTAL_FRAME);
            case 121:
                return Item.BLOCK_TO_ITEM.get(Blocks.END_STONE);
            case 122:
                return Item.BLOCK_TO_ITEM.get(Blocks.DRAGON_EGG);
            case 123:
            case 124:
                return Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_LAMP);
            //case 125: return Item.BLOCK_TO_ITEM.get(Blocks.DOUBLE_WOODEN_SLAB);
            case 126:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196622_bq); // oak slab
            case 127:
                return Item.BLOCK_TO_ITEM.get(Blocks.COCOA);
            case 128:
                return Item.BLOCK_TO_ITEM.get(Blocks.SANDSTONE_STAIRS);
            case 129:
                return Item.BLOCK_TO_ITEM.get(Blocks.EMERALD_ORE);
            case 130:
                return Item.BLOCK_TO_ITEM.get(Blocks.ENDER_CHEST);
            case 131:
                return Item.BLOCK_TO_ITEM.get(Blocks.TRIPWIRE_HOOK);
            case 132:
                return Item.BLOCK_TO_ITEM.get(Blocks.TRIPWIRE);
            case 133:
                return Item.BLOCK_TO_ITEM.get(Blocks.EMERALD_BLOCK);
            case 134:
                return Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_STAIRS);
            case 135:
                return Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_STAIRS);
            case 136:
                return Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_STAIRS);
            case 137:
                return Item.BLOCK_TO_ITEM.get(Blocks.COMMAND_BLOCK);
            case 138:
                return Item.BLOCK_TO_ITEM.get(Blocks.BEACON);
            case 139:
                return Item.BLOCK_TO_ITEM.get(Blocks.COBBLESTONE_WALL);
            //case 140: return Item.BLOCK_TO_ITEM.get(Blocks.FLOWER_POT);
            case 141:
                return Item.BLOCK_TO_ITEM.get(Blocks.CARROTS);
            case 142:
                return Item.BLOCK_TO_ITEM.get(Blocks.POTATOES);
            case 143:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196689_eF); // oak button
            case 144:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196703_eM); // skeleton skull
            case 145:
                return Item.BLOCK_TO_ITEM.get(Blocks.ANVIL);
            case 146:
                return Item.BLOCK_TO_ITEM.get(Blocks.TRAPPED_CHEST);
            case 147:
                return Item.BLOCK_TO_ITEM.get(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
            case 148:
                return Item.BLOCK_TO_ITEM.get(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
            case 149:
            case 150:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196762_fd);
            case 151:
                return Item.BLOCK_TO_ITEM.get(Blocks.DAYLIGHT_DETECTOR);
            case 152:
                return Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_BLOCK);
            case 153:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196766_fg); // nether quartz ore
            case 154:
                return Item.BLOCK_TO_ITEM.get(Blocks.HOPPER);
            case 155:
                return Item.BLOCK_TO_ITEM.get(Blocks.QUARTZ_BLOCK);
            case 156:
                return Item.BLOCK_TO_ITEM.get(Blocks.QUARTZ_STAIRS);
            case 157:
                return Item.BLOCK_TO_ITEM.get(Blocks.ACTIVATOR_RAIL);
            case 158:
                return Item.BLOCK_TO_ITEM.get(Blocks.DROPPER);
            case 159:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196777_fo); // white terracotta
            case 160:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196825_gz); // white stained glass pane
            case 161:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196574_ab); // dark oak leaves
            case 162:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196623_P); // dark oak log
            case 163:
                return Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_STAIRS);
            case 164:
                return Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_STAIRS);
            case 165:
                return Item.BLOCK_TO_ITEM.get(Blocks.SLIME_BLOCK);
            case 166:
                return Item.BLOCK_TO_ITEM.get(Blocks.BARRIER);
            case 167:
                return Item.BLOCK_TO_ITEM.get(Blocks.IRON_TRAPDOOR);
            case 168:
                return Item.BLOCK_TO_ITEM.get(Blocks.PRISMARINE);
            case 169:
                return Item.BLOCK_TO_ITEM.get(Blocks.SEA_LANTERN);
            case 170:
                return Item.BLOCK_TO_ITEM.get(Blocks.HAY_BLOCK);
            case 171:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196724_fH); // white carpet
            case 172:
                return Item.BLOCK_TO_ITEM.get(Blocks.HARDENED_CLAY);
            case 173:
                return Item.BLOCK_TO_ITEM.get(Blocks.COAL_BLOCK);
            case 174:
                return Item.BLOCK_TO_ITEM.get(Blocks.PACKED_ICE);
            case 176:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196784_gT); // white banner
            case 177:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196843_hj); // white wall banner
            case 178:
                return Item.BLOCK_TO_ITEM.get(Blocks.DAYLIGHT_DETECTOR);
            case 179:
                return Item.BLOCK_TO_ITEM.get(Blocks.RED_SANDSTONE);
            case 180:
                return Item.BLOCK_TO_ITEM.get(Blocks.RED_SANDSTONE_STAIRS);
            case 183:
                return Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_FENCE_GATE);
            case 184:
                return Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_FENCE_GATE);
            case 185:
                return Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_FENCE_GATE);
            case 186:
                return Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_FENCE_GATE);
            case 187:
                return Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_FENCE_GATE);
            case 188:
                return Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_FENCE);
            case 189:
                return Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_FENCE);
            case 190:
                return Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_FENCE);
            case 191:
                return Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_FENCE);
            case 192:
                return Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_FENCE);
            //case 193: return Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_DOOR);
            //case 194: return Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_DOOR);
            //case 195: return Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_DOOR);
            //case 196: return Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_DOOR);
            //case 197: return Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_DOOR);
            case 198:
                return Item.BLOCK_TO_ITEM.get(Blocks.END_ROD);
            case 199:
                return Item.BLOCK_TO_ITEM.get(Blocks.CHORUS_PLANT);
            case 200:
                return Item.BLOCK_TO_ITEM.get(Blocks.CHORUS_FLOWER);
            case 201:
                return Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_BLOCK);
            case 202:
                return Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_PILLAR);
            case 203:
                return Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_STAIRS);
            case 205:
                return Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_SLAB);
            case 206:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196806_hJ); // end stone bricks
            //case 207: return Item.BLOCK_TO_ITEM.get(Blocks.BEETROOTS);
            case 208:
                return Item.BLOCK_TO_ITEM.get(Blocks.GRASS_PATH);
            case 209:
                return Item.BLOCK_TO_ITEM.get(Blocks.END_GATEWAY);
            case 210:
                return Item.BLOCK_TO_ITEM.get(Blocks.REPEATING_COMMAND_BLOCK);
            case 211:
                return Item.BLOCK_TO_ITEM.get(Blocks.CHAIN_COMMAND_BLOCK);
            case 212:
                return Item.BLOCK_TO_ITEM.get(Blocks.FROSTED_ICE);
            case 213:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196814_hQ); // magma block
            case 214:
                return Item.BLOCK_TO_ITEM.get(Blocks.NETHER_WART_BLOCK);
            case 215:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196817_hS); // red nether brick
            case 216:
                return Item.BLOCK_TO_ITEM.get(Blocks.BONE_BLOCK);
            case 217:
                return Item.BLOCK_TO_ITEM.get(Blocks.STRUCTURE_VOID);
            case 218:
                return Item.BLOCK_TO_ITEM.get(Blocks.OBSERVER);
            case 219:
                return Item.BLOCK_TO_ITEM.get(Blocks.WHITE_SHULKER_BOX);
            case 220:
                return Item.BLOCK_TO_ITEM.get(Blocks.ORANGE_SHULKER_BOX);
            case 221:
                return Item.BLOCK_TO_ITEM.get(Blocks.MAGENTA_SHULKER_BOX);
            case 222:
                return Item.BLOCK_TO_ITEM.get(Blocks.LIGHT_BLUE_SHULKER_BOX);
            case 223:
                return Item.BLOCK_TO_ITEM.get(Blocks.YELLOW_SHULKER_BOX);
            case 224:
                return Item.BLOCK_TO_ITEM.get(Blocks.LIME_SHULKER_BOX);
            case 225:
                return Item.BLOCK_TO_ITEM.get(Blocks.PINK_SHULKER_BOX);
            case 226:
                return Item.BLOCK_TO_ITEM.get(Blocks.GRAY_SHULKER_BOX);
            case 227:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196875_ie); // light gray shulker box
            case 228:
                return Item.BLOCK_TO_ITEM.get(Blocks.CYAN_SHULKER_BOX);
            case 229:
                return Item.BLOCK_TO_ITEM.get(Blocks.PURPLE_SHULKER_BOX);
            case 230:
                return Item.BLOCK_TO_ITEM.get(Blocks.BLUE_SHULKER_BOX);
            case 231:
                return Item.BLOCK_TO_ITEM.get(Blocks.BROWN_SHULKER_BOX);
            case 232:
                return Item.BLOCK_TO_ITEM.get(Blocks.GREEN_SHULKER_BOX);
            case 233:
                return Item.BLOCK_TO_ITEM.get(Blocks.RED_SHULKER_BOX);
            case 234:
                return Item.BLOCK_TO_ITEM.get(Blocks.BLACK_SHULKER_BOX);
            case 255:
                return Item.BLOCK_TO_ITEM.get(Blocks.STRUCTURE_BLOCK);
            case 256:
                return Items.IRON_SHOVEL;
            case 257:
                return Items.IRON_PICKAXE;
            case 258:
                return Items.IRON_AXE;
            case 259:
                return Items.FLINT_AND_STEEL;
            case 260:
                return Items.APPLE;
            case 261:
                return Items.BOW;
            case 262:
                return Items.ARROW;
            case 263:
                return Items.COAL;
            case 264:
                return Items.DIAMOND;
            case 265:
                return Items.IRON_INGOT;
            case 266:
                return Items.GOLD_INGOT;
            case 267:
                return Items.IRON_SWORD;
            case 268:
                return Items.WOODEN_SWORD;
            case 269:
                return Items.WOODEN_SHOVEL;
            case 270:
                return Items.WOODEN_PICKAXE;
            case 271:
                return Items.WOODEN_AXE;
            case 272:
                return Items.STONE_SWORD;
            case 273:
                return Items.STONE_SHOVEL;
            case 274:
                return Items.STONE_PICKAXE;
            case 275:
                return Items.STONE_AXE;
            case 276:
                return Items.DIAMOND_SWORD;
            case 277:
                return Items.DIAMOND_SHOVEL;
            case 278:
                return Items.DIAMOND_PICKAXE;
            case 279:
                return Items.DIAMOND_AXE;
            case 280:
                return Items.STICK;
            case 281:
                return Items.BOWL;
            case 282:
                return Items.MUSHROOM_STEW;
            case 283:
                return Items.GOLDEN_SWORD;
            case 284:
                return Items.GOLDEN_SHOVEL;
            case 285:
                return Items.GOLDEN_PICKAXE;
            case 286:
                return Items.GOLDEN_AXE;
            case 287:
                return Items.STRING;
            case 288:
                return Items.FEATHER;
            case 289:
                return Items.GUNPOWDER;
            case 290:
                return Items.WOODEN_HOE;
            case 291:
                return Items.STONE_HOE;
            case 292:
                return Items.IRON_HOE;
            case 293:
                return Items.DIAMOND_HOE;
            case 294:
                return Items.GOLDEN_HOE;
            case 295:
                return Items.WHEAT_SEEDS;
            case 296:
                return Items.WHEAT;
            case 297:
                return Items.BREAD;
            case 298:
                return Items.LEATHER_HELMET;
            case 299:
                return Items.LEATHER_CHESTPLATE;
            case 300:
                return Items.LEATHER_LEGGINGS;
            case 301:
                return Items.LEATHER_BOOTS;
            case 302:
                return Items.CHAINMAIL_HELMET;
            case 303:
                return Items.CHAINMAIL_CHESTPLATE;
            case 304:
                return Items.CHAINMAIL_LEGGINGS;
            case 305:
                return Items.CHAINMAIL_BOOTS;
            case 306:
                return Items.IRON_HELMET;
            case 307:
                return Items.IRON_CHESTPLATE;
            case 308:
                return Items.IRON_LEGGINGS;
            case 309:
                return Items.IRON_BOOTS;
            case 310:
                return Items.DIAMOND_HELMET;
            case 311:
                return Items.DIAMOND_CHESTPLATE;
            case 312:
                return Items.DIAMOND_LEGGINGS;
            case 313:
                return Items.DIAMOND_BOOTS;
            case 314:
                return Items.GOLDEN_HELMET;
            case 315:
                return Items.GOLDEN_CHESTPLATE;
            case 316:
                return Items.GOLDEN_LEGGINGS;
            case 317:
                return Items.GOLDEN_BOOTS;
            case 318:
                return Items.FLINT;
            case 319:
                return Items.PORKCHOP;
            case 320:
                return Items.COOKED_PORKCHOP;
            case 321:
                return Items.PAINTING;
            case 322:
                return Items.GOLDEN_APPLE;
            case 323:
                return Items.SIGN;
            case 324:
                return Item.BLOCK_TO_ITEM.get(Blocks.OAK_DOOR);
            case 325:
                return Items.BUCKET;
            case 326:
                return Items.WATER_BUCKET;
            case 327:
                return Items.LAVA_BUCKET;
            case 328:
                return Items.MINECART;
            case 329:
                return Items.SADDLE;
            case 330:
                return Item.BLOCK_TO_ITEM.get(Blocks.IRON_DOOR);
            case 331:
                return Items.REDSTONE;
            case 332:
                return Items.SNOWBALL;
            case 333:
                return Items.BOAT;
            case 334:
                return Items.LEATHER;
            case 335:
                return Items.MILK_BUCKET;
            case 336:
                return Items.BRICK;
            case 338:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196608_cF); // sugar cane
            case 339:
                return Items.PAPER;
            case 340:
                return Items.BOOK;
            case 341:
                return Items.SLIME_BALL;
            case 342:
                return Items.CHEST_MINECART;
            case 343:
                return Items.FURNACE_MINECART;
            case 344:
                return Items.EGG;
            case 345:
                return Items.COMPASS;
            case 346:
                return Items.FISHING_ROD;
            case 347:
                return Items.CLOCK;
            case 348:
                return Items.GLOWSTONE_DUST;
            case 349:
                return Items.field_196086_aW; // cod
            case 350:
                return Items.field_196102_ba; // cooked cod
            case 351:
                return Items.field_196136_br; // ink sac
            case 352:
                return Items.BONE;
            case 353:
                return Items.SUGAR;
            case 354:
                return Item.BLOCK_TO_ITEM.get(Blocks.CAKE);
            case 355:
                return Items.field_196140_bu; // bed
            case 356:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196633_cV); // repeater
            case 357:
                return Items.COOKIE;
            case 358:
                return Items.MAP;
            case 359:
                return Items.SHEARS;
            case 361:
                return Items.PUMPKIN_SEEDS;
            case 362:
                return Items.MELON_SEEDS;
            case 363:
                return Items.BEEF;
            case 364:
                return Items.COOKED_BEEF;
            case 365:
                return Items.CHICKEN;
            case 366:
                return Items.COOKED_CHICKEN;
            case 367:
                return Items.ROTTEN_FLESH;
            case 368:
                return Items.ENDER_PEARL;
            case 369:
                return Items.BLAZE_ROD;
            case 370:
                return Items.GHAST_TEAR;
            case 371:
                return Items.GOLD_NUGGET;
            case 372:
                return Items.NETHER_WART;
            case 373:
            case 374:
                return Items.GLASS_BOTTLE;
            case 375:
                return Items.SPIDER_EYE;
            case 376:
                return Items.FERMENTED_SPIDER_EYE;
            case 377:
                return Items.BLAZE_POWDER;
            case 378:
                return Items.MAGMA_CREAM;
            case 379:
                return Item.BLOCK_TO_ITEM.get(Blocks.BREWING_STAND);
            case 380:
                return Item.BLOCK_TO_ITEM.get(Blocks.CAULDRON);
            case 381:
                return Items.ENDER_EYE;
            case 382:
                return Items.SPECKLED_MELON;
            case 383:
                return Items.field_196127_cN; // pig spawn egg
            case 384:
                return Items.EXPERIENCE_BOTTLE;
            case 385:
                return Items.FIRE_CHARGE;
            case 386:
                return Items.WRITABLE_BOOK;
            case 387:
                return Items.WRITTEN_BOOK;
            case 388:
                return Items.EMERALD;
            case 389:
                return Items.ITEM_FRAME;
            case 390:
                return Item.BLOCK_TO_ITEM.get(Blocks.FLOWER_POT);
            case 391:
                return Items.CARROT;
            case 392:
                return Items.POTATO;
            case 393:
                return Items.BAKED_POTATO;
            case 394:
                return Items.POISONOUS_POTATO;
            case 395:
                return Items.MAP;
            case 396:
                return Items.GOLDEN_CARROT;
            case 397:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196703_eM); // skeleton skull
            case 398:
                return Items.CARROT_ON_A_STICK;
            case 399:
                return Items.NETHER_STAR;
            case 400:
                return Items.PUMPKIN_PIE;
            case 401:
                return Items.field_196152_dE; // firework rocket
            case 402:
                return Items.field_196153_dF; // firework star
            case 403:
                return Items.ENCHANTED_BOOK;
            case 404:
                return Item.BLOCK_TO_ITEM.get(Blocks.field_196762_fd); // comparator
            case 406:
                return Items.QUARTZ;
            case 407:
                return Items.TNT_MINECART;
            case 408:
                return Items.HOPPER_MINECART;
            case 409:
                return Items.PRISMARINE_SHARD;
            case 410:
                return Items.PRISMARINE_CRYSTALS;
            case 411:
                return Items.RABBIT;
            case 412:
                return Items.COOKED_RABBIT;
            case 413:
                return Items.RABBIT_STEW;
            case 414:
                return Items.RABBIT_FOOT;
            case 415:
                return Items.RABBIT_HIDE;
            case 416:
                return Items.ARMOR_STAND;
            case 417:
                return Items.IRON_HORSE_ARMOR;
            case 418:
                return Items.GOLDEN_HORSE_ARMOR;
            case 419:
                return Items.DIAMOND_HORSE_ARMOR;
            case 420:
                return Items.LEAD;
            case 421:
                return Items.NAME_TAG;
            case 422:
                return Items.COMMAND_BLOCK_MINECART;
            case 423:
                return Items.MUTTON;
            case 424:
                return Items.COOKED_MUTTON;
            //case 425: return Items.BLACK_BANNER;
            case 427:
                return Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_DOOR);
            case 428:
                return Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_DOOR);
            case 429:
                return Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_DOOR);
            case 430:
                return Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_DOOR);
            case 431:
                return Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_DOOR);
            case 432:
                return Items.CHORUS_FRUIT;
            case 433:
                return Items.CHORUS_FRUIT_POPPED;
            case 434:
                return Items.BEETROOT;
            case 435:
                return Items.BEETROOT_SEEDS;
            case 436:
                return Items.BEETROOT_SOUP;
            case 437:
                return Items.DRAGON_BREATH;
            case 438:
                return Items.SPLASH_POTION;
            case 439:
                return Items.SPECTRAL_ARROW;
            case 440:
                return Items.TIPPED_ARROW;
            case 441:
                return Items.LINGERING_POTION;
            case 442:
                return Items.SHIELD;
            case 443:
                return Items.ELYTRA;
            case 444:
                return Items.SPRUCE_BOAT;
            case 445:
                return Items.BIRCH_BOAT;
            case 446:
                return Items.JUNGLE_BOAT;
            case 447:
                return Items.ACACIA_BOAT;
            case 448:
                return Items.DARK_OAK_BOAT;
            case 449:
                return Items.TOTEM_OF_UNDYING;
            case 450:
                return Items.SHULKER_SHELL;
            case 452:
                return Items.IRON_NUGGET;
            case 453:
                return Items.KNOWLEDGE_BOOK;

            case 2256:
                return Items.field_196156_dS; // record 13
            case 2257:
                return Items.field_196158_dT; // record cat
            case 2258:
                return Items.field_196160_dU; // record blocks
            case 2259:
                return Items.field_196162_dV; // record chirp
            case 2260:
                return Items.field_196164_dW; // record far
            case 2261:
                return Items.field_196166_dX; // record mall
            case 2262:
                return Items.field_196168_dY; // record mellohi
            case 2263:
                return Items.field_196170_dZ; // record stal
            case 2264:
                return Items.field_196187_ea; // record strad
            case 2265:
                return Items.field_196188_eb; // record ward
            case 2266:
                return Items.field_196189_ec; // record 11
            case 2267:
                return Items.field_196190_ed; // record wait
        }
        return Items.AIR;
    }
}
