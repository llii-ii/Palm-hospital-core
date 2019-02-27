package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class CountByExampleMethodGenerator extends AbstractMethodGenerator
{
    private String tableFieldName;
    
    private CountByExampleMethodGenerator(final Builder builder) {
        super(builder);
        this.tableFieldName = builder.tableFieldName;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateCountByExample()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.QueryExpressionDSL"));
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter"));
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.SqlBuilder"));
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectDSL"));
        final Method method = new Method("countByExample");
        method.setDefault(true);
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        final FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>>");
        method.setReturnType(returnType);
        method.addBodyLine("return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())");
        method.addBodyLine("        .from(" + this.tableFieldName + ");");
        return MethodAndImports.withMethod(method).withImports(imports).build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientCountByExampleMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, CountByExampleMethodGenerator>
    {
        private String tableFieldName;
        
        public Builder withTableFieldName(final String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return this;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }
        
        @Override
        public CountByExampleMethodGenerator build() {
            return new CountByExampleMethodGenerator(this);
        }
    }
}
