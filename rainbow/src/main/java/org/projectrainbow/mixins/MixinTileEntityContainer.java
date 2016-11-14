package org.projectrainbow.mixins;

import PluginReference.MC_Container;
import PluginReference.MC_ItemStack;
import com.google.common.base.Objects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityLockable;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({TileEntityHopper.class, TileEntityFurnace.class, TileEntityBrewingStand.class, TileEntityBeacon.class, TileEntityDispenser.class, TileEntityLockable.class, TileEntityChest.class})
public abstract class MixinTileEntityContainer implements MC_Container, IInventory {
    @Override
    public int getSize() {
        return getSizeInventory();
    }

    @Override
    public MC_ItemStack getItemAtIdx(int var1) {
        return (MC_ItemStack) (Object) getStackInSlot(var1);
    }

    @Override
    public void setItemAtIdx(int var1, MC_ItemStack var2) {
        setInventorySlotContents(var1, PluginHelper.getItemStack(var2));
        markDirty();
    }

    @Override
    public void clearContents() {
        clear();
        markDirty();
    }
}
