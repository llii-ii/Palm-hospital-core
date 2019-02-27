package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.*;
import java.util.*;
import java.io.*;

public class AnnotatedSelectByExampleWithBLOBsMethodGenerator extends SelectByExampleWithBLOBsMethodGenerator
{
    @Override
    public void addMapperAnnotations(final Interface interfaze, final Method method) {
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(this.introspectedTable.getMyBatis3SqlProviderType());
        final StringBuilder sb = new StringBuilder();
        sb.append("@SelectProvider(type=");
        sb.append(fqjt.getShortName());
        sb.append(".class, method=\"");
        sb.append(this.introspectedTable.getSelectByExampleWithBLOBsStatementId());
        sb.append("\")");
        method.addAnnotation(sb.toString());
        if (this.introspectedTable.isConstructorBased()) {
            method.addAnnotation("@ConstructorArgs({");
        }
        else {
            method.addAnnotation("@Results({");
        }
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
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider"));
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
