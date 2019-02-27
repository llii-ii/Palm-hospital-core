package org.mybatis.generator.logging.log4j2;

import org.apache.logging.log4j.spi.*;
import org.apache.logging.log4j.message.*;
import org.mybatis.generator.logging.*;
import org.apache.logging.log4j.*;

public class Log4j2AbstractLoggerImpl implements Log
{
    private static Marker MARKER;
    private static final String FQCN;
    private ExtendedLoggerWrapper log;
    
    public Log4j2AbstractLoggerImpl(final AbstractLogger abstractLogger) {
        this.log = new ExtendedLoggerWrapper((ExtendedLogger)abstractLogger, abstractLogger.getName(), abstractLogger.getMessageFactory());
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        this.log.logIfEnabled(Log4j2AbstractLoggerImpl.FQCN, Level.ERROR, Log4j2AbstractLoggerImpl.MARKER, (Message)new SimpleMessage(s), e);
    }
    
    @Override
    public void error(final String s) {
        this.log.logIfEnabled(Log4j2AbstractLoggerImpl.FQCN, Level.ERROR, Log4j2AbstractLoggerImpl.MARKER, (Message)new SimpleMessage(s), (Throwable)null);
    }
    
    @Override
    public void debug(final String s) {
        this.log.logIfEnabled(Log4j2AbstractLoggerImpl.FQCN, Level.DEBUG, Log4j2AbstractLoggerImpl.MARKER, (Message)new SimpleMessage(s), (Throwable)null);
    }
    
    @Override
    public void warn(final String s) {
        this.log.logIfEnabled(Log4j2AbstractLoggerImpl.FQCN, Level.WARN, Log4j2AbstractLoggerImpl.MARKER, (Message)new SimpleMessage(s), (Throwable)null);
    }
    
    static {
        Log4j2AbstractLoggerImpl.MARKER = MarkerManager.getMarker(LogFactory.MARKER);
        FQCN = Log4j2Impl.class.getName();
    }
}
