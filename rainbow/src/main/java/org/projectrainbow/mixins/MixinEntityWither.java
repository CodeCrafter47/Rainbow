package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityWither.class)
public abstract class MixinEntityWither extends MixinEntity {

    @Redirect(method = "updateAITasks", at = @At(value = "INVOKE", target = "net.minecraft.world.World.destroyBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean grief(World world, BlockPos pos, boolean b) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief(this, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), dimension), MC_MiscGriefType.WITHER_BREAK, ei);
        return !ei.isCancelled && world.destroyBlock(pos, b);
    }
}
