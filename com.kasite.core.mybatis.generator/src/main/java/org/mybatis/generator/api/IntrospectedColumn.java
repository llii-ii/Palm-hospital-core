package org.mybatis.generator.api;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;

public class IntrospectedColumn
{
    protected String actualColumnName;
    protected int jdbcType;
    protected String jdbcTypeName;
    protected boolean nullable;
    protected int length;
    protected int scale;
    protected boolean identity;
    protected boolean isSequenceColumn;
    protected String javaProperty;
    protected FullyQualifiedJavaType fullyQualifiedJavaType;
    protected String tableAlias;
    protected String typeHandler;
    protected Context context;
    protected boolean isColumnNameDelimited;
    protected IntrospectedTable introspectedTable;
    protected Properties properties;
    protected String remarks;
    protected String defaultValue;
    protected boolean isAutoIncrement;
    protected boolean isGeneratedColumn;
    protected boolean isGeneratedAlways;
    
    public IntrospectedColumn() {
        this.properties = new Properties();
    }
    
    public int getJdbcType() {
        return this.jdbcType;
    }
    
    public void setJdbcType(final int jdbcType) {
        this.jdbcType = jdbcType;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public void setLength(final int length) {
        this.length = length;
    }
    
    public boolean isNullable() {
        return this.nullable;
    }
    
    public void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }
    
    public int getScale() {
        return this.scale;
    }
    
    public void setScale(final int scale) {
        this.scale = scale;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Actual Column Name: ");
        sb.append(this.actualColumnName);
        sb.append(", JDBC Type: ");
        sb.append(this.jdbcType);
        sb.append(", Nullable: ");
        sb.append(this.nullable);
        sb.append(", Length: ");
        sb.append(this.length);
        sb.append(", Scale: ");
        sb.append(this.scale);
        sb.append(", Identity: ");
        sb.append(this.identity);
        return sb.toString();
    }
    
    public void setActualColumnName(final String actualColumnName) {
        this.actualColumnName = actualColumnName;
        this.isColumnNameDelimited = StringUtility.stringContainsSpace(actualColumnName);
    }
    
    public boolean isIdentity() {
        return this.identity;
    }
    
    public void setIdentity(final boolean identity) {
        this.identity = identity;
    }
    
    public boolean isBLOBColumn() {
        final String typeName = this.getJdbcTypeName();
        return "BINARY".equals(typeName) || "BLOB".equals(typeName) || "CLOB".equals(typeName) || "LONGNVARCHAR".equals(typeName) || "LONGVARBINARY".equals(typeName) || "LONGVARCHAR".equals(typeName) || "NCLOB".equals(typeName) || "VARBINARY".equals(typeName);
    }
    
    public boolean isStringColumn() {
        return this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getStringInstance());
    }
    
    public boolean isJdbcCharacterColumn() {
        return this.jdbcType == 1 || this.jdbcType == 2005 || this.jdbcType == -1 || this.jdbcType == 12 || this.jdbcType == -16 || this.jdbcType == -15 || this.jdbcType == 2011 || this.jdbcType == -9;
    }
    
    public String getJavaProperty() {
        return this.getJavaProperty(null);
    }
    
    public String getJavaProperty(final String prefix) {
        if (prefix == null) {
            return this.javaProperty;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(this.javaProperty);
        return sb.toString();
    }
    
    public void setJavaProperty(final String javaProperty) {
        this.javaProperty = javaProperty;
    }
    
    public boolean isJDBCDateColumn() {
        return this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getDateInstance()) && "DATE".equalsIgnoreCase(this.jdbcTypeName);
    }
    
    public boolean isJDBCTimeColumn() {
        return this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getDateInstance()) && "TIME".equalsIgnoreCase(this.jdbcTypeName);
    }
    
    public String getTypeHandler() {
        return this.typeHandler;
    }
    
    public void setTypeHandler(final String typeHandler) {
        this.typeHandler = typeHandler;
    }
    
    public String getActualColumnName() {
        return this.actualColumnName;
    }
    
    public void setColumnNameDelimited(final boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
    }
    
    public boolean isColumnNameDelimited() {
        return this.isColumnNameDelimited;
    }
    
    public String getJdbcTypeName() {
        if (this.jdbcTypeName == null) {
            return "OTHER";
        }
        return this.jdbcTypeName;
    }
    
    public void setJdbcTypeName(final String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }
    
    public FullyQualifiedJavaType getFullyQualifiedJavaType() {
        return this.fullyQualifiedJavaType;
    }
    
    public void setFullyQualifiedJavaType(final FullyQualifiedJavaType fullyQualifiedJavaType) {
        this.fullyQualifiedJavaType = fullyQualifiedJavaType;
    }
    
    public String getTableAlias() {
        return this.tableAlias;
    }
    
    public void setTableAlias(final String tableAlias) {
        this.tableAlias = tableAlias;
    }
    
    public Context getContext() {
        return this.context;
    }
    
    public void setContext(final Context context) {
        this.context = context;
    }
    
    public IntrospectedTable getIntrospectedTable() {
        return this.introspectedTable;
    }
    
    public void setIntrospectedTable(final IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }
    
    public Properties getProperties() {
        return this.properties;
    }
    
    public void setProperties(final Properties properties) {
        this.properties.putAll(properties);
    }
    
    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }
    
    public String getDefaultValue() {
        return this.defaultValue;
    }
    
    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public boolean isSequenceColumn() {
        return this.isSequenceColumn;
    }
    
    public void setSequenceColumn(final boolean isSequenceColumn) {
        this.isSequenceColumn = isSequenceColumn;
    }
    
    public boolean isAutoIncrement() {
        return this.isAutoIncrement;
    }
    
    public void setAutoIncrement(final boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }
    
    public boolean isGeneratedColumn() {
        return this.isGeneratedColumn;
    }
    
    public void setGeneratedColumn(final boolean isGeneratedColumn) {
        this.isGeneratedColumn = isGeneratedColumn;
    }
    
    public boolean isGeneratedAlways() {
        return this.isGeneratedAlways;
    }
    
    public void setGeneratedAlways(final boolean isGeneratedAlways) {
        this.isGeneratedAlways = isGeneratedAlways;
    }
}
