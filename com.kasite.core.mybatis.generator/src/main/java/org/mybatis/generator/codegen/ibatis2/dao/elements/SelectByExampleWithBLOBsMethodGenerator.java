package org.mybatis.generator.codegen.ibatis2.dao.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class SelectByExampleWithBLOBsMethodGenerator extends AbstractDAOElementGenerator
{
    private boolean generateForJava5;
    
    public SelectByExampleWithBLOBsMethodGenerator(final boolean generateForJava5) {
        this.generateForJava5 = generateForJava5;
    }
    
    @Override
    public void addImplementationElements(final TopLevelClass topLevelClass) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        final Method method = this.getMethodShell(importedTypes);
        if (this.generateForJava5) {
            method.addAnnotation("@Override");
            method.addSuppressTypeWarningsAnnotation();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(method.getReturnType().getShortName());
        sb.append(" list = ");
        sb.append(this.daoTemplate.getQueryForListMethod(this.introspectedTable.getIbatis2SqlMapNamespace(), this.introspectedTable.getSelectByExampleWithBLOBsStatementId(), "example"));
        method.addBodyLine(sb.toString());
        method.addBodyLine("return list;");
        if (this.context.getPlugins().clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
    
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        if (this.getExampleMethodVisibility() == JavaVisibility.PUBLIC) {
            final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
            final Method method = this.getMethodShell(importedTypes);
            if (this.context.getPlugins().clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, this.introspectedTable)) {
                interfaze.addImportedTypes(importedTypes);
                interfaze.addMethod(method);
            }
        }
    }
    
    private Method getMethodShell(final Set<FullyQualifiedJavaType> importedTypes) {
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getExampleType());
        importedTypes.add(type);
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        final Method method = new Method();
        method.setVisibility(this.getExampleMethodVisibility());
        final FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        if (this.generateForJava5) {
            FullyQualifiedJavaType fqjt;
            if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
                fqjt = new FullyQualifiedJavaType(this.introspectedTable.getRecordWithBLOBsType());
            }
            else {
                fqjt = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
            }
            importedTypes.add(fqjt);
            returnType.addTypeArgument(fqjt);
        }
        method.setReturnType(returnType);
        method.setName(this.getDAOMethodNameCalculator().getSelectByExampleWithBLOBsMethodName(this.introspectedTable));
        method.addParameter(new Parameter(type, "example"));
        for (final FullyQualifiedJavaType fqjt2 : this.daoTemplate.getCheckedExceptions()) {
            method.addException(fqjt2);
            importedTypes.add(fqjt2);
        }
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        return method;
    }
}
