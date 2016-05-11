package org.projectrainbow.mixins;

import com.mojang.authlib.properties.Property;
import net.minecraft.src.NetworkManager;
import org.projectrainbow.interfaces.IBungeeDataStorage;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

/**
 * Created by florian on 03.02.15.
 */
@Mixin(NetworkManager.class)
public class MixinNetworkManager implements IBungeeDataStorage {

    private UUID uuid = null;
    private Property[] properties = new Property[0];

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public Property[] getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Property[] properties) {
        this.properties = properties;
    }
}
