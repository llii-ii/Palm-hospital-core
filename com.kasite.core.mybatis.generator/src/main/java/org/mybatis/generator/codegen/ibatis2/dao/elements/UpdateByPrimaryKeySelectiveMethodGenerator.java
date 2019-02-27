package org.mybatis.generator.codegen.ibatis2.dao.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class UpdateByPrimaryKeySelectiveMethodGenerator extends AbstractDAOElementGenerator
{
    private boolean generateForJava5;
    
    public UpdateByPrimaryKeySelectiveMethodGenerator(final boolean generateForJava5) {
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
        sb.append(this.daoTemplate.getUpdateMethod(this.introspectedTable.getIbatis2SqlMapNamespace(), this.introspectedTable.getUpdateByPrimaryKeySelectiveStatementId(), "record"));
        method.addBodyLine(sb.toString());
        method.addBodyLine("return rows;");
        if (this.context.getPlugins().clientUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
    
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        final Method method = this.getMethodShell(importedTypes);
        if (this.context.getPlugins().clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, this.introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }
    
    private Method getMethodShell(final Set<FullyQualifiedJavaType> importedTypes) {
        FullyQualifiedJavaType parameterType;
        if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(this.introspectedTable.getRecordWithBLOBsType());
        }
        else {
            parameterType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        }
        importedTypes.add(parameterType);
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(this.getDAOMethodNameCalculator().getUpdateByPrimaryKeySelectiveMethodName(this.introspectedTable));
        method.addParameter(new Parameter(parameterType, "record"));
        for (final FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
            method.addException(fqjt);
            importedTypes.add(fqjt);
        }
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        return method;
    }
}
