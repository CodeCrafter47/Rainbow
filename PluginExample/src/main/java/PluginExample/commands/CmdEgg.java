package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;

import static PluginExample.MyPlugin.server;

public class CmdEgg extends CmdBase {
    public CmdEgg() {
        super("egg", "Get a monster spawner egg");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        int num = 0;
        try {
            num = Integer.valueOf(args[0]);
        } catch (Throwable ignored) {
        }

        if (num <= 0) {
            plr.sendMessage(ChatColor.RED + "[ExamplePlugin] Usage: /egg # " + ChatColor.WHITE + " - Use 92 for a cow egg");
        } else {
            MC_ItemStack egg = server.createItemStack(383, 1, num);
            plr.setItemInHand(egg);
            plr.sendMessage(ChatColor.AQUA + "You have egg with sub-id: " + num);
        }
    }
}
