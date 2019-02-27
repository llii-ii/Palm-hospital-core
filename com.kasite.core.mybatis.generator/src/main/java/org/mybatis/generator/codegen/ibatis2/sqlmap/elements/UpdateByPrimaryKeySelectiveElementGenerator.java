package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.ibatis2.*;
import java.util.*;
import java.io.*;

public class UpdateByPrimaryKeySelectiveElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getUpdateByPrimaryKeySelectiveStatementId()));
        String parameterType;
        if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = this.introspectedTable.getRecordWithBLOBsType();
        }
        else {
            parameterType = this.introspectedTable.getBaseRecordType();
        }
        answer.addAttribute(new Attribute("parameterClass", parameterType));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        final XmlElement dynamicElement = new XmlElement("dynamic");
        dynamicElement.addAttribute(new Attribute("prepend", "set"));
        answer.addElement(dynamicElement);
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getNonPrimaryKeyColumns()) {
            final XmlElement isNotNullElement = new XmlElement("isNotNull");
            isNotNullElement.addAttribute(new Attribute("prepend", ","));
            isNotNullElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
            dynamicElement.addElement(isNotNullElement);
            sb.setLength(0);
            sb.append(Ibatis2FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(Ibatis2FormattingUtilities.getParameterClause(introspectedColumn));
            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        boolean and = false;
        for (final IntrospectedColumn introspectedColumn2 : this.introspectedTable.getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and ");
            }
            else {
                sb.append("where ");
                and = true;
            }
            sb.append(Ibatis2FormattingUtilities.getEscapedColumnName(introspectedColumn2));
            sb.append(" = ");
            sb.append(Ibatis2FormattingUtilities.getParameterClause(introspectedColumn2));
            answer.addElement(new TextElement(sb.toString()));
        }
        if (this.context.getPlugins().sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
