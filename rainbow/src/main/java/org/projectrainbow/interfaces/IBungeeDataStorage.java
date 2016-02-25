package org.projectrainbow.interfaces;

import com.mojang.authlib.properties.Property;

import java.util.UUID;

/**
 * Created by florian on 03.02.15.
 */
public interface IBungeeDataStorage {

    UUID getUUID();
    void setUUID(UUID uuid);

    Property[] getProperties();
    void setProperties(Property[] properties);
}
