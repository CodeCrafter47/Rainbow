package org.projectrainbow.launch;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.concurrent.TimeUnit;

public class Bootstrap {
    public static String[] args;
    public static Set<File> tweakers = new HashSet<File>();

    public final static String minecraftVersion;
    public final static String buildNumber;

    public static Logger logger = LogManager.getLogger("Minecraft");

    public static void main(String[] args) {
        Bootstrap.args = args;

        String jversion = System.getProperty("java.version").split("_")[0].replace("1.", "").replace(".0", "").replace("-ea", "");
        if (Integer.parseInt(jversion) < 8) {
            logger.info("*** WARNNING, your Java is outdated         ***");
            logger.info("*** Java 8 will be required as of 1.12      ***");
            logger.info("*** Get Java 8: http://bit.ly/Java8Download ***");
            logger.info("*** Server will start in 5 seconds...       ***");
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        logger.info("Searching for additional tweakers...");

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
                        logger.error("Failed to load tweaker file " + file, th);
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
        
        if (Bootstrap.class.getClassLoader() instanceof URLClassLoader){
            Launch.main(options.toArray(new String[options.size()]));
        } else {
            //Temp fix for Java 9-ea until Launchwrapper updates.
            //Java 9-ea has lots of proformance fixes.
            final Class<?> clazz = Launch.class;
            Launch.classLoader = new LaunchClassLoader(getURLs());
            Launch.blackboard = new HashMap<String,Object>();
            Thread.currentThread().setContextClassLoader(Launch.classLoader);
            try {
                Constructor<?> constructor = Launch.class.getDeclaredConstructor(new Class[0]);
                constructor.setAccessible(true);
                Object l = constructor.newInstance(new Object[0]);
        
                Method m = clazz.getDeclaredMethod("launch", new Class[]{String[].class});
                m.setAccessible(true);
                m.invoke((Launch) l, (Object) options.toArray(new String[options.size()]));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Your system does't support the Java 9-ea workaround. Please use Java 8");
            }
        }
    }

    public static URL[] getURLs() {
        String cp = System.getProperty("java.class.path");
        String[] elements = cp.split(File.pathSeparator);
        if (elements.length == 0) elements = new String[] {""};
        URL[] urls = new URL[elements.length];
        for (int i = 0; i < elements.length; i++) {
            try {
                urls[i] = new File(elements[i]).toURI().toURL();
            } catch (MalformedURLException ignore){/**/}
        }
        return urls;
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
