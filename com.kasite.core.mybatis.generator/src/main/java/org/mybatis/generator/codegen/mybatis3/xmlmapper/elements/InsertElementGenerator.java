package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.dom.*;
import org.mybatis.generator.config.*;
import java.util.*;
import java.io.*;

public class InsertElementGenerator extends AbstractXmlElementGenerator
{
    private boolean isSimple;
    
    public InsertElementGenerator(final boolean isSimple) {
        this.isSimple = isSimple;
    }
    
    @Override
    public void addElements(final XmlElement parentElement) {
        final XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getInsertStatementId()));
        FullyQualifiedJavaType parameterType;
        if (this.isSimple) {
            parameterType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        }
        else {
            parameterType = this.introspectedTable.getRules().calculateAllFieldsClass();
        }
        answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        this.context.getCommentGenerator().addComment(answer);
        final GeneratedKey gk = this.introspectedTable.getGeneratedKey();
        if (gk != null) {
            final IntrospectedColumn introspectedColumn = this.introspectedTable.getColumn(gk.getColumn());
            if (introspectedColumn != null) {
                if (gk.isJdbcStandard()) {
                    answer.addAttribute(new Attribute("useGeneratedKeys", "true"));
                    answer.addAttribute(new Attribute("keyProperty", introspectedColumn.getJavaProperty()));
                    answer.addAttribute(new Attribute("keyColumn", introspectedColumn.getActualColumnName()));
                }
                else {
                    answer.addElement(this.getSelectKey(introspectedColumn, gk));
                }
            }
        }
        final StringBuilder insertClause = new StringBuilder();
        insertClause.append("insert into ");
        insertClause.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" (");
        final StringBuilder valuesClause = new StringBuilder();
        valuesClause.append("values (");
        final List<String> valuesClauses = new ArrayList<String>();
        final List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns());
        for (int i = 0; i < columns.size(); ++i) {
            final IntrospectedColumn introspectedColumn2 = columns.get(i);
            insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn2));
            valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn2));
            if (i + 1 < columns.size()) {
                insertClause.append(", ");
                valuesClause.append(", ");
            }
            if (valuesClause.length() > 80) {
                answer.addElement(new TextElement(insertClause.toString()));
                insertClause.setLength(0);
                OutputUtilities.xmlIndent(insertClause, 1);
                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(valuesClause, 1);
            }
        }
        insertClause.append(')');
        answer.addElement(new TextElement(insertClause.toString()));
        valuesClause.append(')');
        valuesClauses.add(valuesClause.toString());
        for (final String clause : valuesClauses) {
            answer.addElement(new TextElement(clause));
        }
        if (this.context.getPlugins().sqlMapInsertElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
