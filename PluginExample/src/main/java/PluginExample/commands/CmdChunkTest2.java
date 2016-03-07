package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

public class CmdChunkTest2 extends CmdBase {
    public CmdChunkTest2() {
        super("chunktest2", "load chunk.");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        plr.getWorld().loadChunk(1000, 1000);
        plr.sendMessage(ChatColor.GREEN + "Loaded Chunk at cx=1000, cz=1000");
    }
}
