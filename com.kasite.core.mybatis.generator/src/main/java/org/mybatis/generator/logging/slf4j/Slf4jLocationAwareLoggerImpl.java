package org.mybatis.generator.logging.slf4j;

import org.slf4j.spi.*;
import org.mybatis.generator.logging.*;
import org.slf4j.*;

class Slf4jLocationAwareLoggerImpl implements Log
{
    private static Marker MARKER;
    private static final String FQCN;
    private LocationAwareLogger logger;
    
    Slf4jLocationAwareLoggerImpl(final LocationAwareLogger logger) {
        this.logger = logger;
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        this.logger.log(Slf4jLocationAwareLoggerImpl.MARKER, Slf4jLocationAwareLoggerImpl.FQCN, 40, s, (Object[])null, e);
    }
    
    @Override
    public void error(final String s) {
        this.logger.log(Slf4jLocationAwareLoggerImpl.MARKER, Slf4jLocationAwareLoggerImpl.FQCN, 40, s, (Object[])null, (Throwable)null);
    }
    
    @Override
    public void debug(final String s) {
        this.logger.log(Slf4jLocationAwareLoggerImpl.MARKER, Slf4jLocationAwareLoggerImpl.FQCN, 10, s, (Object[])null, (Throwable)null);
    }
    
    @Override
    public void warn(final String s) {
        this.logger.log(Slf4jLocationAwareLoggerImpl.MARKER, Slf4jLocationAwareLoggerImpl.FQCN, 30, s, (Object[])null, (Throwable)null);
    }
    
    static {
        Slf4jLocationAwareLoggerImpl.MARKER = MarkerFactory.getMarker(LogFactory.MARKER);
        FQCN = Slf4jImpl.class.getName();
    }
}
