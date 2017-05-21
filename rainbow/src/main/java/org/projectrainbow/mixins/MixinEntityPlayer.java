package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.projectrainbow._DiwUtils;
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
    public BlockPos bedLocation;

    @Shadow
    public abstract GameProfile getGameProfile();

    @Shadow
    public abstract boolean isPlayerSleeping();

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
    @Redirect(method = "getDisplayName", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayer.getName()Ljava/lang/String;", ordinal = 0))
    public String hook_getName(EntityPlayer player) {
        String s = _CmdNameColor.ColorNameDict.get(player.getUniqueID().toString());
        if (s == null) {
            s = _CmdNameColor.ColorNameDict.get(player.getName().toLowerCase());
        }
        if (s != null) {
            return s;
        }
        return player.getName();
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;", at = @At("HEAD"), cancellable = true)
    private void onItemDropped(ItemStack var1, boolean var2, boolean var3, CallbackInfoReturnable<EntityItem> callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptItemDrop((MC_Player) this, (MC_ItemStack) (Object) var1, ei);
        if (ei.isCancelled) {
            callbackInfo.setReturnValue(null);
        }
    }

    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "net.minecraft.world.GameRules.getBoolean(Ljava/lang/String;)Z"))
    private boolean onDeath(GameRules gameRules, String key) {
        return "keepInventory".equals(key) && _DiwUtils.OpsKeepInventory && ((MC_Player) this).isOp() || gameRules.getBoolean(key);
    }

    @Redirect(method = "getExperiencePoints", at = @At(value = "INVOKE", target = "net.minecraft.world.GameRules.getBoolean(Ljava/lang/String;)Z"))
    private boolean getExperiencePoints(GameRules gameRules, String key) {
        return "keepInventory".equals(key) && _DiwUtils.OpsKeepInventory && ((MC_Player) this).isOp() || gameRules.getBoolean(key);
    }

    @Override
    public List<MC_ItemStack> getArmor() {
        return PluginHelper.copyInvList(inventory.armorInventory);
    }

    @Override
    public void setArmor(List<MC_ItemStack> var1) {
        PluginHelper.updateInv(inventory.armorInventory, var1);
    }
}
