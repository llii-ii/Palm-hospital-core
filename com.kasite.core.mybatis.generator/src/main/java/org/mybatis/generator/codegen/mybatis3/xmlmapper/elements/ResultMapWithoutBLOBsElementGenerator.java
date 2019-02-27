package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;

public class ResultMapWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator
{
    private boolean isSimple;
    
    public ResultMapWithoutBLOBsElementGenerator(final boolean isSimple) {
        this.isSimple = isSimple;
    }
    
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("resultMap");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getBaseResultMapId()));
        String returnType;
        if (this.isSimple) {
            returnType = this.introspectedTable.getBaseRecordType();
        }
        else if (this.introspectedTable.getRules().generateBaseRecordClass()) {
            returnType = this.introspectedTable.getBaseRecordType();
        }
        else {
            returnType = this.introspectedTable.getPrimaryKeyType();
        }
        answer.addAttribute(new Attribute("type", returnType));
        this.context.getCommentGenerator().addComment(answer);
        if (this.introspectedTable.isConstructorBased()) {
            this.addResultMapConstructorElements(answer);
        }
        else {
            this.addResultMapElements(answer);
        }
        if (this.context.getPlugins().sqlMapResultMapWithoutBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
    
    private void addResultMapElements(final XmlElement answer) {
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
            final XmlElement resultElement = new XmlElement("id");
            resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }
            answer.addElement(resultElement);
        }
        List<IntrospectedColumn> columns;
        if (this.isSimple) {
            columns = this.introspectedTable.getNonPrimaryKeyColumns();
        }
        else {
            columns = this.introspectedTable.getBaseColumns();
        }
        for (final IntrospectedColumn introspectedColumn2 : columns) {
            final XmlElement resultElement2 = new XmlElement("result");
            resultElement2.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn2)));
            resultElement2.addAttribute(new Attribute("property", introspectedColumn2.getJavaProperty()));
            resultElement2.addAttribute(new Attribute("jdbcType", introspectedColumn2.getJdbcTypeName()));
            if (StringUtility.stringHasValue(introspectedColumn2.getTypeHandler())) {
                resultElement2.addAttribute(new Attribute("typeHandler", introspectedColumn2.getTypeHandler()));
            }
            answer.addElement(resultElement2);
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
        List<IntrospectedColumn> columns;
        if (this.isSimple) {
            columns = this.introspectedTable.getNonPrimaryKeyColumns();
        }
        else {
            columns = this.introspectedTable.getBaseColumns();
        }
        for (final IntrospectedColumn introspectedColumn2 : columns) {
            final XmlElement resultElement2 = new XmlElement("arg");
            resultElement2.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn2)));
            resultElement2.addAttribute(new Attribute("jdbcType", introspectedColumn2.getJdbcTypeName()));
            resultElement2.addAttribute(new Attribute("javaType", introspectedColumn2.getFullyQualifiedJavaType().getFullyQualifiedName()));
            if (StringUtility.stringHasValue(introspectedColumn2.getTypeHandler())) {
                resultElement2.addAttribute(new Attribute("typeHandler", introspectedColumn2.getTypeHandler()));
            }
            constructor.addElement(resultElement2);
        }
        answer.addElement(constructor);
    }
}
