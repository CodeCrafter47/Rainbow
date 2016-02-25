package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.src.BlockPos;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.IBlockState;
import net.minecraft.src.World;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(EntityEnderman.AIPlaceBlock.class)
public class MixinEndermanAIPlaceBlock {
    @Shadow
    @Final
    private EntityEnderman enderman;

    @Inject(method = "updateTask", at = @At(value = "INVOKE", target = "net.minecraft.src.World.setBlockState(Lnet/minecraft/src/BlockPos;Lnet/minecraft/src/IBlockState;I)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void grief(CallbackInfo callbackInfo, Random var1, World var2, int var3, int var4, int var5, BlockPos pos, IBlockState var7, IBlockState var8, IBlockState var9) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) enderman, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), enderman.dimension), MC_MiscGriefType.ENDERMAN_PLACE_CARRIED_BLOCK, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
