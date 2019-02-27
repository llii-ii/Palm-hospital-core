package org.mybatis.generator.codegen.mybatis3.model;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.io.*;

public class RecordWithBLOBsGenerator extends AbstractJavaGenerator
{
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        final FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.9", table.toString()));
        final Plugin plugins = this.context.getPlugins();
        final CommentGenerator commentGenerator = this.context.getCommentGenerator();
        final TopLevelClass topLevelClass = new TopLevelClass(this.introspectedTable.getRecordWithBLOBsType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);
        final String rootClass = this.getRootClass();
        if (this.introspectedTable.getRules().generateBaseRecordClass()) {
            topLevelClass.setSuperClass(this.introspectedTable.getBaseRecordType());
        }
        else {
            topLevelClass.setSuperClass(this.introspectedTable.getPrimaryKeyType());
        }
        commentGenerator.addModelClassComment(topLevelClass, this.introspectedTable);
        if (this.introspectedTable.isConstructorBased()) {
            this.addParameterizedConstructor(topLevelClass);
            if (!this.introspectedTable.isImmutable()) {
                this.addDefaultConstructor(topLevelClass);
            }
        }
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getBLOBColumns()) {
            if (RootClassInfo.getInstance(rootClass, this.warnings).containsProperty(introspectedColumn)) {
                continue;
            }
            final Field field = JavaBeansUtil.getJavaBeansField(introspectedColumn, this.context, this.introspectedTable);
            if (plugins.modelFieldGenerated(field, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.RECORD_WITH_BLOBS)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }
            Method method = JavaBeansUtil.getJavaBeansGetter(introspectedColumn, this.context, this.introspectedTable);
            if (plugins.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.RECORD_WITH_BLOBS)) {
                topLevelClass.addMethod(method);
            }
            if (this.introspectedTable.isImmutable()) {
                continue;
            }
            method = JavaBeansUtil.getJavaBeansSetter(introspectedColumn, this.context, this.introspectedTable);
            if (!plugins.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.RECORD_WITH_BLOBS)) {
                continue;
            }
            topLevelClass.addMethod(method);
        }
        final List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (this.context.getPlugins().modelRecordWithBLOBsClassGenerated(topLevelClass, this.introspectedTable)) {
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
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getAllColumns()) {
            method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), introspectedColumn.getJavaProperty()));
            topLevelClass.addImportedType(introspectedColumn.getFullyQualifiedJavaType());
        }
        boolean comma = false;
        final StringBuilder sb = new StringBuilder();
        sb.append("super(");
        for (final IntrospectedColumn introspectedColumn2 : this.introspectedTable.getNonBLOBColumns()) {
            if (comma) {
                sb.append(", ");
            }
            else {
                comma = true;
            }
            sb.append(introspectedColumn2.getJavaProperty());
        }
        sb.append(");");
        method.addBodyLine(sb.toString());
        for (final IntrospectedColumn introspectedColumn2 : this.introspectedTable.getBLOBColumns()) {
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
