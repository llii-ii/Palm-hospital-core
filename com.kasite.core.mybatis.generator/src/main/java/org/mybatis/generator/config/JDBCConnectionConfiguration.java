package org.mybatis.generator.config;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import org.mybatis.generator.internal.util.messages.*;

public class JDBCConnectionConfiguration extends PropertyHolder
{
    private String driverClass;
    private String connectionURL;
    private String userId;
    private String password;
    
    public String getConnectionURL() {
        return this.connectionURL;
    }
    
    public void setConnectionURL(final String connectionURL) {
        this.connectionURL = connectionURL;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    public String getDriverClass() {
        return this.driverClass;
    }
    
    public void setDriverClass(final String driverClass) {
        this.driverClass = driverClass;
    }
    
    public XmlElement toXmlElement() {
        final XmlElement xmlElement = new XmlElement("jdbcConnection");
        xmlElement.addAttribute(new Attribute("driverClass", this.driverClass));
        xmlElement.addAttribute(new Attribute("connectionURL", this.connectionURL));
        if (StringUtility.stringHasValue(this.userId)) {
            xmlElement.addAttribute(new Attribute("userId", this.userId));
        }
        if (StringUtility.stringHasValue(this.password)) {
            xmlElement.addAttribute(new Attribute("password", this.password));
        }
        this.addPropertyXmlElements(xmlElement);
        return xmlElement;
    }
    
    public void validate(final List<String> errors) {
        if (!StringUtility.stringHasValue(this.driverClass)) {
            errors.add(Messages.getString("ValidationError.4"));
        }
        if (!StringUtility.stringHasValue(this.connectionURL)) {
            errors.add(Messages.getString("ValidationError.5"));
        }
    }
}
