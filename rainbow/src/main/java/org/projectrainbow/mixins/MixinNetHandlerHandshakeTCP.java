package org.projectrainbow.mixins;

import com.google.gson.Gson;
import com.mojang.authlib.properties.Property;
import io.netty.channel.Channel;
import net.minecraft.src.C00Handshake;
import net.minecraft.src.EnumConnectionState;
import net.minecraft.src.NetHandlerHandshakeTCP;
import net.minecraft.src.NetworkManager;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.interfaces.IBungeeDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

/**
 * Created by florian on 03.02.15.
 */
@Mixin(NetHandlerHandshakeTCP.class)
public class MixinNetHandlerHandshakeTCP {

    private static final Gson gson = new Gson();

    @Shadow
    @Final
    private NetworkManager networkManager;

    @Inject(method = "processHandshake", at = @At(value = "HEAD"))
    private void doBungeeStuff(C00Handshake handshake, CallbackInfo callback){
        if(handshake.getRequestedState().equals(EnumConnectionState.LOGIN) && _DiwUtils.BungeeCord) {
            try {
                Channel channel = null;
                Field socket = null;
                for (Field field : networkManager.getClass().getDeclaredFields()) {
                    if (field.getType().equals(Channel.class)) {
                        field.setAccessible(true);
                        channel = (Channel) field.get(networkManager);
                    }
                    if (field.getType().equals(SocketAddress.class)) {
                        socket = field;
                    }
                }

                String host = null;
                for (Field field : handshake.getClass().getDeclaredFields()) {
                    if (field.getType().equals(String.class)) {
                        field.setAccessible(true);
                        host = (String) field.get(handshake);
                    }
                }

                if (host == null || channel == null || socket == null) {
                    throw new RuntimeException("Hook failed");
                }

                String[] split = host.split("\00");
                if (split.length != 4 && split.length != 3) {
                    throw new RuntimeException("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!");
                }

                // split[0]; Vanilla doesn't use this
                socket.setAccessible(true);
                socket.set(networkManager, new InetSocketAddress(split[1], ((InetSocketAddress) socket.get(networkManager)).getPort()));

                String uuid = split[2];
                ((IBungeeDataStorage) networkManager).setUUID(UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32)));
                if (split.length == 4) {
                    ((IBungeeDataStorage) networkManager).setProperties(gson.fromJson(split[3], Property[].class));
                } else {
                    ((IBungeeDataStorage) networkManager).setProperties(new Property[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException(e);
            }
        }
    }
}
