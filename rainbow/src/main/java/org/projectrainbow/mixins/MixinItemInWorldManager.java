package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import net.minecraft.src.Block;
import net.minecraft.src.BlockPos;
import net.minecraft.src.Blocks;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.EnumHand;
import net.minecraft.src.IBlockState;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemDoor;
import net.minecraft.src.ItemInWorldManager;
import net.minecraft.src.ItemReed;
import net.minecraft.src.ItemSign;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.qo;
import org.apache.logging.log4j.LogManager;
import org.projectrainbow.BlockWrapper;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInWorldManager.class)
public class MixinItemInWorldManager {

    @Shadow
    public World theWorld;

    @Shadow
    public EntityPlayerMP thisPlayerMP;

    @Shadow
    private WorldSettings.GameType gameType;

    @Inject(method = "tryHarvestBlock", at = @At(value = "INVOKE", target = "net.minecraft.src.ItemInWorldManager.removeBlock(Lnet/minecraft/src/BlockPos;)Z"))
    private void blockBreakHook(BlockPos blockPos, CallbackInfoReturnable<Boolean> callbackInfo) {
        Hooks.onBlockBroke((MC_Player) thisPlayerMP, new MC_Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), thisPlayerMP.dimension), Block.getIdFromBlock(theWorld.getBlockState(blockPos).getBlock()));
        Hooks.onBlockBroke((MC_Player) thisPlayerMP, new MC_Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), thisPlayerMP.dimension), new BlockWrapper(theWorld.getBlockState(blockPos)));
    }

    @Redirect(method = "a(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/EnumHand;Lnet/minecraft/src/BlockPos;Lnet/minecraft/src/EnumFacing;FFF)Lnet/minecraft/src/qo;", at = @At(value = "INVOKE", target = "net.minecraft.src.Block.a(Lnet/minecraft/src/World;Lnet/minecraft/src/BlockPos;Lnet/minecraft/src/IBlockState;Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/EnumHand;Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/EnumFacing;FFF)Z"))
    private boolean hookInteract(Block block, World var2, BlockPos var5, IBlockState state, EntityPlayer var1, EnumHand var4, ItemStack var6, EnumFacing face, float var7, float var8, float var9) {
        if (block.a(var2, var5, state, var1, var4, var6, face, var7, var8, var9)) {
            LogManager.getLogger().debug("JKC DBG: INTERACTING WITH BLOCK, like Chest/Furnace but also Cake" + var1.getName());
            Hooks.onInteracted((MC_Player) var1, new MC_Location(var5.getX(), var5.getY(), var5.getZ(), var1.dimension), (MC_ItemStack) (Object) var6);
            return true;
        }
        return false;
    }

    @Redirect(method = "a(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/EnumHand;Lnet/minecraft/src/BlockPos;Lnet/minecraft/src/EnumFacing;FFF)Lnet/minecraft/src/qo;", at = @At(value = "INVOKE", target = "net.minecraft.src.ItemStack.a(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;Lnet/minecraft/src/BlockPos;Lnet/minecraft/src/EnumHand;Lnet/minecraft/src/EnumFacing;FFF)Lnet/minecraft/src/qo;"))
    private qo hookPlace(ItemStack itemStack, EntityPlayer var1, World var2, BlockPos var5, EnumHand var4, EnumFacing face, float var7, float var8, float var9) {
        MC_Location locPlacedAgainst = new MC_Location(var5.getX(), var5.getY(), var5.getZ(), var1.dimension);
        MC_Location loc = new MC_Location(var5.offset(face).getX(), var5.offset(face).getY(), var5.offset(face).getZ(), var1.dimension);
        BlockWrapper bWrap = null;
        if (itemStack.getItem() instanceof ItemBlock) {
            bWrap = new BlockWrapper(((ItemBlock) itemStack.getItem()).getBlock().getDefaultState());
        } else if (itemStack.getItem() instanceof ItemReed) {
            ItemReed item = (ItemReed) itemStack.getItem();

            // todo bWrap = new BlockWrapper(item.getBm_blkObj.toBlockState());
        } else if (itemStack.getItem() instanceof ItemDoor) {
            ItemDoor item = (ItemDoor) itemStack.getItem();

            // todo bWrap = new BlockWrapper(item.getm_blkObj.toBlockState());
            bWrap = new BlockWrapper(Blocks.oak_door.getDefaultState());
        } else if (itemStack.getItem() instanceof ItemSign) {
            bWrap = new BlockWrapper(Blocks.standing_sign.getDefaultState());
        }
        if (bWrap != null) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptBlockPlace((MC_Player) var1, loc, bWrap, (MC_ItemStack) (Object) itemStack, locPlacedAgainst, PluginHelper.directionMap.get(face), ei);
            if (ei.isCancelled) {
                return qo.FAIL;
            }
        }
        qo a = itemStack.a(var1, var2, var5, var4, face, var7, var8, var9);
        if (a == qo.SUCCESS) {
            LogManager.getLogger().debug("JKC DBG: " + var1.getName() + " PLACING " + itemStack.getDisplayName());
            Hooks.onItemPlaced((MC_Player) var1, loc, (MC_ItemStack) (Object) itemStack, locPlacedAgainst, PluginHelper.directionMap.get(face));
        }
        return a;
    }
}
