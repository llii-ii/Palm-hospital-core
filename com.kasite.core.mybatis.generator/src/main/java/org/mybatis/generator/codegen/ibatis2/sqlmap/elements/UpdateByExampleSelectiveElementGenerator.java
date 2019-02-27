package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.ibatis2.*;
import java.util.*;
import java.io.*;

public class UpdateByExampleSelectiveElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getUpdateByExampleSelectiveStatementId()));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        final XmlElement dynamicElement = new XmlElement("dynamic");
        dynamicElement.addAttribute(new Attribute("prepend", "set"));
        answer.addElement(dynamicElement);
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getAllColumns()) {
            final XmlElement isNotNullElement = new XmlElement("isNotNull");
            isNotNullElement.addAttribute(new Attribute("prepend", ","));
            isNotNullElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty("record.")));
            dynamicElement.addElement(isNotNullElement);
            sb.setLength(0);
            sb.append(Ibatis2FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(Ibatis2FormattingUtilities.getParameterClause(introspectedColumn, "record."));
            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        final XmlElement isParameterPresentElement = new XmlElement("isParameterPresent");
        answer.addElement(isParameterPresentElement);
        final XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", this.introspectedTable.getIbatis2SqlMapNamespace() + "." + this.introspectedTable.getExampleWhereClauseId()));
        isParameterPresentElement.addElement(includeElement);
        if (this.context.getPlugins().sqlMapUpdateByExampleSelectiveElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
