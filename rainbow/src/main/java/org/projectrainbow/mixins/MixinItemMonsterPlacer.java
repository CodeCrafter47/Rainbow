package org.projectrainbow.mixins;

import PluginReference.MC_Player;
import net.minecraft.src.BlockPos;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.EnumHand;
import net.minecraft.src.ItemMonsterPlacer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.qo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemMonsterPlacer.class)
public class MixinItemMonsterPlacer {

    @Inject(method = "a(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;Lnet/minecraft/src/BlockPos;Lnet/minecraft/src/EnumHand;Lnet/minecraft/src/EnumFacing;FFF)Lnet/minecraft/src/qo;", at = @At(value = "INVOKE", target = "net.minecraft.src.World.getTileEntity(Lnet/minecraft/src/BlockPos;)Lnet/minecraft/src/TileEntity;"), cancellable = true)
    void onMobSpawnerClicked(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9, CallbackInfoReturnable<qo> callbackInfo) {
        if (!((MC_Player)var2).hasPermission("rainbow.changespawner")) {
            callbackInfo.setReturnValue(qo.SUCCESS);
        }
    }
}
