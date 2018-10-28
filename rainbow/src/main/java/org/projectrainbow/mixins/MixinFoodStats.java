package org.projectrainbow.mixins;

import PluginReference.MC_Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoodStats.class)
public class MixinFoodStats {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayer.heal(F)V"))
    private void injectCustomRegenAmount(EntityPlayer player, float amount) {
        player.heal(((MC_Player)player).getFoodRegenAmount() * amount);
    }
}
