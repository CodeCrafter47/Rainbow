package org.projectrainbow.mixins;

import PluginReference.MC_Attribute;
import PluginReference.MC_AttributeType;
import PluginReference.MC_Horse;
import PluginReference.MC_HorseType;
import PluginReference.MC_HorseVariant;
import PluginReference.MC_Player;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseType;
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
    public abstract void setType(HorseType var1);

    @Shadow
    public abstract HorseType shadow$getType();

    @Shadow
    public abstract void setHorseVariant(int var1);

    @Shadow
    public abstract int shadow$getHorseVariant();

    @Shadow
    public abstract boolean isTame();

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

    @Intrinsic
    public MC_HorseType api$getHorseType() {
        switch (shadow$getType()) {
            case HORSE:
                return MC_HorseType.HORSE;
            case DONKEY:
                return MC_HorseType.DONKEY;
            case MULE:
                return MC_HorseType.MULE;
            case ZOMBIE:
                return MC_HorseType.ZOMBIE;
            case SKELETON:
                return MC_HorseType.SKELETON;
        }
        return MC_HorseType.UNKNOWN;
    }

    public void setHorseType(MC_HorseType arg) {
        switch (arg) {
            case HORSE:
                setType(HorseType.HORSE);
                break;
            case DONKEY:
                setType(HorseType.DONKEY);
                break;
            case MULE:
                setType(HorseType.MULE);
                break;
            case ZOMBIE:
                setType(HorseType.ZOMBIE);
                break;
            case SKELETON:
                setType(HorseType.SKELETON);
                break;
            case UNKNOWN:
                break;
        }
    }

    public MC_HorseVariant getHorseVariant() {
        return MC_HorseVariant.values()[shadow$getHorseVariant()];
    }

    public void setHorseVariant(MC_HorseVariant arg) {
        setHorseVariant(arg.ordinal());
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
