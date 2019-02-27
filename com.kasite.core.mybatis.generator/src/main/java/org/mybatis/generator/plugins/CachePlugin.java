package org.mybatis.generator.plugins;

import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.xml.*;

public class CachePlugin extends PluginAdapter
{
    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean sqlMapDocumentGenerated(final Document document, final IntrospectedTable introspectedTable) {
        final XmlElement element = new XmlElement("cache");
        this.context.getCommentGenerator().addComment(element);
        for (final CacheProperty cacheProperty : CacheProperty.values()) {
            this.addAttributeIfExists(element, introspectedTable, cacheProperty);
        }
        document.getRootElement().addElement(element);
        return true;
    }
    
    private void addAttributeIfExists(final XmlElement element, final IntrospectedTable introspectedTable, final CacheProperty cacheProperty) {
        String property = introspectedTable.getTableConfigurationProperty(cacheProperty.getPropertyName());
        if (property == null) {
            property = this.properties.getProperty(cacheProperty.getPropertyName());
        }
        if (StringUtility.stringHasValue(property)) {
            element.addAttribute(new Attribute(cacheProperty.getAttributeName(), property));
        }
    }
    
    public enum CacheProperty
    {
        EVICTION("cache_eviction", "eviction"), 
        FLUSH_INTERVAL("cache_flushInterval", "flushInterval"), 
        READ_ONLY("cache_readOnly", "readOnly"), 
        SIZE("cache_size", "size"), 
        TYPE("cache_type", "type");
        
        private String propertyName;
        private String attributeName;
        
        private CacheProperty(final String propertyName, final String attributeName) {
            this.propertyName = propertyName;
            this.attributeName = attributeName;
        }
        
        public String getPropertyName() {
            return this.propertyName;
        }
        
        public String getAttributeName() {
            return this.attributeName;
        }
    }
}
