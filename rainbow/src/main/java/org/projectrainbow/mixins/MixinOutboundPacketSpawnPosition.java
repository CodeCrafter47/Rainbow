package org.projectrainbow.mixins;

import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.util.math.BlockPos;
import org.projectrainbow.interfaces.IMixinOutboundPacketSpawnPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SPacketSpawnPosition.class)
public class MixinOutboundPacketSpawnPosition implements IMixinOutboundPacketSpawnPosition {
    @Shadow
    private BlockPos spawnBlockPos;

    @Override
    public BlockPos getPos() {
        return spawnBlockPos;
    }
}
