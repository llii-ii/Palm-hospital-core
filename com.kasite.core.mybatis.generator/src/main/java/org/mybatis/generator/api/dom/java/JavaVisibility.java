package org.mybatis.generator.api.dom.java;

public enum JavaVisibility
{
    PUBLIC("public "), 
    PRIVATE("private "), 
    PROTECTED("protected "), 
    DEFAULT("");
    
    private String value;
    
    private JavaVisibility(final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
}
