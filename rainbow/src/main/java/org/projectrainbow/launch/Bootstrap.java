package org.projectrainbow.launch;

import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;

public class Bootstrap {
    public static String[] args;
    public static Set<File> tweakers = new HashSet<File>();

    public final static String minecraftVersion;
    public final static String buildNumber;

    public static Logger logger = LogManager.getLogger("Minecraft");

    public static void main(String[] args) {
        Bootstrap.args = args;

        logger.info("Searching for additional tweakers...");
        File BukkitPluginsloaderJar = new File("plugins_mod" + File.separator + "PluginBukkitBridge.jar");
        if (BukkitPluginsloaderJar.exists()) {
            logger.info("Found BUKKIT tweaker...");
            logger.info(" Loaded org.bukkit.Bukkit");
            logger.info(" Loaded org.bukkit.plugin.java.PluginClassLoader");
        } else {
            try {
                logger.info("Downloading BUKKIT tweaker...");
                URL BukkitBridgeDLURL = new URL("http://www.project-rainbow.org/site/index.php?action=downloads;sa=downfile&id=69");
                FileUtils.copyURLToFile(BukkitBridgeDLURL, BukkitPluginsloaderJar);
                logger.info("Downloaded BUKKIT tweaker...");
                logger.info(" Loaded org.bukkit.Bukkit");
                logger.info(" Loaded org.bukkit.plugin.java.PluginClassLoader");
            } catch (IOException e) {
                logger.info("Could not load BUKKIT BRIDGE, bukkit plugins will not work unless you download RainbowBukkitBridge, Full Error: " + e);
            }
        }

        List<String> tweakClasses = new ArrayList<String>() {{
            add("org.projectrainbow.launch.ServerTweaker");
            add("org.spongepowered.asm.launch.MixinTweaker");
        }};

        File plugins_mod = new File("plugins_mod");
        if (plugins_mod.exists() && plugins_mod.isDirectory()) {
            for (File file : plugins_mod.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    try {
                        JarFile jarFile = new JarFile(file);
                        Manifest ex = jarFile.getManifest();
                        if (ex != null) {
                            Attributes var3 = ex.getMainAttributes();
                            String tweakClass = var3.getValue("TweakClass");
                            if (tweakClass != null) {
                                tweakers.add(file);
                                addURL(file.toURI().toURL());
                                if (!tweakClasses.contains(tweakClass)) {
                                    tweakClasses.add(tweakClass);
                                }
                            }
                        }
                        jarFile.close();
                    } catch (Throwable th) {
                        logger.error("Failed to load file: " + file, th);
                    }
                }
            }
        }

        List<String> options = new ArrayList<String>() {{
            add("-version");
            add("unknown");
            add("-gameDir");
            add(".");
            add("-assetsDir");
            add(".");
        }};

        if (isMinecraftServerIncluded()) {
            // dev environment
            options.add("--mixin");
            options.add("mixins.json");
        }

        for (String tweakClass : tweakClasses) {
            options.add("--tweakClass");
            options.add(tweakClass);
        }

        Launch.main(options.toArray(new String[options.size()]));
    }

    private static void addURL(URL u) throws IOException {
        URLClassLoader sysloader = (URLClassLoader) Launch.class.getClassLoader();
        Class sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(sysloader, u);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }

    public static boolean isMinecraftServerIncluded() {
        try {
            Class.forName("com.mojang.util.QueueLogAppender");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    static {
        try {
            Properties properties = new Properties();
            properties.load(Bootstrap.class.getClassLoader().getResourceAsStream("build.properties"));
            minecraftVersion = properties.getProperty("mcversion");
            buildNumber = properties.getProperty("build");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
