package PluginReference;

/**
 * Represents a Minecraft Zombie
 */
public interface MC_Zombie extends MC_LivingEntity {
    /**
     * Check if a Villager zombie
     *
     * @return True if a villager, False otherwise
     */
    @Deprecated
    boolean isVillager();
}
