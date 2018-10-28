package org.projectrainbow.mixins;

import PluginReference.MC_Player;
import net.minecraft.item.ItemSpawnEgg;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemSpawnEgg.class)
public class MixinItemMonsterPlacer {

    @Inject(method = "onItemUse", at = @At(value = "INVOKE", target = "net.minecraft.world.World.getTileEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/tileentity/TileEntity;"), cancellable = true)
    void onMobSpawnerClicked(ItemUseContext var1, CallbackInfoReturnable<EnumActionResult> callbackInfo) {
        MC_Player mc_player = (MC_Player) var1.getPlayer();
        if (mc_player != null && !mc_player.hasPermission("rainbow.changespawner")) {
            callbackInfo.setReturnValue(EnumActionResult.FAIL);
        }
    }
}
