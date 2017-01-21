package org.projectrainbow.interfaces;

public interface IMixinMinecraftServer {

    void onServerIconUpdated();

    void onMotdUpdated();

    void setCurrentTime(long l);
}
