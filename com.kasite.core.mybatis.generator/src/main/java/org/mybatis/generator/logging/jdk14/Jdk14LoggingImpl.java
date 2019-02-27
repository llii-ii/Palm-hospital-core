package org.mybatis.generator.logging.jdk14;

import org.mybatis.generator.logging.*;
import java.util.logging.*;

public class Jdk14LoggingImpl implements Log
{
    private Logger log;
    
    public Jdk14LoggingImpl(final Class<?> clazz) {
        this.log = Logger.getLogger(clazz.getName());
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.log.isLoggable(Level.FINE);
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        this.log.log(Level.SEVERE, s, e);
    }
    
    @Override
    public void error(final String s) {
        this.log.log(Level.SEVERE, s);
    }
    
    @Override
    public void debug(final String s) {
        this.log.log(Level.FINE, s);
    }
    
    @Override
    public void warn(final String s) {
        this.log.log(Level.WARNING, s);
    }
}
