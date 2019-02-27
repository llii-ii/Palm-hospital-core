package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class SelectByPrimaryKeyMethodGenerator extends AbstractMethodGenerator
{
    private FullyQualifiedJavaType recordType;
    private String tableFieldName;
    private FragmentGenerator fragmentGenerator;
    
    private SelectByPrimaryKeyMethodGenerator(final Builder builder) {
        super(builder);
        this.recordType = builder.recordType;
        this.tableFieldName = builder.tableFieldName;
        this.fragmentGenerator = builder.fragmentGenerator;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectDSL"));
        imports.add(this.recordType);
        final Method method = new Method("selectByPrimaryKey");
        method.setDefault(true);
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        method.setReturnType(this.recordType);
        final StringBuilder sb = new StringBuilder();
        sb.append("return SelectDSL.selectWithMapper(this::selectOne, ");
        sb.append(this.fragmentGenerator.getSelectList());
        sb.append(')');
        method.addBodyLine(sb.toString());
        method.addBodyLine("        .from(" + this.tableFieldName + ")");
        final MethodAndImports.Builder builder = MethodAndImports.withMethod(method).withStaticImport("org.mybatis.dynamic.sql.SqlBuilder.*").withImports(imports);
        final MethodParts methodParts = this.fragmentGenerator.getPrimaryKeyWhereClauseAndParameters();
        this.acceptParts(builder, method, methodParts);
        method.addBodyLine("        .build()");
        method.addBodyLine("        .execute();");
        return builder.build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, SelectByPrimaryKeyMethodGenerator>
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
        public SelectByPrimaryKeyMethodGenerator build() {
            return new SelectByPrimaryKeyMethodGenerator(this);
        }
    }
}
