package org.projectrainbow.mixins;

import PluginReference.MC_DamageType;
import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Player;
import PluginReference.MC_PotionEffect;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.SharedMonsterAttributes;
import net.minecraft.src.ra;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(EntityLivingBase.class)
@Implements(@Interface(iface = MC_Entity.class, prefix = "api$"))
public abstract class MixinEntityLivingBase extends MixinEntity {
    @Shadow
    protected EntityPlayer attackingPlayer;
    @Shadow
    private EntityLivingBase lastAttacker;
    @Shadow
    @Final
    private Map<Potion, PotionEffect> activePotionsMap;

    @Shadow
    protected abstract void onNewPotionEffect(PotionEffect var1);

    @Shadow
    protected abstract void onChangedPotionEffect(PotionEffect var1, boolean var2);

    @Shadow
    @Final
    public abstract float getHealth();

    @Shadow
    public abstract void setHealth(float var1);

    @Shadow
    @Final
    public abstract float getMaxHealth();

    @Shadow
    public abstract float getAbsorptionAmount();

    @Shadow
    public abstract void setAbsorptionAmount(float var1);

    @Shadow
    @Final
    public abstract void setArrowCountInEntity(int var1);

    @Shadow
    public abstract Collection<PotionEffect> getActivePotionEffects();

    @Shadow
    public abstract void clearActivePotions();

    @Shadow
    protected abstract float applyPotionDamageCalculations(DamageSource var1, float var2);

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    private void onAttacked(DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> callbackInfo) {
        m_rainbowAdjustedDamage = damage;
        damageModified = false;
        attacker = (MC_Entity) damageSource.getEntity();

        MC_EventInfo ei = new MC_EventInfo();

        Hooks.onAttemptEntityDamage(this, PluginHelper.wrap(damageSource), damage, ei);

        if (ei.isCancelled) {
            callbackInfo.cancel();
            callbackInfo.setReturnValue(false);
        }
    }

    @ModifyArg(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityLivingBase.damageEntity(Lnet/minecraft/src/DamageSource;F)V"))
    private float applyAdjustedDamage(float oldDamage) {
        return damageModified ? m_rainbowAdjustedDamage : oldDamage;
    }

    @Inject(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityLivingBase.onDeath(Lnet/minecraft/src/DamageSource;)V"))
    private void hookOnDeath(DamageSource var1, float var2, CallbackInfoReturnable<Boolean> callbackInfo) {
        Hooks.onAttemptDeath(this, (MC_Entity) var1.getEntity(), PluginHelper.wrap(var1), var2);
        if (!(this instanceof MC_Player)) {
            Hooks.onNonPlayerEntityDeath(this, (MC_Entity) var1.getEntity(), PluginHelper.wrap(var1));
        }
    }

    @Inject(method = "addPotionEffect", at = @At("HEAD"), cancellable = true)
    private void onAddPotionEffect(PotionEffect var1, CallbackInfo callbackInfo) {
        if (this instanceof MC_Player) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptPotionEffect((MC_Player) this, PluginHelper.potionMap.get(var1.a()), ei);
            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        }
    }

    @Intrinsic
    public float api$getHealth() {
        return getHealth();
    }

    @Intrinsic
    public void api$setHealth(float var1) {
        setHealth(var1);
    }

    @Intrinsic
    public float api$getMaxHealth() {
        return getMaxHealth();
    }

    @Override
    public void setMaxHealth(float var1) {
        ((EntityLivingBase) (Object) this).getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(var1);
    }

    @Override
    public int getBaseArmorScore() {
        return ((EntityLivingBase) (Object) this).getTotalArmorValue();
    }

    @Override
    public float getArmorAdjustedDamage(MC_DamageType var1, float var2) {
        return ra.a(var2, (float) ((EntityLivingBase) (Object) this).getTotalArmorValue());
    }

    @Override
    public float getTotalAdjustedDamage(MC_DamageType var1, float var2) {
        var2 = getArmorAdjustedDamage(var1, var2);
        var2 = applyPotionDamageCalculations(PluginHelper.unwrap(var1), var2);
        return Math.max(var2 - getAbsorptionAmount(), 0);
    }

    @Intrinsic
    public float api$getAbsorptionAmount() {
        return getAbsorptionAmount();
    }

    @Intrinsic
    public void api$setAbsorptionAmount(float var1) {
        setAbsorptionAmount(var1);
    }

    @Override
    public void setNumberOfArrowsHitWith(int var1) {
        setArrowCountInEntity(var1);
    }

    @Override
    public List<MC_PotionEffect> getPotionEffects() {
        List<MC_PotionEffect> potionEffects = new ArrayList<MC_PotionEffect>();
        for (PotionEffect potionEffect : getActivePotionEffects()) {
            potionEffects.add(PluginHelper.wrap(potionEffect));
        }
        return potionEffects;
    }

    @Override
    public void setPotionEffects(List<MC_PotionEffect> var1) {
        clearActivePotions();
        for (MC_PotionEffect mc_potionEffect : var1) {
            PotionEffect potionEffect = PluginHelper.unwrap(mc_potionEffect);
            PotionEffect var2 = this.activePotionsMap.get(potionEffect.a());
            if (var2 == null) {
                this.activePotionsMap.put(potionEffect.a(), potionEffect);
                this.onNewPotionEffect(potionEffect);
            } else {
                var2.combine(potionEffect);
                this.onChangedPotionEffect(var2, true);
            }
        }
    }
}
