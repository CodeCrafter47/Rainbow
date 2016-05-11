package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

public class CmdNickClear extends CmdBase {
    public CmdNickClear() {
        super("nickclear", "Reset your nickname");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        plr.setCustomName(null);
        plr.sendMessage(ChatColor.GREEN + "Ok. Your name is now: " + ChatColor.YELLOW + plr.getCustomName());
    }
}
