package org.projectrainbow.mixins;

import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.util.text.ITextComponent;
import org.projectrainbow.interfaces.IMixinSPacketPlayerListHeaderFooter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SPacketPlayerListHeaderFooter.class)
public class MixinSPacketPlayerListHeaderFooter implements IMixinSPacketPlayerListHeaderFooter {

    @Shadow
    private ITextComponent a;

    @Shadow
    private ITextComponent b;

    @Override
    public void setHeader(ITextComponent header) {
        this.a = header;
    }

    @Override
    public void setFooter(ITextComponent footer) {
        this.b = footer;
    }
}
