package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.ibatis2.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.dom.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.*;
import java.util.*;
import java.io.*;

public class InsertElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getInsertStatementId()));
        final FullyQualifiedJavaType parameterType = this.introspectedTable.getRules().calculateAllFieldsClass();
        answer.addAttribute(new Attribute("parameterClass", parameterType.getFullyQualifiedName()));
        this.context.getCommentGenerator().addComment(answer);
        final GeneratedKey gk = this.introspectedTable.getGeneratedKey();
        if (gk != null && gk.isPlacedBeforeInsertInIbatis2()) {
            final IntrospectedColumn introspectedColumn = this.introspectedTable.getColumn(gk.getColumn());
            if (introspectedColumn != null) {
                answer.addElement(this.getSelectKey(introspectedColumn, gk));
            }
        }
        final StringBuilder insertClause = new StringBuilder();
        insertClause.append("insert into ");
        insertClause.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" (");
        final StringBuilder valuesClause = new StringBuilder();
        valuesClause.append("values (");
        final List<String> valuesClauses = new ArrayList<String>();
        final Iterator<IntrospectedColumn> iter = this.introspectedTable.getAllColumns().iterator();
        while (iter.hasNext()) {
            final IntrospectedColumn introspectedColumn2 = iter.next();
            if (introspectedColumn2.isIdentity()) {
                continue;
            }
            insertClause.append(Ibatis2FormattingUtilities.getEscapedColumnName(introspectedColumn2));
            valuesClause.append(Ibatis2FormattingUtilities.getParameterClause(introspectedColumn2));
            if (iter.hasNext()) {
                insertClause.append(", ");
                valuesClause.append(", ");
            }
            if (valuesClause.length() <= 80) {
                continue;
            }
            answer.addElement(new TextElement(insertClause.toString()));
            insertClause.setLength(0);
            OutputUtilities.xmlIndent(insertClause, 1);
            valuesClauses.add(valuesClause.toString());
            valuesClause.setLength(0);
            OutputUtilities.xmlIndent(valuesClause, 1);
        }
        insertClause.append(')');
        answer.addElement(new TextElement(insertClause.toString()));
        valuesClause.append(')');
        valuesClauses.add(valuesClause.toString());
        for (final String clause : valuesClauses) {
            answer.addElement(new TextElement(clause));
        }
        if (gk != null && !gk.isPlacedBeforeInsertInIbatis2()) {
            final IntrospectedColumn introspectedColumn2 = this.introspectedTable.getColumn(gk.getColumn());
            if (introspectedColumn2 != null) {
                answer.addElement(this.getSelectKey(introspectedColumn2, gk));
            }
        }
        if (this.context.getPlugins().sqlMapInsertElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
