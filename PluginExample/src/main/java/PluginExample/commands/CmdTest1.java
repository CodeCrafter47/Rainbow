package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

public class CmdTest1 extends CmdBase {
    public CmdTest1() {
        super("test1", "Test command");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        plr.sendMessage(ChatColor.AQUA + "[ExamplePlugin] " + ChatColor.GREEN + "Hello from Plugin!");

        MC_Location loc = plr.getLocation();
        plr.sendMessage(ChatColor.AQUA + "[ExamplePlugin] " + ChatColor.LIGHT_PURPLE + "Your Location: " + ChatColor.WHITE + loc.toString());
    }
}
