package org.mybatis.generator.api;

import org.mybatis.generator.config.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public interface JavaTypeResolver
{
    void addConfigurationProperties(final Properties p0);
    
    void setContext(final Context p0);
    
    void setWarnings(final List<String> p0);
    
    FullyQualifiedJavaType calculateJavaType(final IntrospectedColumn p0);
    
    String calculateJdbcTypeName(final IntrospectedColumn p0);
}
