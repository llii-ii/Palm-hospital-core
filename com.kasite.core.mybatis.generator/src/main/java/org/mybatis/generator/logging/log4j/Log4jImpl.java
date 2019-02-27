package org.mybatis.generator.logging.log4j;

import org.apache.log4j.Priority;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mybatis.generator.logging.Log;

public class Log4jImpl implements Log
{
    private static final String FQCN;
    private Logger log;
    
    public Log4jImpl(final Class<?> clazz) {
        this.log = Logger.getLogger((Class)clazz);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        this.log.log(Log4jImpl.FQCN, (Priority)Level.ERROR, (Object)s, e);
    }
    
    @Override
    public void error(final String s) {
        this.log.log(Log4jImpl.FQCN, (Priority)Level.ERROR, (Object)s, (Throwable)null);
    }
    
    @Override
    public void debug(final String s) {
        this.log.log(Log4jImpl.FQCN, (Priority)Level.DEBUG, (Object)s, (Throwable)null);
    }
    
    @Override
    public void warn(final String s) {
        this.log.log(Log4jImpl.FQCN, (Priority)Level.WARN, (Object)s, (Throwable)null);
    }
    
    static {
        FQCN = Log4jImpl.class.getName();
    }
}
