package org.projectrainbow.interfaces;

import PluginReference.MC_Location;
import net.minecraft.src.WorldServer;
import org.projectrainbow._Backpack;

public interface IMixinEntityPlayerMP {
    /*
         * add teleport helper methods
         */
    void teleport(WorldServer world, double x, double y, double z);

    void teleport(WorldServer world, double x, double y, double z, float yaw, float pitch);

    _Backpack getBackpack();

    void onCompassTargetUpdated(MC_Location location);
}
