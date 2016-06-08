package org.projectrainbow.mixins;

import net.minecraft.init.Bootstrap;
import org.apache.logging.log4j.Logger;
import org.projectrainbow.util.LoggingErrorStream;
import org.projectrainbow.util.LoggingPrintStream;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.PrintStream;

@Mixin(Bootstrap.class)
public class MixinBootstrap {
    @Shadow
    @Final
    private static PrintStream SYSOUT;

    @Redirect(method = "register", at = @At(value = "INVOKE", target = "isDebugEnabled", remap = false))
    private static boolean logStreams(Logger logger) {
        return true;
    }

    /**
     * we redirect all System.err messages to the logger. That way they end up being
     * in the log file.
     *
     * we could also do that for the System.out messages, but I might break the fancy
     * rainbow logo.
     */
    @Overwrite
    private static void redirectOutputToLog() {
        System.setErr(new LoggingErrorStream("STDERR", System.err));
        System.setOut(new LoggingPrintStream("STDOUT", SYSOUT));
    }
}
