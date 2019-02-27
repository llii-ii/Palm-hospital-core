package org.mybatis.generator.internal;

import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.exception.*;
import java.io.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.util.*;
import org.mybatis.generator.config.*;
import org.w3c.dom.*;

public class XmlFileMergerJaxp
{
    public static String getMergedSource(final GeneratedXmlFile generatedXmlFile, final File existingFile) throws ShellException {
        try {
            return getMergedSource(new InputSource(new StringReader(generatedXmlFile.getFormattedContent())), new InputSource(new InputStreamReader(new FileInputStream(existingFile), "UTF-8")), existingFile.getName());
        }
        catch (IOException e) {
            throw new ShellException(Messages.getString("Warning.13", existingFile.getName()), e);
        }
        catch (SAXException e2) {
            throw new ShellException(Messages.getString("Warning.13", existingFile.getName()), e2);
        }
        catch (ParserConfigurationException e3) {
            throw new ShellException(Messages.getString("Warning.13", existingFile.getName()), e3);
        }
    }
    
    public static String getMergedSource(final InputSource newFile, final InputSource existingFile, final String existingFileName) throws IOException, SAXException, ParserConfigurationException, ShellException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setExpandEntityReferences(false);
        final DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(new NullEntityResolver());
        final Document existingDocument = builder.parse(existingFile);
        final Document newDocument = builder.parse(newFile);
        final DocumentType newDocType = newDocument.getDoctype();
        final DocumentType existingDocType = existingDocument.getDoctype();
        if (!newDocType.getName().equals(existingDocType.getName())) {
            throw new ShellException(Messages.getString("Warning.12", existingFileName));
        }
        final Element existingRootElement = existingDocument.getDocumentElement();
        final Element newRootElement = newDocument.getDocumentElement();
        NamedNodeMap attributes = existingRootElement.getAttributes();
        int attributeCount = attributes.getLength();
        for (int i = attributeCount - 1; i >= 0; --i) {
            final Node node = attributes.item(i);
            existingRootElement.removeAttribute(node.getNodeName());
        }
        attributes = newRootElement.getAttributes();
        attributeCount = attributes.getLength();
        for (int i = 0; i < attributeCount; ++i) {
            final Node node = attributes.item(i);
            existingRootElement.setAttribute(node.getNodeName(), node.getNodeValue());
        }
        final List<Node> nodesToDelete = new ArrayList<Node>();
        NodeList children = existingRootElement.getChildNodes();
        for (int length = children.getLength(), j = 0; j < length; ++j) {
            final Node node2 = children.item(j);
            if (isGeneratedNode(node2)) {
                nodesToDelete.add(node2);
            }
            else if (isWhiteSpace(node2) && isGeneratedNode(children.item(j + 1))) {
                nodesToDelete.add(node2);
            }
        }
        final Iterator<Node> iterator = nodesToDelete.iterator();
        while (iterator.hasNext()) {
            final Node node2 = iterator.next();
            existingRootElement.removeChild(node2);
        }
        children = newRootElement.getChildNodes();
        final int length = children.getLength();
        final Node firstChild = existingRootElement.getFirstChild();
        for (int k = 0; k < length; ++k) {
            final Node node3 = children.item(k);
            if (k == length - 1 && isWhiteSpace(node3)) {
                break;
            }
            final Node newNode = existingDocument.importNode(node3, true);
            if (firstChild == null) {
                existingRootElement.appendChild(newNode);
            }
            else {
                existingRootElement.insertBefore(newNode, firstChild);
            }
        }
        return prettyPrint(existingDocument);
    }
    
    private static String prettyPrint(final Document document) throws ShellException {
        final DomWriter dw = new DomWriter();
        final String s = dw.toString(document);
        return s;
    }
    
    private static boolean isGeneratedNode(final Node node) {
        boolean rc = false;
        if (node != null && node.getNodeType() == 1) {
            final Element element = (Element)node;
            final String id = element.getAttribute("id");
            if (id != null) {
                for (final String prefix : MergeConstants.OLD_XML_ELEMENT_PREFIXES) {
                    if (id.startsWith(prefix)) {
                        rc = true;
                        break;
                    }
                }
            }
            if (!rc) {
                final NodeList children = node.getChildNodes();
                for (int length = children.getLength(), i = 0; i < length; ++i) {
                    final Node childNode = children.item(i);
                    if (!isWhiteSpace(childNode)) {
                        if (childNode.getNodeType() != 8) {
                            break;
                        }
                        final Comment comment = (Comment)childNode;
                        final String commentData = comment.getData();
                        for (final String tag : MergeConstants.OLD_ELEMENT_TAGS) {
                            if (commentData.contains(tag)) {
                                rc = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return rc;
    }
    
    private static boolean isWhiteSpace(final Node node) {
        boolean rc = false;
        if (node != null && node.getNodeType() == 3) {
            final Text tn = (Text)node;
            if (tn.getData().trim().length() == 0) {
                rc = true;
            }
        }
        return rc;
    }
    
    private static class NullEntityResolver implements EntityResolver
    {
        @Override
        public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
            final StringReader sr = new StringReader("");
            return new InputSource(sr);
        }
    }
}
