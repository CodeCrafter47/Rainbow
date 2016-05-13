package PluginReference;

import java.util.Collection;

/**
 * An attribute associated with a specific entity.
 */
public interface MC_Attribute {

    /**
     * Get the type of this attribute.
     *
     * @return the type
     */
    MC_AttributeType getType();

    /**
     * Get the base value of this attribute. The base value of an attribute is
     * different for different entities.
     *
     * @return the base value
     */
    double getBaseValue();

    /**
     * Set the base value of this attribute.
     *
     * @param baseValue the new base value
     */
    void setBaseValue(double baseValue);

    /**
     * Get the value of this attribute after applying the modifiers.
     *
     * @return the effective value
     */
    double getEffectiveValue();

    /**
     * Get all modifiers of this attribute.
     *
     * @return an immutable collection of the modifiers
     */
    Collection<? extends MC_AttributeModifier> getModifiers();

    /**
     * Remove a modifier from this attribute.
     *
     * @param modifier the modifier to remove
     */
    void removeModifier(MC_AttributeModifier modifier);

    /**
     * Add a modifier to this attribute.
     *
     * @param modifier the modifier to add
     */
    void addModifier(MC_AttributeModifier modifier);
}
