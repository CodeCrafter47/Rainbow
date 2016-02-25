package org.projectrainbow.mixins;

import PluginReference.MC_Container;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_World;
import net.minecraft.src.BlockDispenser;
import net.minecraft.src.BlockPos;
import net.minecraft.src.BlockSourceImpl;
import net.minecraft.src.TileEntityDispenser;
import net.minecraft.src.World;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockDispenser.class)
public class MixinBlockDispenser {

    @Inject(method = "dispense", at = @At("HEAD"), cancellable = true)
    private void dispense(World var1, BlockPos var2, CallbackInfo callbackInfo) {
        BlockSourceImpl var3 = new BlockSourceImpl(var1, var2);
        TileEntityDispenser var4 = (TileEntityDispenser)var3.getBlockTileEntity();
        int var5 = var4.getDispenseSlot();

        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptDispense(new MC_Location(var2.getX(), var2.getY(), var2.getZ(), ((MC_World)var1).getDimension()), var5, (MC_Container) var4, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
