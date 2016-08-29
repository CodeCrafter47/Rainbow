package ml.rainbowplusplus.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.projectrainbow.launch.Bootstrap;

public class RPPUtils {
	File BukkitPluginsloaderJar = new File("plugins_mod" + File.separator + "PluginBukkitBridge.jar");
	public static void downloadBukkitBridge() {
    if (BukkitPluginsloaderJar.exists()) {
        Bootstrap.logger.info("Found BUKKIT tweaker...");
        Bootstrap.logger.info(" Loaded org.bukkit.Bukkit");
        Bootstrap.logger.info(" Loaded org.bukkit.plugin.java.PluginClassLoader");
    } else {
        try {
            Bootstrap.logger.info("Downloading BUKKIT tweaker...");
            URL BukkitBridgeDLURL = new URL("http://www.project-rainbow.org/site/index.php?action=downloads;sa=downfile&id=69");
            FileUtils.copyURLToFile(BukkitBridgeDLURL, BukkitPluginsloaderJar);
            Bootstrap.logger.info("Downloaded BUKKIT tweaker...");
            Bootstrap.logger.info(" Loaded org.bukkit.Bukkit");
            Bootstrap.logger.info(" Loaded org.bukkit.plugin.java.PluginClassLoader");
        } catch (IOException e) {
            Bootstrap.logger.info("Could not load BUKKIT BRIDGE, bukkit plugins will not work unless you download RainbowBukkitBridge, Full Error: " + e);
        }
    }
}
