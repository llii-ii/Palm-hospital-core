package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.*;

public class CountByExampleElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("select");
        final String fqjt = this.introspectedTable.getExampleType();
        answer.addAttribute(new Attribute("id", this.introspectedTable.getCountByExampleStatementId()));
        answer.addAttribute(new Attribute("parameterType", fqjt));
        answer.addAttribute(new Attribute("resultType", "java.lang.Long"));
        this.context.getCommentGenerator().addComment(answer);
        final StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(this.getExampleIncludeElement());
        if (this.context.getPlugins().sqlMapCountByExampleElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
