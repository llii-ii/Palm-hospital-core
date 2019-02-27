package org.mybatis.generator.api;

import java.io.*;
import org.mybatis.generator.exception.*;

public interface ShellCallback
{
    File getDirectory(final String p0, final String p1) throws ShellException;
    
    String mergeJavaFile(final String p0, final File p1, final String[] p2, final String p3) throws ShellException;
    
    void refreshProject(final String p0);
    
    boolean isMergeSupported();
    
    boolean isOverwriteEnabled();
}
