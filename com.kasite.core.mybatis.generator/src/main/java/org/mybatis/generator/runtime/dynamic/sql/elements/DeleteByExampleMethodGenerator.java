package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class DeleteByExampleMethodGenerator extends AbstractMethodGenerator
{
    private String tableFieldName;
    
    private DeleteByExampleMethodGenerator(final Builder builder) {
        super(builder);
        this.tableFieldName = builder.tableFieldName;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateDeleteByExample()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.delete.DeleteDSL"));
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.delete.MyBatis3DeleteModelAdapter"));
        final Method method = new Method("deleteByExample");
        method.setDefault(true);
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        final FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("DeleteDSL<MyBatis3DeleteModelAdapter<Integer>>");
        method.setReturnType(returnType);
        method.addBodyLine("return DeleteDSL.deleteFromWithMapper(this::delete, " + this.tableFieldName + ");");
        return MethodAndImports.withMethod(method).withImports(imports).build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientDeleteByExampleMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, DeleteByExampleMethodGenerator>
    {
        private String tableFieldName;
        
        public Builder withTableFieldName(final String tableFIeldName) {
            this.tableFieldName = tableFIeldName;
            return this;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }
        
        @Override
        public DeleteByExampleMethodGenerator build() {
            return new DeleteByExampleMethodGenerator(this);
        }
    }
}
