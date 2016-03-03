package org.projectrainbow.plugins;

import sun.misc.CompoundEnumeration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;

public class FilteringClassLoader extends ClassLoader {
    public FilteringClassLoader(ClassLoader delegate) {
        super(delegate);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (isBadClass(name)) {
            throw new ClassNotFoundException(name);
        }
        return super.findClass(name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (isBadClass(name)) {
            throw new ClassNotFoundException(name);
        }
        return super.loadClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean b) throws ClassNotFoundException {
        if (isBadClass(name)) {
            throw new ClassNotFoundException(name);
        }
        return super.loadClass(name, b);
    }

    @Override
    public URL getResource(String s) {
        if (isBadResource(s)) {
            return null;
        }
        return super.getResource(s);
    }

    @Override
    protected URL findResource(String s) {
        if (isBadResource(s)) {
            return null;
        }
        return super.findResource(s);
    }

    private boolean isBadClass(String name) {
        return name.startsWith("org.projectrainbow.") || name.startsWith("net.minecraft.") || name.startsWith("joebkt.") || !name.contains(".");
    }

    private boolean isBadResource(String name) {
        return name.equals("plugin.properties");
    }
}
