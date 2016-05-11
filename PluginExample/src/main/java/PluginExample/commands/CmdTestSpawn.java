package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_EntityType;
import PluginReference.MC_Player;
import PluginReference.RainbowUtils;

public class CmdTestSpawn extends CmdBase {
    public CmdTestSpawn() {
        super("testspawn", "Spawn Entities!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        if (args.length == 0) {
            plr.sendMessage(ChatColor.AQUA + "Usage: /testspawn EntType [CustomName]");
            return;
        }
        String customName = null;
        if (args.length > 1) customName = RainbowUtils.ConcatArgs(args, 1);

        boolean didSpawn = false;
        for (MC_EntityType entType : MC_EntityType.values()) {
            if (entType.toString().equalsIgnoreCase(args[0])) {
                didSpawn = true;
                plr.getWorld().spawnEntity(entType, plr.getLocation(), customName);
                break;
            }
        }

        if (didSpawn) {
            String plrMsg = ChatColor.AQUA + "Spawning " + ChatColor.GOLD + "Type";
            if (customName != null)
                plrMsg += ChatColor.WHITE + " named " + ChatColor.YELLOW + customName;
            plr.sendMessage(plrMsg);
        } else {
            plr.sendMessage(ChatColor.RED + "Unknown EntityType: " + ChatColor.AQUA + args[0]);
        }
    }
}
