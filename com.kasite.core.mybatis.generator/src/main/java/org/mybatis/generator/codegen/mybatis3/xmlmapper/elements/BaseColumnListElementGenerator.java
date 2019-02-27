package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import java.io.*;

public class BaseColumnListElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getBaseColumnListId()));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        final Iterator<IntrospectedColumn> iter = this.introspectedTable.getNonBLOBColumns().iterator();
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
        if (this.context.getPlugins().sqlMapBaseColumnListElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
