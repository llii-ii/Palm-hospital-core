package org.mybatis.generator.runtime.dynamic.sql;

import org.mybatis.generator.api.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.codegen.mybatis3.*;

public class DynamicSqlSupportClassGenerator
{
    private IntrospectedTable introspectedTable;
    private CommentGenerator commentGenerator;
    
    public TopLevelClass generate() {
        final TopLevelClass topLevelClass = this.buildBasicClass();
        final Field tableField = this.calculateTableDefinition(topLevelClass);
        topLevelClass.addImportedType(tableField.getType());
        topLevelClass.addField(tableField);
        final InnerClass innerClass = this.buildInnerTableClass(topLevelClass);
        topLevelClass.addInnerClass(innerClass);
        final List<IntrospectedColumn> columns = this.introspectedTable.getAllColumns();
        for (final IntrospectedColumn column : columns) {
            this.handleColumn(topLevelClass, innerClass, column, tableField.getName());
        }
        return topLevelClass;
    }
    
    private TopLevelClass buildBasicClass() {
        final TopLevelClass topLevelClass = new TopLevelClass(this.introspectedTable.getMyBatisDynamicSqlSupportType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.setFinal(true);
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.SqlColumn"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.SqlTable"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("java.sql.JDBCType"));
        return topLevelClass;
    }
    
    private InnerClass buildInnerTableClass(final TopLevelClass topLevelClass) {
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(this.introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        final InnerClass innerClass = new InnerClass(fqjt.getShortName());
        innerClass.setVisibility(JavaVisibility.PUBLIC);
        innerClass.setStatic(true);
        innerClass.setFinal(true);
        innerClass.setSuperClass(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.SqlTable"));
        final Method method = new Method(fqjt.getShortName());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.addBodyLine("super(\"" + StringUtility.escapeStringForJava(this.introspectedTable.getFullyQualifiedTableNameAtRuntime()) + "\");");
        innerClass.addMethod(method);
        this.commentGenerator.addClassAnnotation(innerClass, this.introspectedTable, topLevelClass.getImportedTypes());
        return innerClass;
    }
    
    private Field calculateTableDefinition(final TopLevelClass topLevelClass) {
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(this.introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        final String fieldName = JavaBeansUtil.getValidPropertyName(this.introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        final Field field = new Field(fieldName, fqjt);
        this.commentGenerator.addFieldAnnotation(field, this.introspectedTable, topLevelClass.getImportedTypes());
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setStatic(true);
        field.setFinal(true);
        final StringBuilder initializationString = new StringBuilder();
        initializationString.append(String.format("new %s()", StringUtility.escapeStringForJava(this.introspectedTable.getFullyQualifiedTable().getDomainObjectName())));
        field.setInitializationString(initializationString.toString());
        return field;
    }
    
    private void handleColumn(final TopLevelClass topLevelClass, final InnerClass innerClass, final IntrospectedColumn column, final String tableFieldName) {
        topLevelClass.addImportedType(column.getFullyQualifiedJavaType());
        final FullyQualifiedJavaType fieldType = this.calculateFieldType(column);
        final String fieldName = column.getJavaProperty();
        Field field = new Field(fieldName, fieldType);
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setStatic(true);
        field.setFinal(true);
        field.setInitializationString(tableFieldName + "." + fieldName);
        this.commentGenerator.addFieldAnnotation(field, this.introspectedTable, column, topLevelClass.getImportedTypes());
        topLevelClass.addField(field);
        field = new Field(fieldName, fieldType);
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setFinal(true);
        field.setInitializationString(this.calculateInnerInitializationString(column));
        innerClass.addField(field);
    }
    
    private FullyQualifiedJavaType calculateFieldType(final IntrospectedColumn column) {
        FullyQualifiedJavaType typeParameter;
        if (column.getFullyQualifiedJavaType().isPrimitive()) {
            typeParameter = column.getFullyQualifiedJavaType().getPrimitiveTypeWrapper();
        }
        else {
            typeParameter = column.getFullyQualifiedJavaType();
        }
        return new FullyQualifiedJavaType(String.format("SqlColumn<%s>", typeParameter.getShortName()));
    }
    
    private String calculateInnerInitializationString(final IntrospectedColumn column) {
        final StringBuilder initializationString = new StringBuilder();
        initializationString.append(String.format("column(\"%s\", JDBCType.%s", StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getEscapedColumnName(column)), column.getJdbcTypeName()));
        if (StringUtility.stringHasValue(column.getTypeHandler())) {
            initializationString.append(String.format(", \"%s\")", column.getTypeHandler()));
        }
        else {
            initializationString.append(')');
        }
        return initializationString.toString();
    }
    
    public static DynamicSqlSupportClassGenerator of(final IntrospectedTable introspectedTable, final CommentGenerator commentGenerator) {
        final DynamicSqlSupportClassGenerator generator = new DynamicSqlSupportClassGenerator();
        generator.introspectedTable = introspectedTable;
        generator.commentGenerator = commentGenerator;
        return generator;
    }
}
