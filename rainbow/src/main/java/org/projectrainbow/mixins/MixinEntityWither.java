package org.projectrainbow.mixins;

import net.minecraft.entity.boss.EntityWither;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityWither.class)
public abstract class MixinEntityWither extends MixinEntity {

    /* todo
    @Redirect(method = "updateAITasks", at = @At(value = "INVOKE", target = "net.minecraft.world.World.destroyBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean grief(World world, BlockPos pos, boolean b) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief(this, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), PluginHelper.getLegacyDimensionId(dimension)), MC_MiscGriefType.WITHER_BREAK, ei);
        return !ei.isCancelled && world.destroyBlock(pos, b);
    }
    */
}
