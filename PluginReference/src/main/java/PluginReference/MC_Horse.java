package PluginReference;

/**
 * Horse Entity
 */
public interface MC_Horse extends MC_Animal {
    @Deprecated
    MC_HorseType getHorseType();

    /**
     * Sets the horse type (Horse, Donkey, etc)
     *
     * @param arg Horse Type
     */
    @Deprecated
    void setHorseType(MC_HorseType arg);

    /**
     * Get Horse Variant
     *
     * @return Horse Variant
     */
    MC_HorseVariant getHorseVariant();

    /**
     * Sets the horse variant (Chestnut, Creamy, etc)
     *
     * @param arg Horse Variant
     */
    void setHorseVariant(MC_HorseVariant arg);

    /**
     * Check if horse has chest
     *
     * @return True if has chest, False otherwise
     */
    boolean hasChest();

    /**
     * Sets whether horse has chest
     *
     * @param flag True for chest, False otherwise
     */
    void setHasChest(boolean flag);

    /**
     * Sets whether horse is tamed.
     *
     * @param flag True for tamed, False otherwise
     */
    void setTamed(boolean flag);

    /**
     * Check if horse is tamed
     *
     * @return True if horse is tamed, False otherwise
     */
    boolean isTame();

    /**
     * Gets temper of Horse
     *
     * @return Temper value
     */
    int getTemper();

    /**
     * Sets temper of horse
     *
     * @param val New Temper
     */
    void setTemper(int val);

    /**
     * Gets Owner UUID
     *
     * @return UUID of owner
     */
    String getOwnerUUID();

    /**
     * Sets Owning UUID
     *
     * @param strUUID UUID of owner
     */
    void setOwnerUUID(String strUUID);

    /**
     * Sets Owning Player
     *
     * @param plr Player Object
     */
    void setOwner(MC_Player plr);
}
