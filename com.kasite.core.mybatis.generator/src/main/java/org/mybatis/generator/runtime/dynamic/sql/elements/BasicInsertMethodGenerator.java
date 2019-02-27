package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.api.dom.java.*;

public class BasicInsertMethodGenerator extends AbstractMethodGenerator
{
    private FullyQualifiedJavaType recordType;
    private FragmentGenerator fragmentGenerator;
    
    private BasicInsertMethodGenerator(final Builder builder) {
        super(builder);
        this.recordType = builder.recordType;
        this.fragmentGenerator = builder.fragmentGenerator;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateInsert() && !this.introspectedTable.getRules().generateInsertSelective()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        final FullyQualifiedJavaType adapter = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter");
        final FullyQualifiedJavaType annotation = new FullyQualifiedJavaType("org.apache.ibatis.annotations.InsertProvider");
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.insert.render.InsertStatementProvider"));
        imports.add(adapter);
        imports.add(annotation);
        final FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.insert.render.InsertStatementProvider");
        imports.add(this.recordType);
        parameterType.addTypeArgument(this.recordType);
        final Method method = new Method("insert");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(parameterType, "insertStatement"));
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        method.addAnnotation("@InsertProvider(type=SqlProviderAdapter.class, method=\"insert\")");
        final MethodAndImports.Builder builder = MethodAndImports.withMethod(method).withImports(imports);
        final GeneratedKey gk = this.introspectedTable.getGeneratedKey();
        if (gk != null) {
            final MethodParts methodParts = this.fragmentGenerator.getGeneratedKeyAnnotation(gk);
            this.acceptParts(builder, method, methodParts);
        }
        return builder.build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientBasicInsertMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, BasicInsertMethodGenerator>
    {
        private FullyQualifiedJavaType recordType;
        private FragmentGenerator fragmentGenerator;
        
        public Builder withRecordType(final FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
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
        public BasicInsertMethodGenerator build() {
            return new BasicInsertMethodGenerator(this);
        }
    }
}
