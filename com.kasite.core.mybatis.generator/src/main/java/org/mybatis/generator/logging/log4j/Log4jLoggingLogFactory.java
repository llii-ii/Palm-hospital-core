package org.mybatis.generator.logging.log4j;

import org.mybatis.generator.logging.*;

public class Log4jLoggingLogFactory implements AbstractLogFactory
{
    @Override
    public Log getLog(final Class<?> clazz) {
        return new Log4jImpl(clazz);
    }
}
