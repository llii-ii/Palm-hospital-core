package org.mybatis.generator.api.dom.xml;

import org.mybatis.generator.api.dom.*;

public class TextElement extends Element
{
    private String content;
    
    public TextElement(final String content) {
        this.content = content;
    }
    
    @Override
    public String getFormattedContent(final int indentLevel) {
        final StringBuilder sb = new StringBuilder();
        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append(this.content);
        return sb.toString();
    }
    
    public String getContent() {
        return this.content;
    }
}
