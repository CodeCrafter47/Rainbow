package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net/minecraft/entity/monster/EntitySilverfish$AISummonSilverfish")
public class MixinSilverfishAIBreakBlocks {
    @Shadow
    @Final
    private EntitySilverfish silverfish;

    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "net.minecraft.world.World.getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    private IBlockState grief(World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof BlockSilverfish) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptEntityMiscGrief((MC_Entity) silverfish, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), PluginHelper.getLegacyDimensionId(silverfish.dimension)), MC_MiscGriefType.SILVERFISH_BREAK_MONSTER_EGG_BLOCK, ei);
            if (ei.isCancelled) {
                return Blocks.AIR.getDefaultState();
            }
        }
        return blockState;
    }
}
