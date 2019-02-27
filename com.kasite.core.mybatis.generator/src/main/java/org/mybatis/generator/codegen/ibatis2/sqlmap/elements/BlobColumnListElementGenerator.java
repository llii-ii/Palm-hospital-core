package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.ibatis2.*;
import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import java.io.*;

public class BlobColumnListElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getBlobColumnListId()));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        final Iterator<IntrospectedColumn> iter = this.introspectedTable.getBLOBColumns().iterator();
        while (iter.hasNext()) {
            sb.append(Ibatis2FormattingUtilities.getSelectListPhrase(iter.next()));
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
        if (this.context.getPlugins().sqlMapBlobColumnListElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
