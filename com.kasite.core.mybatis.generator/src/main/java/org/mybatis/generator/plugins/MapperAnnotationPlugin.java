package org.mybatis.generator.plugins;

import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;

public class MapperAnnotationPlugin extends PluginAdapter
{
    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean clientGenerated(final Interface interfaze, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
            interfaze.addAnnotation("@Mapper");
        }
        return true;
    }
}
