package org.projectrainbow.mixins;

import PluginReference.MC_CommandSenderInfo;
import PluginReference.MC_CommandSenderType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChatComponentTranslation;
import net.minecraft.src.CommandBlockLogic;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommand;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.RConConsoleSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectrainbow.ServerWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CommandHandler.class)
public class MixinCommandHandler {

    @Inject(method = "tryExecute", at = @At("HEAD"))
    private void onCommandExecuteStart(ICommandSender sender, String[] args, ICommand command, String commandLine, CallbackInfoReturnable<Boolean> callbackInfo) {
        MC_CommandSenderInfo newInfo = new MC_CommandSenderInfo();

        newInfo.lastCommand = commandLine;
        newInfo.senderType = MC_CommandSenderType.UNSPECIFIED;
        if (sender instanceof EntityPlayer) {
            newInfo.senderType = MC_CommandSenderType.PLAYER;
        } else if (sender instanceof CommandBlockLogic) {
            newInfo.senderType = MC_CommandSenderType.COMMANDBLOCK;
        } else if (sender instanceof MinecraftServer) {
            newInfo.senderType = MC_CommandSenderType.CONSOLE;
        } else if (sender instanceof RConConsoleSource) {
            newInfo.senderType = MC_CommandSenderType.RCON;
        } else if (sender instanceof Entity) {
            // todo newInfo.senderType = MC_CommandSenderType.ENTITY;
        }

        ServerWrapper.commandSenderInfo.addLast(newInfo);
    }

    @Inject(method = "tryExecute", at = @At("RETURN"))
    private void onCommandExecuteEnd(ICommandSender sender, String[] args, ICommand command, String commandLine, CallbackInfoReturnable<Boolean> callbackInfo) {
        ServerWrapper.commandSenderInfo.removeLast();
    }

    @Inject(method = "tryExecute", at = @At(value = "INVOKE", target = "warn", remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onExceptionPrintStackTrace(ICommandSender sender, String[] args, ICommand command, String commandLine, CallbackInfoReturnable<Boolean> callbackInfo, Throwable throwable, ChatComponentTranslation errorMessageSentToPlayer) {
        LogManager.getLogger().log(Level.WARN, "Couldn\'t process command: \'" + commandLine + "\'", throwable);
    }

    @Redirect(method = "tryExecute", at = @At(value = "INVOKE", target = "warn", remap = false))
    private void onExceptionIgnoreOldLog(Logger logger, String message) {

    }
}
