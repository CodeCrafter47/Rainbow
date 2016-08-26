package org.projectrainbow;

import PluginReference.MC_InventoryGUI;
import PluginReference.MC_ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IInteractionObject;

public class GUIInventory extends InventoryBasic implements MC_InventoryGUI, IInteractionObject {
    private ClickHandler[] clickHandlers;

    public GUIInventory(String title, int size) {
        super(title, true, size);
        clickHandlers = new ClickHandler[size];
    }

    @Override
    public int getSize() {
        return getSizeInventory();
    }

    @Override
    public MC_ItemStack getItemStackAt(int index) {
        return ((MC_ItemStack) (Object) getStackInSlot(index));
    }

    @Override
    public void setItemStackAt(int index, MC_ItemStack itemStack) {
        setInventorySlotContents(index, itemStack == EmptyItemStack.getInstance() ? null : ((ItemStack) (Object) itemStack));
    }

    public String getTitle() {
        return getName();
    }

    @Override
    public void setClickHandler(int slotIndex, ClickHandler clickHandler) {
        this.clickHandlers[slotIndex] = clickHandler;
    }

    @Override
    public ClickHandler getClickHandler(int slotIndex) {
        return clickHandlers[slotIndex];
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new GUIContainer(inventoryPlayer, this, entityPlayer);
    }

    @Override
    public String getGuiID() {
        return "minecraft:container";
    }
}
