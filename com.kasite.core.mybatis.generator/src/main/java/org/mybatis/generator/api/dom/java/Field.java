package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.*;

public class Field extends JavaElement
{
    private FullyQualifiedJavaType type;
    private String name;
    private String initializationString;
    private boolean isTransient;
    private boolean isVolatile;
    
    public Field() {
        this("foo", FullyQualifiedJavaType.getIntInstance());
    }
    
    public Field(final String name, final FullyQualifiedJavaType type) {
        this.name = name;
        this.type = type;
    }
    
    public Field(final Field field) {
        super(field);
        this.type = field.type;
        this.name = field.name;
        this.initializationString = field.initializationString;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public FullyQualifiedJavaType getType() {
        return this.type;
    }
    
    public void setType(final FullyQualifiedJavaType type) {
        this.type = type;
    }
    
    public String getInitializationString() {
        return this.initializationString;
    }
    
    public void setInitializationString(final String initializationString) {
        this.initializationString = initializationString;
    }
    
    public String getFormattedContent(final int indentLevel, final CompilationUnit compilationUnit) {
        final StringBuilder sb = new StringBuilder();
        this.addFormattedJavadoc(sb, indentLevel);
        this.addFormattedAnnotations(sb, indentLevel);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append(this.getVisibility().getValue());
        if (this.isStatic()) {
            sb.append("static ");
        }
        if (this.isFinal()) {
            sb.append("final ");
        }
        if (this.isTransient()) {
            sb.append("transient ");
        }
        if (this.isVolatile()) {
            sb.append("volatile ");
        }
        sb.append(JavaDomUtils.calculateTypeName(compilationUnit, this.type));
        sb.append(' ');
        sb.append(this.name);
        if (this.initializationString != null && this.initializationString.length() > 0) {
            sb.append(" = ");
            sb.append(this.initializationString);
        }
        sb.append(';');
        return sb.toString();
    }
    
    public boolean isTransient() {
        return this.isTransient;
    }
    
    public void setTransient(final boolean isTransient) {
        this.isTransient = isTransient;
    }
    
    public boolean isVolatile() {
        return this.isVolatile;
    }
    
    public void setVolatile(final boolean isVolatile) {
        this.isVolatile = isVolatile;
    }
}
