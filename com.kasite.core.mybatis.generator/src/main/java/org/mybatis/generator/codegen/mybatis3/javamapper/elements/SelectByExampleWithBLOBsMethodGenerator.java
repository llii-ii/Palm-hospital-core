package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class SelectByExampleWithBLOBsMethodGenerator extends AbstractJavaMapperMethodGenerator
{
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getExampleType());
        importedTypes.add(type);
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        final FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType;
        if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            listType = new FullyQualifiedJavaType(this.introspectedTable.getRecordWithBLOBsType());
        }
        else {
            listType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        }
        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);
        method.setName(this.introspectedTable.getSelectByExampleWithBLOBsStatementId());
        method.addParameter(new Parameter(type, "example"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        this.addMapperAnnotations(interfaze, method);
        if (this.context.getPlugins().clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, this.introspectedTable)) {
            this.addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }
    
    public void addMapperAnnotations(final Interface interfaze, final Method method) {
    }
    
    public void addExtraImports(final Interface interfaze) {
    }
}
