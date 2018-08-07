package org.projectrainbow.mixins;

import PluginReference.ChatColor;
import PluginReference.MC_Player;
import com.google.common.collect.Iterables;
import net.minecraft.command.CommandSource;
import net.minecraft.command.impl.MessageCommand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import org.projectrainbow.commands._CmdIgnore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(MessageCommand.class)
public class MixinCommandMessage {

    @Inject(method = "func_198538_a", at = @At("HEAD"), cancellable = true)
    private static void onPrivateMessage(CommandSource var0, Collection<EntityPlayerMP> var1, ITextComponent var2, CallbackInfoReturnable<Integer> ci) {
        MC_Player target = (MC_Player) Iterables.getFirst(var1, null);
        if (var0.func_197022_f() != null && _CmdIgnore.IsIgnoring(target.getName(), ((MC_Player) var0.func_197022_f()).getName())) {
            ((MC_Player) var0.func_197022_f()).sendMessage(ChatColor.RED + "That player is ignoring you!");
            ci.setReturnValue(0);
        }
    }
}
