package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;
import java.util.*;
import java.io.*;

public class DeleteByPrimaryKeyMethodGenerator extends AbstractJavaMapperMethodGenerator
{
    private boolean isSimple;
    
    public DeleteByPrimaryKeyMethodGenerator(final boolean isSimple) {
        this.isSimple = isSimple;
    }
    
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(this.introspectedTable.getDeleteByPrimaryKeyStatementId());
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
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        this.addMapperAnnotations(method);
        if (this.context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, this.introspectedTable)) {
            this.addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }
    
    public void addMapperAnnotations(final Method method) {
    }
    
    public void addExtraImports(final Interface interfaze) {
    }
}
