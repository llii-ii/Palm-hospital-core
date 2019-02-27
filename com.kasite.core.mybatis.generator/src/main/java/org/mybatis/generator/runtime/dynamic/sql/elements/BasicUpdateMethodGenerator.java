package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class BasicUpdateMethodGenerator extends AbstractMethodGenerator
{
    private BasicUpdateMethodGenerator(final Builder builder) {
        super(builder);
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateUpdateByExampleSelective() && !this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs() && !this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs() && !this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective() && !this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs() && !this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        final FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.update.render.UpdateStatementProvider");
        final FullyQualifiedJavaType adapter = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter");
        final FullyQualifiedJavaType annotation = new FullyQualifiedJavaType("org.apache.ibatis.annotations.UpdateProvider");
        imports.add(parameterType);
        imports.add(adapter);
        imports.add(annotation);
        final Method method = new Method("update");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(parameterType, "updateStatement"));
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        method.addAnnotation("@UpdateProvider(type=SqlProviderAdapter.class, method=\"update\")");
        return MethodAndImports.withMethod(method).withImports(imports).build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientBasicUpdateMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, BasicUpdateMethodGenerator>
    {
        @Override
        public Builder getThis() {
            return this;
        }
        
        @Override
        public BasicUpdateMethodGenerator build() {
            return new BasicUpdateMethodGenerator(this);
        }
    }
}
