package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.*;

import static PluginExample.MyPlugin.server;

public class CmdInfo extends CmdBase {
    public CmdInfo() {
        super("info", "All about you");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        int len = 16;
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Health", len) + ChatColor.WHITE + String.format("%.2f", plr.getHealth()));
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Location", len) + ChatColor.WHITE + plr.getLocation().toString());
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Balance", len) + ChatColor.WHITE + String.format("%.2f", plr.getEconomyBalance()));
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("IP", len) + ChatColor.WHITE + plr.getIPAddress());
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Server Port", len) + ChatColor.WHITE + server.getServerPort());
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Spawn Radius", len) + ChatColor.WHITE + server.getSpawnProtectionRadius());
        MC_ItemStack is = plr.getItemInHand();
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Item In Hand", len) + ChatColor.WHITE + "ID=" + is.getOfficialName() + " - " + ChatColor.YELLOW + is.getFriendlyName());
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Motion Data", len) + ChatColor.WHITE + plr.getMotionData().toString());
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("EXP", len) + ChatColor.WHITE + String.format("XP=%.2f, LVL=%d, TOT=%d", plr.getExp(), plr.getLevel(), plr.getTotalExperience()));

        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Absorbtion", len) + ChatColor.WHITE + String.format("%.2f", plr.getAbsorptionAmount()));
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("50 Dmg (After Armor)", len) + ChatColor.WHITE + String.format("%.2f", plr.getArmorAdjustedDamage(MC_DamageType.GENERIC, 50)));
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("50 Dmg (Simulation)", len) + ChatColor.WHITE + String.format("%.2f", plr.getTotalAdjustedDamage(MC_DamageType.GENERIC, 50)));
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Base Armor Value", len) + ChatColor.WHITE + String.format("%d", plr.getBaseArmorScore()));
    }
}
