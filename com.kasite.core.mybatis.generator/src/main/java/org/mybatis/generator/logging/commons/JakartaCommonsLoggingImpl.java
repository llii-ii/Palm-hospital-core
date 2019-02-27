package org.mybatis.generator.logging.commons;

import org.apache.commons.logging.LogFactory;
import org.mybatis.generator.logging.Log;

public class JakartaCommonsLoggingImpl implements Log
{
    private org.apache.commons.logging.Log log;
    
    public JakartaCommonsLoggingImpl(final Class<?> clazz) {
        this.log = LogFactory.getLog((Class)clazz);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        this.log.error((Object)s, e);
    }
    
    @Override
    public void error(final String s) {
        this.log.error((Object)s);
    }
    
    @Override
    public void debug(final String s) {
        this.log.debug((Object)s);
    }
    
    @Override
    public void warn(final String s) {
        this.log.warn((Object)s);
    }
}
