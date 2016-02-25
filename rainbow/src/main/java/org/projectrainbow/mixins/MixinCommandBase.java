package org.projectrainbow.mixins;

import PluginReference.MC_Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommand;
import net.minecraft.src.ICommandSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandBase.class)
public class MixinCommandBase {

    @Inject(method = "canCommandSenderUseCommand", at = @At("HEAD"), cancellable = true)
    private void canUse(MinecraftServer server, ICommandSender commandSender, CallbackInfoReturnable<Boolean> callbackInfo){
        if (commandSender instanceof MC_Player) {
            callbackInfo.setReturnValue(((MC_Player)commandSender).hasPermission("rainbow." + ((ICommand)this).getCommandName()));
        }
    }
}
