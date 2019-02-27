package org.mybatis.generator.config;

import java.util.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.dom.xml.*;

public class DomainObjectRenamingRule
{
    private String searchString;
    private String replaceString;
    
    public String getReplaceString() {
        return this.replaceString;
    }
    
    public void setReplaceString(final String replaceString) {
        this.replaceString = replaceString;
    }
    
    public String getSearchString() {
        return this.searchString;
    }
    
    public void setSearchString(final String searchString) {
        this.searchString = searchString;
    }
    
    public void validate(final List<String> errors, final String tableName) {
        if (!StringUtility.stringHasValue(this.searchString)) {
            errors.add(Messages.getString("ValidationError.28", tableName));
        }
    }
    
    public XmlElement toXmlElement() {
        final XmlElement xmlElement = new XmlElement("domainRenamingRule");
        xmlElement.addAttribute(new Attribute("searchString", this.searchString));
        if (this.replaceString != null) {
            xmlElement.addAttribute(new Attribute("replaceString", this.replaceString));
        }
        return xmlElement;
    }
}
