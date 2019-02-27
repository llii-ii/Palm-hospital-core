package org.mybatis.generator.api.dom;

import org.mybatis.generator.api.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.api.dom.xml.*;

public class DefaultXmlFormatter implements XmlFormatter
{
    protected Context context;
    
    @Override
    public String getFormattedContent(final Document document) {
        return document.getFormattedContent();
    }
    
    @Override
    public void setContext(final Context context) {
        this.context = context;
    }
}
