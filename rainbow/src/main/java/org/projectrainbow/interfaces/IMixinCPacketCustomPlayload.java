package org.projectrainbow.interfaces;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public interface IMixinCPacketCustomPlayload {

    ResourceLocation getChannel();

    PacketBuffer getData();
}
