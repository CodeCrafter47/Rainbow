package org.projectrainbow.mixins;

import net.minecraft.src.BlockPos;
import net.minecraft.src.OutboundPacketSpawnPosition;
import org.projectrainbow.interfaces.IMixinOutboundPacketSpawnPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OutboundPacketSpawnPosition.class)
public class MixinOutboundPacketSpawnPosition implements IMixinOutboundPacketSpawnPosition {
    @Shadow
    private BlockPos spawnBlockPos;

    @Override
    public BlockPos getPos() {
        return spawnBlockPos;
    }
}
