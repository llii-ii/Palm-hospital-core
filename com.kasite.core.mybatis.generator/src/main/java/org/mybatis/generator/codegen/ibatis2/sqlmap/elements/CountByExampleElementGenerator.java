package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.dom.xml.*;
import java.io.*;

public class CountByExampleElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getCountByExampleStatementId()));
        answer.addAttribute(new Attribute("parameterClass", this.introspectedTable.getExampleType()));
        answer.addAttribute(new Attribute("resultClass", "java.lang.Long"));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
        sb.append(this.introspectedTable.getIbatis2SqlMapNamespace());
        sb.append('.');
        sb.append(this.introspectedTable.getExampleWhereClauseId());
        final XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", sb.toString()));
        answer.addElement(includeElement);
        if (this.context.getPlugins().sqlMapCountByExampleElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
