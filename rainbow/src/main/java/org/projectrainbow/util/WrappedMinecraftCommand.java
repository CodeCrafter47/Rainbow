package org.projectrainbow.util;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.EntityPlayerMP;
import org.projectrainbow._DiwUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WrappedMinecraftCommand implements MC_Command {
    public final CommandNode<CommandSource> delegate;

    public WrappedMinecraftCommand(CommandNode<CommandSource> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof WrappedMinecraftCommand && delegate.equals(((WrappedMinecraftCommand) obj).delegate);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public String getCommandName() {
        return delegate.getName();
    }

    @Override
    public List<String> getAliases() {
        return _DiwUtils.getMinecraftServer()
                .getCommandManager()
                .getDispatcher()
                .getRoot()
                .getChildren()
                .stream()
                .filter(node -> node.getRedirect() == delegate)
                .map(CommandNode::getName)
                .collect(Collectors.toList());
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return delegate.getUsageText();
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        try {
            _DiwUtils.getMinecraftServer()
                    .getCommandManager()
                    .handleCommand(plr == null
                    ? _DiwUtils.getMinecraftServer().getCommandSource()
                    : ((EntityPlayerMP)plr).getCommandSource(),
                            delegate.getName() + " " + Arrays.stream(args).collect(Collectors.joining(" ")));
        } catch (CommandException e) {
            Util.sneakyThrow(e);
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return delegate.getRequirement().test(((EntityPlayerMP) plr).getCommandSource());
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return Collections.emptyList(); // todo
    }
}
