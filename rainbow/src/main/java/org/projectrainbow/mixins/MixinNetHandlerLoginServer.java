package org.projectrainbow.mixins;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.NetHandlerLoginServer;
import net.minecraft.src.NetworkManager;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.interfaces.IBungeeDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by florian on 03.02.15.
 */
@Mixin(NetHandlerLoginServer.class)
public class MixinNetHandlerLoginServer {
    @Shadow
    @Final
    public NetworkManager networkManager;

    @Inject(method = "getOfflineProfile", at = @At("HEAD"), cancellable = true)
    private void injectGameProfile(GameProfile gameProfile, CallbackInfoReturnable<GameProfile> callback){
        if(_DiwUtils.BungeeCord){
            GameProfile profile = new GameProfile(((IBungeeDataStorage) networkManager).getUUID(), gameProfile.getName());
            for (Property property : ((IBungeeDataStorage) networkManager).getProperties()) {
                profile.getProperties().put(property.getName(), property);
            }
            callback.setReturnValue(profile);
        }
    }

    @Redirect(method = "a(Lnet/minecraft/src/jt;)V", at = @At(value = "INVOKE", target = "net.minecraft.server.MinecraftServer.isServerInOnlineMode()Z"))
    private boolean simulateOfflineMode(MinecraftServer server) {
        return !_DiwUtils.BungeeCord && server.isServerInOnlineMode();
    }
}
