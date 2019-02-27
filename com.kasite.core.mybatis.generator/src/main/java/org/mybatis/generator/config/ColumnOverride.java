package org.mybatis.generator.config;

import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import org.mybatis.generator.internal.util.messages.*;

public class ColumnOverride extends PropertyHolder
{
    private String columnName;
    private String javaProperty;
    private String jdbcType;
    private String javaType;
    private String typeHandler;
    private boolean isColumnNameDelimited;
    private String configuredDelimitedColumnName;
    private boolean isGeneratedAlways;
    
    public ColumnOverride(final String columnName) {
        this.columnName = columnName;
        this.isColumnNameDelimited = StringUtility.stringContainsSpace(columnName);
    }
    
    public String getColumnName() {
        return this.columnName;
    }
    
    public String getJavaProperty() {
        return this.javaProperty;
    }
    
    public void setJavaProperty(final String javaProperty) {
        this.javaProperty = javaProperty;
    }
    
    public String getJavaType() {
        return this.javaType;
    }
    
    public void setJavaType(final String javaType) {
        this.javaType = javaType;
    }
    
    public String getJdbcType() {
        return this.jdbcType;
    }
    
    public void setJdbcType(final String jdbcType) {
        this.jdbcType = jdbcType;
    }
    
    public String getTypeHandler() {
        return this.typeHandler;
    }
    
    public void setTypeHandler(final String typeHandler) {
        this.typeHandler = typeHandler;
    }
    
    public XmlElement toXmlElement() {
        final XmlElement xmlElement = new XmlElement("columnOverride");
        xmlElement.addAttribute(new Attribute("column", this.columnName));
        if (StringUtility.stringHasValue(this.javaProperty)) {
            xmlElement.addAttribute(new Attribute("property", this.javaProperty));
        }
        if (StringUtility.stringHasValue(this.javaType)) {
            xmlElement.addAttribute(new Attribute("javaType", this.javaType));
        }
        if (StringUtility.stringHasValue(this.jdbcType)) {
            xmlElement.addAttribute(new Attribute("jdbcType", this.jdbcType));
        }
        if (StringUtility.stringHasValue(this.typeHandler)) {
            xmlElement.addAttribute(new Attribute("typeHandler", this.typeHandler));
        }
        if (StringUtility.stringHasValue(this.configuredDelimitedColumnName)) {
            xmlElement.addAttribute(new Attribute("delimitedColumnName", this.configuredDelimitedColumnName));
        }
        this.addPropertyXmlElements(xmlElement);
        return xmlElement;
    }
    
    public boolean isColumnNameDelimited() {
        return this.isColumnNameDelimited;
    }
    
    public void setColumnNameDelimited(final boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
        this.configuredDelimitedColumnName = (isColumnNameDelimited ? "true" : "false");
    }
    
    public void validate(final List<String> errors, final String tableName) {
        if (!StringUtility.stringHasValue(this.columnName)) {
            errors.add(Messages.getString("ValidationError.22", tableName));
        }
    }
    
    public boolean isGeneratedAlways() {
        return this.isGeneratedAlways;
    }
    
    public void setGeneratedAlways(final boolean isGeneratedAlways) {
        this.isGeneratedAlways = isGeneratedAlways;
    }
}
