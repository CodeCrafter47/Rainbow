package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Chunk;
import PluginReference.MC_Player;

import java.util.List;

public class CmdChunkTest3 extends CmdBase {
    public CmdChunkTest3() {
        super("chunktest3", "Print loaded chunks.");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        List<MC_Chunk> loadedChunks = plr.getWorld().getLoadedChunks();
        for (int i = 0; (i < loadedChunks.size()); i++) {
            MC_Chunk thisChunk = loadedChunks.get(i);


            String outMsg = String.format("Chunk %d: cx=%d, cz=%d", i + 1, thisChunk.getCX(), thisChunk.getCZ());
            if (i < 20) plr.sendMessage(ChatColor.AQUA + outMsg);
            else if (i == 20) plr.sendMessage(ChatColor.WHITE + "...");
            System.out.println(outMsg);
        }
        String outMsg2 = ChatColor.LIGHT_PURPLE + "Loaded chunks for this world: " + ChatColor.GREEN + loadedChunks.size();
        plr.sendMessage(outMsg2);
    }
}
