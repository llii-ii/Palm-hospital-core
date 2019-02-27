package org.mybatis.generator.config.xml;

import java.util.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.*;
import java.io.*;
import org.xml.sax.*;
import org.mybatis.generator.internal.util.messages.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class ConfigurationParser
{
    private List<String> warnings;
    private List<String> parseErrors;
    private Properties extraProperties;
    
    public ConfigurationParser(final List<String> warnings) {
        this(null, warnings);
    }
    
    public ConfigurationParser(final Properties extraProperties, final List<String> warnings) {
        this.extraProperties = extraProperties;
        if (warnings == null) {
            this.warnings = new ArrayList<String>();
        }
        else {
            this.warnings = warnings;
        }
        this.parseErrors = new ArrayList<String>();
    }
    
    public Configuration parseConfiguration(final File inputFile) throws IOException, XMLParserException {
        final FileReader fr = new FileReader(inputFile);
        return this.parseConfiguration(fr);
    }
    
    public Configuration parseConfiguration(final Reader reader) throws IOException, XMLParserException {
        final InputSource is = new InputSource(reader);
        return this.parseConfiguration(is);
    }
    
    public Configuration parseConfiguration(final InputStream inputStream) throws IOException, XMLParserException {
        final InputSource is = new InputSource(inputStream);
        return this.parseConfiguration(is);
    }
    
    private Configuration parseConfiguration(final InputSource inputSource) throws IOException, XMLParserException {
        this.parseErrors.clear();
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new ParserEntityResolver());
            final ParserErrorHandler handler = new ParserErrorHandler(this.warnings, this.parseErrors);
            builder.setErrorHandler(handler);
            Document document = null;
            try {
                document = builder.parse(inputSource);
            }
            catch (SAXParseException e3) {
                throw new XMLParserException(this.parseErrors);
            }
            catch (SAXException e) {
                if (e.getException() == null) {
                    this.parseErrors.add(e.getMessage());
                }
                else {
                    this.parseErrors.add(e.getException().getMessage());
                }
            }
            if (this.parseErrors.size() > 0) {
                throw new XMLParserException(this.parseErrors);
            }
            final Element rootNode = document.getDocumentElement();
            final DocumentType docType = document.getDoctype();
            Configuration config;
            if (rootNode.getNodeType() == 1 && docType.getPublicId().equals("-//Apache Software Foundation//DTD Apache iBATIS Ibator Configuration 1.0//EN")) {
                config = this.parseIbatorConfiguration(rootNode);
            }
            else {
                if (rootNode.getNodeType() != 1 || !docType.getPublicId().equals("-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN")) {
                    throw new XMLParserException(Messages.getString("RuntimeError.5"));
                }
                config = this.parseMyBatisGeneratorConfiguration(rootNode);
            }
            if (this.parseErrors.size() > 0) {
                throw new XMLParserException(this.parseErrors);
            }
            return config;
        }
        catch (ParserConfigurationException e2) {
            this.parseErrors.add(e2.getMessage());
            throw new XMLParserException(this.parseErrors);
        }
    }
    
    private Configuration parseIbatorConfiguration(final Element rootNode) throws XMLParserException {
        final IbatorConfigurationParser parser = new IbatorConfigurationParser(this.extraProperties);
        return parser.parseIbatorConfiguration(rootNode);
    }
    
    private Configuration parseMyBatisGeneratorConfiguration(final Element rootNode) throws XMLParserException {
        final MyBatisGeneratorConfigurationParser parser = new MyBatisGeneratorConfigurationParser(this.extraProperties);
        return parser.parseConfiguration(rootNode);
    }
}
