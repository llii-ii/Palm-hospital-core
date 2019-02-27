package org.mybatis.generator.codegen.ibatis2.model;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;

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
        if (this.introspectedTable.getRules().generateBaseRecordClass()) {
            topLevelClass.setSuperClass(this.introspectedTable.getBaseRecordType());
        }
        else {
            topLevelClass.setSuperClass(this.introspectedTable.getPrimaryKeyType());
        }
        final String rootClass = this.getRootClass();
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
}
