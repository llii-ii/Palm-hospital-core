package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class SelectAllMethodGenerator extends AbstractJavaMapperMethodGenerator
{
    @Override
    public void addInterfaceElements(final Interface interfaze) {
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        final FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        final FullyQualifiedJavaType listType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);
        method.setName(this.introspectedTable.getSelectAllStatementId());
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        this.addMapperAnnotations(interfaze, method);
        if (this.context.getPlugins().clientSelectAllMethodGenerated(method, interfaze, this.introspectedTable)) {
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
