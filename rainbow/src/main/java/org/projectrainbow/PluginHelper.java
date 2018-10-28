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
import net.minecraft.init.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

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
    public static BiMap<Item, Integer> legacyItemIdMap = HashBiMap.create();
    public static BiMap<Block, Integer> legacyBlockIdMap = HashBiMap.create();

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
        mc_potionEffect.ambient = potionEffect.isAmbient();
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

        biomeMap.put(Biome.getBiome(0, null), MC_WorldBiomeType.OCEAN);
        biomeMap.put(Biome.getBiome(1, null), MC_WorldBiomeType.PLAINS);
        biomeMap.put(Biome.getBiome(2, null), MC_WorldBiomeType.DESERT);
        biomeMap.put(Biome.getBiome(3, null), MC_WorldBiomeType.EXTREME_HILLS);
        biomeMap.put(Biome.getBiome(4, null), MC_WorldBiomeType.FOREST);
        biomeMap.put(Biome.getBiome(5, null), MC_WorldBiomeType.TAIGA);
        biomeMap.put(Biome.getBiome(6, null), MC_WorldBiomeType.SWAMPLAND);
        biomeMap.put(Biome.getBiome(7, null), MC_WorldBiomeType.RIVER);
        biomeMap.put(Biome.getBiome(8, null), MC_WorldBiomeType.HELL);
        biomeMap.put(Biome.getBiome(9, null), MC_WorldBiomeType.THE_END);
        biomeMap.put(Biome.getBiome(10, null), MC_WorldBiomeType.FROZEN_OCEAN);
        biomeMap.put(Biome.getBiome(11, null), MC_WorldBiomeType.FROZEN_RIVER);
        biomeMap.put(Biome.getBiome(12, null), MC_WorldBiomeType.ICE_PLAINS);
        biomeMap.put(Biome.getBiome(13, null), MC_WorldBiomeType.ICE_MOUNTAINS);
        biomeMap.put(Biome.getBiome(14, null), MC_WorldBiomeType.MUSHROOM_ISLAND);
        biomeMap.put(Biome.getBiome(15, null), MC_WorldBiomeType.MUSHROOM_ISLAND_SHORE);
        biomeMap.put(Biome.getBiome(16, null), MC_WorldBiomeType.BEACH);
        biomeMap.put(Biome.getBiome(17, null), MC_WorldBiomeType.DESERT_HILLS);
        biomeMap.put(Biome.getBiome(18, null), MC_WorldBiomeType.FOREST_HILLS);
        biomeMap.put(Biome.getBiome(19, null), MC_WorldBiomeType.TAIGA_HILLS);
        biomeMap.put(Biome.getBiome(20, null), MC_WorldBiomeType.EXTREME_HILLS_EDGE);
        biomeMap.put(Biome.getBiome(21, null), MC_WorldBiomeType.JUNGLE);
        biomeMap.put(Biome.getBiome(22, null), MC_WorldBiomeType.JUNGLE_HILLS);
        biomeMap.put(Biome.getBiome(23, null), MC_WorldBiomeType.JUNGLE_EDGE);
        biomeMap.put(Biome.getBiome(24, null), MC_WorldBiomeType.DEEP_OCEAN);
        biomeMap.put(Biome.getBiome(25, null), MC_WorldBiomeType.STONE_BEACH);
        biomeMap.put(Biome.getBiome(26, null), MC_WorldBiomeType.COLD_BEACH);
        biomeMap.put(Biome.getBiome(27, null), MC_WorldBiomeType.BIRCH_FOREST);
        biomeMap.put(Biome.getBiome(28, null), MC_WorldBiomeType.BIRCH_FOREST_HILLS);
        biomeMap.put(Biome.getBiome(29, null), MC_WorldBiomeType.ROOFED_FOREST);
        biomeMap.put(Biome.getBiome(30, null), MC_WorldBiomeType.COLD_TAIGA);
        biomeMap.put(Biome.getBiome(31, null), MC_WorldBiomeType.COLD_TAIGA_HILLS);
        biomeMap.put(Biome.getBiome(32, null), MC_WorldBiomeType.MEGA_TAIGA);
        biomeMap.put(Biome.getBiome(33, null), MC_WorldBiomeType.MEGA_TAIGA_HILLS);
        biomeMap.put(Biome.getBiome(34, null), MC_WorldBiomeType.EXTREME_HILLS_PLUS);
        biomeMap.put(Biome.getBiome(35, null), MC_WorldBiomeType.SAVANNA);
        biomeMap.put(Biome.getBiome(36, null), MC_WorldBiomeType.SAVANNA_PLATEAU);
        biomeMap.put(Biome.getBiome(37, null), MC_WorldBiomeType.MESA);
        biomeMap.put(Biome.getBiome(38, null), MC_WorldBiomeType.MESA_PLATEAU_F);
        biomeMap.put(Biome.getBiome(39, null), MC_WorldBiomeType.MESA_PLATEAU);
        biomeMap.put(Biome.getBiome(40, null), MC_WorldBiomeType.END_SMALL_ISLANDS);
        biomeMap.put(Biome.getBiome(41, null), MC_WorldBiomeType.END_MIDLANDS);
        biomeMap.put(Biome.getBiome(42, null), MC_WorldBiomeType.END_HIGHLANDS);
        biomeMap.put(Biome.getBiome(43, null), MC_WorldBiomeType.END_BARRENS);
        biomeMap.put(Biome.getBiome(44, null), MC_WorldBiomeType.WARM_OCEAN);
        biomeMap.put(Biome.getBiome(45, null), MC_WorldBiomeType.LUKEWARM_OCEAN);
        biomeMap.put(Biome.getBiome(46, null), MC_WorldBiomeType.COLD_OCEAN);
        biomeMap.put(Biome.getBiome(47, null), MC_WorldBiomeType.DEEP_WARM_OCEAN);
        biomeMap.put(Biome.getBiome(48, null), MC_WorldBiomeType.DEEP_LUKEWARM_OCEAN);
        biomeMap.put(Biome.getBiome(49, null), MC_WorldBiomeType.DEEP_COLD_OCEAN);
        biomeMap.put(Biome.getBiome(50, null), MC_WorldBiomeType.DEEP_FROZEN_OCEAN);
        biomeMap.put(Biome.getBiome(127, null), MC_WorldBiomeType.VOID);
        biomeMap.put(Biome.getBiome(129, null), MC_WorldBiomeType.SUNFLOWER_PLAINS);
        biomeMap.put(Biome.getBiome(130, null), MC_WorldBiomeType.DESERT_M);
        biomeMap.put(Biome.getBiome(131, null), MC_WorldBiomeType.EXTREME_HILLS_M);
        biomeMap.put(Biome.getBiome(132, null), MC_WorldBiomeType.FLOWER_FOREST);
        biomeMap.put(Biome.getBiome(133, null), MC_WorldBiomeType.TAIGA_M);
        biomeMap.put(Biome.getBiome(134, null), MC_WorldBiomeType.SWAMPLAND_M);
        biomeMap.put(Biome.getBiome(140, null), MC_WorldBiomeType.ICE_PLAINS_SPIKES);
        biomeMap.put(Biome.getBiome(149, null), MC_WorldBiomeType.JUNGLE_M);
        biomeMap.put(Biome.getBiome(151, null), MC_WorldBiomeType.JUNGLE_EDGE_M);
        biomeMap.put(Biome.getBiome(155, null), MC_WorldBiomeType.BIRCH_FOREST_M);
        biomeMap.put(Biome.getBiome(156, null), MC_WorldBiomeType.BIRCH_FOREST_HILLS_M);
        biomeMap.put(Biome.getBiome(157, null), MC_WorldBiomeType.ROOFED_FOREST_M);
        biomeMap.put(Biome.getBiome(158, null), MC_WorldBiomeType.COLD_TAIGA_M);
        biomeMap.put(Biome.getBiome(160, null), MC_WorldBiomeType.MEGA_SPRUCE_TAIGA);
        biomeMap.put(Biome.getBiome(161, null), MC_WorldBiomeType.REDWOOD_TAIGA_HILLS_M);
        biomeMap.put(Biome.getBiome(162, null), MC_WorldBiomeType.EXTREME_HILLS_PLUS_M);
        biomeMap.put(Biome.getBiome(163, null), MC_WorldBiomeType.SAVANNA_M);
        biomeMap.put(Biome.getBiome(164, null), MC_WorldBiomeType.SAVANNA_PLATEAU_M);
        biomeMap.put(Biome.getBiome(165, null), MC_WorldBiomeType.MESA_M);
        biomeMap.put(Biome.getBiome(166, null), MC_WorldBiomeType.MESA_PLATEAU_F_M);
        biomeMap.put(Biome.getBiome(167, null), MC_WorldBiomeType.MESA_PLATEAU_M);

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

        legacyItemIdMap.put(Items.AIR, 0);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE), 1);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GRASS), 2);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DIRT), 3);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COBBLESTONE), 4);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_PLANKS), 5);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_SAPLING), 6);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BEDROCK), 7);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WATER), 9);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LAVA), 10);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SAND), 12);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GRAVEL), 13);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GOLD_ORE), 14);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.IRON_ORE), 15);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COAL_ORE), 16);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_LOG), 17);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_LEAVES), 18);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPONGE), 19);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GLASS), 20);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LAPIS_ORE), 21);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LAPIS_BLOCK), 22);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DISPENSER), 23);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SANDSTONE), 24);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NOTE_BLOCK), 25);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.POWERED_RAIL), 27);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DETECTOR_RAIL), 28);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STICKY_PISTON), 29);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COBWEB), 30);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TALL_GRASS), 31);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DEAD_BUSH), 32);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PISTON), 33);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PISTON_HEAD), 34);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WHITE_WOOL), 35); // white wool
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DANDELION), 37); // dandelion / yellow flower
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.POPPY), 38); // poppy / red flower
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BROWN_MUSHROOM), 39);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_MUSHROOM), 40);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GOLD_BLOCK), 41);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.IRON_BLOCK), 42);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE_SLAB), 44);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BRICKS), 45); // bricks
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TNT), 46);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BOOKSHELF), 47);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MOSSY_COBBLESTONE), 48);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OBSIDIAN), 49);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TORCH), 50);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.FIRE), 51);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPAWNER), 52);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_STAIRS), 53);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CHEST), 54);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_WIRE), 55);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DIAMOND_ORE), 56);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DIAMOND_BLOCK), 57);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CRAFTING_TABLE), 58);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.FARMLAND), 60);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.FURNACE), 61);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SIGN), 63); // sign
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_DOOR), 64);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LADDER), 65);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RAIL), 66);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COBBLESTONE_STAIRS), 67); // cobblestone stairs
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WALL_SIGN), 68);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LEVER), 69);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE_PRESSURE_PLATE), 70);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_PRESSURE_PLATE), 72); // oak pressure plate
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_ORE), 73);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_TORCH), 76);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE_BUTTON), 77);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SNOW), 78);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ICE), 79);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SNOW_BLOCK), 80); // snow block
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CACTUS), 81);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CLAY), 82);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JUKEBOX), 84);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_FENCE), 85);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PUMPKIN), 86);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHERRACK), 87);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SOUL_SAND), 88);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GLOWSTONE), 89);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHER_PORTAL), 90);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JACK_O_LANTERN), 91); // lit pumpkin
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REPEATER), 93); // repeater
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WHITE_STAINED_GLASS), 95); // white stained glass
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_TRAPDOOR), 96); // oak trapdoor
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.INFESTED_STONE), 97); // infested stone
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE_BRICKS), 98); // stone bricks
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BROWN_MUSHROOM_BLOCK), 99);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_MUSHROOM_BLOCK), 100);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.IRON_BARS), 101);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GLASS_PANE), 102);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MELON), 103);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PUMPKIN_STEM), 104);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MELON_STEM), 105);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.VINE), 106);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_FENCE_GATE), 107);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BRICK_STAIRS), 108);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE_BRICK_STAIRS), 109);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MYCELIUM), 110);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LILY_PAD), 111); // lily pad
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHER_BRICKS), 112); // nether bricks
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHER_BRICK_FENCE), 113);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHER_BRICK_STAIRS), 114);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ENCHANTING_TABLE), 116);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_PORTAL), 119);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_PORTAL_FRAME), 120);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_STONE), 121);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DRAGON_EGG), 122);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_LAMP), 123);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_SLAB), 126); // oak slab
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COCOA), 127);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SANDSTONE_STAIRS), 128);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.EMERALD_ORE), 129);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ENDER_CHEST), 130);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TRIPWIRE_HOOK), 131);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TRIPWIRE), 132);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.EMERALD_BLOCK), 133);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_STAIRS), 134);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_STAIRS), 135);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_STAIRS), 136);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COMMAND_BLOCK), 137);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BEACON), 138);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COBBLESTONE_WALL), 139);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CARROTS), 141);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.POTATOES), 142);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_BUTTON), 143); // oak button
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SKELETON_SKULL), 144); // skeleton skull
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ANVIL), 145);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TRAPPED_CHEST), 146);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), 147);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), 148);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COMPARATOR), 149); // comparator
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DAYLIGHT_DETECTOR), 151);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_BLOCK), 152);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHER_QUARTZ_ORE), 153); // nether quartz ore
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.HOPPER), 154);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.QUARTZ_BLOCK), 155);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.QUARTZ_STAIRS), 156);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ACTIVATOR_RAIL), 157);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DROPPER), 158);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CLAY), 159);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WHITE_STAINED_GLASS_PANE), 160); // white stained glass pane
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_LEAVES), 161); // dark oak leaves
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_LOG), 162); // dark oak log
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_STAIRS), 163);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_STAIRS), 164);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SLIME_BLOCK), 165);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BARRIER), 166);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.IRON_TRAPDOOR), 167);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PRISMARINE), 168);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SEA_LANTERN), 169);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.HAY_BLOCK), 170);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WHITE_CARPET), 171); // white carpet
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TERRACOTTA), 172);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COAL_BLOCK), 173);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PACKED_ICE), 174);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WHITE_BANNER), 176); // white banner
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WHITE_WALL_BANNER), 177); // white wall banner
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_SANDSTONE), 179);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_SANDSTONE_STAIRS), 180);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_FENCE_GATE), 183);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_FENCE_GATE), 184);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_FENCE_GATE), 185);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_FENCE_GATE), 186);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_FENCE_GATE), 187);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_FENCE), 188);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_FENCE), 189);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_FENCE), 190);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_FENCE), 191);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_FENCE), 192);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_ROD), 198);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CHORUS_PLANT), 199);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CHORUS_FLOWER), 200);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_BLOCK), 201);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_PILLAR), 202);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_STAIRS), 203);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_SLAB), 205);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_STONE_BRICKS), 206); // end stone bricks
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GRASS_PATH), 208);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_GATEWAY), 209);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REPEATING_COMMAND_BLOCK), 210);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CHAIN_COMMAND_BLOCK), 211);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.FROSTED_ICE), 212);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MAGMA_BLOCK), 213); // magma block
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHER_WART_BLOCK), 214);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_NETHER_BRICKS), 215); // red nether brick
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BONE_BLOCK), 216);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STRUCTURE_VOID), 217);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OBSERVER), 218);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WHITE_SHULKER_BOX), 219);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ORANGE_SHULKER_BOX), 220);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MAGENTA_SHULKER_BOX), 221);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LIGHT_BLUE_SHULKER_BOX), 222);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.YELLOW_SHULKER_BOX), 223);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LIME_SHULKER_BOX), 224);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PINK_SHULKER_BOX), 225);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GRAY_SHULKER_BOX), 226);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LIGHT_GRAY_SHULKER_BOX), 227); // light gray shulker box
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CYAN_SHULKER_BOX), 228);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PURPLE_SHULKER_BOX), 229);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BLUE_SHULKER_BOX), 230);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BROWN_SHULKER_BOX), 231);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GREEN_SHULKER_BOX), 232);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_SHULKER_BOX), 233);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BLACK_SHULKER_BOX), 234);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STRUCTURE_BLOCK), 255);
        legacyItemIdMap.put(Items.IRON_SHOVEL, 256);
        legacyItemIdMap.put(Items.IRON_PICKAXE, 257);
        legacyItemIdMap.put(Items.IRON_AXE, 258);
        legacyItemIdMap.put(Items.FLINT_AND_STEEL, 259);
        legacyItemIdMap.put(Items.APPLE, 260);
        legacyItemIdMap.put(Items.BOW, 261);
        legacyItemIdMap.put(Items.ARROW, 262);
        legacyItemIdMap.put(Items.COAL, 263);
        legacyItemIdMap.put(Items.DIAMOND, 264);
        legacyItemIdMap.put(Items.IRON_INGOT, 265);
        legacyItemIdMap.put(Items.GOLD_INGOT, 266);
        legacyItemIdMap.put(Items.IRON_SWORD, 267);
        legacyItemIdMap.put(Items.WOODEN_SWORD, 268);
        legacyItemIdMap.put(Items.WOODEN_SHOVEL, 269);
        legacyItemIdMap.put(Items.WOODEN_PICKAXE, 270);
        legacyItemIdMap.put(Items.WOODEN_AXE, 271);
        legacyItemIdMap.put(Items.STONE_SWORD, 272);
        legacyItemIdMap.put(Items.STONE_SHOVEL, 273);
        legacyItemIdMap.put(Items.STONE_PICKAXE, 274);
        legacyItemIdMap.put(Items.STONE_AXE, 275);
        legacyItemIdMap.put(Items.DIAMOND_SWORD, 276);
        legacyItemIdMap.put(Items.DIAMOND_SHOVEL, 277);
        legacyItemIdMap.put(Items.DIAMOND_PICKAXE, 278);
        legacyItemIdMap.put(Items.DIAMOND_AXE, 279);
        legacyItemIdMap.put(Items.STICK, 280);
        legacyItemIdMap.put(Items.BOWL, 281);
        legacyItemIdMap.put(Items.MUSHROOM_STEW, 282);
        legacyItemIdMap.put(Items.GOLDEN_SWORD, 283);
        legacyItemIdMap.put(Items.GOLDEN_SHOVEL, 284);
        legacyItemIdMap.put(Items.GOLDEN_PICKAXE, 285);
        legacyItemIdMap.put(Items.GOLDEN_AXE, 286);
        legacyItemIdMap.put(Items.STRING, 287);
        legacyItemIdMap.put(Items.FEATHER, 288);
        legacyItemIdMap.put(Items.GUNPOWDER, 289);
        legacyItemIdMap.put(Items.WOODEN_HOE, 290);
        legacyItemIdMap.put(Items.STONE_HOE, 291);
        legacyItemIdMap.put(Items.IRON_HOE, 292);
        legacyItemIdMap.put(Items.DIAMOND_HOE, 293);
        legacyItemIdMap.put(Items.GOLDEN_HOE, 294);
        legacyItemIdMap.put(Items.WHEAT_SEEDS, 295);
        legacyItemIdMap.put(Items.WHEAT, 296);
        legacyItemIdMap.put(Items.BREAD, 297);
        legacyItemIdMap.put(Items.LEATHER_HELMET, 298);
        legacyItemIdMap.put(Items.LEATHER_CHESTPLATE, 299);
        legacyItemIdMap.put(Items.LEATHER_LEGGINGS, 300);
        legacyItemIdMap.put(Items.LEATHER_BOOTS, 301);
        legacyItemIdMap.put(Items.CHAINMAIL_HELMET, 302);
        legacyItemIdMap.put(Items.CHAINMAIL_CHESTPLATE, 303);
        legacyItemIdMap.put(Items.CHAINMAIL_LEGGINGS, 304);
        legacyItemIdMap.put(Items.CHAINMAIL_BOOTS, 305);
        legacyItemIdMap.put(Items.IRON_HELMET, 306);
        legacyItemIdMap.put(Items.IRON_CHESTPLATE, 307);
        legacyItemIdMap.put(Items.IRON_LEGGINGS, 308);
        legacyItemIdMap.put(Items.IRON_BOOTS, 309);
        legacyItemIdMap.put(Items.DIAMOND_HELMET, 310);
        legacyItemIdMap.put(Items.DIAMOND_CHESTPLATE, 311);
        legacyItemIdMap.put(Items.DIAMOND_LEGGINGS, 312);
        legacyItemIdMap.put(Items.DIAMOND_BOOTS, 313);
        legacyItemIdMap.put(Items.GOLDEN_HELMET, 314);
        legacyItemIdMap.put(Items.GOLDEN_CHESTPLATE, 315);
        legacyItemIdMap.put(Items.GOLDEN_LEGGINGS, 316);
        legacyItemIdMap.put(Items.GOLDEN_BOOTS, 317);
        legacyItemIdMap.put(Items.FLINT, 318);
        legacyItemIdMap.put(Items.PORKCHOP, 319);
        legacyItemIdMap.put(Items.COOKED_PORKCHOP, 320);
        legacyItemIdMap.put(Items.PAINTING, 321);
        legacyItemIdMap.put(Items.GOLDEN_APPLE, 322);
        legacyItemIdMap.put(Items.SIGN, 323);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_DOOR), 324);
        legacyItemIdMap.put(Items.BUCKET, 325);
        legacyItemIdMap.put(Items.WATER_BUCKET, 326);
        legacyItemIdMap.put(Items.LAVA_BUCKET, 327);
        legacyItemIdMap.put(Items.MINECART, 328);
        legacyItemIdMap.put(Items.SADDLE, 329);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.IRON_DOOR), 330);
        legacyItemIdMap.put(Items.REDSTONE, 331);
        legacyItemIdMap.put(Items.SNOWBALL, 332);
        legacyItemIdMap.put(Items.OAK_BOAT, 333);
        legacyItemIdMap.put(Items.LEATHER, 334);
        legacyItemIdMap.put(Items.MILK_BUCKET, 335);
        legacyItemIdMap.put(Items.BRICK, 336);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SUGAR_CANE), 338); // sugar cane
        legacyItemIdMap.put(Items.PAPER, 339);
        legacyItemIdMap.put(Items.BOOK, 340);
        legacyItemIdMap.put(Items.SLIME_BALL, 341);
        legacyItemIdMap.put(Items.CHEST_MINECART, 342);
        legacyItemIdMap.put(Items.FURNACE_MINECART, 343);
        legacyItemIdMap.put(Items.EGG, 344);
        legacyItemIdMap.put(Items.COMPASS, 345);
        legacyItemIdMap.put(Items.FISHING_ROD, 346);
        legacyItemIdMap.put(Items.CLOCK, 347);
        legacyItemIdMap.put(Items.GLOWSTONE_DUST, 348);
        legacyItemIdMap.put(Items.COD, 349); // cod
        legacyItemIdMap.put(Items.COOKED_COD, 350); // cooked cod
        legacyItemIdMap.put(Items.INK_SAC, 351); // ink sac
        legacyItemIdMap.put(Items.BONE, 352);
        legacyItemIdMap.put(Items.SUGAR, 353);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CAKE), 354);
        legacyItemIdMap.put(Items.RED_BED, 355); // bed
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REPEATER), 356); // repeater
        legacyItemIdMap.put(Items.COOKIE, 357);
        legacyItemIdMap.put(Items.MAP, 358);
        legacyItemIdMap.put(Items.SHEARS, 359);
        legacyItemIdMap.put(Items.PUMPKIN_SEEDS, 361);
        legacyItemIdMap.put(Items.MELON_SEEDS, 362);
        legacyItemIdMap.put(Items.BEEF, 363);
        legacyItemIdMap.put(Items.COOKED_BEEF, 364);
        legacyItemIdMap.put(Items.CHICKEN, 365);
        legacyItemIdMap.put(Items.COOKED_CHICKEN, 366);
        legacyItemIdMap.put(Items.ROTTEN_FLESH, 367);
        legacyItemIdMap.put(Items.ENDER_PEARL, 368);
        legacyItemIdMap.put(Items.BLAZE_ROD, 369);
        legacyItemIdMap.put(Items.GHAST_TEAR, 370);
        legacyItemIdMap.put(Items.GOLD_NUGGET, 371);
        legacyItemIdMap.put(Items.NETHER_WART, 372);
        legacyItemIdMap.put(Items.GLASS_BOTTLE, 374);
        legacyItemIdMap.put(Items.SPIDER_EYE, 375);
        legacyItemIdMap.put(Items.FERMENTED_SPIDER_EYE, 376);
        legacyItemIdMap.put(Items.BLAZE_POWDER, 377);
        legacyItemIdMap.put(Items.MAGMA_CREAM, 378);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BREWING_STAND), 379);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CAULDRON), 380);
        legacyItemIdMap.put(Items.ENDER_EYE, 381);
        legacyItemIdMap.put(Items.GLISTERING_MELON_SLICE, 382);
        legacyItemIdMap.put(Items.PIG_SPAWN_EGG, 383); // pig spawn egg
        legacyItemIdMap.put(Items.EXPERIENCE_BOTTLE, 384);
        legacyItemIdMap.put(Items.FIRE_CHARGE, 385);
        legacyItemIdMap.put(Items.WRITABLE_BOOK, 386);
        legacyItemIdMap.put(Items.WRITTEN_BOOK, 387);
        legacyItemIdMap.put(Items.EMERALD, 388);
        legacyItemIdMap.put(Items.ITEM_FRAME, 389);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.FLOWER_POT), 390);
        legacyItemIdMap.put(Items.CARROT, 391);
        legacyItemIdMap.put(Items.POTATO, 392);
        legacyItemIdMap.put(Items.BAKED_POTATO, 393);
        legacyItemIdMap.put(Items.POISONOUS_POTATO, 394);
        legacyItemIdMap.put(Items.MAP, 395);
        legacyItemIdMap.put(Items.GOLDEN_CARROT, 396);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SKELETON_SKULL), 397); // skeleton skull
        legacyItemIdMap.put(Items.CARROT_ON_A_STICK, 398);
        legacyItemIdMap.put(Items.NETHER_STAR, 399);
        legacyItemIdMap.put(Items.PUMPKIN_PIE, 400);
        legacyItemIdMap.put(Items.FIREWORK_ROCKET, 401); // firework rocket
        legacyItemIdMap.put(Items.FIREWORK_STAR, 402); // firework star
        legacyItemIdMap.put(Items.ENCHANTED_BOOK, 403);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COMPARATOR), 404); // comparator
        legacyItemIdMap.put(Items.QUARTZ, 406);
        legacyItemIdMap.put(Items.TNT_MINECART, 407);
        legacyItemIdMap.put(Items.HOPPER_MINECART, 408);
        legacyItemIdMap.put(Items.PRISMARINE_SHARD, 409);
        legacyItemIdMap.put(Items.PRISMARINE_CRYSTALS, 410);
        legacyItemIdMap.put(Items.RABBIT, 411);
        legacyItemIdMap.put(Items.COOKED_RABBIT, 412);
        legacyItemIdMap.put(Items.RABBIT_STEW, 413);
        legacyItemIdMap.put(Items.RABBIT_FOOT, 414);
        legacyItemIdMap.put(Items.RABBIT_HIDE, 415);
        legacyItemIdMap.put(Items.ARMOR_STAND, 416);
        legacyItemIdMap.put(Items.IRON_HORSE_ARMOR, 417);
        legacyItemIdMap.put(Items.GOLDEN_HORSE_ARMOR, 418);
        legacyItemIdMap.put(Items.DIAMOND_HORSE_ARMOR, 419);
        legacyItemIdMap.put(Items.LEAD, 420);
        legacyItemIdMap.put(Items.NAME_TAG, 421);
        legacyItemIdMap.put(Items.COMMAND_BLOCK_MINECART, 422);
        legacyItemIdMap.put(Items.MUTTON, 423);
        legacyItemIdMap.put(Items.COOKED_MUTTON, 424);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_DOOR), 427);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_DOOR), 428);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_DOOR), 429);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_DOOR), 430);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_DOOR), 431);
        legacyItemIdMap.put(Items.CHORUS_FRUIT, 432);
        legacyItemIdMap.put(Items.POPPED_CHORUS_FRUIT, 433);
        legacyItemIdMap.put(Items.BEETROOT, 434);
        legacyItemIdMap.put(Items.BEETROOT_SEEDS, 435);
        legacyItemIdMap.put(Items.BEETROOT_SOUP, 436);
        legacyItemIdMap.put(Items.DRAGON_BREATH, 437);
        legacyItemIdMap.put(Items.SPLASH_POTION, 438);
        legacyItemIdMap.put(Items.SPECTRAL_ARROW, 439);
        legacyItemIdMap.put(Items.TIPPED_ARROW, 440);
        legacyItemIdMap.put(Items.LINGERING_POTION, 441);
        legacyItemIdMap.put(Items.SHIELD, 442);
        legacyItemIdMap.put(Items.ELYTRA, 443);
        legacyItemIdMap.put(Items.SPRUCE_BOAT, 444);
        legacyItemIdMap.put(Items.BIRCH_BOAT, 445);
        legacyItemIdMap.put(Items.JUNGLE_BOAT, 446);
        legacyItemIdMap.put(Items.ACACIA_BOAT, 447);
        legacyItemIdMap.put(Items.DARK_OAK_BOAT, 448);
        legacyItemIdMap.put(Items.TOTEM_OF_UNDYING, 449);
        legacyItemIdMap.put(Items.SHULKER_SHELL, 450);
        legacyItemIdMap.put(Items.IRON_NUGGET, 452);
        legacyItemIdMap.put(Items.KNOWLEDGE_BOOK, 453);
        legacyItemIdMap.put(Items.MUSIC_DISC_13, 2256); // record 13
        legacyItemIdMap.put(Items.MUSIC_DISC_CAT, 2257); // record cat
        legacyItemIdMap.put(Items.MUSIC_DISC_BLOCKS, 2258); // record blocks
        legacyItemIdMap.put(Items.MUSIC_DISC_CHIRP, 2259); // record chirp
        legacyItemIdMap.put(Items.MUSIC_DISC_FAR, 2260); // record far
        legacyItemIdMap.put(Items.MUSIC_DISC_MALL, 2261); // record mall
        legacyItemIdMap.put(Items.MUSIC_DISC_MELLOHI, 2262); // record mellohi
        legacyItemIdMap.put(Items.MUSIC_DISC_STAL, 2263); // record stal
        legacyItemIdMap.put(Items.MUSIC_DISC_STRAD, 2264); // record strad
        legacyItemIdMap.put(Items.MUSIC_DISC_WARD, 2265); // record ward
        legacyItemIdMap.put(Items.MUSIC_DISC_11, 2266); // record 11
        legacyItemIdMap.put(Items.MUSIC_DISC_WAIT, 2267); // record wait

        legacyBlockIdMap.put(Blocks.AIR, 0);
        legacyBlockIdMap.put(Blocks.STONE, 1);
        legacyBlockIdMap.put(Blocks.GRASS, 2);
        legacyBlockIdMap.put(Blocks.DIRT, 3);
        legacyBlockIdMap.put(Blocks.COBBLESTONE, 4);
        legacyBlockIdMap.put(Blocks.OAK_PLANKS, 5); // oak planks
        legacyBlockIdMap.put(Blocks.OAK_SAPLING, 6); // oak sapling
        legacyBlockIdMap.put(Blocks.BEDROCK, 7);
        legacyBlockIdMap.put(Blocks.WATER, 9);
        legacyBlockIdMap.put(Blocks.LAVA, 11);
        legacyBlockIdMap.put(Blocks.SAND, 12);
        legacyBlockIdMap.put(Blocks.GRAVEL, 13);
        legacyBlockIdMap.put(Blocks.GOLD_ORE, 14);
        legacyBlockIdMap.put(Blocks.IRON_ORE, 15);
        legacyBlockIdMap.put(Blocks.COAL_ORE, 16);
        legacyBlockIdMap.put(Blocks.OAK_LOG, 17); // oak log
        legacyBlockIdMap.put(Blocks.OAK_LEAVES, 18); // oak leaves
        legacyBlockIdMap.put(Blocks.SPONGE, 19);
        legacyBlockIdMap.put(Blocks.GLASS, 20);
        legacyBlockIdMap.put(Blocks.LAPIS_ORE, 21);
        legacyBlockIdMap.put(Blocks.LAPIS_BLOCK, 22);
        legacyBlockIdMap.put(Blocks.DISPENSER, 23);
        legacyBlockIdMap.put(Blocks.SANDSTONE, 24);
        legacyBlockIdMap.put(Blocks.NOTE_BLOCK, 25); // note block
        legacyBlockIdMap.put(Blocks.RED_BED, 26); // white bed
        legacyBlockIdMap.put(Blocks.POWERED_RAIL, 27); // powered rail
        legacyBlockIdMap.put(Blocks.DETECTOR_RAIL, 28);
        legacyBlockIdMap.put(Blocks.STICKY_PISTON, 29);
        legacyBlockIdMap.put(Blocks.COBWEB, 30); // web
        legacyBlockIdMap.put(Blocks.TALL_GRASS, 31); // tall grass
        legacyBlockIdMap.put(Blocks.DEAD_BUSH, 32); // dead bush
        legacyBlockIdMap.put(Blocks.PISTON, 33);
        legacyBlockIdMap.put(Blocks.PISTON_HEAD, 34);
        legacyBlockIdMap.put(Blocks.WHITE_WOOL, 35); // white wool
        legacyBlockIdMap.put(Blocks.DANDELION, 37); // dandelion / yellow flower
        legacyBlockIdMap.put(Blocks.POPPY, 38); // poppy / red flower
        legacyBlockIdMap.put(Blocks.BROWN_MUSHROOM, 39);
        legacyBlockIdMap.put(Blocks.RED_MUSHROOM, 40);
        legacyBlockIdMap.put(Blocks.GOLD_BLOCK, 41);
        legacyBlockIdMap.put(Blocks.IRON_BLOCK, 42);
        legacyBlockIdMap.put(Blocks.STONE_SLAB, 44);
        legacyBlockIdMap.put(Blocks.BRICKS, 45); // bricks
        legacyBlockIdMap.put(Blocks.TNT, 46);
        legacyBlockIdMap.put(Blocks.BOOKSHELF, 47);
        legacyBlockIdMap.put(Blocks.MOSSY_COBBLESTONE, 48);
        legacyBlockIdMap.put(Blocks.OBSIDIAN, 49);
        legacyBlockIdMap.put(Blocks.TORCH, 50);
        legacyBlockIdMap.put(Blocks.FIRE, 51);
        legacyBlockIdMap.put(Blocks.SPAWNER, 52);
        legacyBlockIdMap.put(Blocks.OAK_STAIRS, 53);
        legacyBlockIdMap.put(Blocks.CHEST, 54);
        legacyBlockIdMap.put(Blocks.REDSTONE_WIRE, 55);
        legacyBlockIdMap.put(Blocks.DIAMOND_ORE, 56);
        legacyBlockIdMap.put(Blocks.DIAMOND_BLOCK, 57);
        legacyBlockIdMap.put(Blocks.CRAFTING_TABLE, 58);
        legacyBlockIdMap.put(Blocks.WHEAT, 59);
        legacyBlockIdMap.put(Blocks.FARMLAND, 60);
        legacyBlockIdMap.put(Blocks.FURNACE, 61);
        legacyBlockIdMap.put(Blocks.SIGN, 63); // sign
        legacyBlockIdMap.put(Blocks.OAK_DOOR, 64);
        legacyBlockIdMap.put(Blocks.LADDER, 65);
        legacyBlockIdMap.put(Blocks.RAIL, 66);
        legacyBlockIdMap.put(Blocks.COBBLESTONE_STAIRS, 67); // cobblestone stairs
        legacyBlockIdMap.put(Blocks.WALL_SIGN, 68);
        legacyBlockIdMap.put(Blocks.LEVER, 69);
        legacyBlockIdMap.put(Blocks.STONE_PRESSURE_PLATE, 70);
        legacyBlockIdMap.put(Blocks.IRON_DOOR, 71);
        legacyBlockIdMap.put(Blocks.OAK_PRESSURE_PLATE, 72); // oak pressure plate
        legacyBlockIdMap.put(Blocks.REDSTONE_ORE, 73);
        legacyBlockIdMap.put(Blocks.REDSTONE_TORCH, 76);
        legacyBlockIdMap.put(Blocks.STONE_BUTTON, 77);
        legacyBlockIdMap.put(Blocks.SNOW, 78);
        legacyBlockIdMap.put(Blocks.ICE, 79);
        legacyBlockIdMap.put(Blocks.SNOW_BLOCK, 80); // snow block
        legacyBlockIdMap.put(Blocks.CACTUS, 81);
        legacyBlockIdMap.put(Blocks.CLAY, 82);
        legacyBlockIdMap.put(Blocks.JUKEBOX, 84);
        legacyBlockIdMap.put(Blocks.OAK_FENCE, 85);
        legacyBlockIdMap.put(Blocks.PUMPKIN, 86);
        legacyBlockIdMap.put(Blocks.NETHERRACK, 87);
        legacyBlockIdMap.put(Blocks.SOUL_SAND, 88);
        legacyBlockIdMap.put(Blocks.GLOWSTONE, 89);
        legacyBlockIdMap.put(Blocks.NETHER_PORTAL, 90);
        legacyBlockIdMap.put(Blocks.JACK_O_LANTERN, 91); // lit pumpkin
        legacyBlockIdMap.put(Blocks.CAKE, 92);
        legacyBlockIdMap.put(Blocks.REPEATER, 93); // repeater
        legacyBlockIdMap.put(Blocks.WHITE_STAINED_GLASS, 95); // white stained glass
        legacyBlockIdMap.put(Blocks.OAK_TRAPDOOR, 96); // oak trapdoor
        legacyBlockIdMap.put(Blocks.INFESTED_STONE, 97); // infested stone
        legacyBlockIdMap.put(Blocks.STONE_BRICKS, 98); // stone brick
        legacyBlockIdMap.put(Blocks.BROWN_MUSHROOM_BLOCK, 99);
        legacyBlockIdMap.put(Blocks.RED_MUSHROOM_BLOCK, 100);
        legacyBlockIdMap.put(Blocks.IRON_BARS, 101);
        legacyBlockIdMap.put(Blocks.GLASS_PANE, 102);
        legacyBlockIdMap.put(Blocks.MELON, 103);
        legacyBlockIdMap.put(Blocks.PUMPKIN_STEM, 104);
        legacyBlockIdMap.put(Blocks.MELON_STEM, 105);
        legacyBlockIdMap.put(Blocks.VINE, 106);
        legacyBlockIdMap.put(Blocks.OAK_FENCE_GATE, 107);
        legacyBlockIdMap.put(Blocks.BRICK_STAIRS, 108);
        legacyBlockIdMap.put(Blocks.STONE_BRICK_STAIRS, 109);
        legacyBlockIdMap.put(Blocks.MYCELIUM, 110);
        legacyBlockIdMap.put(Blocks.LILY_PAD, 111); // water lily
        legacyBlockIdMap.put(Blocks.NETHER_BRICKS, 112); // nether bricks
        legacyBlockIdMap.put(Blocks.NETHER_BRICK_FENCE, 113);
        legacyBlockIdMap.put(Blocks.NETHER_BRICK_STAIRS, 114);
        legacyBlockIdMap.put(Blocks.NETHER_WART, 115);
        legacyBlockIdMap.put(Blocks.ENCHANTING_TABLE, 116);
        legacyBlockIdMap.put(Blocks.BREWING_STAND, 117);
        legacyBlockIdMap.put(Blocks.CAULDRON, 118);
        legacyBlockIdMap.put(Blocks.END_PORTAL, 119);
        legacyBlockIdMap.put(Blocks.END_PORTAL_FRAME, 120);
        legacyBlockIdMap.put(Blocks.END_STONE, 121);
        legacyBlockIdMap.put(Blocks.DRAGON_EGG, 122);
        legacyBlockIdMap.put(Blocks.REDSTONE_LAMP, 123);
        legacyBlockIdMap.put(Blocks.OAK_SLAB, 126); // oak slab
        legacyBlockIdMap.put(Blocks.COCOA, 127);
        legacyBlockIdMap.put(Blocks.SANDSTONE_STAIRS, 128);
        legacyBlockIdMap.put(Blocks.EMERALD_ORE, 129);
        legacyBlockIdMap.put(Blocks.ENDER_CHEST, 130);
        legacyBlockIdMap.put(Blocks.TRIPWIRE_HOOK, 131);
        legacyBlockIdMap.put(Blocks.TRIPWIRE, 132);
        legacyBlockIdMap.put(Blocks.EMERALD_BLOCK, 133);
        legacyBlockIdMap.put(Blocks.SPRUCE_STAIRS, 134);
        legacyBlockIdMap.put(Blocks.BIRCH_STAIRS, 135);
        legacyBlockIdMap.put(Blocks.JUNGLE_STAIRS, 136);
        legacyBlockIdMap.put(Blocks.COMMAND_BLOCK, 137);
        legacyBlockIdMap.put(Blocks.BEACON, 138);
        legacyBlockIdMap.put(Blocks.COBBLESTONE_WALL, 139);
        legacyBlockIdMap.put(Blocks.FLOWER_POT, 140);
        legacyBlockIdMap.put(Blocks.CARROTS, 141);
        legacyBlockIdMap.put(Blocks.POTATOES, 142);
        legacyBlockIdMap.put(Blocks.OAK_BUTTON, 143); // oak button
        legacyBlockIdMap.put(Blocks.SKELETON_SKULL, 144); // skeleton skull
        legacyBlockIdMap.put(Blocks.ANVIL, 145);
        legacyBlockIdMap.put(Blocks.TRAPPED_CHEST, 146);
        legacyBlockIdMap.put(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, 147);
        legacyBlockIdMap.put(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 148);
        legacyBlockIdMap.put(Blocks.COMPARATOR, 149);
        legacyBlockIdMap.put(Blocks.DAYLIGHT_DETECTOR, 151);
        legacyBlockIdMap.put(Blocks.REDSTONE_BLOCK, 152);
        legacyBlockIdMap.put(Blocks.NETHER_QUARTZ_ORE, 153); // nether quarz ore
        legacyBlockIdMap.put(Blocks.HOPPER, 154);
        legacyBlockIdMap.put(Blocks.QUARTZ_BLOCK, 155);
        legacyBlockIdMap.put(Blocks.QUARTZ_STAIRS, 156);
        legacyBlockIdMap.put(Blocks.ACTIVATOR_RAIL, 157);
        legacyBlockIdMap.put(Blocks.DROPPER, 158);
        legacyBlockIdMap.put(Blocks.WHITE_STAINED_GLASS_PANE, 160); // white stained glass pane
        legacyBlockIdMap.put(Blocks.DARK_OAK_LEAVES, 161); // dark oak leaves
        legacyBlockIdMap.put(Blocks.DARK_OAK_LOG, 162); // dark oak log
        legacyBlockIdMap.put(Blocks.ACACIA_STAIRS, 163);
        legacyBlockIdMap.put(Blocks.DARK_OAK_STAIRS, 164);
        legacyBlockIdMap.put(Blocks.SLIME_BLOCK, 165);
        legacyBlockIdMap.put(Blocks.BARRIER, 166);
        legacyBlockIdMap.put(Blocks.IRON_TRAPDOOR, 167);
        legacyBlockIdMap.put(Blocks.PRISMARINE, 168);
        legacyBlockIdMap.put(Blocks.SEA_LANTERN, 169);
        legacyBlockIdMap.put(Blocks.HAY_BLOCK, 170);
        legacyBlockIdMap.put(Blocks.WHITE_CARPET, 171); // white carpet
        legacyBlockIdMap.put(Blocks.TERRACOTTA, 172);
        legacyBlockIdMap.put(Blocks.COAL_BLOCK, 173);
        legacyBlockIdMap.put(Blocks.PACKED_ICE, 174);
        legacyBlockIdMap.put(Blocks.WHITE_BANNER, 176); // white banner
        legacyBlockIdMap.put(Blocks.WHITE_WALL_BANNER, 177); // white wall banner
        legacyBlockIdMap.put(Blocks.RED_SANDSTONE, 179);
        legacyBlockIdMap.put(Blocks.RED_SANDSTONE_STAIRS, 180);
        legacyBlockIdMap.put(Blocks.SPRUCE_FENCE_GATE, 183);
        legacyBlockIdMap.put(Blocks.BIRCH_FENCE_GATE, 184);
        legacyBlockIdMap.put(Blocks.JUNGLE_FENCE_GATE, 185);
        legacyBlockIdMap.put(Blocks.DARK_OAK_FENCE_GATE, 186);
        legacyBlockIdMap.put(Blocks.ACACIA_FENCE_GATE, 187);
        legacyBlockIdMap.put(Blocks.SPRUCE_FENCE, 188);
        legacyBlockIdMap.put(Blocks.BIRCH_FENCE, 189);
        legacyBlockIdMap.put(Blocks.JUNGLE_FENCE, 190);
        legacyBlockIdMap.put(Blocks.DARK_OAK_FENCE, 191);
        legacyBlockIdMap.put(Blocks.ACACIA_FENCE, 192);
        legacyBlockIdMap.put(Blocks.SPRUCE_DOOR, 193);
        legacyBlockIdMap.put(Blocks.BIRCH_DOOR, 194);
        legacyBlockIdMap.put(Blocks.JUNGLE_DOOR, 195);
        legacyBlockIdMap.put(Blocks.ACACIA_DOOR, 196);
        legacyBlockIdMap.put(Blocks.DARK_OAK_DOOR, 197);
        legacyBlockIdMap.put(Blocks.END_ROD, 198);
        legacyBlockIdMap.put(Blocks.CHORUS_PLANT, 199);
        legacyBlockIdMap.put(Blocks.CHORUS_FLOWER, 200);
        legacyBlockIdMap.put(Blocks.PURPUR_BLOCK, 201);
        legacyBlockIdMap.put(Blocks.PURPUR_PILLAR, 202);
        legacyBlockIdMap.put(Blocks.PURPUR_STAIRS, 203);
        legacyBlockIdMap.put(Blocks.PURPUR_SLAB, 205);
        legacyBlockIdMap.put(Blocks.END_STONE_BRICKS, 206); // end stone bricks
        legacyBlockIdMap.put(Blocks.BEETROOTS, 207);
        legacyBlockIdMap.put(Blocks.GRASS_PATH, 208);
        legacyBlockIdMap.put(Blocks.END_GATEWAY, 209);
        legacyBlockIdMap.put(Blocks.REPEATING_COMMAND_BLOCK, 210);
        legacyBlockIdMap.put(Blocks.CHAIN_COMMAND_BLOCK, 211);
        legacyBlockIdMap.put(Blocks.FROSTED_ICE, 212);
        legacyBlockIdMap.put(Blocks.MAGMA_BLOCK, 213); // magma block
        legacyBlockIdMap.put(Blocks.NETHER_WART_BLOCK, 214);
        legacyBlockIdMap.put(Blocks.RED_NETHER_BRICKS, 215); // red nether brick
        legacyBlockIdMap.put(Blocks.BONE_BLOCK, 216);
        legacyBlockIdMap.put(Blocks.STRUCTURE_VOID, 217);
        legacyBlockIdMap.put(Blocks.OBSERVER, 218);
        legacyBlockIdMap.put(Blocks.WHITE_SHULKER_BOX, 219);
        legacyBlockIdMap.put(Blocks.ORANGE_SHULKER_BOX, 220);
        legacyBlockIdMap.put(Blocks.MAGENTA_SHULKER_BOX, 221);
        legacyBlockIdMap.put(Blocks.LIGHT_BLUE_SHULKER_BOX, 222);
        legacyBlockIdMap.put(Blocks.YELLOW_SHULKER_BOX, 223);
        legacyBlockIdMap.put(Blocks.LIME_SHULKER_BOX, 224);
        legacyBlockIdMap.put(Blocks.PINK_SHULKER_BOX, 225);
        legacyBlockIdMap.put(Blocks.GRAY_SHULKER_BOX, 226);
        legacyBlockIdMap.put(Blocks.LIGHT_GRAY_SHULKER_BOX, 227); // light gray shulker box
        legacyBlockIdMap.put(Blocks.CYAN_SHULKER_BOX, 228);
        legacyBlockIdMap.put(Blocks.PURPLE_SHULKER_BOX, 229);
        legacyBlockIdMap.put(Blocks.BLUE_SHULKER_BOX, 230);
        legacyBlockIdMap.put(Blocks.BROWN_SHULKER_BOX, 231);
        legacyBlockIdMap.put(Blocks.GREEN_SHULKER_BOX, 232);
        legacyBlockIdMap.put(Blocks.RED_SHULKER_BOX, 233);
        legacyBlockIdMap.put(Blocks.BLACK_SHULKER_BOX, 234);
        legacyBlockIdMap.put(Blocks.WHITE_TERRACOTTA, 235); // white terracotta
        legacyBlockIdMap.put(Blocks.ORANGE_TERRACOTTA, 236); // orange
        legacyBlockIdMap.put(Blocks.MAGENTA_TERRACOTTA, 237); // magenta
        legacyBlockIdMap.put(Blocks.LIGHT_BLUE_TERRACOTTA, 238); // light blue
        legacyBlockIdMap.put(Blocks.YELLOW_TERRACOTTA, 239); // yellow
        legacyBlockIdMap.put(Blocks.LIME_TERRACOTTA, 240); // lime
        legacyBlockIdMap.put(Blocks.PINK_TERRACOTTA, 241); // pink
        legacyBlockIdMap.put(Blocks.GRAY_TERRACOTTA, 242); // gray
        legacyBlockIdMap.put(Blocks.LIGHT_GRAY_TERRACOTTA, 243); // light gray
        legacyBlockIdMap.put(Blocks.CYAN_TERRACOTTA, 244); // cyan
        legacyBlockIdMap.put(Blocks.PURPLE_TERRACOTTA, 245); // purple
        legacyBlockIdMap.put(Blocks.BLUE_TERRACOTTA, 246); // blue
        legacyBlockIdMap.put(Blocks.BROWN_TERRACOTTA, 247); // brown
        legacyBlockIdMap.put(Blocks.GREEN_TERRACOTTA, 248); // green
        legacyBlockIdMap.put(Blocks.RED_TERRACOTTA, 249); // red
        legacyBlockIdMap.put(Blocks.BLACK_TERRACOTTA, 250); // black
        legacyBlockIdMap.put(Blocks.WHITE_CONCRETE, 251); // white concrete
        legacyBlockIdMap.put(Blocks.WHITE_CONCRETE_POWDER, 252); // white concrete powder
        legacyBlockIdMap.put(Blocks.STRUCTURE_BLOCK, 255);
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

            Block bo = IRegistry.BLOCK.get(new ResourceLocation(blockName.toLowerCase()));

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
        return legacyItemIdMap.inverse().getOrDefault(id, Items.AIR);
    }

    public static int getLegacyItemId(Item item) {
        return legacyItemIdMap.getOrDefault(item, 0);
    }

    public static Block getBlockFromLegacyId(int id) {
        return legacyBlockIdMap.inverse().getOrDefault(id, Blocks.AIR);
    }

    public static int getLegacyBlockId(Block block) {
        return legacyBlockIdMap.getOrDefault(block, 0);
    }

    public static int getLegacyDimensionId(DimensionType dimensionType) {
        if (dimensionType == DimensionType.OVERWORLD) {
            return 0;
        } else if (dimensionType == DimensionType.NETHER) {
            return -1;
        } else if (dimensionType == DimensionType.THE_END) {
            return 1;
        } else {
            return -2;
        }
    }
}
