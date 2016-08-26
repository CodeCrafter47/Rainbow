package org.projectrainbow;

import PluginReference.MC_InventoryGUI;
import PluginReference.MC_Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class GUIContainer extends ContainerChest {
    private final GUIInventory gui;

    public GUIContainer(IInventory playerInv, GUIInventory gui, EntityPlayer player) {
        super(playerInv, gui, player);
        this.gui = gui;
    }

    @Nullable
    public ItemStack slotClick(int slot, int flags, ClickType var3, EntityPlayer var4) {
        if (slot < gui.getSizeInventory()) {
            MC_InventoryGUI.ClickHandler clickHandler = gui.getClickHandler(slot);
            if (clickHandler != null) {
                try {
                    clickHandler.onSlotClicked(((MC_Player) var4));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return new ItemStack(Blocks.STONE, Integer.MAX_VALUE - 1); // this 'should' cancel the transaction
    }
}
