package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_WorldBiomeType;

public class CmdBiome extends CmdBase {
    public CmdBiome() {
        super("biome", "Get current Biome");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_Location loc = plr.getLocation();
        MC_WorldBiomeType biome = plr.getWorld().getBiomeTypeAt(loc.getBlockX(), loc.getBlockZ());
        plr.sendMessage(ChatColor.GREEN + "Biome where you are: " + ChatColor.WHITE + biome.toString());
    }
}
