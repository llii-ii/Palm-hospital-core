package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.*;
import java.util.*;

public abstract class JavaElement
{
    private List<String> javaDocLines;
    private JavaVisibility visibility;
    private boolean isStatic;
    private boolean isFinal;
    private List<String> annotations;
    
    public JavaElement() {
        this.visibility = JavaVisibility.DEFAULT;
        this.javaDocLines = new ArrayList<String>();
        this.annotations = new ArrayList<String>();
    }
    
    public JavaElement(final JavaElement original) {
        this();
        this.annotations.addAll(original.annotations);
        this.isFinal = original.isFinal;
        this.isStatic = original.isStatic;
        this.javaDocLines.addAll(original.javaDocLines);
        this.visibility = original.visibility;
    }
    
    public List<String> getJavaDocLines() {
        return this.javaDocLines;
    }
    
    public void addJavaDocLine(final String javaDocLine) {
        this.javaDocLines.add(javaDocLine);
    }
    
    public List<String> getAnnotations() {
        return this.annotations;
    }
    
    public void addAnnotation(final String annotation) {
        this.annotations.add(annotation);
    }
    
    public JavaVisibility getVisibility() {
        return this.visibility;
    }
    
    public void setVisibility(final JavaVisibility visibility) {
        this.visibility = visibility;
    }
    
    public void addSuppressTypeWarningsAnnotation() {
        this.addAnnotation("@SuppressWarnings(\"unchecked\")");
    }
    
    public void addFormattedJavadoc(final StringBuilder sb, final int indentLevel) {
        for (final String javaDocLine : this.javaDocLines) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(javaDocLine);
            OutputUtilities.newLine(sb);
        }
    }
    
    public void addFormattedAnnotations(final StringBuilder sb, final int indentLevel) {
        for (final String annotation : this.annotations) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(annotation);
            OutputUtilities.newLine(sb);
        }
    }
    
    public boolean isFinal() {
        return this.isFinal;
    }
    
    public void setFinal(final boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    public boolean isStatic() {
        return this.isStatic;
    }
    
    public void setStatic(final boolean isStatic) {
        this.isStatic = isStatic;
    }
}
