package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import PluginReference.RainbowUtils;

public class CmdItemInfo extends CmdBase {
    public CmdItemInfo() {
        super("iteminfo", "Info about current item.");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack is = plr.getItemInHand();
        if ((is == null) || (is.getId() == 0)) {
            plr.sendMessage(ChatColor.RED + "Nothing in your hand");
            return;
        }
        int len = 16;
        plr.sendMessage(ChatColor.DARK_GRAY + "--------------------------");
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("ID", len) + ChatColor.WHITE + String.format("%d", is.getId()));
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Dmg/Subtype", len) + ChatColor.WHITE + String.format("%d", is.getDamage()));
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Lore", len) + ChatColor.WHITE + RainbowUtils.GetCommaList(is.getLore()));
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Max Stack", len) + ChatColor.WHITE + is.getMaxStackSize());
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Friendly Name", len) + ChatColor.WHITE + is.getFriendlyName());
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Official Name", len) + ChatColor.WHITE + is.getOfficialName());
        String customName = is.getCustomizedName();
        if (customName == null)
            customName = ChatColor.RED + "Not Customized";
        plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Custom Name", len) + ChatColor.WHITE + customName);
    }
}
