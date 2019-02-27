package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.api.dom.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.io.*;

public class AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator extends UpdateByPrimaryKeyWithoutBLOBsMethodGenerator
{
    private boolean isSimple;
    
    public AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator(final boolean isSimple) {
        this.isSimple = isSimple;
    }
    
    @Override
    public void addMapperAnnotations(final Method method) {
        method.addAnnotation("@Update({");
        final StringBuilder sb = new StringBuilder();
        OutputUtilities.javaIndent(sb, 1);
        sb.append("\"update ");
        sb.append(StringUtility.escapeStringForJava(this.introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        sb.append("\",");
        method.addAnnotation(sb.toString());
        sb.setLength(0);
        OutputUtilities.javaIndent(sb, 1);
        sb.append("\"set ");
        Iterator<IntrospectedColumn> iter;
        if (this.isSimple) {
            iter = ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getNonPrimaryKeyColumns()).iterator();
        }
        else {
            iter = ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getBaseColumns()).iterator();
        }
        while (iter.hasNext()) {
            final IntrospectedColumn introspectedColumn = iter.next();
            sb.append(StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn)));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            if (iter.hasNext()) {
                sb.append(',');
            }
            sb.append("\",");
            method.addAnnotation(sb.toString());
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.javaIndent(sb, 1);
                sb.append("  \"");
            }
        }
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
            final IntrospectedColumn introspectedColumn2 = iter.next();
            sb.append(StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn2)));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn2));
            sb.append('\"');
            if (iter.hasNext()) {
                sb.append(',');
            }
            method.addAnnotation(sb.toString());
        }
        method.addAnnotation("})");
    }
    
    @Override
    public void addExtraImports(final Interface interfaze) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Update"));
    }
}
