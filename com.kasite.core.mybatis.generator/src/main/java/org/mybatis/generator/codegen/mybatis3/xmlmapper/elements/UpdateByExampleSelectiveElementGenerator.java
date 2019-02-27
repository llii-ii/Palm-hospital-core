package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import java.util.*;
import java.io.*;

public class UpdateByExampleSelectiveElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getUpdateByExampleSelectiveStatementId()));
        answer.addAttribute(new Attribute("parameterType", "map"));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        final XmlElement dynamicElement = new XmlElement("set");
        answer.addElement(dynamicElement);
        for (final IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getAllColumns())) {
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty("record."));
            sb.append(" != null");
            final XmlElement isNotNullElement = new XmlElement("if");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            dynamicElement.addElement(isNotNullElement);
            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record."));
            sb.append(',');
            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        answer.addElement(this.getUpdateByExampleIncludeElement());
        if (this.context.getPlugins().sqlMapUpdateByExampleSelectiveElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
