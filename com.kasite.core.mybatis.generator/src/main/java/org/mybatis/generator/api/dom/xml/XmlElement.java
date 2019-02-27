package org.mybatis.generator.api.dom.xml;

import org.mybatis.generator.api.dom.*;
import java.util.*;

public class XmlElement extends Element
{
    private List<Attribute> attributes;
    private List<Element> elements;
    private String name;
    
    public XmlElement(final String name) {
        this.attributes = new ArrayList<Attribute>();
        this.elements = new ArrayList<Element>();
        this.name = name;
    }
    
    public XmlElement(final XmlElement original) {
        (this.attributes = new ArrayList<Attribute>()).addAll(original.attributes);
        (this.elements = new ArrayList<Element>()).addAll(original.elements);
        this.name = original.name;
    }
    
    public List<Attribute> getAttributes() {
        return this.attributes;
    }
    
    public void addAttribute(final Attribute attribute) {
        this.attributes.add(attribute);
    }
    
    public List<Element> getElements() {
        return this.elements;
    }
    
    public void addElement(final Element element) {
        this.elements.add(element);
    }
    
    public void addElement(final int index, final Element element) {
        this.elements.add(index, element);
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getFormattedContent(final int indentLevel) {
        final StringBuilder sb = new StringBuilder();
        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append('<');
        sb.append(this.name);
        Collections.sort(this.attributes);
        for (final Attribute att : this.attributes) {
            sb.append(' ');
            sb.append(att.getFormattedContent());
        }
        if (this.elements.size() > 0) {
            sb.append(">");
            for (final Element element : this.elements) {
                OutputUtilities.newLine(sb);
                sb.append(element.getFormattedContent(indentLevel + 1));
            }
            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, indentLevel);
            sb.append("</");
            sb.append(this.name);
            sb.append('>');
        }
        else {
            sb.append(" />");
        }
        return sb.toString();
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
