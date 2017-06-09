package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import org.projectrainbow.Hooks;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._JOT_OnlineTimeUtils;
import org.projectrainbow._UUIDMapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(PlayerList.class)
public class MixinServerConfigurationManager {
    private Map<String, Long> lastConnectTime = new ConcurrentHashMap<>();

    @Inject(method = "playerLoggedIn", at = @At("HEAD"))
    private void onLogin(EntityPlayerMP var1, CallbackInfo callback) {
        _JOT_OnlineTimeUtils.HandlePlayerLogin((MC_Player) var1);
        _UUIDMapper.AddMap(var1.getName(), var1.getUniqueID().toString());
        Hooks.onPlayerLogin(var1.getName(), var1.getUniqueID(), var1.getPlayerIP());
        Hooks.onPlayerJoin((MC_Player) var1);
    }

    @Inject(method = "recreatePlayerEntity", at = @At("RETURN"))
    private void onRespawn(EntityPlayerMP oldPlayer, int dimension, boolean fromEnd /* false if player has been dead; if false inventory is cleared */,
                           CallbackInfoReturnable<EntityPlayerMP> callback){
        Hooks.onPlayerRespawn((MC_Player) callback.getReturnValue());
    }

    @Redirect(method = "recreatePlayerEntity", at = @At(value = "INVOKE", target = "net.minecraft.world.WorldServer.getSpawnPoint()Lnet/minecraft/util/math/BlockPos;"))
    private BlockPos onRespawnSendCompassTarget(WorldServer worldServer, EntityPlayerMP oldPlayer, int dimension, boolean fromEnd){
        MC_Location compassTarget = ((MC_Player) oldPlayer).getCompassTarget();
        return new BlockPos(compassTarget.getBlockX(), compassTarget.getBlockY(), compassTarget.getBlockZ());
    }

    @ModifyArg(method = "removeAllPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetHandlerPlayServer;disconnect(Lnet/minecraft/util/text/ITextComponent;)V"))
    ITextComponent getCustomShutdownMessage(ITextComponent oldShutdownMessage) {
        if (_DiwUtils.CustomShutdownMessage != null) {
            return new TextComponentString(_DiwUtils.CustomShutdownMessage);
        } else {
            return oldShutdownMessage;
        }
    }

    @Inject(method = "allowUserToConnect", at = @At("HEAD"), cancellable = true)
    public void reconnectDelay(SocketAddress var1, GameProfile var2, CallbackInfoReturnable<String> callbackInfo) {
        String var4;

        if (_DiwUtils.DoReconnectDelay) {
            var4 = var2.getName();
            Long var3 = System.currentTimeMillis();
            Long msLast = lastConnectTime.get(var4);

            if (msLast != null
                    && var3 - msLast
                    < (long) (_DiwUtils.ReconnectDelaySeconds * 1000)) {
                callbackInfo.setReturnValue("There is a " + _DiwUtils.ReconnectDelaySeconds + "-second reconnect delay.");
            }

            lastConnectTime.put(var4, var3);
        }
    }

    @Inject(method = "allowUserToConnect", at = @At("RETURN"), cancellable = true)
    public void attemptLoginEvent(SocketAddress var1, GameProfile var2, CallbackInfoReturnable<String> callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        ei.tag = callbackInfo.getReturnValue();
        ei.isCancelled = ei.tag != null;
        Hooks.onPlayerLogin(var2.getName(), var2.getId(), ((InetSocketAddress)var1).getAddress(), ei);

        if (ei.isCancelled) {
            callbackInfo.setReturnValue("" + ei.tag);
        } else {
            callbackInfo.setReturnValue(null);
        }
    }

    @ModifyConstant(method = "transferEntityToWorld", constant = @Constant(doubleValue = 8.0D))
    private double injectNetherDistanceRatio(double ignored) {
        return _DiwUtils.netherDistanceRatio;
    }
}
