package org.mybatis.generator.logging.jdk14;

import org.mybatis.generator.logging.*;

public class Jdk14LoggingLogFactory implements AbstractLogFactory
{
    @Override
    public Log getLog(final Class<?> clazz) {
        return new Jdk14LoggingImpl(clazz);
    }
}
