package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

public class CmdChunkTest1 extends CmdBase {
    public CmdChunkTest1() {
        super("chunktest1", "Test whether chuck is loaded.");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        plr.sendMessage(ChatColor.GREEN + "Chunk at cx=1000, cz=1000. IsLoaded=" + ChatColor.AQUA + plr.getWorld().isChunkLoaded(1000, 1000));
    }
}
