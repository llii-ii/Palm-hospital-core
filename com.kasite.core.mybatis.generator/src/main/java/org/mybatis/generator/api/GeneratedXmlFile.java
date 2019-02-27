package org.mybatis.generator.api;

import org.mybatis.generator.api.dom.xml.*;

public class GeneratedXmlFile extends GeneratedFile
{
    private Document document;
    private String fileName;
    private String targetPackage;
    private boolean isMergeable;
    private XmlFormatter xmlFormatter;
    
    public GeneratedXmlFile(final Document document, final String fileName, final String targetPackage, final String targetProject, final boolean isMergeable, final XmlFormatter xmlFormatter) {
        super(targetProject);
        this.document = document;
        this.fileName = fileName;
        this.targetPackage = targetPackage;
        this.isMergeable = isMergeable;
        this.xmlFormatter = xmlFormatter;
    }
    
    @Override
    public String getFormattedContent() {
        return this.xmlFormatter.getFormattedContent(this.document);
    }
    
    @Override
    public String getFileName() {
        return this.fileName;
    }
    
    @Override
    public String getTargetPackage() {
        return this.targetPackage;
    }
    
    @Override
    public boolean isMergeable() {
        return this.isMergeable;
    }
    
    public void setMergeable(final boolean isMergeable) {
        this.isMergeable = isMergeable;
    }
}
