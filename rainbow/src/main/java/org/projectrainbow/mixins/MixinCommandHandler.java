package org.projectrainbow.mixins;

import PluginReference.MC_CommandSenderInfo;
import PluginReference.MC_CommandSenderType;
import com.google.common.collect.Sets;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import org.projectrainbow.ServerWrapper;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Mixin(CommandHandler.class)
public class MixinCommandHandler {

    @Shadow
    @Final
    @Mutable
    private Map<String, ICommand> commandMap;

    {
        commandMap = new LinkedHashMap<>();
    }

    @Redirect(method = "executeCommand", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private Object caseInsensitiveMapGet(Map map, Object key) {
        Object o = map.get(key);
        if (o == null && key instanceof String) {
            for (Object o1 : map.keySet()) {
                if (o1 instanceof String && ((String) key).equalsIgnoreCase((String) o1)) {
                    o = map.get(o1);
                }
            }
        }
        return o;
    }

    @Inject(method = "tryExecute", at = @At("HEAD"))
    private void onCommandExecuteStart(ICommandSender sender, String[] args, ICommand command, String commandLine, CallbackInfoReturnable<Boolean> callbackInfo) {
        MC_CommandSenderInfo newInfo = new MC_CommandSenderInfo();

        newInfo.lastCommand = commandLine;
        newInfo.senderType = MC_CommandSenderType.UNSPECIFIED;
        if (sender instanceof EntityPlayer) {
            newInfo.senderType = MC_CommandSenderType.PLAYER;
        } else if (sender instanceof CommandBlockBaseLogic) {
            newInfo.senderType = MC_CommandSenderType.COMMANDBLOCK;
        } else if (sender instanceof MinecraftServer) {
            newInfo.senderType = MC_CommandSenderType.CONSOLE;
        } else if (sender instanceof RConConsoleSource) {
            newInfo.senderType = MC_CommandSenderType.RCON;
        } else if (sender instanceof Entity) {
            newInfo.senderType = MC_CommandSenderType.ENTITY;
        }

        ServerWrapper.commandSenderInfo.addLast(newInfo);
    }

    @Inject(method = "tryExecute", at = @At("RETURN"))
    private void onCommandExecuteEnd(ICommandSender sender, String[] args, ICommand command, String commandLine, CallbackInfoReturnable<Boolean> callbackInfo) {
        ServerWrapper.commandSenderInfo.removeLast();
    }

    @Redirect(method = "getPossibleCommands", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "net.minecraft.command.CommandHandler.commandSet:Ljava/util/Set;"))
    private Set<ICommand> getCommandSet(CommandHandler self) {
        return Sets.newHashSet(commandMap.values());
    }
}
