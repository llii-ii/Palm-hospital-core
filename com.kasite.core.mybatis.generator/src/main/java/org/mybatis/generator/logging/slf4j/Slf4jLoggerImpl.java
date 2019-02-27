package org.mybatis.generator.logging.slf4j;

import org.mybatis.generator.logging.*;
import org.slf4j.*;

class Slf4jLoggerImpl implements Log
{
    private Logger log;
    
    public Slf4jLoggerImpl(final Logger logger) {
        this.log = logger;
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        this.log.error(s, e);
    }
    
    @Override
    public void error(final String s) {
        this.log.error(s);
    }
    
    @Override
    public void debug(final String s) {
        this.log.debug(s);
    }
    
    @Override
    public void warn(final String s) {
        this.log.warn(s);
    }
}
