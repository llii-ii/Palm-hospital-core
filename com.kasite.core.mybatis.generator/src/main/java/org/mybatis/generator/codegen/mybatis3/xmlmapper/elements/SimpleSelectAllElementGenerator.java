package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import java.io.*;

public class SimpleSelectAllElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getSelectAllStatementId()));
        answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getBaseResultMapId()));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("select ");
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
        final String orderByClause = this.introspectedTable.getTableConfigurationProperty("selectAllOrderByClause");
        final boolean hasOrderBy = StringUtility.stringHasValue(orderByClause);
        if (hasOrderBy) {
            sb.setLength(0);
            sb.append("order by ");
            sb.append(orderByClause);
            answer.addElement(new TextElement(sb.toString()));
        }
        if (this.context.getPlugins().sqlMapSelectAllElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
