package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_MotionData;
import PluginReference.MC_Player;
import PluginReference.RainbowUtils;

import static PluginExample.MyPlugin.server;

public class CmdShake extends CmdBase {
    public CmdShake() {
        super("shake", "That's fun!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        int num = 2;
        try {
            num = Integer.valueOf(args[0]);
        } catch (Throwable ignored) {
        }

        if (num <= 0) num = 2;
        MC_MotionData data = plr.getMotionData();
        data.xMotion = -num + Math.random() * (2 * num);
        data.yMotion = 1.0f;
        data.zMotion = -num + Math.random() * (num * 2);

        plr.setMotionData(data);
        plr.sendMessage(ChatColor.AQUA + "You're all shook up!");
    }
}
