package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class DeleteByPrimaryKeyMethodGenerator extends AbstractMethodGenerator
{
    private String tableFieldName;
    private FragmentGenerator fragmentGenerator;
    
    private DeleteByPrimaryKeyMethodGenerator(final Builder builder) {
        super(builder);
        this.tableFieldName = builder.tableFieldName;
        this.fragmentGenerator = builder.fragmentGenerator;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        final Set<String> staticImports = new HashSet<String>();
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.delete.DeleteDSL"));
        staticImports.add("org.mybatis.dynamic.sql.SqlBuilder.*");
        final Method method = new Method("deleteByPrimaryKey");
        method.setDefault(true);
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addBodyLine("return DeleteDSL.deleteFromWithMapper(this::delete, " + this.tableFieldName + ")");
        final MethodParts methodParts = this.fragmentGenerator.getPrimaryKeyWhereClauseAndParameters();
        for (final Parameter parameter : methodParts.getParameters()) {
            method.addParameter(parameter);
        }
        method.addBodyLines(methodParts.getBodyLines());
        imports.addAll(methodParts.getImports());
        method.addBodyLine("        .build()");
        method.addBodyLine("        .execute();");
        return MethodAndImports.withMethod(method).withImports(imports).withStaticImports(staticImports).build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, DeleteByPrimaryKeyMethodGenerator>
    {
        private String tableFieldName;
        private FragmentGenerator fragmentGenerator;
        
        public Builder withTableFieldName(final String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return this;
        }
        
        public Builder withFragmentGenerator(final FragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }
        
        @Override
        public DeleteByPrimaryKeyMethodGenerator build() {
            return new DeleteByPrimaryKeyMethodGenerator(this);
        }
    }
}
