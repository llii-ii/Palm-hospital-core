package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.dom.xml.*;
import java.io.*;

public class DeleteByExampleElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("delete");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getDeleteByExampleStatementId()));
        answer.addAttribute(new Attribute("parameterClass", this.introspectedTable.getExampleType()));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
        sb.append(this.introspectedTable.getIbatis2SqlMapNamespace());
        sb.append('.');
        sb.append(this.introspectedTable.getExampleWhereClauseId());
        final XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", sb.toString()));
        answer.addElement(includeElement);
        if (this.context.getPlugins().sqlMapDeleteByExampleElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
