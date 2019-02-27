package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.internal.util.*;
import java.io.*;

public class SelectByExampleWithBLOBsElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getSelectByExampleWithBLOBsStatementId()));
        answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getResultMapWithBLOBsId()));
        answer.addAttribute(new Attribute("parameterClass", this.introspectedTable.getExampleType()));
        this.context.getCommentGenerator().addComment(answer);
        answer.addElement(new TextElement("select"));
        final XmlElement isEqualElement = new XmlElement("isEqual");
        isEqualElement.addAttribute(new Attribute("property", "distinct"));
        isEqualElement.addAttribute(new Attribute("compareValue", "true"));
        isEqualElement.addElement(new TextElement("distinct"));
        final XmlElement isParameterPresent = new XmlElement("isParameterPresent");
        isParameterPresent.addElement(isEqualElement);
        answer.addElement(isParameterPresent);
        final StringBuilder sb = new StringBuilder();
        if (StringUtility.stringHasValue(this.introspectedTable.getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(this.introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID,");
            answer.addElement(new TextElement(sb.toString()));
        }
        answer.addElement(this.getBaseColumnListElement());
        answer.addElement(new TextElement(","));
        answer.addElement(this.getBlobColumnListElement());
        sb.setLength(0);
        sb.append("from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        final XmlElement isParameterPresenteElement = new XmlElement("isParameterPresent");
        answer.addElement(isParameterPresenteElement);
        final XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", this.introspectedTable.getIbatis2SqlMapNamespace() + "." + this.introspectedTable.getExampleWhereClauseId()));
        isParameterPresenteElement.addElement(includeElement);
        final XmlElement isNotNullElement = new XmlElement("isNotNull");
        isNotNullElement.addAttribute(new Attribute("property", "orderByClause"));
        isNotNullElement.addElement(new TextElement("order by $orderByClause$"));
        isParameterPresenteElement.addElement(isNotNullElement);
        if (this.context.getPlugins().sqlMapSelectByExampleWithBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
