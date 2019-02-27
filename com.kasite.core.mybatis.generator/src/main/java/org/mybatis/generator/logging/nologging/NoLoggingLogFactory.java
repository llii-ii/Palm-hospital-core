package org.mybatis.generator.logging.nologging;

import org.mybatis.generator.logging.*;

public class NoLoggingLogFactory implements AbstractLogFactory
{
    @Override
    public Log getLog(final Class<?> clazz) {
        return new NoLoggingImpl(clazz);
    }
}
