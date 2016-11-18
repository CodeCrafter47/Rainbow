package org.projectrainbow;

import PluginReference.*;
import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.MobEffects;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PluginHelper {
    public static BiMap<EnumFacing, MC_DirectionNESWUD> directionMap = HashBiMap.create();
    public static BiMap<MC_GameRuleType, String> gameRuleMap = HashBiMap.create();
    public static BiMap<GameType, MC_GameMode> gamemodeMap = HashBiMap.create();
    public static BiMap<Class<? extends Entity>, MC_EntityType> entityMap = HashBiMap.create();
    public static BiMap<Potion, MC_PotionEffectType> potionMap = HashBiMap.create();
    public static BiMap<Short, MC_EnchantmentType> enchantmentMap = HashBiMap.create();
    public static BiMap<Biome, MC_WorldBiomeType> biomeMap = HashBiMap.create();
    public static BiMap<EnumHand, MC_Hand> handMap = HashBiMap.create();
    public static BiMap<MC_AttributeType, IAttribute> attributeMap = HashBiMap.create();
    public static BiMap<MC_AttributeModifier.Operator, Integer> operatorMap = HashBiMap.create();

    public static MC_EntityType getEntityType(Class<? extends Entity> clazz) {
        if (EntityPlayerMP.class.isAssignableFrom(clazz)) {
            return MC_EntityType.PLAYER;
        } else {
            return Objects.firstNonNull(entityMap.get(clazz), MC_EntityType.UNSPECIFIED);
        }
    }

    public static List<MC_ItemStack> copyInvList(List<ItemStack> items) {
        int size = items.size();
        List<MC_ItemStack> list = new ArrayList<MC_ItemStack>();
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
        type = Objects.firstNonNull(type, MC_PotionEffectType.UNSPECIFIED);
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
        entityMap.put(EntityMinecartEmpty.class, MC_EntityType.MINECART);
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

        enchantmentMap.put((short) 0, MC_EnchantmentType.PROTECTION);
        enchantmentMap.put((short) 1, MC_EnchantmentType.FIRE_PROTECTION);
        enchantmentMap.put((short) 2, MC_EnchantmentType.FEATHER_FALLING);
        enchantmentMap.put((short) 3, MC_EnchantmentType.BLAST_PROTECTION);
        enchantmentMap.put((short) 4, MC_EnchantmentType.PROJECTILE_PROTECTION);
        enchantmentMap.put((short) 5, MC_EnchantmentType.RESPIRATION);
        enchantmentMap.put((short) 6, MC_EnchantmentType.AQUA_AFFINITY);
        enchantmentMap.put((short) 7, MC_EnchantmentType.THORNS);
        enchantmentMap.put((short) 8, MC_EnchantmentType.DEPTH_STRIDER);
        enchantmentMap.put((short) 9, MC_EnchantmentType.FROST_WALKER);
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
        enchantmentMap.put((short) 70, MC_EnchantmentType.MENDING);

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
        } else if (damageSource == DamageSource.generic) {
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
        } else if (damageSource == DamageSource.outOfWorld) {
            return MC_DamageType.OUT_OF_WORLD;
        } else if (damageSource == DamageSource.starve) {
            return MC_DamageType.STARVE;
        } else if (damageSource == DamageSource.wither) {
            return MC_DamageType.WITHER;
        } else if (damageSource == DamageSource.dragonBreath) {
            return MC_DamageType.DRAGON_BREATH;
        } else if (damageSource == DamageSource.hotFloor) {
            return MC_DamageType.HOT_FLOOR;
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
        if (cause == null) return DamageSource.generic;
        else if (cause == MC_DamageType.ANVIL) return DamageSource.anvil;
        else if (cause == MC_DamageType.CACTUS) return DamageSource.cactus;
        else if (cause == MC_DamageType.DROWN) return DamageSource.drown;
        else if (cause == MC_DamageType.FALL) return DamageSource.fall;
        else if (cause == MC_DamageType.FALLING_BLOCK) return DamageSource.fallingBlock;
        else if (cause == MC_DamageType.GENERIC) return DamageSource.generic;
        else if (cause == MC_DamageType.IN_FIRE) return DamageSource.inFire;
        else if (cause == MC_DamageType.IN_WALL) return DamageSource.inWall;
        else if (cause == MC_DamageType.LAVA) return DamageSource.lava;
        else if (cause == MC_DamageType.LIGHTING_BOLT) return DamageSource.lightningBolt;
        else if (cause == MC_DamageType.MAGIC) return DamageSource.magic;
        else if (cause == MC_DamageType.ON_FIRE) return DamageSource.onFire;
        else if (cause == MC_DamageType.OUT_OF_WORLD) return DamageSource.outOfWorld;
        else if (cause == MC_DamageType.STARVE) return DamageSource.starve;
        else if (cause == MC_DamageType.WITHER) return DamageSource.wither;
        else if (cause == MC_DamageType.DRAGON_BREATH) return DamageSource.dragonBreath;
        else if (cause == MC_DamageType.HOT_FLOOR) return DamageSource.hotFloor;
        else if (cause == MC_DamageType.MOB) return DamageSource.causeMobDamage(null);
        else if (cause == MC_DamageType.PLAYER) return DamageSource.causePlayerDamage(null);
        else if (cause == MC_DamageType.ARROW) return DamageSource.causeArrowDamage(null, null);
        else if (cause == MC_DamageType.FIREBALL) return DamageSource.causeFireballDamage(null, null);
        else if (cause == MC_DamageType.THROWN) return DamageSource.causeThrownDamage(null, null);
        else if (cause == MC_DamageType.INDIRECT_MAGIC) return DamageSource.causeIndirectMagicDamage(null, null);
        else if (cause == MC_DamageType.THORNS) return DamageSource.causeThornsDamage(null);
        else if (cause == MC_DamageType.EXPLOSION) return DamageSource.causeExplosionDamage((EntityLivingBase) null);
        else if (cause == MC_DamageType.EXPLOSION_PLAYER) return DamageSource.causeExplosionDamage((Explosion) null);
        else return DamageSource.generic;

    }
}
