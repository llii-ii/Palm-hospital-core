package org.mybatis.generator.plugins;

import org.mybatis.generator.api.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class FluentBuilderMethodsPlugin extends PluginAdapter
{
    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean modelSetterMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedColumn introspectedColumn, final IntrospectedTable introspectedTable, final Plugin.ModelClassType modelClassType) {
        final Method fluentMethod = new Method();
        fluentMethod.setVisibility(JavaVisibility.PUBLIC);
        fluentMethod.setReturnType(topLevelClass.getType());
        fluentMethod.setName("with" + method.getName().substring(3));
        fluentMethod.getParameters().addAll(method.getParameters());
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3_DSQL) {
            this.context.getCommentGenerator().addGeneralMethodAnnotation(fluentMethod, introspectedTable, topLevelClass.getImportedTypes());
        }
        else {
            this.context.getCommentGenerator().addGeneralMethodComment(fluentMethod, introspectedTable);
        }
        final StringBuilder sb = new StringBuilder().append("this.").append(method.getName()).append('(').append(introspectedColumn.getJavaProperty()).append(");");
        fluentMethod.addBodyLine(sb.toString());
        fluentMethod.addBodyLine("return this;");
        topLevelClass.addMethod(fluentMethod);
        return super.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
