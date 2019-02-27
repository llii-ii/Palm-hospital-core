package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.codegen.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.*;

public abstract class AbstractJavaMapperMethodGenerator extends AbstractGenerator
{
    public abstract void addInterfaceElements(final Interface p0);
    
    protected String getResultAnnotation(final Interface interfaze, final IntrospectedColumn introspectedColumn, final boolean idColumn, final boolean constructorBased) {
        final StringBuilder sb = new StringBuilder();
        if (constructorBased) {
            interfaze.addImportedType(introspectedColumn.getFullyQualifiedJavaType());
            sb.append("@Arg(column=\"");
            sb.append(MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn));
            sb.append("\", javaType=");
            sb.append(introspectedColumn.getFullyQualifiedJavaType().getShortName());
            sb.append(".class");
        }
        else {
            sb.append("@Result(column=\"");
            sb.append(MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn));
            sb.append("\", property=\"");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append('\"');
        }
        if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
            final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedColumn.getTypeHandler());
            interfaze.addImportedType(fqjt);
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
    
    protected void addGeneratedKeyAnnotation(final Method method, final GeneratedKey gk) {
        final StringBuilder sb = new StringBuilder();
        final IntrospectedColumn introspectedColumn = this.introspectedTable.getColumn(gk.getColumn());
        if (introspectedColumn != null) {
            if (gk.isJdbcStandard()) {
                sb.append("@Options(useGeneratedKeys=true,keyProperty=\"");
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\")");
                method.addAnnotation(sb.toString());
            }
            else {
                final FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
                sb.append("@SelectKey(statement=\"");
                sb.append(gk.getRuntimeSqlStatement());
                sb.append("\", keyProperty=\"");
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\", before=");
                sb.append(gk.isIdentity() ? "false" : "true");
                sb.append(", resultType=");
                sb.append(fqjt.getShortName());
                sb.append(".class)");
                method.addAnnotation(sb.toString());
            }
        }
    }
    
    protected void addGeneratedKeyImports(final Interface interfaze, final GeneratedKey gk) {
        final IntrospectedColumn introspectedColumn = this.introspectedTable.getColumn(gk.getColumn());
        if (introspectedColumn != null) {
            if (gk.isJdbcStandard()) {
                interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options"));
            }
            else {
                interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectKey"));
                final FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
                interfaze.addImportedType(fqjt);
            }
        }
    }
}
