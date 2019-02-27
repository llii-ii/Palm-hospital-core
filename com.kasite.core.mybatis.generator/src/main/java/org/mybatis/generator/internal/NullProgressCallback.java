package org.mybatis.generator.internal;

import org.mybatis.generator.api.*;

public class NullProgressCallback implements ProgressCallback
{
    @Override
    public void generationStarted(final int totalTasks) {
    }
    
    @Override
    public void introspectionStarted(final int totalTasks) {
    }
    
    @Override
    public void saveStarted(final int totalTasks) {
    }
    
    @Override
    public void startTask(final String taskName) {
    }
    
    @Override
    public void checkCancel() throws InterruptedException {
    }
    
    @Override
    public void done() {
    }
}
