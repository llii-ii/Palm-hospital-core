package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class BasicSelectManyMethodGenerator extends AbstractMethodGenerator
{
    private FullyQualifiedJavaType recordType;
    private FragmentGenerator fragmentGenerator;
    
    private BasicSelectManyMethodGenerator(final Builder builder) {
        super(builder);
        this.recordType = builder.recordType;
        this.fragmentGenerator = builder.fragmentGenerator;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateSelectByExampleWithBLOBs() && !this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        final FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.render.SelectStatementProvider");
        final FullyQualifiedJavaType adapter = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter");
        final FullyQualifiedJavaType annotation = new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider");
        imports.add(parameterType);
        imports.add(adapter);
        imports.add(annotation);
        imports.add(FullyQualifiedJavaType.getNewListInstance());
        imports.add(this.recordType);
        final FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(this.recordType);
        final Method method = new Method("selectMany");
        method.setReturnType(returnType);
        method.addParameter(new Parameter(parameterType, "selectStatement"));
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        method.addAnnotation("@SelectProvider(type=SqlProviderAdapter.class, method=\"select\")");
        final MethodAndImports.Builder builder = MethodAndImports.withMethod(method).withImports(imports);
        MethodParts methodParts;
        if (this.introspectedTable.isConstructorBased()) {
            methodParts = this.fragmentGenerator.getAnnotatedConstructorArgs();
        }
        else {
            methodParts = this.fragmentGenerator.getAnnotatedResults();
        }
        this.acceptParts(builder, method, methodParts);
        return builder.build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientBasicSelectManyMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, BasicSelectManyMethodGenerator>
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
        public BasicSelectManyMethodGenerator build() {
            return new BasicSelectManyMethodGenerator(this);
        }
    }
}
