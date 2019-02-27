package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.api.dom.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.io.*;

public class AnnotatedDeleteByPrimaryKeyMethodGenerator extends DeleteByPrimaryKeyMethodGenerator
{
    public AnnotatedDeleteByPrimaryKeyMethodGenerator(final boolean isSimple) {
        super(isSimple);
    }
    
    @Override
    public void addMapperAnnotations(final Method method) {
        method.addAnnotation("@Delete({");
        final StringBuilder sb = new StringBuilder();
        OutputUtilities.javaIndent(sb, 1);
        sb.append("\"delete from ");
        sb.append(StringUtility.escapeStringForJava(this.introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        sb.append("\",");
        method.addAnnotation(sb.toString());
        boolean and = false;
        final Iterator<IntrospectedColumn> iter = this.introspectedTable.getPrimaryKeyColumns().iterator();
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
            sb.append(StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn)));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
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
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Delete"));
    }
}
