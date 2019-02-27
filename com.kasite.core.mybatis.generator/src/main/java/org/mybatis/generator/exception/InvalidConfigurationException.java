package org.mybatis.generator.exception;

import java.util.*;

public class InvalidConfigurationException extends Exception
{
    static final long serialVersionUID = 4902307610148543411L;
    private List<String> errors;
    
    public InvalidConfigurationException(final List<String> errors) {
        this.errors = errors;
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
