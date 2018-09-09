package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockFarmland.class)
public class MixinBlockFarmland {

    @Inject(method = "onFallenUpon", at = @At("HEAD"), cancellable = true)
    private void onFallenUpon(World var1, BlockPos var2, Entity var3, float var4, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptCropTrample((MC_Entity) var3, new MC_Location(var2.getX(), var2.getY(), var2.getZ(), PluginHelper.getLegacyDimensionId(var3.ap)), ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
