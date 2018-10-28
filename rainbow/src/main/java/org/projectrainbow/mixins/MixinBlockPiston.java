package org.projectrainbow.mixins;

import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_World;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockPistonBase.class)
public class MixinBlockPiston {

    @Inject(method = "eventReceived(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;II)Z", at = @At("HEAD"), cancellable = true)
    private void onPistonEvent(IBlockState state, World var1, BlockPos var2, int var4, int var5, CallbackInfoReturnable<Boolean> callbackInfo) {
        MC_DirectionNESWUD direction = PluginHelper.directionMap.get(state.get(BlockDirectional.FACING));
        MC_EventInfo ei = new MC_EventInfo();
        MC_Location loc = new MC_Location(var2.getX(), var2.getY(), var2.getZ(), ((MC_World)var1).getDimension());

        Hooks.onAttemptPistonAction(loc, direction, ei);

        if (ei.isCancelled) {
            callbackInfo.setReturnValue(false);
        }
    }
}
