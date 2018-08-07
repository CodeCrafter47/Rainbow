package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.*;

import java.util.ArrayList;
import java.util.List;

public class CmdGui extends CmdBase {
    public CmdGui() {
        super("gui", "Display an inventory gui.");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        final MC_Server server = RainbowUtils.getServer();
        MC_InventoryGUI gui = server.createInventoryGUI(4 * 9, ChatColor.LIGHT_PURPLE + "A GUI :)");

        gui.setItemStackAt(0, item("stone", "§aGet a stone head."));
        gui.setClickHandler(0, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack("stone", 1));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(9, item("dirt", "§aGet a dirt head."));
        gui.setClickHandler(9, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack("dirt", 1));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(18, item("diamond_block", "§aGet a diamond head."));
        gui.setClickHandler(18, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack("diamond_block", 1));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(27, item("tnt", "§aGet a tnt head."));
        gui.setClickHandler(27, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack("tnt", 1));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(2, item("diamond_chestplate", "§aGet a full diamond armor."));
        gui.setClickHandler(2, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack("diamond_helmet", 1));
                armor.set(2, server.createItemStack("diamond_chestplate", 1));
                armor.set(1, server.createItemStack("diamond_leggings", 1));
                armor.set(0, server.createItemStack("diamond_boots", 1));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(11, item("chainmail_chestplate", "§aGet a full chain armor."));
        gui.setClickHandler(11, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack("chainmail_helmet", 1));
                armor.set(2, server.createItemStack("chainmail_chestplate", 1));
                armor.set(1, server.createItemStack("chainmail_leggings", 1));
                armor.set(0, server.createItemStack("chainmail_boots", 1));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(35, item("barrier", "§aClose GUI."));
        gui.setClickHandler(35, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                player.closeInventory();
            }
        });

        plr.displayInventoryGUI(gui);
    }

    private static MC_ItemStack item(String id, String name) {
        return item(id, name, false);
    }

    private static MC_ItemStack item(String id, String name, boolean enchanted) {
        MC_ItemStack itemStack = RainbowUtils.getServer().createItemStack(id, 1);
        itemStack.setCustomName(name);
        if (enchanted) {
            itemStack.setEnchantments(new ArrayList<>());
        }
        return itemStack;
    }
}
