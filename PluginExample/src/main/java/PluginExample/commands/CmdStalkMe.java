package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.RainbowUtils;

import static PluginExample.MyPlugin.server;

public class CmdStalkMe extends CmdBase {
    public CmdStalkMe() {
        super("stalkme", "Evil");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_Location srcLoc = plr.getLocation();
        for (MC_Player tgt : server.getPlayers()) {
            MC_Location tgtLoc = tgt.getLocation();

            tgtLoc.yaw = RainbowUtils.YawToFaceLocation(tgtLoc, srcLoc);
            tgtLoc.pitch = RainbowUtils.PitchToFaceLocation(tgtLoc, srcLoc);

            tgt.teleport(tgtLoc);
            tgt.sendMessage(ChatColor.LIGHT_PURPLE + "You turn to face " + ChatColor.RED + plr.getName());
        }
        plr.sendMessage(ChatColor.GREEN + "Everyone was forced to look at you!");
    }
}
