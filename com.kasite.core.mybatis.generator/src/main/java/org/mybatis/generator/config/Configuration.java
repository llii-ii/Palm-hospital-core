package org.mybatis.generator.config;

import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.exception.*;
import java.util.*;
import org.mybatis.generator.api.dom.xml.*;

public class Configuration
{
    private List<Context> contexts;
    private List<String> classPathEntries;
    
    public Configuration() {
        this.contexts = new ArrayList<Context>();
        this.classPathEntries = new ArrayList<String>();
    }
    
    public void addClasspathEntry(final String entry) {
        this.classPathEntries.add(entry);
    }
    
    public List<String> getClassPathEntries() {
        return this.classPathEntries;
    }
    
    public void validate() throws InvalidConfigurationException {
        final List<String> errors = new ArrayList<String>();
        for (final String classPathEntry : this.classPathEntries) {
            if (!StringUtility.stringHasValue(classPathEntry)) {
                errors.add(Messages.getString("ValidationError.19"));
                break;
            }
        }
        if (this.contexts.size() == 0) {
            errors.add(Messages.getString("ValidationError.11"));
        }
        else {
            for (final Context context : this.contexts) {
                context.validate(errors);
            }
        }
        if (errors.size() > 0) {
            throw new InvalidConfigurationException(errors);
        }
    }
    
    public List<Context> getContexts() {
        return this.contexts;
    }
    
    public void addContext(final Context context) {
        this.contexts.add(context);
    }
    
    public Context getContext(final String id) {
        for (final Context context : this.contexts) {
            if (id.equals(context.getId())) {
                return context;
            }
        }
        return null;
    }
    
    public Document toDocument() {
        final Document document = new Document("-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN", "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd");
        final XmlElement rootElement = new XmlElement("generatorConfiguration");
        document.setRootElement(rootElement);
        for (final String classPathEntry : this.classPathEntries) {
            final XmlElement cpeElement = new XmlElement("classPathEntry");
            cpeElement.addAttribute(new Attribute("location", classPathEntry));
            rootElement.addElement(cpeElement);
        }
        for (final Context context : this.contexts) {
            rootElement.addElement(context.toXmlElement());
        }
        return document;
    }
}
