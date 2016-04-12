package PluginReference;

/**
 * Primed TNT entity.
 */
public interface MC_PrimedTNT extends MC_Entity {

    /**
     * Get the entity that initiated the explosion.
     *
     * e.g. if a player causes the explosion by shooting an ignited arrow, this
     * return the player.
     *
     * @return the entity that initiated the explosion
     */
    MC_Entity getEntityWhichCausedExplosion();
}
