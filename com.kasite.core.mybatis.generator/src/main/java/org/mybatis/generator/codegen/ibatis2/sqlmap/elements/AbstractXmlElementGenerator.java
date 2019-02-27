package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.codegen.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.xml.*;

public abstract class AbstractXmlElementGenerator extends AbstractGenerator
{
    public abstract void addElements(final XmlElement p0);
    
    protected XmlElement getSelectKey(final IntrospectedColumn introspectedColumn, final GeneratedKey generatedKey) {
        final String identityColumnType = introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName();
        final XmlElement answer = new XmlElement("selectKey");
        answer.addAttribute(new Attribute("resultClass", identityColumnType));
        answer.addAttribute(new Attribute("keyProperty", introspectedColumn.getJavaProperty()));
        if (StringUtility.stringHasValue(generatedKey.getType())) {
            answer.addAttribute(new Attribute("type", generatedKey.getType()));
        }
        answer.addElement(new TextElement(generatedKey.getRuntimeSqlStatement()));
        return answer;
    }
    
    protected XmlElement getBaseColumnListElement() {
        final XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", this.introspectedTable.getIbatis2SqlMapNamespace() + "." + this.introspectedTable.getBaseColumnListId()));
        return answer;
    }
    
    protected XmlElement getBlobColumnListElement() {
        final XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", this.introspectedTable.getIbatis2SqlMapNamespace() + "." + this.introspectedTable.getBlobColumnListId()));
        return answer;
    }
}
