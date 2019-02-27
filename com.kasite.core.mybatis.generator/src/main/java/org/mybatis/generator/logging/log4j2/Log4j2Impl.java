package org.mybatis.generator.logging.log4j2;

import org.mybatis.generator.logging.*;
import org.apache.logging.log4j.spi.*;
import org.apache.logging.log4j.*;

public class Log4j2Impl implements Log
{
    private Log log;
    
    public Log4j2Impl(final Class<?> clazz) {
        final Logger logger = LogManager.getLogger((Class)clazz);
        if (logger instanceof AbstractLogger) {
            this.log = new Log4j2AbstractLoggerImpl((AbstractLogger)logger);
        }
        else {
            this.log = new Log4j2LoggerImpl(logger);
        }
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
