package org.projectrainbow.interfaces;

import net.minecraft.command.CommandSource;

import java.util.function.Predicate;

public interface IMixinCommandNode {

    void setRequirement(Predicate<CommandSource> requirement);
}
