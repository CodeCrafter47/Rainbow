package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Entity;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;

public class CmdNearvis extends CmdBase {
    public CmdNearvis() {
        super("nearvis", "Make all entities visible");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        int num = 10;
        try {
            num = Integer.valueOf(args[0]);
        } catch (Throwable ignored) {
        }
        MC_World world = plr.getWorld();
        MC_Location loc = plr.getLocation();
        for (MC_Entity ent : world.getEntities()) {
            if (ent.getLocation().distanceTo(loc) <= num) {
                plr.sendMessage(ChatColor.AQUA + "Making Visible: " + ChatColor.WHITE + ent.getType() + ": " + ChatColor.YELLOW + ent.getName());
                ent.setInvisible(false);
            }
        }
    }
}
