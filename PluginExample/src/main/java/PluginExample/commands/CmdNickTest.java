package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.RainbowUtils;

public class CmdNickTest extends CmdBase {
    public CmdNickTest() {
        super("nicktest", "Your name in rainbow colors!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        plr.setCustomName(RainbowUtils.RainbowString(plr.getName()));
        plr.sendMessage(ChatColor.GREEN + "Ok. Your name is now: " + ChatColor.WHITE + plr.getCustomName());
    }
}
