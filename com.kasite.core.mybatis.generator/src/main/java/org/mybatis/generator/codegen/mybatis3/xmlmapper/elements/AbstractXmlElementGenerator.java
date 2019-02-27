package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.codegen.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.api.dom.xml.*;

public abstract class AbstractXmlElementGenerator extends AbstractGenerator
{
    public abstract void addElements(final XmlElement p0);
    
    protected XmlElement getSelectKey(final IntrospectedColumn introspectedColumn, final GeneratedKey generatedKey) {
        final String identityColumnType = introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName();
        final XmlElement answer = new XmlElement("selectKey");
        answer.addAttribute(new Attribute("resultType", identityColumnType));
        answer.addAttribute(new Attribute("keyProperty", introspectedColumn.getJavaProperty()));
        answer.addAttribute(new Attribute("order", generatedKey.getMyBatis3Order()));
        answer.addElement(new TextElement(generatedKey.getRuntimeSqlStatement()));
        return answer;
    }
    
    protected XmlElement getBaseColumnListElement() {
        final XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", this.introspectedTable.getBaseColumnListId()));
        return answer;
    }
    
    protected XmlElement getBlobColumnListElement() {
        final XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", this.introspectedTable.getBlobColumnListId()));
        return answer;
    }
    
    protected XmlElement getExampleIncludeElement() {
        final XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "_parameter != null"));
        final XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", this.introspectedTable.getExampleWhereClauseId()));
        ifElement.addElement(includeElement);
        return ifElement;
    }
    
    protected XmlElement getUpdateByExampleIncludeElement() {
        final XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "_parameter != null"));
        final XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", this.introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));
        ifElement.addElement(includeElement);
        return ifElement;
    }
}
