package org.projectrainbow.mixins;

import PluginReference.MC_Enchantment;
import PluginReference.MC_EnchantmentType;
import PluginReference.MC_ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
    private int stackSize;
    @Shadow
    private NBTTagCompound stackTagCompound;

    @Shadow(prefix = "getDisplayName$")
    public abstract ITextComponent getDisplayName$func_200301_q();

    @Shadow
    public abstract boolean hasDisplayName();

    @Shadow(prefix = "setDisplayName$")
    public abstract ItemStack setDisplayName$func_200302_a(ITextComponent name);

    @Shadow
    public abstract void clearCustomName();

    @Shadow
    public abstract boolean isItemEnchanted();

    @Shadow
    public abstract Item getItem();

    @Shadow public abstract int getItemDamage();

    @Shadow
    public abstract void func_196085_b(int var1);

    @ModifyArg(method = "func_200302_a", at = @At(value = "INVOKE", target = "net.minecraft.nbt.NBTTagCompound.setString(Ljava/lang/String;Ljava/lang/String;)V"), index = 1)
    private String censorItemName(String name) {
        if (_DiwUtils.DoCensor && _DiwUtils.HasBadLanguage(name)) {
            return ITextComponent.Serializer.componentToJson(new TextComponentString("Censored"));
        }
        return name;
    }

    @Override
    public String getFriendlyName() {
        return getItem().func_200295_i((ItemStack) (Object) this).getFormattedText();
    }

    @Override
    public String getCustomizedName() {
        return getDisplayName$func_200301_q().getString();
    }

    @Override
    public String getOfficialName() {
        return GameRegistry.s.b(getItem()).getPath();
    }

    @Override
    @Deprecated
    public int getId() {
        return PluginHelper.getLegacyItemId(getItem());
    }

    @Override
    public int getDamage() {
        return getItemDamage();
    }

    @Override
    public int getCount() {
        return stackSize;
    }

    @Override
    public void setDamage(int damage) {
        func_196085_b(damage);
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
        setDisplayName$func_200302_a(new TextComponentString(name));
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
        return getItem().getItemStackLimit();
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

                for (int i = 0; i < lore.size(); ++i) {
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
                lore.add(new NBTTagString(line));
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
        if (getItem() == Items.field_196184_dx) {
            if (stackTagCompound == null) {
                stackTagCompound = new NBTTagCompound();
            }

            stackTagCompound.setString("SkullOwner", name);
            getItem().updateItemStackNBT(stackTagCompound);
        }
    }
}
