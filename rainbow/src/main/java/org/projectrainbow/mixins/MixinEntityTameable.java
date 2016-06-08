package org.projectrainbow.mixins;

import PluginReference.MC_AnimalTameable;
import PluginReference.MC_Player;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(EntityTameable.class)
@Implements(@Interface(iface = MC_AnimalTameable.class, prefix = "api$"))
public abstract class MixinEntityTameable extends MixinEntityAnimal implements MC_AnimalTameable{

    @Shadow
    public abstract boolean isTamed();

    @Shadow
    public abstract void setTamed(boolean var1);

    @Shadow
    public abstract boolean isSitting();

    @Shadow
    public abstract void setSitting(boolean var1);

    @Shadow
    public abstract UUID getOwnerId();

    @Shadow
    public abstract void setOwnerId(UUID var1);

    @Shadow
    public abstract EntityLivingBase shadow$getOwner();

    @Shadow
    protected abstract void playTameEffect(boolean var1);

    @Intrinsic
    public boolean api$isTamed() {
        return isTamed();
    }

    @Intrinsic
    public void api$setTamed(boolean flag) {
        setTamed(flag);
    }

    @Override
    public boolean getSitting() {
        return isSitting();
    }

    @Intrinsic
    public void api$setSitting(boolean flag) {
        setSitting(flag);
    }

    @Override
    public String getUUIDOfOwner() {
        return getOwnerId().toString();
    }

    @Override
    public void setUUIDOfOwner(String uuid) {
        setOwnerId(UUID.fromString(uuid));
    }

    @Override
    public void setOwner(MC_Player plr) {
        setUUIDOfOwner(plr.getUUID().toString());
    }

    @Override
    public MC_Player getOwner() {
        return (MC_Player) shadow$getOwner();
    }

    @Override
    public boolean isOwnedBy(MC_Player plr) {
        return getUUIDOfOwner().endsWith(plr.getUUID().toString());
    }

    @Override
    public void showLoveHateEffect(boolean flag) {
        playTameEffect(flag);
    }
}
