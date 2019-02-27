package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import java.io.*;

public class AnnotatedSelectAllMethodGenerator extends SelectAllMethodGenerator
{
    @Override
    public void addMapperAnnotations(final Interface interfaze, final Method method) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select"));
        final StringBuilder sb = new StringBuilder();
        method.addAnnotation("@Select({");
        OutputUtilities.javaIndent(sb, 1);
        sb.append("\"select\",");
        method.addAnnotation(sb.toString());
        sb.setLength(0);
        OutputUtilities.javaIndent(sb, 1);
        sb.append('\"');
        boolean hasColumns = false;
        final Iterator<IntrospectedColumn> iter = this.introspectedTable.getAllColumns().iterator();
        while (iter.hasNext()) {
            sb.append(StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(iter.next())));
            hasColumns = true;
            if (iter.hasNext()) {
                sb.append(", ");
            }
            if (sb.length() > 80) {
                sb.append("\",");
                method.addAnnotation(sb.toString());
                sb.setLength(0);
                OutputUtilities.javaIndent(sb, 1);
                sb.append('\"');
                hasColumns = false;
            }
        }
        if (hasColumns) {
            sb.append("\",");
            method.addAnnotation(sb.toString());
        }
        sb.setLength(0);
        OutputUtilities.javaIndent(sb, 1);
        sb.append("\"from ");
        sb.append(StringUtility.escapeStringForJava(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append('\"');
        final String orderByClause = this.introspectedTable.getTableConfigurationProperty("selectAllOrderByClause");
        final boolean hasOrderBy = StringUtility.stringHasValue(orderByClause);
        if (hasOrderBy) {
            sb.append(',');
        }
        method.addAnnotation(sb.toString());
        if (hasOrderBy) {
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append("\"order by ");
            sb.append(orderByClause);
            sb.append('\"');
            method.addAnnotation(sb.toString());
        }
        method.addAnnotation("})");
        this.addAnnotatedResults(interfaze, method);
    }
    
    private void addAnnotatedResults(final Interface interfaze, final Method method) {
        if (this.introspectedTable.isConstructorBased()) {
            method.addAnnotation("@ConstructorArgs({");
        }
        else {
            method.addAnnotation("@Results({");
        }
        final StringBuilder sb = new StringBuilder();
        final Iterator<IntrospectedColumn> iterPk = this.introspectedTable.getPrimaryKeyColumns().iterator();
        final Iterator<IntrospectedColumn> iterNonPk = this.introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iterPk.hasNext()) {
            final IntrospectedColumn introspectedColumn = iterPk.next();
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append(this.getResultAnnotation(interfaze, introspectedColumn, true, this.introspectedTable.isConstructorBased()));
            if (iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }
            method.addAnnotation(sb.toString());
        }
        while (iterNonPk.hasNext()) {
            final IntrospectedColumn introspectedColumn = iterNonPk.next();
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append(this.getResultAnnotation(interfaze, introspectedColumn, false, this.introspectedTable.isConstructorBased()));
            if (iterNonPk.hasNext()) {
                sb.append(',');
            }
            method.addAnnotation(sb.toString());
        }
        method.addAnnotation("})");
    }
    
    @Override
    public void addExtraImports(final Interface interfaze) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.type.JdbcType"));
        if (this.introspectedTable.isConstructorBased()) {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Arg"));
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ConstructorArgs"));
        }
        else {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Result"));
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Results"));
        }
    }
}
