package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.ibatis2.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.*;
import java.util.*;

public class InsertSelectiveElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getInsertSelectiveStatementId()));
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
        final StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        final XmlElement insertElement = new XmlElement("dynamic");
        insertElement.addAttribute(new Attribute("prepend", "("));
        answer.addElement(insertElement);
        answer.addElement(new TextElement("values"));
        final XmlElement valuesElement = new XmlElement("dynamic");
        valuesElement.addAttribute(new Attribute("prepend", "("));
        answer.addElement(valuesElement);
        for (final IntrospectedColumn introspectedColumn2 : this.introspectedTable.getAllColumns()) {
            if (introspectedColumn2.isIdentity()) {
                continue;
            }
            final XmlElement insertNotNullElement = new XmlElement("isNotNull");
            insertNotNullElement.addAttribute(new Attribute("prepend", ","));
            insertNotNullElement.addAttribute(new Attribute("property", introspectedColumn2.getJavaProperty()));
            insertNotNullElement.addElement(new TextElement(Ibatis2FormattingUtilities.getEscapedColumnName(introspectedColumn2)));
            insertElement.addElement(insertNotNullElement);
            final XmlElement valuesNotNullElement = new XmlElement("isNotNull");
            valuesNotNullElement.addAttribute(new Attribute("prepend", ","));
            valuesNotNullElement.addAttribute(new Attribute("property", introspectedColumn2.getJavaProperty()));
            valuesNotNullElement.addElement(new TextElement(Ibatis2FormattingUtilities.getParameterClause(introspectedColumn2)));
            valuesElement.addElement(valuesNotNullElement);
        }
        insertElement.addElement(new TextElement(")"));
        valuesElement.addElement(new TextElement(")"));
        if (gk != null && !gk.isPlacedBeforeInsertInIbatis2()) {
            final IntrospectedColumn introspectedColumn3 = this.introspectedTable.getColumn(gk.getColumn());
            if (introspectedColumn3 != null) {
                answer.addElement(this.getSelectKey(introspectedColumn3, gk));
            }
        }
        if (this.context.getPlugins().sqlMapInsertSelectiveElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
