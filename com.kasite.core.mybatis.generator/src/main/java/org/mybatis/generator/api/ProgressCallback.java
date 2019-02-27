package org.mybatis.generator.api;

public interface ProgressCallback
{
    void introspectionStarted(final int p0);
    
    void generationStarted(final int p0);
    
    void saveStarted(final int p0);
    
    void startTask(final String p0);
    
    void done();
    
    void checkCancel() throws InterruptedException;
}
