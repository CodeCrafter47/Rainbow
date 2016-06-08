package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(targets = "net.minecraft.entity.monster.EntityEnderman$AIPlaceBlock")
public class MixinEndermanAIPlaceBlock {
    @Shadow
    @Final
    private EntityEnderman enderman;

    @Inject(method = "updateTask", at = @At(value = "INVOKE", target = "net.minecraft.world.World.setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void grief(CallbackInfo callbackInfo, Random var1, World var2, int var3, int var4, int var5, BlockPos pos, IBlockState var7, IBlockState var8, IBlockState var9) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) enderman, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), enderman.dimension), MC_MiscGriefType.ENDERMAN_PLACE_CARRIED_BLOCK, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
