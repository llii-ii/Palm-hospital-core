package org.mybatis.generator.codegen.ibatis2.dao.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class DeleteByExampleMethodGenerator extends AbstractDAOElementGenerator
{
    private boolean generateForJava5;
    
    public DeleteByExampleMethodGenerator(final boolean generateForJava5) {
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
        sb.append("int rows = ");
        sb.append(this.daoTemplate.getDeleteMethod(this.introspectedTable.getIbatis2SqlMapNamespace(), this.introspectedTable.getDeleteByExampleStatementId(), "example"));
        method.addBodyLine(sb.toString());
        method.addBodyLine("return rows;");
        if (this.context.getPlugins().clientDeleteByExampleMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
    
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        if (this.getExampleMethodVisibility() == JavaVisibility.PUBLIC) {
            final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
            final Method method = this.getMethodShell(importedTypes);
            if (this.context.getPlugins().clientDeleteByExampleMethodGenerated(method, interfaze, this.introspectedTable)) {
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
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(this.getDAOMethodNameCalculator().getDeleteByExampleMethodName(this.introspectedTable));
        method.addParameter(new Parameter(type, "example"));
        for (final FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
            method.addException(fqjt);
            importedTypes.add(fqjt);
        }
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        return method;
    }
}
