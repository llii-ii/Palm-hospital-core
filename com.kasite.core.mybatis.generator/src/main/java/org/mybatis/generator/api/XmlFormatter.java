package org.mybatis.generator.api;

import org.mybatis.generator.config.*;
import org.mybatis.generator.api.dom.xml.*;

public interface XmlFormatter
{
    void setContext(final Context p0);
    
    String getFormattedContent(final Document p0);
}
