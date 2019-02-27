package org.mybatis.generator.logging.nologging;

import org.mybatis.generator.logging.*;

public class NoLoggingImpl implements Log
{
    public NoLoggingImpl(final Class<?> clazz) {
    }
    
    @Override
    public boolean isDebugEnabled() {
        return false;
    }
    
    @Override
    public void error(final String s, final Throwable e) {
    }
    
    @Override
    public void error(final String s) {
    }
    
    @Override
    public void debug(final String s) {
    }
    
    @Override
    public void warn(final String s) {
    }
}
