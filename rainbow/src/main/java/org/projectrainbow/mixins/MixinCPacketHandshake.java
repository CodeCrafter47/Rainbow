package org.projectrainbow.mixins;

import net.minecraft.network.handshake.client.CPacketHandshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CPacketHandshake.class)
public class MixinCPacketHandshake {

    @ModifyArg(method = "readPacketData", at = @At(value = "INVOKE", target = "net.minecraft.network.PacketBuffer.readString(I)Ljava/lang/String;"))
    private int hostNameFieldSize(int oldSize){
        return Short.MAX_VALUE;
    }
}
