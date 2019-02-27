package org.mybatis.generator.internal.util;

import java.io.*;
import org.mybatis.generator.internal.util.messages.*;
import java.net.*;
import java.util.*;

public class ClassloaderUtility
{
    public static ClassLoader getCustomClassloader(final Collection<String> entries) {
        final List<URL> urls = new ArrayList<URL>();
        if (entries != null) {
            for (final String classPathEntry : entries) {
                final File file = new File(classPathEntry);
                if (!file.exists()) {
                    throw new RuntimeException(Messages.getString("RuntimeError.9", classPathEntry));
                }
                try {
                    urls.add(file.toURI().toURL());
                }
                catch (MalformedURLException e) {
                    throw new RuntimeException(Messages.getString("RuntimeError.9", classPathEntry));
                }
            }
        }
        final ClassLoader parent = Thread.currentThread().getContextClassLoader();
        final URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[urls.size()]), parent);
        return ucl;
    }
}
