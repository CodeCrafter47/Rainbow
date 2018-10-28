package org.projectrainbow.mixins;

import PluginReference.MC_Enchantment;
import PluginReference.MC_EnchantmentType;
import PluginReference.MC_ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.projectrainbow.PluginHelper;
import org.projectrainbow._DiwUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements MC_ItemStack {
    @Shadow
    private int count;
    @Shadow
    private NBTTagCompound tag;

    @Shadow
    public abstract ITextComponent getDisplayName();

    @Shadow
    public abstract boolean hasDisplayName();

    @Shadow
    public abstract ItemStack setDisplayName(ITextComponent name);

    @Shadow
    public abstract void clearCustomName();

    @Shadow
    public abstract boolean isEnchanted();

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract int shadow$getDamage();

    @Shadow
    public abstract void shadow$setDamage(int var1);

    @ModifyArg(method = "setDisplayName", at = @At(value = "INVOKE", target = "net.minecraft.nbt.NBTTagCompound.putString(Ljava/lang/String;Ljava/lang/String;)V"), index = 1)
    private String censorItemName(String name) {
        if (_DiwUtils.DoCensor && _DiwUtils.HasBadLanguage(name)) {
            return ITextComponent.Serializer.toJson(new TextComponentString("Censored"));
        }
        return name;
    }

    @Override
    public String getFriendlyName() {
        return getItem().getDisplayName((ItemStack) (Object) this).getFormattedText();
    }

    @Override
    public String getCustomizedName() {
        return getDisplayName().getString();
    }

    @Override
    public String getOfficialName() {
        return IRegistry.ITEM.getKey(getItem()).getPath();
    }

    @Override
    @Deprecated
    public int getId() {
        return PluginHelper.getLegacyItemId(getItem());
    }

    @Override
    public int getDamage() {
        return shadow$getDamage();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setDamage(int damage) {
        shadow$setDamage(damage);
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean hasCustomName() {
        return hasDisplayName();
    }

    @Override
    public void setCustomName(String name) {
        setDisplayName(new TextComponentString(name));
    }

    @Override
    public void removeCustomName() {
        clearCustomName();
    }

    @Override
    public boolean getHasCustomDetails() {
        return tag != null;
    }

    @Override
    public int getMaxStackSize() {
        return getItem().getMaxStackSize();
    }

    @Override
    public int getEnchantmentLevel(MC_EnchantmentType var1) {
        return EnchantmentHelper.getEnchantmentLevel(PluginHelper.enchantmentMap.inverse().get(var1), PluginHelper.getItemStack(this));
    }

    @Override
    public void setEnchantments(List<MC_Enchantment> var1) {
        EnchantmentHelper.setEnchantments(var1.stream()
                .collect(Collectors.toMap(
                        ench -> PluginHelper.enchantmentMap.inverse().get(ench.type),
                        ench -> ench.level
                ))
                , PluginHelper.getItemStack(this));
    }

    @Override
    public List<MC_Enchantment> getEnchantments() {
        return EnchantmentHelper.getEnchantments(PluginHelper.getItemStack(this))
                .entrySet().stream()
                .map(entry -> new MC_Enchantment(PluginHelper.enchantmentMap.get(entry.getKey()),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public void setEnchantmentLevel(MC_EnchantmentType var1, int var2) {
        // try to change existing enchantment level
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(PluginHelper.getItemStack(this));
        enchantments.put(PluginHelper.enchantmentMap.inverse().get(var1), var2);
        EnchantmentHelper.setEnchantments(enchantments, PluginHelper.getItemStack(this));
    }

    @Override
    public MC_ItemStack getDuplicate() {
        return (MC_ItemStack) (Object) ((ItemStack) (Object) this).copy();
    }

    @Override
    public List<String> getLore() {
        if (tag == null) {
            return null;
        } else if (!tag.contains("display")) {
            return null;
        } else {
            NBTTagCompound display = tag.getCompound("display");
            if (!display.contains("Lore")) {
                return null;
            } else {
                ArrayList<String> res = new ArrayList<String>();
                NBTTagList lore = display.getList("Lore", 8);

                for (int i = 0; i < lore.size(); ++i) {
                    res.add(lore.getString(i));
                }

                return res;
            }
        }
    }

    @Override
    public void setLore(List<String> argLore) {
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        if (!tag.contains("display")) {
            tag.put("display", new NBTTagCompound());
        }

        NBTTagCompound tag = this.tag.getCompound("display");

        if (argLore != null && argLore.size() > 0) {
            NBTTagList lore = new NBTTagList();

            for (String line : argLore) {
                lore.add(new NBTTagString(line));
            }

            tag.put("Lore", lore);
        } else {
            tag.remove("Lore");
        }
    }

    @Override
    public byte[] serialize() {
        NBTTagCompound data = new NBTTagCompound();

        data = ((ItemStack) (Object) this).write(data);

        try {
            ByteArrayOutputStream exc = new ByteArrayOutputStream();
            DataOutputStream w = new DataOutputStream(exc);

            data.write(w);
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
        if (getItem() == Items.PLAYER_HEAD) {
            if (tag == null) {
                tag = new NBTTagCompound();
            }

            tag.putString("SkullOwner", name);
            getItem().updateItemStackNBT(tag);
        }
    }
}
