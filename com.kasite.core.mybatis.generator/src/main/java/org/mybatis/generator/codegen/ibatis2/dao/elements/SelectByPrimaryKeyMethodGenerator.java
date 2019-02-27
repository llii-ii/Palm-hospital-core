package org.mybatis.generator.codegen.ibatis2.dao.elements;

import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.io.*;

public class SelectByPrimaryKeyMethodGenerator extends AbstractDAOElementGenerator
{
    private boolean generateForJava5;
    
    public SelectByPrimaryKeyMethodGenerator(final boolean generateForJava5) {
        this.generateForJava5 = generateForJava5;
    }
    
    @Override
    public void addImplementationElements(final TopLevelClass topLevelClass) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        final Method method = this.getMethodShell(importedTypes);
        if (this.generateForJava5) {
            method.addAnnotation("@Override");
        }
        final StringBuilder sb = new StringBuilder();
        if (!this.introspectedTable.getRules().generatePrimaryKeyClass()) {
            final FullyQualifiedJavaType keyType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
            topLevelClass.addImportedType(keyType);
            sb.setLength(0);
            sb.append(keyType.getShortName());
            sb.append(" _key = new ");
            sb.append(keyType.getShortName());
            sb.append("();");
            method.addBodyLine(sb.toString());
            for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
                sb.setLength(0);
                sb.append("_key.");
                sb.append(JavaBeansUtil.getSetterMethodName(introspectedColumn.getJavaProperty()));
                sb.append('(');
                sb.append(introspectedColumn.getJavaProperty());
                sb.append(");");
                method.addBodyLine(sb.toString());
            }
        }
        final FullyQualifiedJavaType returnType = method.getReturnType();
        sb.setLength(0);
        sb.append(returnType.getShortName());
        sb.append(" record = (");
        sb.append(returnType.getShortName());
        sb.append(") ");
        sb.append(this.daoTemplate.getQueryForObjectMethod(this.introspectedTable.getIbatis2SqlMapNamespace(), this.introspectedTable.getSelectByPrimaryKeyStatementId(), "_key"));
        method.addBodyLine(sb.toString());
        method.addBodyLine("return record;");
        if (this.context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
    
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        final Method method = this.getMethodShell(importedTypes);
        if (this.context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, this.introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }
    
    private Method getMethodShell(final Set<FullyQualifiedJavaType> importedTypes) {
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        final FullyQualifiedJavaType returnType = this.introspectedTable.getRules().calculateAllFieldsClass();
        method.setReturnType(returnType);
        importedTypes.add(returnType);
        method.setName(this.getDAOMethodNameCalculator().getSelectByPrimaryKeyMethodName(this.introspectedTable));
        if (this.introspectedTable.getRules().generatePrimaryKeyClass()) {
            final FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getPrimaryKeyType());
            importedTypes.add(type);
            method.addParameter(new Parameter(type, "_key"));
        }
        else {
            for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
                final FullyQualifiedJavaType type2 = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type2);
                method.addParameter(new Parameter(type2, introspectedColumn.getJavaProperty()));
            }
        }
        for (final FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
            method.addException(fqjt);
            importedTypes.add(fqjt);
        }
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        return method;
    }
}
