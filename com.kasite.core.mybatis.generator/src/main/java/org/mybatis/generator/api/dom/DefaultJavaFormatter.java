package org.mybatis.generator.api.dom;

import org.mybatis.generator.api.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.api.dom.java.*;

public class DefaultJavaFormatter implements JavaFormatter
{
    protected Context context;
    
    @Override
    public String getFormattedContent(final CompilationUnit compilationUnit) {
        return compilationUnit.getFormattedContent();
    }
    
    @Override
    public void setContext(final Context context) {
        this.context = context;
    }
}
