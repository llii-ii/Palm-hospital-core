package org.mybatis.generator.config.xml;

import org.xml.sax.*;
import java.io.*;

public class ParserEntityResolver implements EntityResolver
{
    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
        if ("-//Apache Software Foundation//DTD Apache iBATIS Ibator Configuration 1.0//EN".equalsIgnoreCase(publicId)) {
            final InputStream is = this.getClass().getClassLoader().getResourceAsStream("org/mybatis/generator/config/xml/ibator-config_1_0.dtd");
            final InputSource ins = new InputSource(is);
            return ins;
        }
        if ("-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN".equalsIgnoreCase(publicId)) {
            final InputStream is = this.getClass().getClassLoader().getResourceAsStream("org/mybatis/generator/config/xml/mybatis-generator-config_1_0.dtd");
            final InputSource ins = new InputSource(is);
            return ins;
        }
        return null;
    }
}
