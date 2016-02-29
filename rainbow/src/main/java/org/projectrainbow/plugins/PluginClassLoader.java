package org.projectrainbow.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.appender.SyslogAppender;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by florian on 20.12.14.
 */
public class PluginClassLoader extends URLClassLoader {
	private final Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();
	private final PluginManager pluginManager;
	private final File file;

	static {
		try {
			java.lang.reflect.Method method = ClassLoader.class.getDeclaredMethod("registerAsParallelCapable");
			if (method != null) {
				boolean oldAccessible = method.isAccessible();
				method.setAccessible(true);
				method.invoke(null);
				method.setAccessible(oldAccessible);
				LogManager.getLogger().info("Set PluginClassLoader as parallel capable");
			}
		}
		catch (NoSuchMethodException ignored) {
			// Ignore
		}
		catch (Exception ex) {
			LogManager.getLogger().warn("Error setting PluginClassLoader as parallel capable", ex);
		}
	}

	PluginClassLoader(final ClassLoader parent, final PluginManager pluginManager, final File file) throws MalformedURLException {
		super(new URL[] { file.toURI().toURL() }, parent);

		this.file = file;
		this.pluginManager = pluginManager;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return findClass(name, true);
	}

	Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
		Class<?> result = classes.get(name);

		if (result == null) {
			if (checkGlobal) {
				result = pluginManager.getClassByName(name);
			}

			if (result == null) {
				result = super.findClass(name);

				if (result != null) {
					pluginManager.setClass(name, result);
				}
			}

			classes.put(name, result);
		}

		return result;
	}

	Set<String> getClasses() {
		return classes.keySet();
	}
}
