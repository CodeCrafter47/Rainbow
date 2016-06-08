package org.projectrainbow.mixins;

import PluginReference.MC_Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemMonsterPlacer.class)
public class MixinItemMonsterPlacer {

    @Inject(method = "onItemUse", at = @At(value = "INVOKE", target = "net.minecraft.world.World.getTileEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/tileentity/TileEntity;"), cancellable = true)
    void onMobSpawnerClicked(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9, CallbackInfoReturnable<EnumActionResult> callbackInfo) {
        if (!((MC_Player)var2).hasPermission("rainbow.changespawner")) {
            callbackInfo.setReturnValue(EnumActionResult.SUCCESS);
        }
    }
}
