package org.mybatis.generator.logging;

public interface Log
{
    boolean isDebugEnabled();
    
    void error(final String p0, final Throwable p1);
    
    void error(final String p0);
    
    void debug(final String p0);
    
    void warn(final String p0);
}
