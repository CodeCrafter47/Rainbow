package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_World;
import com.google.common.base.Objects;
import net.minecraft.src.EntityMinecartHopper;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IHopper;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityHopper;
import net.minecraft.src.qg;
import org.projectrainbow.EmptyItemStack;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityHopper.class)
public class MixinTileEntityHopper {

    @Inject(method = "a(Lnet/minecraft/src/qg;Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/EnumFacing;)Lnet/minecraft/src/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void onHopperItemReceived(qg argInventory, ItemStack itemStack, EnumFacing facing, CallbackInfoReturnable<ItemStack> callbackInfo) {
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
