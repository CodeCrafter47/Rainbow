package org.projectrainbow.mixins;

import PluginReference.MC_Attribute;
import PluginReference.MC_AttributeType;
import PluginReference.MC_Horse;
import PluginReference.MC_HorseType;
import PluginReference.MC_HorseVariant;
import PluginReference.MC_Player;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(EntityHorse.class)
@Implements(@Interface(iface = MC_Horse.class, prefix = "api$"))
public abstract class MixinEntityHorse extends MixinEntityAnimal {

    @Shadow
    protected abstract boolean isTame();

    @Shadow
    public abstract void setHorseTamed(boolean var1);

    @Shadow
    public abstract boolean isChested();

    @Shadow
    public abstract void setChested(boolean var1);

    @Shadow
    public abstract int getTemper();

    @Shadow
    public abstract void setTemper(int var1);

    @Shadow
    public abstract UUID getOwnerUniqueId();

    @Shadow
    public abstract void setOwnerUniqueId(UUID var1);

    @Shadow
    @Final
    private static IAttribute JUMP_STRENGTH;

    public void setOwner(MC_Player plr) {
        if (plr == null) {
            setOwnerUniqueId(null);
            return;
        }
        setOwnerUniqueId(plr.getUUID());
    }

    public MC_HorseType api$getHorseType() {
        EntityHorse handle = (EntityHorse) (Object) this;
        if (handle instanceof EntityHorseGeneric)
                return MC_HorseType.HORSE;
        if (handle instanceof EntityHorseDonkey)
                return MC_HorseType.DONKEY;
        if (handle instanceof EntityHorseMule)
                return MC_HorseType.MULE;
        if (handle instanceof EntityHorseZombie)
                return MC_HorseType.ZOMBIE;
        if (handle instanceof EntityHorseSkeleton)
                return MC_HorseType.SKELETON;
        if (handle instanceof EntityHorseLlama)
            return MC_HorseType.LLAMA;
        return MC_HorseType.UNKNOWN;
    }

    public void setHorseType(MC_HorseType arg) {
        throw new UnsupportedOperationException("setHorseType no longer works");
    }

    public MC_HorseVariant getHorseVariant() {
        EntityHorse handle = (EntityHorse) (Object) this;
        if (handle instanceof EntityHorseGeneric) {
            return MC_HorseVariant.values()[((EntityHorseGeneric) handle).df()];
        }
        return MC_HorseVariant.UNKNOWN;
    }

    public void setHorseVariant(MC_HorseVariant arg) {
        EntityHorse handle = (EntityHorse) (Object) this;
        if (handle instanceof EntityHorseGeneric) {
            ((EntityHorseGeneric) handle).o(arg.ordinal());
        }
    }

    public boolean hasChest() {
        return isChested();
    }

    public void setHasChest(boolean flag) {
        setChested(flag);
    }


    public void api$setTamed(boolean flag) {
        setHorseTamed(flag);
    }

    @Intrinsic
    public boolean api$isTame() {
        return isTame();
    }

    @Intrinsic
    public int api$getTemper() {
        return getTemper();
    }

    @Intrinsic
    public void api$setTemper(int val) {
        setTemper(val);
    }

    public String getOwnerUUID() {
        return getOwnerUniqueId().toString();
    }

    public void setOwnerUUID(String strUUID) {
        setOwnerUniqueId(UUID.fromString(strUUID));
    }

    @Override
    public MC_Attribute getAttribute(MC_AttributeType type) {
        if (type == MC_AttributeType.HORSE_JUMP_STRENGTH) {
            return (MC_Attribute) getEntityAttribute(JUMP_STRENGTH);
        }
        return super.getAttribute(type);
    }
}
