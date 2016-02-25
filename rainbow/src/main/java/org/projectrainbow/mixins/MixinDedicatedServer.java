package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ICommandSender;
import org.projectrainbow.Hooks;
import org.projectrainbow._DiwUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
 * Every mixin must be annotated with @Mixin(TargetClass.class)
 */
@Mixin(DedicatedServer.class)
public class MixinDedicatedServer {

    /*
     * @Inject allows us to add code to a method in the target class.
     *
     * Inject the rainbow startup code a the beginning of startServer() in DedicatedServer.
     */
    @Inject(method = "startServer", at = @At("HEAD"))
    void onServerStart(CallbackInfoReturnable<Boolean> callbackInfo) {
        _DiwUtils.minecraftServer = (MinecraftServer) (Object) this;
        _DiwUtils.ServerStartTime = System.currentTimeMillis();
        _DiwUtils.Startup();

        System.out.println(" ");
        System.out.println("                  ___________________________");
        System.out.println("           _,----'  _______________________  `----._");
        System.out.println("        ,-'  __,---'  ___________________  `---.__  `-.");
        System.out.println("     ,-'  ,-'  __,---'  _______________  `---.__  `-.  `-.");
        System.out.println("   ,'  ,-'  ,-'  __,---'                `---.__  `-.  `-.  `.");
        System.out.println("  /  ,'  ,-'  ,-'                               `-.  `-.  `.  \\");
        System.out.println(" / ,'  ,' ,--' ____        _       __              `--. `.  `. \\");
        System.out.println("| /  ,' ,'    /    \\____ _( )___  / /_  ____ _      __ `. `.  \\ |");
        System.out.println("             / /_/ / __ `/ / __ \\/ __ \\/ __ \\ | /| / /");
        System.out.println("            / _, _/ /_/ / / / / / /_/ / /_/ / |/ |/ / ");
        System.out.println("           /_/ |_|\\__,_/_/_/ /_/_.___/\\____/|__/|__/  ");
        System.out.println(" ");
        System.out.println("Rainbow Mod v" + _DiwUtils.DiwModVersion + ": Based on Minecraft version " + _DiwUtils.MC_VERSION_STRING);
    }

    /*
     * @Inject allows us to add code to a method in the target class.
     */
    @Inject(method = "startServer", at = @At("RETURN"))
    void onServerFullyLoaded(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (callbackInfo.getReturnValue()) {
            Hooks.onServerFullyLoaded();
        }
    }

    /**
     * Using @Overwrite we can replace methods in the target class
     *
     * I don't like the gui.
     * Let's replace setGuiEnabled with an empty method.
     *
     * Actually the server log window in the gui doesn't work.
     * I think this can be fixed by adding a classloader exclusion for com.mojang.
     *
     * @author CodeCrafter47
     * @reason We don't need the gui
     */
    @Overwrite
    public void setGuiEnabled() {
    }

    @Redirect(method = "executePendingCommands", at = @At(value = "INVOKE", target = "net.minecraft.src.ICommandManager.executeCommand(Lnet/minecraft/src/ICommandSender;Ljava/lang/String;)I"))
    private int onCommand(ICommandManager commandManager, ICommandSender commandSender, String command) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onConsoleInput(command, ei);
        if (!ei.isCancelled) {
            return commandManager.executeCommand(commandSender, command);
        }
        return 0;
    }
}
