package org.mybatis.generator.codegen.ibatis2.model;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class BaseRecordGenerator extends AbstractJavaGenerator
{
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        final FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.8", table.toString()));
        final Plugin plugins = this.context.getPlugins();
        final CommentGenerator commentGenerator = this.context.getCommentGenerator();
        final TopLevelClass topLevelClass = new TopLevelClass(this.introspectedTable.getBaseRecordType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);
        final FullyQualifiedJavaType superClass = this.getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }
        List<IntrospectedColumn> introspectedColumns;
        if (this.includePrimaryKeyColumns()) {
            if (this.includeBLOBColumns()) {
                introspectedColumns = this.introspectedTable.getAllColumns();
            }
            else {
                introspectedColumns = this.introspectedTable.getNonBLOBColumns();
            }
        }
        else if (this.includeBLOBColumns()) {
            introspectedColumns = this.introspectedTable.getNonPrimaryKeyColumns();
        }
        else {
            introspectedColumns = this.introspectedTable.getBaseColumns();
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
        FullyQualifiedJavaType superClass;
        if (this.introspectedTable.getRules().generatePrimaryKeyClass()) {
            superClass = new FullyQualifiedJavaType(this.introspectedTable.getPrimaryKeyType());
        }
        else {
            final String rootClass = this.getRootClass();
            if (rootClass != null) {
                superClass = new FullyQualifiedJavaType(rootClass);
            }
            else {
                superClass = null;
            }
        }
        return superClass;
    }
    
    private boolean includePrimaryKeyColumns() {
        return !this.introspectedTable.getRules().generatePrimaryKeyClass() && this.introspectedTable.hasPrimaryKeyColumns();
    }
    
    private boolean includeBLOBColumns() {
        return !this.introspectedTable.getRules().generateRecordWithBLOBsClass() && this.introspectedTable.hasBLOBColumns();
    }
}
