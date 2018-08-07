package org.projectrainbow.launch;

import com.google.common.io.ByteStreams;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ServerTweaker implements ITweaker {
    static {
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
                    URL url = new URL(Bootstrap.serverURL);
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
