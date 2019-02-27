package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.ibatis2.*;
import java.util.*;
import java.io.*;

public class SelectByPrimaryKeyElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getSelectByPrimaryKeyStatementId()));
        if (this.introspectedTable.getRules().generateResultMapWithBLOBs()) {
            answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getResultMapWithBLOBsId()));
        }
        else {
            answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getBaseResultMapId()));
        }
        String parameterType;
        if (this.introspectedTable.getRules().generatePrimaryKeyClass()) {
            parameterType = this.introspectedTable.getPrimaryKeyType();
        }
        else {
            parameterType = this.introspectedTable.getBaseRecordType();
        }
        answer.addAttribute(new Attribute("parameterClass", parameterType));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (StringUtility.stringHasValue(this.introspectedTable.getSelectByPrimaryKeyQueryId())) {
            sb.append('\'');
            sb.append(this.introspectedTable.getSelectByPrimaryKeyQueryId());
            sb.append("' as QUERYID,");
        }
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(this.getBaseColumnListElement());
        if (this.introspectedTable.hasBLOBColumns()) {
            answer.addElement(new TextElement(","));
            answer.addElement(this.getBlobColumnListElement());
        }
        sb.setLength(0);
        sb.append("from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        boolean and = false;
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and ");
            }
            else {
                sb.append("where ");
                and = true;
            }
            sb.append(Ibatis2FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(Ibatis2FormattingUtilities.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        if (this.context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
