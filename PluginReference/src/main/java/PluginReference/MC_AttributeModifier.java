package PluginReference;

import java.util.UUID;

/**
 * An attribute modifier.
 * <p>
 * New instances of this class can be created using the
 * {@link MC_Server#createAttributeModifier} factory method.
 */
public interface MC_AttributeModifier {

    /**
     * Get the operator used by this attribute modifier.
     *
     * @return the operator
     */
    Operator getOperator();

    /**
     * Get the value of this attribute modifier.
     *
     * @return the value
     */
    double getValue();

    /**
     * Get the name of this attribute modifier.
     *
     * @return the name
     */
    String getName();

    /**
     * Get the uuid of the attribute modifier. Each attribute modifier must have
     * a unique UUID
     *
     * @return the UUID
     */
    UUID getUUID();

    /**
     * Operator of the attribute modifier.
     */
    enum Operator {

        /**
         * Add a fixed value to the base value.
         */
        ADD_CONSTANT,

        /**
         * Add a scalar amount relative to the value of the attribute after
         * applying the ADD_CONSTANT modifiers.
         */
        ADD_SCALAR_BASE,

        /**
         * Increment the value of the attribute by a scalar amount. These
         * modifiers are applied after the ADD_CONSTANT and ADD_SCALAR_BASE
         * modifiers.
         */
        ADD_SCALAR
    }
}
