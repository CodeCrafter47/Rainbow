package org.projectrainbow.mixins;

import net.minecraft.src.OutboundPacketSoundEffect;
import net.minecraft.src.SoundEffect;
import org.projectrainbow.interfaces.IMixinOutboundPacketSoundEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OutboundPacketSoundEffect.class)
public class MixinOutboundPacketSoundEffect implements IMixinOutboundPacketSoundEffect {
    @Shadow
    private SoundEffect a;
    @Shadow
    private int c;
    @Shadow
    private int d;
    @Shadow
    private int e;

    @Override
    public int getX() {
        return c;
    }

    @Override
    public int getY() {
        return d;
    }

    @Override
    public int getZ() {
        return e;
    }

    @Override
    public String getSoundName() {
        return SoundEffect.a.getNameForObject(a).getResourcePath();
    }
}
