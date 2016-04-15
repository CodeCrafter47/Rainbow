package org.projectrainbow.plugins;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import com.google.common.base.Objects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BlockPos;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.ICommandSender;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

// todo catch exceptions
public class PluginCommand extends CommandBase {
    private final MC_Command delegate;

    public PluginCommand(MC_Command delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getCommandName() {
        return delegate.getCommandName();
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return delegate.getHelpLine(sender instanceof MC_Player ? (MC_Player) sender : null);
    }

    @Override
    public void processCommand(MinecraftServer minecraftServer, ICommandSender sender, String[] args) throws CommandException {
        delegate.handleCommand(sender instanceof MC_Player ? (MC_Player) sender : null, args);
    }

    @Override
    public List<String> getCommandAliases() {
        return Objects.firstNonNull(delegate.getAliases(), Collections.<String>emptyList());
    }

    @Override
    public boolean canCommandSenderUseCommand(MinecraftServer minecraftServer, ICommandSender sender) {
        return delegate.hasPermissionToUse(sender instanceof MC_Player ? (MC_Player) sender : null);
    }

    @Override
    public List<String> addTabCompletionOptions(MinecraftServer minecraftServer, ICommandSender sender, String[] args, BlockPos blockPos) {
        return Objects.firstNonNull(delegate.getTabCompletionList(sender instanceof MC_Player ? (MC_Player) sender : null, args), Collections.<String>emptyList());
    }
}
