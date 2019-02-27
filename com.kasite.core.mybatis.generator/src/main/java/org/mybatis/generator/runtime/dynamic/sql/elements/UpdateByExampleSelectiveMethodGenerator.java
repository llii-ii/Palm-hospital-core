package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class UpdateByExampleSelectiveMethodGenerator extends AbstractMethodGenerator
{
    private FullyQualifiedJavaType recordType;
    private String tableFieldName;
    private FragmentGenerator fragmentGenerator;
    
    private UpdateByExampleSelectiveMethodGenerator(final Builder builder) {
        super(builder);
        this.recordType = builder.recordType;
        this.tableFieldName = builder.tableFieldName;
        this.fragmentGenerator = builder.fragmentGenerator;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.update.UpdateDSL"));
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.update.MyBatis3UpdateModelAdapter"));
        imports.add(this.recordType);
        final Method method = new Method("updateByExampleSelective");
        method.setDefault(true);
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        final FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("UpdateDSL<MyBatis3UpdateModelAdapter<Integer>>");
        method.setReturnType(returnType);
        method.addParameter(new Parameter(this.recordType, "record"));
        method.addBodyLine("return UpdateDSL.updateWithMapper(this::update, " + this.tableFieldName + ")");
        method.addBodyLines(this.fragmentGenerator.getSetEqualWhenPresentLines(this.introspectedTable.getAllColumns(), true));
        return MethodAndImports.withMethod(method).withImports(imports).build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientUpdateByExampleSelectiveMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, UpdateByExampleSelectiveMethodGenerator>
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
        public UpdateByExampleSelectiveMethodGenerator build() {
            return new UpdateByExampleSelectiveMethodGenerator(this);
        }
    }
}
