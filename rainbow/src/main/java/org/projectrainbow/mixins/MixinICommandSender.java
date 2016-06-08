package org.projectrainbow.mixins;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.text.TextComponentString;
import org.projectrainbow.interfaces.IMixinICommandSender;
import org.spongepowered.asm.mixin.Mixin;

// add to all subclasses of ICommandSender
@Mixin({Entity.class, MinecraftServer.class, RConConsoleSource.class, CommandBlockBaseLogic.class})
public abstract class MixinICommandSender implements IMixinICommandSender {

    /*
     * Add a useful helper method
     */
    public void sendMessage(String legacyText) {
        ((ICommandSender)this).addChatMessage(new TextComponentString(legacyText));
    }
}
