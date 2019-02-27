package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.ibatis2.*;
import org.mybatis.generator.api.dom.*;
import java.util.*;
import java.io.*;

public class UpdateByExampleWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getUpdateByExampleStatementId()));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
        sb.append("set ");
        final Iterator<IntrospectedColumn> iter = this.introspectedTable.getNonBLOBColumns().iterator();
        while (iter.hasNext()) {
            final IntrospectedColumn introspectedColumn = iter.next();
            sb.append(Ibatis2FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(Ibatis2FormattingUtilities.getParameterClause(introspectedColumn, "record."));
            if (iter.hasNext()) {
                sb.append(',');
            }
            answer.addElement(new TextElement(sb.toString()));
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }
        final XmlElement isParameterPresentElement = new XmlElement("isParameterPresent");
        answer.addElement(isParameterPresentElement);
        final XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", this.introspectedTable.getIbatis2SqlMapNamespace() + "." + this.introspectedTable.getExampleWhereClauseId()));
        isParameterPresentElement.addElement(includeElement);
        if (this.context.getPlugins().sqlMapUpdateByExampleWithoutBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
