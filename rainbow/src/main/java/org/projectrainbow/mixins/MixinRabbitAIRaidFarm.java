package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.src.Block;
import net.minecraft.src.BlockPos;
import net.minecraft.src.EntityRabbit;
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

@Mixin(EntityRabbit.AIRaidFarm.class)
public class MixinRabbitAIRaidFarm {
    @Shadow
    @Final
    private EntityRabbit c;

    @Inject(method = "updateTask()V", at = @At(value = "INVOKE", target = "net.minecraft.src.IBlockState.getValue(Lnet/minecraft/src/IProperty;)Ljava/lang/Comparable;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void grief(CallbackInfo callbackInfo, World w, BlockPos pos, IBlockState var3, Block var4) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) c, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), c.dimension), MC_MiscGriefType.RABBIT_EATS_CARROT, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
