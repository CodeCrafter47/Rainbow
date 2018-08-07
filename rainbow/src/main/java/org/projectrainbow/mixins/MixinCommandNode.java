package org.projectrainbow.mixins;

import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.CommandSource;
import org.projectrainbow.interfaces.IMixinCommandNode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;

@Mixin(value = CommandNode.class, remap = false)
public class MixinCommandNode implements IMixinCommandNode {

    @Shadow(remap = false)
    @Final
    @Mutable
    private Predicate<CommandSource> requirement;

    @Override
    public void setRequirement(Predicate<CommandSource> requirement) {
        this.requirement = requirement;
    }
}
