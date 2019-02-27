package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.*;
import java.util.*;

public class Method extends JavaElement
{
    private List<String> bodyLines;
    private boolean constructor;
    private FullyQualifiedJavaType returnType;
    private String name;
    private List<TypeParameter> typeParameters;
    private List<Parameter> parameters;
    private List<FullyQualifiedJavaType> exceptions;
    private boolean isSynchronized;
    private boolean isNative;
    private boolean isDefault;
    
    public Method() {
        this("bar");
    }
    
    public Method(final String name) {
        this.bodyLines = new ArrayList<String>();
        this.typeParameters = new ArrayList<TypeParameter>();
        this.parameters = new ArrayList<Parameter>();
        this.exceptions = new ArrayList<FullyQualifiedJavaType>();
        this.name = name;
    }
    
    public Method(final Method original) {
        super(original);
        this.bodyLines = new ArrayList<String>();
        this.typeParameters = new ArrayList<TypeParameter>();
        this.parameters = new ArrayList<Parameter>();
        this.exceptions = new ArrayList<FullyQualifiedJavaType>();
        this.bodyLines.addAll(original.bodyLines);
        this.constructor = original.constructor;
        this.exceptions.addAll(original.exceptions);
        this.name = original.name;
        this.typeParameters.addAll(original.typeParameters);
        this.parameters.addAll(original.parameters);
        this.returnType = original.returnType;
        this.isNative = original.isNative;
        this.isSynchronized = original.isSynchronized;
        this.isDefault = original.isDefault;
    }
    
    public List<String> getBodyLines() {
        return this.bodyLines;
    }
    
    public void addBodyLine(final String line) {
        this.bodyLines.add(line);
    }
    
    public void addBodyLine(final int index, final String line) {
        this.bodyLines.add(index, line);
    }
    
    public void addBodyLines(final Collection<String> lines) {
        this.bodyLines.addAll(lines);
    }
    
    public void addBodyLines(final int index, final Collection<String> lines) {
        this.bodyLines.addAll(index, lines);
    }
    
    public String getFormattedContent(int indentLevel, final boolean interfaceMethod, final CompilationUnit compilationUnit) {
        final StringBuilder sb = new StringBuilder();
        this.addFormattedJavadoc(sb, indentLevel);
        this.addFormattedAnnotations(sb, indentLevel);
        OutputUtilities.javaIndent(sb, indentLevel);
        if (interfaceMethod) {
            if (this.isStatic()) {
                sb.append("static ");
            }
            else if (this.isDefault()) {
                sb.append("default ");
            }
        }
        else {
            sb.append(this.getVisibility().getValue());
            if (this.isStatic()) {
                sb.append("static ");
            }
            if (this.isFinal()) {
                sb.append("final ");
            }
            if (this.isSynchronized()) {
                sb.append("synchronized ");
            }
            if (this.isNative()) {
                sb.append("native ");
            }
            else if (this.bodyLines.size() == 0) {
                sb.append("abstract ");
            }
        }
        if (!this.getTypeParameters().isEmpty()) {
            sb.append("<");
            boolean comma = false;
            for (final TypeParameter typeParameter : this.getTypeParameters()) {
                if (comma) {
                    sb.append(", ");
                }
                else {
                    comma = true;
                }
                sb.append(typeParameter.getFormattedContent(compilationUnit));
            }
            sb.append("> ");
        }
        if (!this.constructor) {
            if (this.getReturnType() == null) {
                sb.append("void");
            }
            else {
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, this.getReturnType()));
            }
            sb.append(' ');
        }
        sb.append(this.getName());
        sb.append('(');
        boolean comma = false;
        for (final Parameter parameter : this.getParameters()) {
            if (comma) {
                sb.append(", ");
            }
            else {
                comma = true;
            }
            sb.append(parameter.getFormattedContent(compilationUnit));
        }
        sb.append(')');
        if (this.getExceptions().size() > 0) {
            sb.append(" throws ");
            comma = false;
            for (final FullyQualifiedJavaType fqjt : this.getExceptions()) {
                if (comma) {
                    sb.append(", ");
                }
                else {
                    comma = true;
                }
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, fqjt));
            }
        }
        if (this.bodyLines.size() == 0 || this.isNative()) {
            sb.append(';');
        }
        else {
            sb.append(" {");
            ++indentLevel;
            final ListIterator<String> listIter = this.bodyLines.listIterator();
            while (listIter.hasNext()) {
                final String line = listIter.next();
                if (line.startsWith("}")) {
                    --indentLevel;
                }
                OutputUtilities.newLine(sb);
                OutputUtilities.javaIndent(sb, indentLevel);
                sb.append(line);
                if ((line.endsWith("{") && !line.startsWith("switch")) || line.endsWith(":")) {
                    ++indentLevel;
                }
                if (line.startsWith("break")) {
                    if (listIter.hasNext()) {
                        final String nextLine = listIter.next();
                        if (nextLine.startsWith("}")) {
                            ++indentLevel;
                        }
                        listIter.previous();
                    }
                    --indentLevel;
                }
            }
            --indentLevel;
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append('}');
        }
        return sb.toString();
    }
    
    public boolean isConstructor() {
        return this.constructor;
    }
    
    public void setConstructor(final boolean constructor) {
        this.constructor = constructor;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public List<TypeParameter> getTypeParameters() {
        return this.typeParameters;
    }
    
    public void addTypeParameter(final TypeParameter typeParameter) {
        this.typeParameters.add(typeParameter);
    }
    
    public void addTypeParameter(final int index, final TypeParameter typeParameter) {
        this.typeParameters.add(index, typeParameter);
    }
    
    public List<Parameter> getParameters() {
        return this.parameters;
    }
    
    public void addParameter(final Parameter parameter) {
        this.parameters.add(parameter);
    }
    
    public void addParameter(final int index, final Parameter parameter) {
        this.parameters.add(index, parameter);
    }
    
    public FullyQualifiedJavaType getReturnType() {
        return this.returnType;
    }
    
    public void setReturnType(final FullyQualifiedJavaType returnType) {
        this.returnType = returnType;
    }
    
    public List<FullyQualifiedJavaType> getExceptions() {
        return this.exceptions;
    }
    
    public void addException(final FullyQualifiedJavaType exception) {
        this.exceptions.add(exception);
    }
    
    public boolean isSynchronized() {
        return this.isSynchronized;
    }
    
    public void setSynchronized(final boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }
    
    public boolean isNative() {
        return this.isNative;
    }
    
    public void setNative(final boolean isNative) {
        this.isNative = isNative;
    }
    
    public boolean isDefault() {
        return this.isDefault;
    }
    
    public void setDefault(final boolean isDefault) {
        this.isDefault = isDefault;
    }
}
