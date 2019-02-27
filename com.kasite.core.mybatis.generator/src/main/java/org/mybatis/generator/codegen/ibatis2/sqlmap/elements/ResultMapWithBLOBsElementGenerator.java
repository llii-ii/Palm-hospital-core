package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.ibatis2.*;
import org.mybatis.generator.api.dom.xml.*;
import java.util.*;

public class ResultMapWithBLOBsElementGenerator extends AbstractXmlElementGenerator
{
    @Override
    public void addElements(final XmlElement parentElement) {
        final boolean useColumnIndex = StringUtility.isTrue(this.introspectedTable.getTableConfigurationProperty("useColumnIndexes"));
        final XmlElement answer = new XmlElement("resultMap");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getResultMapWithBLOBsId()));
        String returnType;
        if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            returnType = this.introspectedTable.getRecordWithBLOBsType();
        }
        else {
            returnType = this.introspectedTable.getBaseRecordType();
        }
        answer.addAttribute(new Attribute("class", returnType));
        final StringBuilder sb = new StringBuilder();
        sb.append(this.introspectedTable.getIbatis2SqlMapNamespace());
        sb.append('.');
        sb.append(this.introspectedTable.getBaseResultMapId());
        answer.addAttribute(new Attribute("extends", sb.toString()));
        this.context.getCommentGenerator().addComment(answer);
        int i = this.introspectedTable.getNonBLOBColumnCount() + 1;
        if (StringUtility.stringHasValue(this.introspectedTable.getSelectByPrimaryKeyQueryId()) || StringUtility.stringHasValue(this.introspectedTable.getSelectByExampleQueryId())) {
            ++i;
        }
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getBLOBColumns()) {
            final XmlElement resultElement = new XmlElement("result");
            if (useColumnIndex) {
                resultElement.addAttribute(new Attribute("columnIndex", Integer.toString(i++)));
            }
            else {
                resultElement.addAttribute(new Attribute("column", Ibatis2FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            }
            resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }
            answer.addElement(resultElement);
        }
        if (this.context.getPlugins().sqlMapResultMapWithBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
