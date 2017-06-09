package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.projectrainbow.BlockWrapper;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.projectrainbow.interfaces.IMixinItemDoor;
import org.projectrainbow.interfaces.IMixinItemReed;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInteractionManager.class)
public class MixinItemInWorldManager {

    @Shadow
    public World world;

    @Shadow
    public EntityPlayerMP player;

    @Shadow
    private GameType gameType;

    @Inject(method = "tryHarvestBlock", at = @At(value = "INVOKE", target = "net.minecraft.server.management.PlayerInteractionManager.removeBlock(Lnet/minecraft/util/math/BlockPos;)Z"))
    private void blockBreakHook(BlockPos blockPos, CallbackInfoReturnable<Boolean> callbackInfo) {
        Hooks.onBlockBroke((MC_Player) player, new MC_Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), player.dimension), Block.getIdFromBlock(world.getBlockState(blockPos).getBlock()));
        Hooks.onBlockBroke((MC_Player) player, new MC_Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), player.dimension), new BlockWrapper(world.getBlockState(blockPos)));
    }

    @Redirect(method = "processRightClickBlock", at = @At(value = "INVOKE", target = "net.minecraft.block.Block.onBlockActivated(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Z"))
    private boolean hookInteract(Block block, World var2, BlockPos var5, IBlockState state, EntityPlayer var1, EnumHand var4, EnumFacing face, float var7, float var8, float var9) {
        if (block.onBlockActivated(var2, var5, state, var1, var4, face, var7, var8, var9)) {
            LogManager.getLogger().debug("JKC DBG: INTERACTING WITH BLOCK, like Chest/Furnace but also Cake" + var1.getName());
            Hooks.onInteracted((MC_Player) var1, new MC_Location(var5.getX(), var5.getY(), var5.getZ(), var1.dimension), (MC_ItemStack) (Object) var1.getHeldItem(var4));
            return true;
        }
        return false;
    }

    @Redirect(method = "processRightClickBlock", at = @At(value = "INVOKE", target = "net.minecraft.item.ItemStack.onItemUse(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;"))
    private EnumActionResult hookPlace(ItemStack itemStack, EntityPlayer var1, World var2, BlockPos var5, EnumHand var4, EnumFacing face, float var7, float var8, float var9) {
        MC_Location locPlacedAgainst = new MC_Location(var5.getX(), var5.getY(), var5.getZ(), var1.dimension);
        MC_Location loc = new MC_Location(var5.offset(face).getX(), var5.offset(face).getY(), var5.offset(face).getZ(), var1.dimension);
        BlockWrapper bWrap = null;
        if (itemStack.getItem() instanceof ItemBlock) {
            bWrap = new BlockWrapper(((ItemBlock) itemStack.getItem()).getBlock().getDefaultState());
        } else if (itemStack.getItem() instanceof ItemBlockSpecial) {
            bWrap = new BlockWrapper(((IMixinItemReed) itemStack.getItem()).getBlock().getDefaultState());
        } else if (itemStack.getItem() instanceof ItemDoor) {
            bWrap = new BlockWrapper(((IMixinItemDoor) itemStack.getItem()).getBlock().getDefaultState());
        } else if (itemStack.getItem() instanceof ItemSign) {
            bWrap = new BlockWrapper(Blocks.STANDING_SIGN.getDefaultState());
        }
        if (bWrap != null) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptBlockPlace((MC_Player) var1, loc, bWrap, (MC_ItemStack) (Object) itemStack, locPlacedAgainst, PluginHelper.directionMap.get(face), ei);
            if (ei.isCancelled) {
                return EnumActionResult.FAIL;
            }
        }
        EnumActionResult a = itemStack.onItemUse(var1, var2, var5, var4, face, var7, var8, var9);
        if (a == EnumActionResult.SUCCESS) {
            LogManager.getLogger().debug("JKC DBG: " + var1.getName() + " PLACING " + itemStack.getDisplayName());
            Hooks.onItemPlaced((MC_Player) var1, loc, (MC_ItemStack) (Object) itemStack, locPlacedAgainst, PluginHelper.directionMap.get(face));
        }
        return a;
    }
}
