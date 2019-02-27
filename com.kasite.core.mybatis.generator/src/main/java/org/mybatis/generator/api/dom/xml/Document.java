package org.mybatis.generator.api.dom.xml;

import org.mybatis.generator.api.dom.*;

public class Document
{
    private String publicId;
    private String systemId;
    private XmlElement rootElement;
    
    public Document(final String publicId, final String systemId) {
        this.publicId = publicId;
        this.systemId = systemId;
    }
    
    public Document() {
    }
    
    public XmlElement getRootElement() {
        return this.rootElement;
    }
    
    public void setRootElement(final XmlElement rootElement) {
        this.rootElement = rootElement;
    }
    
    public String getPublicId() {
        return this.publicId;
    }
    
    public String getSystemId() {
        return this.systemId;
    }
    
    public String getFormattedContent() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        if (this.publicId != null && this.systemId != null) {
            OutputUtilities.newLine(sb);
            sb.append("<!DOCTYPE ");
            sb.append(this.rootElement.getName());
            sb.append(" PUBLIC \"");
            sb.append(this.publicId);
            sb.append("\" \"");
            sb.append(this.systemId);
            sb.append("\">");
        }
        OutputUtilities.newLine(sb);
        sb.append(this.rootElement.getFormattedContent(0));
        return sb.toString();
    }
}
