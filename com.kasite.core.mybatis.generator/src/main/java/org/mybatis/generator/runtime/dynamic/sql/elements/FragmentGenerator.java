package org.mybatis.generator.runtime.dynamic.sql.elements;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.*;
import java.util.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.codegen.mybatis3.*;
import java.io.*;

public class FragmentGenerator
{
    private IntrospectedTable introspectedTable;
    private String resultMapId;
    
    private FragmentGenerator(final Builder builder) {
        this.introspectedTable = builder.introspectedTable;
        this.resultMapId = builder.resultMapId;
    }
    
    public String getSelectList() {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (final IntrospectedColumn column : this.introspectedTable.getAllColumns()) {
            if (first) {
                first = false;
            }
            else {
                sb.append(", ");
            }
            sb.append(column.getJavaProperty());
        }
        return sb.toString();
    }
    
    public MethodParts getPrimaryKeyWhereClauseAndParameters() {
        final MethodParts.Builder builder = new MethodParts.Builder();
        boolean first = true;
        for (final IntrospectedColumn column : this.introspectedTable.getPrimaryKeyColumns()) {
            builder.withImport(column.getFullyQualifiedJavaType());
            builder.withParameter(new Parameter(column.getFullyQualifiedJavaType(), column.getJavaProperty() + "_"));
            if (first) {
                builder.withBodyLine("        .where(" + column.getJavaProperty() + ", isEqualTo(" + column.getJavaProperty() + "_))");
                first = false;
            }
            else {
                builder.withBodyLine("        .and(" + column.getJavaProperty() + ", isEqualTo(" + column.getJavaProperty() + "_))");
            }
        }
        return builder.build();
    }
    
    public List<String> getPrimaryKeyWhereClauseForUpdate() {
        final List<String> lines = new ArrayList<String>();
        boolean first = true;
        for (final IntrospectedColumn column : this.introspectedTable.getPrimaryKeyColumns()) {
            final String methodName = JavaBeansUtil.getGetterMethodName(column.getJavaProperty(), column.getFullyQualifiedJavaType());
            if (first) {
                lines.add("        .where(" + column.getJavaProperty() + ", isEqualTo(record::" + methodName + "))");
                first = false;
            }
            else {
                lines.add("        .and(" + column.getJavaProperty() + ", isEqualTo(record::" + methodName + "))");
            }
        }
        return lines;
    }
    
    public MethodParts getAnnotatedConstructorArgs() {
        final MethodParts.Builder builder = new MethodParts.Builder();
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.type.JdbcType"));
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ConstructorArgs"));
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Arg"));
        builder.withAnnotation("@ConstructorArgs({");
        final StringBuilder sb = new StringBuilder();
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        final Iterator<IntrospectedColumn> iterPk = this.introspectedTable.getPrimaryKeyColumns().iterator();
        final Iterator<IntrospectedColumn> iterNonPk = this.introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iterPk.hasNext()) {
            final IntrospectedColumn introspectedColumn = iterPk.next();
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append(this.getArgAnnotation(imports, introspectedColumn, true));
            if (iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }
            builder.withAnnotation(sb.toString());
        }
        while (iterNonPk.hasNext()) {
            final IntrospectedColumn introspectedColumn = iterNonPk.next();
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append(this.getArgAnnotation(imports, introspectedColumn, false));
            if (iterNonPk.hasNext()) {
                sb.append(',');
            }
            builder.withAnnotation(sb.toString());
        }
        builder.withAnnotation("})").withImports(imports);
        return builder.build();
    }
    
    private String getArgAnnotation(final Set<FullyQualifiedJavaType> imports, final IntrospectedColumn introspectedColumn, final boolean idColumn) {
        final StringBuilder sb = new StringBuilder();
        sb.append("@Arg(column=\"");
        sb.append(introspectedColumn.getActualColumnName());
        imports.add(introspectedColumn.getFullyQualifiedJavaType());
        sb.append("\", javaType=");
        sb.append(introspectedColumn.getFullyQualifiedJavaType().getShortName());
        sb.append(".class");
        if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
            final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedColumn.getTypeHandler());
            imports.add(fqjt);
            sb.append(", typeHandler=");
            sb.append(fqjt.getShortName());
            sb.append(".class");
        }
        sb.append(", jdbcType=JdbcType.");
        sb.append(introspectedColumn.getJdbcTypeName());
        if (idColumn) {
            sb.append(", id=true");
        }
        sb.append(')');
        return sb.toString();
    }
    
    public MethodParts getAnnotatedResults() {
        final MethodParts.Builder builder = new MethodParts.Builder();
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.type.JdbcType"));
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Result"));
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Results"));
        builder.withAnnotation("@Results(id=\"" + this.resultMapId + "\", value = {");
        final StringBuilder sb = new StringBuilder();
        final Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        final Iterator<IntrospectedColumn> iterPk = this.introspectedTable.getPrimaryKeyColumns().iterator();
        final Iterator<IntrospectedColumn> iterNonPk = this.introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iterPk.hasNext()) {
            final IntrospectedColumn introspectedColumn = iterPk.next();
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append(this.getResultAnnotation(imports, introspectedColumn, true));
            if (iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }
            builder.withAnnotation(sb.toString());
        }
        while (iterNonPk.hasNext()) {
            final IntrospectedColumn introspectedColumn = iterNonPk.next();
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append(this.getResultAnnotation(imports, introspectedColumn, false));
            if (iterNonPk.hasNext()) {
                sb.append(',');
            }
            builder.withAnnotation(sb.toString());
        }
        builder.withAnnotation("})").withImports(imports);
        return builder.build();
    }
    
    private String getResultAnnotation(final Set<FullyQualifiedJavaType> imports, final IntrospectedColumn introspectedColumn, final boolean idColumn) {
        final StringBuilder sb = new StringBuilder();
        sb.append("@Result(column=\"");
        sb.append(introspectedColumn.getActualColumnName());
        sb.append("\", property=\"");
        sb.append(introspectedColumn.getJavaProperty());
        sb.append('\"');
        if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
            final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedColumn.getTypeHandler());
            imports.add(fqjt);
            sb.append(", typeHandler=");
            sb.append(fqjt.getShortName());
            sb.append(".class");
        }
        sb.append(", jdbcType=JdbcType.");
        sb.append(introspectedColumn.getJdbcTypeName());
        if (idColumn) {
            sb.append(", id=true");
        }
        sb.append(')');
        return sb.toString();
    }
    
    public MethodParts getGeneratedKeyAnnotation(final GeneratedKey gk) {
        final MethodParts.Builder builder = new MethodParts.Builder();
        final StringBuilder sb = new StringBuilder();
        final IntrospectedColumn introspectedColumn = this.introspectedTable.getColumn(gk.getColumn());
        if (introspectedColumn != null) {
            if (gk.isJdbcStandard()) {
                builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options"));
                sb.append("@Options(useGeneratedKeys=true,keyProperty=\"record.");
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\")");
                builder.withAnnotation(sb.toString());
            }
            else {
                builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectKey"));
                final FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
                sb.append("@SelectKey(statement=\"");
                sb.append(gk.getRuntimeSqlStatement());
                sb.append("\", keyProperty=\"record.");
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\", before=");
                sb.append(gk.isIdentity() ? "false" : "true");
                sb.append(", resultType=");
                sb.append(fqjt.getShortName());
                sb.append(".class)");
                builder.withAnnotation(sb.toString());
            }
        }
        return builder.build();
    }
    
    public List<String> getSetEqualLines(final List<IntrospectedColumn> columnList, final boolean terminate) {
        final List<String> lines = new ArrayList<String>();
        final List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(columnList);
        final Iterator<IntrospectedColumn> iter = columns.iterator();
        while (iter.hasNext()) {
            final IntrospectedColumn column = iter.next();
            final String methodName = JavaBeansUtil.getGetterMethodName(column.getJavaProperty(), column.getFullyQualifiedJavaType());
            String line = "        .set(" + column.getJavaProperty() + ").equalTo(record::" + methodName + ")";
            if (terminate && !iter.hasNext()) {
                line += ";";
            }
            lines.add(line);
        }
        return lines;
    }
    
    public List<String> getSetEqualWhenPresentLines(final List<IntrospectedColumn> columnList, final boolean terminate) {
        final List<String> lines = new ArrayList<String>();
        final List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(columnList);
        final Iterator<IntrospectedColumn> iter = columns.iterator();
        while (iter.hasNext()) {
            final IntrospectedColumn column = iter.next();
            final String methodName = JavaBeansUtil.getGetterMethodName(column.getJavaProperty(), column.getFullyQualifiedJavaType());
            String line = "        .set(" + column.getJavaProperty() + ").equalToWhenPresent(record::" + methodName + ")";
            if (terminate && !iter.hasNext()) {
                line += ";";
            }
            lines.add(line);
        }
        return lines;
    }
    
    public static class Builder
    {
        private IntrospectedTable introspectedTable;
        private String resultMapId;
        
        public Builder withIntrospectedTable(final IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
            return this;
        }
        
        public Builder withResultMapId(final String resultMapId) {
            this.resultMapId = resultMapId;
            return this;
        }
        
        public FragmentGenerator build() {
            return new FragmentGenerator(this);
        }
    }
}
