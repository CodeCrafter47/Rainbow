package org.projectrainbow;

import PluginReference.MC_EnchantmentType;
import PluginReference.MC_ItemStack;

import java.util.Collections;
import java.util.List;

public class EmptyItemStack implements MC_ItemStack {
    private byte[] emptyByteArray = new byte[0];

    private static EmptyItemStack instance = new EmptyItemStack();

    public static EmptyItemStack getInstance() {
        return instance;
    }

    private EmptyItemStack() {
    }

    @Override
    public String getFriendlyName() {
        return "";
    }

    @Override
    public String getCustomizedName() {
        return "";
    }

    @Override
    public String getOfficialName() {
        return "";
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void setDamage(int var1) {

    }

    @Override
    public void setCount(int var1) {

    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void setCustomName(String var1) {

    }

    @Override
    public void removeCustomName() {

    }

    @Override
    public boolean getHasCustomDetails() {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 0;
    }

    @Override
    public int getEnchantmentLevel(MC_EnchantmentType var1) {
        return 0;
    }

    @Override
    public void setEnchantments(List var1) {

    }

    @Override
    public List getEnchantments() {
        return Collections.emptyList();
    }

    @Override
    public void setEnchantmentLevel(MC_EnchantmentType var1, int var2) {

    }

    @Override
    public MC_ItemStack getDuplicate() {
        return this;
    }

    @Override
    public List getLore() {
        return Collections.emptyList();
    }

    @Override
    public void setLore(List var1) {

    }

    @Override
    public byte[] serialize() {
        return emptyByteArray;
    }

    @Override
    public void setSkullOwner(String var1) {

    }
}
