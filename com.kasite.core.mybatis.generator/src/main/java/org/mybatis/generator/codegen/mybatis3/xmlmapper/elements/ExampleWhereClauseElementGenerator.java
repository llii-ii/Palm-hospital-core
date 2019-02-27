package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import org.mybatis.generator.api.dom.xml.*;
import java.io.*;

public class ExampleWhereClauseElementGenerator extends AbstractXmlElementGenerator
{
    private boolean isForUpdateByExample;
    
    public ExampleWhereClauseElementGenerator(final boolean isForUpdateByExample) {
        this.isForUpdateByExample = isForUpdateByExample;
    }
    
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("sql");
        if (this.isForUpdateByExample) {
            answer.addAttribute(new Attribute("id", this.introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));
        }
        else {
            answer.addAttribute(new Attribute("id", this.introspectedTable.getExampleWhereClauseId()));
        }
        this.context.getCommentGenerator().addComment(answer);
        final XmlElement whereElement = new XmlElement("where");
        answer.addElement(whereElement);
        final XmlElement outerForEachElement = new XmlElement("foreach");
        if (this.isForUpdateByExample) {
            outerForEachElement.addAttribute(new Attribute("collection", "example.oredCriteria"));
        }
        else {
            outerForEachElement.addAttribute(new Attribute("collection", "oredCriteria"));
        }
        outerForEachElement.addAttribute(new Attribute("item", "criteria"));
        outerForEachElement.addAttribute(new Attribute("separator", "or"));
        whereElement.addElement(outerForEachElement);
        final XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "criteria.valid"));
        outerForEachElement.addElement(ifElement);
        final XmlElement trimElement = new XmlElement("trim");
        trimElement.addAttribute(new Attribute("prefix", "("));
        trimElement.addAttribute(new Attribute("suffix", ")"));
        trimElement.addAttribute(new Attribute("prefixOverrides", "and"));
        ifElement.addElement(trimElement);
        trimElement.addElement(this.getMiddleForEachElement(null));
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getNonBLOBColumns()) {
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                trimElement.addElement(this.getMiddleForEachElement(introspectedColumn));
            }
        }
        if (this.context.getPlugins().sqlMapExampleWhereClauseElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
    
    private XmlElement getMiddleForEachElement(final IntrospectedColumn introspectedColumn) {
        final StringBuilder sb = new StringBuilder();
        String criteriaAttribute;
        boolean typeHandled;
        String typeHandlerString;
        if (introspectedColumn == null) {
            criteriaAttribute = "criteria.criteria";
            typeHandled = false;
            typeHandlerString = null;
        }
        else {
            sb.setLength(0);
            sb.append("criteria.");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append("Criteria");
            criteriaAttribute = sb.toString();
            typeHandled = true;
            sb.setLength(0);
            sb.append(",typeHandler=");
            sb.append(introspectedColumn.getTypeHandler());
            typeHandlerString = sb.toString();
        }
        final XmlElement middleForEachElement = new XmlElement("foreach");
        middleForEachElement.addAttribute(new Attribute("collection", criteriaAttribute));
        middleForEachElement.addAttribute(new Attribute("item", "criterion"));
        final XmlElement chooseElement = new XmlElement("choose");
        middleForEachElement.addElement(chooseElement);
        XmlElement when = new XmlElement("when");
        when.addAttribute(new Attribute("test", "criterion.noValue"));
        when.addElement(new TextElement("and ${criterion.condition}"));
        chooseElement.addElement(when);
        when = new XmlElement("when");
        when.addAttribute(new Attribute("test", "criterion.singleValue"));
        sb.setLength(0);
        sb.append("and ${criterion.condition} #{criterion.value");
        if (typeHandled) {
            sb.append(typeHandlerString);
        }
        sb.append('}');
        when.addElement(new TextElement(sb.toString()));
        chooseElement.addElement(when);
        when = new XmlElement("when");
        when.addAttribute(new Attribute("test", "criterion.betweenValue"));
        sb.setLength(0);
        sb.append("and ${criterion.condition} #{criterion.value");
        if (typeHandled) {
            sb.append(typeHandlerString);
        }
        sb.append("} and #{criterion.secondValue");
        if (typeHandled) {
            sb.append(typeHandlerString);
        }
        sb.append('}');
        when.addElement(new TextElement(sb.toString()));
        chooseElement.addElement(when);
        when = new XmlElement("when");
        when.addAttribute(new Attribute("test", "criterion.listValue"));
        when.addElement(new TextElement("and ${criterion.condition}"));
        final XmlElement innerForEach = new XmlElement("foreach");
        innerForEach.addAttribute(new Attribute("collection", "criterion.value"));
        innerForEach.addAttribute(new Attribute("item", "listItem"));
        innerForEach.addAttribute(new Attribute("open", "("));
        innerForEach.addAttribute(new Attribute("close", ")"));
        innerForEach.addAttribute(new Attribute("separator", ","));
        sb.setLength(0);
        sb.append("#{listItem");
        if (typeHandled) {
            sb.append(typeHandlerString);
        }
        sb.append('}');
        innerForEach.addElement(new TextElement(sb.toString()));
        when.addElement(innerForEach);
        chooseElement.addElement(when);
        return middleForEachElement;
    }
}
