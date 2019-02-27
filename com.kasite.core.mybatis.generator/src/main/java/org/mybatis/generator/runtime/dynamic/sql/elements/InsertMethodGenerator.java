package org.mybatis.generator.runtime.dynamic.sql.elements;

import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.api.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class InsertMethodGenerator extends AbstractMethodGenerator
{
    private String tableFieldName;
    private FullyQualifiedJavaType recordType;
    
    private InsertMethodGenerator(final Builder builder) {
        super(builder);
        this.tableFieldName = builder.tableFieldName;
        this.recordType = builder.recordType;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateInsert()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.SqlBuilder"));
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.render.RenderingStrategy"));
        imports.add(this.recordType);
        final Method method = new Method("insert");
        method.setDefault(true);
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(this.recordType, "record"));
        method.addBodyLine("return insert(SqlBuilder.insert(record)");
        method.addBodyLine("        .into(" + this.tableFieldName + ")");
        final List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns());
        for (final IntrospectedColumn column : columns) {
            method.addBodyLine("        .map(" + column.getJavaProperty() + ").toProperty(\"" + column.getJavaProperty() + "\")");
        }
        method.addBodyLine("        .build()");
        method.addBodyLine("        .render(RenderingStrategy.MYBATIS3));");
        return MethodAndImports.withMethod(method).withImports(imports).build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientInsertMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, InsertMethodGenerator>
    {
        private String tableFieldName;
        private FullyQualifiedJavaType recordType;
        
        public Builder withTableFieldName(final String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return this;
        }
        
        public Builder withRecordType(final FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }
        
        @Override
        public InsertMethodGenerator build() {
            return new InsertMethodGenerator(this);
        }
    }
}
