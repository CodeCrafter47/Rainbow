package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
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
    private EntityVillager villager;

    @Inject(method = "updateTask", at = @At(value = "INVOKE", target = "Layg;a(Lnet/minecraft/util/math/BlockPos;Z)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD) // destroyBlock
    private void grief(CallbackInfo callbackInfo, World w, BlockPos pos, IBlockState state, Block block) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) villager, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), PluginHelper.getLegacyDimensionId(villager.ap)), MC_MiscGriefType.VILLAGER_HARVEST, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
/* TODO fix this
    @Inject(method = "updateTask", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IWorldWriter;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD, expect = 4, require = 4)
    private void grief2(CallbackInfo callbackInfo, IWorld w, BlockPos pos, IBlockState state, Block block, InventoryBasic inv, int i, ItemStack is, @Coerce  boolean b) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) villager, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), villager.dimension), MC_MiscGriefType.VILLAGER_PLANT_SEEDS, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
    */
}
