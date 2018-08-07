package org.projectrainbow.mixins;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import org.projectrainbow.interfaces.IMixinCPacketCustomPlayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CPacketCustomPayload.class)
public class MixinCPacketCustomPayload implements IMixinCPacketCustomPlayload {

    @Shadow
    ResourceLocation channel;

    @Shadow
    PacketBuffer data;

    @Override
    public ResourceLocation getChannel() {
        return channel;
    }

    @Override
    public PacketBuffer getData() {
        return data;
    }
}
