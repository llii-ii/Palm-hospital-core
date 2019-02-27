package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import java.io.*;

public class SimpleSelectByPrimaryKeyElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getSelectByPrimaryKeyStatementId()));
        answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getBaseResultMapId()));
        String parameterType;
        if (this.introspectedTable.getPrimaryKeyColumns().size() > 1) {
            parameterType = "map";
        }
        else {
            parameterType = this.introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().toString();
        }
        answer.addAttribute(new Attribute("parameterType", parameterType));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (StringUtility.stringHasValue(this.introspectedTable.getSelectByPrimaryKeyQueryId())) {
            sb.append('\'');
            sb.append(this.introspectedTable.getSelectByPrimaryKeyQueryId());
            sb.append("' as QUERYID,");
        }
        final Iterator<IntrospectedColumn> iter = this.introspectedTable.getAllColumns().iterator();
        while (iter.hasNext()) {
            sb.append(MyBatis3FormattingUtilities.getSelectListPhrase(iter.next()));
            if (iter.hasNext()) {
                sb.append(", ");
            }
            if (sb.length() > 80) {
                answer.addElement(new TextElement(sb.toString()));
                sb.setLength(0);
            }
        }
        if (sb.length() > 0) {
            answer.addElement(new TextElement(sb.toString()));
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
            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        if (this.context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
