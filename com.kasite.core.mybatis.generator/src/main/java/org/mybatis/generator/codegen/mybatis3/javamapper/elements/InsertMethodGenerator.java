package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class InsertMethodGenerator extends AbstractJavaMapperMethodGenerator
{
    private boolean isSimple;
    
    public InsertMethodGenerator(final boolean isSimple) {
        this.isSimple = isSimple;
    }
    
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        final Method method = new Method();
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(this.introspectedTable.getInsertStatementId());
        FullyQualifiedJavaType parameterType;
        if (this.isSimple) {
            parameterType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        }
        else {
            parameterType = this.introspectedTable.getRules().calculateAllFieldsClass();
        }
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(parameterType);
        method.addParameter(new Parameter(parameterType, "record"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        this.addMapperAnnotations(method);
        if (this.context.getPlugins().clientInsertMethodGenerated(method, interfaze, this.introspectedTable)) {
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
