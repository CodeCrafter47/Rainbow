package org.projectrainbow.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.play.server.SPacketSetSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerPlayer.class)
public abstract class MixinContainerPlayer extends Container {
    @Shadow
    public InventoryCrafting craftMatrix;
    @Shadow
    public IInventory craftResult;
    @Shadow
    @Final
    private EntityPlayer thePlayer;

    @Overwrite
    public void onCraftMatrixChanged(IInventory var1) {
        ItemStack itemStack = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.world);
        this.craftResult.setInventorySlotContents(0, itemStack);
        if (super.listeners.size() < 1) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) super.listeners.get(0);
        player.connection.sendPacket(new SPacketSetSlot(player.openContainer.windowId, 0, itemStack));
    }
}
