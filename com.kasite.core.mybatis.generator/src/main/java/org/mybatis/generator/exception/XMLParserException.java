package org.mybatis.generator.exception;

import java.util.*;

public class XMLParserException extends Exception
{
    private static final long serialVersionUID = 5172525430401340573L;
    private List<String> errors;
    
    public XMLParserException(final List<String> errors) {
        this.errors = errors;
    }
    
    public XMLParserException(final String error) {
        super(error);
        (this.errors = new ArrayList<String>()).add(error);
    }
    
    public List<String> getErrors() {
        return this.errors;
    }
    
    @Override
    public String getMessage() {
        if (this.errors != null && this.errors.size() > 0) {
            return this.errors.get(0);
        }
        return super.getMessage();
    }
}
