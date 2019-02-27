package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class SelectByExampleWithoutBLOBsMethodGenerator extends AbstractJavaMapperMethodGenerator
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
        if (this.introspectedTable.getRules().generateBaseRecordClass()) {
            listType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        }
        else {
            if (!this.introspectedTable.getRules().generatePrimaryKeyClass()) {
                throw new RuntimeException(Messages.getString("RuntimeError.12"));
            }
            listType = new FullyQualifiedJavaType(this.introspectedTable.getPrimaryKeyType());
        }
        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);
        method.setName(this.introspectedTable.getSelectByExampleStatementId());
        method.addParameter(new Parameter(type, "example"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        this.addMapperAnnotations(interfaze, method);
        if (this.context.getPlugins().clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, this.introspectedTable)) {
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
