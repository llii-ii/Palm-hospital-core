package org.mybatis.generator.api.dom.java;

import java.util.*;

public class Parameter
{
    private String name;
    private FullyQualifiedJavaType type;
    private boolean isVarargs;
    private List<String> annotations;
    
    public Parameter(final FullyQualifiedJavaType type, final String name, final boolean isVarargs) {
        this.name = name;
        this.type = type;
        this.isVarargs = isVarargs;
        this.annotations = new ArrayList<String>();
    }
    
    public Parameter(final FullyQualifiedJavaType type, final String name) {
        this(type, name, false);
    }
    
    public Parameter(final FullyQualifiedJavaType type, final String name, final String annotation) {
        this(type, name, false);
        this.addAnnotation(annotation);
    }
    
    public Parameter(final FullyQualifiedJavaType type, final String name, final String annotation, final boolean isVarargs) {
        this(type, name, isVarargs);
        this.addAnnotation(annotation);
    }
    
    public String getName() {
        return this.name;
    }
    
    public FullyQualifiedJavaType getType() {
        return this.type;
    }
    
    public List<String> getAnnotations() {
        return this.annotations;
    }
    
    public void addAnnotation(final String annotation) {
        this.annotations.add(annotation);
    }
    
    public String getFormattedContent(final CompilationUnit compilationUnit) {
        final StringBuilder sb = new StringBuilder();
        for (final String annotation : this.annotations) {
            sb.append(annotation);
            sb.append(' ');
        }
        sb.append(JavaDomUtils.calculateTypeName(compilationUnit, this.type));
        sb.append(' ');
        if (this.isVarargs) {
            sb.append("... ");
        }
        sb.append(this.name);
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.getFormattedContent(null);
    }
    
    public boolean isVarargs() {
        return this.isVarargs;
    }
}
