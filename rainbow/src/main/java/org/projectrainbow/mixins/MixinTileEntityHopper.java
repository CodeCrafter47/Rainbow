package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_World;
import com.google.common.base.Objects;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import org.projectrainbow.EmptyItemStack;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityHopper.class)
public class MixinTileEntityHopper {

    @Inject(method = "putStackInInventoryAllSlots", at = @At("HEAD"), cancellable = true)
    private static void onHopperItemReceived(IInventory argInventory, ItemStack itemStack, EnumFacing facing, CallbackInfoReturnable<ItemStack> callbackInfo) {
        MC_Location joeLoc = null;
        boolean doEvents = false;
        boolean isMinecartHopper = false;

        IHopper hopper;

        if (argInventory instanceof EntityMinecartHopper) {
            doEvents = true;
            isMinecartHopper = true;
            hopper = (IHopper) argInventory;
            joeLoc = new MC_Location(hopper.getXPos(), hopper.getYPos(), hopper.getZPos(),
                    ((MC_World)hopper.getWorld()).getDimension());
            joeLoc.x = (double) joeLoc.getBlockX();
            joeLoc.y = (double) joeLoc.getBlockY();
            joeLoc.z = (double) joeLoc.getBlockZ();
        } else if (argInventory instanceof TileEntityHopper) {
            doEvents = true;
            hopper = (IHopper) argInventory;
            joeLoc = new MC_Location(hopper.getXPos(), hopper.getYPos(), hopper.getZPos(),
                    ((MC_World)hopper.getWorld()).getDimension());
        }

        if (doEvents) {
            MC_EventInfo ei = new MC_EventInfo();


            Hooks.onAttemptHopperReceivingItem(joeLoc, Objects.firstNonNull((MC_ItemStack) (Object) itemStack, EmptyItemStack.getInstance()), isMinecartHopper, ei);


            if (ei.isCancelled) {
                callbackInfo.setReturnValue(itemStack);
            }
        }
    }
}
