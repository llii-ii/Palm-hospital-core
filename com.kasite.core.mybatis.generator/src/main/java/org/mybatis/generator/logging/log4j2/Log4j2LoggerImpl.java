package org.mybatis.generator.logging.log4j2;

import org.mybatis.generator.logging.*;
import org.apache.logging.log4j.*;

public class Log4j2LoggerImpl implements Log
{
    private static Marker MARKER;
    private Logger log;
    
    public Log4j2LoggerImpl(final Logger logger) {
        this.log = logger;
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        this.log.error(Log4j2LoggerImpl.MARKER, s, e);
    }
    
    @Override
    public void error(final String s) {
        this.log.error(Log4j2LoggerImpl.MARKER, s);
    }
    
    @Override
    public void debug(final String s) {
        this.log.debug(Log4j2LoggerImpl.MARKER, s);
    }
    
    @Override
    public void warn(final String s) {
        this.log.warn(Log4j2LoggerImpl.MARKER, s);
    }
    
    static {
        Log4j2LoggerImpl.MARKER = MarkerManager.getMarker(LogFactory.MARKER);
    }
}
