package org.mybatis.generator.config;

import java.util.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.dom.xml.*;

public class ConnectionFactoryConfiguration extends TypedPropertyHolder
{
    public void validate(final List<String> errors) {
        if (this.getConfigurationType() == null || "DEFAULT".equals(this.getConfigurationType())) {
            if (!StringUtility.stringHasValue(this.getProperty("driverClass"))) {
                errors.add(Messages.getString("ValidationError.18", "connectionFactory", "driverClass"));
            }
            if (!StringUtility.stringHasValue(this.getProperty("connectionURL"))) {
                errors.add(Messages.getString("ValidationError.18", "connectionFactory", "connectionURL"));
            }
        }
    }
    
    public XmlElement toXmlElement() {
        final XmlElement xmlElement = new XmlElement("connectionFactory");
        if (StringUtility.stringHasValue(this.getConfigurationType())) {
            xmlElement.addAttribute(new Attribute("type", this.getConfigurationType()));
        }
        this.addPropertyXmlElements(xmlElement);
        return xmlElement;
    }
}
