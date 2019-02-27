package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class SelectDistinctByExampleMethodGenerator extends AbstractMethodGenerator
{
    private FullyQualifiedJavaType recordType;
    private String tableFieldName;
    private FragmentGenerator fragmentGenerator;
    
    private SelectDistinctByExampleMethodGenerator(final Builder builder) {
        super(builder);
        this.recordType = builder.recordType;
        this.tableFieldName = builder.tableFieldName;
        this.fragmentGenerator = builder.fragmentGenerator;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateSelectByExampleWithBLOBs() && !this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.QueryExpressionDSL"));
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter"));
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectDSL"));
        imports.add(FullyQualifiedJavaType.getNewListInstance());
        imports.add(this.recordType);
        final Method method = new Method("selectDistinctByExample");
        method.setDefault(true);
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        final FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("QueryExpressionDSL<MyBatis3SelectModelAdapter<List<" + this.recordType.getShortNameWithoutTypeArguments() + ">>>");
        method.setReturnType(returnType);
        final StringBuilder sb = new StringBuilder();
        sb.append("return SelectDSL.selectDistinctWithMapper(this::selectMany, ");
        sb.append(this.fragmentGenerator.getSelectList());
        sb.append(')');
        method.addBodyLine(sb.toString());
        method.addBodyLine("        .from(" + this.tableFieldName + ");");
        return MethodAndImports.withMethod(method).withImports(imports).build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, SelectDistinctByExampleMethodGenerator>
    {
        private FullyQualifiedJavaType recordType;
        private String tableFieldName;
        private FragmentGenerator fragmentGenerator;
        
        public Builder withRecordType(final FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }
        
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
        public SelectDistinctByExampleMethodGenerator build() {
            return new SelectDistinctByExampleMethodGenerator(this);
        }
    }
}
