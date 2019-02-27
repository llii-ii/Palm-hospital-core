package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;
import java.util.*;
import java.io.*;

public class SelectByPrimaryKeyMethodGenerator extends AbstractJavaMapperMethodGenerator
{
    private boolean isSimple;
    
    public SelectByPrimaryKeyMethodGenerator(final boolean isSimple) {
        this.isSimple = isSimple;
    }
    
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        final FullyQualifiedJavaType returnType = this.introspectedTable.getRules().calculateAllFieldsClass();
        method.setReturnType(returnType);
        importedTypes.add(returnType);
        method.setName(this.introspectedTable.getSelectByPrimaryKeyStatementId());
        if (!this.isSimple && this.introspectedTable.getRules().generatePrimaryKeyClass()) {
            final FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getPrimaryKeyType());
            importedTypes.add(type);
            method.addParameter(new Parameter(type, "key"));
        }
        else {
            final List<IntrospectedColumn> introspectedColumns = this.introspectedTable.getPrimaryKeyColumns();
            final boolean annotate = introspectedColumns.size() > 1;
            if (annotate) {
                importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
            }
            final StringBuilder sb = new StringBuilder();
            for (final IntrospectedColumn introspectedColumn : introspectedColumns) {
                final FullyQualifiedJavaType type2 = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type2);
                final Parameter parameter = new Parameter(type2, introspectedColumn.getJavaProperty());
                if (annotate) {
                    sb.setLength(0);
                    sb.append("@Param(\"");
                    sb.append(introspectedColumn.getJavaProperty());
                    sb.append("\")");
                    parameter.addAnnotation(sb.toString());
                }
                method.addParameter(parameter);
            }
        }
        this.addMapperAnnotations(interfaze, method);
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        if (this.context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, this.introspectedTable)) {
            this.addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }
    
    public void addMapperAnnotations(final Interface interfaze, final Method method) {
    }
    
    public void addExtraImports(final Interface interfaze) {
    }
}
