package PluginReference;

/**
 * Represents a living entity.
 */
public interface MC_LivingEntity extends MC_Entity {

    /**
     * Get a specific attribute.
     *
     * @param type type of the attribute
     * @return the attribute
     */
    MC_Attribute getAttribute(MC_AttributeType type);
}
