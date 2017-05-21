package org.projectrainbow.mixins;

import PluginReference.MC_Player;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandBase.class)
public class MixinCommandBase {

    @Inject(method = "checkPermission", at = @At("HEAD"), cancellable = true)
    private void canUse(MinecraftServer server, ICommandSender commandSender, CallbackInfoReturnable<Boolean> callbackInfo){
        if (commandSender instanceof MC_Player) {
            callbackInfo.setReturnValue(((MC_Player)commandSender).hasPermission("rainbow." + ((ICommand)this).getName()));
        }
    }
}
