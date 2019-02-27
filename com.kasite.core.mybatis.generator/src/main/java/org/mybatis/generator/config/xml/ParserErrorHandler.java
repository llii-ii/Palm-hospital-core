package org.mybatis.generator.config.xml;

import java.util.*;
import org.mybatis.generator.internal.util.messages.*;
import org.xml.sax.*;

public class ParserErrorHandler implements ErrorHandler
{
    private List<String> warnings;
    private List<String> errors;
    
    public ParserErrorHandler(final List<String> warnings, final List<String> errors) {
        this.warnings = warnings;
        this.errors = errors;
    }
    
    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        this.warnings.add(Messages.getString("Warning.7", Integer.toString(exception.getLineNumber()), exception.getMessage()));
    }
    
    @Override
    public void error(final SAXParseException exception) throws SAXException {
        this.errors.add(Messages.getString("RuntimeError.4", Integer.toString(exception.getLineNumber()), exception.getMessage()));
    }
    
    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        this.errors.add(Messages.getString("RuntimeError.4", Integer.toString(exception.getLineNumber()), exception.getMessage()));
    }
}
