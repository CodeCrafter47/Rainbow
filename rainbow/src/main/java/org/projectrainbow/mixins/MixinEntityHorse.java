package org.projectrainbow.mixins;

import PluginReference.MC_Horse;
import PluginReference.MC_HorseType;
import PluginReference.MC_HorseVariant;
import PluginReference.MC_Player;
import net.minecraft.src.EntityHorse;
import net.minecraft.src.wm;
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
    public abstract void a(wm var1);

    @Shadow
    public abstract wm da();

    @Shadow
    public abstract void setHorseType(int var1);

    @Shadow
    public abstract int getHorseType();

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
    public abstract UUID di();

    @Shadow
    public abstract void b(UUID var1);

    public void setOwner(MC_Player plr) {
        if (plr == null) {
            b(null);
            return;
        }
        b(plr.getUUID());
    }

    @Intrinsic
    public MC_HorseType api$getHorseType() {
        switch (da()) {
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
                a(wm.HORSE);
                break;
            case DONKEY:
                a(wm.DONKEY);
                break;
            case MULE:
                a(wm.MULE);
                break;
            case ZOMBIE:
                a(wm.ZOMBIE);
                break;
            case SKELETON:
                a(wm.SKELETON);
                break;
            case UNKNOWN:
                break;
        }
    }

    public MC_HorseVariant getHorseVariant() {
        return MC_HorseVariant.values()[getHorseType()];
    }

    public void setHorseVariant(MC_HorseVariant arg) {
        setHorseType(arg.ordinal());
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
        return di().toString();
    }

    public void setOwnerUUID(String strUUID) {
        b(UUID.fromString(strUUID));
    }
}
