package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityDragon.class)
public abstract class MixinEntityDragon extends MixinEntity {

    @Redirect(method = "destroyBlocksInAABB", at = @At(value = "INVOKE", target = "net.minecraft.world.World.setBlockToAir(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean grief(World world, BlockPos pos) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief(this, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), dimension), MC_MiscGriefType.ENDERDRAGON_BRUSH, ei);
        return !ei.isCancelled && world.setBlockToAir(pos);
    }
}
