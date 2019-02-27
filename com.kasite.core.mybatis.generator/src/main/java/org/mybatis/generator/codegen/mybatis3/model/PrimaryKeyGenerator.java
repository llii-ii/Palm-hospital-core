package org.mybatis.generator.codegen.mybatis3.model;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.io.*;

public class PrimaryKeyGenerator extends AbstractJavaGenerator
{
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        final FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.7", table.toString()));
        final Plugin plugins = this.context.getPlugins();
        final CommentGenerator commentGenerator = this.context.getCommentGenerator();
        final TopLevelClass topLevelClass = new TopLevelClass(this.introspectedTable.getPrimaryKeyType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);
        final String rootClass = this.getRootClass();
        if (rootClass != null) {
            topLevelClass.setSuperClass(new FullyQualifiedJavaType(rootClass));
            topLevelClass.addImportedType(topLevelClass.getSuperClass());
        }
        if (this.introspectedTable.isConstructorBased()) {
            this.addParameterizedConstructor(topLevelClass);
            if (!this.introspectedTable.isImmutable()) {
                this.addDefaultConstructor(topLevelClass);
            }
        }
        commentGenerator.addModelClassComment(topLevelClass, this.introspectedTable);
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
            if (RootClassInfo.getInstance(rootClass, this.warnings).containsProperty(introspectedColumn)) {
                continue;
            }
            final Field field = JavaBeansUtil.getJavaBeansField(introspectedColumn, this.context, this.introspectedTable);
            if (plugins.modelFieldGenerated(field, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.PRIMARY_KEY)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }
            Method method = JavaBeansUtil.getJavaBeansGetter(introspectedColumn, this.context, this.introspectedTable);
            if (plugins.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.PRIMARY_KEY)) {
                topLevelClass.addMethod(method);
            }
            if (this.introspectedTable.isImmutable()) {
                continue;
            }
            method = JavaBeansUtil.getJavaBeansSetter(introspectedColumn, this.context, this.introspectedTable);
            if (!plugins.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.PRIMARY_KEY)) {
                continue;
            }
            topLevelClass.addMethod(method);
        }
        final List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (this.context.getPlugins().modelPrimaryKeyClassGenerated(topLevelClass, this.introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }
    
    private void addParameterizedConstructor(final TopLevelClass topLevelClass) {
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        final StringBuilder sb = new StringBuilder();
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
            method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), introspectedColumn.getJavaProperty()));
            sb.setLength(0);
            sb.append("this.");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" = ");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(';');
            method.addBodyLine(sb.toString());
        }
        topLevelClass.addMethod(method);
    }
}
