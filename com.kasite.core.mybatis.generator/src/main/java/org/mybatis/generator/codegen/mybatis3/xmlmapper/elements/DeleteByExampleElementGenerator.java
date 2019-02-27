package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.*;

public class DeleteByExampleElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("delete");
        final String fqjt = this.introspectedTable.getExampleType();
        answer.addAttribute(new Attribute("id", this.introspectedTable.getDeleteByExampleStatementId()));
        answer.addAttribute(new Attribute("parameterType", fqjt));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(this.getExampleIncludeElement());
        if (this.context.getPlugins().sqlMapDeleteByExampleElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
