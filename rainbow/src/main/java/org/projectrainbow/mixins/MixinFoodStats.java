package org.projectrainbow.mixins;

import PluginReference.MC_Player;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FoodStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoodStats.class)
public class MixinFoodStats {

    @Redirect(method = "onUpdate(Lnet/minecraft/src/EntityPlayer;)V", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityPlayer.heal(F)V"))
    private void injectCustomRegenAmount(EntityPlayer player, float amount) {
        player.heal(((MC_Player)player).getFoodRegenAmount() * amount);
    }
}
