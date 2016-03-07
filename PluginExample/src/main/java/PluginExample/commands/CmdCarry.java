package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

import static PluginExample.MyPlugin.server;

public class CmdCarry extends CmdBase {
    public CmdCarry() {
        super("carry", "Carry someone!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_Player tgtPlr = args.length == 0 ? null : server.getOnlinePlayerByName(args[0]);
        if (tgtPlr == null) {
            plr.sendMessage(ChatColor.RED + "Player not found");
            return;
        }
        plr.setRider(tgtPlr);
        plr.sendMessage(ChatColor.GREEN + "You now have rider: " + ChatColor.YELLOW + tgtPlr.getName());
    }
}
