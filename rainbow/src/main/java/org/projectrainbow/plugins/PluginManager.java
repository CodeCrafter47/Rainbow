package org.projectrainbow.plugins;

import PluginReference.PluginBase;
import PluginReference.PluginInfo;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow.launch.Bootstrap;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PluginManager {
    public ArrayList<PluginInfo> plugins = new ArrayList<PluginInfo>();
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();
    private final List<PluginClassLoader> loaders = new ArrayList<PluginClassLoader>();

    public void enable() {
        LoadPlugins();
        ReorderPlugins();
    }

    private void LoadPlugins() {
        System.out.println(
                "\n----------------------- Loading Plugins -----------------------");
        String pluginFolderName = "plugins_mod";
        File folder = new File(pluginFolderName);

        if (!folder.exists()) {
            System.out.println(
                    String.format("* FYI: No \'%s\' folder found.",
                            new Object[]{pluginFolderName}));
            System.out.println(" * Creating one now but will be empty...");
            folder.mkdir();
        } else {
            File[] orgListOfFiles = folder.listFiles();

            Arrays.sort((Object[]) orgListOfFiles);
            File[] listOfFiles = new File[orgListOfFiles.length];
            int idx = 0;

            int i;
            File filename;

            for (i = 0; i < orgListOfFiles.length; ++i) {
                filename = orgListOfFiles[i];
                if (filename.getName().startsWith("_")) {
                    listOfFiles[idx] = filename;
                    ++idx;
                }
            }

            for (i = 0; i < orgListOfFiles.length; ++i) {
                filename = orgListOfFiles[i];
                if (!filename.getName().startsWith("_")) {
                    listOfFiles[idx] = filename;
                    ++idx;
                }
            }

            for (i = 0; i < listOfFiles.length; ++i) {
                if (listOfFiles[i].isFile() && !Bootstrap.tweakers.contains(listOfFiles[i])) {
                    String var16 = listOfFiles[i].getName();

                    if (var16.endsWith(".jar")) {
                        System.out.println("\n\\/\\/\\/\\ " + var16);

                        try {
                            String exc = var16.substring(0, var16.length() - 4);
                            String className = exc + ".MyPlugin";
                            PluginClassLoader ClassLoader = new PluginClassLoader(Bootstrap.class.getClassLoader(), this, listOfFiles[i]);

                            URL resource = ClassLoader.getResource("plugin.properties");
                            InputStream stream = resource != null ? resource.openStream() : null;
                            Properties properties = null;
                            if (stream != null) {
                                properties = new Properties();
                                properties.load(stream);
                                className = properties.getProperty("main", className);
                            }

                            Class clazz;

                            try {
                                clazz = Class.forName(className, true,
                                        ClassLoader);
                            } catch (ClassNotFoundException var14) {
                                System.out.println(
                                        "* Did not find class name: "
                                                + className);
                                continue;
                            }

                            PluginBase plugin = (PluginBase) clazz.newInstance();
                            PluginInfo info = plugin.getPluginInfo();

                            if (info == null) {
                                info = new PluginInfo();
                            }

                            if (info.description == null
                                    || info.description.trim().length() <= 0) {
                                info.description = "No Description";
                            }

                            info.ref = plugin;
                            info.path = var16;
                            info.name = exc;
                            plugins.add(info);
                            loaders.add(ClassLoader);
                            plugin.onStartup(ServerWrapper.getInstance());
                        } catch (Throwable var15) {
                            var15.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    private void ReorderPlugins() {
        if (plugins.size() > 0) {
            try {
                Collections.sort(plugins,
                        new java.util.Comparator<PluginInfo>() {
                            public int compare(PluginInfo o1, PluginInfo o2) {
                                return o1.eventSortOrder > o2.eventSortOrder
                                        ? 1
                                        : (o1.eventSortOrder < o2.eventSortOrder
                                        ? -1
                                        : 0);
                            }
                        });

                int exc;

                for (exc = 0; exc < 20; ++exc) {
                    boolean info = false;
                    boolean didActionPass = false;

                    int i;
                    PluginInfo info1;
                    boolean idxFound;
                    int j;
                    PluginInfo tgtInfo;
                    boolean foundName;
                    String name;
                    Iterator var10;

                    for (i = 1; i < plugins.size(); ++i) {
                        info1 = (PluginInfo) plugins.get(i);
                        if (info1.pluginNamesINeedToGetEventsBefore != null
                                && info1.pluginNamesINeedToGetEventsBefore.size()
                                > 0) {
                            idxFound = false;

                            for (j = 0; j < i; ++j) {
                                tgtInfo = (PluginInfo) plugins.get(j);
                                foundName = false;
                                var10 = info1.pluginNamesINeedToGetEventsBefore.iterator();

                                while (var10.hasNext()) {
                                    name = (String) var10.next();
                                    if (name != null
                                            && name.equalsIgnoreCase(
                                            tgtInfo.name)) {
                                        foundName = true;
                                        break;
                                    }
                                }

                                if (foundName) {
                                    didActionPass = true;
                                    info = true;
                                    info1.eventSortOrder = tgtInfo.eventSortOrder
                                            - 1.0E-5D;
                                    break;
                                }
                            }
                        }
                    }

                    if (didActionPass) {
                        Collections.sort(plugins,
                                new java.util.Comparator<PluginInfo>() {
                                    public int compare(PluginInfo o1, PluginInfo o2) {
                                        return o1.eventSortOrder > o2.eventSortOrder
                                                ? 1
                                                : (o1.eventSortOrder < o2.eventSortOrder
                                                ? -1
                                                : 0);
                                    }
                                });
                    }

                    didActionPass = false;

                    for (i = 0; i < plugins.size() - 1; ++i) {
                        info1 = (PluginInfo) plugins.get(i);
                        if (info1.pluginNamesINeedToGetEventsAfter != null
                                && info1.pluginNamesINeedToGetEventsAfter.size()
                                > 0) {
                            idxFound = false;

                            for (j = plugins.size() - 1; j > i; --j) {
                                tgtInfo = (PluginInfo) plugins.get(j);
                                foundName = false;
                                var10 = info1.pluginNamesINeedToGetEventsAfter.iterator();

                                while (var10.hasNext()) {
                                    name = (String) var10.next();
                                    if (name != null
                                            && name.equalsIgnoreCase(
                                            tgtInfo.name)) {
                                        foundName = true;
                                        break;
                                    }
                                }

                                if (foundName) {
                                    didActionPass = true;
                                    info = true;
                                    info1.eventSortOrder = tgtInfo.eventSortOrder
                                            + 1.0E-5D;
                                    break;
                                }
                            }
                        }
                    }

                    if (didActionPass) {
                        Collections.sort(plugins,
                                new java.util.Comparator<PluginInfo>() {
                                    public int compare(PluginInfo o1, PluginInfo o2) {
                                        return o1.eventSortOrder > o2.eventSortOrder
                                                ? 1
                                                : (o1.eventSortOrder < o2.eventSortOrder
                                                ? -1
                                                : 0);
                                    }
                                });
                    }

                    if (!info) {
                        break;
                    }
                }

                System.out.println("\nLoaded Plugins and Ordering:");
                System.out.println("----------------------------");

                for (exc = 0; exc < plugins.size(); ++exc) {
                    PluginInfo var12 = (PluginInfo) plugins.get(exc);

                    System.out.println(
                            String.format("%26s : %s",
                                    new Object[]{var12.name, var12.description}));
                }

                System.out.println("----------------------------");
            } catch (Exception var11) {
                var11.printStackTrace();
            }

        }
    }

    Class<?> getClassByName(final String name) {
        Class<?> cachedClass = classes.get(name);

        if (cachedClass != null) {
            return cachedClass;
        } else {
            for (PluginClassLoader loader : loaders) {

                try {
                    cachedClass = loader.findClass(name, false);
                } catch (ClassNotFoundException cnfe) {
                }
                if (cachedClass != null) {
                    return cachedClass;
                }
            }
        }
        return null;
    }

    void setClass(final String name, final Class<?> clazz) {
        if (!classes.containsKey(name)) {
            classes.put(name, clazz);
        }
    }
}
