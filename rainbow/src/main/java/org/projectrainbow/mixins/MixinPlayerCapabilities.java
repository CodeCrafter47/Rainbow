package org.projectrainbow.mixins;

import net.minecraft.entity.player.PlayerCapabilities;
import org.projectrainbow.interfaces.IMixinPlayerCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerCapabilities.class)
public class MixinPlayerCapabilities implements IMixinPlayerCapabilities {

	@Shadow
	private double flySpeed;
	
	@Shadow
	private float walkSpeed;

	@Override
	public void setFlySpeed(float flySpeed) {
		this.flySpeed = flySpeed;
	}

	@Override
	public void setWalkSpeed(float walkSpeed) {
		this.walkSpeed = walkSpeed;
	}
}
