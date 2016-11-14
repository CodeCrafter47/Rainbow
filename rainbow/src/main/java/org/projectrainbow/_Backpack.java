package org.projectrainbow;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class _Backpack extends InventoryBasic {

    public _Backpack() {
        super("Backpack", false, 54);
    }

    public void loadInventoryFromNBT(NBTTagList var1) {
        int var2;
        for (var2 = 0; var2 < this.getSizeInventory(); ++var2) {
            this.setInventorySlotContents(var2, (ItemStack) null);
        }

        for (var2 = 0; var2 < var1.tagCount(); ++var2) {
            NBTTagCompound var3 = var1.getCompoundTagAt(var2);
            int var4 = var3.getByte("Slot") & 255;
            if (var4 >= 0 && var4 < this.getSizeInventory()) {
                this.setInventorySlotContents(var4, new ItemStack(var3));
            }
        }

    }

    public NBTTagList saveInventoryToNBT() {
        NBTTagList var1 = new NBTTagList();

        for (int var2 = 0; var2 < this.getSizeInventory(); ++var2) {
            ItemStack var3 = this.getStackInSlot(var2);
            if (var3 != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var2);
                var3.writeToNBT(var4);
                var1.appendTag(var4);
            }
        }

        return var1;
    }
}
