package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.api.dom.*;
import java.util.*;
import java.io.*;

public class UpdateByExampleWithBLOBsElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getUpdateByExampleWithBLOBsStatementId()));
        answer.addAttribute(new Attribute("parameterType", "map"));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
        sb.append("set ");
        final Iterator<IntrospectedColumn> iter = ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getAllColumns()).iterator();
        while (iter.hasNext()) {
            final IntrospectedColumn introspectedColumn = iter.next();
            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record."));
            if (iter.hasNext()) {
                sb.append(',');
            }
            answer.addElement(new TextElement(sb.toString()));
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }
        answer.addElement(this.getUpdateByExampleIncludeElement());
        if (this.context.getPlugins().sqlMapUpdateByExampleWithBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
