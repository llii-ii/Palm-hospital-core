package org.mybatis.generator.config;

import java.util.regex.*;
import java.util.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;

public class IgnoredColumnPattern
{
    private String patternRegex;
    private Pattern pattern;
    private List<IgnoredColumnException> exceptions;
    
    public IgnoredColumnPattern(final String patternRegex) {
        this.exceptions = new ArrayList<IgnoredColumnException>();
        this.patternRegex = patternRegex;
        this.pattern = Pattern.compile(patternRegex);
    }
    
    public void addException(final IgnoredColumnException exception) {
        this.exceptions.add(exception);
    }
    
    public boolean matches(final String columnName) {
        boolean matches = this.pattern.matcher(columnName).matches();
        if (matches) {
            for (final IgnoredColumnException exception : this.exceptions) {
                if (exception.matches(columnName)) {
                    matches = false;
                    break;
                }
            }
        }
        return matches;
    }
    
    public XmlElement toXmlElement() {
        final XmlElement xmlElement = new XmlElement("ignoreColumnsByRegex");
        xmlElement.addAttribute(new Attribute("pattern", this.patternRegex));
        for (final IgnoredColumnException exception : this.exceptions) {
            xmlElement.addElement(exception.toXmlElement());
        }
        return xmlElement;
    }
    
    public void validate(final List<String> errors, final String tableName) {
        if (!StringUtility.stringHasValue(this.patternRegex)) {
            errors.add(Messages.getString("ValidationError.27", tableName));
        }
    }
}
