package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.entity.monster.EntityEnderman$AITakeBlock")
public class MixinEndermanAITakeBlock {
    @Shadow
    @Final
    private EntityEnderman enderman;

    @Redirect(method = "updateTask()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;g(Lnet/minecraft/util/math/BlockPos;)Z")) // setBlockToAir
    private boolean grief(World world, BlockPos pos) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) enderman, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), PluginHelper.getLegacyDimensionId(enderman.ap)), MC_MiscGriefType.ENDERMAN_PICKUP_BLOCK, ei);
        if (ei.isCancelled) {
            this.enderman.func_195406_b(null);
        }
        return !ei.isCancelled && world.g(pos);
    }
}
