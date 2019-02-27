package org.mybatis.generator.codegen.mybatis3.model;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.io.*;

public class SimpleModelGenerator extends AbstractJavaGenerator
{
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        final FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.8", table.toString()));
        final Plugin plugins = this.context.getPlugins();
        final CommentGenerator commentGenerator = this.context.getCommentGenerator();
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        final TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);
        final FullyQualifiedJavaType superClass = this.getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }
        commentGenerator.addModelClassComment(topLevelClass, this.introspectedTable);
        final List<IntrospectedColumn> introspectedColumns = this.introspectedTable.getAllColumns();
        if (this.introspectedTable.isConstructorBased()) {
            this.addParameterizedConstructor(topLevelClass);
            if (!this.introspectedTable.isImmutable()) {
                this.addDefaultConstructor(topLevelClass);
            }
        }
        final String rootClass = this.getRootClass();
        for (final IntrospectedColumn introspectedColumn : introspectedColumns) {
            if (RootClassInfo.getInstance(rootClass, this.warnings).containsProperty(introspectedColumn)) {
                continue;
            }
            final Field field = JavaBeansUtil.getJavaBeansField(introspectedColumn, this.context, this.introspectedTable);
            if (plugins.modelFieldGenerated(field, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }
            Method method = JavaBeansUtil.getJavaBeansGetter(introspectedColumn, this.context, this.introspectedTable);
            if (plugins.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addMethod(method);
            }
            if (this.introspectedTable.isImmutable()) {
                continue;
            }
            method = JavaBeansUtil.getJavaBeansSetter(introspectedColumn, this.context, this.introspectedTable);
            if (!plugins.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                continue;
            }
            topLevelClass.addMethod(method);
        }
        final List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (this.context.getPlugins().modelBaseRecordClassGenerated(topLevelClass, this.introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }
    
    private FullyQualifiedJavaType getSuperClass() {
        final String rootClass = this.getRootClass();
        FullyQualifiedJavaType superClass;
        if (rootClass != null) {
            superClass = new FullyQualifiedJavaType(rootClass);
        }
        else {
            superClass = null;
        }
        return superClass;
    }
    
    private void addParameterizedConstructor(final TopLevelClass topLevelClass) {
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        final List<IntrospectedColumn> constructorColumns = this.introspectedTable.getAllColumns();
        for (final IntrospectedColumn introspectedColumn : constructorColumns) {
            method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), introspectedColumn.getJavaProperty()));
        }
        final StringBuilder sb = new StringBuilder();
        final List<IntrospectedColumn> introspectedColumns = this.introspectedTable.getAllColumns();
        for (final IntrospectedColumn introspectedColumn2 : introspectedColumns) {
            sb.setLength(0);
            sb.append("this.");
            sb.append(introspectedColumn2.getJavaProperty());
            sb.append(" = ");
            sb.append(introspectedColumn2.getJavaProperty());
            sb.append(';');
            method.addBodyLine(sb.toString());
        }
        topLevelClass.addMethod(method);
    }
}
