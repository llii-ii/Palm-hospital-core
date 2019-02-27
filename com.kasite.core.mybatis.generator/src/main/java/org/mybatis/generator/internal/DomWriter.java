package org.mybatis.generator.internal;

import java.io.*;
import org.mybatis.generator.exception.*;
import org.w3c.dom.*;
import org.mybatis.generator.internal.util.messages.*;

public class DomWriter
{
    protected PrintWriter printWriter;
    protected boolean isXML11;
    
    public synchronized String toString(final Document document) throws ShellException {
        final StringWriter sw = new StringWriter();
        this.printWriter = new PrintWriter(sw);
        this.write(document);
        final String s = sw.toString();
        return s;
    }
    
    protected Attr[] sortAttributes(final NamedNodeMap attrs) {
        final int len = (attrs != null) ? attrs.getLength() : 0;
        final Attr[] array = new Attr[len];
        for (int i = 0; i < len; ++i) {
            array[i] = (Attr)attrs.item(i);
        }
        for (int i = 0; i < len - 1; ++i) {
            String name = array[i].getNodeName();
            int index = i;
            for (int j = i + 1; j < len; ++j) {
                final String curName = array[j].getNodeName();
                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }
            if (index != i) {
                final Attr temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }
        return array;
    }
    
    protected void normalizeAndPrint(final String s, final boolean isAttValue) {
        for (int len = (s != null) ? s.length() : 0, i = 0; i < len; ++i) {
            final char c = s.charAt(i);
            this.normalizeAndPrint(c, isAttValue);
        }
    }
    
    protected void normalizeAndPrint(final char c, final boolean isAttValue) {
        switch (c) {
            case '<': {
                this.printWriter.print("&lt;");
                break;
            }
            case '>': {
                this.printWriter.print("&gt;");
                break;
            }
            case '&': {
                this.printWriter.print("&amp;");
                break;
            }
            case '\"': {
                if (isAttValue) {
                    this.printWriter.print("&quot;");
                    break;
                }
                this.printWriter.print('\"');
                break;
            }
            case '\r': {
                this.printWriter.print("&#xD;");
                break;
            }
            case '\n': {
                this.printWriter.print(System.getProperty("line.separator"));
                break;
            }
            default: {
                if ((this.isXML11 && ((c >= '\u0001' && c <= '\u001f' && c != '\t' && c != '\n') || (c >= '\u007f' && c <= '\u009f') || c == '\u2028')) || (isAttValue && (c == '\t' || c == '\n'))) {
                    this.printWriter.print("&#x");
                    this.printWriter.print(Integer.toHexString(c).toUpperCase());
                    this.printWriter.print(';');
                    break;
                }
                this.printWriter.print(c);
                break;
            }
        }
    }
    
    protected String getVersion(final Document document) {
        if (document == null) {
            return null;
        }
        return document.getXmlVersion();
    }
    
    protected void writeAnyNode(final Node node) throws ShellException {
        if (node == null) {
            return;
        }
        final short type = node.getNodeType();
        switch (type) {
            case 9: {
                this.write((Document)node);
                break;
            }
            case 10: {
                this.write((DocumentType)node);
                break;
            }
            case 1: {
                this.write((Element)node);
                break;
            }
            case 5: {
                this.write((EntityReference)node);
                break;
            }
            case 4: {
                this.write((CDATASection)node);
                break;
            }
            case 3: {
                this.write((Text)node);
                break;
            }
            case 7: {
                this.write((ProcessingInstruction)node);
                break;
            }
            case 8: {
                this.write((Comment)node);
                break;
            }
            default: {
                throw new ShellException(Messages.getString("RuntimeError.18", Short.toString(type)));
            }
        }
    }
    
    protected void write(final Document node) throws ShellException {
        this.isXML11 = "1.1".equals(this.getVersion(node));
        if (this.isXML11) {
            this.printWriter.println("<?xml version=\"1.1\" encoding=\"UTF-8\"?>");
        }
        else {
            this.printWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        }
        this.printWriter.flush();
        this.write(node.getDoctype());
        this.write(node.getDocumentElement());
    }
    
    protected void write(final DocumentType node) throws ShellException {
        this.printWriter.print("<!DOCTYPE ");
        this.printWriter.print(node.getName());
        final String publicId = node.getPublicId();
        final String systemId = node.getSystemId();
        if (publicId != null) {
            this.printWriter.print(" PUBLIC \"");
            this.printWriter.print(publicId);
            this.printWriter.print("\" \"");
            this.printWriter.print(systemId);
            this.printWriter.print('\"');
        }
        else if (systemId != null) {
            this.printWriter.print(" SYSTEM \"");
            this.printWriter.print(systemId);
            this.printWriter.print('\"');
        }
        final String internalSubset = node.getInternalSubset();
        if (internalSubset != null) {
            this.printWriter.println(" [");
            this.printWriter.print(internalSubset);
            this.printWriter.print(']');
        }
        this.printWriter.println('>');
    }
    
    protected void write(final Element node) throws ShellException {
        this.printWriter.print('<');
        this.printWriter.print(node.getNodeName());
        final Attr[] sortAttributes;
        final Attr[] attrs = sortAttributes = this.sortAttributes(node.getAttributes());
        for (final Attr attr : sortAttributes) {
            this.printWriter.print(' ');
            this.printWriter.print(attr.getNodeName());
            this.printWriter.print("=\"");
            this.normalizeAndPrint(attr.getNodeValue(), true);
            this.printWriter.print('\"');
        }
        if (node.getChildNodes().getLength() == 0) {
            this.printWriter.print(" />");
            this.printWriter.flush();
        }
        else {
            this.printWriter.print('>');
            this.printWriter.flush();
            for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                this.writeAnyNode(child);
            }
            this.printWriter.print("</");
            this.printWriter.print(node.getNodeName());
            this.printWriter.print('>');
            this.printWriter.flush();
        }
    }
    
    protected void write(final EntityReference node) {
        this.printWriter.print('&');
        this.printWriter.print(node.getNodeName());
        this.printWriter.print(';');
        this.printWriter.flush();
    }
    
    protected void write(final CDATASection node) {
        this.printWriter.print("<![CDATA[");
        final String data = node.getNodeValue();
        for (int len = (data != null) ? data.length() : 0, i = 0; i < len; ++i) {
            final char c = data.charAt(i);
            if (c == '\n') {
                this.printWriter.print(System.getProperty("line.separator"));
            }
            else {
                this.printWriter.print(c);
            }
        }
        this.printWriter.print("]]>");
        this.printWriter.flush();
    }
    
    protected void write(final Text node) {
        this.normalizeAndPrint(node.getNodeValue(), false);
        this.printWriter.flush();
    }
    
    protected void write(final ProcessingInstruction node) {
        this.printWriter.print("<?");
        this.printWriter.print(node.getNodeName());
        final String data = node.getNodeValue();
        if (data != null && data.length() > 0) {
            this.printWriter.print(' ');
            this.printWriter.print(data);
        }
        this.printWriter.print("?>");
        this.printWriter.flush();
    }
    
    protected void write(final Comment node) {
        this.printWriter.print("<!--");
        final String comment = node.getNodeValue();
        if (comment != null && comment.length() > 0) {
            this.normalizeAndPrint(comment, false);
        }
        this.printWriter.print("-->");
        this.printWriter.flush();
    }
}
