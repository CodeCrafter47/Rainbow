package org.projectrainbow.mixins;

import PluginReference.MC_Enchantment;
import PluginReference.MC_EnchantmentType;
import PluginReference.MC_ItemStack;
import PluginReference.MC_ItemType;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.projectrainbow.PluginHelper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.interfaces.IMixinNBTBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements MC_ItemStack {
    @Shadow
    public int stackSize;
    @Shadow
    private Item item;
    @Shadow
    private NBTTagCompound stackTagCompound;
    @Shadow
    private int itemDamage;
    @Shadow
    private EntityItemFrame itemFrame;

    @Shadow
    public abstract String getDisplayName();

    @Shadow
    public abstract boolean hasDisplayName();

    @Shadow
    public abstract ItemStack setStackDisplayName(String name);

    @Shadow
    public abstract void clearCustomName();

    @Shadow
    public abstract boolean isItemEnchanted();

    @ModifyArg(method = "setStackDisplayName", at = @At(value = "INVOKE", target = "net.minecraft.nbt.NBTTagCompound.setString(Ljava/lang/String;Ljava/lang/String;)V"), index = 1)
    private String censorItemName(String name) {
        if (_DiwUtils.DoCensor && _DiwUtils.HasBadLanguage(name)) {
            return "Censored";
        }
        return name;
    }

    @Override
    public String getFriendlyName() {
        return item.getItemStackDisplayName((ItemStack) (Object) this);
    }

    @Override
    public String getCustomizedName() {
        return getDisplayName();
    }

    @Override
    public String getOfficialName() {
        return item.getUnlocalizedName((ItemStack) (Object) this);
    }

    @Override
    public int getId() {
        return Item.getIdFromItem(item);
    }

    @Override
    public int getDamage() {
        return itemDamage;
    }

    @Override
    public int getCount() {
        return stackSize;
    }

    @Override
    public void setDamage(int damage) {
        itemDamage = damage;
    }

    @Override
    public void setCount(int count) {
        stackSize = count;
    }

    @Override
    public boolean hasCustomName() {
        return hasDisplayName();
    }

    @Override
    public void setCustomName(String name) {
        setStackDisplayName(name);
    }

    @Override
    public void removeCustomName() {
        clearCustomName();
    }

    @Override
    public boolean getHasCustomDetails() {
        return stackTagCompound != null;
    }

    @Override
    public int getMaxStackSize() {
        return item.getItemStackLimit();
    }

    @Override
    public int getEnchantmentLevel(MC_EnchantmentType var1) {
        int level = 0;
        if (isItemEnchanted()) {
            NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);
            for (int i = 0; i < var3.tagCount(); i++) {
                if (var3.getCompoundTagAt(i).getShort("id") == PluginHelper.enchantmentMap.inverse().get(var1)) {
                    level = Math.max(level, var3.getCompoundTagAt(i).getShort("lvl"));
                }
            }
        }
        return level;
    }

    @Override
    public void setEnchantments(List<MC_Enchantment> var1) {
        if (stackTagCompound == null) {
            stackTagCompound = new NBTTagCompound();
        }

        if (!this.stackTagCompound.hasKey("ench", 9)) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }

        NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);
        while (var3.tagCount() > 0) {
            var3.removeTag(0);
        }
        for (MC_Enchantment mc_enchantment : var1) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setShort("id", PluginHelper.enchantmentMap.inverse().get(mc_enchantment.type));
            var4.setShort("lvl", (short) mc_enchantment.level);
            var3.appendTag(var4);
        }
    }

    @Override
    public List<MC_Enchantment> getEnchantments() {
        if (!isItemEnchanted()) {
            return Collections.emptyList();
        }
        List<MC_Enchantment> enchantments = new ArrayList<MC_Enchantment>();
        NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);
        for (int i = 0; i < var3.tagCount(); i++) {
            enchantments.add(new MC_Enchantment(PluginHelper.enchantmentMap.get(var3.getCompoundTagAt(i).getShort("id")), var3.getCompoundTagAt(i).getShort("lvl")));
        }
        return enchantments;
    }

    @Override
    public void setEnchantmentLevel(MC_EnchantmentType var1, int var2) {
        // try to change existing enchantment level
        if (isItemEnchanted()) {
            NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);
            for (int i = 0; i < var3.tagCount(); i++) {
                if (var3.getCompoundTagAt(i).getShort("id") == PluginHelper.enchantmentMap.inverse().get(var1)) {
                    var3.getCompoundTagAt(i).setShort("lvl", (short) var2);
                    return;
                }
            }
        }

        if (stackTagCompound == null) {
            stackTagCompound = new NBTTagCompound();
        }

        if (!this.stackTagCompound.hasKey("ench", 9)) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }

        NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);

        NBTTagCompound var4 = new NBTTagCompound();
        var4.setShort("id", PluginHelper.enchantmentMap.inverse().get(var1));
        var4.setShort("lvl", (short) var2);
        var3.appendTag(var4);
    }

    @Override
    public MC_ItemStack getDuplicate() {
        return (MC_ItemStack) (Object) ((ItemStack) (Object) this).copy();
    }

    @Override
    public List<String> getLore() {
        if (stackTagCompound == null) {
            return null;
        } else if (!stackTagCompound.hasKey("display")) {
            return null;
        } else {
            NBTTagCompound display = stackTagCompound.getCompoundTag("display");
            if (!display.hasKey("Lore")) {
                return null;
            } else {
                ArrayList<String> res = new ArrayList<String>();
                NBTTagList lore = display.getTagList("Lore", 8);

                for (int i = 0; i < lore.tagCount(); ++i) {
                    res.add(lore.getStringTagAt(i));
                }

                return res;
            }
        }
    }

    @Override
    public void setLore(List<String> argLore) {
        if (stackTagCompound == null) {
            stackTagCompound = new NBTTagCompound();
        }

        if (!stackTagCompound.hasKey("display")) {
            stackTagCompound.setTag("display", new NBTTagCompound());
        }

        NBTTagCompound tag = stackTagCompound.getCompoundTag("display");

        if (argLore != null && argLore.size() > 0) {
            NBTTagList lore = new NBTTagList();

            for (String line : argLore) {
                lore.appendTag(new NBTTagString(line));
            }

            tag.setTag("Lore", lore);
        } else {
            tag.removeTag("Lore");
        }
    }

    @Override
    public byte[] serialize() {
        NBTTagCompound data = new NBTTagCompound();

        data = ((ItemStack) (Object) this).writeToNBT(data);

        try {
            ByteArrayOutputStream exc = new ByteArrayOutputStream();
            DataOutputStream w = new DataOutputStream(exc);

            ((IMixinNBTBase) data).write1(w);
            w.flush();
            byte[] res = exc.toByteArray();

            w.close();
            return res;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    @Override
    public void setSkullOwner(String name) {
        if (getId() == MC_ItemType.SKELETON_SKULL) {
            if (stackTagCompound == null) {
                stackTagCompound = new NBTTagCompound();
            }

            stackTagCompound.setString("SkullOwner", name);
            item.updateItemStackNBT(stackTagCompound);
        }
    }
}
