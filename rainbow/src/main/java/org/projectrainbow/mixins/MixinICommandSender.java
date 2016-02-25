package org.projectrainbow.mixins;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChatComponentText;
import net.minecraft.src.CommandBlockLogic;
import net.minecraft.src.Entity;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.RConConsoleSource;
import org.projectrainbow.interfaces.IMixinICommandSender;
import org.spongepowered.asm.mixin.Mixin;

// add to all subclasses of ICommandSender
@Mixin({Entity.class, MinecraftServer.class, RConConsoleSource.class, CommandBlockLogic.class})
public abstract class MixinICommandSender implements IMixinICommandSender {

    /*
     * Add a useful helper method
     */
    public void sendMessage(String legacyText) {
        ((ICommandSender)this).addChatMessage(new ChatComponentText(legacyText));
    }
}
