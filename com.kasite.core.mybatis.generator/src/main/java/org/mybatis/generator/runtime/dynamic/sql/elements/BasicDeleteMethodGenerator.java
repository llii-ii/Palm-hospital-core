package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class BasicDeleteMethodGenerator extends AbstractMethodGenerator
{
    private BasicDeleteMethodGenerator(final Builder builder) {
        super(builder);
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateDeleteByExample() && !this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        final FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider");
        final FullyQualifiedJavaType adapter = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter");
        final FullyQualifiedJavaType annotation = new FullyQualifiedJavaType("org.apache.ibatis.annotations.DeleteProvider");
        imports.add(parameterType);
        imports.add(adapter);
        imports.add(annotation);
        final Method method = new Method("delete");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(parameterType, "deleteStatement"));
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        method.addAnnotation("@DeleteProvider(type=SqlProviderAdapter.class, method=\"delete\")");
        return MethodAndImports.withMethod(method).withImports(imports).build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientBasicDeleteMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, BasicDeleteMethodGenerator>
    {
        @Override
        public Builder getThis() {
            return this;
        }
        
        @Override
        public BasicDeleteMethodGenerator build() {
            return new BasicDeleteMethodGenerator(this);
        }
    }
}
