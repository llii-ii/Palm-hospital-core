package org.mybatis.generator.config;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import org.mybatis.generator.internal.util.messages.*;

public class IgnoredColumnException extends IgnoredColumn
{
    public IgnoredColumnException(final String columnName) {
        super(columnName);
    }
    
    @Override
    public XmlElement toXmlElement() {
        final XmlElement xmlElement = new XmlElement("except");
        xmlElement.addAttribute(new Attribute("column", this.columnName));
        if (StringUtility.stringHasValue(this.configuredDelimitedColumnName)) {
            xmlElement.addAttribute(new Attribute("delimitedColumnName", this.configuredDelimitedColumnName));
        }
        return xmlElement;
    }
    
    @Override
    public void validate(final List<String> errors, final String tableName) {
        if (!StringUtility.stringHasValue(this.columnName)) {
            errors.add(Messages.getString("ValidationError.26", tableName));
        }
    }
}
