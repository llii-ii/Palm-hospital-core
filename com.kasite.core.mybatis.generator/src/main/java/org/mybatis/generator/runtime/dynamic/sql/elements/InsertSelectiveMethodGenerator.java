package org.mybatis.generator.runtime.dynamic.sql.elements;

import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public class InsertSelectiveMethodGenerator extends AbstractMethodGenerator
{
    private FullyQualifiedJavaType recordType;
    private String tableFieldName;
    
    private InsertSelectiveMethodGenerator(final Builder builder) {
        super(builder);
        this.recordType = builder.recordType;
        this.tableFieldName = builder.tableFieldName;
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!this.introspectedTable.getRules().generateInsertSelective()) {
            return null;
        }
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.SqlBuilder"));
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.render.RenderingStrategy"));
        imports.add(this.recordType);
        final Method method = new Method("insertSelective");
        method.setDefault(true);
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(this.recordType, "record"));
        method.addBodyLine("return insert(SqlBuilder.insert(record)");
        method.addBodyLine("        .into(" + this.tableFieldName + ")");
        final List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns());
        for (final IntrospectedColumn column : columns) {
            if (column.isSequenceColumn()) {
                method.addBodyLine("        .map(" + column.getJavaProperty() + ").toProperty(\"" + column.getJavaProperty() + "\")");
            }
            else {
                final String methodName = JavaBeansUtil.getGetterMethodName(column.getJavaProperty(), column.getFullyQualifiedJavaType());
                method.addBodyLine("        .map(" + column.getJavaProperty() + ").toPropertyWhenPresent(\"" + column.getJavaProperty() + "\", record::" + methodName + ")");
            }
        }
        method.addBodyLine("        .build()");
        method.addBodyLine("        .render(RenderingStrategy.MYBATIS3));");
        return MethodAndImports.withMethod(method).withImports(imports).build();
    }
    
    @Override
    public boolean callPlugins(final Method method, final Interface interfaze) {
        return this.context.getPlugins().clientInsertSelectiveMethodGenerated(method, interfaze, this.introspectedTable);
    }
    
    public static class Builder extends BaseBuilder<Builder, InsertSelectiveMethodGenerator>
    {
        private FullyQualifiedJavaType recordType;
        private String tableFieldName;
        
        public Builder withRecordType(final FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }
        
        public Builder withTableFieldName(final String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return this;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }
        
        @Override
        public InsertSelectiveMethodGenerator build() {
            return new InsertSelectiveMethodGenerator(this);
        }
    }
}
