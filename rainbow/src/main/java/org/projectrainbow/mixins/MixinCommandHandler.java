package org.projectrainbow.mixins;

import PluginReference.MC_CommandSenderInfo;
import PluginReference.MC_CommandSenderType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.projectrainbow.ServerWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Commands.class)
public class MixinCommandHandler {

    @Inject(method = "func_197059_a", at = @At("HEAD"))
    private void onCommandExecuteStart(CommandSource sender, String commandLine, CallbackInfoReturnable<Integer> callbackInfo) {
        MC_CommandSenderInfo newInfo = new MC_CommandSenderInfo();

        newInfo.lastCommand = commandLine;
        newInfo.senderType = MC_CommandSenderType.UNSPECIFIED;
        if (sender.func_197022_f() instanceof EntityPlayer) {
            newInfo.senderType = MC_CommandSenderType.PLAYER;
            // todo
        //} else if (sender. instanceof CommandBlockBaseLogic) {
        //    newInfo.senderType = MC_CommandSenderType.COMMANDBLOCK;
        //} else if (sender instanceof MinecraftServer) {
        //    newInfo.senderType = MC_CommandSenderType.CONSOLE;
        //} else if (sender instanceof RConConsoleSource) {
        //    newInfo.senderType = MC_CommandSenderType.RCON;
        } else if (sender.func_197022_f() instanceof Entity) {
            newInfo.senderType = MC_CommandSenderType.ENTITY;
        } else {
            newInfo.senderType = MC_CommandSenderType.CONSOLE;
        }

        ServerWrapper.commandSenderInfo.addLast(newInfo);
    }

    @Inject(method = "func_197059_a", at = @At("RETURN"))
    private void onCommandExecuteEnd(CommandSource sender, String commandLine, CallbackInfoReturnable<Integer> callbackInfo) {
        ServerWrapper.commandSenderInfo.removeLast();
    }
}
