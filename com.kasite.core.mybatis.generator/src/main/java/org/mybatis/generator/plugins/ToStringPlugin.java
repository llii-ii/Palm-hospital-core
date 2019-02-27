package org.mybatis.generator.plugins;

import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import java.io.*;

public class ToStringPlugin extends PluginAdapter
{
    private boolean useToStringFromRoot;
    
    @Override
    public void setProperties(final Properties properties) {
        super.setProperties(properties);
        this.useToStringFromRoot = StringUtility.isTrue(properties.getProperty("useToStringFromRoot"));
    }
    
    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean modelBaseRecordClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        this.generateToString(introspectedTable, topLevelClass);
        return true;
    }
    
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        this.generateToString(introspectedTable, topLevelClass);
        return true;
    }
    
    @Override
    public boolean modelPrimaryKeyClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        this.generateToString(introspectedTable, topLevelClass);
        return true;
    }
    
    private void generateToString(final IntrospectedTable introspectedTable, final TopLevelClass topLevelClass) {
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("toString");
        if (introspectedTable.isJava5Targeted()) {
            method.addAnnotation("@Override");
        }
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3_DSQL) {
            this.context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, topLevelClass.getImportedTypes());
        }
        else {
            this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        }
        method.addBodyLine("StringBuilder sb = new StringBuilder();");
        method.addBodyLine("sb.append(getClass().getSimpleName());");
        method.addBodyLine("sb.append(\" [\");");
        method.addBodyLine("sb.append(\"Hash = \").append(hashCode());");
        final StringBuilder sb = new StringBuilder();
        for (final Field field : topLevelClass.getFields()) {
            final String property = field.getName();
            sb.setLength(0);
            sb.append("sb.append(\"").append(", ").append(property).append("=\")").append(".append(").append(property).append(");");
            method.addBodyLine(sb.toString());
        }
        method.addBodyLine("sb.append(\"]\");");
        if (this.useToStringFromRoot && topLevelClass.getSuperClass() != null) {
            method.addBodyLine("sb.append(\", from super class \");");
            method.addBodyLine("sb.append(super.toString());");
        }
        method.addBodyLine("return sb.toString();");
        topLevelClass.addMethod(method);
    }
}
