package org.projectrainbow.mixins;

import PluginReference.MC_ItemStack;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends MixinEntityLivingBase{

    @Shadow
    private NonNullList<ItemStack> inventoryArmor;

    @Override
    public List<MC_ItemStack> getArmor() {
        return PluginHelper.copyInvList(inventoryArmor);
    }

    @Override
    public void setArmor(List<MC_ItemStack> var1) {
        PluginHelper.updateInv(inventoryArmor, var1);
    }
}
