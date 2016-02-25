package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_World;
import net.minecraft.src.BlockDynamicLiquid;
import net.minecraft.src.BlockPos;
import net.minecraft.src.IBlockState;
import net.minecraft.src.World;
import org.projectrainbow.BlockWrapper;
import org.projectrainbow.Hooks;
import org.projectrainbow._DiwUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockDynamicLiquid.class)
public class MixinBlockDynamicLiquid {

    @Inject(method = "tryFlowInto", at = @At("HEAD"), cancellable = true)
    private void tryFlowInto(World var1, BlockPos var2, IBlockState var3, int var4, CallbackInfo callbackInfo) {
        if (!_DiwUtils.BlockFlowOn) {
            callbackInfo.cancel();
            return;
        }
        MC_Location sloc = new MC_Location(
                (double) var2.getX(), (double) var2.getY(),
                (double) var2.getZ(), ((MC_World)var1).getDimension());

        MC_EventInfo ei = new MC_EventInfo();
        BlockWrapper blkWrap = new BlockWrapper(var3);

        Hooks.onAttemptBlockFlow(sloc, blkWrap, ei);

        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
