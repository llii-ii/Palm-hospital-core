package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.codegen.mybatis3.*;
import java.util.*;
import java.io.*;

public class DeleteByPrimaryKeyElementGenerator extends AbstractXmlElementGenerator
{
    private boolean isSimple;
    
    public DeleteByPrimaryKeyElementGenerator(final boolean isSimple) {
        this.isSimple = isSimple;
    }
    
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("delete");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getDeleteByPrimaryKeyStatementId()));
        String parameterClass;
        if (!this.isSimple && this.introspectedTable.getRules().generatePrimaryKeyClass()) {
            parameterClass = this.introspectedTable.getPrimaryKeyType();
        }
        else if (this.introspectedTable.getPrimaryKeyColumns().size() > 1) {
            parameterClass = "map";
        }
        else {
            parameterClass = this.introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().toString();
        }
        answer.addAttribute(new Attribute("parameterType", parameterClass));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
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
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        if (this.context.getPlugins().sqlMapDeleteByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
