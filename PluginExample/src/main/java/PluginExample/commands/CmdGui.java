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

        gui.setItemStackAt(0, item(MC_ID.BLOCK_STONE, "§aGet a stone head."));
        gui.setClickHandler(0, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack(MC_ID.BLOCK_STONE, 1, 0));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(9, item(MC_ID.BLOCK_DIRT, "§aGet a dirt head."));
        gui.setClickHandler(9, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack(MC_ID.BLOCK_DIRT, 1, 0));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(18, item(MC_ID.BLOCK_DIAMOND_BLOCK, "§aGet a diamond head."));
        gui.setClickHandler(18, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack(MC_ID.BLOCK_DIAMOND_BLOCK, 1, 0));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(27, item(MC_ID.BLOCK_TNT, "§aGet a tnt head."));
        gui.setClickHandler(27, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack(MC_ID.BLOCK_TNT, 1, 0));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(2, item(MC_ID.ITEM_DIAMOND_CHESTPLATE, "§aGet a full diamond armor."));
        gui.setClickHandler(2, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack(MC_ID.ITEM_DIAMOND_HELMET, 1, 0));
                armor.set(2, server.createItemStack(MC_ID.ITEM_DIAMOND_CHESTPLATE, 1, 0));
                armor.set(1, server.createItemStack(MC_ID.ITEM_DIAMOND_LEGGINGS, 1, 0));
                armor.set(0, server.createItemStack(MC_ID.ITEM_DIAMOND_BOOTS, 1, 0));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(11, item(MC_ID.ITEM_CHAIN_CHESTPLATE, "§aGet a full chain armor."));
        gui.setClickHandler(11, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                List<MC_ItemStack> armor = player.getArmor();
                armor.set(3, server.createItemStack(MC_ID.ITEM_CHAIN_HELMET, 1, 0));
                armor.set(2, server.createItemStack(MC_ID.ITEM_CHAIN_CHESTPLATE, 1, 0));
                armor.set(1, server.createItemStack(MC_ID.ITEM_CHAIN_LEGGINGS, 1, 0));
                armor.set(0, server.createItemStack(MC_ID.ITEM_CHAIN_BOOTS, 1, 0));
                player.setArmor(armor);
            }
        });

        gui.setItemStackAt(35, item(MC_ID.BLOCK_BARRIER, "§aClose GUI."));
        gui.setClickHandler(35, new MC_InventoryGUI.ClickHandler() {
            @Override
            public void onSlotClicked(MC_Player player) {
                player.closeInventory();
            }
        });

        plr.displayInventoryGUI(gui);
    }

    private static MC_ItemStack item(int id, String name) {
        return item(id, name, false);
    }

    private static MC_ItemStack item(int id, String name, boolean enchanted) {
        MC_ItemStack itemStack = RainbowUtils.getServer().createItemStack(id, 1, 0);
        itemStack.setCustomName(name);
        if (enchanted) {
            itemStack.setEnchantments(new ArrayList<MC_Enchantment>());
        }
        return itemStack;
    }
}
