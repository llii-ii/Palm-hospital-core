package org.mybatis.generator.runtime.dynamic.sql;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.io.*;

public class DynamicSqlModelGenerator extends AbstractJavaGenerator
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
            this.addParameterizedConstructor(topLevelClass, commentGenerator);
            if (!this.introspectedTable.isImmutable()) {
                final Method method = this.getDefaultConstructor(topLevelClass);
                method.getJavaDocLines().clear();
                commentGenerator.addGeneralMethodAnnotation(method, this.introspectedTable, topLevelClass.getImportedTypes());
                topLevelClass.addMethod(method);
            }
        }
        final String rootClass = this.getRootClass();
        for (final IntrospectedColumn introspectedColumn : introspectedColumns) {
            if (RootClassInfo.getInstance(rootClass, this.warnings).containsProperty(introspectedColumn)) {
                continue;
            }
            final Field field = JavaBeansUtil.getJavaBeansField(introspectedColumn, this.context, this.introspectedTable);
            field.getJavaDocLines().clear();
            commentGenerator.addFieldAnnotation(field, this.introspectedTable, introspectedColumn, topLevelClass.getImportedTypes());
            if (plugins.modelFieldGenerated(field, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }
            Method method2 = JavaBeansUtil.getJavaBeansGetter(introspectedColumn, this.context, this.introspectedTable);
            method2.getJavaDocLines().clear();
            commentGenerator.addGeneralMethodAnnotation(method2, this.introspectedTable, introspectedColumn, topLevelClass.getImportedTypes());
            if (plugins.modelGetterMethodGenerated(method2, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addMethod(method2);
            }
            if (this.introspectedTable.isImmutable()) {
                continue;
            }
            method2 = JavaBeansUtil.getJavaBeansSetter(introspectedColumn, this.context, this.introspectedTable);
            method2.getJavaDocLines().clear();
            commentGenerator.addGeneralMethodAnnotation(method2, this.introspectedTable, introspectedColumn, topLevelClass.getImportedTypes());
            if (!plugins.modelSetterMethodGenerated(method2, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                continue;
            }
            topLevelClass.addMethod(method2);
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
    
    private void addParameterizedConstructor(final TopLevelClass topLevelClass, final CommentGenerator commentGenerator) {
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        method.getJavaDocLines().clear();
        commentGenerator.addGeneralMethodAnnotation(method, this.introspectedTable, topLevelClass.getImportedTypes());
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
