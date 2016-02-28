package org.projectrainbow.mixins;

import PluginReference.ChatColor;
import PluginReference.MC_Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChatComponentText;
import net.minecraft.src.IChatComponent;
import net.minecraft.src.Profiler;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.jz;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectrainbow.Hooks;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._Announcer;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._EconomyManager;
import org.projectrainbow._Janitor;
import org.projectrainbow.commands._CmdCron;
import org.projectrainbow.interfaces.IMixinMinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.src.ng.at;
import static org.projectrainbow.launch.Bootstrap.logger;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements IMixinMinecraftServer {
    @Shadow
    @Final
    private static Logger logger;
    @Shadow
    private int tickCounter;

    @Shadow
    @Final
    public Profiler theProfiler;

    @Shadow
    private ServerConfigurationManager serverConfigManager;
    private int g_restartCountdown;

    @Shadow
    protected abstract void saveAllWorlds(boolean b);

    @Shadow
    public abstract void a(jz var1);

    @Shadow
    @Final
    private jz q;
    @Shadow
    private String motd;

    int g_secondTick;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "net.minecraft.src.ServerConfigurationManager.saveAllPlayerData()V"))
    void cancelMojangAutosave1(ServerConfigurationManager serverConfigManager) {

    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "net.minecraft.server.MinecraftServer.saveAllWorlds(Z)V"))
    void cancelMojangAutosave2(MinecraftServer minecraftServer, boolean b) {

    }

    @Inject(method = "tick", at = @At("RETURN"))
    void hook_onTick(CallbackInfo callbackInfo) {
        if (this.tickCounter % 10 == 0) {
            _CmdCron.Run500ms();
            _DiwUtils.Do_ArmorStand_Fun_At_Spawn();
        }

        if (this.tickCounter % 20 == 0) {
            ++g_secondTick;
            this.Do_Second_Based_Things();
            _Announcer.runSecondCount();
        }

        if (this.tickCounter % (1200 * _DiwUtils.AutoSaveMinutes) == 0) {
            System.out.println("----------------------   AUTO SAVE   ----------------------");
            _DiwUtils.SaveStuffs();
            System.out.println("----------------------   AUTO SAVE   ----------------------");
            System.out.println("==============================================================");
            LogManager.getLogger().info("Saving Players and Worlds...");
            if (_DiwUtils.DoSavingWorldNotice) {
                _DiwUtils.MessageAllPlayers(
                        _DiwUtils.RainbowString("Saving World"));
            }

            System.out.println("\n----------- PLAYER LIST ----------");
            for (MC_Player player : ServerWrapper.getInstance().getPlayers()) {
                String nanoEnd = String.format("%-16s %s",
                        player.getName(), player.getLocation().toString());
                System.out.println(nanoEnd);
            }
            System.out.println("----------------------------------\n");
            this.DumpDiwMemInfo();
            System.out.println("----------------------------------\n");
            this.theProfiler.startSection("save");
            this.serverConfigManager.saveAllPlayerData();
            this.saveAllWorlds(true);
            this.theProfiler.endSection();
            System.out.println("==============================================================");
        }

        Hooks.onTick(tickCounter);
    }

    private void DumpDiwMemInfo() {
        Runtime rt = Runtime.getRuntime();
        double maxGig = (double) ((float) rt.maxMemory() / 1.07374182E9F);
        double allocGig = (double) ((float) rt.totalMemory() / 1.07374182E9F);
        double freeGig = (double) ((float) rt.freeMemory() / 1.07374182E9F);
        double usagePerc = 100.0D * (allocGig - freeGig) / maxGig;
        String usageMsg = " Good";
        if (usagePerc > 50.0D) {
            usageMsg = " OK";
        }

        if (usagePerc > 90.0D) {
            usageMsg = " High";
        }

        String msg = "Uptime: " + _DiwUtils.TimeDeltaString(System.currentTimeMillis() - _DiwUtils.ServerStartTime);
        System.out.println(msg);
        msg = "RAM Usage: " + String.format("%.1fperc %s (%.1f of %.1f GB)", usagePerc, usageMsg, allocGig - freeGig, maxGig);
        System.out.println(msg);
    }

    public void Do_Second_Based_Things() {
        if (_DiwUtils.DoJanitor) {
            if (g_secondTick % (long) _DiwUtils.JanitorInterval == (long) (_DiwUtils.JanitorInterval - _DiwUtils.JanitorWarnSecs)) {
                String msg1 = _DiwUtils.TranslateColorString("&k|&b&lJanitor&f&k|&f ", true);
                String msg2 = _DiwUtils.TranslateColorString("&eGround Items/Boats/etc removed in " + _DiwUtils.JanitorWarnSecs + " secs", true);
                _DiwUtils.MessageAllPlayers(msg1 + msg2);
            }

            if (g_secondTick % (long) _DiwUtils.JanitorInterval == 0L) {
                System.out.println("-----------------------------------------------------------");
                this.DumpDiwMemInfo();
                _Janitor.DoMobClean(null, true, new String[]{"Item", "XPOrb", "arrow"});
                String msg1 = _DiwUtils.TranslateColorString("&b&lJanitor: ", true);
                String msg2 = _DiwUtils.TranslateColorString("&aGround items cleaned!", true);
                _DiwUtils.MessageAllPlayers(msg1 + msg2);
                System.out.println("-----------------------------------------------------------");
            }
        }

        if (g_secondTick % 3L == 0L) {
            _DiwUtils.Do_Spawn_Forcefield_MobClean();
        }

        if (_DiwUtils.DoPaydays
                && g_secondTick % (long) (60 * _DiwUtils.PayDayMinutes) == 0L) {
            System.out.println("----------------------------------\n");
            _EconomyManager.DoPayDay();
            System.out.println("----------------------------------\n");
        }

        if (g_restartCountdown >= 0L) {
            String var5 = ChatColor.MAGIC + "|" + ChatColor.RESET + " "
                    + ChatColor.LIGHT_PURPLE + "[AUTO-RESTART] "
                    + ChatColor.MAGIC + "|" + ChatColor.RESET + " "
                    + ChatColor.AQUA;
            --g_restartCountdown;
            if (g_restartCountdown == 60L) {
                _DiwUtils.MessageAllPlayers(
                        var5 + ChatColor.GREEN
                                + "Server restart in 60 seconds...");
            } else if (g_restartCountdown == 52L) {
                _DiwUtils.MessageAllPlayers(
                        var5 + "Don\'t panic, it will return quickly");
            } else if (g_restartCountdown == 30L) {
                _DiwUtils.MessageAllPlayers(
                        var5 + ChatColor.WHITE + "Maybe a hug will help... "
                                + ChatColor.RED + "\u2764");
            } else if (g_restartCountdown == 10L) {
                _DiwUtils.MessageAllPlayers(
                        var5 + ChatColor.RED + "Restarting in 10 seconds...");
            } else if (g_restartCountdown == 1L) {
                _DiwUtils.MessageAllPlayers(
                        var5 + ChatColor.DARK_RED + "Restarting...");
            } else if (g_restartCountdown == 0L) {
                ServerWrapper.getInstance().executeCommand("stop");
            }
        }

    }

    @Inject(method = "stopServer", at = @At("RETURN"))
    void hook_onShutdown(CallbackInfo callbackInfo) {
        _DiwUtils.Shutdown();
    }

    @ModifyArg(method = "a(Lnet/minecraft/src/jz;)V", at = @At(value = "INVOKE", target = "net.minecraft.server.MinecraftServer.getFile(Ljava/lang/String;)Ljava/io/File;"))
    private String setServerIcon(String old) {
        return ServerWrapper.serverIconFileName;
    }

    @Overwrite
    public void addChatMessage(IChatComponent var1) {
        logger.info(_ColorHelper.stripColor(var1.getUnformattedText()));
    }

    @Override
    public void onServerIconUpdated() {
        this.a(this.q);
    }

    @Override
    public void onMotdUpdated() {
        this.q.a(new ChatComponentText(this.motd));
    }
}
