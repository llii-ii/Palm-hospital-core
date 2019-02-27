package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.api.dom.*;
import java.util.*;
import java.io.*;

public class UpdateByPrimaryKeyWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator
{
    private boolean isSimple;
    
    public UpdateByPrimaryKeyWithoutBLOBsElementGenerator(final boolean isSimple) {
        this.isSimple = isSimple;
    }
    
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getUpdateByPrimaryKeyStatementId()));
        answer.addAttribute(new Attribute("parameterType", this.introspectedTable.getBaseRecordType()));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
        sb.append("set ");
        Iterator<IntrospectedColumn> iter;
        if (this.isSimple) {
            iter = ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getNonPrimaryKeyColumns()).iterator();
        }
        else {
            iter = ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getBaseColumns()).iterator();
        }
        while (iter.hasNext()) {
            final IntrospectedColumn introspectedColumn = iter.next();
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
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
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn2));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn2));
            answer.addElement(new TextElement(sb.toString()));
        }
        if (this.context.getPlugins().sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
