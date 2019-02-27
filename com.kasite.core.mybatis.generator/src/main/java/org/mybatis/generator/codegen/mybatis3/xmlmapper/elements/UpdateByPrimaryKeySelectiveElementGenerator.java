package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
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
        answer.addAttribute(new Attribute("parameterType", parameterType));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        final XmlElement dynamicElement = new XmlElement("set");
        answer.addElement(dynamicElement);
        for (final IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getNonPrimaryKeyColumns())) {
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null");
            final XmlElement isNotNullElement = new XmlElement("if");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            dynamicElement.addElement(isNotNullElement);
            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.append(',');
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
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn2));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn2));
            answer.addElement(new TextElement(sb.toString()));
        }
        if (this.context.getPlugins().sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
