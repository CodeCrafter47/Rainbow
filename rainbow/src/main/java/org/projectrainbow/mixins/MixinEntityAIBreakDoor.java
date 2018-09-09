package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIDoorInteract;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAIBreakDoor.class)
public class MixinEntityAIBreakDoor extends EntityAIDoorInteract {

    public MixinEntityAIBreakDoor(EntityLiving entityLiving) {
        super(entityLiving);
        // dummy
    }

    @Inject(method = "updateTask", at = @At("HEAD"), cancellable = true)
    private void grief(CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptEntityMiscGrief((MC_Entity) entity, new MC_Location(doorPosition.getX(), doorPosition.getY(), doorPosition.getZ(), PluginHelper.getLegacyDimensionId(entity.ap)), MC_MiscGriefType.ZOMBIE_DOOR_BREAK, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
