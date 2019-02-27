package org.mybatis.generator.logging;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.logging.jdk14.*;
import org.mybatis.generator.logging.slf4j.*;
import org.mybatis.generator.logging.commons.*;
import org.mybatis.generator.logging.log4j.*;
import org.mybatis.generator.logging.log4j2.*;
import org.mybatis.generator.logging.nologging.*;

public class LogFactory
{
    private static AbstractLogFactory logFactory;
    public static String MARKER;
    
    public static Log getLog(final Class<?> clazz) {
        try {
            return LogFactory.logFactory.getLog(clazz);
        }
        catch (Throwable t) {
            throw new RuntimeException(Messages.getString("RuntimeError.21", clazz.getName(), t.getMessage()), t);
        }
    }
    
    public static synchronized void forceJavaLogging() {
        setImplementation(new Jdk14LoggingLogFactory());
    }
    
    public static synchronized void forceSlf4jLogging() {
        setImplementation(new Slf4jLoggingLogFactory());
    }
    
    public static synchronized void forceCommonsLogging() {
        setImplementation(new JakartaCommonsLoggingLogFactory());
    }
    
    public static synchronized void forceLog4jLogging() {
        setImplementation(new Log4jLoggingLogFactory());
    }
    
    public static synchronized void forceLog4j2Logging() {
        setImplementation(new Log4j2LoggingLogFactory());
    }
    
    public static synchronized void forceNoLogging() {
        setImplementation(new NoLoggingLogFactory());
    }
    
    public static void setLogFactory(final AbstractLogFactory logFactory) {
        setImplementation(logFactory);
    }
    
    private static void tryImplementation(final AbstractLogFactory factory) {
        if (LogFactory.logFactory == null) {
            try {
                setImplementation(factory);
            }
            catch (LogException ex) {}
        }
    }
    
    private static void setImplementation(final AbstractLogFactory factory) {
        try {
            final Log log = factory.getLog(LogFactory.class);
            if (log.isDebugEnabled()) {
                log.debug("Logging initialized using '" + factory + "' adapter.");
            }
            LogFactory.logFactory = factory;
        }
        catch (Throwable t) {
            throw new LogException("Error setting Log implementation.  Cause: " + t.getMessage(), t);
        }
    }
    
    static {
        LogFactory.MARKER = "MYBATIS-GENERATOR";
        tryImplementation(new Slf4jLoggingLogFactory());
        tryImplementation(new JakartaCommonsLoggingLogFactory());
        tryImplementation(new Log4j2LoggingLogFactory());
        tryImplementation(new Log4jLoggingLogFactory());
        tryImplementation(new Jdk14LoggingLogFactory());
        tryImplementation(new NoLoggingLogFactory());
    }
}
