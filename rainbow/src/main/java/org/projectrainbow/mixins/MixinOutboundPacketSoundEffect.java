package org.projectrainbow.mixins;

import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundEvent;
import org.projectrainbow.interfaces.IMixinOutboundPacketSoundEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SPacketSoundEffect.class)
public class MixinOutboundPacketSoundEffect implements IMixinOutboundPacketSoundEffect {
    @Shadow
    private SoundEvent sound;
    @Shadow
    private int posX;
    @Shadow
    private int posY;
    @Shadow
    private int posZ;

    @Override
    public int getX() {
        return posX;
    }

    @Override
    public int getY() {
        return posY;
    }

    @Override
    public int getZ() {
        return posZ;
    }

    @Override
    public String getSoundName() {
        return SoundEvent.REGISTRY.getNameForObject(sound).getPath();
    }
}
