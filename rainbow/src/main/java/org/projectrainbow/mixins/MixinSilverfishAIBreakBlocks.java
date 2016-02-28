package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.src.BlockPos;
import net.minecraft.src.Blocks;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.IBlockState;
import net.minecraft.src.World;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net/minecraft/src/EntitySilverfish$b")
public class MixinSilverfishAIBreakBlocks {
    @Shadow
    private EntitySilverfish a;

    @Redirect(method = "updateTask()V", at = @At(value = "INVOKE", target = "net.minecraft.src.World.getBlockState(Lnet/minecraft/src/BlockPos;)Lnet/minecraft/src/IBlockState;"))
    private IBlockState grief(World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() == Blocks.monster_egg) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptEntityMiscGrief((MC_Entity) a, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), a.dimension), MC_MiscGriefType.SILVERFISH_BREAK_MONSTER_EGG_BLOCK, ei);
            if (ei.isCancelled) {
                return Blocks.air.getDefaultState();
            }
        }
        return blockState;
    }
}
