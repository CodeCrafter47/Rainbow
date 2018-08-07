package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.util.math.BlockPos;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityAIEatGrass.class)
public class MixinEntityAIEatGrass {
    @Shadow
    @Final
    private EntityLiving grassEaterEntity;

    @Inject(method = "updateTask", at = @At(value = "INVOKE", target = "net.minecraft.world.World.destroyBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void grief0(CallbackInfo callbackInfo, BlockPos pos) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) grassEaterEntity, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), grassEaterEntity.dimension), MC_MiscGriefType.SHEEP_GRAZING_GRASS, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "updateTask", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void grief1(CallbackInfo callbackInfo, BlockPos pos0, BlockPos pos) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) grassEaterEntity, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), grassEaterEntity.dimension), MC_MiscGriefType.SHEEP_GRAZING_GRASS, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
