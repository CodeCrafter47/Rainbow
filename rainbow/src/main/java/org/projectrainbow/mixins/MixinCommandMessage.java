package org.projectrainbow.mixins;

import PluginReference.ChatColor;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.projectrainbow.commands._CmdIgnore;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CommandMessage.class)
public class MixinCommandMessage {

    @Inject(method = "execute", at = @At(value = "INVOKE_ASSIGN", target = "net.minecraft.command.server.CommandMessage.getPlayer(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)Lnet/minecraft/entity/player/EntityPlayerMP;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onPrivateMessage(MinecraftServer var1, ICommandSender var2, String[] var3, CallbackInfo ci, EntityPlayerMP target) {
        if (_CmdIgnore.IsIgnoring(target.getName(), var2.getName())) {
            var2.addChatMessage(new TextComponentString(ChatColor.RED + "That player is ignoring you!"));
            ci.cancel();
        }
    }
}
