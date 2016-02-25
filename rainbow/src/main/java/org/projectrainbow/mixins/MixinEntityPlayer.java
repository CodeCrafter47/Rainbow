package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import net.minecraft.src.BlockPos;
import net.minecraft.src.Container;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FoodStats;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.PlayerCapabilities;
import org.projectrainbow.EmptyItemStack;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.projectrainbow.commands._CmdNameColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends MixinEntityLivingBase {
    @Shadow
    public int experienceLevel;
    @Shadow
    public int experienceTotal;
    @Shadow
    public float experience;
    @Shadow
    public Container openContainer;
    @Shadow
    public InventoryPlayer inventory;
    @Shadow
    public PlayerCapabilities capabilities;
    @Shadow
    protected FoodStats foodStats;
    @Shadow
    public BlockPos playerLocation;

    @Shadow
    public abstract GameProfile getGameProfile();

    @Shadow
    public abstract boolean isPlayerSleeping();

    @Shadow
    public void clonePlayer(EntityPlayer oldPlayer, boolean b) {
        // dummy
    }

    @Shadow
    public abstract void addExperience(int var1);

    @Shadow
    public abstract void addExperienceLevel(int var1);

    @Shadow
    public abstract BlockPos getBedLocation();

    @Shadow
    public abstract void setSpawnPoint(BlockPos var1, boolean var2);

    /*
     * Change the getDisplayName method to return the name set using /namecolor.
     * To do this the first call to getName() is intercepted and if we have a colored
     * name for that player we return it. Otherwise we return the result of getName().
     */
    @Redirect(method = "getDisplayName", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityPlayer.getName()Ljava/lang/String;", ordinal = 0))
    public String hook_getName(EntityPlayer player) {
        String s = _CmdNameColor.ColorNameDict.get(player.getName().toLowerCase());
        if (s != null) {
            return s;
        }
        return player.getName();
    }

    @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
    private void onItemDropped(ItemStack var1, boolean var2, boolean var3, CallbackInfoReturnable<EntityItem> callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptItemDrop((MC_Player) this, Objects.firstNonNull((MC_ItemStack) (Object) var1, EmptyItemStack.getInstance()), ei);
        if (ei.isCancelled) {
            callbackInfo.setReturnValue(null);
        }
    }

    @Override
    public List<MC_ItemStack> getArmor() {
        return PluginHelper.invArrayToList(inventory.armorInventory);
    }

    @Override
    public void setArmor(List<MC_ItemStack> var1) {
        PluginHelper.updateInv(inventory.armorInventory, var1);
    }
}
