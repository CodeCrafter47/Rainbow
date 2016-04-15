package org.projectrainbow.mixins;

import net.minecraft.src.Container;
import net.minecraft.src.ContainerPlayer;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.OutboundPacketSetSlot;
import net.minecraft.src.qg;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerPlayer.class)
public abstract class MixinContainerPlayer extends Container{
    @Shadow
    public InventoryCrafting craftMatrix;
    @Shadow
    public qg dragMode;
    @Shadow
    @Final
    private EntityPlayer thePlayer;

    @Overwrite
    public void a(qg var1) {
        ItemStack itemStack = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj);
        this.dragMode.setInventorySlotContents(0, itemStack);
        if (super.crafters.size() < 1) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) super.crafters.get(0);
        player.playerNetServerHandler.sendPacket(new OutboundPacketSetSlot(player.openContainer.windowId, 0, itemStack));
    }
}
