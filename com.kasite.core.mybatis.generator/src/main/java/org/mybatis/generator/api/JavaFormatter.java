package org.mybatis.generator.api;

import org.mybatis.generator.config.*;
import org.mybatis.generator.api.dom.java.*;

public interface JavaFormatter
{
    void setContext(final Context p0);
    
    String getFormattedContent(final CompilationUnit p0);
}
