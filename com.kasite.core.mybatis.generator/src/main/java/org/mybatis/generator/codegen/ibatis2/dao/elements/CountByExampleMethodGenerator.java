package org.mybatis.generator.codegen.ibatis2.dao.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class CountByExampleMethodGenerator extends AbstractDAOElementGenerator
{
    private boolean generateForJava5;
    
    public CountByExampleMethodGenerator(final boolean generateForJava5) {
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
        sb.append("Long count = (Long)  ");
        sb.append(this.daoTemplate.getQueryForObjectMethod(this.introspectedTable.getIbatis2SqlMapNamespace(), this.introspectedTable.getCountByExampleStatementId(), "example"));
        method.addBodyLine(sb.toString());
        if (this.generateForJava5) {
            method.addBodyLine("return count;");
        }
        else {
            method.addBodyLine("return count.longValue();");
        }
        if (this.context.getPlugins().clientCountByExampleMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
    
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        if (this.getExampleMethodVisibility() == JavaVisibility.PUBLIC) {
            final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
            final Method method = this.getMethodShell(importedTypes);
            if (this.context.getPlugins().clientCountByExampleMethodGenerated(method, interfaze, this.introspectedTable)) {
                interfaze.addImportedTypes(importedTypes);
                interfaze.addMethod(method);
            }
        }
    }
    
    private Method getMethodShell(final Set<FullyQualifiedJavaType> importedTypes) {
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getExampleType());
        importedTypes.add(type);
        final Method method = new Method();
        method.setVisibility(this.getExampleMethodVisibility());
        method.setReturnType(new FullyQualifiedJavaType("long"));
        method.setName(this.getDAOMethodNameCalculator().getCountByExampleMethodName(this.introspectedTable));
        method.addParameter(new Parameter(type, "example"));
        for (final FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
            method.addException(fqjt);
            importedTypes.add(fqjt);
        }
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        return method;
    }
}
