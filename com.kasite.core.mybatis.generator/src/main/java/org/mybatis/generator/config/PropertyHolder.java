package org.mybatis.generator.config;

import org.mybatis.generator.api.dom.xml.*;
import java.util.*;

public abstract class PropertyHolder
{
    private Properties properties;
    
    public PropertyHolder() {
        this.properties = new Properties();
    }
    
    public void addProperty(final String name, final String value) {
        this.properties.setProperty(name, value);
    }
    
    public String getProperty(final String name) {
        return this.properties.getProperty(name);
    }
    
    public Properties getProperties() {
        return this.properties;
    }
    
    protected void addPropertyXmlElements(final XmlElement xmlElement) {
        final Enumeration<?> enumeration = this.properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            final String propertyName = (String)enumeration.nextElement();
            final XmlElement propertyElement = new XmlElement("property");
            propertyElement.addAttribute(new Attribute("name", propertyName));
            propertyElement.addAttribute(new Attribute("value", this.properties.getProperty(propertyName)));
            xmlElement.addElement(propertyElement);
        }
    }
}
