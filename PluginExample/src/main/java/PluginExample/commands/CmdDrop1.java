package PluginExample.commands;

import PluginExample.CmdBase;
import PluginExample.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;

public class CmdDrop1 extends CmdBase {
    public CmdDrop1() {
        super("drop1", "Test drops");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack is = MyPlugin.server.createItemStack("grass", 2);
        plr.getWorld().dropItem(is, plr.getLocation(), plr.getName());
        plr.sendMessage(ChatColor.GREEN + "Ok - 2 grass");
    }
}
