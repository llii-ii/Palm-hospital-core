package org.mybatis.generator.api;

import org.mybatis.generator.internal.*;

public class VerboseProgressCallback extends NullProgressCallback
{
    @Override
    public void startTask(final String taskName) {
        System.out.println(taskName);
    }
}
