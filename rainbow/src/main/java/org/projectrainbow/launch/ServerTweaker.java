package org.projectrainbow.launch;

import com.google.common.io.ByteStreams;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinTweakContainer;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.util.JavaVersion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ServerTweaker implements ITweaker {
    static {
        MixinTweakContainer.agentClasses.remove("org.spongepowered.asm.launch.MixinLaunchAgentFML");
        if (JavaVersion.current() >= 1.7D) {
            // If running at least Java 7 set MixinCompatibilityLevel to Java 7,
            // so that the Mixin Processor doesn't throw an exception if it
            // encounters a Java 7 Mixin Class.
            // This is done to prevent a mixin failure in dev environment. In the
            // final jar our maven plugins convert all our Java 7 classes to Java 6.
            // But when running the server from the IDE that has not happened yet.
            MixinEnvironment.setCompatibilityLevel(MixinEnvironment.CompatibilityLevel.JAVA_7);
        }

        Launch.classLoader.addClassLoaderExclusion("com.google.gson.");
        Launch.classLoader.addClassLoaderExclusion("org.apache.commons.io");
        Launch.classLoader.addClassLoaderExclusion("PluginReference.");
        Launch.classLoader.addClassLoaderExclusion("joebkt.");

        if (!Bootstrap.isMinecraftServerIncluded()) {
            // download server jar
            File cache = new File("cache");
            if (!cache.exists()) {
                cache.mkdir();
            }

            File serverJar = new File(cache, "minecraft_server." + Bootstrap.minecraftVersion + ".jar");
            if (!serverJar.exists()) {
                Bootstrap.logger.info("Downloading minecraft server. Please wait.");
                try {
                    URL url = new URL("https://s3.amazonaws.com/Minecraft.Download/versions/" + Bootstrap.minecraftVersion + "/minecraft_server." + Bootstrap.minecraftVersion + ".jar");
                    InputStream in = url.openStream();
                    OutputStream out = new FileOutputStream(serverJar);
                    ByteStreams.copy(in, out);
                    in.close();
                    out.close();
                    Bootstrap.logger.info("Downloaded minecraft server. Starting.");
                } catch (IOException ex) {
                    Bootstrap.logger.error("Failed to download minecraft server.", ex);
                    System.exit(-1);
                }
            }

            try {
                Launch.classLoader.addURL(serverJar.toURI().toURL());
            } catch (MalformedURLException ex) {
                Bootstrap.logger.error("Failed to add minecraft server to classpath.", ex);
                System.exit(-1);
            }
        }
    }

    @Override
    public void acceptOptions(List<String> list, File file, File file1, String s) {

    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.server.MinecraftServer";
    }

    @Override
    public String[] getLaunchArguments() {
        return Bootstrap.args;
    }
}
