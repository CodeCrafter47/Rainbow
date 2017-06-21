package org.projectrainbow.mixins;

import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(C00Handshake.class)
public class MixinCPacketHandshake {

    @ModifyArg(method = "readPacketData", at = @At(value = "INVOKE", target = "net.minecraft.network.PacketBuffer.readString(I)Ljava/lang/String;"))
    private int hostNameFieldSize(int oldSize){
        return Short.MAX_VALUE;
    }
}
