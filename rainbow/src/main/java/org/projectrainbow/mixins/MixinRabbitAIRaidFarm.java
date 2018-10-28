package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net.minecraft.entity.passive.EntityRabbit$AIRaidFarm")
public class MixinRabbitAIRaidFarm {
    @Shadow
    @Final
    private EntityRabbit rabbit;

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/state/IStateHolder;get(Lnet/minecraft/state/IProperty;)Ljava/lang/Comparable;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void grief(CallbackInfo callbackInfo, World w, BlockPos pos, IBlockState var3, Block var4) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) rabbit, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), PluginHelper.getLegacyDimensionId(rabbit.dimension)), MC_MiscGriefType.RABBIT_EATS_CARROT, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
