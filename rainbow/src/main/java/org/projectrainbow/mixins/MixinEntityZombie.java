package org.projectrainbow.mixins;

import PluginReference.MC_Zombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityZombie.class)
public abstract class MixinEntityZombie implements MC_Zombie {

    public boolean isVillager() {
        return ((Object) this) instanceof EntityZombieVillager;
    }
}
