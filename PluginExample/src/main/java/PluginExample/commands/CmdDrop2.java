package PluginExample.commands;

import PluginExample.CmdBase;
import PluginExample.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;

public class CmdDrop2 extends CmdBase {
    public CmdDrop2() {
        super("drop2", "Test drops");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack is = MyPlugin.server.createItemStack("stone", 4);
        plr.getWorld().dropItem(is, plr.getLocation(), null);
        plr.sendMessage(ChatColor.GREEN + "Ok. 4 stone");
    }
}
