package org.projectrainbow.mixins;

import PluginReference.MC_Player;
import com.mojang.authlib.GameProfile;
import net.minecraft.src.BlockPos;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemInWorldManager;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.WorldServer;
import org.projectrainbow.Hooks;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._JOT_OnlineTimeUtils;
import org.projectrainbow._UUIDMapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(ServerConfigurationManager.class)
public class MixinServerConfigurationManager {
    private Map<String, Long> lastConnectTime = new ConcurrentHashMap<String, Long>();

    @Inject(method = "playerLoggedIn", at = @At("HEAD"))
    private void onLogin(EntityPlayerMP var1, CallbackInfo callback) {
        _JOT_OnlineTimeUtils.HandlePlayerLogin(var1.getName());
        _UUIDMapper.AddMap(var1.getName(), var1.getUniqueID().toString());
        Hooks.onPlayerLogin(var1.getName(), var1.getUniqueID(), var1.getPlayerIP());
        Hooks.onPlayerJoin((MC_Player) var1);
    }

    @Inject(method = "recreatePlayerEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NetHandlerPlayServer;setPlayerLocation(DDDFF)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onRespawn(EntityPlayerMP oldPlayer, int dimension, boolean fromEnd /* false if player has been dead; if false inventory is cleared */,
                           CallbackInfoReturnable<EntityPlayerMP> callback, BlockPos whatEver, @Coerce boolean b, ItemInWorldManager maybe,
                           EntityPlayerMP newPlayer, WorldServer worldServer, BlockPos spawnPoint){
        Hooks.onPlayerRespawn((MC_Player) newPlayer);
    }

    @ModifyArg(method = "removeAllPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NetHandlerPlayServer;kickPlayerFromServer(Ljava/lang/String;)V"))
    String getCustomShutdownMessage(String oldShutdownMessage) {
        return _DiwUtils.CustomShutdownMessage;
    }

    @Inject(method = "allowUserToConnect", at = @At("HEAD"), cancellable = true)
    public void reconnectDelay(SocketAddress var1, GameProfile var2, CallbackInfoReturnable<String> callbackInfo) {
        String var4;

        if (_DiwUtils.DoReconnectDelay) {
            var4 = var2.getName();
            Long var3 = System.currentTimeMillis();
            Long msLast = (Long) lastConnectTime.get(var4);

            if (msLast != null
                    && var3 - msLast
                    < (long) (_DiwUtils.ReconnectDelaySeconds * 1000)) {
                callbackInfo.setReturnValue("There is a " + _DiwUtils.ReconnectDelaySeconds + "-second reconnect delay.");
            }

            lastConnectTime.put(var4, var3);
        }
    }
}
