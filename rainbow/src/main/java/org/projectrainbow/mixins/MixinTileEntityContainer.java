package org.projectrainbow.mixins;

import PluginReference.MC_Container;
import PluginReference.MC_ItemStack;
import com.google.common.base.Objects;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityBeacon;
import net.minecraft.src.TileEntityBrewingStand;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.TileEntityDispenser;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.TileEntityHopper;
import net.minecraft.src.TileEntityLockable;
import net.minecraft.src.qg;
import org.projectrainbow.EmptyItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({TileEntityHopper.class, TileEntityFurnace.class, TileEntityBrewingStand.class, TileEntityBeacon.class, TileEntityDispenser.class, TileEntityLockable.class, TileEntityChest.class})
public abstract class MixinTileEntityContainer implements MC_Container, qg {
    @Override
    public int getSize() {
        return getSizeInventory();
    }

    @Override
    public MC_ItemStack getItemAtIdx(int var1) {
        return Objects.firstNonNull((MC_ItemStack) (Object) getStackInSlot(var1), EmptyItemStack.getInstance());
    }

    @Override
    public void setItemAtIdx(int var1, MC_ItemStack var2) {
        setInventorySlotContents(var1, var2 instanceof EmptyItemStack ? null : (ItemStack) (Object) var2);
        markDirty();
    }

    @Override
    public void clearContents() {
        clear();
        markDirty();
    }
}
