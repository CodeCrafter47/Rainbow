package PluginReference;

/**
 * An inventory used as GUI. The clients cannot modify the content of the inventory.
 */
public interface MC_InventoryGUI extends MC_Inventory {

    /**
     * Set the action to be performed when the client clicks the slot.
     *
     * @param slotIndex   the index of the slot
     * @param clickHandler the action to be performed, can be null
     */
    void setClickHandler(int slotIndex, ClickHandler clickHandler);

    /**
     * Get the action to be performed when a slot is clicked.
     *
     * @param slotIndex the index of the slot
     * @return the action to be performed
     */
    ClickHandler getClickHandler(int slotIndex);

    interface ClickHandler {
        void onSlotClicked(MC_Player player);
    }
}
