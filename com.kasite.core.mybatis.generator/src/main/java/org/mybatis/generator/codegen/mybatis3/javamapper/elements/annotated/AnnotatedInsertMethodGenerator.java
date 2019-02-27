package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.api.dom.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import java.util.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.api.dom.java.*;
import java.io.*;

public class AnnotatedInsertMethodGenerator extends InsertMethodGenerator
{
    public AnnotatedInsertMethodGenerator(final boolean isSimple) {
        super(isSimple);
    }
    
    @Override
    public void addMapperAnnotations(final Method method) {
        method.addAnnotation("@Insert({");
        final StringBuilder insertClause = new StringBuilder();
        final StringBuilder valuesClause = new StringBuilder();
        OutputUtilities.javaIndent(insertClause, 1);
        OutputUtilities.javaIndent(valuesClause, 1);
        insertClause.append("\"insert into ");
        insertClause.append(StringUtility.escapeStringForJava(this.introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        insertClause.append(" (");
        valuesClause.append("\"values (");
        final List<String> valuesClauses = new ArrayList<String>();
        final Iterator<IntrospectedColumn> iter = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns()).iterator();
        boolean hasFields = false;
        while (iter.hasNext()) {
            final IntrospectedColumn introspectedColumn = iter.next();
            insertClause.append(StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn)));
            valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            hasFields = true;
            if (iter.hasNext()) {
                insertClause.append(", ");
                valuesClause.append(", ");
            }
            if (valuesClause.length() > 60) {
                if (!iter.hasNext()) {
                    insertClause.append(')');
                    valuesClause.append(')');
                }
                insertClause.append("\",");
                valuesClause.append('\"');
                if (iter.hasNext()) {
                    valuesClause.append(',');
                }
                method.addAnnotation(insertClause.toString());
                insertClause.setLength(0);
                OutputUtilities.javaIndent(insertClause, 1);
                insertClause.append('\"');
                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.javaIndent(valuesClause, 1);
                valuesClause.append('\"');
                hasFields = false;
            }
        }
        if (hasFields) {
            insertClause.append(")\",");
            method.addAnnotation(insertClause.toString());
            valuesClause.append(")\"");
            valuesClauses.add(valuesClause.toString());
        }
        for (final String clause : valuesClauses) {
            method.addAnnotation(clause);
        }
        method.addAnnotation("})");
        final GeneratedKey gk = this.introspectedTable.getGeneratedKey();
        if (gk != null) {
            this.addGeneratedKeyAnnotation(method, gk);
        }
    }
    
    @Override
    public void addExtraImports(final Interface interfaze) {
        final GeneratedKey gk = this.introspectedTable.getGeneratedKey();
        if (gk != null) {
            this.addGeneratedKeyImports(interfaze, gk);
        }
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Insert"));
    }
}
