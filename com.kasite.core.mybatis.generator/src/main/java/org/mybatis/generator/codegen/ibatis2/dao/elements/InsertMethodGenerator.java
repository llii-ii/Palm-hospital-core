package org.mybatis.generator.codegen.ibatis2.dao.elements;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;
import java.util.*;
import java.io.*;

public class InsertMethodGenerator extends AbstractDAOElementGenerator
{
    private boolean generateForJava5;
    
    public InsertMethodGenerator(final boolean generateForJava5) {
        this.generateForJava5 = generateForJava5;
    }
    
    @Override
    public void addImplementationElements(final TopLevelClass topLevelClass) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        final Method method = this.getMethodShell(importedTypes);
        if (this.generateForJava5) {
            method.addAnnotation("@Override");
        }
        final FullyQualifiedJavaType returnType = method.getReturnType();
        final StringBuilder sb = new StringBuilder();
        if (returnType != null) {
            sb.append("Object newKey = ");
        }
        sb.append(this.daoTemplate.getInsertMethod(this.introspectedTable.getIbatis2SqlMapNamespace(), this.introspectedTable.getInsertStatementId(), "record"));
        method.addBodyLine(sb.toString());
        if (returnType != null) {
            if ("Object".equals(returnType.getShortName())) {
                method.addBodyLine("return newKey;");
            }
            else {
                sb.setLength(0);
                if (returnType.isPrimitive()) {
                    final PrimitiveTypeWrapper ptw = returnType.getPrimitiveTypeWrapper();
                    sb.append("return ((");
                    sb.append(ptw.getShortName());
                    sb.append(") newKey");
                    sb.append(").");
                    sb.append(ptw.getToPrimitiveMethod());
                    sb.append(';');
                }
                else {
                    sb.append("return (");
                    sb.append(returnType.getShortName());
                    sb.append(") newKey;");
                }
                method.addBodyLine(sb.toString());
            }
        }
        if (this.context.getPlugins().clientInsertMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
    
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        final Method method = this.getMethodShell(importedTypes);
        if (this.context.getPlugins().clientInsertMethodGenerated(method, interfaze, this.introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }
    
    private Method getMethodShell(final Set<FullyQualifiedJavaType> importedTypes) {
        final Method method = new Method();
        FullyQualifiedJavaType returnType;
        if (this.introspectedTable.getGeneratedKey() != null) {
            final IntrospectedColumn introspectedColumn = this.introspectedTable.getColumn(this.introspectedTable.getGeneratedKey().getColumn());
            if (introspectedColumn == null) {
                returnType = null;
            }
            else {
                returnType = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(returnType);
            }
        }
        else {
            returnType = null;
        }
        method.setReturnType(returnType);
        method.setVisibility(JavaVisibility.PUBLIC);
        final DAOMethodNameCalculator methodNameCalculator = this.getDAOMethodNameCalculator();
        method.setName(methodNameCalculator.getInsertMethodName(this.introspectedTable));
        final FullyQualifiedJavaType parameterType = this.introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(parameterType);
        method.addParameter(new Parameter(parameterType, "record"));
        for (final FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
            method.addException(fqjt);
            importedTypes.add(fqjt);
        }
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        return method;
    }
}
