package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import java.io.*;

public class ExampleWhereClauseElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getExampleWhereClauseId()));
        this.context.getCommentGenerator().addComment(answer);
        final XmlElement outerIterateElement = new XmlElement("iterate");
        outerIterateElement.addAttribute(new Attribute("property", "oredCriteria"));
        outerIterateElement.addAttribute(new Attribute("conjunction", "or"));
        outerIterateElement.addAttribute(new Attribute("prepend", "where"));
        outerIterateElement.addAttribute(new Attribute("removeFirstPrepend", "iterate"));
        answer.addElement(outerIterateElement);
        final XmlElement isEqualElement = new XmlElement("isEqual");
        isEqualElement.addAttribute(new Attribute("property", "oredCriteria[].valid"));
        isEqualElement.addAttribute(new Attribute("compareValue", "true"));
        outerIterateElement.addElement(isEqualElement);
        isEqualElement.addElement(new TextElement("("));
        XmlElement innerIterateElement = new XmlElement("iterate");
        innerIterateElement.addAttribute(new Attribute("prepend", "and"));
        innerIterateElement.addAttribute(new Attribute("property", "oredCriteria[].criteriaWithoutValue"));
        innerIterateElement.addAttribute(new Attribute("conjunction", "and"));
        innerIterateElement.addElement(new TextElement("$oredCriteria[].criteriaWithoutValue[]$"));
        isEqualElement.addElement(innerIterateElement);
        innerIterateElement = new XmlElement("iterate");
        innerIterateElement.addAttribute(new Attribute("prepend", "and"));
        innerIterateElement.addAttribute(new Attribute("property", "oredCriteria[].criteriaWithSingleValue"));
        innerIterateElement.addAttribute(new Attribute("conjunction", "and"));
        innerIterateElement.addElement(new TextElement("$oredCriteria[].criteriaWithSingleValue[].condition$ #oredCriteria[].criteriaWithSingleValue[].value#"));
        isEqualElement.addElement(innerIterateElement);
        innerIterateElement = new XmlElement("iterate");
        innerIterateElement.addAttribute(new Attribute("prepend", "and"));
        innerIterateElement.addAttribute(new Attribute("property", "oredCriteria[].criteriaWithListValue"));
        innerIterateElement.addAttribute(new Attribute("conjunction", "and"));
        innerIterateElement.addElement(new TextElement("$oredCriteria[].criteriaWithListValue[].condition$"));
        XmlElement innerInnerIterateElement = new XmlElement("iterate");
        innerInnerIterateElement.addAttribute(new Attribute("property", "oredCriteria[].criteriaWithListValue[].values"));
        innerInnerIterateElement.addAttribute(new Attribute("open", "("));
        innerInnerIterateElement.addAttribute(new Attribute("close", ")"));
        innerInnerIterateElement.addAttribute(new Attribute("conjunction", ","));
        innerInnerIterateElement.addElement(new TextElement("#oredCriteria[].criteriaWithListValue[].values[]#"));
        innerIterateElement.addElement(innerInnerIterateElement);
        isEqualElement.addElement(innerIterateElement);
        innerIterateElement = new XmlElement("iterate");
        innerIterateElement.addAttribute(new Attribute("prepend", "and"));
        innerIterateElement.addAttribute(new Attribute("property", "oredCriteria[].criteriaWithBetweenValue"));
        innerIterateElement.addAttribute(new Attribute("conjunction", "and"));
        innerIterateElement.addElement(new TextElement("$oredCriteria[].criteriaWithBetweenValue[].condition$"));
        innerIterateElement.addElement(new TextElement("#oredCriteria[].criteriaWithBetweenValue[].values[0]# and"));
        innerIterateElement.addElement(new TextElement("#oredCriteria[].criteriaWithBetweenValue[].values[1]#"));
        isEqualElement.addElement(innerIterateElement);
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getNonBLOBColumns()) {
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                final StringBuilder sb1 = new StringBuilder();
                innerIterateElement = new XmlElement("iterate");
                innerIterateElement.addAttribute(new Attribute("prepend", "and"));
                sb1.append("oredCriteria[].");
                sb1.append(introspectedColumn.getJavaProperty());
                sb1.append("CriteriaWithSingleValue");
                innerIterateElement.addAttribute(new Attribute("property", sb1.toString()));
                innerIterateElement.addAttribute(new Attribute("conjunction", "and"));
                final StringBuilder sb2 = new StringBuilder();
                sb2.append((CharSequence)sb1);
                sb1.insert(0, '$');
                sb1.append("[].condition$ ");
                sb2.insert(0, '#');
                sb2.append("[].value,handler=");
                sb2.append(introspectedColumn.getTypeHandler());
                sb2.append('#');
                sb1.append((CharSequence)sb2);
                innerIterateElement.addElement(new TextElement(sb1.toString()));
                isEqualElement.addElement(innerIterateElement);
                sb1.setLength(0);
                sb2.setLength(0);
                sb1.append("oredCriteria[].");
                sb1.append(introspectedColumn.getJavaProperty());
                sb1.append("CriteriaWithListValue");
                innerIterateElement = new XmlElement("iterate");
                innerIterateElement.addAttribute(new Attribute("prepend", "and"));
                innerIterateElement.addAttribute(new Attribute("property", sb1.toString()));
                innerIterateElement.addAttribute(new Attribute("conjunction", "and"));
                sb2.append('$');
                sb2.append((CharSequence)sb1);
                sb2.append("[].condition$");
                innerIterateElement.addElement(new TextElement(sb2.toString()));
                sb2.setLength(0);
                sb2.append((CharSequence)sb1);
                sb2.append("[].values");
                innerInnerIterateElement = new XmlElement("iterate");
                innerInnerIterateElement.addAttribute(new Attribute("property", sb2.toString()));
                innerInnerIterateElement.addAttribute(new Attribute("open", "("));
                innerInnerIterateElement.addAttribute(new Attribute("close", ")"));
                innerInnerIterateElement.addAttribute(new Attribute("conjunction", ","));
                sb2.setLength(0);
                sb2.append('#');
                sb2.append((CharSequence)sb1);
                sb2.append("[].values[],handler=");
                sb2.append(introspectedColumn.getTypeHandler());
                sb2.append('#');
                innerInnerIterateElement.addElement(new TextElement(sb2.toString()));
                innerIterateElement.addElement(innerInnerIterateElement);
                isEqualElement.addElement(innerIterateElement);
                sb1.setLength(0);
                sb2.setLength(0);
                sb1.append("oredCriteria[].");
                sb1.append(introspectedColumn.getJavaProperty());
                sb1.append("CriteriaWithBetweenValue");
                innerIterateElement = new XmlElement("iterate");
                innerIterateElement.addAttribute(new Attribute("prepend", "and"));
                innerIterateElement.addAttribute(new Attribute("property", sb1.toString()));
                innerIterateElement.addAttribute(new Attribute("conjunction", "and"));
                sb2.append('$');
                sb2.append((CharSequence)sb1);
                sb2.append("[].condition$");
                innerIterateElement.addElement(new TextElement(sb2.toString()));
                sb2.setLength(0);
                sb2.append((CharSequence)sb1);
                sb1.insert(0, '#');
                sb1.append("[].values[0],handler=");
                sb1.append(introspectedColumn.getTypeHandler());
                sb1.append("# and");
                sb2.insert(0, '#');
                sb2.append("[].values[1],handler=");
                sb2.append(introspectedColumn.getTypeHandler());
                sb2.append('#');
                innerIterateElement.addElement(new TextElement(sb1.toString()));
                innerIterateElement.addElement(new TextElement(sb2.toString()));
                isEqualElement.addElement(innerIterateElement);
            }
        }
        isEqualElement.addElement(new TextElement(")"));
        if (this.context.getPlugins().sqlMapExampleWhereClauseElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
