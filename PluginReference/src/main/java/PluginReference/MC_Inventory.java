package PluginReference;

/**
 * Represents an inventory
 */
public interface MC_Inventory {

    /**
     * Retrieve the size of the inventory.
     * @return size of the inventory.
     */
    int getSize();

    /**
     * Get the item stack at the given index.
     *
     * @param index the index
     * @return the item stack
     */
    MC_ItemStack getItemStackAt(int index);

    /**
     * Set the item stack at the given index.
     *
     * @param index the index
     * @param itemStack the item stack
     */
    void setItemStackAt(int index, MC_ItemStack itemStack);

    /**
     * Get the title of the inventory.
     *
     * @return the title
     */
    String getTitle();
}
