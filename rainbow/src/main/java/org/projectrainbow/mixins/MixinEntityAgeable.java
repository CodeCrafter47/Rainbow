package org.projectrainbow.mixins;

import PluginReference.MC_EntityAgeable;
import net.minecraft.src.EntityAgeable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAgeable.class)
public abstract class MixinEntityAgeable extends MixinEntity implements MC_EntityAgeable {

    @Shadow
    public abstract void setGrowingAge(int var1);

    @Shadow
    public abstract int getGrowingAge();

    @Override
    public int getAge() {
        return getGrowingAge();
    }

    @Override
    public void setAge(int idx) {
        setGrowingAge(idx);
    }
}
