package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.*;
import java.util.*;

public class InnerEnum extends JavaElement
{
    private List<Field> fields;
    private List<InnerClass> innerClasses;
    private List<InnerEnum> innerEnums;
    private FullyQualifiedJavaType type;
    private Set<FullyQualifiedJavaType> superInterfaceTypes;
    private List<Method> methods;
    private List<String> enumConstants;
    
    public InnerEnum(final FullyQualifiedJavaType type) {
        this.type = type;
        this.fields = new ArrayList<Field>();
        this.innerClasses = new ArrayList<InnerClass>();
        this.innerEnums = new ArrayList<InnerEnum>();
        this.superInterfaceTypes = new HashSet<FullyQualifiedJavaType>();
        this.methods = new ArrayList<Method>();
        this.enumConstants = new ArrayList<String>();
    }
    
    public List<Field> getFields() {
        return this.fields;
    }
    
    public void addField(final Field field) {
        this.fields.add(field);
    }
    
    public List<InnerClass> getInnerClasses() {
        return this.innerClasses;
    }
    
    public void addInnerClass(final InnerClass innerClass) {
        this.innerClasses.add(innerClass);
    }
    
    public List<InnerEnum> getInnerEnums() {
        return this.innerEnums;
    }
    
    public void addInnerEnum(final InnerEnum innerEnum) {
        this.innerEnums.add(innerEnum);
    }
    
    public List<String> getEnumConstants() {
        return this.enumConstants;
    }
    
    public void addEnumConstant(final String enumConstant) {
        this.enumConstants.add(enumConstant);
    }
    
    public String getFormattedContent(int indentLevel, final CompilationUnit compilationUnit) {
        final StringBuilder sb = new StringBuilder();
        this.addFormattedJavadoc(sb, indentLevel);
        this.addFormattedAnnotations(sb, indentLevel);
        OutputUtilities.javaIndent(sb, indentLevel);
        if (this.getVisibility() == JavaVisibility.PUBLIC) {
            sb.append(this.getVisibility().getValue());
        }
        sb.append("enum ");
        sb.append(this.getType().getShortName());
        if (this.superInterfaceTypes.size() > 0) {
            sb.append(" implements ");
            boolean comma = false;
            for (final FullyQualifiedJavaType fqjt : this.superInterfaceTypes) {
                if (comma) {
                    sb.append(", ");
                }
                else {
                    comma = true;
                }
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, fqjt));
            }
        }
        sb.append(" {");
        ++indentLevel;
        final Iterator<String> strIter = this.enumConstants.iterator();
        while (strIter.hasNext()) {
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            final String enumConstant = strIter.next();
            sb.append(enumConstant);
            if (strIter.hasNext()) {
                sb.append(',');
            }
            else {
                sb.append(';');
            }
        }
        if (this.fields.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Iterator<Field> fldIter = this.fields.iterator();
        while (fldIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final Field field = fldIter.next();
            sb.append(field.getFormattedContent(indentLevel, compilationUnit));
            if (fldIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        if (this.methods.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Iterator<Method> mtdIter = this.methods.iterator();
        while (mtdIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final Method method = mtdIter.next();
            sb.append(method.getFormattedContent(indentLevel, false, compilationUnit));
            if (mtdIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        if (this.innerClasses.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Iterator<InnerClass> icIter = this.innerClasses.iterator();
        while (icIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final InnerClass innerClass = icIter.next();
            sb.append(innerClass.getFormattedContent(indentLevel, compilationUnit));
            if (icIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        if (this.innerEnums.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Iterator<InnerEnum> ieIter = this.innerEnums.iterator();
        while (ieIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final InnerEnum innerEnum = ieIter.next();
            sb.append(innerEnum.getFormattedContent(indentLevel, compilationUnit));
            if (ieIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        --indentLevel;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');
        return sb.toString();
    }
    
    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return this.superInterfaceTypes;
    }
    
    public void addSuperInterface(final FullyQualifiedJavaType superInterface) {
        this.superInterfaceTypes.add(superInterface);
    }
    
    public List<Method> getMethods() {
        return this.methods;
    }
    
    public void addMethod(final Method method) {
        this.methods.add(method);
    }
    
    public FullyQualifiedJavaType getType() {
        return this.type;
    }
}
