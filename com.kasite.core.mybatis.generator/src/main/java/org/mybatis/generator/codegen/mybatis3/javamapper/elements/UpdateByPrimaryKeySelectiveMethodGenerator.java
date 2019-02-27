package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class UpdateByPrimaryKeySelectiveMethodGenerator extends AbstractJavaMapperMethodGenerator
{
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
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
        method.setName(this.introspectedTable.getUpdateByPrimaryKeySelectiveStatementId());
        method.addParameter(new Parameter(parameterType, "record"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        this.addMapperAnnotations(method);
        if (this.context.getPlugins().clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, this.introspectedTable)) {
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
