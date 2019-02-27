package org.mybatis.generator.logging.slf4j;

import org.mybatis.generator.logging.*;

public class Slf4jLoggingLogFactory implements AbstractLogFactory
{
    @Override
    public Log getLog(final Class<?> clazz) {
        return new Slf4jImpl(clazz);
    }
}
