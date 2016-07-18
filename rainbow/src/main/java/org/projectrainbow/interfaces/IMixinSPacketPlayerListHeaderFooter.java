package org.projectrainbow.interfaces;

import net.minecraft.util.text.ITextComponent;

public interface IMixinSPacketPlayerListHeaderFooter {

    void setHeader(ITextComponent header);

    void setFooter(ITextComponent footer);
}
