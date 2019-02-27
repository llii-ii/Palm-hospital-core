package org.mybatis.generator.logging.slf4j;

import org.mybatis.generator.logging.*;
import org.slf4j.spi.*;
import org.slf4j.*;

public class Slf4jImpl implements Log
{
    private Log log;
    
    public Slf4jImpl(final Class<?> clazz) {
        final Logger logger = LoggerFactory.getLogger((Class)clazz);
        if (logger instanceof LocationAwareLogger) {
            try {
                logger.getClass().getMethod("log", Marker.class, String.class, Integer.TYPE, String.class, Object[].class, Throwable.class);
                this.log = new Slf4jLocationAwareLoggerImpl((LocationAwareLogger)logger);
                return;
            }
            catch (SecurityException ex) {}
            catch (NoSuchMethodException ex2) {}
        }
        this.log = new Slf4jLoggerImpl(logger);
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
