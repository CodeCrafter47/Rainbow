package org.projectrainbow.plugins;

import PluginReference.MC_Command;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;

import java.util.function.Predicate;

public class PluginCommand extends LiteralCommandNode<CommandSource> {
    public final MC_Command delegate;

    public PluginCommand(MC_Command delegate, String literal, Command<CommandSource> command, Predicate<CommandSource> requirement, CommandNode<CommandSource> redirect, RedirectModifier<CommandSource> modifier, boolean forks) {
        super(literal, command, requirement, redirect, modifier, forks);
        this.delegate = delegate;
    }
}
