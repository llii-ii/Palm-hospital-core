package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.*;
import java.util.*;
import java.io.*;

public class InsertSelectiveElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getInsertSelectiveStatementId()));
        final FullyQualifiedJavaType parameterType = this.introspectedTable.getRules().calculateAllFieldsClass();
        answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        this.context.getCommentGenerator().addComment(answer);
        final GeneratedKey gk = this.introspectedTable.getGeneratedKey();
        if (gk != null) {
            final IntrospectedColumn introspectedColumn = this.introspectedTable.getColumn(gk.getColumn());
            if (introspectedColumn != null) {
                if (gk.isJdbcStandard()) {
                    answer.addAttribute(new Attribute("useGeneratedKeys", "true"));
                    answer.addAttribute(new Attribute("keyProperty", introspectedColumn.getJavaProperty()));
                    answer.addAttribute(new Attribute("keyColumn", introspectedColumn.getActualColumnName()));
                }
                else {
                    answer.addElement(this.getSelectKey(introspectedColumn, gk));
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        final XmlElement insertTrimElement = new XmlElement("trim");
        insertTrimElement.addAttribute(new Attribute("prefix", "("));
        insertTrimElement.addAttribute(new Attribute("suffix", ")"));
        insertTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        answer.addElement(insertTrimElement);
        final XmlElement valuesTrimElement = new XmlElement("trim");
        valuesTrimElement.addAttribute(new Attribute("prefix", "values ("));
        valuesTrimElement.addAttribute(new Attribute("suffix", ")"));
        valuesTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        answer.addElement(valuesTrimElement);
        for (final IntrospectedColumn introspectedColumn2 : ListUtilities.removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns())) {
            if (introspectedColumn2.isSequenceColumn() || introspectedColumn2.getFullyQualifiedJavaType().isPrimitive()) {
                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn2));
                sb.append(',');
                insertTrimElement.addElement(new TextElement(sb.toString()));
                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn2));
                sb.append(',');
                valuesTrimElement.addElement(new TextElement(sb.toString()));
            }
            else {
                sb.setLength(0);
                sb.append(introspectedColumn2.getJavaProperty());
                sb.append(" != null");
                final XmlElement insertNotNullElement = new XmlElement("if");
                insertNotNullElement.addAttribute(new Attribute("test", sb.toString()));
                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn2));
                sb.append(',');
                insertNotNullElement.addElement(new TextElement(sb.toString()));
                insertTrimElement.addElement(insertNotNullElement);
                sb.setLength(0);
                sb.append(introspectedColumn2.getJavaProperty());
                sb.append(" != null");
                final XmlElement valuesNotNullElement = new XmlElement("if");
                valuesNotNullElement.addAttribute(new Attribute("test", sb.toString()));
                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn2));
                sb.append(',');
                valuesNotNullElement.addElement(new TextElement(sb.toString()));
                valuesTrimElement.addElement(valuesNotNullElement);
            }
        }
        if (this.context.getPlugins().sqlMapInsertSelectiveElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
