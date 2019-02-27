package org.mybatis.generator.internal;

import org.mybatis.generator.api.*;
import java.io.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.exception.*;
import java.util.*;

public class DefaultShellCallback implements ShellCallback
{
    private boolean overwrite;
    
    public DefaultShellCallback(final boolean overwrite) {
        this.overwrite = overwrite;
    }
    
    @Override
    public File getDirectory(final String targetProject, final String targetPackage) throws ShellException {
        final File project = new File(targetProject);
        if (!project.isDirectory()) {
            throw new ShellException(Messages.getString("Warning.9", targetProject));
        }
        final StringBuilder sb = new StringBuilder();
        final StringTokenizer st = new StringTokenizer(targetPackage, ".");
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }
        final File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            final boolean rc = directory.mkdirs();
            if (!rc) {
                throw new ShellException(Messages.getString("Warning.10", directory.getAbsolutePath()));
            }
        }
        return directory;
    }
    
    @Override
    public void refreshProject(final String project) {
    }
    
    @Override
    public boolean isMergeSupported() {
        return false;
    }
    
    @Override
    public boolean isOverwriteEnabled() {
        return this.overwrite;
    }
    
    @Override
    public String mergeJavaFile(final String newFileSource, final File existingFile, final String[] javadocTags, final String fileEncoding) throws ShellException {
        throw new UnsupportedOperationException();
    }
}
