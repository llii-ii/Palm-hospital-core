package org.mybatis.generator.logging.commons;

import org.mybatis.generator.logging.*;

public class JakartaCommonsLoggingLogFactory implements AbstractLogFactory
{
    @Override
    public Log getLog(final Class<?> clazz) {
        return new JakartaCommonsLoggingImpl(clazz);
    }
}
