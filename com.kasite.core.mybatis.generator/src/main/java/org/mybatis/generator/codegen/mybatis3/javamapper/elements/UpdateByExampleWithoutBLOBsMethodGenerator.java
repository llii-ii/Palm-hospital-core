package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class UpdateByExampleWithoutBLOBsMethodGenerator extends AbstractJavaMapperMethodGenerator
{
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(this.introspectedTable.getUpdateByExampleStatementId());
        FullyQualifiedJavaType parameterType;
        if (this.introspectedTable.getRules().generateBaseRecordClass()) {
            parameterType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        }
        else {
            parameterType = new FullyQualifiedJavaType(this.introspectedTable.getPrimaryKeyType());
        }
        method.addParameter(new Parameter(parameterType, "record", "@Param(\"record\")"));
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(parameterType);
        final FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(this.introspectedTable.getExampleType());
        method.addParameter(new Parameter(exampleType, "example", "@Param(\"example\")"));
        importedTypes.add(exampleType);
        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        this.addMapperAnnotations(method);
        if (this.context.getPlugins().clientUpdateByExampleWithoutBLOBsMethodGenerated(method, interfaze, this.introspectedTable)) {
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
