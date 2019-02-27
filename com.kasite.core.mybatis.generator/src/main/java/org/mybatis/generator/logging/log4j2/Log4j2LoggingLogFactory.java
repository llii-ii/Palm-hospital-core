package org.mybatis.generator.logging.log4j2;

import org.mybatis.generator.logging.*;

public class Log4j2LoggingLogFactory implements AbstractLogFactory
{
    @Override
    public Log getLog(final Class<?> clazz) {
        return new Log4j2Impl(clazz);
    }
}
