package org.projectrainbow.mixins;

import PluginReference.MC_Zombie;
import net.minecraft.src.EntityZombie;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityZombie.class)
@Implements(@Interface(iface = MC_Zombie.class, prefix = "api$"))
public abstract class MixinEntityZombie {

    @Shadow
    public abstract boolean isVillager();

    @Intrinsic
    public boolean api$isVillager() {
        return isVillager();
    }
}
