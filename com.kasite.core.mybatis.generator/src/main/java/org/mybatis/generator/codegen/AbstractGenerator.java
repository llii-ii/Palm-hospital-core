package org.mybatis.generator.codegen;

import org.mybatis.generator.config.*;
import java.util.*;
import org.mybatis.generator.api.*;

public abstract class AbstractGenerator
{
    protected Context context;
    protected IntrospectedTable introspectedTable;
    protected List<String> warnings;
    protected ProgressCallback progressCallback;
    
    public Context getContext() {
        return this.context;
    }
    
    public void setContext(final Context context) {
        this.context = context;
    }
    
    public IntrospectedTable getIntrospectedTable() {
        return this.introspectedTable;
    }
    
    public void setIntrospectedTable(final IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }
    
    public List<String> getWarnings() {
        return this.warnings;
    }
    
    public void setWarnings(final List<String> warnings) {
        this.warnings = warnings;
    }
    
    public ProgressCallback getProgressCallback() {
        return this.progressCallback;
    }
    
    public void setProgressCallback(final ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }
}
