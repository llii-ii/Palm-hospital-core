package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.ibatis2.*;
import org.mybatis.generator.api.dom.*;
import java.util.*;
import java.io.*;

public class UpdateByPrimaryKeyWithBLOBsElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getUpdateByPrimaryKeyWithBLOBsStatementId()));
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
        sb.setLength(0);
        sb.append("set ");
        final Iterator<IntrospectedColumn> iter = this.introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iter.hasNext()) {
            final IntrospectedColumn introspectedColumn = iter.next();
            sb.append(Ibatis2FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(Ibatis2FormattingUtilities.getParameterClause(introspectedColumn));
            if (iter.hasNext()) {
                sb.append(',');
            }
            answer.addElement(new TextElement(sb.toString()));
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
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
        if (this.context.getPlugins().sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
