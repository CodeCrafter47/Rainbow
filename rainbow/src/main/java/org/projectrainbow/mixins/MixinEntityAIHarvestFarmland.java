package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.src.Block;
import net.minecraft.src.BlockPos;
import net.minecraft.src.EntityAIHarvestFarmland;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.IBlockState;
import net.minecraft.src.World;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityAIHarvestFarmland.class)
public class MixinEntityAIHarvestFarmland {
    @Shadow
    @Final
    private EntityVillager theVillager;

    @Inject(method = "updateTask", at = @At(value = "INVOKE", target = "net.minecraft.src.World.destroyBlock(Lnet/minecraft/src/BlockPos;Z)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void grief(World w, BlockPos pos, IBlockState state, Block block, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) theVillager, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), theVillager.dimension), MC_MiscGriefType.VILLAGER_HARVEST, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "updateTask", at = @At(value = "FIELD", target = "net.minecraft.src.EntityAIHarvestFarmland.theVillager:Lnet/minecraft/src/EntityVillager;", ordinal = 3), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void grief2(World w, BlockPos pos, IBlockState state, Block block, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) theVillager, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), theVillager.dimension), MC_MiscGriefType.VILLAGER_PLANT_SEEDS, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
