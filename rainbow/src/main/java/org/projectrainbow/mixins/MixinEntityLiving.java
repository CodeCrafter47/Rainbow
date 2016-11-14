package org.projectrainbow.mixins;

import PluginReference.MC_ItemStack;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NullSafeList;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends MixinEntityLivingBase{

    @Shadow
    private NullSafeList<ItemStack> armorItems;

    @Override
    public List<MC_ItemStack> getArmor() {
        return PluginHelper.copyInvList(armorItems);
    }

    @Override
    public void setArmor(List<MC_ItemStack> var1) {
        PluginHelper.updateInv(armorItems, var1);
    }
}
