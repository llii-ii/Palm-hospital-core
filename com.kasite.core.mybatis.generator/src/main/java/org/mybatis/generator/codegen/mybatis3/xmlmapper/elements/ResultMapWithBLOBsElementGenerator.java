package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;

public class ResultMapWithBLOBsElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("resultMap");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getResultMapWithBLOBsId()));
        String returnType;
        if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            returnType = this.introspectedTable.getRecordWithBLOBsType();
        }
        else {
            returnType = this.introspectedTable.getBaseRecordType();
        }
        answer.addAttribute(new Attribute("type", returnType));
        if (!this.introspectedTable.isConstructorBased()) {
            answer.addAttribute(new Attribute("extends", this.introspectedTable.getBaseResultMapId()));
        }
        this.context.getCommentGenerator().addComment(answer);
        if (this.introspectedTable.isConstructorBased()) {
            this.addResultMapConstructorElements(answer);
        }
        else {
            this.addResultMapElements(answer);
        }
        if (this.context.getPlugins().sqlMapResultMapWithBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
    
    private void addResultMapElements(final XmlElement answer) {
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getBLOBColumns()) {
            final XmlElement resultElement = new XmlElement("result");
            resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }
            answer.addElement(resultElement);
        }
    }
    
    private void addResultMapConstructorElements(final XmlElement answer) {
        final XmlElement constructor = new XmlElement("constructor");
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
            final XmlElement resultElement = new XmlElement("idArg");
            resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            resultElement.addAttribute(new Attribute("javaType", introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }
            constructor.addElement(resultElement);
        }
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getNonPrimaryKeyColumns()) {
            final XmlElement resultElement = new XmlElement("arg");
            resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                final StringBuilder sb = new StringBuilder();
                sb.append('_');
                sb.append(introspectedColumn.getFullyQualifiedJavaType().getShortName());
                resultElement.addAttribute(new Attribute("javaType", sb.toString()));
            }
            else if ("byte[]".equals(introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName())) {
                resultElement.addAttribute(new Attribute("javaType", "_byte[]"));
            }
            else {
                resultElement.addAttribute(new Attribute("javaType", introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));
            }
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }
            constructor.addElement(resultElement);
        }
        answer.addElement(constructor);
    }
}
