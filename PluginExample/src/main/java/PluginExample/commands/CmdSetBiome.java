package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_WorldBiomeType;

public class CmdSetBiome extends CmdBase {
    public CmdSetBiome() {
        super("setbiome", "Change the current biome!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        String remainder = args.length == 0 ? "" : args[0];
        MC_WorldBiomeType biomeType = null;
        for (MC_WorldBiomeType biomeIter : MC_WorldBiomeType.values()) {
            if (biomeIter.toString().equalsIgnoreCase(remainder)) {
                biomeType = biomeIter;
            }
        }
        if (biomeType == null) {
            plr.sendMessage(ChatColor.RED + "Unknown biome type: " + ChatColor.WHITE + remainder);
            return;
        }
        MC_Location loc = plr.getLocation();
        plr.getWorld().setBiomeTypeAt(loc.getBlockX(), loc.getBlockZ(), biomeType);
        plr.sendMessage(ChatColor.GREEN + "Biome set to: " + ChatColor.WHITE + biomeType.toString());
    }
}
