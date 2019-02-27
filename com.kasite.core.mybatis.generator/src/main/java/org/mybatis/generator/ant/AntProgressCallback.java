package org.mybatis.generator.ant;

import org.mybatis.generator.internal.*;
import org.apache.tools.ant.*;

public class AntProgressCallback extends NullProgressCallback
{
    private Task task;
    private boolean verbose;
    
    public AntProgressCallback(final Task task, final boolean verbose) {
        this.task = task;
        this.verbose = verbose;
    }
    
    @Override
    public void startTask(final String subTaskName) {
        if (this.verbose) {
            this.task.log(subTaskName, 3);
        }
    }
}
