package org.mybatis.generator.config;

import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import org.mybatis.generator.internal.util.messages.*;

public class IgnoredColumn
{
    protected String columnName;
    private boolean isColumnNameDelimited;
    protected String configuredDelimitedColumnName;
    
    public IgnoredColumn(final String columnName) {
        this.columnName = columnName;
        this.isColumnNameDelimited = StringUtility.stringContainsSpace(columnName);
    }
    
    public String getColumnName() {
        return this.columnName;
    }
    
    public boolean isColumnNameDelimited() {
        return this.isColumnNameDelimited;
    }
    
    public void setColumnNameDelimited(final boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
        this.configuredDelimitedColumnName = (isColumnNameDelimited ? "true" : "false");
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj instanceof IgnoredColumn && this.columnName.equals(((IgnoredColumn)obj).getColumnName());
    }
    
    @Override
    public int hashCode() {
        return this.columnName.hashCode();
    }
    
    public XmlElement toXmlElement() {
        final XmlElement xmlElement = new XmlElement("ignoreColumn");
        xmlElement.addAttribute(new Attribute("column", this.columnName));
        if (StringUtility.stringHasValue(this.configuredDelimitedColumnName)) {
            xmlElement.addAttribute(new Attribute("delimitedColumnName", this.configuredDelimitedColumnName));
        }
        return xmlElement;
    }
    
    public void validate(final List<String> errors, final String tableName) {
        if (!StringUtility.stringHasValue(this.columnName)) {
            errors.add(Messages.getString("ValidationError.21", tableName));
        }
    }
    
    public boolean matches(final String columnName) {
        if (this.isColumnNameDelimited) {
            return this.columnName.equals(columnName);
        }
        return this.columnName.equalsIgnoreCase(columnName);
    }
}
