package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class BasicSelectOneMethodGenerator extends AbstractMethodGenerator
{
    private FullyQualifiedJavaType recordType;
    private String resultMapId;
    private FragmentGenerator fragmentGenerator;
    
    private BasicSelectOneMethodGenerator(final Builder builder) {
        super(builder);
        this.recordType = builder.recordType;
        this.resultMapId = builder.resultMapId;
        this.fragmentGenerator = builder.fragmentGenerator;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        final boolean reuseResultMap = this.introspectedTable.getRules().generateSelectByExampleWithBLOBs() || this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs();
        final FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.render.SelectStatementProvider");
        final FullyQualifiedJavaType adapter = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter");
        final FullyQualifiedJavaType annotation = new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider");
        imports.add(parameterType);
        imports.add(adapter);
        imports.add(annotation);
        final Method method = new Method("selectOne");
        imports.add(this.recordType);
        method.setReturnType(this.recordType);
        method.addParameter(new Parameter(parameterType, "selectStatement"));
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        method.addAnnotation("@SelectProvider(type=SqlProviderAdapter.class, method=\"select\")");
        final MethodAndImports.Builder builder = MethodAndImports.withMethod(method).withImports(imports);
        if (this.introspectedTable.isConstructorBased()) {
            final MethodParts methodParts = this.fragmentGenerator.getAnnotatedConstructorArgs();
            this.acceptParts(builder, method, methodParts);
        }
        else if (reuseResultMap) {
            final FullyQualifiedJavaType rmAnnotation = new FullyQualifiedJavaType("org.apache.ibatis.annotations.ResultMap");
            builder.withImport(rmAnnotation);
            method.addAnnotation("@ResultMap(\"" + this.resultMapId + "\")");
        }
        else {
            final MethodParts methodParts = this.fragmentGenerator.getAnnotatedResults();
            this.acceptParts(builder, method, methodParts);
        }
        return builder.build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientBasicSelectOneMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, BasicSelectOneMethodGenerator>
    {
        private FullyQualifiedJavaType recordType;
        private String resultMapId;
        private FragmentGenerator fragmentGenerator;
        
        public Builder withRecordType(final FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }
        
        public Builder withResultMapId(final String resultMapId) {
            this.resultMapId = resultMapId;
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
        public BasicSelectOneMethodGenerator build() {
            return new BasicSelectOneMethodGenerator(this);
        }
    }
}
