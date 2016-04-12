package PluginReference;

/**
 * An entity that is a projectile. These are snowballs, enderpearls, expbottles,
 * eggs, potions as well as all kinds of arrows.
 *
 */
public interface MC_Projectile extends MC_Entity {

    /**
     * Get the entity that threw/ shot the projectile.
     * @return entity
     */
    MC_Entity getProjectileSource();
}
