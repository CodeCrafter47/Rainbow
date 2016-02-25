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
    private ItemStack[] bw;

    @Override
    public List getArmor() {
        return PluginHelper.invArrayToList(bw);
    }

    @Override
    public void setArmor(List var1) {
        PluginHelper.updateInv(bw, var1);
    }
}
