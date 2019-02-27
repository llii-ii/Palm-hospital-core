package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.api.dom.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.io.*;

public class AnnotatedSelectByPrimaryKeyMethodGenerator extends SelectByPrimaryKeyMethodGenerator
{
    private boolean useResultMapIfAvailable;
    
    public AnnotatedSelectByPrimaryKeyMethodGenerator(final boolean useResultMapIfAvailable, final boolean isSimple) {
        super(isSimple);
        this.useResultMapIfAvailable = useResultMapIfAvailable;
    }
    
    @Override
    public void addMapperAnnotations(final Interface interfaze, final Method method) {
        final StringBuilder sb = new StringBuilder();
        method.addAnnotation("@Select({");
        OutputUtilities.javaIndent(sb, 1);
        sb.append("\"select\",");
        method.addAnnotation(sb.toString());
        sb.setLength(0);
        OutputUtilities.javaIndent(sb, 1);
        sb.append('\"');
        boolean hasColumns = false;
        Iterator<IntrospectedColumn> iter = this.introspectedTable.getAllColumns().iterator();
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
        sb.append("\",");
        method.addAnnotation(sb.toString());
        boolean and = false;
        iter = this.introspectedTable.getPrimaryKeyColumns().iterator();
        while (iter.hasNext()) {
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            if (and) {
                sb.append("  \"and ");
            }
            else {
                sb.append("\"where ");
                and = true;
            }
            final IntrospectedColumn introspectedColumn = iter.next();
            sb.append(StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn)));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.append('\"');
            if (iter.hasNext()) {
                sb.append(',');
            }
            method.addAnnotation(sb.toString());
        }
        method.addAnnotation("})");
        if (this.useResultMapIfAvailable) {
            if (this.introspectedTable.getRules().generateBaseResultMap() || this.introspectedTable.getRules().generateResultMapWithBLOBs()) {
                this.addResultMapAnnotation(method);
            }
            else {
                this.addAnnotatedResults(interfaze, method);
            }
        }
        else {
            this.addAnnotatedResults(interfaze, method);
        }
    }
    
    private void addResultMapAnnotation(final Method method) {
        final String annotation = String.format("@ResultMap(\"%s.%s\")", this.introspectedTable.getMyBatis3SqlMapNamespace(), this.introspectedTable.getRules().generateResultMapWithBLOBs() ? this.introspectedTable.getResultMapWithBLOBsId() : this.introspectedTable.getBaseResultMapId());
        method.addAnnotation(annotation);
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
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select"));
        if (this.useResultMapIfAvailable) {
            if (this.introspectedTable.getRules().generateBaseResultMap() || this.introspectedTable.getRules().generateResultMapWithBLOBs()) {
                interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ResultMap"));
            }
            else {
                this.addAnnotationImports(interfaze);
            }
        }
        else {
            this.addAnnotationImports(interfaze);
        }
    }
    
    private void addAnnotationImports(final Interface interfaze) {
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
