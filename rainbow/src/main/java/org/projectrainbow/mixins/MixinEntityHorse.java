package org.projectrainbow.mixins;

import PluginReference.*;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.*;
import org.spongepowered.asm.mixin.*;

import java.util.UUID;

@Mixin(AbstractHorse.class)
@Implements(@Interface(iface = MC_Horse.class, prefix = "api$"))
public abstract class MixinEntityHorse extends MixinEntityAnimal {

    @Shadow
    public abstract boolean isTame();

    @Shadow
    public abstract void setHorseTamed(boolean var1);

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
    protected static IAttribute JUMP_STRENGTH;

    public void setOwner(MC_Player plr) {
        if (plr == null) {
            setOwnerUniqueId(null);
            return;
        }
        setOwnerUniqueId(plr.getUUID());
    }

    @Deprecated
    public MC_HorseType api$getHorseType() {
        AbstractHorse handle = (AbstractHorse) (Object) this;
        if (handle instanceof EntityHorse)
                return MC_HorseType.HORSE;
        if (handle instanceof EntityDonkey)
                return MC_HorseType.DONKEY;
        if (handle instanceof EntityMule)
                return MC_HorseType.MULE;
        if (handle instanceof EntityZombieHorse)
                return MC_HorseType.ZOMBIE;
        if (handle instanceof EntitySkeletonHorse)
                return MC_HorseType.SKELETON;
        if (handle instanceof EntityLlama)
            return MC_HorseType.LLAMA;
        return MC_HorseType.UNKNOWN;
    }

    public void setHorseType(MC_HorseType arg) {
        throw new UnsupportedOperationException("setHorseType no longer works");
    }

    public MC_HorseVariant getHorseVariant() {
        AbstractHorse handle = (EntityHorse) (Object) this;
        if (handle instanceof EntityHorse) {
            return MC_HorseVariant.values()[((EntityHorse) handle).getHorseVariant()];
        }
        return MC_HorseVariant.UNKNOWN;
    }

    public void setHorseVariant(MC_HorseVariant arg) {
        AbstractHorse handle = (EntityHorse) (Object) this;
        if (handle instanceof EntityHorse) {
            ((EntityHorse) handle).setHorseVariant(arg.ordinal());
        }
    }

    public boolean hasChest() {
        AbstractHorse handle = (EntityHorse) (Object) this;
        return handle instanceof AbstractChestHorse && ((AbstractChestHorse) handle).hasChest();
    }

    public void setHasChest(boolean flag) {
        AbstractHorse handle = (EntityHorse) (Object) this;
        if (handle instanceof AbstractChestHorse) {
            ((AbstractChestHorse) handle).setChested(flag);
        } else {
            throw new UnsupportedOperationException("setHasChest not supported for this entity type");
        }
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
