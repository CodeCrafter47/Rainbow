package org.projectrainbow.mixins;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.ItemStack;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends MixinEntityLivingBase{

    @Shadow
    private ItemStack[] bx;

    @Override
    public List getArmor() {
        return PluginHelper.invArrayToList(bx);
    }

    @Override
    public void setArmor(List var1) {
        PluginHelper.updateInv(bx, var1);
    }
}
